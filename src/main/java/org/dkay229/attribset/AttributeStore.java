package org.dkay229.attribset;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Contains attributes and attribute values and provides related meta data and statistical information.
 * 
 */
public class AttributeStore {
	private final AtomicInteger seq = new AtomicInteger();
	private final ConcurrentHashMap<String, Attribute> attributes = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Integer, Attribute> attributeLookup = new ConcurrentHashMap<>();
	private final int attributeValueBitCount;
	
	
	public AttributeStore() {
		this(16);
	}
	public AttributeStore(int attributeValueBitCount) {
		super();
		this.attributeValueBitCount = attributeValueBitCount;
	}
	public int add(String attrName, String attrValue) {
		synchronized (attributes) {
			if (!attributes.containsKey(attrName)) {
				Attribute eventAttribute=new Attribute(attrName, attrName, seq.incrementAndGet(), null,attributeValueBitCount);
				attributes.put(attrName, eventAttribute);
				attributeLookup.put(eventAttribute.getAttrCode(), eventAttribute);
			}
		}
		return attributes.get(attrName).encode(attrValue);
	}
	public int getAttributeCode(int code) {
		return code>>>(32-attributeValueBitCount);
	}
	public String getAttributeName(int code) {
		return attributeLookup.get(getAttributeCode(code)).getAttrName();
	}
	public String getAttributeValue(int code) {
		return attributeLookup.get(getAttributeCode(code)).decode(code);
	}
}
