package me.ttno1.collatz;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

class Collatz {
    //TODO Stop on exception in any class
    public static void main(String[] args) {
    	
    	Options options = new Options();
    	
    	options.addOption(Option.builder("f").longOpt("resultFile").desc("The file to wich results are saved.").hasArg().argName("file").type(File.class).required().build());
    	
    	options.addOption(Option.builder("sn").longOpt("startNumber").desc("The number to start computing from.").hasArg().argName("number").type(long.class).required().build());
    	
    	options.addOption(Option.builder("en").longOpt("endNumber").desc("The number to stop computing on.").hasArg().argName("number").type(long.class).required().build());
    	
    	options.addOption(Option.builder("si").longOpt("saveInterval").desc("The frequency (in milliseconds) with which results are saved.").hasArg().argName("interval").type(long.class).build());
    	
    	options.addOption(Option.builder("rc").longOpt("resultCache").desc("The size of the result cache (in # of entries). Results will be saved whenever it gets full.").hasArg().argName("cacheSize").type(long.class).build());
    	
    	options.addOption(Option.builder("pi").longOpt("progressInterval").desc("The frequency (in milliseconds) with which progress is logged.").hasArg().argName("interval").type(long.class).build());
    	
    	try {
    		
			CommandLine cmdln = new DefaultParser().parse(options, args, false);
			
		} catch (ParseException e1) {
			
			new HelpFormatter().printHelp("java -jar <Path-To-Jar>", options, true);
			
		}
    	
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
        
        StatsHolder statsHolder = new HashMapStatsHolder(15000);//TODO Cahce Size
        
        StatsSaver statsSaver;
    	
		try {
			
			statsSaver = new TxtStatsSaver(statsHolder, logFile, 5000);//TODO interval
			
		} catch (IOException e) {
			
			System.out.println("Something went wrong while trying to create/access the log file.");
			
			e.printStackTrace();
			
			return;
			
		}
    	
    	Thread saverThread = new Thread(statsSaver);
    	
    	saverThread.start();
        
        Computer[] computers = {new StdComputer(1, (long) Math.pow(2, 15), 2, statsHolder), new StdComputer(2, (long) Math.pow(2, 15), 2, statsHolder)};
        
        Thread[] computerThreads = new Thread[computers.length];
        
        for(int i = 0; i < computers.length; i++) {
        	
        	computerThreads[i] = new Thread(computers[i]);
        	
        	computerThreads[i].start();
        	
        }
        
        ProgressLogger progressLogger = new ConsoleProgressLogger(statsHolder, 2000, 2000); //TODO interval & maxnum
        
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
        
        progressThread.interrupt();
        
        saverThread.interrupt();
        
        try {
        	
			saverThread.join();
			
		} catch (InterruptedException e) {
			
		}
        
    }
    
}