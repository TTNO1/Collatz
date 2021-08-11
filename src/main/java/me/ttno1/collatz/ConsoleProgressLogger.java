package me.ttno1.collatz;

import java.text.DecimalFormat;
import java.util.Arrays;

public class ConsoleProgressLogger implements ProgressLogger {
	
	private StatsHolder statsHolder;
	
	private final long maxNum;
	
	private final long interval;
	
	private long currentNum;
	
	public ConsoleProgressLogger(StatsHolder statsHolder, long interval, long maxNum) {
		
		this.statsHolder = statsHolder;
		
		this.interval = interval;
		
		this.maxNum = maxNum;
		
		currentNum = 0;
		
	}
	
	@Override
	public synchronized double getProgress() {
		
		Long[] sortedStartNums = statsHolder.getStartingNums();
		
		Arrays.sort(sortedStartNums);
		
		currentNum = sortedStartNums[sortedStartNums.length];
		
		return currentNum / maxNum;
		
	}

	@Override
	public synchronized void logProgress() throws Exception {
		
		DecimalFormat formatter = new DecimalFormat("###.####");
		
		String percentage = formatter.format(getProgress() * 100);
		
		System.out.println(currentNum + "/" + maxNum + " " + percentage + "%");
		
	}
	
	public void run() {
		
		while(currentNum != maxNum && !Thread.interrupted()) {
			
			try {
				
				logProgress();
				
				Thread.sleep(interval);
				
			} catch (InterruptedException e) {
				
				break;
				
			} catch (Exception e) {
				
				System.out.println("An error occured while trying to log progress.");
				
				e.printStackTrace();
				
			} 
			
		}
		
		System.out.println("Completed");
		
	}
	
}
