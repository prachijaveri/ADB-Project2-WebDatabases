package main;

import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;

public class WebDatabase 
{
	protected static String web_database_url="";
	protected static double user_specificity_threshold = -1;
	protected static double user_coverage_threshold = -1;
	private static Classification classification_tree;
	protected static LinkedList<Category> database_classification=new LinkedList<Category>();
	
	public static void main(String arg[])
	{
		Scanner in = new Scanner(System.in);
		System.out.println("----------------------------------------------------------");
		System.out.println("\t\tWEB DATABASE CLASSIFICATION");
		System.out.println("----------------------------------------------------------");
		System.out.println();
		do
		{
			System.out.println("Enter the url for the Web database");
			web_database_url="nba.com";
			//web_database_url=in.nextLine();
		}while(checkUrl());
		System.out.println();
		System.out.println("----------------------------------------------------------");
		System.out.println();
		do
		{
			System.out.println("Enter the Specificity Threshold");
			user_specificity_threshold=0.6;
			//user_specificity_threshold=in.nextDouble();
		}while(checkSpecificity());
		System.out.println();
		System.out.println("----------------------------------------------------------");
		System.out.println();
		do
		{
			System.out.println("Enter the Coverage Threshold");
			user_coverage_threshold=100;
			//user_coverage_threshold=in.nextDouble();
		}while(checkCoverage());
		System.out.println();
		System.out.println("----------------------------------------------------------");
		System.out.println();
		in.close();
		classification_tree=new Classification();
		classification_tree.executeQueries();
		System.out.println("\n\n\nFINAL ANSWER");
		displayClassification();
	}
	
	private static void displayClassification()
	{
		
		for(int i = database_classification.size()-1;i>=0;i--)
		{
			if(containsChildOf(database_classification.get(i)))
			{
				continue;
			}
			else
			{
				Category node = database_classification.get(i);
				String r="";
				do
				{
					if(r.equals(""))
						r=node.getCategoryName();
					else
						r=node.getCategoryName()+" ---> "+r;
					node=node.getParent();
				}while(node != null);
				System.out.println(r);
			}
			
		}
	}
	
	private static boolean containsChildOf(Category node)
	{
		if(node.getParent() == null)
			return false;
		String parent_name = node.getParent().getCategoryName();
		for(int i=0;i<database_classification.size();i++)
		{
			if(database_classification.get(i).getCategoryName().equalsIgnoreCase(parent_name))
				return true;
		}
		return false;
	}
	private static boolean checkUrl()
	{
		try
		{
			new URL("http://"+web_database_url);
			return false;
		}
		catch(Exception e)
		{
			return true;
		}
	}
	
	private static boolean checkSpecificity()
	{
		if(user_specificity_threshold >= 0 && user_specificity_threshold <= 1)
			return false;
		return true;
	}
	
	private static boolean checkCoverage()
	{
		if(user_coverage_threshold >= 1)
			return false;
		return true;
	}
	
}