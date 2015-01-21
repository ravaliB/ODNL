package tool;

import genetic.Population;
import genetic.Sequence;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Problem {

	public int nb_cpu;
	public int nb_tasks;
	public int[][] tasktimes;
	public int upperbound;
	public int lowerbound;
	public int initseed;
	
	public void print()
	{
		for (int i = 0; i < nb_cpu; i++)
		{
			String pctime = "";
			for (int j = 0; j < nb_tasks; j++)
			{
				pctime += tasktimes[i][j];
				pctime += " ";
			}
			System.out.println(pctime);
		}
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
	
	private Sequence Mutate(Sequence gene)
	{
		int randomIndex = (int)(Math.random()*gene.size());
		Integer tmp =gene.get(randomIndex);
		gene.remove(randomIndex);
		gene.add(randomIndex - (int)Math.random()*randomIndex, tmp);
		return gene;
	}
	
	
	
}
