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
		
<<<<<<< HEAD
		System.out.println("done");
=======
		System.out.println(travels.get(0).id + " " +
				travels.get(0).start + " " +
				travels.get(0).end + " " +
				travels.get(0).startT.hour + ":" + travels.get(0).startT.min + " " +
				travels.get(0).endT.hour + ":" + travels.get(0).endT.min);
		
>>>>>>> 130b68f592af5f3c013276e069ceb32a369745fb
	}
}
