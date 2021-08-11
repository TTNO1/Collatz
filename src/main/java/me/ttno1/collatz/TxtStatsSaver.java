package me.ttno1.collatz;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TxtStatsSaver implements StatsSaver {

	private StatsHolder statsHolder;
	
	private File logFile;
	
	private final long interval;
	
	public TxtStatsSaver(StatsHolder statsHolder, File logFile, long interval) throws IOException {
		
		this.statsHolder = statsHolder;
		
		this.interval = interval;
		
		this.logFile = logFile;
		
		if(logFile == null) {
			
			throw new IllegalArgumentException("Log file cannot be null.");
			
		}
		
		logFile = logFile.isDirectory() ? new File(logFile, "Collatz-Log.txt") : logFile;
		
		if(logFile.getName().toLowerCase().endsWith(".txt")) {
			
			throw new IllegalArgumentException("Log file must be a \".txt\" file.");
			
		}
		
		logFile.createNewFile();
		
	}

	public void run() {

		while(!Thread.interrupted()) {

			try {

				statsHolder.waitUnitlFull(interval, TimeUnit.MILLISECONDS);

				System.out.println("Saving...");
				
				logStats();

				System.out.println("Saved");

			} catch (IOException e) {
				
				System.out.println("An error occured while trying to save results.");
				
				e.printStackTrace();
				
				//TODO Data Recovery

			} catch (InterruptedException e) {
				
				break;
				
			}
			
		}
		
		try {
			
			System.out.println("Saving...");
			
			logStats();
			
			System.out.println("Saved");
			
		} catch (IOException e1) {
			
			System.out.println("An error occured while trying to save results.");
			
			e1.printStackTrace();
			
			//TODO Data Recovery
			
		}

	}
	
	@Override
	public synchronized void logStats() throws IOException {
		
    	FileWriter writer = new FileWriter(logFile, true);
    	
    	for(long startNum : statsHolder.getStartingNums()) {
    	
    		if(statsHolder.getSteps(startNum).length == 0 || statsHolder.getSteps(startNum)[statsHolder.getSteps(startNum).length - 1] != 1) {
    			
    			continue;
    			
    		}
    		
    		StringBuilder builder = new StringBuilder(startNum + ":");
    		
    		for(long step : statsHolder.getSteps(startNum)) {
    			
    			builder.append(step + ",");
    			
    		}
    		
    		if(builder.charAt(builder.length() - 1) == ',') {
    			
    			builder.deleteCharAt(builder.length() - 1);
    			
    		}
    		
    		writer.write(builder.toString());
    		
    		statsHolder.clearPath(startNum);
    		
    	}
        
    	writer.close();
        
    }
	
}
