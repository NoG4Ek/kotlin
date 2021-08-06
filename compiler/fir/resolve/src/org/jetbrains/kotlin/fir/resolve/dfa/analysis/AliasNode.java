package org.jetbrains.kotlin.fir.resolve.dfa.analysis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jetbrains.kotlin.fir.resolve.dfa.RealVariableAndType;
import org.jetbrains.kotlin.fir.resolve.dfa.utils.StringJoiner;

public class AliasNode {
	Set<RealVariableAndType> _vars;
	Map<RealVariableAndType, AliasNode> _labelToNode;   // labeled outgoing edge
	Map<RealVariableAndType, AliasNode> _labelFromNode; // labeled incoming edge

	public AliasNode() {
		_vars          = new HashSet<>();
		_labelToNode   = new HashMap<>();
		_labelFromNode = new HashMap<>();
	}

	public boolean isEmpty() {
		return _vars.isEmpty();
	}

	public Set<RealVariableAndType> getVariables() {
		return _vars;
	}

	public void addVariable(RealVariableAndType variable) {
		_vars.add(variable);
	}

	public void removeVariable(RealVariableAndType variable) {
		_vars.remove(variable);
	}

	public void addOutEdge(RealVariableAndType label, AliasNode node) {
		this._labelToNode.put(label, node);
		node._labelFromNode.put(label, this);
	}

	public void removeOutEdge(RealVariableAndType label) {
		AliasNode toNode = this._labelToNode.remove(label);
		if (toNode != null) {
			toNode._labelFromNode.remove(label);
		}
	}

	public void removeInEdge(RealVariableAndType label) {
		AliasNode fromNode = this._labelFromNode.remove(label);
		if (fromNode != null) {
			fromNode._labelToNode.remove(label);
		}
	}

	public Map<RealVariableAndType, AliasNode> getOutEdgeMap() {
		return _labelToNode;
	}

	public Map<RealVariableAndType, AliasNode> getInEdgeMap() {
		return _labelFromNode;
	}

	String varsToString() {
		StringJoiner joiner = new StringJoiner(",");
		for (RealVariableAndType var : _vars) joiner.add(var.toString());
		return "{ " + joiner.toString() + " }";
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner("\n");
		joiner.add(varsToString());
		for (Entry<RealVariableAndType, AliasNode> outEdge : _labelToNode.entrySet()) {
			joiner.add("--" + outEdge.getKey() + "--> " + outEdge.getValue().varsToString());
		}
		for (Entry<RealVariableAndType, AliasNode> inEdge : _labelFromNode.entrySet()) {
			joiner.add("<--" + inEdge.getKey() + "-- " + inEdge.getValue().varsToString());
		}
		joiner.add("\n");
		return joiner.toString();
	}

	public int printNodeMustAliasPairs(BufferedWriter br, String instruction) throws IOException {
		int num = 0;
		Set<RealVariableAndType> nodeVars = new HashSet<>(_vars);
		for (RealVariableAndType var: _vars) {
			nodeVars.remove(var);
			for (RealVariableAndType var2: nodeVars) {
				br.write(instruction + "\t" + var + "\t" + var2 + "\n");
				num++;
			}
			if (nodeVars.size() <= 1) break;
		}
		return num;
	}

	public void printNodeOutEdges(BufferedWriter br, String instruction, int nodeID1) throws IOException {
		for (Entry<RealVariableAndType, AliasNode> labelAndNode: _labelToNode.entrySet()) {
			int nodeID2 = labelAndNode.getValue().getNodeId();
			RealVariableAndType label = labelAndNode.getKey();
			br.write(instruction + "\t" + nodeID1 + "\t" + nodeID2 + "\t" + label + "\n");
		}
	}

	public static AliasNode intersectVars(AliasNode n1, AliasNode n2) {
		AliasNode newOne = new AliasNode();
		boolean set1IsLarger = n1._vars.size() > n2._vars.size();
		newOne._vars.addAll(set1IsLarger ? n2._vars : n1._vars);
		newOne._vars.retainAll(set1IsLarger ? n1._vars : n2._vars);
		return newOne;
	}

	public static AliasNode cloneVarsOnly(AliasNode other) {
		AliasNode newOne = new AliasNode();
		newOne._vars = new HashSet<>(other._vars);
		return newOne;
	}

	public static AliasNode cloneAndMarkVarsOnly(AliasNode other) {
		AliasNode newOne = new AliasNode();
		newOne._vars = new HashSet<>();
		newOne._vars.addAll(other._vars);
		return newOne;
	}

	public int getNodeId() {
		return _vars.hashCode();
	}
}
