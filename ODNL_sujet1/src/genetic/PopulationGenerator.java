package genetic;

import tool.Problem;

public class PopulationGenerator 
{
	public static Population generate_population(Problem pb, int popu_size)
	{
		Population population = new Population(popu_size);
		//NOT using the seed
		SequenceGenerator.initGenerator();
		int nb_task = pb.getNb_tasks();
		for (int i = 0; i < popu_size; i++)
		{
			population.add(SequenceGenerator.generate(nb_task));
		}
		
		return population;
	}
	
	public static Population generate_population_seed(Problem pb, int popu_size)
	{
		Population population = new Population(popu_size);
		//Using the seed
		SequenceGenerator.initGenerator(pb.getInitseed());
		int nb_task = pb.getNb_tasks();
		for (int i = 0; i < popu_size; i++)
		{
			population.add(SequenceGenerator.generate(nb_task));
		}
		
		return population;
	}
	
	
	
}
