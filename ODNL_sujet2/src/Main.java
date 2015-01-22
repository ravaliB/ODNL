import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import MetaHeuristic.Env;
import MetaHeuristic.Params;
import tool.ScheduleForWorker;
import tool.Travel;
import iofile.FILEUTIL;

public class Main {
	
	public static void main (String[] args) throws ParseException
	{
		List<Travel> travels = FILEUTIL.getfiles(args[0]);
		List<ScheduleForWorker> result = new ArrayList<ScheduleForWorker>();
		List<List<Integer>> travelsLists;
		Params params = new Params(300, travels);
		Env env = new Env(params);
		travelsLists = env.simulate(result);
		FILEUTIL.writeInFile(result, travelsLists, args[1]);
		
		System.out.println("done");
	}
}
