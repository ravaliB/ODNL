package genetic;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class Sequence extends ArrayList<Integer>
{
	int cost = -1;
	public Sequence()
	{
		super();
	}

	public Sequence(int max)
	{
		super(max);
	}
}
