var baseUrl = document.getElementsByTagName('base')[0].href;

$(function () {
  $("#style").parent().toggle();

  var isAdded= true;
  var isSorted= true;

  var $docs = $("#docs"), $pubs = $("#published_docs");

  $("#published_docs").sortable({
  	connectWith: ".connectedSortable",
  	stop: function(e, ui){
	  if(isSorted || isAdded ){
	    doAddDocument( ui.item.attr("id"), eval( ui.item.index()) );
	    isAdded= false;
	   }
	    isSorted= true;
    }
  });

  $docs.sortable({
  	connectWith: ".connectedSortable",
  	stop: function(e, ui){
	  if(!isSorted && isAdded){
		    doAddDocument( ui.item.attr("id"), eval( ui.item.index()) );
		}
		isSorted= true;
  	}
  });

  $pubs.droppable({
    accept: "#docs > a",
    classes: {
      "ui-droppable-active": "active"
    },
    drop: function (event, ui) {
    isAdded= true;
	   isSorted= false;
	  }
  });

  // Let the gallery be droppable as well, accepting items from the trash
  $docs.droppable({
    accept: "#published_docs a",
    classes: {
      "ui-droppable-active": "active"
    },
    drop: function (event, ui) {
      ui.draggable.fadeOut(function () {
      ui.draggable
        .appendTo( $docs )
        .fadeIn();
      });
  	isAdded= false;
  	isSorted= false;
    doDeleteDocument(ui.draggable.attr("id"));
    }
  });
});

function doAddDocument(idDocument, orderDocument) {
  $.ajax({
    url: baseUrl + "jsp/admin/plugins/blog/DoAddDocument.jsp?idDocument=" + idDocument + "&orderDocument=" + orderDocument + "&action=add",
    type: 'GET',
    dataType: "json",
    data: {},
    async: false,
    cache: false,
    success: function (data) {
      if (data.status == 'OK') {
        // alert("success")
      }  else {
          alert("echec")
      }
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("error")
    }
  });
}

function doDeleteDocument(idDocument) {
  $.ajax({
    url: baseUrl + "jsp/admin/plugins/blog/DoAddDocument.jsp?idDocument=" + idDocument + "&action=remove",
    type: 'GET',
    dataType: "json",
    data: {},
    async: false,
    cache: false,
    success: function (data) {
      if (data.status == 'OK') {
        //alert("success")
      } else {
        alert("echec")
      }
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("error")
    }
  });
}
