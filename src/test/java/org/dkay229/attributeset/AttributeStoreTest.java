package org.dkay229.attributeset;

import static org.junit.Assert.*;

import org.dkay229.attribset.AttributeStore;
import org.junit.Test;

public class AttributeStoreTest {


	@Test
	public void testAddAndDecode() {
		AttributeStore eas = new AttributeStore();
		String [][] attributesAndValues = 
			{	{ "firstName","John"},  { "lastName","King"},{"age","12"}	
			,	{ "firstName","Martha"},{ "lastName","King"},{"age","32"}	
			,	{ "firstName","Peter"},{ "lastName","Smith"},{"age","31"}	
			,	{ "firstName","Mary"},{ "lastName","Smith"},{"age","43"}	
			,	{ "firstName","Bob"},{ "lastName","Smith"},{"age","2"}	
			};
		int codes[]=new int[attributesAndValues.length];
		int i=0;
		for (String[] nameValuePair:attributesAndValues)
			codes[i++]=eas.add(nameValuePair[0], nameValuePair[1]);
		for (int j=0;j<codes.length;j++)
			assertEquals("Decoded value matches",attributesAndValues[j][1],eas.getAttributeValue(codes[j]));
	}



}
