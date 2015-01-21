package tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SequenceGenerator {

	private static Random rng = null;
	
	public static void initGenerator(long seed)
	{
		if (rng == null)
			rng = new Random(seed);
	}
	
	public static void initGenerator()
	{
		if (rng == null)
			rng = new Random();
	}
	
	public static List<Integer> generate(int max)
	{
		if (rng == null)
			SequenceGenerator.initGenerator();
		
		ArrayList<Integer> result = new ArrayList<Integer>(max);
		
		for (int i = 0; i < max; i++)
			result.add(i);

		Collections.shuffle(result, rng);
		return result;
	}
}
