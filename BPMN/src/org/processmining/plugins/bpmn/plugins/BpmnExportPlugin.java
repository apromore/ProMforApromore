package org.processmining.plugins.bpmn.plugins;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.io.IOUtils;
import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.plugins.bpmn.BpmnDefinitions;
import org.processmining.plugins.bpmn.BpmnDefinitions.BpmnDefinitionsBuilder;

/**
 * Export BPMN diagrams to BPMN XML format
 *
 * @author Anna Kalenkova
 * Aug 21, 2013
 */
@Plugin(name = "BPMN XML 2.0 export", level = PluginLevel.PeerReviewed, returnLabels = {}, returnTypes = {}, 
	parameterLabels = { "BPMN definitions", "File" }, userAccessible = true)
@UIExportPlugin(description = "BPMN 2.0 files", extension = "bpmn")
public class BpmnExportPlugin {
	// xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" 
	
	private static final String ENCODING = "UTF-8";
	
	private static final String EXPORTER = "ProM. http://www.promtools.org/prom6";
	
	private static final String EXPORTER_VERSION = "6.3";
	
	private static final String TARGET_NAMESPACE = "http://www.omg.org/bpmn20";
	
	private static final String BPMN_MODEL_NAMESPACE = 
			"http://www.omg.org/spec/BPMN/20100524/MODEL";
	
	private static final String SCHEMA_LOCATION = 
			"http://www.omg.org/spec/BPMN/20100524/MODEL "
			+ "BPMN20.xsd";
	
	private static final String DI_PREFIX = "bpmndi";
	
	private static final String DI_NAMESPACE = "http://www.omg.org/spec/BPMN/20100524/DI";
	
	private static final String DC_PREFIX = "dc";
	
	private static final String DC_NAMESPACE = "http://www.omg.org/spec/DD/20100524/DC";
	
	private static final String DD_DI_PREFIX = "di";
	
	private static final String DD_DI_NAMESPACE = "http://www.omg.org/spec/DD/20100524/DI";
	
	private static final String XSI_PREFIX = "xsi";
	
	private static final String XSI_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
	
	private static final String CONTENT_MARKER = "@CONTENT@";

	/**
	 * Plugin for BPMN XML 2.0 export from BPMN diagram
	 * 
	 * @param context
	 * @param bpmnDiagram
	 * @param file
	 * @throws IOException
	 */
	@PluginVariant(variantLabel = "BPMN XML 2.0 export", requiredParameterLabels = { 0, 1 })
	public void export(PluginContext context, BPMNDiagram bpmnDiagram, File file) throws IOException {
		String bodyString = retrieveContent(context, bpmnDiagram);
		exportWithContent(context, file, bodyString);
	}
	
	/**
	 * Plugin for BPMN XML 2.0 export from BPMN Definitions
	 * 
	 * @param context
	 * @param definitions
	 * @param file
	 * @throws IOException
	 */
	@PluginVariant(variantLabel = "BPMN XML 2.0 export", requiredParameterLabels = { 0, 1 })
	public void export(PluginContext context, BpmnDefinitions definitions, File file) throws IOException {
		String bodyString = definitions.exportElements();
		exportWithContent(context, file, bodyString);
	}
	
	/**
	 * Export with content
	 * 
	 * @param context
	 * @param file
	 * @param bodyString
	 */
	public void exportWithContent(PluginContext context, File file, String bodyString) throws IOException {
		XMLOutputFactory xmlOutput = XMLOutputFactory.newInstance();
		OutputStream outputStream = null, fileOutputStream = null;
		try {
			outputStream = new ByteArrayOutputStream();
			XMLStreamWriter writer = xmlOutput.createXMLStreamWriter(outputStream, ENCODING);
			writer.writeStartDocument(ENCODING, "1.0");

			// Export envelope
			exportEnvelope(context, writer);
			
			writer.writeEndDocument();
			writer.flush();
			
			// Add definition content
			String envelopeString = outputStream.toString();
			String contentString = envelopeString.replace(CONTENT_MARKER, bodyString);
			
			// Write to file
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(contentString.getBytes());

		} catch (XMLStreamException e) {
			System.out.println("Error during export BPMN diagram");
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fileOutputStream);
		}
	}

	/**
	 * Export envelope
	 * 
	 * @param context
	 * @param writer
	 * @throws XMLStreamException
	 */
	private void exportEnvelope(PluginContext context, XMLStreamWriter writer)
			throws XMLStreamException {
		writer.writeStartElement("definitions");

		writer.setDefaultNamespace(BPMN_MODEL_NAMESPACE);
		writer.writeDefaultNamespace(BPMN_MODEL_NAMESPACE);
		
		writer.setPrefix(DC_PREFIX, DC_NAMESPACE);
		writer.writeNamespace(DC_PREFIX, DC_NAMESPACE);
		
		writer.setPrefix(DI_PREFIX, DI_NAMESPACE);
		writer.writeNamespace(DI_PREFIX, DI_NAMESPACE);
		
		writer.setPrefix(DD_DI_PREFIX, DD_DI_NAMESPACE);
		writer.writeNamespace(DD_DI_PREFIX, DD_DI_NAMESPACE);

		writer.setPrefix(XSI_PREFIX, XSI_NAMESPACE);
		writer.writeNamespace(XSI_PREFIX, XSI_NAMESPACE);

		writer.writeAttribute("targetNamespace", TARGET_NAMESPACE);
		writer.writeAttribute("exporter", EXPORTER);
		// TODO: change the exporter version to context.getGlobalContext().getFrameworkVersion()
		// when this method will be implemented
		writer.writeAttribute("exporterVersion", EXPORTER_VERSION);
		writer.writeAttribute(XSI_NAMESPACE, "schemaLocation", SCHEMA_LOCATION);
		
		// write marker for definition content
		writer.writeCharacters(CONTENT_MARKER);
		
		writer.writeEndElement();
	}
	

	/**
	 * Retrieve definition content from BPMN diagram
	 * 
	 * @param diagram
	 * @return
	 */
	private String retrieveContent(PluginContext context, BPMNDiagram diagram) {
		BpmnDefinitionsBuilder definitionsBuilder = new BpmnDefinitionsBuilder(context, diagram);
		BpmnDefinitions definitions = new BpmnDefinitions("definitions", definitionsBuilder);
		return definitions.exportElements();
	}
}
