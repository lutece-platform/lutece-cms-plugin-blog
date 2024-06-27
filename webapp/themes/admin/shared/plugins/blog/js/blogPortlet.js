

async function doUpdateBlogList( baseUrl, searchText, itemPerPage ) {
    const response = await fetch(`${baseUrl}jsp/admin/plugins/blog/DoSearchPortletList.jsp?search_text=${searchText}&items_per_page=${itemPerPage}&button_search=true`)
    if ( !response.ok ) {
        return;
    } else {
        const data = await response.json();
        if ( data.status == 'OK' ){
            return updateBlogList( data.result.blog_list )
        }
    }
}

function updateBlogList( blog_list ) {
    const result = [];
    const content =  document.getElementById( 'doc-available' );
    content.innerHTML = "";
    blog_list.forEach( ( blog ) => {
        let li = content.appendChild( document.createElement( 'li' ) );
        li.classList.add( 'list-group-item', 'list-group-item-action', 'bg-gray-100','border', 'border-gray-300', 'min-h-40', 'd-flex', 'justify-content-between');
      	li.setAttribute( 'id', `${blog.id}` );
      	li.setAttribute( 'draggable', 'true' );
      	li.setAttribute( 'tabindex', '0' );
      	let span = li.appendChild( document.createElement( 'span' ) );
      	span.textContent = `${blog.contentLabel}`;
      	let icon = li.appendChild( document.createElement( 'i' ) );
      	icon.classList.add( 'ti', 'ti-arrows-move', 'ms-auto');
      	icon.setAttribute( 'aria-hidden', 'true' );
        content.appendChild( li );
        result.push(li);
    })
    return result;
}
