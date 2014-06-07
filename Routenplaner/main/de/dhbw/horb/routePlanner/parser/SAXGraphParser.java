package de.dhbw.horb.routePlanner.parser;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SAXGraphParser {

	public void main( String[] args )
	{
		if( args.length != 5 )
	    {
			System.err.println( "Usage:" );
			System.err.println( "java ExampleSaxGetData <XmlFile> "
	                        + "<ParentElem> <ChildElem> <FindText> <DataElem>" );
			System.err.println( "Example:" );
			System.err.println( "java ExampleSaxGetData MyXmlFile.xml "
	                        + "Button Title \"Mein dritter Button\" Comment" );
			System.exit( 1 );
	    }
	    try {
	    	SAXDefaultHandler handler = new SAXDefaultHandler();
	    	handler.args = args;
	    	SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
	    	saxParser.parse( new File( args[0] ), handler );
	    	if( 0 < handler.sbResult.length() )
	    	{
	    		System.out.println( "Result string in child element '<"
	                            + args[4] + ">':" );
	    		System.out.println( handler.sbResult );
	    		System.exit( 0 );
	    	}
	    	else
	    	{
	    		System.out.println( "No fitting element found." );
	    		System.exit( 2 );
	    	}
	    } catch( Throwable t ) {
	    	t.printStackTrace();
	    	System.exit( 3 );
	    }
	}
}
