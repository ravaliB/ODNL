package tool;

public class Travel {
	public int id;
	public String start, end;
	public Time startT;
	public Time endT;
	
	public Travel()
	{
		this.id = 0;
		this.start = "";
		this.end = "";
		this.startT = new Time();
		this.endT = new Time();
	}
	
	public Travel(int id, String start, String end, Time startT, Time endT)
	{
		this.id = id;
		this.start = start;
		this.end = end;
		this.startT = startT;
		this.endT = endT;
	}
}
