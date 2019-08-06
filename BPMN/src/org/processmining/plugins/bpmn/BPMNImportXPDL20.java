package org.processmining.plugins.bpmn;

import java.io.InputStream;

import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagramFactory;

@Deprecated
//@Plugin(name = "Open XPDL 2.0 File", parameterLabels = { "XPDL 2.0 File" }, returnLabels = { "BPMN Diagram" }, returnTypes = { BPMNDiagram.class })
//@UIImportPlugin(description = "XPDL 2.0 Files", extensions = { "xpdl" })
public class BPMNImportXPDL20 extends AbstractImportPlugin {

	protected Object importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes)
			throws Exception {

		BPMNDiagram bpmndiagram = BPMNDiagramFactory.newBPMNDiagram(filename);
		context.getFutureResult(0).setLabel(filename);

		XPDLReader reader = new XPDLReader();
		reader.read(input, bpmndiagram);

		return bpmndiagram;
	}

}
