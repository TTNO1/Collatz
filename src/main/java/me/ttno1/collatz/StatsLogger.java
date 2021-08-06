package me.ttno1.collatz;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsLogger implements Runnable {

	private Computer[] computers;
	
	private File logFile;
	
	private File instancesFile;
	
	private File stepsFile;
	
	private File resultsFile;
	
	public StatsLogger(Computer[] computers, File logFile) throws IOException {
		
		this.computers = computers;
		
		this.logFile = logFile;
		
		logFile = logFile.isDirectory() ? logFile : logFile.getParentFile();
		
		if(logFile == null) {
			
			throw new IllegalArgumentException("Log File Must Be A Directory And Cannot Be Null.");
			
		}
		
		instancesFile = new File(logFile, "instances.txt");
		
		stepsFile = new File(logFile, "steps.txt");
		
		resultsFile = new File(logFile, "results.txt");
		
		instancesFile.createNewFile();
		
		stepsFile.createNewFile();
		
		resultsFile.createNewFile();
		
	}

	public void run() {
		
		while(!Thread.interrupted()) {
			System.out.println("Logged");
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				break;
			}


			try {
				logStats();
			} catch (IOException e) {
				e.printStackTrace();
			}


		}
		
		try {
			logStats();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void logStats() throws IOException {
        
		List<Map<Long, Long>> instancesMaps = new ArrayList<Map<Long, Long>>();
		
		List<Map<Long, Long>> stepsMaps = new ArrayList<Map<Long, Long>>();
		
		List<Map<Long, Boolean>> resultsMaps = new ArrayList<Map<Long, Boolean>>();
		
		for(Computer computer : computers) {
			
			synchronized(computer.getInstances()) {
				
				instancesMaps.add(new HashMap<Long, Long>(computer.getInstances()));
				
				computer.getInstances().clear();
				
			}
			
			synchronized(computer.getSteps()) {
				
				stepsMaps.add(new HashMap<Long, Long>(computer.getSteps()));
				
				computer.getSteps().clear();
				
			}
			
			synchronized(computer.getResults()) {
				
				resultsMaps.add(new HashMap<Long, Boolean>(computer.getResults()));
				
				computer.getResults().clear();
				
			}
			
		}
		
    	FileWriter instancesWriter = new FileWriter(instancesFile, true);
    	
    	for(Map<Long, Long> map : instancesMaps) {
    	
    		for(Map.Entry<Long, Long> entry : map.entrySet()) {

    			instancesWriter.write("(" + entry.getKey() + "," + entry.getValue() + ")" + System.lineSeparator());

    		}
    		
    		
    		
    	}
        
    	instancesWriter.close();
    	
    	instancesMaps.clear();
        
    	FileWriter stepsWriter = new FileWriter(stepsFile, true);
        
    	for(Map<Long, Long> map : stepsMaps) {
    	
        	for(Map.Entry<Long, Long> entry : map.entrySet()) {

        		stepsWriter.write("(" + entry.getKey() + "," + entry.getValue() + ")" + System.lineSeparator());

        	}

    	}
        
        stepsWriter.close();
        
        stepsMaps.clear();
        
        FileWriter resultsWriter = new FileWriter(resultsFile, true);
        
        for(Map<Long, Boolean> map : resultsMaps) {
        
        	for(Map.Entry<Long, Boolean> entry : map.entrySet()) {
        		
        		resultsWriter.write("(" + entry.getKey() + "," + entry.getValue() + ")" + System.lineSeparator());

        	}

        }
        
        resultsWriter.close();
        
        resultsMaps.clear();
        
    }
	
}
