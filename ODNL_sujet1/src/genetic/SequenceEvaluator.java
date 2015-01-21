package genetic;

import tool.Problem;

public class SequenceEvaluator {

	private Problem problem = null;
	
	public SequenceEvaluator(Problem pb)
	{
		problem = pb;
	}
	
	public int compute_cost(Sequence gene)
	{
		int[][] tabforCmax = new int[problem.nb_cpu][problem.nb_tasks];
		
		tabforCmax[0][gene.get(0)] = problem.tasktimes[0][gene.get(0)];
		
		for (int i = 1 ; i < problem.nb_tasks; i++)
		{
			tabforCmax[0][gene.get(i)] = tabforCmax[0][gene.get(i - 1)] + problem.tasktimes[0][gene.get(i)];	
		}
		for (int i = 1 ; i < problem.nb_cpu; i++)
		{
			tabforCmax[i][gene.get(0)] = tabforCmax[i - 1][gene.get(0)] + problem.tasktimes[i][gene.get(0)];	
		}
		
		for (int i = 1; i < problem.nb_tasks; i++)
			for (int j = 1; j < problem.nb_cpu; j++)
			{
				tabforCmax[j][gene.get(i)] = Math.max(tabforCmax[j - 1][gene.get(i)], tabforCmax[j][gene.get(i - 1)]) + problem.tasktimes[j][gene.get(i)];
			}
		
		return tabforCmax[problem.nb_cpu - 1][gene.get(gene.size() - 1)];
	}
}
