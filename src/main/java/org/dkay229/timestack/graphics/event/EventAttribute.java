package org.dkay229.timestack.graphics.event;

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
public class EventAttribute {
	private final short attrCode;
	private final String attrName;
	private final String attrLongDescr;
	private final Comparator<String> sortFunc;
	private final AtomicInteger codeSequence=new AtomicInteger();
	private final ConcurrentHashMap<String,Integer> encode=new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Integer,String> decode=new ConcurrentHashMap<>();
	public  EventAttribute(String attrName, String attrLongDescr,int attrCode,Comparator<String> sortFunc) {
		super();
		this.attrName = attrName;
		this.attrLongDescr = attrLongDescr;
		this.attrCode=(short)(0x0000FFFF & attrCode);
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
				encode.putIfAbsent(attribValue, code);
				decode.putIfAbsent( code,attribValue);
			}
		}
		return (attrCode<<16 |(0x0000FFFF & encode.get(attribValue)));
	}
	@SuppressWarnings("serial")
	public static class UnknownAttributeCodeRuntimeException extends RuntimeException {
		UnknownAttributeCodeRuntimeException(int code,int shouldbe) {
			super("Attribute code "+code+" does not match "+shouldbe);
		}
	}
	public String decode(int code) {
		if (code>>>16 !=attrCode) {
			throw new UnknownAttributeCodeRuntimeException(code>>>16,attrCode);
		}
		return decode.get(0x0000FFFF&code);
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
	public static short attrCode(int encodedValue) {
		return(short)(encodedValue>>>16);
	}
}
