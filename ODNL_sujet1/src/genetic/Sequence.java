package genetic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

@SuppressWarnings("serial")
public class Sequence extends ArrayList<Integer>
{
	public int cost = -1;
	public Sequence()
	{
		super();
	}

	public Sequence(int max)
	{
		super(max);
	}
	
	@Override
	public String toString()
	{
		int width = (int) Math.log10(this.size()) +1;
		String format = "%" + width +"d";
		
		String s = "   < ";
		
		for (int i = 0; i < this.size(); i++)
			s += String.format(format, this.get(i)) + " ";
		
		s += ">   ";
		s += "Poid = ";
		s += this.cost;
		return s;
	}
	public int getDist(Sequence seq)
	{
		int distance = 0;
		for (int i = 0; i < seq.size(); i++)
		{
			if (!(seq.get(i).intValue() == this.get(i).intValue()))
				distance++;
		}
		return distance;
	}
}
