import genetic.Population;
import genetic.PopulationGenerator;
import genetic.Sequence;
import iofile.Fileutils;

import java.text.ParseException;
import java.util.Random;
import java.util.Date;

import tool.Problem;


public class Main {
	public static void main (String[] args) throws ParseException
	{
		System.out.println("Opening problem file :");
		System.out.println(args[0]);
		Problem p = Fileutils.getfilesubject2(args[0]);
		
		Population population;
		Population newgen;
		
		PopulationGenerator popu_generator = new PopulationGenerator(p, p.initseed);
		population = popu_generator.generate_population(1000);
		newgen = popu_generator.generate_newgen(population);
		
		
		Random rn = new Random();
		int optimalvalue = newgen.get(newgen.size() -1).cost;
		
		//la reproduction
		for (int i = 1; i < 100 ; i++)
		{
			population = newgen;
			newgen = popu_generator.generate_newgen(population);
			optimalvalue = newgen.get(newgen.size() -1).cost;
			
			//la mutation
			for (int j = 0; j < newgen.size(); j += newgen.size() / 10)
			{
				newgen.Mutate(rn.nextInt(newgen.size()));
			}
			newgen.sort();
			System.out.println("Iteration n°" + i + " : Best value found => " +optimalvalue);
		}
		
		System.out.println("Best value found : " + optimalvalue);
		System.out.println("Best Sequence " + newgen.get(newgen.size() -1));
		
		
	}
}
