package org.processmining.plugins.xpdl.importing;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagramFactory;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.plugins.xpdl.Xpdl;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

@Plugin(name = "Import BPMN model from XPDL 2.2 file", parameterLabels = { "Filename" }, returnLabels = { "BPMN diagram" }, returnTypes = { BPMNDiagram.class })
@UIImportPlugin(description = "XPDL 2.2 files", extensions = { "xpdl" })
public class XpdlImportBpmn extends AbstractImportPlugin {

	protected FileFilter getFileFilter() {
		return new FileNameExtensionFilter("XPDL 2.2 files", "xpdl");
	}

	protected BPMNDiagram importFromStream(PluginContext context, InputStream input, String filename,
			long fileSizeInBytes) throws Exception {
		Xpdl xpdl = importXpdlFromStream(context, input, filename, fileSizeInBytes);
		if (xpdl == null) {
			/*
			 * No XPDL found in file. Fail.
			 */
			return null;
		}
		/*
		 * XPDL file has been imported. Now we need to convert the contents to a
		 * BPMN diagram.
		 */
		BPMNDiagram bpmn = BPMNDiagramFactory.newBPMNDiagram(filename);
		Map<String, BPMNNode> id2node = new HashMap<String, BPMNNode>();

		/*
		 * Initialize the BPMN diagram from the XPDL element.
		 */
		xpdl.convertToBpmn(bpmn, id2node);

		/*
		 * Set the label of the BPMN diagram.
		 */
		context.getFutureResult(0).setLabel(filename);

		return bpmn;
	}

	private Xpdl importXpdlFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes)
			throws Exception {
		/*
		 * Get an XML pull parser.
		 */
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		/*
		 * Initialize the parser on the provided input.
		 */
		xpp.setInput(input, null);
		/*
		 * Get the first event type.
		 */
		int eventType = xpp.getEventType();
		/*
		 * Create a fresh PNML object.
		 */
		Xpdl xpdl = new Xpdl();

		/*
		 * Skip whatever we find until we've found a start tag.
		 */
		while (eventType != XmlPullParser.START_TAG) {
			eventType = xpp.next();
		}
		/*
		 * Check whether start tag corresponds to PNML start tag.
		 */
		if (xpp.getName().equals(xpdl.tag)) {
			/*
			 * Yes it does. Import the PNML element.
			 */
			xpdl.importElement(xpp, xpdl);
		} else {
			/*
			 * No it does not. Return null to signal failure.
			 */
			xpdl.log(xpdl.tag, xpp.getLineNumber(), "Expected " + xpdl.tag + ", got " + xpp.getName());
		}
		if (xpdl.hasErrors()) {
			context.getProvidedObjectManager().createProvidedObject("Log of XPDL import", xpdl.getLog(), XLog.class,
					context);
			return null;
		}
		return xpdl;
	}
}
