package genetic;

import tool.Problem;

public class PopulationGenerator 
{
	long seed;
	Problem problem;
	SequenceGenerator sqG;
	
	public PopulationGenerator(Problem pb)
	{
		sqG = new SequenceGenerator();
		problem = pb;
	}
	
	public PopulationGenerator(Problem pb, long seed)
	{
		sqG = new SequenceGenerator(seed);
		problem = pb;
	}
	
	public Population generate_population(int popu_size)
	{
		SequenceEvaluator sqE = new SequenceEvaluator(problem);
		Population population = new Population(sqE, popu_size);

		for (int i = 0; i < popu_size; i++)
			population.add(sqG.generate(problem.nb_tasks));
		
		return population;
	}

}
