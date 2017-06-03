package org.dkay229.attribset;

import java.util.Arrays;

public class Record {
	int[] attributes;
	public Record(int[] attributes) {
		super();
		this.attributes = attributes;
	}
	public String toString(AttributeSet attSet)
	{
		String attStr=null;
		if (attSet != null) {
			StringBuilder sb=new StringBuilder();
			sb.append("[");
			for (int i:attributes) {
				sb.append(attSet.getAttributeName(i));
				sb.append("=");
				sb.append(attSet.getAttributeValue(i));
				sb.append(",");
			}
			attStr=sb.toString().replaceFirst(",$", "]");
		} else {
			attStr=Arrays.toString(attributes);
		}
		return "Record "+attStr;
	}
	@Override
	public String toString() {
		return toString(null);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(attributes);
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
		Record other = (Record) obj;
		if (!Arrays.equals(attributes, other.attributes))
			return false;
		return true;
	}
}
