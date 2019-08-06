package org.processmining.plugins.bpmn;

import java.util.Collection;
import java.util.Map;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway.GatewayType;
import org.processmining.models.graphbased.directed.bpmn.elements.SubProcess;
import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;

public class BpmnExclusiveGateway extends BpmnAbstractGateway {
	
	public BpmnExclusiveGateway(String tag) {
		super(tag);
	}
	
	public void unmarshall(BPMNDiagram diagram, Map<String, BPMNNode> id2node, Swimlane lane) {
		Gateway gateway = diagram.addGateway(name, GatewayType.DATABASED, lane);
		gateway.getAttributeMap().put("Original id", id);
		id2node.put(id, gateway);
	}

	public void unmarshall(BPMNDiagram diagram, Collection<String> elements, Map<String, BPMNNode> id2node, Swimlane lane) {
		if (elements.contains(id)) {
			Gateway gateway = diagram.addGateway(name, GatewayType.DATABASED, lane);
			gateway.getAttributeMap().put("Original id", id);
			id2node.put(id, gateway);
		}
	}
	
	public void unmarshall(BPMNDiagram diagram, Map<String, BPMNNode> id2node, SubProcess subProcess) {
		Gateway gateway = diagram.addGateway(name, GatewayType.DATABASED, subProcess);
		gateway.getAttributeMap().put("Original id", id);
		id2node.put(id, gateway);
	}

	public void unmarshall(BPMNDiagram diagram, Collection<String> elements, Map<String, BPMNNode> id2node, SubProcess subProcess) {
		if (elements.contains(id)) {
			Gateway gateway = diagram.addGateway(name, GatewayType.DATABASED, subProcess);
			gateway.getAttributeMap().put("Original id", id);
			id2node.put(id, gateway);
		}
	}
}
