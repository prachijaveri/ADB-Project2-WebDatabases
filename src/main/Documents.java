package main;

import java.util.Set;

//THIS CLASS IS USED TO SAVE THE RELEVANT DOCUMENTS RETRIEVED FOR EACH OF THE QUERY EXECUTED
//EACH RELEVANT DOCUMENT RETRIEVED FOR EACH QUERY OF EACH CLASSIFIED CATEGORY IS SAVED AS AN INSTANCE OF THIS CLASS AND THEN ADDED TO A LIST IN THE CORRESPONDING CATEGORY 
//IT ALSO SAVES THE CONTENT OF EACH OF THE RETRIEVED DOCUMENTS
public class Documents 
{
	//SAVES THE TITLE OF THE RETRIEVED DOCUMENT
	private String title;
	
	//SAVES THE DESCRIPTION OF THE RETRIEVED DOCUMENT
	private String description;
	
	//SAVES THE URL OF THE RETRIEVED DOCUMENT
	private String url;
	
	//SAVES THE SET OF DINSTINCT WORDS IN THE CONTENT OF THE SPECIFIED PAGE
	private Set<String> document_words;
	
	//CONSTRUCTOR FOR THE CLASS
	Documents()
	{
		title ="";
		description ="";
		url="";
	}
	
	//CONSTRUCTOR IS USED TO INITIALIZE THE VALUES OF THE INSTANCE
	Documents(String t, String d, String u)
	{
		title = t;
		description = d;
		url = u;
	}
	
	//RETURNS THE TITLE OF THE DOCUMENT
	String getTitle()
	{
		return title;
	}
	
	//RETURNS THE DESCRIPTION OF THE DOCUMENT
	String getDescription()
	{
		return description;
	}
	
	//RETURNS THE URL OF THE DOCUMENT
	String getUrl()
	{
		return url;
	}
	
	//CONPARES THE CALLING INSTANCE WITH THE INSTANCE PASSES AS A PARAMETER TO CHECK IF THEY PROVIDE THE SAME URL
	//USED TO REMOVE THE DUPLICATE PAGES
	boolean equals(Documents d)
	{
		if(url.equalsIgnoreCase(d.getUrl()))
			return true;
		return false;
	}
	
	//USED TO CALL THE RUNlYX FUNCTION FOR THE DOCUMENT TO GET ITS SET OF DISTINCT WORDS
	Set<String> getWordsOfDocument()
	{
		System.out.println("Fetching content of page : "+url);
		document_words= WordsLynx.runLynx(url);
		return document_words;
	}
}
