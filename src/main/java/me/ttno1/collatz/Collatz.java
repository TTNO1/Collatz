package me.ttno1.collatz;

import java.io.File;
import java.io.IOException;

class Collatz {
    
    public static void main(String[] args) {
    	
        if(args.length == 0 || args[0] == null || args[0] == "") {
        	
        	throw new IllegalArgumentException("Please provide a directory to save log files as first command line argument.");
        	
        }
        
        File logFile = new File(args[0]);
        
        if(!logFile.isDirectory()) {
        	
        	System.out.println("The provided log save location is not a folder. Will attempt to use parent directory of file as new save location. Pausing for 10 seconds...");
        	
        	try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				
			}
        	
        	logFile = logFile.getParentFile();
        	
        	if(logFile == null) {
            	
            	throw new IllegalArgumentException("The Provided log save location does not have a parent directory to fall back on.");
            	
            }
        	
        }
        
        Computer[] computers = {new Computer(1, (long) Math.pow(2, 22), 2), new Computer(2, (long) Math.pow(2, 22), 2)};
        
        Thread[] computerThreads = new Thread[computers.length];
        
        for(int i = 0; i < computers.length; i++) {
        	
        	computerThreads[i] = new Thread(computers[i]);
        	
        	computerThreads[i].start();
        	
        }
        
    	StatsLogger statsLogger;
    	
		try {
			statsLogger = new StatsLogger(computers, logFile);
		} catch (IOException e) {
			System.out.println("Something went wrong while trying to create/access the log files.");
			e.printStackTrace();
			return;
		}
    	
    	Thread loggerThread = new Thread(statsLogger);
    	
    	loggerThread.start();
        
        ProgressLogger progressLogger = new ProgressLogger(computers);
        
        Thread progressThread = new Thread(progressLogger);
        
        progressThread.setDaemon(true);
        
        progressThread.start();
        
        for(Thread thread : computerThreads) {
        	
        	try {
				thread.join();
			} catch (InterruptedException e) {
				break;
			}
        	
        }
        
        loggerThread.interrupt();
        
    }
    
}