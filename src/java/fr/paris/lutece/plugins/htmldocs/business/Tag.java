package fr.paris.lutece.plugins.htmldocs.business;

public class Tag {
	
     private int _nIdTag;
	 private String _strName;

     
	 
     public Tag(){
		 
		 
	 } 
	 public Tag(int nIdTag){
		 
		 this.setIdTag(nIdTag);
	 }
     /**
      * Returns the _nIdTag
      *
      * @return The_nIdTag
      */
     public int getIdTag(  )
     {
         return _nIdTag;
     }

     /**
      * Sets the nIdTag
      *
      * @param nIdTag The nIdTag
      */
     public void setIdTag( int nIdTag )
     {
    	 _nIdTag = nIdTag;
     }
    
     /**
      * Sets the Name
      * @param Name The value
      */
     public void setName( String strName )
     {
         _strName = strName;
     }

     /**
      * Gets the value
      * @return The value
      */
     public String getName(  )
     {
         return _strName;
     }
     


}
