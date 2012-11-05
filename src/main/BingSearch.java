package main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

//THIS CLASS IS USED TO OBTAIN THE NUMBER OF DOCUMENTS AND THE TOP 4 RESULT FOR EACH OF THE QUERY WHICH IS PASSS AS A PARAMETER TO IT

public class BingSearch
{
	//TO SAVE THE XML FILE RETURNED BY BING
	private static String content="";
	
	//TO SAVE THE TOTAL NUMBER OF DOCUMENTS RETRIEVED FOR THE QUERY
	private static Elements total;
	
	//TO SAVE TITLES OF RETRIEVED DOCUMENTD OUT OF WHICH A MAXIMUM OF 4 ARE USED
	private static Elements title;
		
	//TO SAVE DESCRIPTION OF RETRIEVED DOCUMENTD OUT OF WHICH A MAXIMUM OF 4 ARE USED
	private static Elements description;
		
	//TO SAVE URLS OF RETRIEVED DOCUMENTD OUT OF WHICH A MAXIMUM OF 4 ARE USED
	private static Elements siteurl;
	
	// TO SAVE THE URL THAT IS PASSED IN ORDER TO GET THE RESULTS FROM BING
	private static String bingUrl="";
	
	//TO SAVE THE ACCOUNT KEY FOR BING
	private static String accountKey="";
	
	//RETURNS THE BING URL USED FOR THE QUERY
	String getBingUrl()
	{
		return bingUrl;
	}
	
	//RETURNS THE BING ACCOUNT KEY USED
	String getAccountKey()
	{
		return accountKey;
	}

	//THIS FUNCTION RETURNS A LIST OF THE TOP 4 DOUMENTS RETRIEVED AND THE TOTAL NUMBER OF DOCUMENTS RETRIEVED FOR THE QUERY PASSES AS THE PARAMETER
	//IT RETRIEVES THE DOCUMENTS FOR THE SPECIFIED WEB DATABASE 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static Vector getDocumentsFromBing(String web_database_url , String query ) throws IOException
	{
		Vector result = new Vector();
		bingUrl = "https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Composite?Query=%27site%3a"+web_database_url+"%20"+query+"%27&$top=10&$format=Atom"; 
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
		Document doc = Jsoup.parse(content,"",Parser.xmlParser());
		total= doc.select("d|WebTotal");
		title = doc.select("d|title");
		description = doc.select("d|description");
		siteurl = doc.select("d|url");
		LinkedList<Documents> docs_for_query= new LinkedList<Documents>();
		for(int i =0;i<4 && i<title.size();i++)
		{
			Documents d= new Documents(title.get(i).text(),description.get(i).text(),siteurl.get(i).text());
			docs_for_query.add(d);
		}
		result.add(docs_for_query);
		if(total.isEmpty())
		{
			System.out.println("EMPTY");
			result.add(0);
		}
		else
		{
			result.add(total.text());
		}
		return result;
	}
}