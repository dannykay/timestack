package org.dkay229.timestack.graphics.event;

import java.util.Arrays;

public class Event {
	long tStart;
	long tEnd;
	int attributes[];
	public Event(long tStart, long tEnd, int[] attributes) {
		super();
		this.tStart = tStart;
		this.tEnd = tEnd;
		this.attributes = attributes;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(attributes);
		result = prime * result + (int) (tEnd ^ (tEnd >>> 32));
		result = prime * result + (int) (tStart ^ (tStart >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (!Arrays.equals(attributes, other.attributes))
			return false;
		if (tEnd != other.tEnd)
			return false;
		if (tStart != other.tStart)
			return false;
		return true;
	}
	
}
