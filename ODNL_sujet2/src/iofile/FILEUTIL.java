package iofile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tool.ScheduleForWorker;
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

	public static void writeInFile(List<ScheduleForWorker> ws, List<List<Integer>> travelList, String filename)
	{
		try {
			File file = new File(filename);

			if (!file.exists())
			{
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(fw);

			for (int i = 0; i < ws.size(); i++)
			{
				ScheduleForWorker tmp = ws.get(i);

				for (int day = 0; day < 5; ++day)
				{
					int tday = day + 1;
					bw.write(tmp.Id + 1 + " " + tday);

					List<Integer> tmpl = tmp.travelId.get(day) == -1 ? new ArrayList<Integer>()	: travelList.get(tmp.travelId.get(day));

					for (Iterator<Integer> it = tmpl.iterator(); it.hasNext();)
					{
						Integer intIt = it.next();
						bw.write(" " + Integer.toString(intIt));
					}
      
					bw.append(System.getProperty("line.separator"));
		    	}
			}
			
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
