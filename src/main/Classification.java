package main;

import java.util.LinkedList;
import java.util.Vector;

public class Classification 
{	
	Category root;
	
	Classification()
	{
		root = new Category();
		root.setQueries("http://www.cs.columbia.edu/~gravano/cs6111/Proj2/data/root.txt");
		root.setSpecificity(1.0);
	}
	
	void executeQueries()
	{
		System.out.println("Classifying ..... ");
		LinkedList<Category> nodes = new LinkedList<Category>();
		nodes.add(root);
		while(! nodes.isEmpty())
		{
//			System.out.print("\t-----> ");
//			for(int i=0;i<nodes.size();i++)
//			{
//			System.out.print(nodes.get(i).getCategoryName() +" , ");
//			}
//			System.out.println();
			Category node = nodes.removeFirst();
			System.out.println("...");
			node.setTotalNoDocs(0);
			WebDatabase.database_classification.add(node);
			for(int i=0;i<node.getNumberOfQueries();i++)
			{
				String query=node.getQueryAtIndex(i).trim();
				int index = query.indexOf(" ");
				String category_name = query.substring(0,index).trim().toLowerCase();
				query = query.substring(index).trim().replaceAll(" ", "+");
				double no_docs=0.0;
				try
				{
					Vector result = BingSearch.getDocumentNumber(WebDatabase.web_database_url, query);
					@SuppressWarnings("unchecked")
					LinkedList<Documents> docs_for_query = (LinkedList<Documents>)result.elementAt(0);
					no_docs =Double.parseDouble(result.elementAt(1) +"");
					node.addDocuments(docs_for_query);
				}
				catch(Exception e)
				{
					System.out.println(e);
				}
				if(node.isChild(category_name))
				{
					node.increaseTotal(no_docs);
					Category c = node.getChildByName(category_name);
					c.increaseTotal(no_docs);
				}
				else
				{
					Category c = new Category(category_name,node);
					node.increaseTotal(no_docs);
					c.increaseTotal(no_docs);
					if(node.getCategoryName().equalsIgnoreCase("root"))
					{
						c.setQueries("http://www.cs.columbia.edu/~gravano/cs6111/Proj2/data/"+c.getCategoryName().toLowerCase()+".txt");
					}
				}	
			}
			for(int i=0;i<node.getNumberOfChildren();i++)
			{
				Category c =node.getChildByIndex(i);
				c.setCoverage(c.getTotalNoDocs());
				c.setSpecificity(node.getSpecificity() * c.getTotalNoDocs() / node.getTotalNoDocs());
				if(c.getCoverage() >= WebDatabase.user_coverage_threshold && c.getSpecificity() >= WebDatabase.user_specificity_threshold)
				{	
					nodes.add(c);
				}
			}	
		}
		System.out.println("Classification Completed");
	}
	public String toString()
	{
		String s="";
		LinkedList<Category> node = new LinkedList<Category>();
		node.add(root);
		while(!node.isEmpty())
		{
			Category c =node.removeFirst();
			s+=(c.getCategoryName()+" ----->  ");
			for(int i=0;i<c.getNumberOfChildren();i++)
			{
				Category x = c.getChildByIndex(i);
				s+= (x.getCategoryName()+" , ");
				node.add(x);
			}
			s+="\n";
		}
		return s;
	}
}
