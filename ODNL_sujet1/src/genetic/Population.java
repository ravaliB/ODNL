package genetic;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Population extends ArrayList<Sequence> 
{

	public Population()
	{
		super();
	}
	
	public Population(int max)
	{
		super(max);
	}
}
