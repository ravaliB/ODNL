package genetic;

import java.util.ArrayList;

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
	
	public void print()
	{
		System.out.println("Population :");
		System.out.println("Taille : " + this.size());
	}
}
