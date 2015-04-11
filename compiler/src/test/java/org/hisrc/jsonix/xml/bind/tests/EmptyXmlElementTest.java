package org.hisrc.jsonix.xml.bind.tests;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;

public class EmptyXmlElementTest {

	@Test
	public void checksNull() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(AType.class);
		final Marshaller marshaller = context.createMarshaller();
		final AType a = new AType();
		final StringWriter sw = new StringWriter();
		marshaller.marshal(a, sw);
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><aType><three xsi:nil=\"true\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/><four xsi:nil=\"true\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/></aType>", sw.toString());

	}
	@Test
	public void checksEmptyString() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(AType.class);
		final Marshaller marshaller = context.createMarshaller();
		final AType a = new AType();
		a.one = "";
		a.two = "";
		a.three = "";
		a.four = "";
		final StringWriter sw = new StringWriter();
		marshaller.marshal(a, sw);
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><aType><one></one><two></two><three></three><four></four></aType>", sw.toString());

	}
	@Test
	public void checksDefault() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(AType.class);
		final Marshaller marshaller = context.createMarshaller();
		final AType a = new AType();
		a.one = "";
		a.two = "two";
		a.three = "";
		a.four = "four";
		final StringWriter sw = new StringWriter();
		marshaller.marshal(a, sw);
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><aType><one></one><two>two</two><three></three><four>four</four></aType>", sw.toString());

	}
	
	@Test
	public void checksInteger() throws JAXBException {
		final JAXBContext context = JAXBContext.newInstance(BType.class);
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		final String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bType><one></one><two></two><three></three><four></four><five></five></bType>";
		final BType bType = (BType) unmarshaller.unmarshal(new StringReader(str));
		Assert.assertEquals(0, bType.one.intValue());
		Assert.assertEquals(2, bType.two.intValue());
		Assert.assertEquals(0, bType.three.intValue());
		Assert.assertEquals(4, bType.four.intValue());

	}
}
