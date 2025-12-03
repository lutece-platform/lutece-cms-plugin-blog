const typeWarning = 'warning'
const typeDanger = 'danger'
let defaultImgFileType='file-x'

async function doAddTag( idTag, tgName, blogId ) {
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoAddTag.jsp?action=addTag&tag_doc=${idTag}&tag_name=${tgName}&id=${blogId}` );
	if ( !response.ok ) {
		setBlogToast( typeDanger , labelError, response.statusText )		
	} else {
		const data = await response.json();
		if ( data.status == 'OK' ){
			if ( data.result == 'BLOG_LOCKED' ) {
				setBlogToast( typeWarning , labelWarning, msgErrorBlogLocked )	
			} else {
			    blog_tag.push( idTag );
			    refreshListTag();
				setListTag(  idTag, tgName, blogId  )
                numberOfTagsAssigned++;
				if( parseInt( numberMandatoryTags ) > 0 ){
                	refreshMandatoryTagInfo( );
				}
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
			    blog_tag = blog_tag.filter(id => id != idTag)
                refreshListTag();
				document.querySelector( `#tag_${idTag}`).remove();
                numberOfTagsAssigned--;
				if( parseInt( numberMandatoryTags ) > 0 ){
					refreshMandatoryTagInfo( );
				}
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

function refreshMandatoryTagInfo( ){
    const alertRequiredTags = document.getElementById('required-tag');
	const actionBtn = document.querySelectorAll('#blog-toolbar .btn.action');
	const elemEnoughTagsInfo = document.getElementById('enoughTagsInfo');

	if( actionBtn.length > 0 ){
		actionBtn.forEach( ( btn ) => {
			if( numberOfTagsAssigned <= ( parseInt( numberMandatoryTags ) - 1 )  ) {
				btn.disabled = true;
			} else {
				btn.disabled = false;
			}
   	 	});
	}

    if( numberOfTagsAssigned <= ( parseInt( numberMandatoryTags ) - 1 )  ) {
        alertRequiredTags.classList.remove('visually-hidden');
        if(elemEnoughTagsInfo!=null)
        {
            elemEnoughTagsInfo.classList.add('alert-warning');
            elemEnoughTagsInfo.classList.remove('alert-info');
        }
    } else {
        alertRequiredTags.classList.add('visually-hidden');
        if(elemEnoughTagsInfo!=null)
        {
            elemEnoughTagsInfo.classList.remove('alert-warning');
            elemEnoughTagsInfo.classList.add('alert-info');
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
	if(blogId !== undefined && blogId !== null && blogId !== '' && blogId !== 0){
		btnDown.setAttribute('onclick', `doUpdatePriorityContent( ${idFile}, 'moveDown', ${blogId})` );
	} else {
		btnDown.setAttribute('onclick', `doUpdatePriorityContentBis( ${idFile}, 'moveDown')` );
	}
	let iconDown = btnDown.appendChild( document.createElement( 'i' ) );
	iconDown.classList.add( 'ti','ti-arrow-down');
	let btnUp = div.appendChild( document.createElement( 'button' ) );
	btnUp.classList.add( 'btn', 'btn-none', 'btn-up', 'text-primary');
	btnUp.setAttribute('type', 'button' );
	btnUp.setAttribute('title', 'Up' );
	if(blogId !== undefined && blogId !== null && blogId !== '' && blogId !== 0){
		btnUp.setAttribute('onclick', `doUpdatePriorityContent( ${idFile}, 'moveUp'), ${blogId})` );
	} else {
		btnUp.setAttribute('onclick', `doUpdatePriorityContentBis( ${idFile}, 'moveUp')` );
	}
	let iconUp = btnUp.appendChild( document.createElement( 'i' ) );
	iconUp.classList.add( 'ti','ti-arrow-up');
	let btnRm = div.appendChild( document.createElement( 'button' ) );
	btnRm.classList.add( 'btn', 'btn-none', 'text-danger');
	btnRm.setAttribute('id', idFile );
	btnRm.setAttribute('type', 'button' );
	if(blogId !== undefined && blogId !== null && blogId !== '' && blogId !== 0){
		btnRm.setAttribute('onclick', `deleteFileContent( ${idFile}, ${blogId})` );
	} else {
		btnRm.setAttribute('onclick', `deleteFileContent( ${idFile})` );
	}
	let iconRm = btnRm.appendChild( document.createElement( 'i' ) );
	iconRm.classList.add( 'ti','ti-x');
	document.querySelector( '#content-list' ).appendChild( li );
}

function getFile( blogId ) {
	const file =  document.querySelector('#attachment').files[0];
    const rgx = /image/g;
    const fileType = rgx.test( file.type ) ? 1 : 2, extension = file.name.split('.').pop().toLowerCase()
    doAddContent( file.name, file, fileType, blogId ).then( resp => {
        /* call the callback and populate the Title field with the file name */
        if ( resp.status == 'OK' ){
          if( resp.result == "BLOG_LOCKED" ){
            setBlogToast( 'warning', 'Attention', 'Billet verrouillé !' );
          } else {
            setListFile( resp.result[1], resp.result[0].replace(/'/g, "\\'"), fileType, extension, blogId )
          }
        }
    });

 }

/* TODO : Is this is pnly used with uploadimage plugin */
 function getCroppedCanva( fieldName ){

	const currentIdBlog = document.querySelector('#id').value;
	const currentfileType = document.querySelector('#fileType') != null ? document.querySelector('#fileType').value : 1;
    const currentdHeight = document.querySelector('#dataHeightattachment').value;
    const currentdWidth = document.querySelector('#dataWidthattachment').value;
 	const $element= $('.img-container'+fieldName+' > img');
 	const resultCanva = $element.cropper('getCroppedCanvas', { width: currentdWidth, height: currentdHeight });

     resultCanva.toBlob((blob) => {
         const extension = blob.type.split('/').pop().toLowerCase();
         doAddContent( fieldName+'.png', blob, currentfileType, currentIdBlog ).then( resp => {
           /* call the callback and populate the Title field with the file name */
           if ( resp.status == 'OK' ){
             if( resp.result == "BLOG_LOCKED" ){
               setBlogToast( 'warning', 'Attention', 'Billet verrouillé !' );
             } else {
               setListFile( resp.result[1], resp.result[0], currentfileType, extension, currentIdBlog );
             }
           }
         });
     });
 };

 
function deleteFileContent( idContent, idBlog ) {
	doDeleteContent( idContent, idBlog);
	document.querySelector( `.blog-resources #doc_${idContent}`).remove();
}

async function doAddContent( fileName, result, fileType, idBlog ){
    const modal = bootstrap.Modal.getInstance(document.getElementById('imageModal'));
    if (modal) {modal.hide();}

	const formData = new FormData();

	// Convert the JSON data to a JSON string and add it to the FormData object
	formData.append( 'file', result );
    formData.append( 'fileName', fileName );
    formData.append( 'fileType', fileType );
	if(idBlog !== undefined && idBlog !== null && idBlog !== '' && idBlog !== 0) {
		formData.append( 'id', idBlog );
	}

	// Make a POST request with the FormData object
	const response = await fetch(`${baseUrl}jsp/admin/plugins/blog/DoCreateImage.jsp?action=addContent`, {
		method: 'POST',
		datatype : 'multipart/form-data',
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
	 let response;
	if(idBlog !== undefined && idBlog !== null && idBlog !== '' && idBlog !== 0) {
		response = await fetch(`${baseUrl}jsp/admin/plugins/blog/DoDeleteImage.jsp?action=removeContent&idContent=${idContent}&id=${idBlog}`);
	}
	else {
		response = await fetch(`${baseUrl}jsp/admin/plugins/blog/DoDeleteImage.jsp?action=removeContent&idContent=${idContent}`);
	}
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
	let response;
	if(idBlog !== undefined && idBlog !== null && idBlog !== '' && idBlog !== 0) {
		response = await fetch(`${baseUrl}jsp/admin/plugins/blog/DoUpdateContentType.jsp?action=updateContentType`, {
		method: 'POST',
		headers: {'Content-Type': 'application/json'},
		body: {idType: idTypeContent, idContent: idContent, id: idBlog},
	});
    }
	else {
		response = await fetch(`${baseUrl}jsp/admin/plugins/blog/DoUpdateContentType.jsp?action=updateContentType`, {
		method: 'POST',
		headers: {'Content-Type': 'application/json'},
		body: {idType: idTypeContent, idContent: idContent},
	});
	}
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
				if(data.result == "BLOG_LOCKED"){
					setBlogToast( typeWarning, labelWarning, msgErrorBlogLocked )	
				}else if( action == "moveUp" ){
					document.querySelector( `#doc_${idContent}`).after( document.querySelector( `#doc_${data.result}` ) );
				} else if( action == "moveDown" ){
					document.querySelector( `#doc_${data.result}` ).after( document.querySelector( `#doc_${idContent}` ) );
				}
	    }
		else
		{
		setBlogToast( typeDanger , labelError, msgErrorTagUpdatePosition )	
		}
	}
}
/*
 * Duplicate of the function doUpdatePriorityContent without the idBlog parameter
 * Due to a parsing error in createBlog when calling the function, the idBlog parameter is not passed
 */
async function doUpdatePriorityContentBis( idContent, action ){
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoUpdatePriorityContent.jsp?contentAction=${action}&idContent=${idContent}` );
	if ( !response.ok ) {
		setBlogToast( typeDanger , labelError, response.status )
	} else {
		const data = await response.json();
		if ( data.status == 'OK' ){
				if(data.result == "BLOG_LOCKED"){
					setBlogToast( typeWarning, labelWarning, msgErrorBlogLocked )
				}else if( action == "moveUp" ){
					document.querySelector( `#doc_${idContent}`).after( document.querySelector( `#doc_${data.result}` ) );
				} else if( action == "moveDown" ){
					document.querySelector( `#doc_${data.result}` ).after( document.querySelector( `#doc_${idContent}` ) );
				}
		  }
		else
		{
			setBlogToast( typeDanger , labelError, msgErrorTagUpdatePosition )
		}
	}
}

/*
 * Refresh the list of available tag to add
 */
async function refreshListTag()
{
    const tagSelect = document.getElementById('tag_doc');
    tagSelect.textContent = '';

    for (const tag of all_tag) {
    	if( blog_tag.indexOf(tag[0]) === -1 )
    	{
    	    const opt = document.createElement('option');
    	    opt.value = tag[0];
    	    opt.innerHTML = tag[1];
    	    tagSelect.appendChild(opt);
    	}
    }
}