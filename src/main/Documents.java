package main;

public class Documents 
{
	private String title;
	private String description;
	private String url;
	
	Documents()
	{
		title ="";
		description ="";
		url="";
	}
	
	Documents(String t, String d, String u)
	{
		title = t;
		description = d;
		url = u;
	}
	
	String getTitle()
	{
		return title;
	}
	
	String getDescription()
	{
		return description;
	}
	
	String getUrl()
	{
		return url;
	}
	
	boolean equals(Documents d)
	{
		if(title.equalsIgnoreCase(d.getTitle()) && description.equalsIgnoreCase(d.getDescription()) && url.equalsIgnoreCase(d.getUrl()))
			return true;
		return false;
	}
}
