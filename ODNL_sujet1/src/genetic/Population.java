package genetic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		return this.get(0);
	}
	
	public Population best(int n)
	{
		return new Population(this.subList(0, n), sqE);
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
}
