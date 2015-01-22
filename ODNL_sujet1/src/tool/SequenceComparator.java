package tool;

import genetic.Sequence;

import java.util.Comparator;

public class SequenceComparator implements Comparator<Sequence> {

	@Override
	public int compare(Sequence o1, Sequence o2) {

		if (o1.cost > o2.cost)
			return -1;
		else if (o1.cost < o2.cost)
			return 1;
		return 0;
			
	}

}
