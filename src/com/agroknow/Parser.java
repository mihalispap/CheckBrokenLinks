package com.agroknow;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

public class Parser 
{
	String to_write="";
	String rights="";
	String enrichments="";
	String prefix="";
	List<String> stack=new ArrayList<String>();
	List<String> rights_list=new ArrayList<String>();
	
	List<String> coverages=new ArrayList<String>();
	
	boolean has_rights=false;
	
	public void generate(String filename)
	{
		try {

			
        	File fXmlFile = new File(filename);
        	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        	
        	//Document doc = dBuilder.parse(fXmlFile);
        		
        	FileInputStream in = new FileInputStream(fXmlFile);
        	Document doc = dBuilder.parse(in, "UTF-8");
        	
        	
        	//optional, but recommended
        	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        	doc.getDocumentElement().normalize();
        	
        	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        	//System.out.println("ToString method call:" + doc.getDocumentElement().getTextContent());
        			
        	NodeList nList = doc.getElementsByTagName("response");
        	
        	System.out.println("----------------------------");

        	for (int temp = 0; temp < nList.getLength(); temp++) 
        	{
        		System.out.println(nList.item(temp).getNodeName());
        		
        		Node nNode = nList.item(temp);
        		
        		if (nNode.getNodeType() == Node.ELEMENT_NODE) 
        		{

        			Element eElement = (Element) nNode;  			
        			
        			NodeList node_list = eElement.getChildNodes();
        			for(int i=0;i<node_list.getLength();i++)
        			{
        				Node node=node_list.item(i);
        				if (node.getNodeType() == Node.ELEMENT_NODE) 
                		{
        					//write(node, only_filename, output);
	        				Element element = (Element) node;
	        				
	        				System.out.println(element.getTagName());
	        				
	        				if(element.getTagName().equals("result"))
	        				{
	        					NodeList resource_list=element.getChildNodes();
		        				for(int j=0;j<resource_list.getLength();j++)
		        				{
		        					Node resource=resource_list.item(j);
		        					if (resource.getNodeType() == Node.ELEMENT_NODE) 
		                    		{
			            				Element doc_element = (Element) resource;
			            				
			            				check(doc_element);
			            				
		                    		}
		        				
		        				}
	        				}
                		}
        			}
        		}
        	}
        	
        	System.out.println("I processed:"+count);
        	in.close();
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
		
          
	}
	
	private void check(Element element)
	{
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		try {
			XPathExpression expr = xpath.compile("str[@name=\"ARN\"]");
			
			String arn = expr.evaluate(element);
			
			expr = xpath.compile("arr[@name=\"fulltext\"]/str");
			String url = expr.evaluate(element);
			
			if(!getResponseCode(url))
				System.out.println("INEXISTENT:"+arn+", with url="+url);
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.count++;
	}
	
	public int count=0;
	
	public static boolean exists(String URLName){
	    
		
		
		if(URLName.isEmpty())
			return false;
		
		try {
	      HttpURLConnection.setFollowRedirects(true);
	      // note : you may also need
	      //        HttpURLConnection.setInstanceFollowRedirects(false)
	      HttpURLConnection con =
	         (HttpURLConnection) new URL(URLName).openConnection();
	      con.setRequestMethod("HEAD");
	      
	      System.out.println(con.getResponseCode());
	      
	      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	    }
	    catch (Exception e) {
	       //e.printStackTrace();
	       return false;
	    }
	  }  
	
	
	public static boolean getResponseCode(String urlString){
		try
		{
		    URL u = new URL(urlString); 
		    HttpURLConnection huc =  (HttpURLConnection)  u.openConnection(); 
		    huc.setRequestMethod("GET"); 
		    huc.connect(); 
		    
		    System.out.println(urlString+" "+huc.getResponseCode());
		    
		    return (huc.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
}















