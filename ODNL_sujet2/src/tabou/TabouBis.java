package tabou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tool.ScheduleForWorker;
import tool.ScheduleUtile;
import tool.Travel;

public class TabouBis {
	private int maxTabouNb_;
	private List<Integer> listTabou_;
	private List<ScheduleForWorker> allMove_;
	private List<Travel> vt_;
	private List<ScheduleForWorker> result_;
	private int workerCount_;
	private int maxWorker_;
	private int maxDay_;
	private int maxTravelId_;

	private List<Integer> travelIdMovesIndex_;
	private Map<Integer, List<Integer>> mapTravelIdMoves_;
	private Map<Integer, Integer> mapTravelIdIndex_;
	private int visitedCount_;

	public TabouBis ( int maxTabouNb,
			int maxTravelId,
			List<ScheduleForWorker> result,
			List<Travel> vt)
	{
		this.maxTabouNb_ = maxTabouNb;
		this.listTabou_ = new ArrayList<Integer>();
		this.allMove_ = new ArrayList<ScheduleForWorker>();
		this.vt_ = vt;
		this.result_ = result;
		this.workerCount_ = 1;
		this.visitedCount_ = 0;
		this.maxDay_ = 5;
		this.maxWorker_ = maxTravelId;
		this.mapTravelIdIndex_ = new HashMap<Integer, Integer>();

		for (int i = 0; i < vt_.size (); i++)
			mapTravelIdIndex_.put(vt_.get(i).id, i);

		FillMapping();
	}

	public void Run()
	{
		float bestEnergy = GetEnergy();
		while(!stopCondition(workerCount_, bestEnergy))
		{
			// reset
			allMove_.clear ();
			listTabou_.clear ();
			visitedCount_ = 0;

			for ( int day = 1; day <= maxDay_; day++)
			{
				for ( int worker = 1; worker <= workerCount_; worker++)
				{
					float bestLocalProba = 0;
					ScheduleForWorker ws = new ScheduleForWorker(worker, day);
					ScheduleForWorker wsTmp = new ScheduleForWorker(worker, day);
					String end = GetLastEndPoint(worker, day);
					List<Integer> bestMove = new ArrayList<Integer>();
					//Best move selection
					for ( int i = 0; i < travelIdMovesIndex_.size (); i++)
					{
						int index = travelIdMovesIndex_.get(i);
						// Forbid same move as last one
						if (!allMove_.isEmpty ())
						{
							List<Integer> tmp = allMove_.get(allMove_.size () - 1).travelId;
							Travel last = getLastFromVector(mapTravelIdMoves_.get(index));
							Travel lastTmp = getLastFromVector(tmp);
							if (tmp.size () == mapTravelIdMoves_.get(index).size ()
									&& (tmp.equals(mapTravelIdMoves_.get(index)))
									|| (last != null && tmp.size () <= mapTravelIdMoves_.get(index).size () && (last.end.compareTo(lastTmp.end) == 0)))
								continue;
						}
						if (end.length () > 0
								&& vt_.get(mapTravelIdIndex_.get(mapTravelIdMoves_.get(index).get(0))).start.compareTo(end) != 0)
							continue;
						wsTmp.travelId = mapTravelIdMoves_.get(index);
						if (!isValid (wsTmp))
							continue;
						float localEnergy = GetEnergyFromMove (wsTmp);
						float localProba = GetProbaFromMove (wsTmp);

						boolean inTabou = IsTabou(mapTravelIdMoves_.get(index));
						if ((!inTabou && localProba > bestLocalProba)
								|| (inTabou && (localEnergy < bestEnergy)))
						{
							bestMove = mapTravelIdMoves_.get(index);
							bestLocalProba = localProba;
							if (bestEnergy >= localEnergy)
							{
								bestEnergy = localEnergy;
								break;
							}
						}
					}
					if (!bestMove.isEmpty ())
					{
						ws.travelId = bestMove;
						day = MakeMove(ws, true);
						AddTabou(bestMove);
					}
					else
						MakeMove(ws, false);
				}
			}

			++this.workerCount_;
		}
	}
	private boolean stopCondition(int workerCount, float energy)
	{
		if ((workerCount > maxWorker_) || (vt_.size () == visitedCount_) || energy < 25)
			return true;
		return false;
	}

	private String GetLastEndPoint(int workerId, int dayId)
	{
		if (!allMove_.isEmpty ())
			for (int i = allMove_.size () - 1; i >= 0 && i < allMove_.size (); i--)
			{
				if (allMove_.get(i).Id == workerId)
				{
					if ((allMove_.get(i).dayId == (dayId - 1)))
					{
						List<Integer> tId = allMove_.get(i).travelId;
						if (tId.isEmpty ())
							return "";
						return vt_.get(mapTravelIdIndex_.get(tId.get(tId.size () - 1))).end;
					}
					else
						break;
				}
			}
		return "";
	}

	private Travel getLastFromVector(List<Integer> tId)
	{
		if (tId.isEmpty())
			return null;
		else
			return vt_.get(mapTravelIdIndex_.get(tId.get(tId.size () - 1)));
	}

	private void AddTabou(List<Integer> tId)
	{
		for ( int i = 0; i < tId.size (); i++)
		{
			if (listTabou_.contains(tId.get(i)))
			{
				if (listTabou_.indexOf(tId.get(i)) == listTabou_.size())
				{
					listTabou_.add(tId.get(i));
					if (listTabou_.size() >= maxTabouNb_)
						listTabou_.remove(0);
				}
			}
		}
	}

	private boolean IsTabou(List<Integer> tId)
	{
		for (int i = 0; i < tId.size (); i++)
		{
			for (; i < tId.size (); i++)
				if (listTabou_.contains(tId.get(i)))
					if (listTabou_.indexOf(tId.get(i)) != listTabou_.size())
						return true;
		}

		return false;
	}

	private boolean IsValidMove(List<Integer> tId)
	{
		boolean b = false;

		if (tId.isEmpty ())
			b = false;
		if (tId.size () == 1)
			b = true;

		int idFirstMove = mapTravelIdIndex_.get(tId.get(0));
		int idLastMove = mapTravelIdIndex_.get(tId.get(tId.size() - 1));

		// compute full time for this schedule
		int totalFullTime = ScheduleUtile.substractTime(vt_.get(idLastMove).endT, vt_.get(idFirstMove).startT);
		if (totalFullTime > 420)
			b = false;

		// compute work time for this schedule
		int totalWorkTime = 0;
		for ( int i = 0; i < tId.size() - 1; ++i)
		{
			int tmpIdMove = mapTravelIdIndex_.get(tId.get(i));
			Travel tv = vt_.get(tmpIdMove);
			totalWorkTime += ScheduleUtile.substractTime(tv.endT, tv.startT);
		}

		if (totalWorkTime > 300)
			b = false;

		return b;
	}

	private boolean isValid(ScheduleForWorker ws)
	{
		for ( int i = 0; i < allMove_.size(); ++i)
		{
			if ((allMove_.get(i).travelId.size () == ws.travelId.size())
					&& ((getLastFromVector(allMove_.get(i).travelId).end.compareTo(getLastFromVector(ws.travelId).end) == 0)
							|| ((allMove_.get(i).travelId.equals(ws.travelId)))))
				return false;
		}

		return true;
	}

	private ScheduleForWorker alreadyMoved(List<Integer> tId)
	{
		Iterator<ScheduleForWorker> it = allMove_.iterator();
		ScheduleForWorker res = it.next();
		int dayMin = maxDay_ + 1;
		ScheduleForWorker sfw;

		while (it.hasNext())
		{
			sfw = it.next();

			List<Integer> tmp = sfw.travelId;

			for ( int i = 0; i < tId.size (); i++)
				if (tmp.contains(tId.get(i)))
				{	
					if (tmp.indexOf(tId.get(i)) != tmp.size())
					{
						if (sfw.dayId < dayMin)
						{
							dayMin = sfw.dayId;
							res = sfw;
						}
					}
				}
		}

		return res;
	}

	private void RemoveMove(ScheduleForWorker it)
	{
		ScheduleForWorker sfw;

		if (allMove_.contains(it))
		{
			for (int begin = allMove_.indexOf(it); begin < allMove_.size(); begin++)
			{
				sfw = allMove_.get(begin);
				visitedCount_ -= sfw.travelId.size();
				allMove_.remove(sfw);
			}
		}
	}

	private int MakeMove(ScheduleForWorker mv, boolean del)
	{
		int day = mv.dayId;

		if (del && !mv.travelId.isEmpty ())
		{
			ScheduleForWorker it = alreadyMoved(mv.travelId);

			if (mv.travelId.indexOf(it) < mv.travelId.size())
			{
				ScheduleForWorker sfw = it;
				day = sfw.dayId;
				RemoveMove(it);
			}
		}

		mv.dayId = day;
		allMove_.add(mv);
		visitedCount_ += mv.travelId.size ();

		return day;
	}

	private void FillMapping()
	{
		List<Integer> candList = new ArrayList<Integer>();

		for (int i = 0; i < vt_.size (); i++)
		{
			candList.clear();
			candList.add (vt_.get(i).id);

			for ( int j = 0; j < vt_.size (); j++)
			{
				if (j == i)
					continue;

				if ((ScheduleUtile.substractTime(vt_.get(j).startT, vt_.get(mapTravelIdIndex_.get(candList.get(candList.size() - 1))).endT) > 0)
						&& (vt_.get(j).start.compareTo(vt_.get(mapTravelIdIndex_.get(candList.get(candList.size()- 1))).end) == 0))
					candList.add(vt_.get(j).id);
			}

			while (!IsValidMove(candList))
				candList.remove(candList.size() - 1);

			if (IsValidMove(candList))
			{
				mapTravelIdMoves_.put(vt_.get(i).id, candList);
				travelIdMovesIndex_.add(vt_.get(i).id);
			}
		}
	}

	private float GetEnergy()
	{
		int remainingTravel = vt_.size () - visitedCount_;
		int remainingDay = (allMove_.isEmpty () ? maxDay_ : maxDay_ - allMove_.get(allMove_.size () - 1).dayId) + 1;

		return remainingTravel / remainingDay;
	}

	private float GetEnergyFromMove(ScheduleForWorker mv)
	{
		// Simulate
		ScheduleForWorker toErase = alreadyMoved(mv.travelId);
		List<ScheduleForWorker> copied = new ArrayList<ScheduleForWorker>();
		int energy = -1;
		int factor = 1;

		if (allMove_.indexOf(toErase) != allMove_.size())
		{
			ScheduleForWorker sfw = toErase;

			factor = (sfw.travelId.size () <= mv.travelId.size () ? 0 : 10);
			ScheduleForWorker it = toErase;

			for (int i = allMove_.indexOf(it); i < allMove_.size(); i++)
			{
				ScheduleForWorker copy = allMove_.get(i);
				copied.add(copy);
			}

			RemoveMove(toErase);
		}

		MakeMove(mv, false);

		// Get the energy
		energy = (int) Math.round(GetEnergy() / (copied.isEmpty () ? 1 : copied.size ()) * factor);

		// Rollback
		RemoveMove(allMove_.get(allMove_.size() - 1));

		if (!copied.isEmpty ())
			for (int i = 0; i < copied.size (); i++)
				MakeMove(copied.get(i), false);

		return energy;
	}


	private float GetProbaFromMove(ScheduleForWorker mv)
	{
		return (float) Math.exp(-GetEnergyFromMove(mv));
	}
}
