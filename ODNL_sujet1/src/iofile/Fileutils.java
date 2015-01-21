package iofile;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import tool.Problem;


public class Fileutils {
	public static Problem getfilesubject2(String file) throws ParseException
	{
		BufferedReader br = null;
		String line = "";
		String splitBy = "\\s+";
		Problem p = new Problem();
	
		try {
			br = new BufferedReader(new FileReader(file));
			
			br.readLine();
			line = br.readLine();
			
			
			String[] l =  line.split(splitBy);
			p.setNb_tasks(Integer.parseInt(l[1]));
			p.setNb_cpu(Integer.parseInt(l[2]));
			p.setInitseed(Integer.parseInt(l[3]));
			p.setLowerbound(Integer.parseInt(l[4]));
			p.setUpperbound(Integer.parseInt(l[5]));
			
			int[][] tasktimes = new int[p.getNb_cpu()][p.getNb_tasks()];
			
			
			br.readLine();
			for (int i = 0;  i < p.getNb_cpu(); i++)
			{
				line = br.readLine();
				l = line.split(splitBy);
				for (int j = 0; j < p.getNb_tasks(); j++)
				{
					tasktimes[i][j] = Integer.parseInt(l[j + 1]);
				}
			}
			p.setTasktimes(tasktimes);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return p;
	}
}
