package me.ttno1.collatz;

public interface Computer extends Runnable {

	public long getStart();

	public long getEndNum();

	public long getIncrement();

	public long getCurrentNum();

	public void doCollatz(long input);

}
