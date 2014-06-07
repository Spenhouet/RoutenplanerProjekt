package de.dhbw.horb.routePlanner.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXDefaultHandler extends DefaultHandler 
{
	public  String[]     args;
	public  StringBuffer sbResult = new StringBuffer();
	private int          iState   = 0;

	@Override
	public void startElement( String namespaceURI,
	                          String localName,
	                          String qName,
	                          Attributes attrs )
	throws SAXException
	{
		if( 5 <= iState )  return;
	    String eName = ( "".equals( localName ) ) ? qName : localName;
	    if( null != eName )
	    {
	    	if( eName.equals( args[1] ) )                 // <ParentElem>
	    		iState = 1;
	    	if( 1 == iState && eName.equals( args[2] ) )  // <ChildElem>
	    		iState = 2;
	    	if( 4 == iState )
	    		sbResult.append( "<" + eName + ">" );
	    	if( 3 == iState && eName.equals( args[4] ) )  // <DataElem>
	    		iState = 4;
	    }
	}

	@Override
	public void endElement( String namespaceURI,
	                        String localName,
	                        String qName )
	throws SAXException
	{
		if( 5 <= iState )  return;
		
	    String eName = ( "".equals( localName ) ) ? qName : localName;
	    if( null != eName )
	    {
	    	if( eName.equals( args[1] ) )                 // <ParentElem>
	    		iState = 0;
	    	if( 2 == iState && eName.equals( args[2] ) )  // <ChildElem>
	    		iState = 1;
	    	if( 4 == iState && eName.equals( args[4] ) )  // <DataElem>
	    		iState = 5;
	    	if( 4 == iState )
	    		sbResult.append( "</" + eName + ">" );
	    }
	}

	@Override
	public void characters( char[] buf, int offset, int len )
	throws SAXException
	{
		if( 5 <= iState )  return;
	    
		String s = new String( buf, offset, len );
	    if( 2 == iState && s.equals( args[3] ) )        // <FindText>
	    	iState = 3;
	    if( 4 == iState )
	    	sbResult.append( s );
	}
}
