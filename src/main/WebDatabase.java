package main;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;

public class WebDatabase 
{	
	//TO SAVE THE URL OF THE DATABASE TO BE CATEGORIZED
	protected static String web_database_url="";
	
	//TO SAVE THE THRESHOLD VALUE FOR SPECIFICITY AS MENTIONED BY THE USER
	protected static double user_specificity_threshold = -1;
	
	//TO SAVE THE THRESHOLD VALUE FOR COVERAGE AS MENTIONED BY USER
	protected static double user_coverage_threshold = -1;
	
	//TO STORE THE CLASSIFICATION TREE
	private static Classification classification_tree;
	
	//TO STORE THE OUTPUT OF THE CLASSIFICATION
	protected static LinkedList<Category> database_classification=new LinkedList<Category>();
	
	//THIS TAKES THE INPUT FROM THE USER AND THEN CALLS OTHER FUNCTIONS FOR CLASSIFYING THE DATABASE AND GENERATING THE CONTENT SUMMARY
	public static void main(String arg[]) throws IOException
	{
		Scanner in = new Scanner(System.in);
		System.out.println("----------------------------------------------------------");
		System.out.println("\t\tWEB DATABASE CLASSIFICATION");
		System.out.println("----------------------------------------------------------");
		System.out.println();
		do
		{
			System.out.println("Enter the url for the Web database");
			web_database_url=in.nextLine();
		}while(checkUrl());
		System.out.println();
		System.out.println("----------------------------------------------------------");
		System.out.println();
		do
		{
			System.out.println("Enter the Specificity Threshold");
			user_specificity_threshold=in.nextDouble();
		}while(checkSpecificity());
		System.out.println();
		System.out.println("----------------------------------------------------------");
		System.out.println();
		do
		{
			System.out.println("Enter the Coverage Threshold");
			user_coverage_threshold=in.nextDouble();
		}while(checkCoverage());
		System.out.println();
		System.out.println("----------------------------------------------------------");
		System.out.println();
		in.close();
		classification_tree=new Classification();
		classification_tree.startClassification();
		System.out.println();
		System.out.println("----------------------------------------------------------");
		System.out.println();		
		System.out.println("Classification for the Url : "+web_database_url);
		displayClassification();
		System.out.println();
		System.out.println("----------------------------------------------------------");
		System.out.println();
		getContentSummary();
	}
	
	//METHOD TO GET THE CONTENT SUMMARY FOR THE NODES THAT HAVE BEEN CLASSIFIED
	private static void getContentSummary()throws IOException
	{
		for(int i = database_classification.size()-1;i>=0;i--)
		{
			Category node = database_classification.get(i);
			if(node.getNumberOfChildren() > 0)
				node.getWordSetForAll();
		}
	}
	
	//METHOD TO DISPLAY THE OUTPUT OF THE CLASSIFICATION
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
	
	//IT CHECKS WHETHER THE LIST OF CLASSIFIED CATEGORIES CAONTAINS A CHILD OF THE CATEGORY PASSES AS THE PARAMETER
	private static boolean containsChildOf(Category node)
	{
		LinkedList<Category> children = node.getChildren();
		if(children.size() == 0)
			return false;
		for(int i=0;i<database_classification.size();i++)
		{
			for(int j=0;j<children.size();j++)
			{
				if(database_classification.get(i).getCategoryName().equalsIgnoreCase(children.get(j).getCategoryName()))
					return true;
			}
		}
		return false;
	}
	
	// METHOD TO CHECK IF URL IS VALID
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
	
	//METHOD TO CHECK IF SPECIFICITY IS IN THE DESIRED RANGE
	private static boolean checkSpecificity()
	{
		if(user_specificity_threshold >= 0 && user_specificity_threshold <= 1)
			return false;
		return true;
	}
	
	//METHOD TO CHECK IF COVERAGE IS IN DESIRED RANGE
	private static boolean checkCoverage()
	{
		if(user_coverage_threshold >= 1)
			return false;
		return true;
	}
}