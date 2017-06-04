package org.dkay229.attributeset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.List;

import org.dkay229.attribset.Attribute;
import org.dkay229.attribset.Attribute.UnknownAttributeCodeRuntimeException;
import org.junit.Test;

public class AttibuteTest {

	@Test
	public void testString() {
		Attribute nameAttr = new Attribute("Name","Full name of user",1,null);
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
	public void badAttrCodeExceptionTest() {
		
		Throwable th=null;
		String str=null;
		Attribute nameAttr = new Attribute("Name","Full name of user",1,null);
		try {
			nameAttr.decode(0xAAAAAAA);
		} catch (Throwable e) {
			th=e;
		}
		assertTrue("Bad encoded value throws excepton",th instanceof UnknownAttributeCodeRuntimeException );
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
		
		Attribute dateAttr = new Attribute("Date","DD-MON-YYYY date strings",2,dateSort);
		dateAttr.encode("2-feb-2017");
		dateAttr.encode("1-sep-2016");
		dateAttr.encode("29-JAN-2017");
		dateAttr.encode("7-aug-2015");
		
		List<String> sorted=dateAttr.values(true);
		
		
		assertEquals("Date sorted by sort in ctor","1-sep-2016",sorted.get(1));
		assertEquals("Date sorted by sort in ctor","29-JAN-2017",sorted.get(2));
		assertEquals("Date sorted by sort in ctor","2-feb-2017",sorted.get(3));
		assertEquals("Date sorted by sort in ctor","7-aug-2015",sorted.get(0));

		Attribute dateAttr1 = new Attribute("Date","DD-MON-YYYY date strings",4,null);
		dateAttr1.encode("2-feb-2017");
		dateAttr1.encode("1-sep-2016");
		dateAttr1.encode("29-JAN-2017");
		dateAttr1.encode("7-aug-2015");
		List<String> sorted1=dateAttr1.values(true);
		assertEquals("Default string ordering","1-sep-2016",sorted1.get(0));
		assertEquals("Default string ordering","2-feb-2017",sorted1.get(1));
		assertEquals("Default string ordering","29-JAN-2017",sorted1.get(2));
		assertEquals("Default string ordering","7-aug-2015",sorted1.get(3));
	
		dateAttr1.setSortFunc(dateSort);
		List<String> sorted2=dateAttr1.values(true);
		assertEquals("Date sorted after settingsort func","1-sep-2016",sorted.get(1));
		assertEquals("Date sorted after settingsort func","29-JAN-2017",sorted.get(2));
		assertEquals("Date sorted after settingsort func","2-feb-2017",sorted.get(3));
		assertEquals("Date sorted after settingsort func","7-aug-2015",sorted.get(0));
	}

	Comparator<String> intCompare = new Comparator<String>() {
		
		@Override
		public int compare(String s1,String s2) {
			int i1 = Integer.parseInt(s1);
			int i2 = Integer.parseInt(s2);
			return i1-i2;
		}
	};
	
	@Test
	public void filterTest() {
		Attribute intAttr = new Attribute("Number","Number",9,intCompare);
		intAttr.encode("999");
		intAttr.encode("10");
		intAttr.encode("50");
		intAttr.encode("9");
		intAttr.encode("100");
		intAttr.encode("1000");
		
		int [] lt100 = intAttr.lessThan("100");
		for (int code:lt100) {
			int iVal=Integer.parseInt(intAttr.decode(code));
			assertTrue(""+iVal+ " less than 100",iVal<100);
		}
		assertEquals("3 less than 100",3,lt100.length);
		int [] le100 = intAttr.lessThanOrEqual("100");
		for (int code:le100) {
			int iVal=Integer.parseInt(intAttr.decode(code));
			assertTrue(""+iVal+ " less than 100",iVal<=100);
		}
		assertEquals("4 less than  or equal 100",4,le100.length);
		int [] gt100 = intAttr.greaterThan("100");
		for (int code:gt100) {
			int iVal=Integer.parseInt(intAttr.decode(code));
			assertTrue(""+iVal+ " less than 100",iVal>100);
		}
		assertEquals("2 greater than 100",2,gt100.length);
		int [] ge100 = intAttr.greaterThanOrEqual("100");
		for (int code:ge100) {
			int iVal=Integer.parseInt(intAttr.decode(code));
			assertTrue(""+iVal+ " less than 100",iVal>=100);
		}
		assertEquals("3 greater than or equal 100",3,ge100.length);
	}

}
