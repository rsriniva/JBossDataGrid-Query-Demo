/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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
package com.redhat.demos.datagrid_demo_app.service;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;

import com.redhat.demos.datagrid_demo_app.model.Member;

public class MemberMarshaller implements MessageMarshaller<Member> {

   @Override
   public String getTypeName() {
      return "com.redhat.demos.datagrid_demo_app.model.Member";
   }

   @Override
   public Class<? extends Member> getJavaClass() {
      return Member.class;
   }

   @Override
   public Member readFrom(ProtoStreamReader reader) throws IOException {
      String name = reader.readString("name");
      int id = reader.readInt("id");
      String middleName = reader.readString("middleName");
      String lastName = reader.readString("lastName");
      Member person = new Member();
      person.setName(name);
      person.setId(id);
      person.setMiddleName(middleName);
      person.setLastName(lastName);
      return person;
   }

   @Override
   public void writeTo(ProtoStreamWriter writer, Member person) throws IOException {
      writer.writeString("name", person.getName());
      writer.writeInt("id", person.getId());
      writer.writeString("middleName", person.getMiddleName());
      writer.writeString("lastName", person.getLastName());
   }
}
