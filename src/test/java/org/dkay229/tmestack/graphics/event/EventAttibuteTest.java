package org.dkay229.tmestack.graphics.event;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.List;

import org.dkay229.timestack.graphics.event.EventAttribute;
import org.junit.Test;

public class EventAttibuteTest {

	@Test
	public void testString() {
		EventAttribute nameAttr = new EventAttribute("Name","Full name of user",1,null);
		int john=nameAttr.encode("John");
		int amit=nameAttr.encode("Amit");
		int zulu=nameAttr.encode("Zulu");
		
		
		
		assertEquals("Code get","John",nameAttr.decode(john));
		assertEquals("Code get","Amit",nameAttr.decode(amit));
		assertEquals("Code get","Zulu",nameAttr.decode(zulu));
		List sorted=nameAttr.values(true);
		assertEquals("Sort check","Amit",sorted.get(0));
		assertEquals("Sort check","John",sorted.get(1));
		assertEquals("Sort check","Zulu",sorted.get(2));
		
		List rlist=nameAttr.values(false);
		assertEquals("Sort check","Amit",rlist.get(2));
		assertEquals("Sort check","John",rlist.get(1));
		assertEquals("Sort check","Zulu",rlist.get(0));
		
		assertEquals("Attribute Name","Name",nameAttr.getAttrName());
		assertEquals("Attribute Desc","Full name of user",nameAttr.getAttrLongDescr());
		
		assertEquals("Re-adding John gives same code",john,nameAttr.encode("John"));

		assertEquals("Size is 3",3,nameAttr.size());
		assertEquals("code is 1",1,nameAttr.getAttrCode());

	}
	
	@Test
	public void testsCustomSort() {
		Comparator<String> dateSort = new Comparator<String>() {
			
			private int decodeMonth(String m) {
				switch (m.toLowerCase().substring(0, 3)) {
				case "jan" : return 0; 
				case "feb" : return 1; 
				case "mar" : return 2; 
				case "apr" : return 3; 
				case "may" : return 4; 
				case "jun" : return 5; 
				case "jul" : return 6; 
				case "aug" : return 7; 
				case "sep" : return 8; 
				case "oct" : return 9; 
				case "nov" : return 10; 
				case "dec" : return 11; 
				}
				throw new RuntimeException("Bad month string: "+m);
			}
			@Override
			public int compare(String s1,String s2) {
				String[] d1 = s1.split("-");
				String[] d2 = s2.split("-");
				int i1 = Integer.parseInt(d1[2])*31*12+decodeMonth(d1[1])*31+Integer.parseInt(d1[0]);
				int i2 = Integer.parseInt(d2[2])*31*12+decodeMonth(d2[1])*31+Integer.parseInt(d2[0]);
				return i1-i2;
			}
		};
		EventAttribute dateAttr = new EventAttribute("Date","DD-MON-YYYY date strings",2,dateSort);
		dateAttr.encode("2-feb-2017");
		dateAttr.encode("1-sep-2016");
		dateAttr.encode("29-JAN-2017");
		dateAttr.encode("7-aug-2017");
		
		List<String> sorted=dateAttr.values(true);
		assertEquals("Date sort","1-sep-2016",sorted.get(0));
		assertEquals("Date sort","29-JAN-2017",sorted.get(1));
		assertEquals("Date sort","2-feb-2017",sorted.get(2));
		assertEquals("Date sort","7-aug-2017",sorted.get(3));
	}

}
