package tabou;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tool.ScheduleForWorker;
import tool.RemoveTravel;
import tool.ScheduleUtile;
import tool.Travel;


public class Tabou {
	private int maxNbTabou_;
	private int maxWorker_;
	private int maxTarget_;
	private int maxDay_;
	private int maxWorkTime_;
	private int maxFullTime_;
	private int totaltravels_;
	private List<ScheduleForWorker> tabous_;
	private List<ScheduleForWorker> allMove_;
	private List<ScheduleForWorker> result_;
	private List<Travel> travels_;
	private List<List<Integer>> travelsList_;
	private List<Float> energys_;
	private ScheduleForWorker bestMove_;
	private Map<Integer, Integer> mapIdTravel_;


	public Tabou(int maxTabouNb, 
			int maxTravelId,
			List<ScheduleForWorker> result,
			List<Travel> travels)
	{
		this.maxNbTabou_ = maxTabouNb;
		this.tabous_ = new ArrayList<ScheduleForWorker>();
		this.allMove_ = new ArrayList<ScheduleForWorker>();
		this.travels_ = travels;
		this.result_ = result;
		this.bestMove_ = new ScheduleForWorker();
		this.travelsList_ = new ArrayList<List<Integer>>();
		this.maxDay_ = 5;
		this.maxWorkTime_ = 300;
		this.maxFullTime_ = 420;
		this.maxWorker_ = maxTravelId;
		this.maxTarget_ = maxTravelId;
		this.totaltravels_ = maxTarget_ * maxDay_;
		this.mapIdTravel_ = new HashMap<Integer, Integer>();
		this.energys_ = new ArrayList<Float>();

		for ( int i = 0; i < travels_.size (); i++)
			mapIdTravel_.put(travels_.get(i).id, i);

		List<Integer> srcVect = new ArrayList<Integer>();

		for ( int i = 0; i < travels_.size(); ++i)
			srcVect.add(travels_.get(i).id);

		System.out.println("## building vector of valid travel list ##");

		for ( int i = 1; i < 6; ++i)
		{
			List<List<Integer>> tmpList = combinatory(i, srcVect, new ArrayList<Integer>(), 0);
			List<List<Integer>> tmpVec = new ArrayList<List<Integer>>();
			tmpVec.addAll(tmpList);

			for (int j = 0; j < tmpVec.size(); j++ )
				this.travelsList_.add(tmpVec.get(j));
		}

		System.out.println("## end building ##");
	}

	public List<List<Integer>> Run()
	{
		float bestEnergy = GetEnergy();
		List<ScheduleForWorker> candList = new ArrayList<ScheduleForWorker>();

		while(!stopCondition(bestEnergy))
		{
			FillCandidateList(candList);

			float bestLocalEnergy = Integer.MAX_VALUE;
			Iterator<ScheduleForWorker> it = candList.iterator();
			ScheduleForWorker nsf;
			ScheduleForWorker bestMove = new ScheduleForWorker();

			while (it.hasNext())
			{
				nsf = it.next();
				float localEnergy = GetEnergyFromMove(nsf);

				if ((!IsTabou(nsf) && localEnergy < bestLocalEnergy)
						|| (localEnergy < bestEnergy))
				{
					bestMove = nsf;
					bestLocalEnergy = localEnergy;
				}
			}

			bestMove_ = bestMove;

			MakeMove(bestMove);
			AddTabou(bestMove);

			float currentEnergy = GetEnergy();

			if (bestEnergy >= currentEnergy)
				bestEnergy = currentEnergy;
		}

		List<ScheduleForWorker> tmp = new ArrayList<ScheduleForWorker>();

		tmp.addAll(allMove_);
		allMove_.clear();
		allMove_.addAll(result_);
		result_.clear();
		result_.addAll(tmp);

		return travelsList_;
	}

	private Travel getTravelFromMap(int id)
	{
		return travels_.get(mapIdTravel_.get(id));
	}

	private int getFirstElement(List<Integer> src)
	{
		int tmp = src.get(0);

		src.remove(0);

		return tmp;
	}

	private List<List<Integer>> combinatory(int nb, List<Integer> srcList, List<Integer> deb, int totalworkTime)
	{
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		List<Integer> src = new ArrayList<Integer>();
		src.addAll(srcList);

		if ((nb > 0) && !(totalworkTime > maxWorkTime_) && !(src.isEmpty()))
		{
			while (src.size() >= nb)
			{
				int idNextTravel = -1;
				int diffTime = totalworkTime;

				if (deb.size () > 0)
				{
					Travel start = getTravelFromMap(deb.get(0));
					Travel end = getTravelFromMap(deb.get(deb.size() - 1));

					while (!src.isEmpty())
					{
						idNextTravel = getFirstElement(src);
						Travel tmp = getTravelFromMap(idNextTravel);
						diffTime = totalworkTime + ScheduleUtile.substractTime(tmp.endT, tmp.startT);

						if ((diffTime <= maxWorkTime_)
								&& (ScheduleUtile.substractTime(tmp.endT, start.startT) <= maxFullTime_)
								&& (end.end.compareTo(tmp.start) == 0)
								&& (ScheduleUtile.greaterThan(end.endT, tmp.startT)))
							break;
						else
							idNextTravel = -1;
					}

					if (idNextTravel == -1)
						break;
				}
				else
				{
					idNextTravel = getFirstElement(src);
					Travel tmp = getTravelFromMap(idNextTravel);
					diffTime = totalworkTime + ScheduleUtile.substractTime(tmp.endT, tmp.startT);
				}

				List<Integer> newDeb = new ArrayList<Integer>();
				newDeb.addAll(deb);
				newDeb.add(idNextTravel);

				List<List<Integer>> comb = combinatory(nb - 1, src, newDeb, diffTime);

				if (comb.size() > 0)
					res.addAll(comb);
			}
		}
		else
		{
			res.add(0, deb);
		}

		return res;
	}

	private boolean stopCondition(float energy)
	{
		energys_.add(energy);

		if (energys_.size() > 10)
			energys_.remove(0);

		boolean sameEnergy = true;

		if (energys_.size() < 10)
			sameEnergy = false;
		else
		{
			Iterator<Float> it = energys_.iterator();
			Float fl;

			while (it.hasNext())
			{
				fl = it.next();

				if (fl != energy)
				{
					sameEnergy = false;
					break;
				}
			}
		}

		boolean res = (energy < maxWorker_ / 5 || sameEnergy);

		return res;
	}

	void AddTabou(ScheduleForWorker ws)
	{
		tabous_.add(ws);

		if (tabous_.size() >= maxNbTabou_)
			tabous_.remove(0);
	}

	private boolean IsTabou(ScheduleForWorker ws)
	{
		ScheduleForWorker tmp;
		boolean b = false;

		for (Iterator<ScheduleForWorker> it = tabous_.iterator(); it.hasNext();)
		{	
			tmp = it.next();

			if (ScheduleUtile.isEqual(tmp, ws))
				b = true;
		}

		return b;
	}

	private boolean isValid(ScheduleForWorker ws)
	{
		if (ws.travelId.size() > maxDay_ || ws.Id < 0 || ws.Id > maxWorker_)
			return false;
		else if (!ws.isActif)
			return true;

		List<List<Travel>> travels = new ArrayList<List<Travel>>();

		//construct travels vector
		for (int i = 0; i < maxDay_; ++i)
		{
			if (ws.travelId.get(i) < 0)
			{
				travels.add(new ArrayList<Travel>());
				continue;
			}

			if (ws.travelId.get(i) >= travelsList_.size())
				return false;

			travels.add(new ArrayList<Travel>());
			List<Integer> idsTravel = travelsList_.get(ws.travelId.get(i));
			Integer inttmp;

			for (Iterator<Integer> it = idsTravel.iterator(); it.hasNext();)
			{
				inttmp = it.next();
				travels.get(i).add(getTravelFromMap(inttmp));
			}
		}

		Set<Integer> travelDone = new HashSet<Integer>();

		for (int i = 0; i < maxDay_; ++i)
		{
			if (ws.travelId.get(i) < 0)
			{
				travels.add(new ArrayList<Travel>());
				continue;
			}

			if (ws.travelId.get(i) >= travelsList_.size())
				return false;

			travels.add(new ArrayList<Travel>());
			List<Integer> idsTravel = travelsList_.get(ws.travelId.get(i));

			Integer idInt;

			for (Iterator<Integer> it = idsTravel.iterator(); it.hasNext();)
			{
				idInt = it.next();

				if (travelDone.contains(idInt))
					return false;
				else
					travelDone.add(idInt);
			}
		}

		//check continus path over days
		for (int day = 0; day < maxDay_ - 1; ++day)
		{
			if (travels.get(day).size() > 0 && travels.get(day + 1).size() > 0)
				if (travels.get(day).get(travels.get(day).size() - 1).end.compareTo(travels.get(day + 1).get(0).start) != 0)
					return false;
		}

		return true;
	}

	private void MakeMove(ScheduleForWorker mv)
	{
		boolean added = false;

		for (int i = 0; i < allMove_.size(); ++i)
		{
			if (allMove_.get(i).Id == mv.Id)
			{
				allMove_.set(i, mv);
				added = true;
			}
		}

		// Remove done travels by others workers
		for (int i = 0; i < allMove_.size(); ++i)
		{
			if (allMove_.get(i).isActif)
			{
				for (int day = 0; day < mv.travelId.size (); day++)
				{
					if (mv.travelId.get(day) == -1)
						continue;

					List<Integer> tmp = travelsList_.get(mv.travelId.get(day));

					if (tmp.isEmpty())
						continue;

					for (int day2 = 0; day2 < allMove_.get(i).travelId.size (); day2++)
					{
						if (allMove_.get(i).travelId.get(day2) == -1)
							continue;

						List<Integer> tmp2 = travelsList_.get(allMove_.get(i).travelId.get(day2));

						if (!tmp2.isEmpty ())
						{
							Iterator<Integer> itInt = tmp.iterator();
							Integer tmpInt = 0;

							while (itInt.hasNext())
							{
								tmpInt = itInt.next();

								if (tmp2.contains(tmpInt))
								{
									allMove_.get(i).travelId.set(day2, -1);
									break;
								}
							}
						}
					}
				}
			}
		}

		if (!added)
			allMove_.add(mv);
	}

	void FillCandidateList(List<ScheduleForWorker> cand)
	{
		cand.clear();

		for (int i = 0; i < allMove_.size(); ++i)
		{
			ScheduleForWorker move = allMove_.get(i);
			move.isActif = !move.isActif;
			cand.add (move);
		}

		for (int workerID = -1; workerID < 2; ++workerID)
		{
			if (bestMove_.Id + workerID < 0)
				break;

			for (int dayID = 0; dayID < maxDay_; ++dayID)
			{
				List<Integer> noTravels = new ArrayList<Integer>(bestMove_.travelId);
				noTravels.set(dayID, -1);

				ScheduleForWorker moveWithNoTravel = new ScheduleForWorker(bestMove_.Id + workerID, noTravels);
				cand.add(moveWithNoTravel);

				for (int travelID = -maxTarget_ * 2; travelID < maxTarget_ * 2; ++travelID)
				{
					if (bestMove_.travelId.get(dayID) + travelID < 0)
						continue;

					List<Integer> travels = new ArrayList<Integer>(bestMove_.travelId);
					travels.set(dayID, travels.get(dayID) + travelID);
					ScheduleForWorker move = new ScheduleForWorker(bestMove_.Id + workerID, travels);

					if (isValid(move))
						cand.add(move);
				}
			}
		}
	}

	private int CalculateTravelNb(List<Set<Integer>> travelsID)
	{
		int travelsNb = 0;

		for (int day = 0; day < maxDay_; ++day)
		{
			Iterator<Integer> it = travelsID.get(day).iterator();
			Integer idTravel;

			while (it.hasNext())
			{
				idTravel = it.next();
				List<Integer> travelIds = travelsList_.get(idTravel);

				for (Iterator<Integer> it2 = travelIds.iterator(); it2.hasNext();)
				{
					Integer int2 = it2.next();
					travelsNb++;
				}
			}
		}

		return travelsNb;
	}

	private float DefineEnergy(int totalTrajet, int workerNb, int travelNb, float averageTravel)
	{
		float coef = 1;

		if (averageTravel < 1)
			coef = 3;

		return coef * workerNb + (totalTrajet - travelNb);
	}

	private float GetEnergy()
	{
		Set<Integer> workerID = new HashSet<Integer>();
		List<Set<Integer>> travelsID = new ArrayList<Set<Integer>>();

		for (int i = 0; i < maxDay_; i++)
			travelsID.add(new HashSet<Integer>());

		List<Integer> travelsByWorker = new ArrayList<Integer>();

		for (int i = 0; i < allMove_.size(); ++i)
		{
			if (allMove_.get(i).isActif)
			{
				travelsByWorker.add(0);
				workerID.add(allMove_.get(i).Id);

				for (int day = 0; day < maxDay_; ++day)
				{
					int travelID = allMove_.get(i).travelId.get(day);

					if (travelID != -1)
					{
						travelsByWorker.set(travelsByWorker.size() - 1, travelsByWorker.get(travelsByWorker.size() - 1) + 1);
						travelsID.get(day).add(travelID);
					}
				}
			}
		}

		float averageTravel = 0;

		for (int i = 0; i < travelsByWorker.size(); ++i)
			averageTravel += travelsByWorker.get(i);

		averageTravel /= travelsByWorker.size();

		return DefineEnergy(totaltravels_, workerID.size(), CalculateTravelNb(travelsID), averageTravel);
	}

	private float GetEnergyFromMove(ScheduleForWorker mv)
	{
		Set<Integer> workerID = new HashSet<Integer>();
		List<Set<Integer>> travelsID = new ArrayList<Set<Integer>>(); //travelsID / Day

		for (int i = 0; i < maxDay_; i++)
			travelsID.add(new HashSet<Integer>());

		List<Integer> travelsByWorker = new ArrayList<Integer>();
		List<RemoveTravel> removed = new ArrayList<RemoveTravel>();

		// Remove done travels by others workers
		for (int i = 0; i < allMove_.size(); ++i)
		{
			if (allMove_.get(i).isActif)
			{
				for (int day = 0; day < mv.travelId.size (); day++)
				{
					if (mv.travelId.get(day) == -1)
						continue;

					List<Integer> tmp = travelsList_.get(mv.travelId.get(day));

					if (tmp.isEmpty())
						continue;

					for (int day2 = 0; day2 < allMove_.get(i).travelId.size (); day2++)
					{
						if (allMove_.get(i).travelId.get(day2) == -1)
							continue;

						List<Integer> tmp2 = travelsList_.get(allMove_.get(i).travelId.get(day2));

						if (!tmp2.isEmpty())
						{
							Iterator<Integer> it = tmp.iterator();

							while (it.hasNext())
							{
								Integer integer = it.next();

								if (tmp2.contains(integer))
								{
									removed.add(new RemoveTravel(i, day2, allMove_.get(i).travelId.get(day2)));
									allMove_.get(i).travelId.set(day2, -1);
									break;
								}
							}
						}
					}
				}
			}
		}

		boolean added = false;

		for (int i = 0; i < allMove_.size(); ++i)
		{
			if (allMove_.get(i).isActif)
			{
				travelsByWorker.add(0);
				workerID.add(allMove_.get(i).Id);

				if (allMove_.get(i).Id == mv.Id)
				{
					added = true;

					for (int day = 0; day < maxDay_; ++day)
					{
						int travelID = mv.travelId.get(day);

						if (travelID != -1)
						{
							travelsByWorker.set(travelsByWorker.size() - 1, travelsByWorker.get(travelsByWorker.size() - 1) +  1);
							travelsID.get(day).add(travelID);
						}
					}
				}
				else
				{
					for (int day = 0; day < maxDay_; ++day)
					{
						int travelID = allMove_.get(i).travelId.get(day);

						if (travelID != -1)
						{
							travelsByWorker.set(travelsByWorker.size() - 1, travelsByWorker.get(travelsByWorker.size() - 1) +  1);
							travelsID.get(day).add(travelID);
						}
					}
				}
			}
		}

		if (!added && mv.isActif)
		{
			travelsByWorker.add(0);
			workerID.add(mv.Id);

			for (int day = 0; day < maxDay_; ++day)
			{
				int travelID = mv.travelId.get(day);

				if (travelID != -1)
				{
					travelsByWorker.set(travelsByWorker.size() - 1, travelsByWorker.get(travelsByWorker.size() - 1) +  1);
					travelsID.get(day).add(travelID);
				}
			}
		}

		float averageTravel = 0;

		for (int i = 0; i < travelsByWorker.size(); ++i)
			averageTravel += travelsByWorker.get(i);

		averageTravel /= travelsByWorker.size();

		int energy = (int)DefineEnergy(totaltravels_, workerID.size(), CalculateTravelNb(travelsID), averageTravel);

		for (int i = 0; i < removed.size (); i++)
		{
			RemoveTravel r = removed.get(i);
			allMove_.get(r.index).travelId.set(r.dayId, r.travelId);
		}

		return energy;
	}
}
