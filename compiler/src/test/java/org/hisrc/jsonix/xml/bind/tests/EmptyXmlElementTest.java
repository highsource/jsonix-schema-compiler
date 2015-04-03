package org.hisrc.jsonix.xml.bind.tests;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

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
}
