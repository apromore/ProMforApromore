package org.processmining.plugins.bpmn.plugins;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagramFactory;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.plugins.bpmn.dialogs.BpmnSelectDiagramDialog;
import org.processmining.plugins.bpmn.parameters.BpmnSelectDiagramParameters;

@Plugin(name = "Select BPMN Diagram", level = PluginLevel.PeerReviewed, parameterLabels = { "BPMN", "Paramaters" }, returnLabels = { "BPMN Diagram" }, returnTypes = { BPMNDiagram.class })
public class BpmnSelectDiagramPlugin {

	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "H.M.W. Verbeek", email = "h.m.w.verbeek@tue.nl")
	@PluginVariant(variantLabel = "Select BPMN Diagram, Dialog", requiredParameterLabels = { 0 })
	public BPMNDiagram selectDialog(UIPluginContext context, Bpmn bpmn) {
		BpmnSelectDiagramParameters parameters = new BpmnSelectDiagramParameters();
		BpmnSelectDiagramDialog dialog = new BpmnSelectDiagramDialog(bpmn.getDiagrams(), parameters);
		InteractionResult result = context.showWizard("Select BPMN diagram", true, true, dialog);
		if (result != InteractionResult.FINISHED) {
			return null;
		}
		BPMNDiagram newDiagram = BPMNDiagramFactory.newBPMNDiagram("");
		Map<String, BPMNNode> id2node = new HashMap<String, BPMNNode>();
		Map<String, Swimlane> id2lane = new HashMap<String, Swimlane>();
		if (parameters.getDiagram() == BpmnSelectDiagramParameters.NODIAGRAM) {
			bpmn.unmarshall(newDiagram, id2node, id2lane);
		} else {
			Collection<String> elements = parameters.getDiagram().getElements();
			bpmn.unmarshall(newDiagram, elements, id2node, id2lane);
		}
		return newDiagram;
	}

	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "H.M.W. Verbeek", email = "h.m.w.verbeek@tue.nl")
	@PluginVariant(variantLabel = "Select BPMN Diagram, Parameters", requiredParameterLabels = { 0 })
	public BPMNDiagram selectDefault(PluginContext context, Bpmn bpmn) {
		BpmnSelectDiagramParameters parameters = new BpmnSelectDiagramParameters();
		if (!bpmn.getDiagrams().isEmpty()) {
			parameters.setDiagram(bpmn.getDiagrams().iterator().next());
		} else {
			parameters.setDiagram(BpmnSelectDiagramParameters.NODIAGRAM);
		}
		return selectParameters(context, bpmn, parameters);
	}
	
	@PluginVariant(variantLabel = "Select BPMN Diagram, Parameters", requiredParameterLabels = { 0, 1 })
	public BPMNDiagram selectParameters(PluginContext context, Bpmn bpmn, BpmnSelectDiagramParameters parameters) {
		BPMNDiagram newDiagram = BPMNDiagramFactory.newBPMNDiagram("");
		Map<String, BPMNNode> id2node = new HashMap<String, BPMNNode>();
		Map<String, Swimlane> id2lane = new HashMap<String, Swimlane>();
		if (parameters.getDiagram() == BpmnSelectDiagramParameters.NODIAGRAM) {
			bpmn.unmarshall(newDiagram, id2node, id2lane);
		} else {
			Collection<String> elements = parameters.getDiagram().getElements();
			bpmn.unmarshall(newDiagram, elements, id2node, id2lane);
		}
		return newDiagram;
	}
}
