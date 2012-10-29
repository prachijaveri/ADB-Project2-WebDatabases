package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

//CLASS TO GET THE QUERY RESULTS FOM BING SEARCH ENGINE
public class BingSearch
{
	//TO SAVE THE XML FILE RETURNED BY BING
	private String content="";
	
	//TO SAVE TITLES OF TOP 10 RESULTS
	private Elements title;
	
	//TO SAVE DESCRIPTION OF THE TOP 10 RESULTS
	private Elements description;
	
	//TO SAVE THE URL OF THE TOP 10 RESULTS
	private Elements siteurl;
	
	//SAVES THE URL OF THE RELEVANT DOCUMENT TO GET THE CONTENT OF THAT WEBPAGE
	private URL result_url;
	
	// TO SAVE THE URL USED TO GET THE RESULTS
	private String bingUrl="";
	
	//TO SAVE THE ACCOUNT KEY
	private String accountKey="";
	
	//RETURNS THE BING URL USED FOR THE QUERY
	String getBingUrl()
	{
		return bingUrl;
	}
	
	//RETURNS THE ACCOUNT KEY
	String getAccountKey()
	{
		return accountKey;
	}
	
	//TO GET THE XML FILE CONTAINING THE TOP 10 RESULTS OF THE QUERY
	void getXMLResult(String query) throws IOException
	{
		bingUrl = "https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Web?Query=%27"+query+"%27&$top=10&$format=Atom"; 
		accountKey = "cl+CGEC5TNbMOpk+QOGLlbwLXAihfnwscJZRQdmNDDE=";
		
		byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
		String accountKeyEnc = new String(accountKeyBytes);

		URL url = new URL(bingUrl);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
				
		InputStream inputStream = (InputStream) urlConnection.getContent();		
		byte[] contentRaw = new byte[urlConnection.getContentLength()];
		inputStream.read(contentRaw);
		content = new String(contentRaw);
	}
	
	//TO PARSE THE XML FILE CONTAINING THE TOP 10 RESULTS TO GET THE TITLE, DESCRIPTION AND URL FOR EACH RESULT
	void parseResults()
	{
		Document doc = Jsoup.parse(content,"",Parser.xmlParser());
		title = doc.select("d|title");
		if( title.isEmpty() || title.size() < 10)
		{
			System.err.println("NO DOCUMENTS WERE FOUND");
			System.exit(0);
		}
		description = doc.select("d|description");
		siteurl = doc.select("d|url");
	}
	
	//TO PARSE THE HTML FILE OBTAINED FOR EACH OF THE RELEVANT DOCUMENT
	String parseUrlResult(String u) throws IOException
	{
		String body_content="";
		String url_text="";
		result_url = new URL(u);
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(result_url.openStream()));
			String str;
			while ((str = in.readLine()) != null) 	
			{
				url_text=url_text+" "+str;
			}
			in.close();
			Document doc = Jsoup.parse(url_text);
			Elements body = doc.select("p");
			for(int i=0; i<body.size();i++)
			{
				body_content += (" "+body.get(i).text());
			}
		}
		catch(Exception e)
		{
		}
	    return body_content;
	}
	
	//TO PRINT THE TOP 10 RESULTS AND CALCULATE THE INVERTED FILES AND GET RELEVANCE FEEDBACK FOR EACH RESULT
	void displayResults(RelevanceFeedback rel_feedback, InvertedFiles invert_files) throws IOException
	{
		for(int i=0;i<10;i++)
		{
			System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println("TITLE : " +title.get(i).text());
			System.out.println("DESCRIPTION : " +description.get(i).text());
			System.out.println("URL : " +siteurl.get(i).text());
			System.out.println();
			boolean b=rel_feedback.getFeedback(i);
			result_url = new URL(siteurl.get(i).text());
			String body_content="";
			if(b)
			{
				body_content=parseUrlResult(siteurl.get(i).text());
			}	
			invert_files.getInvertedFiles(title.get(i).text()+" "+description.get(i).text()+" "+body_content,i);
		}
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}
}
