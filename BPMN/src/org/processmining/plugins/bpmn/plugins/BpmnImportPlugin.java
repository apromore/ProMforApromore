package org.processmining.plugins.bpmn.plugins;

import java.io.InputStream;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.plugins.bpmn.Bpmn;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

@Plugin(name = "Import BPMN model from BPMN 2.0 file", level = PluginLevel.PeerReviewed, parameterLabels = { "Filename" }, returnLabels = { "BPMN" }, returnTypes = { Bpmn.class })
@UIImportPlugin(description = "BPMN 2.0 files", extensions = {"bpmn", "xml"})
public class BpmnImportPlugin extends AbstractImportPlugin {

	protected FileFilter getFileFilter() {
		return new FileNameExtensionFilter("BPMN 2.0 files", "bpmn", "xml");
	}

	protected Bpmn importFromStream(PluginContext context, InputStream input, String filename,
			long fileSizeInBytes) throws Exception {
		Bpmn bpmn = importBpmnFromStream(context, input, filename, fileSizeInBytes);
		if (bpmn == null) {
			/*
			 * No BPMN found in file. Fail.
		`	 */
			return null;
		}
		/*
		 * XPDL file has been imported. Now we need to convert the contents to a
		 * BPMN diagram.
		 */
//		BpmnDiagrams diagrams = new BpmnDiagrams();
//		diagrams.setBpmn(bpmn);
//		diagrams.setName(filename);
//		diagrams.addAll(bpmn.getDiagrams());
		context.getFutureResult(0).setLabel(filename);
		return bpmn;
	}

	private Bpmn importBpmnFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes)
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
		Bpmn bpmn = new Bpmn();

		/*
		 * Skip whatever we find until we've found a start tag.
		 */
		while (eventType != XmlPullParser.START_TAG) {
			eventType = xpp.next();
		}
		/*
		 * Check whether start tag corresponds to PNML start tag.
		 */
		if (xpp.getName().equals(bpmn.tag)) {
			/*
			 * Yes it does. Import the PNML element.
			 */
			bpmn.importElement(xpp, bpmn);
		} else {
			/*
			 * No it does not. Return null to signal failure.
			 */
			bpmn.log(bpmn.tag, xpp.getLineNumber(), "Expected " + bpmn.tag + ", got " + xpp.getName());
		}
		if (bpmn.hasErrors()) {
			context.getProvidedObjectManager().createProvidedObject("Log of BPMN import", bpmn.getLog(), XLog.class,
					context);
			return null;
		}
		return bpmn;
	}
}
