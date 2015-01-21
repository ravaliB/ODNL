package genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SequenceGenerator {

	private static Random rng = null;
	
	public static void initGenerator(long seed)
	{
			rng = new Random(seed);
	}
	
	public static void initGenerator()
	{
			rng = new Random();
	}
	
	public static Sequence generate(int max)
	{
		if (rng == null)
			SequenceGenerator.initGenerator();
		
		Sequence result = new Sequence(max);
		
		for (int i = 0; i < max; i++)
			result.add(i);

		Collections.shuffle(result, rng);
		return result;
	}
}
