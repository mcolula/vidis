package sim.xml.modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sim.exceptions.DocumentMalformedException;
import sim.xml.CommonDomParser;
import sim.xml.modules.dataStructure.DocumentData;
import sim.xml.modules.dataStructure.DocumentDataConnection;
import sim.xml.modules.dataStructure.DocumentDataLink;
import sim.xml.modules.dataStructure.DocumentDataNode;

/**
 * this class should - once finished - read .msim module files
 * 
 * @author dominik
 * 
 */
public class XMLModuleReader implements CommonDomParser {
	private File simFile;
	private DocumentData document;

	private XMLModuleReader(File simFile, DocumentData document) {
		this.simFile = simFile;
		this.document = document;
	}

	private static DocumentData analyze(Document document) throws DocumentMalformedException {
		// analyze here
		NodeList rootNodes = document.getElementsByTagName("module");
		if (rootNodes.getLength() == 1) {
			DocumentData data = DocumentData.getInstance();
			for (int i = 0; i < rootNodes.getLength(); i++) {
				Node module = rootNodes.item(i);
				if (module.hasChildNodes()) {
					int moduleNodesFound = 0;
					for (int j = 0; j < module.getChildNodes().getLength(); j++) {
						Node moduleChild = module.getChildNodes().item(j);
						if (moduleChild.getNodeName().equalsIgnoreCase("id")) {
							String id = moduleChild.getFirstChild().getNodeValue();
							// System.out.println("module.id=" + id);
							data.setId(id);
							moduleNodesFound++;
						} else if (moduleChild.getNodeName().equalsIgnoreCase("classpath")) {
							String classpath = moduleChild.getFirstChild().getNodeValue();
							// System.out.println("module.classpath=" + classpath);
							data.setClasspath(classpath);
							moduleNodesFound++;
						} else if (moduleChild.getNodeName().equalsIgnoreCase("nodeDensity")) {
							double density = Double.parseDouble(moduleChild.getFirstChild().getNodeValue());
							data.setNodeDensity(density);
							moduleNodesFound++;
						} else if (moduleChild.getNodeName().equalsIgnoreCase("objects")) {
							for (int k = 0; k < moduleChild.getChildNodes().getLength(); k++) {
								Node node = moduleChild.getChildNodes().item(k);
								if (node.getNodeName().equalsIgnoreCase("node")) {
									if (node.hasChildNodes()) {
										int nodeNodesFound = 0;
										DocumentDataNode dataNode = DocumentDataNode.getInstance();
										for (int l = 0; l < node.getChildNodes().getLength(); l++) {
											Node nodeChild = node.getChildNodes().item(l);
											if (nodeChild.getNodeName().equalsIgnoreCase("id")) {
												String id = nodeChild.getFirstChild().getNodeValue();
												// System.out.println("node.id=" +
												// nodeChild.getFirstChild().getNodeValue());
												dataNode.setId(id);
												nodeNodesFound++;
											} else if (nodeChild.getNodeName().equalsIgnoreCase("class")) {
												String classpath = nodeChild.getFirstChild().getNodeValue();
												// System.out.println("node.class=" +
												// nodeChild.getFirstChild().getNodeValue());
												dataNode.setClasspath(classpath);
												nodeNodesFound++;
											} else if (nodeChild.getNodeName().equalsIgnoreCase("variables")) {
												if (nodeChild.hasChildNodes()) {
													for (int m = 0; m < nodeChild.getChildNodes().getLength(); m++) {
														Node varNode = nodeChild.getChildNodes().item(m);
														if (varNode.hasChildNodes()) {
															String varName = varNode.getNodeName();
															String varValue = varNode.getFirstChild().getNodeValue();
															dataNode.addVariable(varName, varValue);
														}
													}
													nodeNodesFound++;
												} else {
													// no vars set
												}
											}
										}
										if (nodeNodesFound == 2 || nodeNodesFound == 3) {
											data.addOrUpdate(dataNode);
										} else {
											throw new DocumentMalformedException("node malformed; expected 2 elements but got " + nodeNodesFound);
										}
									} else {
										throw new DocumentMalformedException("node malformed");
									}
								} else if (node.getNodeName().equalsIgnoreCase("link")) {
									if (node.hasChildNodes()) {
										int nodeNodesFound = 0;
										DocumentDataLink dataLink = DocumentDataLink.getInstance();
										for (int l = 0; l < node.getChildNodes().getLength(); l++) {
											Node linkChild = node.getChildNodes().item(l);
											if (linkChild.getNodeName().equalsIgnoreCase("id")) {
												String id = linkChild.getFirstChild().getNodeValue();
												// System.out.println("node.id=" +
												// nodeChild.getFirstChild().getNodeValue());
												dataLink.setId(id);
												nodeNodesFound++;
											} else if (linkChild.getNodeName().equalsIgnoreCase("class")) {
												String classpath = linkChild.getFirstChild().getNodeValue();
												// System.out.println("node.class=" +
												// nodeChild.getFirstChild().getNodeValue());
												dataLink.setClasspath(classpath);
												nodeNodesFound++;
											} else if (linkChild.getNodeName().equalsIgnoreCase("delay")) {
												long delay = Long.parseLong(linkChild.getFirstChild().getNodeValue());
												dataLink.setDelay(delay);
												nodeNodesFound++;
											} else if (linkChild.getNodeName().equalsIgnoreCase("variables")) {
												if (linkChild.hasChildNodes()) {
													for (int m = 0; m < linkChild.getChildNodes().getLength(); m++) {
														Node varNode = linkChild.getChildNodes().item(m);
														if (varNode.hasChildNodes()) {
															String varName = varNode.getNodeName();
															String varValue = varNode.getFirstChild().getNodeValue();
															dataLink.addVariable(varName, varValue);
														}
													}
													nodeNodesFound++;
												} else {
													// no vars set
												}
											}
										}
										if (nodeNodesFound == 3 || nodeNodesFound == 4) {
											data.addOrUpdate(dataLink);
										} else {
											throw new DocumentMalformedException("node malformed; expected 2 elements but got " + nodeNodesFound);
										}
									} else {
										throw new DocumentMalformedException("node malformed");
									}
								}
							}
							moduleNodesFound++;
						} else if (moduleChild.getNodeName().equalsIgnoreCase("connections")) {
							for (int k = 0; k < moduleChild.getChildNodes().getLength(); k++) {
								Node connection = moduleChild.getChildNodes().item(k);
								if (connection.getNodeName().equalsIgnoreCase("connection")) {
									if (connection.hasChildNodes()) {
										int connectionNodesFound = 0;
										DocumentDataConnection dataConnection = DocumentDataConnection.getInstance();
										for (int l = 0; l < connection.getChildNodes().getLength(); l++) {
											Node connectionChild = connection.getChildNodes().item(l);
											if (connectionChild.getNodeName().equalsIgnoreCase("nodeA")) {
												String nodeId = connectionChild.getFirstChild().getNodeValue();
												DocumentDataNode from = data.getNodeById(nodeId);
												if (from == null) {
													throw new DocumentMalformedException("connection malformed; could not find nodeA-node '" + nodeId + "'");
												} else {
													dataConnection.setNodeA(from);
													connectionNodesFound++;
												}
											} else if (connectionChild.getNodeName().equalsIgnoreCase("nodeB")) {
												String nodeId = connectionChild.getFirstChild().getNodeValue();
												DocumentDataNode to = data.getNodeById(nodeId);
												if (to == null) {
													throw new DocumentMalformedException("connection malformed; could not find nodeB-node '" + nodeId + "'");
												} else {
													dataConnection.setNodeB(to);
													connectionNodesFound++;
												}
											} else if (connectionChild.getNodeName().equalsIgnoreCase("link")) {
												try {
													String nodeId = connectionChild.getFirstChild().getNodeValue();
													DocumentDataLink link = data.getLinkById(nodeId);
													if (link == null) {
														throw new DocumentMalformedException("connection malformed; could not find link-node '" + nodeId + "'");
													} else {
														dataConnection.setLink(link);
														connectionNodesFound++;
													}
												} catch (NumberFormatException e) {
													throw new DocumentMalformedException("connection malformed; expected long at delay");
												}
											}
										}
										if (connectionNodesFound == 3) {
											// add connection node
											data.addConnection(dataConnection);
										} else {
											throw new DocumentMalformedException("connection malformed; expected 3 elements but got " + connectionNodesFound);
										}
									} else {
										throw new DocumentMalformedException("connection malformed");
									}
								}
							}
							moduleNodesFound++;
						}
					}
					if (!(moduleNodesFound == 4 || moduleNodesFound == 5)) {
						throw new DocumentMalformedException("module malformed; expected 4 elements but got " + moduleNodesFound);
					}
				} else {
					throw new DocumentMalformedException("module malformed");
				}
			}
			return data;
		} else {
			return null;
		}
	}

	public static XMLModuleReader parse(File simFile) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new FileInputStream(simFile)));
			return new XMLModuleReader(simFile, analyze(document));
		} catch (ParserConfigurationException e) {
			System.err.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (SAXException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (DocumentMalformedException e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	public File getSimFile() {
		return simFile;
	}

	public DocumentData getDocument() {
		return document;
	}

	public static XMLModuleReader parse(String configFile) {
		return parse(new File(configFile));
	}
}