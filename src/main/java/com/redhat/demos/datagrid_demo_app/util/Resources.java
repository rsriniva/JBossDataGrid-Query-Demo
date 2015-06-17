/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.demos.datagrid_demo_app.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Properties;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;

import com.redhat.demos.datagrid_demo_app.model.Member;
import com.redhat.demos.datagrid_demo_app.service.MemberMarshaller;

public class Resources {

	private static final String SERVER_HOST = "jdg.host";
	private static final String HOTROD_PORT = "jdg.hotrod.port";
	private static final String CACHE_NAME = "jdg.cache";
	private static final String PROPERTIES_FILE = "jdg.properties";

	private static final String PROTOBUF_DEFINITION_RESOURCE = "/Member.proto";

	private static Resources instance;

	private RemoteCacheManager cacheManager;
	private RemoteCache<Integer, Member> cache;

	private Resources() throws Exception {
		final String host = jdgProperty(SERVER_HOST);
		final int hotrodPort = Integer.parseInt(jdgProperty(HOTROD_PORT));
		final String cacheName = jdgProperty(CACHE_NAME);

		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.addServer().host(host).port(hotrodPort).marshaller(new ProtoStreamMarshaller());
		cacheManager = new RemoteCacheManager(builder.build());
		cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			throw new RuntimeException(
					"Cache '" + cacheName + "' not found. Please make sure the server is properly configured");
		}
		registerSchemasAndMarshallers();
	}

	public static Resources getInstance() throws Exception {
		if (instance == null) {
			instance = new Resources();
		}
		return instance;
	}

	public void stop() {
		cacheManager.stop();
	}

	public RemoteCacheManager getCacheManager() {
		return cacheManager;
	}

	public RemoteCache<Integer, Member> getCache() {
		return cache;
	}

	/**
	 * Register the Protobuf schemas and marshallers with the client and then
	 * register the schemas with the server too.
	 */
	private void registerSchemasAndMarshallers() throws IOException {
		SerializationContext ctx = ProtoStreamMarshaller.getSerializationContext(cacheManager);
		ctx.registerProtoFiles(FileDescriptorSource.fromResources(PROTOBUF_DEFINITION_RESOURCE));
		ctx.registerMarshaller(new MemberMarshaller());

		RemoteCache<String, String> metadataCache = cacheManager.getCache(ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME);
		metadataCache.put(PROTOBUF_DEFINITION_RESOURCE,readResource(PROTOBUF_DEFINITION_RESOURCE));
		String errors = metadataCache.get(ProtobufMetadataManagerConstants.ERRORS_KEY_SUFFIX);
		if (errors != null) {
			throw new IllegalStateException("Some Protobuf schema files contain errors:\n" + errors);
		}
	}

	private String jdgProperty(String name) {
		InputStream res = null;
		try {
			res = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
			Properties props = new Properties();
			props.load(res);
			return props.getProperty(name);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		} finally {
			if (res != null) {
				try {
					res.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	private String readResource(String resourcePath) throws IOException {
		InputStream is = getClass().getResourceAsStream(resourcePath);
		try {
			final Reader reader = new InputStreamReader(is, "UTF-8");
			StringWriter writer = new StringWriter();
			char[] buf = new char[1024];
			int len;
			while ((len = reader.read(buf)) != -1) {
				writer.write(buf, 0, len);
			}
			return writer.toString();
		} finally {
			is.close();
		}
	}
}
