package org.processmining.plugins.xpdl.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.processmining.contexts.cli.CLIPluginContext;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.plugins.xpdl.Xpdl;
import org.processmining.plugins.xpdl.converter.BPMN2XPDLConversion;

@Plugin(name = "XPDL 2.2 export", returnLabels = {}, returnTypes = {}, parameterLabels = { "XPDL 2.2 export", "File" }, userAccessible = true)
@UIExportPlugin(description = "XPDL 2.2 export", extension = "xpdl")
public class XPDLExport {

	@PluginVariant(variantLabel = "XPDL 2.2 export", requiredParameterLabels = { 0, 1 })
	public void export(UIPluginContext context, BPMNDiagram bpmn, File file) throws IOException {

		BPMN2XPDLConversion xpdlConversion = new BPMN2XPDLConversion(bpmn);
		Xpdl xpdl = xpdlConversion.convert2XPDL(context);

		String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xpdl.exportElement();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		bw.write(text);
		bw.close();
	}

	/*
	 *  HV: The next line results in an error message when ProM tries to load this plug-in, as an export
	 *  plug-in should have a UIPLuginContext (or better) as first parameter. However, removing this line
	 *  results in test cases going wrong. Apparently, the scripting feature also uses this line.
	 */
	
	@PluginVariant(variantLabel = "XPDL 2.2 export", requiredParameterLabels = { 0, 1 })
	public void export(CLIPluginContext context, BPMNDiagram bpmn, File file) throws IOException {

		BPMN2XPDLConversion xpdlConversion = new BPMN2XPDLConversion(bpmn);
		Xpdl xpdl = xpdlConversion.convert2XPDL_noLayout();

		String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xpdl.exportElement();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		bw.write(text);
		bw.close();
	}
}
