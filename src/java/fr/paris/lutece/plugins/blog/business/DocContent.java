package fr.paris.lutece.plugins.blog.business;

public class DocContent {
	
     private int _nIdDocContent;
     private int _nIdHtmlDocument;
	 private String _strTextValue;
     private byte[] _bytes;
     private String _strValueContentType;
     
     /**
      * Returns the IdDocContent
      *
      * @return The _nIdDocContent
      */
     public int getId(  )
     {
         return _nIdDocContent;
     }

     /**
      * Sets the _nIdDocContent
      *
      * @param nIdDocumentContent The _nIdDocContent
      */
     public void setId( int nIdDocumentContent )
     {
    	 _nIdDocContent = nIdDocumentContent;
     }
     /**
      * Returns the _nIdDocument
      *
      * @return The _nIdDocument
      */
     public int getIdHtmlDocument(  )
     {
         return _nIdHtmlDocument;
     }

     /**
      * Sets the _nIdDocContent
      *
      * @param nIdDocumentContent The _nIdDocContent
      */
     public void setIdHtmlDocument( int nIdDocument )
     {
    	 _nIdHtmlDocument = nIdDocument;
     }
     /**
      * Sets the value
      * @param strTextValue The value
      */
     public void setTextValue( String strTextValue )
     {
         _strTextValue = strTextValue;
     }

     /**
      * Gets the value
      * @return The value
      */
     public String getTextValue(  )
     {
         return _strTextValue;
     }

     /**
      * Sets the value
      * @param bytes The value
      */
     public void setBinaryValue( byte[] bytes )
     {
         _bytes = bytes;
     }

     /**
      * Gets the value
      * @return The value
      */
     public byte[] getBinaryValue(  )
     {
         return _bytes;
     }

     /**
      * Sets the content type value
      * @param strValueContentType The content type value
      */
     public void setValueContentType( String strValueContentType )
     {
         _strValueContentType = strValueContentType;
     }

     /**
      * Gets the content type value
      * @return The content type value
      */
     public String getValueContentType(  )
     {
         return _strValueContentType;
     }
     
     


}
