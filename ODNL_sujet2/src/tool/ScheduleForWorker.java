package tool;

import java.util.ArrayList;
import java.util.List;

public class ScheduleForWorker {
	public int Id, dayId;
	public List<Integer> travelId;

	public ScheduleForWorker()
	{
		this.Id = -1;
		this.dayId = -1;
		this.travelId = new ArrayList<Integer>();
	}
	
	public ScheduleForWorker(int Id, int dayId, List<Integer> travelId)
	{
		this.Id = Id;
		this.dayId = dayId;
		this.travelId = travelId;
	}
	
	public ScheduleForWorker(int Id, int dayId)
	{
		this.Id = Id;
		this.dayId = dayId;
	}
}
