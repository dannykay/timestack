package org.dkay229.timestack.graphics.event;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/** Parses xml messages that look like this:
 * <pre>
 * &lt;event>
 * 	  &lt;startMs>1287631923&lt;/startMs>
 * 	  &lt;endMs>1287631929&lt;/endMs>
 * 	  &lt;username>F080976&lt;/username>
 * 	  &lt;recordDate>20-MAY-2016&lt;recordDate>
 * &lt;/event>
 * </pre>
 * The only mandatory fields are <code>startMs</code> and <code>endMs</code>. All other attributes are accumulated for later fitlering of timelines
 * @author Danny
 *
 */
public class EventXmlParser {
	private static Logger logger = LoggerFactory.getLogger(EventXmlParser.class);
	private static class AttributeHandler extends DefaultHandler {
		private String currAttribute;
		private String currValue;
		private long startMs=0L;
		private long endMs=0L;
		List<Integer>attributeCodes=new ArrayList<>();
		private final EventAttributeSet eventAttributeSet;
		private final List<Event> eventList;
		

		public AttributeHandler(EventAttributeSet eventAttributeSet, List<Event> eventList) {
			super();
			this.eventAttributeSet = eventAttributeSet;
			this.eventList = eventList;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			logger.info("uri="+uri+" localName="+localName+" qName="+qName);
			
				currAttribute = qName;
		}

		@Override
		public void characters(char ch[], int start, int length) throws SAXException {
			currValue=new String(ch, start, length);
			logger.info("chars="+currValue);
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			logger.info(qName);
			if ("startMs".equals(qName)) {
				startMs=Long.parseLong(currValue);
			} else if ("endMs".equals(qName)) {
				endMs=Long.parseLong(currValue);
			} else if ("event".equals(qName)) {
				int[] codes=new int[attributeCodes.size()];
				for (int i=0;i<attributeCodes.size();codes[i]=attributeCodes.get(i++));
				Event ev;
				eventList.add(ev=new Event(startMs,endMs,codes));
				logger.info("Added event: "+ev);
				attributeCodes=new ArrayList<>();
			} else {
				attributeCodes.add(eventAttributeSet.add(currAttribute, currValue));
			}
		}
	}

	public static List<Event> parseEventMessage(InputStream inputStream,EventAttributeSet eventAttributeSet) throws Exception {
		List<Event> r = new ArrayList<>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		AttributeHandler handler  = new AttributeHandler(eventAttributeSet,r);
		saxParser.parse(inputStream, handler);
		return r;
	}

}
