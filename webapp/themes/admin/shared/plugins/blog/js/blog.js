const typeWarning = 'warning'
const typeDanger = 'danger'
let defaultImgFileType='file-x'

async function createTag( blogId ){
	const tgName = document.querySelector('#tag_name').value;
	if( tgName != null && tgName !='' ){
		const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoCreateTag.jsp?createTagByAjax=createTagByAjax&name=${tgName}&id=${blogId}` );
		if (!response.ok) {
			setBlogToast( typeDanger , labelError, response.statusText )	
		} else {
			const data = await response.json();
			if ( data.status == 'OK' ){
				if( data.result == "BLOG_LOCKED" ){
					setBlogToast( typeWarning ,  labelWarning, msgErrorTagExist )	
				} else if( data.result != 'TAG_EXIST'){
					doAddTag( data.result, tgName, blogId )
				} else {
					setBlogToast( typeWarning , labelWarning, msgErrorTagExist )	
				}
			}
		}
	} else {
		setBlogToast( typeWarning , labelWarning, msgErrorTagTitleNotEmpty )
	}
}

async function doAddTag( idTag, tgName, blogId ) {
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoAddTag.jsp?action=addTag&tag_doc=${idTag}&tag_name=${tgName}&id=${blogId}` );
	if ( !response.ok ) {
		setBlogToast( typeDanger , labelError, response.statusText )		
	} else {
		const data = await response.json();
		if ( data.status == 'OK' ){
			if(data.result == "BLOG_LOCKED"){
				setBlogToast( typeWarning , labelWarning, msgErrorBlogLocked )	
			} else {
				setListTag(  idTag, tgName, blogId  )
		  	}
	  }	else {
		setBlogToast( typeDanger , labelError, msgErrorTagNotSet )	
	  }
	}
}

async function doDeleteTag( idTag, tgName, blogId ){
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoDeleteTag.jsp?tagAction=removeTag&tag_doc=${idTag}&id=${blogId}` );
	if (!response.ok) {
		setBlogToast( typeDanger , labelError, response.statusText )	
	} else {
		const data = await response.json();
		if ( data.status == 'OK' ){
			if(data.result == "BLOG_LOCKED"){
				setBlogToast( typeWarning , labelWarning, msgErrorBlogLocked )	
			} else {
				document.querySelector( `#tag_${idTag}`).remove();
			}
	  }	else {
		setBlogToast( typeDanger , labelError, msgErrorTagDeletion )	
	  }
	}
}

async function doUpdatePriorityTag( idTag, action, blogId ){
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoUpdatePriority.jsp?tagAction=${action}&tag_doc=${idTag}&id=${blogId}` );
	if (!response.ok) {
		setBlogToast( typeDanger , labelError, response.statusText )	
	} else {
		const data = await response.json();
		if ( data.status === 'OK' ){
			if(data.result == "BLOG_LOCKED"){
				setBlogToast( typeWarning, labelWarning, msgErrorBlogLocked )	
			}else if( action == "moveUp" ){
				document.querySelector( `#tag_${idTag}`).after( document.querySelector( `#tag_${data.result}` ) );
			} else if( action == "moveDown" ){
				document.querySelector( `#tag_${data.result}` ).after( document.querySelector( `#tag_${idTag}` ) );
			}
	  }	else {
		setBlogToast( typeDanger , labelError, msgErrorTagUpdatePosition )	
	  }
	}
}

function setListTag( idTag, tgName, blogId ){
	let li = document.createElement( 'li' );
	li.classList.add( 'list-group-item', 'list-group-item-action', 'd-flex', 'justify-content-between');
	li.setAttribute('id', `tag_${idTag}` );
	li.textContent = tgName;
	let div = li.appendChild( document.createElement( 'div' ) );
	div.classList.add( 'btn-group' );
	div.setAttribute( 'role', 'group' );
	div.setAttribute('aria-label', `Tag ${tgName}`);
	let btnDown = div.appendChild( document.createElement( 'button' ) );
	btnDown.classList.add( 'btn', 'btn-none' ,'btn-down' ,'text-primary');
	btnDown.setAttribute('type', 'button' );
	btnDown.setAttribute('title', 'Down' );
	btnDown.setAttribute('onclick', `doUpdatePriorityTag( ${idTag}, 'moveDown', ${blogId})` );
	let iconDown = btnDown.appendChild( document.createElement( 'i' ) );
	iconDown.classList.add( 'ti','ti-arrow-down');
	let btnUp = div.appendChild( document.createElement( 'button' ) );
	btnUp.classList.add( 'btn', 'btn-none' ,'btn-up', 'text-primary');
	btnUp.setAttribute('type', 'button' );
	btnUp.setAttribute('title', 'Down' );
	btnUp.setAttribute('onclick', `doUpdatePriorityTag( ${idTag}, 'moveUp', ${blogId})` );
	let iconUp = btnUp.appendChild( document.createElement( 'i' ) );
	iconUp.classList.add( 'ti','ti-arrow-up');
	let btnRm = div.appendChild( document.createElement( 'button' ) );
	btnRm.classList.add( 'btn', 'btn-none' ,'text-danger');
	btnRm.setAttribute('id', idTag );
	btnRm.setAttribute('type', 'button' );
	btnRm.setAttribute('value', 'removeTag' );
	btnRm.setAttribute('name', 'removeTag' );
	btnRm.setAttribute('onclick', `doDeleteTag( ${idTag}, '${tgName}', ${blogId})` );
	let iconRm = btnRm.appendChild( document.createElement( 'i' ) );
	iconRm.classList.add( 'ti','ti-x');
	document.querySelector( "#tag-list" ).appendChild( li );
}

function setListFile( idFile, fileName, fileType, fileExt, blogId ){
	let li = document.createElement( 'li' );
	li.classList.add( 'list-group-item', 'list-group-item-action', 'blog-image-content', 'd-flex', 'justify-content-between', 'align-items-center');
	li.setAttribute( 'id', `doc_${idFile}` );
	if ( fileType == 1 ){   
		let imgFile = li.appendChild( document.createElement( 'img' ) );
		imgFile.setAttribute('src', `servlet/plugins/blogs/file?id_file=${idFile}` );
		imgFile.setAttribute('alt', `${fileName}` );
		imgFile.setAttribute('title', `${fileName}` );
		imgFile.classList.add( 'img-fluid', 'img-thumbnail', 'blog-thumbnails');
	} else {
		let imgFile = li.appendChild( document.createElement( 'img' ) );
		imgFile.setAttribute('src', `themes/admin/shared/plugins/blog/images/file-type-${fileExt}.svg` );
		imgFile.setAttribute('alt', `${fileName}` );
		imgFile.setAttribute('title', `${fileName}` );
		imgFile.classList.add( 'img-fluid', 'img-thumbnail', 'blog-thumbnails');
		let linkFile = li.appendChild( document.createElement( 'a' ) );
		linkFile.setAttribute('href', `servlet/plugins/blogs/file?id_file=${idFile}` );
		linkFile.setAttribute('title', `${fileName}` );
		linkFile.textContent = fileName;
	}
	let div = li.appendChild( document.createElement( 'div' ) );
	div.classList.add( 'btn-group' );
	div.setAttribute( 'role', 'group' );
	div.setAttribute('aria-label', `Manage ${fileName}` );
	let btnInsert = div.appendChild( document.createElement( 'button' ) );
	btnInsert.classList.add( 'btn','btn-none', 'text-primary');
	btnInsert.setAttribute('title', 'Ajouter au contenu' );
	btnInsert.setAttribute('type', 'button' );
	btnInsert.setAttribute('onclick', `doInsertContent( ${idFile}, '${fileName}', ${fileType} )` );
	let iconInsert = btnInsert.appendChild( document.createElement( 'i' ) );
	iconInsert.classList.add( 'ti','ti-file-plus');
	let btnDown = div.appendChild( document.createElement( 'button' ) );
	btnDown.classList.add( 'btn','btn-none', 'btn-down' ,'text-primary');
	btnDown.setAttribute('type', 'button' );
	btnDown.setAttribute('title', 'Down' );
	btnDown.setAttribute('onclick', `doUpdatePriorityContent( ${idFile}, 'moveDown', ${blogId})` );
	let iconDown = btnDown.appendChild( document.createElement( 'i' ) );
	iconDown.classList.add( 'ti','ti-arrow-down');
	let btnUp = div.appendChild( document.createElement( 'button' ) );
	btnUp.classList.add( 'btn', 'btn-none', 'btn-up', 'text-primary');
	btnUp.setAttribute('type', 'button' );
	btnUp.setAttribute('title', 'Down' );
	btnUp.setAttribute('onclick', `doUpdatePriorityContent( ${idFile}, 'moveUp'), ${blogId})` );
	let iconUp = btnUp.appendChild( document.createElement( 'i' ) );
	iconUp.classList.add( 'ti','ti-arrow-up');
	let btnRm = div.appendChild( document.createElement( 'button' ) );
	btnRm.classList.add( 'btn', 'btn-none', 'text-danger');
	btnRm.setAttribute('id', idFile );
	btnRm.setAttribute('type', 'button' );
	btnRm.setAttribute('onclick', `deleteFileContent( ${idFile}, ${blogId})` );
	let iconRm = btnRm.appendChild( document.createElement( 'i' ) );
	iconRm.classList.add( 'ti','ti-x');
	document.querySelector( '#content-list' ).appendChild( li );
}

function getFile( blogId ) {
	const file =  document.querySelector('#attachment').files[0];
	const reader = new FileReader();
	reader.readAsDataURL( file );
	reader.onload=function(){
		const rgx = /image/g;
		const fileType = rgx.test( file.type ) ? 1 : 2, extension = file.name.split('.').pop().toLowerCase()	
		doAddContent( file.name, file.size, reader.result, fileType, blogId ).then( resp => {
			/* call the callback and populate the Title field with the file name */
			if ( resp.status == 'OK' ){
			  if( resp.result == "BLOG_LOCKED" ){
				setBlogToast( 'warning', 'Attention', 'Billet verrouillé !' );
			  } else {
				setListFile( resp.result[1], resp.result[0], fileType, extension, blogId )
			  }
			}
		});
	};
	reader.onerror = function (error) {
	  setBlogToast( typeDanger , labelError, error )	
	};
 }
 
 /* TODO : Is this is pnly used with uploadimage plugin */
 function getCroppedCanva( fieldName ) {
	const idBlog = document.querySelector('#id').value;
	const fileType = document.querySelector('#fileType').value;
	const thisElement= document.querySelector( `.img-container ${fieldName} > img` );
	const result = thisElement.cropper( 'getCroppedCanvas', { width: "222", height:"555" });
	doAddContent( fieldName, {}, result.toDataURL(), fileType, idBlog );
 }
 
function deleteFileContent( idContent, idBlog ) {
	doDeleteContent( idContent, idBlog);
	document.querySelector( `.blog-resources #doc_${idContent}`).remove();
}

async function doAddContent( fileName, fileInfo, result, fileType, idBlog ){
	const formData = new FormData();

	// Convert the JSON data to a JSON string and add it to the FormData object
	formData.append( 'fileContent', result );
	formData.append( 'fileName', fileName );
	formData.append( 'fileType', fileType );
	formData.append( 'id', idBlog );

	// Make a POST request with the FormData object
	const response = await fetch(`${baseUrl}jsp/admin/plugins/blog/DoCreateImage.jsp?action=addContent`, {
		method: 'POST',
		datatype: 'json',
		body: formData,
	})
	if (!response.ok) {
		setBlogToast( typeDanger , labelError, response.statusText )	
	} else {
		const resp = await response.json();
		return resp;
	}
}

function doInsertContent( idContent, titleContent, typeContent ) {
	let contentToInsert='', contentTag = 'p';
	if ( typeContent === 1 ) {
		contentTag = 'figure'
		contentToInsert=`<img src="servlet/plugins/blogs/file?id_file=${idContent}" alt="${titleContent}" />`
	} else {
		contentTag = 'p'
		contentToInsert=`<a title="${titleContent}" href="servlet/plugins/blogs/file?id_file=${idContent}">${titleContent}</a>`
	}
	const tinyCurrentNode = tinymce.activeEditor.selection.getNode() 
	tinymce.activeEditor.dom.add( tinyCurrentNode, contentTag, {}, contentToInsert);
}

async function doDeleteContent( idContent, idBlog ) {
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoDeleteImage.jsp?action=removeContent&idContent=${idContent}&id=${idBlog}` );
	if ( !response.ok ) {
		setBlogToast( typeDanger , labelError, response.status )	
	} else {
		const data = await response.json();
		if ( data.status == 'OK' ){
			if(data.result == "BLOG_LOCKED"){
				setBlogToast( typeWarning, labelWarning, msgErrorBlogLocked )	
			}
	  }	else {
		setBlogToast( typeDanger , labelError, msgErrorBlogFileCannotBeDeleted )	
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
		setBlogToast( typeDanger , labelError, response.statusText )	
	} else {
		const data = await response.json();
		if ( data.status == 'OK' ){
			if(data.result == "BLOG_LOCKED"){
				setBlogToast( typeWarning,labelWarning,msgErrorBlogLocked )	
			}
	  }	else {
		setBlogToast( typeDanger , labelError, msgErrorBlogFileTypeNotUpdated )	
	  }
	}
}
 
async function doUpdatePriorityContent( idContent, action, idBlog ){
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoUpdatePriorityContent.jsp?contentAction=${action}&idContent=${idContent}&id=${idBlog}` );
	if ( !response.ok ) {
		setBlogToast( typeDanger , labelError, response.status )	
	} else {
		const data = await response.json();
		if ( data.status == 'OK' ){
			if ( data.status == 'OK' ){
				if(data.result == "BLOG_LOCKED"){
					setBlogToast( typeWarning, labelWarning, msgErrorBlogLocked )	
				}else if( action == "moveUp" ){
					document.querySelector( `#doc_${idContent}`).after( document.querySelector( `#doc_${data.result}` ) );
				} else if( action == "moveDown" ){
					document.querySelector( `#doc_${data.result}` ).after( document.querySelector( `#doc_${idContent}` ) );
				}
			} else {
				setBlogToast( typeDanger , labelError, msgErrorTagUpdatePosition )	
			}
	  }	else {
		setBlogToast( typeDanger , labelError, msgErrorTagUpdatePosition )	
	  }
	}
}
