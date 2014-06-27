package de.dhbw.horb.routePlanner.parser;


import java.io.File;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.graphData.Node;

public class GraphDataDom {

	public GraphDataDom() {

	}

	public void addKM() {

		try {
			// ---- Read XML file ----
			SAXBuilder builder = new SAXBuilder();
			Document wayXml = builder.build(new File(Constants.XML_WAY_HIGHWAY)); // <XmlFile>
			Document nodeXml = builder.build(new File(Constants.XML_NODE_HIGHWAY)); // <XmlFile>
			// ---- Modify XML data ----
			Element wayRoot = wayXml.getRootElement();
			 java.util.List<Element> listParentElements = wayRoot.getChildren("way"); // <Elem>
			 for( int i=0; i<listParentElements.size(); i++ )
			 {
			 // Find searched element with given attribute:
			 Element elMain = (Element)(listParentElements.get( i ));
			 if( null == elMain ) continue;
//			 String s = elMain.getAttributeValue( args[3] ); // <AttrS>
//			 if( null == s || !s.equals( args[4] ) ) continue; // <ValS>
			 // Add new attribute to correct element:
//			 elMain.setAttribute( args[5], args[6] ); // <AttrNew>=<ValN>
			// // ---- Write result ----
			// XMLOutputter outp = new XMLOutputter();
			// outp.setFormat( Format.getPrettyFormat() );
			// // ---- Show the modified element on the screen ----
			// System.out.println( "\nModified Element:\n" );
			// outp.output( elMain, System.out );
			// System.out.println();
			// // ---- Write the complete result document to XML file ----
			// outp.output( doc, new FileOutputStream( new File( args[1] ) ) );
			// break; // <NewFile>
			 }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public Node getNode(Long id){
		
		
		
		return null;
	}
}
