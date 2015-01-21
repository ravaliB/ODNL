package genetic;

import tool.Problem;

public class PopulationGenerator 
{
	long seed;
	SequenceGenerator sqG;
	
	public PopulationGenerator()
	{
		sqG = new SequenceGenerator();
	}
	
	public PopulationGenerator(long seed)
	{
		sqG = new SequenceGenerator(seed);
	}
	
	public Population generate_population(Problem pb, int popu_size)
	{
		SequenceEvaluator sqE = new SequenceEvaluator(pb);
		
		Population population = new Population(sqE, popu_size);


		for (int i = 0; i < popu_size; i++)
		{
			population.add(sqG.generate(pb.nb_tasks));
		}
		
		return population;
	}
	
	
	
	
}
