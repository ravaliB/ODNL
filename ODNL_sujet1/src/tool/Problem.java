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
	
}
