package me.ttno1.collatz;

import java.util.Objects;

public class PathCoordinate {

	private long startNum;
	//Inclusive
	private long index;
	
	public PathCoordinate(long startNum, long index) {
		
		this.startNum = startNum;
		
		this.index = index;
		
	}
	
	public long getStartNum() {
		
		return startNum;
		
	}
	
	public long getIndex() {
		
		return index;
		
	}
	
	public String getCode() {
		
		return String.format("@%d:%d", startNum, index);
		
	}
	
	@Override
	public boolean equals(Object object) {
		
		if(object == this) {
			
			return true;
			
		}
		
		if(!(object instanceof PathCoordinate)) {
			
			return false;
			
		}
		
		PathCoordinate pc = (PathCoordinate) object;
		
		return startNum == pc.getStartNum() && index == pc.getIndex();
		
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(startNum, index);
		
	}
	
}
