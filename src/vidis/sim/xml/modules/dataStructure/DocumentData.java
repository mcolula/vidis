/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.sim.xml.modules.dataStructure;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DocumentData {
	private String description;
	private String packageName;
	private Double density;
	private Map<String, DocumentDataNode> nodes;
	private Map<String, DocumentDataLink> links;
	private List<DocumentDataConnection> connections;

	private DocumentData() {
		density = null;
		nodes = new ConcurrentHashMap<String, DocumentDataNode>();
		links = new ConcurrentHashMap<String, DocumentDataLink>();
		connections = new LinkedList<DocumentDataConnection>();
	}

	public static DocumentData getInstance() {
		return new DocumentData();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getNodeDensity() {
		return density;
	}

	public void setNodeDensity(double density) {
		this.density = density;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Map<String, DocumentDataNode> getNodes() {
		return nodes;
	}

	public Map<String, DocumentDataLink> getLinks() {
		return links;
	}

	public void addOrUpdate(DocumentDataNode dataNode) {
		if (dataNode.getId() != null) {
			getNodes().put(dataNode.getId(), dataNode);
		}
	}

	public void addOrUpdate(DocumentDataLink dataLink) {
		if (dataLink.getId() != null) {
			getLinks().put(dataLink.getId(), dataLink);
		}
	}

	public DocumentDataNode getNodeById(String nodeId) {
		try {
			for (String tmpId : getNodes().keySet()) {
				if (tmpId.equals(nodeId)) {
					return getNodes().get(tmpId);
				}
			}
		} catch (NullPointerException e) {
			// can't really react
		}
		return null;
	}

	public DocumentDataLink getLinkById(String id) {
		try {
			for (String tmpId : getLinks().keySet()) {
				if (tmpId.equals(id)) {
					return getLinks().get(tmpId);
				}
			}
		} catch (NullPointerException e) {
			// can't really react
		}
		return null;
	}

	public void addConnection(DocumentDataConnection dataConnection) {
		connections.add(dataConnection);
	}

	public List<DocumentDataConnection> getConnections() {
		return connections;
	}
}