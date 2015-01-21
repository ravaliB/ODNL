package iofile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

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
}
