package genetic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.List;

import tool.SequenceComparator;

@SuppressWarnings("serial")
public class Population extends ArrayList<Sequence> 
{
	SequenceEvaluator sqE;
	public double disparity;
	
	
	public Population(SequenceEvaluator sqE)
	{
		super();
		this.sqE = sqE;
	}
	
	public Population(SequenceEvaluator sqE, int max)
	{
		super(max);
		this.sqE = sqE;
	}

	private Population(List<Sequence> popu, SequenceEvaluator sqE)
	{
		super(popu);
		this.sqE = sqE;
	}

	@Override
	public boolean add(Sequence sequence)
	{
		sequence.cost = sqE.compute_cost(sequence);
		return (super.add(sequence));
	}
	
	public void sort()
	{
		Collections.sort(this, new SequenceComparator());
	}
	
	public Sequence best()
	{
		return this.get(this.size()-1);
	}
	
	public Population best(int n)
	{
		return new Population(this.subList(this.size()-n, this.size()), sqE);
	}
	
	public void probabilistic_distribution()
	{
		int seq_size = this.get(0).size();
		int mat_distrib[][] = new int[seq_size][seq_size];
		for (Sequence s : this)
		{
			for (int i = 0; i < seq_size; i++)
			{
				mat_distrib[s.get(i)][i]++;
			}
		}
		int width = (int) Math.log10(seq_size) +2;
		String format = "%" + width +"d";
		for (int i = 0; i < seq_size; i++)
		{
			String s = "Job n° " + String.format(format, i) + " :";
			s += "   < "; 
			
			for (int j = 0; j < seq_size; j++)
			{
					s += String.format(format, mat_distrib[i][j]) + " ";
			}
			s += ">";
			System.out.println(s);
		}
			
	}
	
	
	@Override
	public String toString()
	{
		
		String s = "";
		System.out.println("============");
		System.out.println("=Population=");
		System.out.println("============");
		System.out.println("Taille : " + this.size());
		
		for (int i = 0; i < this.size(); i++)
			s += this.get(i) + "\n";
		
		return s;
	}
	
	
	//fait muter le gene d'indice index dans la population
	public void Mutate(int index)
	{
		Random rand = new Random();
		Sequence gene = this.get(index);
		
		int randomIndex = rand.nextInt(gene.size());
		int randomIndex2 = randomIndex - rand.nextInt(randomIndex + 1);
		
		Integer value = gene.get(randomIndex);
		gene.remove(randomIndex);
		
		gene.add(randomIndex2, value);
		
		gene.cost = sqE.compute_cost(gene);
	}
	
	public double ComputeDisparity()
	{
		double disparity = 0.0;
		Sequence seq1;
		Sequence seq2;
		int count = 0;
		for (int i = 0; i < this.size(); i++)
		{
			for (int j = i+1; j < this.size(); j++)
			{
				count++;
				seq1 = this.get(i);
				seq2 = this.get(j);
				disparity += seq1.getDist(seq2);
			}
		}
		disparity = disparity / count;
		this.disparity = disparity;
		
		return disparity;
	}
}
