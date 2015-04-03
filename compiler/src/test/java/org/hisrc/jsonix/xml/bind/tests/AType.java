package org.hisrc.jsonix.xml.bind.tests;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AType {
	
	@XmlElement
	public String one;

	@XmlElement(defaultValue = "two")
	public String two;

	@XmlElement(nillable = true)
	public String three;

	@XmlElement(nillable = true, defaultValue = "four")
	public String four;
}
