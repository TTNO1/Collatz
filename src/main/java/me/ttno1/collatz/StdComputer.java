package me.ttno1.collatz;

public class StdComputer implements Computer {
    
    private long currentNum;
    
    private final long start;
    
    private final long increment;
    
    private final long endNum;
    
    private StatsHolder statsHolder;
    
    private Object currentNumLock;
	
	public StdComputer(long start, long endNum, long increment, StatsHolder statsHolder) {
        
        this.start = start;
        
        this.endNum = endNum;
        
        this.increment = increment;
        
        this.statsHolder = statsHolder;
        
        currentNum = 0;
        
        currentNumLock = new Object();
		
	}
    
	@Override
    public long getStart() {
    	
		return start;
		
	}
    
	@Override
    public long getEndNum() {
    	
		return endNum;
		
	}
	
	@Override
	public long getIncrement() {
		
		return increment;
		
	}
	
	@Override
	public long getCurrentNum() {
    	
		synchronized(currentNumLock) {
		
			return currentNum;
		
		}
		
	}

	public void run() {
		
		for(currentNum = start; currentNum != endNum && !Thread.interrupted();) {
			
            doCollatz(currentNum);
            
            synchronized(currentNumLock) {
            	
            	currentNum += increment;
            	
            }
            
        }
		
	}
	
	@Override
	public void doCollatz(long input) {
        
        long num = input;
        
        while(num != 1) {
            
            if(num % 2 != 0) {
                //Odd
                num = num * 3 + 1;
            
            } else {
                //Even
                num = num / 2;
            
            }
            
            statsHolder.addStepToPath(input, num);
            
        }
        
    }
	
}
