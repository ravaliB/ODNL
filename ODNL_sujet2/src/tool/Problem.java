package tool;

public class Problem {

	private int nb_cpu;
	private int nb_tasks;
	private int[][] tasktimes;
	private int upperbound;
	private int lowerbound;
	private int initseed;
	
	
	
	public int getNb_tasks() {
		return nb_tasks;
	}
	public void setNb_tasks(int nb_tasks) {
		this.nb_tasks = nb_tasks;
	}
	public int getNb_cpu() {
		return nb_cpu;
	}
	public void setNb_cpu(int nb_cpu) {
		this.nb_cpu = nb_cpu;
	}
	public int[][] getTasktimes() {
		return tasktimes;
	}
	public void setTasktimes(int[][] tasktimes) {
		this.tasktimes = tasktimes;
	}
	public int getUpperbound() {
		return upperbound;
	}
	public void setUpperbound(int upperbound) {
		this.upperbound = upperbound;
	}
	public int getLowerbound() {
		return lowerbound;
	}
	public void setLowerbound(int lowerbound) {
		this.lowerbound = lowerbound;
	}
	public int getInitseed() {
		return initseed;
	}
	public void setInitseed(int initseed) {
		this.initseed = initseed;
	}
	
	public void print()
	{
		for (int i = 0; i < nb_cpu; i++)
		{
			String pctime = "";
			for (int j = 0; j < nb_tasks; j++)
			{
				pctime += tasktimes[i][j];
				pctime += " ";
			}
			System.out.println(pctime);
		}
		
		
	}
	
}
