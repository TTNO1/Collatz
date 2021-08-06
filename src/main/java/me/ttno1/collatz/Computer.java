package me.ttno1.collatz;

import java.util.HashMap;
import java.util.Map;

public class Computer implements Runnable {

	private Map<Long, Long> steps;
    
    private Map<Long, Long> instances;
    
    private Map<Long, Boolean> results;
    
    private long currentNum;
    
    private long start;
    
    private int increment;
    
    private long maxNum;
	
	public Computer(long start, long maxNum, int increment) {
		
		steps = new HashMap<Long, Long>();
        
        instances = new HashMap<Long, Long>();
        
        results = new HashMap<Long, Boolean>();
        
        this.start = start;
        
        this.maxNum = maxNum;
        
        this.increment = increment;
        
        currentNum = 0;
		
	}
    
    public long getStart() {
    	
		return start;
		
	}
    
    public long getMaxNum() {
    	
		return maxNum;
		
	}

	public int getIncrement() {
		
		return increment;
		
	}

	public long getCurrentNum() {
    	
		return currentNum;
		
	}

	public Map<Long, Long> getSteps() {
    	
    	return steps;
    	
    }
    
    public Map<Long, Long> getInstances() {
    	
    	return instances;
    	
    }
    
    public Map<Long, Boolean> getResults() {
    	
    	return results;
    	
    }

	public void run() {
		
		for(currentNum = start; currentNum <= maxNum; currentNum += increment) {
            
			if(Thread.interrupted()) {
				
				break;
				
			}
			
            doCollatz(currentNum);
            
        }
		
	}
	
	private boolean doCollatz(long input) {
        
        long num = input;
        
        long step = 0;
        
        while(num != 1) {
            
            if(num % 2 != 0) {
                //Odd
                num = num * 3 + 1;
            
            } else {
                //Even
                num = num / 2;
            
            }
            
            synchronized(instances) {
            	instances.put(num, instances.get(num) == null ? 1 : instances.get(num) + 1);
            }
            
            step++;
            
        }
        
        synchronized(steps) {
        	
        	steps.put(input, step);
        	
        }
        
        synchronized(results) {
        	
        	results.put(input, true);
        	
        }
        
        return true;
        
    }
	
}
