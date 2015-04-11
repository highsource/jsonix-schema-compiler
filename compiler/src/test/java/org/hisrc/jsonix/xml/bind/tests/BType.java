package org.hisrc.jsonix.xml.bind.tests;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BType {
	
	@XmlElement
	public Integer one;

	@XmlElement(defaultValue = "2")
	public Integer two;

	@XmlElement(nillable = true)
	public Integer three;

	@XmlElement(nillable = true, defaultValue = "4")
	public Integer four;
	
	@XmlElement
	public Date five;

	@XmlElement
	public Date six;
}
