var baseUrl = document.getElementsByTagName('base')[0].href;

function createTag(){
	var tg = $('#tag_name').val();
	if( tg != null &&tg !=""){
		var idBlog = $('#id').val();
		doCreateTag( tg, idBlog );
	} else {
		alert("OOOuuppps ! ");
	}
}
function doAddTag( idTag, tgName ){
	var idBlog = $('#id').val();
	addTag( idTag, tgName, idBlog );
	
}

function addTag( idTag, tgName, idBlog )
{
	$.ajax({
    url : baseUrl + "jsp/admin/plugins/blog/DoAddTag.jsp?action=addTag&tag_doc="+idTag+"&tag_name="+tgName+"&id="+idBlog,
    type: 'GET',
    dataType: "json",
    data: {},
    async: false,
    cache:false,
    success:function(data) {
  	if ( data.status == 'OK' )
		{
			setListTag(  idTag, tgName  )
			$( '#tag_doc option:selected' ).detach();
    	}	else	{
				alert( "Echec" );
			}
		},
  	error: function(jqXHR, textStatus, errorThrown) {
			alert( "Error" );
    }
	});
}
function doDeleteTag( idTag, tgName ){
	
	var idBlog = $('#id').val();
	deleteTag( idTag, tgName, idBlog );
}
function deleteTag( idTag, tgName, idBlog )
{
	$.ajax({
    url : baseUrl + "jsp/admin/plugins/blog/DoDeleteTag.jsp?action=removeTag&tag_doc=" + idTag +"&id="+idBlog,
  	type: 'GET',
    dataType: "json",
  	data: {},
    async: false,
  	cache:false,
  	success:function(data) {
	  	if ( data.status == 'OK' )
			{
				var tg = '#tag_' + idTag;
				$( tg ).detach();
				$('#tag_doc').append( '<option value="' + idTag + '">' + tgName + '</option>' );
	  	} else	{
				alert("echec")
			}
		},
  	error: function(jqXHR, textStatus, errorThrown) {
			alert("error")
    }
	});
}
function doUpdatePriorityTag( idTag, action){
	var idBlog = $('#id').val();
	updatePriorityTag( idTag, action, idBlog );
	
}
function updatePriorityTag( idTag, action, idBlog )
{
	$.ajax({
    url : baseUrl + "jsp/admin/plugins/blog/DoUpdatePriority.jsp?tagAction="+action+"&tag_doc="+idTag+ "&id="+idBlog,
    type: 'GET',
    dataType: "json",
    data: {},
    async: false,
    cache:false,
    success:function(data) {
  	if ( data.status == 'OK' )
  		{
			if( action == "moveUp" ){
				$('#tag_' + data.result ).insertAfter( $('#tag_' + idTag) );
			} else if( action == "moveDown" ){
				$('#tag_' + idTag ).insertAfter( $('#tag_' + data.result) );
			}
  	}
  	else
		{
		alert("echec")
		}
	},
  error: function(jqXHR, textStatus, errorThrown) {
	alert("error")
  }
});
}

function doCreateTag( tgName, idBlog ){
	$.ajax({
	    url : baseUrl + "jsp/admin/plugins/blog/DoCreateTag.jsp?createTagByAjax=createTagByAjax&name=" + tgName+"&id="+idBlog,
	    type: 'GET',
	    dataType: "json",
	    data: {},
	    async: false,
	  	cache:false,
	    success:function(data) {
		  	if ( data.status == 'OK' ){
					if( data.result != 'TAG_EXIST'){
						setListTag(  data.result, tgName  )
					} else {
						alert('Ce tag existe déja !!!!');
					}
				}
			},
		 	error: function(jqXHR, textStatus, errorThrown) {
				alert(" Erreur ");
			}
	});
}

function setListTag( idTag, tgName ){
	$( "#tag-list" ).append(
		'<li class="list-group-item" id="tag_' + idTag + '">' + tgName + '<span class="pull-right"><button type="button" class="btn btn-primary btn-xs btn-flat btn-down" onclick="doUpdatePriorityTag(' + idTag + ',\'moveDown\')" title="Descendre" ><i class="fa fa-arrow-down"></i></button><button  type="button" class="btn btn-primary btn-xs btn-flat btn-up" title="Monter" onclick="doUpdatePriorityTag(' + idTag + ',\'moveUp\')"><i class="fa fa-arrow-up"></i></button><button id="' + idTag + '" type="button" value="removeTag" name="removeTag" class="btn btn-danger btn-xs btn-flat" onclick="doDeleteTag(' + idTag + ',\'' + tgName + '\')"><i class="fa fa-trash"></i></button></span></li>');
}
