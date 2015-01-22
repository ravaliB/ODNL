package MetaHeuristic;

import java.util.List;

import tool.ScheduleForWorker;
import tool.Travel;

public class Env {
	private final Params params_;

	public Env(final Params params)
	{
		params_ = params;
	}

	public List<Travel> info()
	{
		return params_.input;
	}

	public List<List<Integer>> simulate(List<ScheduleForWorker> result)
	{
		Resolver resolv = new Resolver(this);
		{
			Params params = new Params();
			params.input = params_.input;
			params.maxTabuLen = params_.maxTabuLen;

			resolv.init(params);
		}

		return resolv.run(result);
	}
}
