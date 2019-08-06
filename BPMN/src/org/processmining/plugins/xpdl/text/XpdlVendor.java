package org.processmining.plugins.xpdl.text;

/**
 * @author hverbeek
 * 
 *         <xsd:element name="Vendor"> <xsd:complexType> <xsd:simpleContent>
 *         <xsd:extension base="xsd:string"> <xsd:anyAttribute
 *         namespace="##other" processContents="lax"/> </xsd:extension>
 *         </xsd:simpleContent> </xsd:complexType> </xsd:element>
 */
public class XpdlVendor extends XpdlText {

	public XpdlVendor(String tag) {
		super(tag);
	}

}
