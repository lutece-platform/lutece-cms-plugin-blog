function getImage( ) {
   var fileType = $('#fileType').val();
   var file = $('#attachment').get(0).files[0];
   var idBlog = $('#id').val();

   var reader = new FileReader();
   reader.readAsDataURL(file);
   reader.onload = function () {
     doAddContent( file.name, reader.result, fileType, idBlog);
     
   };
   reader.onerror = function (error) {
     console.log('Error: ', error);
   };
}

function getCroppedCanva(fieldName){

	var fileType = $('#fileType').val();
	var idBlog = $('#id').val();	
	var $element= $('.img-container'+fieldName+' > img');
  
	result = $element.cropper('getCroppedCanvas', { width: "222", height:"555" });
	doAddContent( fieldName, result.toDataURL(), fileType, idBlog );
	
};

function  deleteImage(idContent){ 
		
	   var idBlog = $('#id').val();
		doDeleteContent(idContent, idBlog);
		$("#doc_"+idContent).detach();
	  	
};

function doAddContent( fileName, result, fileType, idBlog )
{
    $.ajax({
    url : baseUrl + "jsp/admin/plugins/blog/DoCreateImage.jsp?action=addContent",
    type: 'POST',
    dataType: "json",
    data: {fileContent:result, fileName:fileName, fileType:fileType, id:idBlog},
    async: false,
    cache:true,
    success:function(data) {
  	if ( data.status == 'OK' )
		{
  			if(data.result == "BLOG_LOCKED"){
				alert( "Billet Verrouillé" );
			}else{
				$('#imagesrc'+fileName).val(result);
				$('#content-list').append( '<li id="doc_'+data.result[1]+'" class="list-group-item clearfix"><div id= "' +
			              data.result[1] +
			              '">' +
			              '<button id="deleteButtonattachment" class="btn btn-default" onclick=deleteImage("' +
			              data.result[1] +
			              '") type="button" title="Supprimer" style=""><span class="glyphicon glyphicon-remove-circle"></span> Supprimer</button>' +
			              '<a href="servlet/plugins/blogs/file?id_file=' +
			              data.result[1] +
			              '" title="preview">'+data.result[0]+'<img id="preview_attachmen" src=servlet/plugins/blogs/file?id_file=' +
			              data.result[1] +
			              ' alt="Preview" class="img-responsive img-thumbnail"></a>' +
			              '<span class="pull-right"><button type="button" class="btn btn-primary btn-xs btn-flat btn-down" title="Descendre" onclick="doUpdatePriorityContent( ' +
			              data.result[1] +
			              ', \'moveDown\' )"><i class="fa fa-arrow-down"></i></button><button type="button" class="btn btn-primary btn-xs btn-flat btn-up" title="Monter" onclick="doUpdatePriorityContent(' +
			              data.result[1] +
			              ', \'moveUp\')"><i class="fa fa-arrow-up"></i></button></span></div></li>');			//$('#deleteButton'+fileName).show();
			}
    	}	else	{
				alert( "Echec" );
			}
		},
  	error: function(jqXHR, textStatus, errorThrown) {
			alert( "Error" );
    }
	});
}

function doDeleteContent( idContent, idBlog )
{
	$.ajax({
    url : baseUrl + "jsp/admin/plugins/blog/DoDeleteImage.jsp?action=removeContent&idContent=" + idContent +"&id="+ idBlog ,
  	type: 'GET',
    dataType: "json",
  	data: {},
    async: false,
  	cache:false,
  	success:function(data) {
	  	if ( data.status == 'OK' )
			{
	  		if(data.result == "BLOG_LOCKED"){
  				alert( "Billet Verrouillé" );
  			}
	  	} else	{
				alert("echec")
			}
		},
  	error: function(jqXHR, textStatus, errorThrown) {
			alert("error")
    }
	});
}

function doUpdateContentType( idContent, idTypeContent){
	var idBlog = $('#id').val();
	updateContentType( idContent, idTypeContent, idBlog);
}

function updateContentType( idContent, idTypeContent, idBlog)
{
    $.ajax({
    url : baseUrl + "jsp/admin/plugins/blog/DoUpdateContentType.jsp?action=updateContentType",
    type: 'POST',
    dataType: "json",
    data: {idType:idTypeContent, idContent:idContent, id:idBlog},
    async: false,
    cache:true,
    success:function(data) {
  	if ( data.status == 'OK' )
		{
  		if(data.result == "BLOG_LOCKED"){
				alert( "Billet Verrouillé" );
		}	
    	}	else	{
				alert( "Echec" );
			}
		},
  	error: function(jqXHR, textStatus, errorThrown) {
			alert( "Error" );
    }
	});
}

function doUpdatePriorityContent( idContent, action )
{
	var idBlog = $('#id').val();
	updatePriorityContent( idContent, action, idBlog );
}

function updatePriorityContent( idContent, action, idBlog )
{
	$.ajax({
    url : baseUrl + "jsp/admin/plugins/blog/DoUpdatePriorityContent.jsp?contentAction="+action+"&idContent="+idContent+ "&id="+idBlog,
    type: 'GET',
    dataType: "json",
    data: {},
    async: false,
    cache:false,
    success:function(data) {
  	if ( data.status == 'OK' )
  		{
  			if(data.result == "BLOG_LOCKED"){
				alert( "Billet Verrouillé" );
			}else if( action == "moveUp" ){
				$('#doc_' + data.result ).insertAfter( $('#doc_' + idContent) );
			} else if( action == "moveDown" ){
				$('#doc_' + idContent ).insertAfter( $('#doc_' + data.result) );
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
