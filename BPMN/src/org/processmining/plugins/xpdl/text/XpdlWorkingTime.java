package org.processmining.plugins.xpdl.text;

/**
 * @author hverbeek
 * 
 *         <xsd:element name="WorkingTime"> <xsd:complexType>
 *         <xsd:simpleContent> <xsd:extension base="xsd:string">
 *         <xsd:anyAttribute namespace="##other" processContents="lax"/>
 *         </xsd:extension> </xsd:simpleContent> </xsd:complexType>
 *         </xsd:element>
 */
public class XpdlWorkingTime extends XpdlText {

	public XpdlWorkingTime(String tag) {
		super(tag);
	}

}