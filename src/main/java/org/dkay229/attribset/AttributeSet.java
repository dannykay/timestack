package org.dkay229.attribset;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Contains attributes and attribute values and provides related meta data and statistical information.
 * 
 */
public class AttributeSet {
	private AtomicInteger seq = new AtomicInteger();
	private ConcurrentHashMap<String, Attribute> attributes = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Integer, Attribute> attributeLookup = new ConcurrentHashMap<>();
	
	public int add(String attrName, String attrValue) {
		synchronized (attributes) {
			if (!attributes.containsKey(attrName)) {
				Attribute eventAttribute=new Attribute(attrName, attrName, seq.incrementAndGet(), null);
				attributes.put(attrName, eventAttribute);
				attributeLookup.put(eventAttribute.getAttrCode(), eventAttribute);
			}
		}
		return attributes.get(attrName).encode(attrValue);
	}
	public static int getAttributeCode(int code) {
		return Attribute.attrCode(code);
	}
	public String getAttributeName(int code) {
		return attributeLookup.get(getAttributeCode(code)).getAttrName();
	}
	public String getAttributeValue(int code) {
		return attributeLookup.get(getAttributeCode(code)).decode(code);
	}
}
