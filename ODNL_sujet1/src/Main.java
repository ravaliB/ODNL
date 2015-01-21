import iofile.Fileutils;

import java.text.ParseException;

import tool.Problem;


public class Main {
	public static void main (String[] args) throws ParseException
	{
		Problem p = Fileutils.getfilesubject2(args[0]);
		p.print();
		
	}
}
