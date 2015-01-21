package genetic;

import java.util.LinkedList;
import java.util.Random;

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
			population.add(sqG.generate(pb.nb_tasks));
		
		population.sort();
		return population;
	}
	public Population generate_newgen(Population p)
	{
		Random rn = new Random();
		Population newgen = new Population(p.sqE, p.size());
		Sequence parent1;
		Sequence parent2;
		for (int i = 0; i < p.size(); i += 2)
		{
			parent1 = selectGoodParent(p);
			parent2 = p.get(rn.nextInt(p.size()));
			BuildChildren(newgen, parent1, parent2);
		}
		
		newgen.sort();
		return newgen;
	}
	
	private Population BuildChildren(Population p, Sequence parent1, Sequence parent2)
	{
		Sequence o1 = new Sequence();
		Sequence o2 = new Sequence();
		
		
		Random rand = new Random();
		int crosspoint = rand.nextInt(parent1.size());
		
		Integer cp2val;
		Integer cp1val;
		LinkedList<Integer> stackunused_o1 = new LinkedList<Integer>();
		LinkedList<Integer> stackunused_o2 = new LinkedList<Integer>();
		
		//fils 1
			for (int i = 0; i < crosspoint; i++)
			{
				o1.add(parent1.get(i));
				o2.add(parent2.get(i));
			}
			for (int i = crosspoint; i < parent1.size(); i++)
			{
				cp2val = parent2.get(i);
				cp1val = parent1.get(i);
				
				if (!o1.contains(cp2val))
				{
					o1.add(cp2val);
					stackunused_o1.push(parent1.get(i));
				}
				else
				{
					if (!o1.contains(cp1val))
					{
						o1.add(cp1val);
					}
					else
					{
						Integer tmp = stackunused_o1.removeFirst();
						while (o1.contains(tmp))
							tmp = stackunused_o1.removeFirst();
						o1.add(tmp);
					}
				}
				
				if (!o2.contains(cp1val))
				{
					o2.add(cp1val);
					stackunused_o2.push(parent2.get(i));
				}
				else
				{
					if (!o2.contains(cp2val))
					{
						o2.add(cp2val);
					}
					else
					{
						Integer tmp = stackunused_o2.removeFirst();
						while (o2.contains(tmp))
							tmp = stackunused_o2.removeFirst();
						o2.add(tmp);
					}
				}
			}
		p.add(o1);
		p.add(o2);
		return p;
	}
	
	private Sequence selectGoodParent(Population population)
	{
		double p = Math.random();
		double cumulativeProbability = 0.0;
		for (int i = 0; i < population.size(); i++) {
		    cumulativeProbability += 2*(i+1)/(population.size() * (population.size()+1));
		    if (p <= cumulativeProbability) {
		        return population.get(i);
		    }
		}
		return population.get(population.size() - 1);
	}
	
	
	

}
