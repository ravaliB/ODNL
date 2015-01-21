package genetic;

import java.util.ArrayList;
import java.util.Collections;

import java.util.Random;

public class SequenceGenerator {

	private Random rng = null;
	
	public SequenceGenerator(long seed)
	{
			rng = new Random(seed);
	}
	
	public SequenceGenerator()
	{
			rng = new Random();
	}
	
	public Sequence generate(int max)
	{	
		Sequence result = new Sequence(max);
		
		for (int i = 0; i < max; i++)
			result.add(i);

		Collections.shuffle(result, rng);
		return result;
	}
}
