package com.redhat.demos.datagrid_demo_app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.infinispan.client.hotrod.Flag;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

import com.redhat.demos.datagrid_demo_app.model.Member;
import com.redhat.demos.datagrid_demo_app.model.SearchRequest;
import com.redhat.demos.datagrid_demo_app.model.SearchResult;
import com.redhat.demos.datagrid_demo_app.util.Resources;

public class MemberController {

	private static final Logger log = Logger.getLogger("MemberSearch");

	private RemoteCache<Integer, Member> cache;

	public MemberController() {
		try {
			cache = Resources.getInstance().getCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void loadBulkData() {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 600; i++) {
			Runnable worker = new MemberInserter(i, cache);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {

		}
		System.out.println("Finished all threads");
	}

	public int getCacheSize(){
		return cache.size();
	}
	
	public SearchResult search(SearchRequest request) {
		System.out.println("Request: " + request);
		if (request.getId() != null) {
			return searchById(request);
		} else {
			return searchByName(request);
		}
	}

	public SearchResult searchById(SearchRequest request) {
		SearchResult result = new SearchResult();
		try {
			int id = Integer.parseInt(request.getId());
			Member member = cache.get(id);
			List<Member> members = new ArrayList<Member>();
			members.add(member);
			result.setMembers(members);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public SearchResult searchByName(SearchRequest request) {
		String namePattern = request.getName() + "%";
		System.out.println("Searching: " + namePattern);
		QueryFactory qf = Search.getQueryFactory(cache);
		Query query = qf.from(Member.class).having("name").like(namePattern)
				.toBuilder().maxResults(50).build();

		List<Member> results = query.list();
		System.out.println("Found " + results.size() + " matches:");
		SearchResult result = new SearchResult();
		result.setMembers(results);
		return result;
	}

	public SearchResult queryId(SearchRequest request) {
		int id = Integer.parseInt(request.getId());
		Member member = cache.get(id);
		List<Member> members = new ArrayList<Member>();
		members.add(member);
		System.out.println("Member found: " + member);
		SearchResult result = new SearchResult();
		result.setMembers(members);
		return result;
	}

	public void addMember(Member member) {
		cache.put(member.getId(), member);
	}

	public void updateMember(Member member) {
		cache.replace(member.getId(), member);
	}

	public void removeMember(Member member) {
		int id = member.getId();
		Member prevValue = cache.withFlags(Flag.FORCE_RETURN_VALUE).remove(id);
		log.info("Removed: " + prevValue);
	}

	public void printAll() {
		for (int id : cache.keySet()) {
			Member person = cache.get(id);
			log.info(person.toString());
		}
	}
}
