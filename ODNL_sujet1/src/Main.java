import genetic.Population;
import genetic.PopulationGenerator;
import iofile.Fileutils;

import java.text.ParseException;

import tool.Problem;


public class Main {
	public static void main (String[] args) throws ParseException
	{
		System.out.println("Opening problem file :");
		System.out.println(args[0]);
		Problem p = Fileutils.getfilesubject2(args[0]);
		p.print();
		PopulationGenerator popu_generator = new PopulationGenerator();
		Population popu = popu_generator.generate_population(p, 1000);
		popu.print();
	}
}
