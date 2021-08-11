package me.ttno1.collatz;

public interface ProgressLogger extends Runnable {

	public double getProgress();
	
	public void logProgress() throws Exception;
	
}
