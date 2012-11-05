package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

//THIS CLASS IS USED TO OBTAIN THE DISTINCT WORDS
public class WordsLynx 
{
	//THIS RETURNS A SET OF ALL THE DINSTICT WORDS IN THE CONTENT OF THE URL PASSED AS THE PARAMETR TO IT
	public static Set<String> runLynx(String url) 
	{
		int buffersize = 40000;
		StringBuffer buffer = new StringBuffer(buffersize);
		try 
		{
			String cmdline[] = {"/usr/bin/lynx", "--dump", url };
			Process p = Runtime.getRuntime().exec(cmdline);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			char[] cbuf = new char[1];
			while (stdInput.read(cbuf, 0, 1) != -1 || stdError.read(cbuf, 0, 1) != -1) 
			{
				buffer.append(cbuf);
			}
			p.waitFor();
			stdInput.close();
			stdError.close();
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
        //REMOVE THE REFERENCES AT THE END OF THE DUMP
		int end = buffer.indexOf("\nReferences\n");
		if (end == -1) 
		{
			end = buffer.length();
		}
		//REMOVE EVERYTHING INSIDE [ ] AND DO NOT WRITE MORE THAN TWO CONSECUTIVE SPACE
		boolean recording = true;
		boolean wrotespace = false;
		StringBuffer output = new StringBuffer(end);
		for (int i = 0; i < end; i++) 
		{
			if (recording) 
			{
				if (buffer.charAt(i) == '[') 
				{
					recording = false;
					if (!wrotespace) 
					{
						output.append(' ');
						wrotespace = true;
					}
					continue;
				} 
				else 
				{
					if (Character.isLetter(buffer.charAt(i)) && buffer.charAt(i)<128) 
					{
						output.append(Character.toLowerCase(buffer.charAt(i)));
						wrotespace = false;
					} 
					else 
					{
						if (!wrotespace) 
						{
							output.append(' ');
							wrotespace = true;
						}
					}
				}
			} 
			else 
			{
				if (buffer.charAt(i) == ']') 
				{
					recording = true;
					continue;
				}
			}
		}
		Set<String> document = new TreeSet<String>();
		StringTokenizer st = new StringTokenizer(output.toString());
		while (st.hasMoreTokens()) 
		{
			String tok = st.nextToken();
			//System.out.println(tok);
			document.add(tok);
		}
		return document;
	}
}