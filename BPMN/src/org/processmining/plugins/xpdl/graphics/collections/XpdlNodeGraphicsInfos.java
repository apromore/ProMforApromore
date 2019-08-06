package org.processmining.plugins.xpdl.graphics.collections;

import org.processmining.plugins.xpdl.collections.XpdlCollections;
import org.processmining.plugins.xpdl.graphics.XpdlNodeGraphicsInfo;

/**
 * @author hverbeek
 * 
 *         <xsd:element name="NodeGraphicsInfos"> <xsd:complexType>
 *         <xsd:sequence> <xsd:element ref="xpdl:NodeGraphicsInfo" minOccurs="0"
 *         maxOccurs="unbounded"/> <xsd:any namespace="##other"
 *         processContents="lax" minOccurs="0" maxOccurs="unbounded"/>
 *         </xsd:sequence> <xsd:anyAttribute namespace="##other"
 *         processContents="lax"/> </xsd:complexType> </xsd:element>
 */
public class XpdlNodeGraphicsInfos extends XpdlCollections<XpdlNodeGraphicsInfo> {

	public XpdlNodeGraphicsInfos(String tag) {
		super(tag);
	}

	public XpdlNodeGraphicsInfo create() {
		return new XpdlNodeGraphicsInfo("NodeGraphicsInfo");
	}

}
