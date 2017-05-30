package org.dkay229.timestack.graphics;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.List;

import org.dkay229.timestack.graphics.event.Event;
import org.dkay229.timestack.graphics.event.EventAttributeSet;
import org.dkay229.timestack.graphics.event.EventXmlParser;
import org.junit.Test;

public class EventXmlParserTest {

	@Test
	public void testEventXmlParser() {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-event-messages.xml");
		EventAttributeSet eventAttributeSet = new EventAttributeSet();
		Exception e=null;
		List<Event> events=null;
		try {
			events = EventXmlParser.parseEventMessage(in, eventAttributeSet);
		} catch (Exception e1) {
			e1.printStackTrace();
			e=e1;
		}
		assertNull("No parse exceptions",e);
		assertEquals("All test messages parsed",3,events.size());
	}

}
