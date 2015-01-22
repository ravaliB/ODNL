package MetaHeuristic;

import java.util.List;

import tool.Travel;

public class Params {
	public List<Travel> input;
	public int maxTabuLen;
	
	public Params(int maxTabuLen, List<Travel> input)
	{
		this.maxTabuLen = maxTabuLen;
		this.input = input;
	}
	
	public Params()
	{
	}
}
