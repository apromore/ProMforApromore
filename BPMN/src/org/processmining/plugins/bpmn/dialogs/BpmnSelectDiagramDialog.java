package org.processmining.plugins.bpmn.dialogs;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.awt.Dimension;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.processmining.framework.util.ui.widgets.ProMList;
import org.processmining.plugins.bpmn.diagram.BpmnDiagram;
import org.processmining.plugins.bpmn.parameters.BpmnSelectDiagramParameters;

public class BpmnSelectDiagramDialog extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7746246377097958814L;
	

	public BpmnSelectDiagramDialog(Collection<BpmnDiagram> diagrams, final BpmnSelectDiagramParameters parameters) {
		double size[][] = { { TableLayoutConstants.FILL }, { TableLayoutConstants.FILL } };
		setLayout(new TableLayout(size));
		
		final JPanel panel = new JPanel();
		double panelSize[][] = { { TableLayoutConstants.FILL }, { TableLayoutConstants.FILL } };
		panel.setLayout(new TableLayout(panelSize));
		
		DefaultListModel listModel = new DefaultListModel();
//		listModel.addElement(BpmnSelectDiagramParameters.NODIAGRAM);
		for (BpmnDiagram diagram: diagrams) {
			listModel.addElement(diagram);
		}
		final ProMList list = new ProMList("Select diagram", listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		parameters.setDiagram(BpmnSelectDiagramParameters.NODIAGRAM);
		list.setSelection(BpmnSelectDiagramParameters.NODIAGRAM);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				Object[] selected = list.getSelectedValues();
				if (selected.length == 1) {
					parameters.setDiagram((BpmnDiagram) selected[0]);
				} else {
					/*
					 * Nothing selected. Revert to selection of default classifier.
					 */
					list.setSelection(BpmnSelectDiagramParameters.NODIAGRAM);
					parameters.setDiagram(BpmnSelectDiagramParameters.NODIAGRAM);
				}
			}
		});
		list.setPreferredSize(new Dimension(100, 100));
		add(list, "0, 0");
	}
}
