package me.ttno1.collatz;

public interface StatsSaver extends Runnable {
	
	public void logStats() throws Exception;
	
}
