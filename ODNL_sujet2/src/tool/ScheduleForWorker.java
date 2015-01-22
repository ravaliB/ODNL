package tool;

import java.util.ArrayList;
import java.util.List;

public class ScheduleForWorker {
	public int Id;
	public List<Integer> travelId;
	public boolean isActif;

	public ScheduleForWorker()
	{
		this.Id = 1;
		this.isActif = true;
		this.travelId = new ArrayList<Integer>();

		for (int i = 0; i < 5; i++)
			this.travelId.add(-1);
	}

	public ScheduleForWorker(int Id, List<Integer> travelId)
	{
		this.Id = Id;
		this.isActif = true;
		this.travelId = travelId;
	}
}
