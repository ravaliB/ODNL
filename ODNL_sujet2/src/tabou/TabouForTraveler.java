package tabou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import tool.ScheduleForWorker;
import tool.ScheduleUtile;
import tool.Time;
import tool.Travel;

public class TabouForTraveler {
	private int maxEltNb_;
	private int allMoveSetted_;
	private List<Integer> listTabou_;
	private List<Boolean> allMove_;
	private List<Travel> vt_;
	private List<ScheduleForWorker> resultMove_;
	private Queue<Travel> firstMove_;
	private Map<String, List<Travel>> mapEndPtTravelId_;

	public TabouForTraveler(int maxEltNb,
			int maxTravelId,
			List<ScheduleForWorker> result,
			List<Travel> vt)
	{
		this.maxEltNb_ = maxEltNb;
		this.listTabou_ = new ArrayList<Integer>();
		this.vt_ = vt;
		this.resultMove_ = result;
		this.allMove_ = new ArrayList<Boolean>(maxTravelId + 1);
		this.allMoveSetted_ = 0;

		RandomHelper.init(42);
		fillTravelMap();
		fillTravelQueue();
	}

	public void Run()
	{
		Map<Integer, String> workerMap = new HashMap<Integer, String>();
		List<ScheduleForWorker> resultTmp = new ArrayList<ScheduleForWorker>();
		List<Travel> candList = new ArrayList<Travel>();
		List<Travel> candListTmp = new ArrayList<Travel>();
		int workerCount = 0;
		int dayCount = 0;

		while (allMoveSetted_ != vt_.size())
		{
			++workerCount;
			workerMap.clear();

			for (int k = 1; k <= workerCount; k++)
			{
				workerMap.put(k, "");
			}

			reset();
			resultTmp.clear();
			// 5 days

			for (int i = 1; i <= 5; i++)
			{
				for (int j = 1; j <= workerCount; j++)
				{
					ScheduleForWorker ws = new ScheduleForWorker();
					ws.Id = j;
					ws.dayId = i;
					listTabou_.clear();
					Entry<Integer, String> it = null;
					
					for (Map.Entry<Integer, String> entry : workerMap.entrySet())
					{
						if (entry.getKey() == j)
							it = entry;
					}
					
					Travel t = getFirstMvAvailable(it.getValue());
					
					if (t == null)
					{
						workerMap.put(j, "");
						resultTmp.add(ws);
				
						continue;
					}
					
					makeMove(workerMap, ws, t);
					addTabou(t.id);
					/*
					 * Setup for a new worker
					 * Full time should not exceed 7 hours => 420 mins
					 * Work time should not exceed 5 hours => 300 mins
					 */
					Time firstStartTime = t.startT;
					Time lastEndTime = t.endT;
					int fullTime = ScheduleUtile.substractTime(t.endT, t.startT);
					int workTime = ScheduleUtile.substractTime(t.endT, t.startT);
			
					while (fullTime < 420 && workTime < 300)
					{
						fillCandidateList(it.getValue(), candList);

						float bestLocalProba = 0;
						boolean foundCand = false;
						Travel bestCand = null;
						// Looking for the best move
						for ( int i2 = 0; i2 < candList.size (); i2++)
						{
							Travel cand = candList.get(i);
							int tmpFullTime = ScheduleUtile.substractTime(cand.endT, firstStartTime);
							int tmpWorkTime = (workTime + ScheduleUtile.substractTime(cand.endT, cand.startT));
							int tmpDifTime = ScheduleUtile.substractTime(cand.startT, lastEndTime);
							
							if (tmpFullTime >= 420
									|| (tmpWorkTime >= 300)
									|| (tmpDifTime < 0))
								continue;
							
							fillCandidateList(cand.end, candListTmp);
							float energy = ScheduleUtile.substractTime(cand.startT, lastEndTime) * (candListTmp.size () == 0 ? 1 : (1 / candListTmp.size ()));
							float localProba = (float)Math.exp(-energy);

							/*
							 * If not in tabuuList, try move
							 * or it is in tabuuList but it will be the best proba ever had, take it out
							 * if the proba from the mv is better (>) than the best ever had, we take it
							 * if we found the same proba has the one we ever had, we can also take it we a chance of 1/2
							 */
							boolean inTabou = isTabou(cand.id);
	
							if (!inTabou
									|| (inTabou
											&& (localProba > bestLocalProba
													|| ((localProba == bestLocalProba)
															&& (RandomHelper.instance().getNext(0, 100) >= 50)))))
							{
								foundCand = true;
								bestCand = cand;
								bestLocalProba = localProba;
							}
						}
						if (!foundCand)
						{
							workerMap.put(j, "");
							break;
						}
						makeMove(workerMap, ws, bestCand);
						addTabou(bestCand.id);
						fullTime = ScheduleUtile.substractTime(bestCand.endT, firstStartTime);
						workTime += ScheduleUtile.substractTime(bestCand.endT, bestCand.startT);
					}
					resultTmp.add(ws);
				}
			}
		}

		List<ScheduleForWorker> tmp = new ArrayList<ScheduleForWorker>();
		tmp.addAll(resultMove_);
		resultMove_.clear();
		resultMove_.addAll(resultTmp);
		resultTmp.clear();
		resultTmp.addAll(tmp);
	}

	private void reset()
	{
		allMove_.set(allMove_.size(), false);
		allMoveSetted_ = 0;
	}

	private Travel getFirstMvAvailable(String endPoint)
	{
		if (endPoint.length() == 0)
		{
			if (!firstMove_.isEmpty ())
			{
				Travel move = firstMove_.peek();
				firstMove_.poll();

				return move;
			}

			for ( int i = 0; i < vt_.size(); i++)
			{
				if (!allMove_.get(vt_.get(i).id))
					return vt_.get(i);
			}
		}
		else
		{
			Entry<String, List<Travel>> entry = null;

			for (Map.Entry<String, List<Travel>> mapPt : mapEndPtTravelId_.entrySet())
			{
				if (mapPt.getKey().compareTo(endPoint) == 0)
					entry = mapPt;
			}


			if (entry == null)
				return null;

			List<Travel> v = entry.getValue();

			for ( int i = 0; i < v.size (); i++)
			{
				if (!allMove_.get(v.get(i).id))
					return v.get(i);
			}
		}

		return null;
	}

	private void addTabou(int travelId)
	{
		listTabou_.add(travelId);

		if (listTabou_.size() >= maxEltNb_)
			listTabou_.remove(0);
	}

	private boolean isTabou(int travelId)
	{
		for (Iterator<Integer> it = listTabou_.iterator(); it.hasNext();)
		{
			Integer tmp = it.next();

			if (tmp == travelId)
				return true;
		}

		return false;
	}

	private void fillTravelQueue()
	{
		for ( int i = 0; i < vt_.size (); i++)
		{
			if (mapEndPtTravelId_.containsKey(vt_.get(i).start))
				continue;

			firstMove_.add(vt_.get(i));
		}
	}

	private void fillTravelMap()
	{
		for ( int i = 0; i < vt_.size (); i++)
		{
			List<Travel> candList = new ArrayList<Travel>();

			if (mapEndPtTravelId_.containsKey(vt_.get(i).end))
				continue;

			for ( int j = 0; j < vt_.size (); j++)
			{
				if (j == i || vt_.get(j).start.compareTo(vt_.get(i).end) != 0)
					continue;

				candList.add(vt_.get(j));
			}

			mapEndPtTravelId_.put(vt_.get(i).end, candList);
		}
	}

	private void fillCandidateList(String endPoint, List<Travel> cand)
	{
		cand.clear();

		Entry<String, List<Travel>> it = null;

		for (Map.Entry<String, List<Travel>> entry : mapEndPtTravelId_.entrySet())
		{
			if (entry.getKey().compareTo(endPoint) == 0)
				it = entry;
		}

		List<Travel> v = it.getValue();

		for ( int i = 0; i < v.size (); i++)
		{
			if (allMove_.contains(v.get(i).id))
				continue;

			cand.add(v.get(i));
		}
	}

	private void makeMove(Map<Integer, String> wm,
			ScheduleForWorker ws,
			Travel travel)
	{
		wm.put(ws.Id, travel.end);
		ws.travelId.add(travel.id);
		allMove_.set(travel.id, true);
		allMoveSetted_++;
	}
}
