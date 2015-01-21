import java.text.ParseException;
import java.util.List;

import tool.Problem;
import tool.Travel;
import iofile.FILEUTIL;


public class Main {
	
	public static void main (String[] args) throws ParseException
	{
//		List<Travel> travels = FILEUTIL.getfiles(args[0]);
//		
//		System.out.println(travels.get(0).id + " " +
//				travels.get(0).start + " " +
//				travels.get(0).end + " " +
//				travels.get(0).startT.hour + ":" + travels.get(0).startT.min + " " +
//				travels.get(0).endT.hour + ":" + travels.get(0).endT.min);
		Problem p = FILEUTIL.getfilesubject2(args[0]);
		//p.print();
		
	}
}
