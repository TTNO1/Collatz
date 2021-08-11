package me.ttno1.collatz;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface StatsHolder {

	public Long[] getSteps(long startNum);
	
	public Map<Long, Long[]> getPathStepsMap();
	
	public Long[] getStartingNums();
	
	public long getTotalSteps();
	
	public void addStepToPath(long startNum, long step);
	
	public void addStepToPath(long startNum, Long ... steps);
	
	public void clearPath(long startNum);
	
	public void clear();
	
	public void waitUnitlFull(long timeout, TimeUnit unit) throws InterruptedException;
	
}
