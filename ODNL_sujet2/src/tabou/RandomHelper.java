package tabou;
import java.util.Random;


public class RandomHelper {
	private static long seed_ = 0;
	private static RandomHelper instance_ = null;
	private Random rand_;

	public static void dispose()
	{
		instance_ = null;
	}

	public static void init(final long seed)
	{
		seed_ = seed;
	}

	public static RandomHelper instance()
	{
		if (instance_ == null)
			instance_ = new RandomHelper(seed_);

		return instance_;
	}

	public boolean eventOccurs(double prob)
	{
		return (rand_.nextDouble() <= prob);
	}

	public int getNext(int min, int max)
	{
		return rand_.nextInt((max-min) + 1) + min;
	}

	private RandomHelper(final long seed)
	{
		rand_ = new Random(seed);
	}
}
