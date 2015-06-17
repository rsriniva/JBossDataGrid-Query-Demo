package com.redhat.demos.datagrid_demo_app.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.demos.datagrid_demo_app.model.Member;
import com.redhat.demos.datagrid_demo_app.model.SearchRequest;
import com.redhat.demos.datagrid_demo_app.model.SearchResult;
import com.redhat.demos.datagrid_demo_app.service.MemberController;

@Path("/members")
@RequestScoped
public class MembersRESTService {

	private MemberController controller = new MemberController();

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public SearchResult findAll() {
		SearchRequest req = new SearchRequest();
		req.setName("");
		return controller.search(req);
	}

	@GET
	@Path("search/{query}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public SearchResult findByName(@PathParam("query") String query) {
		SearchRequest req = new SearchRequest();
		req.setName(query);
		return controller.search(req);
	}
	
	@GET
	@Path("count")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public int findByName() {
		return controller.getCacheSize();
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public SearchResult findById(@PathParam("id") String id) {
		SearchRequest request = new SearchRequest();
		request.setId(id);
		return controller.searchById(request);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Member create(Member member) {
		controller.addMember(member);
		return member;
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Member update(Member member) {
		controller.updateMember(member);
		return member;
	}

	@GET
	@Path("load")
	public void createMembers() {
		controller.loadBulkData();
	}

	@DELETE
	@Path("{id}")
	public void remove(@PathParam("id") int id) {
		Member member = new Member();
		member.setId(id);
		controller.removeMember(member);
	}
}
