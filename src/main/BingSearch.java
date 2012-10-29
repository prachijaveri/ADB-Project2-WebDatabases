package main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;

//CLASS TO GET THE QUERY RESULTS FOM BING SEARCH ENGINE
public class BingSearch
{
	//TO SAVE THE XML FILE RETURNED BY BING
	private String content="";
	
	//TO SAVE TITLES OF TOP 10 RESULTS
	//private Elements title;
	
	//TO SAVE DESCRIPTION OF THE TOP 10 RESULTS
	//private Elements description;
	
	//TO SAVE THE URL OF THE TOP 10 RESULTS
	//private Elements siteurl;
	
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
}
