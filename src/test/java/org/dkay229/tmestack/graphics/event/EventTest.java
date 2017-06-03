package org.dkay229.tmestack.graphics.event;

import org.dkay229.timestack.graphics.event.Event;
import org.junit.Test;
import static org.junit.Assert.*;
public class EventTest {
	int [][] attr = { { 0,1,2 }, {0,1,2,3}, {}, { 1,2 } };
	@Test
	public void testHashCode() {
		assertNotEquals("No hash clash",new Event(1,2,attr[0]).hashCode(),new Event(1,2,attr[2]).hashCode());
		assertNotEquals("No hash clash",new Event(1,2,attr[0]).hashCode(),new Event(1,2,attr[1]).hashCode());
		assertNotEquals("No hash clash",new Event(1,2,attr[3]).hashCode(),new Event(1,2,attr[1]).hashCode());
		assertNotEquals("No hash clash",new Event(1,1,attr[3]).hashCode(),new Event(1,2,attr[3]).hashCode());
		assertNotEquals("No hash clash",new Event(2,2,attr[3]).hashCode(),new Event(1,2,attr[3]).hashCode());
		assertEquals("hash match",new Event(2,2,attr[3]).hashCode(),new Event(2,2,attr[3]).hashCode());
	}


	@Test
	public void testEqualsObject() {
		Event e=new Event(1,2,attr[0]);
		assertTrue("equals check",e.equals(new Event(1,2,attr[0])));
		assertTrue("equals check",e.equals(e));
		assertFalse("equals check",e.equals(new Event(1,2,attr[1])));
		assertFalse("equals check",e.equals(new Event(2,2,attr[0])));
		assertFalse("equals check",e.equals(new Event(1,1,attr[0])));
		assertFalse("equals check",e.equals(null));
		assertFalse("equals check",e.equals(new Object()));
	}

	@Test
	public void testToStringEventAttributeSet() {
	}

	@Test
	public void testToString() {
		Event e=new Event(1,2,attr[0]);
		assertEquals("hash match","Event [tStart=1, tEnd=2, attributes=[0, 1, 2]]",e.toString());
		assertEquals("hash match","Event [tStart=1, tEnd=2, attributes=[0, 1, 2]]",e.toString(null));
	}

}
