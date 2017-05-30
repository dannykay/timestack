package org.dkay229.timestack.graphics.event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Contains all String attributes associated with an EventTimeline and encodes into integers to be attached to events
 * 
 */
public class EventAttributeSet {
	private AtomicInteger seq = new AtomicInteger();
	private ConcurrentHashMap<String, EventAttribute> attributes = new ConcurrentHashMap<>();

	public int add(String attrName, String attrValue) {
		synchronized (attributes) {
			if (!attributes.contains(attrName)) {
				attributes.put(attrName, (new EventAttribute(attrName, attrName, seq.incrementAndGet(), null)));
			}
		}
		return attributes.get(attrName).encode(attrValue);
	}
}
