package MetaHeuristic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tabou.Tabou;
import tool.NewScheduleForWorker;
//import tool.ScheduleUtile;
//import tool.Time;
import tool.Travel;

public class Resolver {
	private Env env_;
	private Params params_;
	private List<Travel> problem_;
	private int maxTravelId_;

	
	enum TravelComparator implements Comparator<Travel> {
		ID_COMP 
		{
			public int compare(Travel o1, Travel o2)
			{
				return Integer.valueOf(o1.id).compareTo(o2.id); 
			}
		},

		HOUR_COMP
		{
			public int compare(Travel o1, Travel o2)
			{
				return Integer.valueOf(o1.startT.hour).compareTo(o2.startT.hour);
			}
		},

		MIN_COMP
		{
			public int compare(Travel o1, Travel o2)
			{
				return Integer.valueOf(o1.startT.min).compareTo(o2.startT.min);
			}
		};

		public static Comparator<Travel> decending(final Comparator<Travel> other) {
			return new Comparator<Travel>() {
				public int compare(Travel o1, Travel o2) {
					return -1 * other.compare(o1, o2);
				}
			};
		}

		public static Comparator<Travel> getComparator(final TravelComparator... multipleOptions) {
			return new Comparator<Travel>() {
				public int compare(Travel o1, Travel o2) {
					for (TravelComparator option : multipleOptions) {
						int result = option.compare(o1, o2);

						if (result != 0) {
							return result;
						}
					}
					return 0;
				}
			};
		}
	}

	public Resolver(Env env)
	{
		this.env_ = env;
		this.params_ = new Params();
		this.problem_ = new ArrayList<Travel>();
		this.maxTravelId_ = 0;
	}

	public void init(Params param)
	{
		params_ = param;

		for (int i = 0; i < params_.input.size(); i++)
			problem_.add(params_.input.get(i));

	}

	public List<List<Integer>> run(List<NewScheduleForWorker> result)
	{
		//long maxWorkTimeMin = 0;
		//long minWork = 0;

		Collections.sort(problem_,
				TravelComparator.decending(TravelComparator.getComparator(TravelComparator.ID_COMP,
						TravelComparator.HOUR_COMP,
						TravelComparator.MIN_COMP)));

		for (int i = 0; i < problem_.size (); i++)
		{
			Travel t = problem_.get(i);

			if (maxTravelId_ < t.id)
				maxTravelId_ = t.id;

		//	maxWorkTimeMin += ScheduleUtile.substractTime(t.endT, t.startT);
		}

		//minWork = (long) Math.ceil((maxWorkTimeMin / 60) / 5);
		Tabou t = new Tabou(params_.maxTabuLen, maxTravelId_, result, problem_);

		return t.Run();
	}
}
