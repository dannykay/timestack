package org.dkay229.attribset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Descrete attributes of an event like: Username, Group, RecordDate. Each distinct value gets a unique code.
 * @author Danny
 *
 */
public class Attribute {
	private final int attrCode;
	private final String attrName;
	private final String attrLongDescr;
	private Comparator<String> sortFunc;
	private final AtomicInteger codeSequence=new AtomicInteger();
	private final ConcurrentHashMap<String,Integer> encode=new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Integer,String> decode=new ConcurrentHashMap<>();
	private final int valueBitMask;
	private final int valueBitCount;
	private final int attributeBitCount;
	
	private int[] encodeList(ArrayList<Integer> values) {
		int [] rtn = new int[values.size()];
		int i=0;
		for (Integer v:values) {
			rtn[i++]=v;
		}
		return rtn;
	}
	/** Returns the list of codes than are less than specified value */
	public int[] lessThan(String value) {
		ArrayList<Integer> values=new ArrayList<>();
		for (String s:decode.values()) {
			if (sortFunc.compare(value, s)>0) {
				values.add(encode(s));
			}
		}
		return encodeList(values);
	}

	/** Returns the list of codes than are less or equal to the specified value */
	public int[] lessThanOrEqual(String value) {
		ArrayList<Integer> values=new ArrayList<>();
		for (String s:decode.values()) {
			if (sortFunc.compare(value, s)>=0) {
				values.add(encode(s));
			}
		}
		return encodeList(values);
	}
	/** Returns the list of codes than are greater or equal to the specified value */
	public int[] greaterThanOrEqual(String value) {
		ArrayList<Integer> values=new ArrayList<>();
		for (String s:decode.values()) {
			if (sortFunc.compare(value, s)<=0) {
				values.add(encode(s));
			}
		}
		return encodeList(values);
	}
	/** Returns the list of codes than are greater than the specified value */
	public int[] greaterThan(String value) {
		ArrayList<Integer> values=new ArrayList<>();
		for (String s:decode.values()) {
			if (sortFunc.compare(value, s)<0) {
				values.add(encode(s));
			}
		}
		return encodeList(values);
	}
	
	public static class AttributeCodeTooBigRuntimeException extends RuntimeException {
		AttributeCodeTooBigRuntimeException(int code,int bits,int max) {
			super("attribute code "+code+" is larger than the limit of "+bits+" bits: "+max);
		}
	}
	public static class TooManyValuesRuntimeException extends RuntimeException {
		TooManyValuesRuntimeException(int bits,int max) {
			super("max number of values using "+bits+" is "+max);
		}
	}
	public Attribute(String attrName, String attrLongDescr,int attrCode,Comparator<String> sortFunc) {
		this(attrName, attrLongDescr,attrCode, sortFunc, 16);
	}
	public  Attribute(String attrName, String attrLongDescr,int attrCode,Comparator<String> sortFunc, int attributeBits) {
		super();
		this.attrName = attrName;
		this.attrLongDescr = attrLongDescr;
		this.attributeBitCount=attributeBits;
		this.valueBitCount=32-attributeBits;
		this.valueBitMask=(0xFFFFFFFF>>>(32-attributeBits));
		this.attrCode=(valueBitMask & attrCode);
		if (this.attrCode!=attrCode) {
			throw new AttributeCodeTooBigRuntimeException(attrCode,valueBitCount,valueBitMask);
		}
		if (sortFunc==null) {
			sortFunc = new Comparator<String>() {
				@Override
				public int compare(String s1,String s2) {
					return s1.compareTo(s2);
				}
			};
		}
		this.sortFunc=sortFunc;
	}
	
	public void setSortFunc(Comparator<String> sortFunc) {
		this.sortFunc = sortFunc;
	}

	public String getAttrName() {
		return attrName;
	}
	public String getAttrLongDescr() {
		return attrLongDescr;
	}
	public int encode(String attribValue)
	{
		Integer code=encode.get(attribValue);
		if (code==null) {
			synchronized (encode) {
				code=codeSequence.incrementAndGet();
				if (code>valueBitMask) {
					throw new TooManyValuesRuntimeException(valueBitCount,valueBitMask);
				}
				encode.putIfAbsent(attribValue, code);
				decode.putIfAbsent( code,attribValue);
			}
		}
		return (attrCode<<valueBitCount |(valueBitMask & encode.get(attribValue)));
	}
	@SuppressWarnings("serial")
	public static class UnknownAttributeCodeRuntimeException extends RuntimeException {
		UnknownAttributeCodeRuntimeException(int code,int shouldbe) {
			super("Attribute code "+code+" does not match "+shouldbe);
		}
	}
	public String decode(int code) {
		if (code>>>valueBitCount !=attrCode) {
			throw new UnknownAttributeCodeRuntimeException(code>>>valueBitCount,attrCode);
		}
		return decode.get(valueBitMask&code);
	}
	public int size()
	{
		return encode.size();
	}
	public List<String> values(boolean sortAssending) {
		ArrayList<String> r=new ArrayList<String>();
		r.addAll(decode.values());
		r.sort(sortFunc);
		if (!sortAssending) {
			Collections.reverse(r);
		}
		return r;
	}
	public int getAttrCode() {
		return attrCode;
	}
	public int attrCode(int encodedValue) {
		return(short)(encodedValue>>>valueBitCount);
	}
}
