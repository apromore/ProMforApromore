package org.processmining.plugins.bpmn;

import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;


public class BpmnResource extends BpmnIdName {

	public BpmnResource(String tag) {
		super(tag);		
	}
	
	public void marshall(Swimlane swimlane) {
		super.marshall(swimlane);
		id = swimlane.getPartitionElement();
	}
}
