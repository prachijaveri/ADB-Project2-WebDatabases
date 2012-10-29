package main;

import java.net.URL;
import java.util.Scanner;

public class WebDatabase 
{
	private static String web_database_url="";
	private static double user_specificity_threshold = -1;
	private static double user_coverage_threshold = -1;
	
	private static boolean checkUrl()
	{
		try
		{
			new URL(web_database_url);
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
	
	public static void main(String arg[])
	{
		Scanner in = new Scanner(System.in);
		System.out.println("----------------------------------------------------------");
		System.out.println("\t\tWEB DATABASE CLASSIFICATION");
		System.out.println("----------------------------------------------------------");
		System.out.println();
		while(checkUrl())
		{
			System.out.println("Enter the url for the Web database");
			web_database_url="http://"+in.nextLine();
		}
		System.out.println();
		System.out.println("----------------------------------------------------------");
		System.out.println();
		while(checkSpecificity())
		{
			System.out.println("Enter the Specificity Threshold");
			user_specificity_threshold=in.nextDouble();
		}
		System.out.println();
		System.out.println("----------------------------------------------------------");
		System.out.println();
		while(checkCoverage())
		{
			System.out.println("Enter the Coverage Threshold");
			user_coverage_threshold=in.nextDouble();
		}
		System.out.println();
		System.out.println("----------------------------------------------------------");
		System.out.println();
		in.close();
	}
}
