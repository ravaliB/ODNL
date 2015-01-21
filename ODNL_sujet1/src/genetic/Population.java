package genetic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import tool.SequenceComparator;

@SuppressWarnings("serial")
public class Population extends ArrayList<Sequence> 
{
	SequenceEvaluator sqE;
	
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
	
	
}
