package tool;

public class RemoveTravel {
	public int index, dayId, travelId;

	public RemoveTravel()
	{
		this.index = -1;
		this.dayId = -1;
		this.travelId = -1;
	}
	
	public RemoveTravel(int index, int dayId, int travelId)
	{
		this.index = index;
		this.dayId = dayId;
		this.travelId = travelId;
	}
}
