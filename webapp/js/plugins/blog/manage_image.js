function getImage( ) {
   var fileType = $('#fileType').val();
   var file = $('#attachment').get(0).files[0];
   var reader = new FileReader();
   reader.readAsDataURL(file);
   reader.onload = function () {
     doAddContent( "", reader.result, fileType);
     
   };
   reader.onerror = function (error) {
     console.log('Error: ', error);
   };
}

function getCroppedCanva(fieldName){

	var fileType = $('#fileType').val();
	var $element= $('.img-container'+fieldName+' > img');
  
	result = $element.cropper('getCroppedCanvas', { width: "222", height:"555" });
	doAddContent( fieldName, result.toDataURL(), fileType );
	
};

function  deleteImage(fName){ 

		doDeleteContent(fName);
	   	$("#"+fName).html('');
	  	
};

function doAddContent( fileName, result, fileType )
{
    $.ajax({
    url : baseUrl + "jsp/admin/plugins/blog/DoCreateImage.jsp?action=addContent",
    type: 'POST',
    dataType: "json",
    data: {fileContent:result, fileName:fileName, fileType:fileType},
    async: false,
    cache:true,
    success:function(data) {
  	if ( data.status == 'OK' )
		{
			$('#imagesrc'+fileName).val(result);
			$('#imageappend').append('<div id= "'+data.result+'">'+'<button id="deleteButtonattachment" class="btn btn-default" onclick=deleteImage("'+data.result+'") type="button" title="Supprimer" style=""><span class="glyphicon glyphicon-remove-circle"></span> Supprimer</button>' + '<img id="preview_attachmen" src="'+result+'" alt="Preview" > </div>');
			//$('#deleteButton'+fileName).show();
	
    	}	else	{
				alert( "Echec" );
			}
		},
  	error: function(jqXHR, textStatus, errorThrown) {
			alert( "Error" );
    }
	});
}

function doDeleteContent( fileName )
{
	$.ajax({
    url : baseUrl + "jsp/admin/plugins/blog/DoDeleteImage.jsp?action=removeContent&fileName=" + fileName,
  	type: 'GET',
    dataType: "json",
  	data: {},
    async: false,
  	cache:false,
  	success:function(data) {
	  	if ( data.status == 'OK' )
			{

	  	} else	{
				alert("echec")
			}
		},
  	error: function(jqXHR, textStatus, errorThrown) {
			alert("error")
    }
	});
}