package org.dkay229.attributeset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.dkay229.attribset.AttributeSet;
import org.dkay229.attribset.Record;
import org.junit.Test;

public class RecordTest {
	int [][] attr = { { 0,1,2 }, {0,1,2,3}, {}, { 1,2 } };
	@Test
	public void testHashCode() {
		assertNotEquals("No hash clash",new Record(attr[0]).hashCode(),new Record(attr[2]).hashCode());
		assertEquals("hash match",new Record(attr[0]).hashCode(),new Record(attr[0]).hashCode());

	}

	@Test
	public void testRecord() {
	}

	@Test
	public void testToStringAttributeSet() {
		AttributeSet as = new AttributeSet();
		int[] att=new int[1];
		att[0]=as.add("Name", "john");
		assertEquals("hash match","Record [Name=john]",new Record(att).toString(as));
	}

	@Test
	public void testToString() {
		Record e=new Record(attr[0]);
		assertEquals("hash match","Record [0, 1, 2]",e.toString());
	}

	@Test
	public void testEqualsObject() {
		Record e=new Record(attr[0]);
		assertTrue("equals check",e.equals(new Record(attr[0])));
		assertTrue("equals check",e.equals(e));
		assertFalse("equals check",e.equals(new Record(attr[1])));
		assertFalse("equals check",e.equals(null));
		assertFalse("equals check",e.equals(new Object()));
	}

}
