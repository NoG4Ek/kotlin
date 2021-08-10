package org.jetbrains.kotlin.fir.resolve.dfa.analysis;

import org.jetbrains.kotlin.fir.resolve.dfa.RealVariable;
import org.jetbrains.kotlin.fir.resolve.dfa.RealVariableAndType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.jetbrains.kotlin.fir.resolve.dfa.utils.Pair;

public class AliasGraph {
	Set<AliasNode> _nodes;
	Map<RealVariableAndType, AliasNode> _varToNode;
	Map<RealVariable, RealVariableAndType> linkToRVAT;

	public AliasGraph() {
		_nodes     = new HashSet<>();
		_varToNode = new HashMap<>();
		linkToRVAT = new HashMap<>();
	}

	public AliasGraph(AliasGraph orig) {
		this(orig, false);
	}

	public AliasGraph(AliasGraph orig, boolean markVars) {
		this();

		linkToRVAT = orig.linkToRVAT;
		Map<AliasNode, AliasNode> origToNewNode = new HashMap<>();
		for (AliasNode origNode : orig._nodes) {
			if (markVars)
				origToNewNode.put(origNode, AliasNode.cloneAndMarkVarsOnly(origNode));
			else
				origToNewNode.put(origNode, AliasNode.cloneVarsOnly(origNode));
		}

		for (AliasNode origNode : orig._nodes) {
			AliasNode newNode = origToNewNode.get(origNode);
			addNode(newNode);
			for (Entry<RealVariableAndType, AliasNode> origOutEdge : origNode.getOutEdgeMap().entrySet()) {
				newNode.addOutEdge(origOutEdge.getKey(), origToNewNode.get(origOutEdge.getValue()));
			}
		}
	}

	public void union(AliasGraph other) {
		AliasGraph thisC = new AliasGraph(this);
		Map<AliasNode, AliasNode> fromOtherToThis = new HashMap<>();
		for (AliasNode otherNode : other._nodes) {
			AliasNode thisNode = null;
			for (RealVariableAndType var : otherNode._vars) {
				AliasNode temp = _varToNode.get(var);
				// var exists in "this" graph
				if (temp != null) {
					// The two graphs have conflicts! Should not happen
					if (thisNode != null && thisNode != temp) {
						System.out.println("WRONG!!!!!!!!!!!!!!!!!!!!!!");
						System.out.println(other + " -- "+thisC);
						throw new RuntimeException();
					}
					thisNode = temp;
				}
			}
			fromOtherToThis.put(otherNode, thisNode);
		}
		for (AliasNode otherNode : other._nodes) {
			AliasNode thisNode = fromOtherToThis.get(otherNode);
			if (thisNode == null) {
				thisNode = new AliasNode();
				_nodes.add(thisNode);
				fromOtherToThis.put(otherNode, thisNode);
			}
			for (RealVariableAndType var : otherNode._vars) {
				AliasNode temp = _varToNode.get(var);
				// var doesn't exist in "this" graph
				// should appear in "thisNode"
				if (temp == null) {
					addVariableToNode(thisNode, var);
				}
			}
		}
		for (AliasNode otherNode : other._nodes) {
			AliasNode thisNode = fromOtherToThis.get(otherNode);
			for (RealVariableAndType label : otherNode._labelToNode.keySet()) {
				AliasNode otherTarget = otherNode._labelToNode.get(label);
				AliasNode thisTarget = fromOtherToThis.get(otherTarget);
				thisNode.addOutEdge(label, thisTarget);
			}
		}
	}

	public boolean equals(AliasGraph other) {
		if (_nodes.size() != other._nodes.size())
			return false;

		Map<AliasNode, AliasNode> fromThisToOther = new HashMap<>();
		for (AliasNode node : _nodes) {
			AliasNode otherNode = null;
			for (RealVariableAndType var : node._vars) {
				AliasNode temp = other._varToNode.get(var);
				// for the first iteration
				if (otherNode == null)
					otherNode = temp;

				if (otherNode != temp)
					return false;
			}
			if (otherNode == null ||
			    node._vars.size() != otherNode._vars.size() ||
			    node._labelToNode.size() != otherNode._labelToNode.size() ||
			    node._labelFromNode.size() != otherNode._labelFromNode.size())
				return false;
			fromThisToOther.put(node, otherNode);
		}
		for (AliasNode node : _nodes) {
			AliasNode otherNode = fromThisToOther.get(node);
			for (RealVariableAndType label : node._labelToNode.keySet()) {
				AliasNode target = node._labelToNode.get(label);
				AliasNode otherTarget = otherNode._labelToNode.get(label);
				if (fromThisToOther.get(target) != otherTarget)
					return false;
			}
			for (RealVariableAndType label : node._labelFromNode.keySet()) {
				AliasNode source = node._labelFromNode.get(label);
				AliasNode otherSource = otherNode._labelFromNode.get(label);
				if (fromThisToOther.get(source) != otherSource)
					return false;
			}
		}
		return true;
	}

	public boolean addLinkRVtoRVAT(RealVariable rv, RealVariableAndType rvat){
		linkToRVAT.put(rv, rvat);
		return true;
	}

	public RealVariableAndType getRVATfromRV(RealVariable rv){
		return linkToRVAT.get(rv);
	}

	public void addNode(AliasNode node) {
		_nodes.add(node);
		for (RealVariableAndType var : node.getVariables()) {
			_varToNode.put(var, node);
		}
	}

	// remove node, and all edges associated with it
	public void removeNode(AliasNode node) {
		_nodes.remove(node);
		for (RealVariableAndType var : node.getVariables()) {
			_varToNode.remove(var);
		}
		// Get copies because the loops are destructive
		Set<Entry<RealVariableAndType, AliasNode>> inEdges = new HashSet<>(node.getInEdgeMap().entrySet());
		for (Entry<RealVariableAndType, AliasNode> inEdge : inEdges) {
			inEdge.getValue().removeOutEdge(inEdge.getKey());
		}
		Set<Entry<RealVariableAndType, AliasNode>> outEdges = new HashSet<>(node.getOutEdgeMap().entrySet());
		for (Entry<RealVariableAndType, AliasNode> outEdge : outEdges) {
			outEdge.getValue().removeInEdge(outEdge.getKey());
		}
	}

	void addVariableToNode(AliasNode node, RealVariableAndType var) {
		node.addVariable(var);
		_varToNode.put(var, node);
	}

	void removeVariableFromNode(AliasNode node, RealVariableAndType var) {
		if (node != null) {
			node.removeVariable(var);
			_varToNode.remove(var);
		}
	}

	public Set<AliasNode> getNodes() {
		return _nodes;
	}

	public AliasNode lookupVar(RealVariableAndType var) {
		return _varToNode.get(var);
	}

	// Traverse set of nodes in the graph to find ones that either:
	//  -contain a single var, are pointed by nothing and point to nothing
	//  -are empty (contain no vars) and have zero in-edges
    //  -are empty (contain no vars) have one in-edge and zero out-edges
	public void gcNodes() {
		assert sanityVariables();
		assert sanityEdges2way();
		boolean changed;
		do {
			changed = false;
			Set<AliasNode> nodesToRemove = new HashSet<>();
			for (AliasNode n : _nodes) {
				int numVars = n.getVariables().size();
				int numOutEdges = n.getOutEdgeMap().size();
				int numInEdges = n.getInEdgeMap().size();
				boolean nodeToRemove = false;
				nodeToRemove = (numVars == 1 && (numOutEdges + numInEdges == 0));
				nodeToRemove = nodeToRemove || (numVars == 0 && numInEdges == 0);
				nodeToRemove = nodeToRemove || (numVars == 0 && numInEdges == 1 && numOutEdges == 0);
				if (nodeToRemove) {
					nodesToRemove.add(n);
					changed = true;
				}
			}
			for (AliasNode n : nodesToRemove) {
				removeNode(n);
			}
		} while(changed);
		//		assert sanityForMovesOnly();
	}

	// Removes all edges associated with each node in graph.
	public void removeHeapAliases() {
		for (AliasNode node : _nodes) {
			// Get copies because the loops are destructive
			Set<Entry<RealVariableAndType, AliasNode>> inEdges = new HashSet<>(node.getInEdgeMap().entrySet());
			for (Entry<RealVariableAndType, AliasNode> inEdge : inEdges) {
				inEdge.getValue().removeOutEdge(inEdge.getKey());
			}
			Set<Entry<RealVariableAndType, AliasNode>> outEdges = new HashSet<>(node.getOutEdgeMap().entrySet());
			for (Entry<RealVariableAndType, AliasNode> outEdge : outEdges) {
				outEdge.getValue().removeInEdge(outEdge.getKey());
			}
		}
	}

	// Removes all local variables from the graph.
	// Algorithm:
	// 1. In every node that contains variables to be remmapted (i.e. arguments,
	//    return variable, this), we remove all variables that won't.
	// 2. We keep all nodes that connect with a remmaping node. That is they have
	//    at least one in or out edge to a remmaping node or to a node that
	//    connects with them.
	// 3. We remove the remaining nodes.
	public void removeLocalVars(Set<RealVariableAndType> variablesToMap, Set<RealVariableAndType> callerVariables) {
		Set<AliasNode> checked = new HashSet<AliasNode>();
		Set<AliasNode> toBeRemoved = new HashSet<AliasNode>(_nodes);

		// Step 1
		for (RealVariableAndType var : variablesToMap) {
			AliasNode node = lookupVar(var);
			if (node != null && !checked.contains(node)) {
				checked.add(node);
				toBeRemoved.remove(node);
				Set<RealVariableAndType> allVars = new HashSet<RealVariableAndType>(node.getVariables());
				for (RealVariableAndType localVar : allVars) {
					if (!variablesToMap.contains(localVar) && !callerVariables.contains(localVar)) {
						node.removeVariable(localVar);
						_varToNode.remove(localVar);
					}
				}
			}
		}

		// Step 2
		while (checked.size() > 0) {
			Set<AliasNode> temp = new HashSet<AliasNode>(checked);
			checked.clear();
			for (AliasNode node : temp) {
				Collection<AliasNode> inNodes = node.getInEdgeMap().values();
				for (AliasNode inNode : inNodes) {
					if (toBeRemoved.contains(inNode)) {
						checked.add(inNode);
						toBeRemoved.remove(inNode);
					}
				}

				Collection<AliasNode> outNodes = node.getOutEdgeMap().values();
				for (AliasNode outNode : outNodes) {
					if (toBeRemoved.contains(outNode)) {
						checked.add(outNode);
						toBeRemoved.remove(outNode);
					}
				}
			}
		}

		// Step 3
		for (AliasNode node : toBeRemoved) {
			if (!callerVariables.containsAll(node.getVariables()))
				removeNode(node);
		}
	}

	/*
	public void renameVars(Map<String, List<String>> mapping) {
		//remapping of vars
		if (Facts.PRINT) System.out.println("RENAME renameVars -> " + mapping);
		for (String from : mapping.keySet()) {
			AliasNode oldNode = _varToNode.get(from);
			if (oldNode != null) {
				removeVariableFromNode(oldNode, from);
				for (String to : mapping.get(from)) {
					addVariableToNode(oldNode, to);
				}
			}
			// create new node if a variable is mapped to more than one variables
			else if (mapping.get(from).size() > 1) {
				AliasNode newNode = new AliasNode();
				for (String to : mapping.get(from)) {
					newNode.addVariable(to);
				}
				addNode(newNode);
			}
		}
	}
	*/

	public void varsOnCall(Map<RealVariableAndType, List<RealVariableAndType>> mapping) {
		for (RealVariableAndType from : mapping.keySet()) {
			AliasNode node = _varToNode.get(from);
			if (node != null) {
				for (RealVariableAndType to : mapping.get(from))
					addVariableToNode(node, to);
			}
			else {
				node = new AliasNode();
				addNode(node);
				addVariableToNode(node, from);
				for (RealVariableAndType to : mapping.get(from))
					addVariableToNode(node, to);
			}
		}
	}

	public void varsOnReturn() {
		for (AliasNode node : _nodes) {
			Set<RealVariableAndType> variables = new HashSet<>(node.getVariables());
			for (RealVariableAndType var : variables) {
				removeVariableFromNode(node, var);
				//if (var.startsWith("[")) {
				//System.out.println("[var]: " + var);
				//System.out.println("var: " + var.substring(1, var.length() - 1));
				addVariableToNode(node, var);
			}
		}
	}


	// Populates the current graph with the intersection of two existing ones.
	// Just like in copyContentsFrom, the current graph is assumed initialized.
	// Algorithm:
	// 1. For every nodes i,j of the original two graphs, we create new node (i,j).
	//    Its variables are the intersection of the variables of i and of j.
	// 2. For every label f, if the first of the input graphs has an edge (i,k) with
	//    label f, and the second graph has an edge (j, l), also with label f, the
	//    intersection result has an edge ((i,j), (k,l)) with label f.
	public void intersect(AliasGraph graph1, AliasGraph graph2) {
		if (graph1 == null || graph2 == null)
			return;

		Map<Pair<AliasNode, AliasNode>, AliasNode> oldToNewNode = new HashMap<>();
		// Step 1
		for (AliasNode oldNode1 : graph1._nodes) {
			for (AliasNode oldNode2 : graph2._nodes) {
				AliasNode newNode = AliasNode.intersectVars(oldNode1, oldNode2);
				oldToNewNode.put(new Pair<>(oldNode1,oldNode2), newNode);
				addNode(newNode);
			}
		}

		// Step 2
		for (AliasNode oldNode1 : graph1._nodes) {
			for (AliasNode oldNode2 : graph2._nodes) {
				intersectNodes(oldNode1, oldNode2, oldToNewNode);
			}
		}
		gcNodes();
	}

	// Updates the graph to correctly represent a new node as the intersection
	// of two original nodes.
	void intersectNodes(AliasNode oldNode1, AliasNode oldNode2, Map<Pair<AliasNode,AliasNode>, AliasNode> oldToNewNode) {
		AliasNode newNode = oldToNewNode.get(new Pair<>(oldNode1,oldNode2));
		for (Entry<RealVariableAndType,AliasNode> oldOutEdge1 : oldNode1.getOutEdgeMap().entrySet()) {
			RealVariableAndType label = oldOutEdge1.getKey();
			AliasNode oldNextNode1 = oldOutEdge1.getValue();
			AliasNode oldNextNode2 = oldNode2.getOutEdgeMap().get(label);
			if (oldNextNode2 != null) {
				AliasNode newNextNode = oldToNewNode.get(new Pair<>(oldNextNode1, oldNextNode2));
				newNode.addOutEdge(label, newNextNode);
			}
		}
	}


	public Set<RealVariableAndType> allAliases(RealVariable accessPath) {
		AliasNode startNode = lookupVar(getRVATfromRV(accessPath));
		if (startNode == null) return null;

		return new HashSet<>(startNode.getVariables());
	}

	public boolean removeVar(RealVariableAndType var) {
		AliasNode nodeRV = lookupVar(var);
		if (nodeRV == null) return false;

		nodeRV.removeVariable(var);
		linkToRVAT.remove(var.getVariable());
		return false;
	}


	public boolean sanityVariables() {
		for (AliasNode node : _nodes) {
			for (RealVariableAndType var : node.getVariables()) {
				assert _varToNode.get(var) == node;
			}
		}
		return true;
	}

	// properties that should hold if we only have move instructions
	public boolean sanityForMovesOnly() {
		for (AliasNode node : _nodes) {
			assert !node.getVariables().isEmpty();
		}

		assert new HashSet<AliasNode>(_varToNode.values()).size() == _nodes.size();
		return true;
	}

	public boolean sanityEdges2way() {
		for (AliasNode node : _nodes) {
			for (Entry<RealVariableAndType, AliasNode> inEdge : node.getInEdgeMap().entrySet()) {
				assert inEdge.getValue().getOutEdgeMap().get(inEdge.getKey()) == node;
			}
			for (Entry<RealVariableAndType, AliasNode> outEdge : node.getOutEdgeMap().entrySet()) {
				assert outEdge.getValue().getInEdgeMap().get(outEdge.getKey()) == node;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		if (_nodes.isEmpty()) return "{}";

		String nodes = "";
		for (AliasNode node: _nodes) {
			nodes += node.toString();
		}
		return nodes;
	}

//	public String getGraphMustAliasPairs(String instruction) {
//		String aliases = "";
//		for (AliasNode node: _nodes) {
//			if (node.getVariables().size() > 1) {
//				aliases += node.getNodeMustAliasPairs(instruction);
//			}
//		}
//		return aliases;
//	}

	public int printGraphMustAliasPairs(BufferedWriter br, String instruction) throws IOException {
		int num = 0;
		for (AliasNode node: _nodes) {
			if (node.getVariables().size() > 1) {
				num += node.printNodeMustAliasPairs(br, instruction);
			}
		}
		return num;
	}

//	public String getGraphNodes(String instruction, int n) {
//		String nodes = "";
//		if (n == 1 || n == 100 || n == 1000 || n == 10000 || n == 50000 || n == 100000 || n == 130988)
//			System.out.println("nodes: " + _nodes.size());
//		for (AliasNode node: _nodes) {
//			int nodeID = node.getNodeId();
//			Set<String> vars = node.getVariables();
//			for (String var: vars) {
//				nodes += instruction + "\t" + nodeID + "\t" + var + "\n";
//			}
//		}
//		return nodes;
//	}

	public void printGraphNodes(BufferedWriter br, String instruction, int n) throws IOException {
		if (n == 1 || n == 100 || n == 1000 || n == 10000 || n == 50000 || n == 100000 || n == 130988)
			System.out.println("nodes: " + _nodes.size());
		for (AliasNode node: _nodes) {
			int nodeID = node.getNodeId();
			Set<RealVariableAndType> vars = node.getVariables();
			for (RealVariableAndType var: vars) {
				br.write(instruction + "\t" + nodeID + "\t" + var + "\n");
			}
		}
	}

//	public String getGraphEdges(String instruction) {
//		String edges = "";
//		for (AliasNode node: _nodes) {
//			int nodeID1 = node.getNodeId();
//			edges += node.getNodeOutEdges(instruction, nodeID1);
//		}
//		return edges;
//	}

	public void printGraphEdges(BufferedWriter br, String instruction) throws IOException {
		for (AliasNode node: _nodes) {
			int nodeID1 = node.getNodeId();
			node.printNodeOutEdges(br, instruction, nodeID1);
		}
	}
}
