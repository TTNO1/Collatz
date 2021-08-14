package me.ttno1.collatz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class HashMapStatsHolder implements StatsHolder {

	private Map<Long, List<Long>> pathsMap;
	
	private Map<Long, PathCoordinate> referenceMap;
	
	private Object cacheFullObj;
	
	private long cacheSize;
	
	public HashMapStatsHolder(long cacheSize) {
		
		cacheFullObj = new Object();
		
		this.cacheSize = cacheSize;
		
		pathsMap = new HashMap<Long, List<Long>>();
		
		referenceMap = new HashMap<Long, PathCoordinate>();
		
	}
	
	/*public HashMapStatsHolder(long maxNum, long cacheSize, Map<Long, List<Long>> pathsMap) {
		
		this(maxNum, cacheSize);
		
		pathsMap.putAll(pathsMap);
		
		checkFull();
		
	}*/
	
	private synchronized void initializePath(long startNum) {
		
		pathsMap.putIfAbsent(startNum, new ArrayList<Long>());
		
	}
	
	@Override
	public synchronized Long[] getSteps(long startNum) {
		
		return pathsMap.get(startNum).toArray(new Long[0]);
		
	}
	
	@Override
	public PathCoordinate getReference(long startNum) {
		
		return referenceMap.get(startNum);
		
	}
	
	@Override
	public void setReference(long startNum, PathCoordinate reference) {
		
		referenceMap.put(startNum, reference);
		
	}
	
	@Override
	public PathCoordinate getFirstComputed(long num) {
		
		for(Entry<Long, List<Long>> entry : pathsMap.entrySet()) {
			
			int index = entry.getValue().indexOf(num);
			
			if(index > -1) {
				
				return new PathCoordinate(entry.getKey(), index);
				
			}
			
		}
		
		return null;
		
	}
	
	@Override
	public synchronized Map<Long, Long[]> getPathStepsMap() {
		
		Map<Long, Long[]> result = new HashMap<Long, Long[]>();
		
		for(Entry<Long, List<Long>> entry : pathsMap.entrySet()) {
			
			result.put(entry.getKey(), entry.getValue().toArray(new Long[0]));
			
		}
		
		return result;
		
	}
	
	@Override
	public synchronized Long[] getStartingNums() {
		
		List<Long> results = new ArrayList<Long>();
		
		for(Entry<Long, List<Long>> entry : pathsMap.entrySet()) {
			
			if(!entry.getValue().isEmpty()) {
				
				results.add(entry.getKey());
				
			}
			
		}
		
		return results.toArray(new Long[0]);
		
	}
	
	@Override
	public synchronized long getTotalSteps() {
		
		long total = 0;
		
		for(List<Long> list : pathsMap.values()) {
			
			total += list.size();
			
		}
		
		return total;
		
	}
	
	@Override
	public synchronized void addStepToPath(long startNum, long step) {
		
		initializePath(startNum);
		
		if(!pathsMap.get(startNum).contains(step)) {
			
			pathsMap.get(startNum).add(step);
			
		}
		
		checkFull();
		
	}
	
	@Override
	public synchronized void addStepToPath(long startNum, Long ... steps) {
		
		for(Long step : steps) {
			
			addStepToPath(step);
			
		}
		
	}
	
	@Override
	public synchronized void clearPath(long startNum) {
		
		pathsMap.remove(startNum);
		
	}

	@Override
	public synchronized void clear() {
		
		pathsMap.clear();
		
	}

	@Override
	public synchronized void waitUnitlFull(long timeout, TimeUnit unit) throws InterruptedException {
		
		long deadline = System.nanoTime() + unit.toNanos(timeout);
		//TODO Maybe change from getTotalSteps() to something more optimized
		while(getTotalSteps() < cacheSize && System.nanoTime() < deadline) {
			
			cacheFullObj.wait(timeout);
			
		}
		
	}
	
	private synchronized void checkFull() {
		
		if(getTotalSteps() >= cacheSize) {
			
			cacheFullObj.notifyAll();
			
		}
		
	}
	
}
