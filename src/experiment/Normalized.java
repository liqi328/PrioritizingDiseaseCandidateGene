package experiment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 归一化 a * score1 + (1- a)*score2
 * @author Liqi
 *
 */
public class Normalized {
	private double a_threshhold = 0.5;
	
	public void setAthreshhold(double a){
		this.a_threshhold = a;
	}
	
	public double getAthreshhold(){
		return this.a_threshhold;
	}
	
	/**
	 * 对两种方法的到的排名进行归一化处理, 得到新的rankList
	 * @param method1_rankList
	 * @param method2_rankList
	 * @return
	 */
	public List<Rank> run(List<Rank> method1_rankList, List<Rank> method2_rankList){
		Map<Integer, Rank> method2_rankMap = new HashMap<Integer, Rank>();
		Iterator<Rank> goItr = method2_rankList.iterator();
		while(goItr.hasNext()){
			Rank rank = goItr.next();
			method2_rankMap.put(rank.getId(), rank);
		}
		
		double method1_rank_max = max(method1_rankList);
		double method2_rank_max = max(method2_rankList);
		
		double newScore = 0.0;
		Rank method1_rank = null;
		Rank method2_rank = null;
		Iterator<Rank> vsItr = method1_rankList.iterator();
		while(vsItr.hasNext()){
			method1_rank = vsItr.next();
			method2_rank = method2_rankMap.get(method1_rank.getId());
			/*
			 * 归一化, 得到新的分数
			 * */
			newScore = a_threshhold * (method1_rank.getScore() / method1_rank_max) +
					(1 - a_threshhold) * (method2_rank.getScore() / method2_rank_max);
			method1_rank.setScore(newScore);
		}
		
		return method1_rankList;
	}
	
	/**
	 * 找 rankList中的最大值
	 * @param rankList
	 * @return
	 */
	private double max(List<Rank> rankList){
		double max = -1;
		Iterator<Rank> itr = rankList.iterator();
		Rank rank = null;
		while(itr.hasNext()){
			rank = itr.next();
			if(rank.getScore() > max){
				max = rank.getScore();
			}
		}
		
		return max;
	}
}
