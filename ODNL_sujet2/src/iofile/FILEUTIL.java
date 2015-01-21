package iofile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import tool.Problem;
import tool.Time;
import tool.Travel;

public class FILEUTIL {
	public static List<Travel> getfiles(String file) throws ParseException
	{
		BufferedReader br = null;
		String line = "";
		String splitBy = " ";
		String splitByColon =  ":";
		List<Travel> trajets = new ArrayList<Travel>();
	
		try {
			br = new BufferedReader(new FileReader(file));

			while ((line = br.readLine()) != null) {
				String[] l = line.split(splitBy);
				int id = Integer.parseInt(l[0]);
				String dep = l[1];
				String arr = l[2];
				String[] t1 = l[3].split(splitByColon);
				String[] t2 = l[4].split(splitByColon);
				Time hdep = new Time(Integer.parseInt(t1[0]), Integer.parseInt(t1[1]));
				Time harr = new Time(Integer.parseInt(t2[0]), Integer.parseInt(t2[1]));

				Travel travel = new Travel(id, dep, arr, hdep, harr);
				trajets.add(travel);
			}
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

		return trajets;
	}
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
