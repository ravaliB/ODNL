import genetic.Population;
import genetic.PopulationGenerator;
import iofile.Fileutils;

import java.text.ParseException;
import java.util.Date;

import tool.Problem;


public class Main {
	public static void main (String[] args) throws ParseException
	{
		System.out.println("Opening problem file :");
		System.out.println(args[0]);
		Problem problem = Fileutils.getfilesubject2(args[0]);
		problem.print();
		PopulationGenerator popu_generator = new PopulationGenerator(problem, new Date().getTime());
		Population population = popu_generator.generate_population(10000000);
		population.sort();
		System.out.println(population.best());
		System.out.println(problem.mutate(population.best()));
		
	
		
	}
}
