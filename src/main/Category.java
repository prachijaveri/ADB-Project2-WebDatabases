package main;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class Category 
{
	private String category_name;
	private LinkedList<String> list_of_queries=new LinkedList<String>();
	private LinkedList<Documents> list_of_documents;
	private double specificity = -1;
	private double coverage = -1;
	private Category parent;
	private LinkedList<Category> children = new LinkedList<Category>();
	private double total_no_docs = 0.0;
	
	Category()
	{
		category_name="root";
		parent=null;
	}
	
	Category(String s, Category p)
	{
		category_name=s;
		parent = p;
		if(p != null)
			p.addChild(this);
	}
	
	void addChild(Category c)
	{
		children.add(c);
	}
	
	void increaseTotal(double n)
	{
		total_no_docs+=n;
	}
	
	String getCategoryName()
	{
		return category_name;
	}
	
	int getNumberOfQueries()
	{
		return list_of_queries.size();
	}
	
	String getQueryAtIndex(int i)
	{
		return list_of_queries.get(i);
	}
	int getNumberOfChildren()
	{
		return children.size();
	}
	boolean isChild(String name)
	{
		for(int i=0;i<children.size();i++)
		{
			if(name.equalsIgnoreCase(children.get(i).getCategoryName()))
				return true;
		}
		return false;
	}
	
	Category getChildByName(String name)
	{
		for(int i=0;i<children.size();i++)
		{
			Category c = children.get(i);
			if(name.equalsIgnoreCase(c.getCategoryName()))
				return c;
		}
		return null;
	}
	Category getChildByIndex(int i)
	{
		if(i< children.size())
			return children.get(i);
		return null;
	}
	void setQueries(String query_url)
	{
		try
		{
			URL url = new URL(query_url);
			URLConnection urlConnection = url.openConnection();					
			InputStream inputStream = (InputStream) urlConnection.getContent();			
			byte[] contentRaw = new byte[urlConnection.getContentLength()];
			inputStream.read(contentRaw);
			String content = new String(contentRaw);
			for(String x:content.split("\n"))
				list_of_queries.add(x);
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
	}
	
	void addDocuments(LinkedList<Documents> docs_for_query)
	{
		for(int i=0;i<docs_for_query.size();i++)
		{
			boolean flag = true;
			for(int j=0;j<list_of_documents.size();j++)
			{
				if(docs_for_query.get(i).equals(list_of_documents.get(j)))
				{
					flag=false;
					break;
				}
			}
			if(flag)
				list_of_documents.add(docs_for_query.get(i));
		}
	}
	void setCoverage(double n)
	{
		coverage = n;
	}
	
	void setSpecificity(double n)
	{
		specificity = n;
	}
	
	double getTotalNoDocs()
	{
		return total_no_docs;
	}
	double getSpecificity()
	{
		return specificity;
	}
	double getCoverage()
	{
		return coverage;
	}
	void setTotalNoDocs(double n)
	{
		total_no_docs = n;
	}
}
