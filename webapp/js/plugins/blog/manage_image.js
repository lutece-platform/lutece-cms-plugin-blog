function getImage( ) {
   var fileType = $('#fileType').val();
   var file = $('#attachment').get(0).files[0];
   var idBlog = $('#id').val();

   var reader = new FileReader();
   reader.readAsDataURL(file);
   reader.onload = function () {
     doAddContent( "", reader.result, fileType, idBlog);
     
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
	   	$("#"+fName).html('');
	  	
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
				$('#imageappend').append('<div id= "'+data.result[0]+'">'+'<button id="deleteButtonattachment" class="btn btn-default" onclick=deleteImage("'+data.result[1]+'") type="button" title="Supprimer" style=""><span class="glyphicon glyphicon-remove-circle"></span> Supprimer</button>'+'<img id="preview_attachmen" src=servlet/plugins/blogs/file?id_file='+data.result[1]+' alt="Preview"> </div>');			//$('#deleteButton'+fileName).show();
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