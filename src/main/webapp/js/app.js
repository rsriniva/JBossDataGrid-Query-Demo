
var rootURL = 'http://localhost:8080/datagrid-demo-app/rest/members';


function buildMemberRows(members) {
	return _.template($("#member-tmpl").html(), {
		"members" : members
	});
}

function renderList(data) {
	$("#members").empty().append(buildMemberRows(data.members));
	$("#member-table").table("refresh");
}

function celanList() {
	$("#members").empty();
	$("#member-table").table("refresh");
}


function executeLoad(){
	$("body").addClass('ui-disabled');
	$.mobile.loading("show",{
		text: "Cargando",
		textVisible: true
		});
	$.ajax({
		url : "rest/members/load", 
		cache : false,
		success : function(data) {
			MsgPop.open({
          	  Type:  "success",
          	  Content:"Datos cargados a Data Grid!"});
		},
		error : function(error) {
			MsgPop.open({
	          	  Type:  "error",
	          	  Content:"Existe un problema al cargar los datos!"});
		},
		complete : function() {
			$.mobile.loading("hide");
			$("body").removeClass('ui-disabled');
		}
	});
}

function findAll() {
	$("body").addClass('ui-disabled');
	$.mobile.loading("show",{
		text: "Cargando",
		textVisible: true
		});
	$.ajax({
        type: 'GET',
        url: rootURL,
        dataType: "json", 
        success: function(data){
        	renderList(data);
        },
		error : function(error) {
			MsgPop.open({
	          	  Type:  "error",
	          	  Content:"Existe un problema al cargar los datos!"});
		},
		complete : function() {
			$.mobile.loading("hide");
			$("body").removeClass('ui-disabled');
		}
    });
}
 
function findByName(searchKey) {
    $.ajax({
        type: 'GET',
        url: rootURL + '/search/' + searchKey,
        dataType: "json",
        success: function(data){
        	renderList(data);
        },
		error : function(error) {
			MsgPop.open({
	          	  Type:  "error",
	          	  Content:"Existe un problema al cargar los datos!"});
		},
		complete : function() {
			
		}
    });
}

function countMembers() {
	$("body").addClass('ui-disabled');
	$.mobile.loading("show",{
		text: "Cargando",
		textVisible: true
		});
    $.ajax({
        type: 'GET',
        url: rootURL + '/count',
        dataType: "json",
        success: function(data){
        	MsgPop.open({
	          	  Type:  "success",
	          	  Content:'Se encontraron '+data+' registros'});
        },
		error : function(error) {
			MsgPop.open({
	          	  Type:  "error",
	          	  Content:"Existe un problema al cargar los datos!"});
		},
		complete : function() {
			$.mobile.loading("hide");
			$("body").removeClass('ui-disabled');
		}
    });
}
 
function findById(id) {
	$("body").addClass('ui-disabled');
	$.mobile.loading("show",{
		text: "Cargando",
		textVisible: true
		});
    $.ajax({
        type: 'GET',
        url: rootURL + '/' + id,
        dataType: "json",
        success: function(data){
            $('#btnDelete').show();
            renderDetails(data);
        },
        error: function(jqXHR, textStatus, errorThrown){
        	MsgPop.open({
	          	  Type:  "error",
	          	  Content:"Error al buscar por ID!"});
        },
		complete : function() {
			$.mobile.loading("hide");
			$("body").removeClass('ui-disabled');
		}
    });
}
 
function addMember() {
	$("body").addClass('ui-disabled');
	$.mobile.loading("show",{
		text: "Cargando",
		textVisible: true
		});
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: rootURL,
        dataType: "json",
        data: formToJSON(),
        success: function(data, textStatus, jqXHR){
        	MsgPop.open({
	          	  Type:  "success",
	          	  Content:'Usuario agregado!'});
        },
        error: function(jqXHR, textStatus, errorThrown){
        	MsgPop.open({
	          	  Type:  "error",
	          	  Content:"Error al agregar el usuario!"});
        },
		complete : function() {
			$.mobile.loading("hide");
			$("body").removeClass('ui-disabled');
		}
    });
}
 
function updateMember() {
	$("body").addClass('ui-disabled');
	$.mobile.loading("show",{
		text: "Cargando",
		textVisible: true
		});
    $.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: rootURL + '/' + $('#wineId').val(),
        dataType: "json",
        data: formToJSON(),
        success: function(data, textStatus, jqXHR){
            alert('Wine updated successfully');
        },
        error: function(jqXHR, textStatus, errorThrown){
            alert('updateWine error: ' + textStatus);
        },
		complete : function() {
			$.mobile.loading("hide");
			$("body").removeClass('ui-disabled');
		}
    });
}
 
function deleteMember(data) {
	$("body").addClass('ui-disabled');
	$.mobile.loading("show",{
		text: "Cargando",
		textVisible: true
		});
    console.log('deleteMembee');
    $.ajax({
        type: 'DELETE',
        url: rootURL + '/' + data,
        success: function(data){
        	celanList();
        	MsgPop.open({
	          	  Type:  "success",
	          	  Content:'Usuario eliminado!'});
        	
        },
		error : function(error) {
			MsgPop.open({
	          	  Type:  "error",
	          	  Content:"Existe un problema al eliminar el usuario!"});
		},
		complete : function() {
			$.mobile.loading("hide");
			$("body").removeClass('ui-disabled');
		}
    });
}
 
// Helper function to serialize all the form fields into a JSON string
function formToJSON() {
    return JSON.stringify({
        "id": $('#userid').val(),
        "name": $('#username').val(),
        "middleName": $('#usermiddlename').val(),
        "lastName": $('#userlastname').val(),
        "age": $('#userage').val(),
        });
}


