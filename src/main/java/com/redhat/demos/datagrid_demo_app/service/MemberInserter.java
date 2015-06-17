package com.redhat.demos.datagrid_demo_app.service;

import org.infinispan.client.hotrod.RemoteCache;

import com.redhat.demos.datagrid_demo_app.model.Member;

public class MemberInserter implements Runnable {

	public static final int INSERTS_NUMBER = 1000;
	
	
	private int id;
	private RemoteCache<Integer, Member> cache;
	

	public MemberInserter(int id, RemoteCache<Integer, Member> cache) {
		this.id = id;
		this.cache = cache;
	}

	@Override
	public void run() {
		int offset=id*INSERTS_NUMBER;
		int limit = offset+INSERTS_NUMBER;
		for (int o = offset; o < limit; o++) {
			System.out.println("Insertando member: " + o);
			Member member = new Member();
			member.setId(o);
			member.setAge(o);
			member.setLastName("LastName" + o);
			member.setLocation("Location" + o);
			member.setMiddleName("MiddleName" + o);
			member.setName("Name" + o);
			member.setStatus("Created");
			member.setSupervisor("My Supervisor " + o);
			addMember(member);
		}
	}

	private void addMember(Member member) {
		cache.put(member.getId(), member);
	}

}
