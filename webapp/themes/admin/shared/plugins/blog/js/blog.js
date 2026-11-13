const typeWarning = 'warning'
const typeDanger = 'danger'
let defaultImgFileType='file-x'

async function doAddTag( idTag, tgName, blogId ) {
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoAddTag.jsp?action=addTag&tag_doc=${encodeURIComponent( idTag )}&tag_name=${encodeURIComponent( tgName )}&id=${blogId}` );
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
	if( alertRequiredTags != null ){
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
}

/*
 * Adds a tag to the list: a copy of the row rendered by the blogTagItem macro (blog_commons.html), from its <template>,
 * its placeholders __ID__ and __NAME__ filled in through the DOM. Its buttons are run by the click listener below.
 */
function setListTag( idTag, tgName, blogId ){
	const li = fillTemplate( '#blog-tag-template', { '__ID__': String( idTag ), '__NAME__': String( tgName ?? '' ) } );
	document.querySelector( "#tag-list" ).appendChild( li );
}

/*
 * Copies the first element of a <template>, its placeholders replaced in the attribute values and the texts. One pass with
 * a replacer function: a $ or a placeholder in a value is not read as a pattern. Set through the DOM, a value needs no HTML escaping.
 */
function fillTemplate( selector, values ){
	const pattern = new RegExp( Object.keys( values ).map( key => key.replace( /[.*+?^${}()|[\]\\]/g, '\\$&' ) ).join( '|' ), 'g' );
	const fill = value => value.replace( pattern, placeholder => values[ placeholder ] );
	const element = document.querySelector( selector ).content.firstElementChild.cloneNode( true );
	[ element, ...element.querySelectorAll( '*' ) ].forEach( el => {
		[ ...el.attributes ].forEach( attr => attr.value = fill( attr.value ) );
	});
	const walker = document.createTreeWalker( element, NodeFilter.SHOW_TEXT );
	while ( walker.nextNode() ) walker.currentNode.nodeValue = fill( walker.currentNode.nodeValue );
	return element;
}

/*
 * The actions of the tags and attachments rows (blogTagItem and blogDocContentItem macros), declared with data-blog-command
 */
document.addEventListener( 'click', e => {
	const el = e.target.closest( '[data-blog-command]' );
	if ( !el ) return;
	const data = el.dataset, blogId = Number( data.blogId );
	switch ( data.blogCommand ) {
		case 'tag-move':
			doUpdatePriorityTag( data.tagId, data.direction, blogId );
			break;
		case 'tag-remove':
			doDeleteTag( data.tagId, '', blogId );
			break;
		case 'content-insert':
			e.preventDefault();
			doInsertContent( data.docId, el.closest( '.blog-image' ).querySelector( '.blog-doc-content-link' ).textContent.trim( ) );
			break;
		case 'content-move':
			e.preventDefault();
			doUpdatePriorityContent( data.docId, data.direction, blogId );
			break;
		case 'content-type':
			e.preventDefault();
			setContentType( el, data.docId, data.contentType, blogId );
			break;
		case 'content-remove':
			e.preventDefault();
			deleteFileContent( data.docId, blogId );
			break;
	}
});

/* The file chosen in the attachment field is uploaded at once */
document.addEventListener( 'change', e => {
	if ( e.target.id === 'attachment' ) getFile( Number( e.target.dataset.blogId ) );
});

/*
 * Adds an uploaded attachment to the list: a copy of the row rendered by the blogDocContentItem macro (blog_commons.html),
 * from its <template>, its placeholders __ID__, __NAME__ and __EXT__ filled in. The extension is the one of the name,
 * the id of the post the one the template was rendered with.
 */
function setListFile( idFile, fileName, fileType ){
	fileName = String( fileName ?? '' );
	const extension = fileName.includes( '.' ) ? fileName.split( '.' ).pop().toLowerCase() : '';
	// No event handler of the row holds the name: "Add to content" reads it from the link
	const li = fillTemplate( '#blog-doc-content-template', { '__NAME__.__EXT__': fileName, '__EXT__': extension, '__ID__': String( idFile ) } );
	if ( isImageFile( fileName ) ) {
		const img = li.querySelector( 'img' );
		img.src = `servlet/plugins/blogs/file?id_file=${idFile}`;
		img.alt = img.title = fileName;
		img.classList.add( 'object-fit-cover' );
		li.classList.replace( 'blog-file-content', 'blog-image-content' );
	}
	showContentType( li, fileType );
	document.querySelector( '#content-list' ).appendChild( li );
}

/*
 * Shows the content type of an attachment row: the label of its badge, the check of its item in the actions menu
 */
function showContentType( li, idType ){
	li.querySelectorAll( '.blog-content-type' ).forEach( item => {
		const isCurrent = item.dataset.contentType == idType;
		item.setAttribute( 'aria-checked', isCurrent );
		item.querySelector( '.ti-check' ).classList.toggle( 'invisible', !isCurrent );
		if ( isCurrent ) {
			const label = li.querySelector( '.blog-content-type-label' );
			if ( label ) label.textContent = item.textContent.trim();
		}
	});
}

/*
 * Saves the content type chosen in the actions menu of an attachment, then shows it
 */
async function setContentType( item, idContent, idType, idBlog ){
	if ( await doUpdateContentType( idContent, idType, idBlog ) ) {
		showContentType( item.closest( '.blog-image' ), idType );
	}
}

function isImageFile( fileName ){
	return /\.(png|jpe?g|gif|webp|svg)$/i.test( fileName );
}

function getFile( blogId ) {
	const file =  document.querySelector('#attachment').files[0];
    const rgx = /image/g;
    const fileType = rgx.test( file.type ) ? 1 : 2
    doAddContent( file.name, file, fileType, blogId ).then( resp => {
        /* call the callback and populate the Title field with the file name */
        if ( resp.status == 'OK' ){
          if( resp.result == "BLOG_LOCKED" ){
            setBlogToast( 'warning', 'Attention', 'Billet verrouillé !' );
          } else {
            setListFile( resp.result[1], resp.result[0], fileType )
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
         doAddContent( fieldName+'.png', blob, currentfileType, currentIdBlog ).then( resp => {
           /* call the callback and populate the Title field with the file name */
           if ( resp.status == 'OK' ){
             if( resp.result == "BLOG_LOCKED" ){
               setBlogToast( 'warning', 'Attention', 'Billet verrouillé !' );
             } else {
               setListFile( resp.result[1], resp.result[0], currentfileType );
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

    const data = await response.text();
    if (data.trim().startsWith("<!DOCTYPE html") || data.trim().startsWith("<html")) {
        parseAdminMessageError( data );
        return { status : "KO" };
    }
    else
    {
        if (!response.ok) {
            setBlogToast( typeDanger , labelError, response.statusText )
        } else {
            const resp = JSON.parse(data);
            return resp;
        }
    }
}

function parseAdminMessageError( htmlContent ){
    const parser = new DOMParser();
    const doc = parser.parseFromString(htmlContent, "text/html");
    const message = doc.querySelector("p.text-center");
    if( message )
    {
        setBlogToast( typeDanger , labelError, message.innerText )
    }
    else
    {
        setBlogToast( typeDanger , labelError, msgErrorFileUpload )
    }
}

function doInsertContent( idContent, titleContent ) {
	let contentToInsert='', contentTag = 'p';
	const title = titleContent.replace( /[&<>"]/g, c => ( { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;' } )[ c ] );
	if ( isImageFile( titleContent ) ) {
		contentTag = 'figure'
		contentToInsert=`<img src="servlet/plugins/blogs/file?id_file=${idContent}" alt="${title}" />`
	} else {
		contentTag = 'p'
		contentToInsert=`<a title="${title}" href="servlet/plugins/blogs/file?id_file=${idContent}">${title}</a>`
	}
	if ( window.tinymce && tinymce.activeEditor ) {
		const tinyCurrentNode = tinymce.activeEditor.selection.getNode()
		tinymce.activeEditor.dom.add( tinyCurrentNode, contentTag, {}, contentToInsert);
		return;
	}
	insertIntoCoreEditor( `<${contentTag}>${contentToInsert}</${contentTag}>` );
}

/* A core editor (Autumn Note, Milkdown) is built in place of the textarea #html_content.
   Clicking the attachment menu moves the page selection out of Autumn Note, so its last selection is kept here. */
let autumnNoteBookmark = null;
document.addEventListener( 'selectionchange', () => {
	const editor = window.AutumnNote && AutumnNote.getInstance( '#html_content' );
	const editable = editor && editor.layoutInfo && editor.layoutInfo.editable;
	const selection = window.getSelection();
	if ( editable && selection.rangeCount && editable.contains( selection.anchorNode ) ) {
		autumnNoteBookmark = editor.getSelectionBookmark();
	}
});

function insertIntoCoreEditor( html ) {
	const autumnNote = window.AutumnNote && AutumnNote.getInstance( '#html_content' );
	if ( autumnNote ) {
		autumnNote.focus();
		if ( autumnNoteBookmark ) autumnNote.restoreSelectionBookmark( autumnNoteBookmark );
		// The zero-width space, which Autumn Note strips from its HTML, keeps the caret after the inserted link or figure rather than inside
		autumnNote.insertHTML( html + '&#8203;' );
		return;
	}
	// Milkdown (ProseMirror) exposes no instance: the HTML is pasted at the selection it keeps, through its own paste handling
	const proseMirror = document.querySelector( '#html_content ~ .lutece-milkdown .ProseMirror' );
	if ( proseMirror ) {
		const data = new DataTransfer();
		data.setData( 'text/html', html );
		data.setData( 'text/plain', html.replace( /<[^>]*>/g, '' ) );
		proseMirror.dispatchEvent( new ClipboardEvent( 'paste', { clipboardData: data, bubbles: true, cancelable: true } ) );
		proseMirror.focus();
	}
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

/*
 * Saves the content type of an attachment. Returns true when it is saved
 */
async function doUpdateContentType( idContent, idTypeContent, idBlog ) {
	const formData = new FormData();
	formData.append( 'idType', idTypeContent );
	formData.append( 'idContent', idContent );
	if ( idBlog !== undefined && idBlog !== null && idBlog !== '' && idBlog !== 0 ) {
		formData.append( 'id', idBlog );
	}
	const response = await fetch( `${baseUrl}jsp/admin/plugins/blog/DoUpdateContentType.jsp?action=updateContentType`, {
		method: 'POST',
		body: formData,
		cache: 'no-cache',
	});
	if ( !response.ok ) {
		setBlogToast( typeDanger, labelError, response.statusText );
		return false;
	}
	const data = await response.json();
	if ( data.status != 'OK' ) {
		setBlogToast( typeDanger, labelError, msgErrorBlogFileTypeNotUpdated );
		return false;
	}
	if ( data.result == 'BLOG_LOCKED' ) {
		setBlogToast( typeWarning, labelWarning, msgErrorBlogLocked );
		return false;
	}
	return true;
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
 * Refresh the list of available tag to add
 */
async function refreshListTag(){
    const tagSelect = document.getElementById('tag_doc');
    tagSelect.textContent = '';

    for (const tag of all_tag) {
    	if( blog_tag.indexOf(tag[0]) === -1 )
    	{
    	    const opt = document.createElement('option');
    	    opt.value = tag[0];
    	    opt.textContent = tag[1];
    	    tagSelect.appendChild(opt);
    	}
    }
}