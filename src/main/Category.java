package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

//EACH CATEGORY IN THE CLASSIFICATION TREE IS SAVED AS AN INSTANCE OF THIS CLASS
//EACH CATEGORY HAS A PARENT AND A LIST OF CHILDREN ASSOCIATEDWITH ITSELF
//THE ENTIRE CLASSIFICATION IS SAVED AS A TREE STRUCTURE WHICH IS OBTAINED FROM THE QUERIES SPECIFIED AT EACH NODE
public class Category 
{
	//SAVES THE CATEGORY NAME
	private String category_name;
	
	//CONTAINS THE QUERIES THAT HAVE TO BE EXECUTED FOR THE CATEGORY
	private LinkedList<String> list_of_queries=new LinkedList<String>();
	
	//CONTAINS THE NUBER OF DOCUMENTS THAT IS RETRIEVED FOR EACH QUERY 
	private LinkedList<Integer> number_of_documents_for_query = new LinkedList<Integer>();
	
	//CONTAINS THE RETRIEVED DOCUMENTS FOR ALL QUERIES
	private LinkedList<Documents> list_of_documents= new LinkedList<Documents>();
	
	//SAVES THE SPECIFICITY FOR THE CATEGORY
	private double specificity = -1;
	
	//SAVES THE COVERAGE FOR THE CATEGORY
	private double coverage = -1;
	
	//CONATINS A POINTER TO THE PARENT OF THIS CATEGORY
	//IT IS NULL IN CASE OF THE ROOT
	private Category parent;
	
	//CONTAINS THE LIST OF CHILDREN FOR THIS CATEGORY
	//IT HAS A SIZE OF ZERO IN CASE THE CATEGORY IS A LEAF IN THE TREE
	private LinkedList<Category> children = new LinkedList<Category>();
	
	//CONTAINS THE NUMBER OF RELEVANT DOCUMENTS THAT IS REQUIRED FOR CALCULATING THE COVERAGE AND SPECIFICITY
	private double total_no_docs = 0.0;
	
	//CONSTRUCTOR USED WHEN CREATING THE ROOT CATEGORY
	Category()
	{
		category_name="root";
		parent=null;
	}
	
	//CONSTRUCTOR USED FOR CATEGORIES EXCEPT THE ROOT
	//IT ASSIGNS A CATEGORY NAME AND THE PARENT TO THE CATEGORY
	Category(String s, Category p)
	{
		category_name=s;
		parent = p;
		if(p != null)
			p.addChild(this);
	}
	
	//RETURNS A LIST OF ALL THE CHILDREN OF THE CATEGORY
	LinkedList<Category> getChildren()
	{
		return children;
	}
	
	//RETURNS THE PARENT OF THE CATEGORY
	Category getParent()
	{
		return parent;
	}
	
	//ADDS A NEW CHILD TO THE LIST OF CHILDREN FOR THIS CATEGORY
	void addChild(Category c)
	{
		children.add(c);
	}
	
	//INCREASES THE NUMBER OF DOCUMENTS WHEN A NEW QUERY IS EXECUTED
	void increaseTotal(double n)
	{
		total_no_docs+=n;
	}
	
	//METHOD TO GET THE NAME OF THE CATEGORY
	String getCategoryName()
	{
		return category_name;
	}
	
	//RETURNS THE NUMBER OF QUERIES
	int getNumberOfQueries()
	{
		return list_of_queries.size();
	}
	
	//RETURNS THE QUERY AT INDEX I
	String getQueryAtIndex(int i)
	{
		return list_of_queries.get(i);
	}
	
	//RETURNS THE NUMBER OF CHILDREN
	int getNumberOfChildren()
	{
		return children.size();
	}
	
	// METHOD TO CHECK IF THE INPUT PARAMETER IS A CHILD OF THE CATEGORY 
	boolean isChild(String name)
	{
		for(int i=0;i<children.size();i++)
		{
			if(name.equalsIgnoreCase(children.get(i).getCategoryName()))
				return true;
		}
		return false;
	}
	
	//METHOD TO RETURN THE CHILD BY NAME
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
	
	//METHOD TO RETURN THE CHILD BY INDEX
	Category getChildByIndex(int i)
	{
		if(i< children.size())
			return children.get(i);
		return null;
	}
	
	//METHOD TO EXTRACT THE QUERIES FROM THE CORRESPONDING PAGE AND ADD THEM TO THE LIST OF QUERIES FOR A CATEGORY
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
	
	//METHOD THAT GETS THE LIST OF WORDS FOR THE DOCUMENTS OF THE CATEGORY
	//IT COMBINES THE DINSTICT WORDS GOT FOR EACH DOCUMENT AND THEN CALCUATES THE DOCUMENT FREQUENCY OF EACH WORD
	//IT WRITES THE WORD AND IT DOCUMENT FREQUENCY TO A FILE CREATED
	void getWordSetForAll() throws IOException
	{
		try
		{
			System.out.println("Generating Content Summary for "+category_name.toUpperCase());
			ArrayList<String> total_words = new ArrayList<String>();
			int index=0;	
			for(int i=0 ; i< number_of_documents_for_query.size(); i++)		
			{
				System.out.println("Documents for Query "+i+" of "+getNumberOfQueries()+" ---> "+ list_of_queries.get(i));
				for(int j=0;j<number_of_documents_for_query.get(i);j++)
				{
					Documents d = list_of_documents.get(index);
					Set<String> words = d.getWordsOfDocument();
					total_words.addAll(words);
					index++;
				}
			}
			FileWriter fstream = new FileWriter("./"+category_name+"-"+WebDatabase.web_database_url+".txt");
			BufferedWriter out = new BufferedWriter(fstream);
			//Collections.sort(total_words); 	 
			Hashtable<String,Integer> final_list = new Hashtable<String,Integer>();
			int count=1;
			for(int i=0;i<total_words.size();i++)
			{ 	  
				if(final_list.containsKey(total_words.get(i).toString()))
				{
					count++;
				}
				else
				{
					count=1;	
				}
				final_list.put(total_words.get(i).toString(),count);	
			}	
			ArrayList<String> aList = new ArrayList<String>(final_list.keySet()); 	
			Collections.sort(aList);
			int j=0;
			for (Iterator<String> i = aList.iterator();i.hasNext();)
			{
				out.write(aList.get(j).toString()+":"+final_list.get(i.next()));
				out.newLine();
				j++; 	 	
			} 
			out.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
    //METHOD TO ADD DOCUMENT TO THE LIST OF DOCUMENTS FOR THE CATEGORY
	//IT CHECKS FOR DUPLICATE DOCUMENTS
	void addDocuments(LinkedList<Documents> docs_for_query)
	{
		int cnt=0;
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
			{
				list_of_documents.add(docs_for_query.get(i));
				cnt++;
			}
		}
		number_of_documents_for_query.add(cnt);
	}
	
	//METHOD TO SET THE COVERAGE OF A CATEGORY
	void setCoverage(double n)
	{
		coverage = n;
	}
	
	//METHOD TO SET SPECIFICITY OF A CATEGORY
	void setSpecificity(double n)
	{
		specificity = n;
	}
	
	//METHOD TO SET THE TOTAL NUMBER OF DOCUMENTS FOR THE CATEGORY
	double getTotalNoDocs()
	{
		return total_no_docs;
	}
	
	//METHOD TO GET SPECIFICITY OF THE CATEGORY
	double getSpecificity()
	{
		return specificity;
	}
	
	//METHOD TO GET THE COVERAGE OF THE CATEGORY
	double getCoverage()
	{
		return coverage;
	}
	
	//METHOD TO SET THE TOTAL NUMBER OF DOCUMENTS OF THE CATEGORY
	void setTotalNoDocs(double n)
	{
		total_no_docs = n;
	}
}
