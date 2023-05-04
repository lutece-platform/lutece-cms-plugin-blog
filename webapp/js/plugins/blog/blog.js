async function createTag( ){
	const tgName = document.querySelector('#tag_name').value;
	if( tgName != null && tgName !='' ){
		const idBlog = document.querySelector('#id').value;
		const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoCreateTag.jsp?createTagByAjax=createTagByAjax&name=${tgName}&id=${idBlog}` );
		if (!response.ok) {
			setBlogToast( 'danger' , 'Erreur', response.statusText )	
		} else {
			const data = await response.json();
			if ( data.status == 'OK' ){
				if( data.result == "BLOG_LOCKED"){
					setBlogToast( '' ,  'Attention', 'Billet verrouillé !' )	
				} else if( data.result != 'TAG_EXIST'){
					setListTag(  data.result, tgName  )
				} else {
					setBlogToast( 'warning' , 'Attention', 'Ce tag existe déja !' )	
				}
			}
		}
	} else {
		setBlogToast( 'warning' , 'Attention', 'Le titre du tag peut être vide !' )
	}
}

async function doAddTag( idTag, tgName ) {
	const idBlog = document.querySelector('#id').value;
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoAddTag.jsp?action=addTag&tag_doc=${idTag}&tag_name=${tgName}&id=${idBlog}` );
	if ( !response.ok ) {
		setBlogToast( 'danger' , 'Erreur', response.statusText )		
	} else {
		const data = await response.json();
		if ( data.status == 'OK' ){
			if(data.result == "BLOG_LOCKED"){
				setBlogToast( 'warning' , 'Attention', 'Billet verrouillé !' )	
			} else {
				setListTag(  idTag, tgName  )
		  	}
	  }	else {
		setBlogToast( 'danger' , 'Erreur', 'Impossible d\'ajouter ce tag' )	
	  }
	}
}

async function doDeleteTag( idTag, tgName ){
	const idBlog = document.querySelector('#id').value;
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoDeleteTag.jsp?action=removeTag&tag_doc=${idTag}&id=${idBlog}` );
	if (!response.ok) {
		setBlogToast( 'danger' , 'Erreur', response.statusText )	
	} else {
		const data = await response.json();
		if ( data.status == 'OK' ){
			if(data.result == "BLOG_LOCKED"){
				setBlogToast( 'warning' , 'Attention', 'Billet verrouillé !' )	
			} else {
				document.querySelector( `#tag_${idTag}`).remove();
			}
	  }	else {
		setBlogToast( 'danger' , 'Erreur', 'Impossible de supprimer ce tag' )	
	  }
	}
}

async function doUpdatePriorityTag( idTag, action ){
	const idBlog = document.querySelector('#id').value;
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoUpdatePriority.jsp?tagAction=${action}&tag_doc=${idTag}&id=${idBlog}` );
	if (!response.ok) {
		setBlogToast( 'danger' , 'Erreur', response.statusText )	
	} else {
		const data = await response.json();
		if ( data.status == 'OK' ){
			if(data.result == "BLOG_LOCKED"){
				setBlogToast( 'warning','Attention','Billet verrouillé !' )	
			}else if( action == "moveUp" ){
				document.querySelector( `#tag_${idTag}`).after( document.querySelector( `#tag_${data.result}` ) );
			} else if( action == "moveDown" ){
				document.querySelector( `#tag_${data.result}` ).after( document.querySelector( `#tag_${idTag}` ) );
			}
	  }	else {
		setBlogToast( 'danger' , 'Erreur', 'Impossible de mettre à jour la position de ce tag' )	
	  }
	}
}

function setListTag( idTag, tgName ){
	let li = document.createElement( 'li' );
	li.classList.add( 'list-group-item', 'list-group-item-action', 'd-flex', 'justify-content-between');
	li.setAttribute('id', `tag_${idTag}` );
	li.textContent = tgName;
	let div = li.appendChild( document.createElement( 'div' ) );
	div.classList.add( 'btn-group' );
	div.setAttribute( 'role', 'group' );
	div.setAttribute('aria-label', 'Update Tag' );
	let btnDown = div.appendChild( document.createElement( 'button' ) );
	btnDown.classList.add( 'btn','btn-sm', 'btn-down' ,'text-primary');
	btnDown.setAttribute('type', 'button' );
	btnDown.setAttribute('title', 'Down' );
	btnDown.setAttribute('onclick', `doUpdatePriorityTag( ${idTag}, 'moveDown')` );
	let iconDown = btnDown.appendChild( document.createElement( 'i' ) );
	iconDown.classList.add( 'ti','ti-arrow-down');
	let btnUp = div.appendChild( document.createElement( 'button' ) );
	btnUp.classList.add( 'btn', 'btn-sm', 'btn-up', 'text-primary');
	btnUp.setAttribute('type', 'button' );
	btnUp.setAttribute('title', 'Down' );
	btnUp.setAttribute('onclick', `doUpdatePriorityTag( ${idTag}, 'moveUp')` );
	let iconUp = btnUp.appendChild( document.createElement( 'i' ) );
	iconUp.classList.add( 'ti','ti-arrow-up');
	let btnRm = div.appendChild( document.createElement( 'button' ) );
	btnRm.classList.add( 'btn', 'btn-sm', 'text-danger');
	btnRm.setAttribute('id', idTag );
	btnRm.setAttribute('type', 'button' );
	btnRm.setAttribute('value', 'removeTag' );
	btnRm.setAttribute('name', 'removeTag' );
	btnRm.setAttribute('onclick', `doDeleteTag( ${idTag}, ${tgName})` );
	let iconRm = btnRm.appendChild( document.createElement( 'i' ) );
	iconRm.classList.add( 'ti','ti-x');
	document.querySelector( "#tag-list" ).appendChild( li );
}

function getImage( ) {
	const file =  document.querySelector('#attachment').files[0];
	const fileInfo={ size: file.size, ext: file.type };
	const fileBlog = parseInt( document.querySelector('#id').value );
	const reader = new FileReader();
	reader.readAsDataURL( file );
	reader.onload = function () {
		const rgx = /image/g;
		const fileType = rgx.test( fileInfo.ext ) ? 1 : 2;
		doAddContent( file.name, fileInfo, reader.result, fileType, fileBlog );
	};
	reader.onerror = function (error) {
	  console.log( 'Error: ', error);
	};
 }
 
 function getCroppedCanva( fieldName ) {
	const fileType = document.querySelector('#fileType').value;
	const idBlog = document.querySelector('#id').value;
	const thisElement= document.querySelector( `.img-container ${fieldName} > img` );
	const result = thisElement.cropper( 'getCroppedCanvas', { width: "222", height:"555" });
	doAddContent( fieldName, {}, result.toDataURL(), fileType, idBlog );
 }
 
function deleteImage( idContent ) {
	const idBlog = document.querySelector('#id').value;
	doDeleteContent(idContent, idBlog);
	document.querySelector( `.blog-resources #doc_${idContent}`).remove();
}

function doAddContent( fileName, fileInfo, result, fileType, idBlog ){
	var res='';
	$.ajax({
		url : baseUrl + "jsp/admin/plugins/blog/DoCreateImage.jsp?action=addContent",
		type: 'POST',
		dataType: "json",
		data: { fileContent:result, fileName:fileName, fileType:fileType, id:idBlog},
		async: false,
		cache:true,
		success:function(data){
		    if ( data.status == 'OK' ){
			  if( data.result == "BLOG_LOCKED" ){
				  setBlogToast( 'warning','Attention','Billet verrouillé !' );
			  } else {

          if ( fileType == 1 ){
            //$('#imagesrc' + data.result[0] ).val(result);
            $('#content-list').append('<li class="list-group-item blog-item-resource blog-image-content" id="doc_'+ data.result[1] +'"><img class="img-fluid img-thumbnail blog-thumbnails" src="servlet/plugins/blogs/file?id_file=' + data.result[1] + '" title="'+data.result[0]+'" alt="' + data.result[0] + '"><span class="pull-right"><button type="button" class="btn btn-link btn-xs btn-flat btn-down" title="Descendre" onclick="doUpdatePriorityContent(' + data.result[1] + ', \'moveDown\' )"><i class="fa fa-arrow-down"></i></button><button type="button" class="btn btn-link btn-xs btn-flat btn-up" title="Monter" onclick="doUpdatePriorityContent(' + data.result[1] +  ', \'moveUp\')"><i class="fa fa-arrow-up"></i></button></span></li>');
          } else {
            var fileSize = fileInfo.size;
            $('#content-list').append('<li class="list-group-item blog-item-resource blog-file-content" id="doc_'+data.result[1]+'"><p><a href=servlet/plugins/blogs/file?id_file='+data.result[1]+' alt="Download">'+ fileName + '<br><small>' + fileSize + ' Ko </small><span class="pull-right"><button type="button" class="btn btn-link btn-xs btn-flat btn-down" title="Descendre" onclick="doUpdatePriorityContent(' + data.result[1] + ', \'moveDown\' )"><i class="fa fa-arrow-down"></i></button><button type="button" class="btn btn-link btn-xs btn-flat btn-up" title="Monter" onclick="doUpdatePriorityContent(' + data.result[1] + ', \'moveUp\')"><i class="fa fa-arrow-up"></i></button></span></p></li>');
          }
          res = {location: 'servlet/plugins/blogs/file?id_file=' + data.result[1] };
			  }
			} else {
			  setBlogToast( 'warning','Attention','Billet verrouillé !' );
		   }
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert( "Error" );
		}
	});
	return res;
}


// async function doAddContent( fileName, fileInfo, fileResult, fileType, fileBlog ){
// 	let res = '';
// 	const formData = new FormData();
// 	const encodedFileResult = encodeURI( fileResult );
// 	formData.append('fileContent', fileResult );
// 	formData.append('fileName', fileName );
// 	formData.append('fileType', fileType );
// 	formData.append('id', fileBlog );
// 	const fileData = { fileContent: fileResult ,fileName: fileName, fileType: fileType, id : fileBlog }
// 	const jsonData = JSON.stringify( fileData );
// 	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoCreateImage.jsp?action=addContent&id=${fileBlog}`, {
// 		method: 'POST',
// 		headers: { 'Content-Type': 'application/json' },
// 		body: fileData,
// 	}).then(response => response.json() )
// 	.then( data => console.log(data) )
// 	.catch( error => setBlogToast( 'danger' , 'Erreur', 'Erreur : ' + error ));
// 	if ( response.ok) {
// 		const data = await response.json();
// 		if ( data.status == 'OK' ) {
// 			if ( data.result == "BLOG_LOCKED" ) {
// 				setBlogToast( 'danger' , 'Attention', 'Billet verrouillé !' )	
// 			} else {
// 			  if ( fileType == 1 ){
// 				  $('#content-list').append('<li class="list-group-item blog-item-resource blog-image-content" id="doc_'+ data.result[1] +'"><img class="img-fluid img-thumbnail blog-thumbnails" src="servlet/plugins/blogs/file?id_file=' + data.result[1] + '" title="'+data.result[0]+'" alt="' + data.result[0] + '"><span class="pull-right"><button type="button" class="btn btn-link btn-xs btn-flat btn-down" title="Descendre" onclick="doUpdatePriorityContent(' + data.result[1] + ', \'moveDown\' )"><i class="fa fa-arrow-down"></i></button><button type="button" class="btn btn-link btn-xs btn-flat btn-up" title="Monter" onclick="doUpdatePriorityContent(' + data.result[1] +  ', \'moveUp\')"><i class="fa fa-arrow-up"></i></button></span></li>');
// 				} else {
// 				  var fileSize = fileInfo.size;
// 				  $('#content-list').append('<li class="list-group-item blog-item-resource blog-file-content" id="doc_'+data.result[1]+'"><p><a href=servlet/plugins/blogs/file?id_file='+data.result[1]+' alt="Download">'+ fileName + '<br><small>' + fileSize + ' Ko </small><span class="pull-right"><button type="button" class="btn btn-link btn-xs btn-flat btn-down" title="Descendre" onclick="doUpdatePriorityContent(' + data.result[1] + ', \'moveDown\' )"><i class="fa fa-arrow-down"></i></button><button type="button" class="btn btn-link btn-xs btn-flat btn-up" title="Monter" onclick="doUpdatePriorityContent(' + data.result[1] + ', \'moveUp\')"><i class="fa fa-arrow-up"></i></button></span></p></li>');
// 				}
// 				res = { location: `servlet/plugins/blogs/file?id_file=${data.result[1]}` };
// 			}
// 		} else {
// 			setBlogToast( 'warning' , 'Attention', 'Impossible d\'ajouter un cette image !' )	
// 	  	}
// 	}
// 	res = { location: `servlet/plugins/blogs/file?id_file=${data.result[1]}` };
// }

async function doDeleteContent( idContent, idBlog ) {
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoDeleteImage.jsp?action=removeContent&idContent=${idContent}&id=${idBlog}` );
	if ( !response.ok ) {
		setBlogToast( 'danger' , 'Erreur', 'Erreur : ' + response.status )	
	} else {
		const data = await response.json();
		if ( data.status == 'OK' ){
			if(data.result == "BLOG_LOCKED"){
				setBlogToast( 'warning', 'Attention', 'Billet verrouillé !' )	
			}
	  }	else {
		setBlogToast( 'danger' , 'Erreur', 'Impossible de supprimer ' )	
	  }
	}
}
 
async function doUpdateContentType( idContent, idTypeContent, idBlog ) {
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoUpdateContentType.jsp?action=updateContentType`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: { idType:idTypeContent, idContent:idContent, id:idBlog },
	});
	if (!response.ok) {
		setBlogToast( 'danger' , 'Erreur', response.statusText )	
	} else {
		const data = await response.json();
		if ( data.status == 'OK' ){
			if(data.result == "BLOG_LOCKED"){
				setBlogToast( 'warning','Attention','Billet verrouillé !' )	
			}
	  }	else {
		setBlogToast( 'danger' , 'Erreur', 'Impossible de mettre à jour le type de ce fichier' )	
	  }
	}
}
 
async function doUpdatePriorityContent( idContent, action, idBlog ){
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoUpdatePriorityContent.jsp?contentAction=${action}&idContent=${idContent}&id=${idBlog}` );
	if ( !response.ok ) {
		setBlogToast( 'danger' , 'Erreur', 'Erreur : ' + response.status )	
	} else {
		const data = await response.json();
		if ( data.status == 'OK' ){
			const data = await response.json();
			if ( data.status == 'OK' ){
				if(data.result == "BLOG_LOCKED"){
					setBlogToast( 'warning','Attention','Billet verrouillé !' )	
				}else if( action == "moveUp" ){
					document.querySelector( `#doc_${idContent}`).after( document.querySelector( `#doc_${data.result}` ) );
				} else if( action == "moveDown" ){
					document.querySelector( `#doc_${data.result}` ).after( document.querySelector( `#doc_${idContent}` ) );
				}
			} else {
				setBlogToast( 'danger' , 'Erreur', 'Impossible de mettre à jour la position de ce tag' )	
			}
	  }	else {
		setBlogToast( 'danger' , 'Erreur', 'Impossible de supprimer ' )	
	  }
	}
}