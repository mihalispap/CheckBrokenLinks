package com.agroknow;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Mihalis
 *
 */
public class CheckBroken {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*AKSPARQL aksparql=new AKSPARQL();
		
		System.out.println(aksparql.queryFAOGeo("Africa"));
		
		if(true)
			return;
		*/
		if (args.length != 1) {
            System.err.println("Usage: param1(inputfile)");                
            System.exit(1);
        } 
		
		String input=args[0];


        Parser handler=new Parser();
        
        
        handler.generate(input);
        
          
	}
        

		

}
