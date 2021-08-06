package me.ttno1.collatz;

import java.text.DecimalFormat;

public class ProgressLogger implements Runnable {

	private Computer[] computers;
	
	private long maxNum;
	
	private long currentNum;
	
	public ProgressLogger(Computer[] computers) {
		
		this.computers = computers;
		
		maxNum = 0;
		
		for(Computer comp : computers) {
			
			if(comp.getMaxNum() > maxNum) {
				
				maxNum = comp.getMaxNum();
				
			}
			
		}
		
		currentNum = 0;
		
	}
	
	public void run() {
		
		DecimalFormat formatter = new DecimalFormat("###.###");
		
		while(currentNum != maxNum && !Thread.interrupted()) {
			
			currentNum = 0;
			
			for(Computer comp : computers) {
				
				currentNum += (comp.getCurrentNum() - comp.getStart() + 1) / comp.getIncrement();
				
			}
			
			System.out.println(currentNum + "/" + maxNum + " " + formatter.format((double)currentNum/(double)maxNum * 100) + "%");
			
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				
			}
			
		}
		
	}
	
}
