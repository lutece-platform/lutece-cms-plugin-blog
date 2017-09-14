var baseUrl = document.getElementsByTagName('base')[0].href;

function removeEntry(idEntry, tgName){
	$('#tag_remove').val(idEntry);
	doDeleteTag(idEntry, tgName);
}

function addIdEntry(){
	
	doAddTag($('select#tag_doc').val(),$('#tag_doc option:selected').text());
}


function doAddTag( idTag, tgName )

{

	$.ajax({

            url : baseUrl + "jsp/admin/plugins/htmldocs/DoAddTag.jsp?action=addTag&tag_doc="+idTag+"&tag_name="+tgName,

	    type: 'GET',

	    dataType: "json",

	    data: {},

            async: false,

	    cache:false,

            success:function(data) {

        	if ( data.status == 'OK' )

    		{
			$( "#tag" ).append("<div id="+idTag+"> "+tgName+"<button class='btn btn-primary btn-xs btn-flat'  onClick= doUpdatePriorityTag("+idTag+",'"+"moveDown"+"') title='Descendre' ><i class='glyphicon glyphicon-arrow-down'></i></button><button class='btn btn-primary btn-xs btn-flat'  title='Monter' onClick= doUpdatePriorityTag("+idTag+",'"+"moveUp"+"')><i class='glyphicon glyphicon-arrow-up'></i></button>"+"<button id='"+idTag+"' type='submit' value='removeTag' name='removeTag' class='btn btn-danger btn-small' onClick=removeEntry("+idTag+",'"+tgName+"');><i class='fa fa-trash'></i>&nbsp;</button> </div> ");

			$('#tag_doc option:selected').detach();
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
    
function doDeleteTag( idTag, tgName  )

{

	
	$.ajax({

            url : baseUrl + "jsp/admin/plugins/htmldocs/DoDeleteTag.jsp?action=removeTag&tag_doc="+idTag,

	    type: 'GET',

	    dataType: "json",

	    data: {},

            async: false,

	    cache:false,

            success:function(data) {

        	if ( data.status == 'OK' )

    		{
			$('#'+idTag).detach();
			$('#tag_doc').append("<option value="+idTag+">"+tgName+"</option>")
			
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


function doUpdatePriorityTag( idTag, action )

{

	$.ajax({

            url : baseUrl + "jsp/admin/plugins/htmldocs/DoUpdatePriority.jsp?tagAction="+action+"&tag_doc="+idTag,

	    type: 'GET',

	    dataType: "json",

	    data: {},

            async: false,

	    cache:false,

            success:function(data) {

        	if ( data.status == 'OK' )

    		{
			if( action == "moveUp" ){

				$('#'+data.result).insertAfter($('#'+idTag));

			}else if( action == "moveDown" ){

				$('#'+idTag ).insertAfter($('#'+data.result));
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
 