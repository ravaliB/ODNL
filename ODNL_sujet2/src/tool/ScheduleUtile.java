package tool;

public class ScheduleUtile {
	
	public static boolean isEqual(ScheduleForWorker mv1, ScheduleForWorker mv2)
	{
		for (int i = 0; i < mv1.travelId.size(); i++)
			if (mv1.travelId.get(i) != mv2.travelId.get(i))
				return false;
		
		return ((mv1.Id == mv2.Id) && (mv1.isActif == mv2.isActif));
	}
	
	
	public static int substractTime(Time t1, Time t2)
	{
		return ((t1.hour - t2.hour) * 60 + (t1.min - t2.min));
	}
	
	public static boolean greaterThan(Time t1, Time t2)
	{
		if (t1.hour < t2. hour)
			return true;
		else
			return ((t1.hour == t2.hour) && (t1.min <= t2.min));
	}
}
