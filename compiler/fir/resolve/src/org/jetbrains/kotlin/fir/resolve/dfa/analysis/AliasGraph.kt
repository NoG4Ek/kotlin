package org.jetbrains.kotlin.fir.resolve.dfa.analysis

import org.jetbrains.kotlin.fir.resolve.dfa.RealVariable
import org.jetbrains.kotlin.fir.resolve.dfa.RealVariableAndType
import org.jetbrains.kotlin.fir.resolve.dfa.analysis.AliasNode.Companion.cloneVarsOnly
import org.jetbrains.kotlin.fir.resolve.dfa.analysis.AliasNode.Companion.intersectVars

class AliasGraph() {
    private val nodesSet: MutableSet<AliasNode> = HashSet()
    private val varToNode: MutableMap<RealVariableAndType, AliasNode> = HashMap()
    private val linkToRVAT: MutableMap<RealVariable, RealVariableAndType> = HashMap()
    val nodes: Set<AliasNode>
        get() = nodesSet

    constructor(orig: AliasGraph) : this() {
        linkToRVAT.putAll(orig.linkToRVAT)

        val origToNewNode: MutableMap<AliasNode, AliasNode> = HashMap()
        for (origNode in orig.nodesSet) {
            origToNewNode[origNode] = cloneVarsOnly(origNode)
        }
        for (origNode in orig.nodesSet) {
            val newNode = origToNewNode[origNode]!!
            addNode(newNode)
            for ((key, value) in origNode.outEdgeMap) {
                newNode.addOutEdge(key, origToNewNode[value]!!)
            }
        }
    }

    fun union(other: AliasGraph) {
        AliasGraph(this)
        val fromOtherToThis: MutableMap<AliasNode, AliasNode> = HashMap()
        for (otherNode in other.nodesSet) {
            var thisNode: AliasNode? = null
            for (`var` in otherNode.variables) {
                val temp = varToNode[`var`]
                // var exists in "this" graph
                if (temp != null) {
                    // The two graphs have conflicts! Should not happen
                    if (thisNode != null && thisNode != temp) {
                        throw RuntimeException()
                    }
                    thisNode = temp
                }
            }
            if (thisNode != null) {
                fromOtherToThis[otherNode] = thisNode
            }
        }
        for (otherNode in other.nodesSet) {
            var thisNode = fromOtherToThis[otherNode]
            if (thisNode == null) {
                thisNode = AliasNode()
                nodesSet.add(thisNode)
                fromOtherToThis[otherNode] = thisNode
            }
            for (`var` in otherNode.variables) {
                val temp = varToNode[`var`]
                // var doesn't exist in "this" graph
                // should appear in "thisNode"
                if (temp == null) {
                    addVariableToNode(thisNode, `var`)
                }
            }
        }
        for (otherNode in other.nodesSet) {
            val thisNode = fromOtherToThis[otherNode]
            for (label in otherNode.outEdgeMap.keys) {
                val otherTarget = otherNode.outEdgeMap[label]
                val thisTarget = fromOtherToThis[otherTarget]
                thisNode!!.addOutEdge(label, thisTarget!!)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AliasGraph) return false
        if (nodesSet.size != other.nodesSet.size) return false
        val fromThisToOther: MutableMap<AliasNode?, AliasNode> = HashMap()
        for (node in nodesSet) {
            var otherNode: AliasNode? = null
            for (`var` in node.variables) {
                val temp = other.varToNode[`var`]
                // for the first iteration
                if (otherNode == null) otherNode = temp
                if (otherNode != temp) return false
            }
            if (otherNode == null || node.variables.size != otherNode.variables.size || node.outEdgeMap.size != otherNode.outEdgeMap.size || node.inEdgeMap.size != otherNode.inEdgeMap.size) return false
            fromThisToOther[node] = otherNode
        }
        for (node in nodesSet) {
            val otherNode = fromThisToOther[node]
            for (label in node.outEdgeMap.keys) {
                val target = node.outEdgeMap[label]
                val otherTarget = otherNode!!.outEdgeMap[label]
                if (fromThisToOther[target] != otherTarget) return false
            }
            for (label in node.inEdgeMap.keys) {
                val source = node.inEdgeMap[label]
                val otherSource = otherNode!!.inEdgeMap[label]
                if (fromThisToOther[source] != otherSource) return false
            }
        }
        return true
    }

    fun addLinkRVtoRVAT(rv: RealVariable, rvat: RealVariableAndType): Boolean {
        linkToRVAT[rv] = rvat
        return true
    }

    fun getRVATfromRV(rv: RealVariable): RealVariableAndType? {
        return linkToRVAT[rv]
    }

    fun addNode(node: AliasNode) {
        nodesSet.add(node)
        for (`var` in node.variables) {
            varToNode[`var`] = node
        }
    }

    // remove node, and all edges associated with it
    fun removeNode(node: AliasNode) {
        nodesSet.remove(node)
        for (`var` in node.variables) {
            varToNode.remove(`var`)
        }
        // Get copies because the loops are destructive
        val inEdges = node.inEdgeMap
        for ((key, value) in inEdges) {
            value.removeOutEdge(key)
        }
        val outEdges = node.outEdgeMap
        for ((key, value) in outEdges) {
            value.removeInEdge(key)
        }
    }

    fun addVariableToNode(node: AliasNode, `var`: RealVariableAndType) {
        node.addVariable(`var`)
        varToNode[`var`] = node
    }

    fun removeVariableFromNode(node: AliasNode, `var`: RealVariableAndType) {
        node.removeVariable(`var`)
        varToNode.remove(`var`)
    }

    fun lookupVar(`var`: RealVariableAndType): AliasNode? {
        return varToNode[`var`]
    }

    // Traverse set of nodes in the graph to find ones that either:
    //  -contain a single var, are pointed by nothing and point to nothing
    //  -are empty (contain no vars) and have zero in-edges
    //  -are empty (contain no vars) have one in-edge and zero out-edges
    fun gcNodes() {
        //assert sanityVariables();
        assert(sanityEdges2way())
        var changed: Boolean
        do {
            changed = false
            val nodesToRemove: MutableSet<AliasNode> = HashSet()
            for (n in nodesSet) {
                val numVars = n.variables.size
                val numOutEdges = n.outEdgeMap.size
                val numInEdges = n.inEdgeMap.size
                var nodeToRemove: Boolean
                nodeToRemove = numVars == 1 && numOutEdges + numInEdges == 0
                nodeToRemove = nodeToRemove || numVars == 0 && numInEdges == 0
                nodeToRemove = nodeToRemove || numVars == 0 && numInEdges == 1 && numOutEdges == 0
                if (nodeToRemove) {
                    nodesToRemove.add(n)
                    changed = true
                }
            }
            for (n in nodesToRemove) {
                removeNode(n)
            }
        } while (changed)
        //		assert sanityForMovesOnly();
    }

    // Removes all edges associated with each node in graph.
    fun removeHeapAliases() {
        for (node in nodesSet) {
            // Get copies because the loops are destructive
            val inEdges = node.inEdgeMap
            for ((key, value) in inEdges) {
                value.removeOutEdge(key)
            }
            val outEdges = node.outEdgeMap
            for ((key, value) in outEdges) {
                value.removeInEdge(key)
            }
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
    fun varsOnCall(mapping: Map<RealVariableAndType, List<RealVariableAndType>>) {
        for (from in mapping.keys) {
            var node = varToNode[from]
            if (node != null) {
                for (to in mapping[from]!!) addVariableToNode(node, to)
            } else {
                node = AliasNode()
                addNode(node)
                addVariableToNode(node, from)
                for (to in mapping[from]!!) addVariableToNode(node, to)
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
    fun intersect(graph1: AliasGraph?, graph2: AliasGraph?) {
        if (graph1 == null || graph2 == null) return
        val oldToNewNode: MutableMap<Pair<AliasNode?, AliasNode?>, AliasNode> = HashMap()
        // Step 1
        for (oldNode1 in graph1.nodesSet) {
            for (oldNode2 in graph2.nodesSet) {
                val newNode = intersectVars(oldNode1, oldNode2)
                oldToNewNode[Pair(oldNode1, oldNode2)] = newNode
                addNode(newNode)
            }
        }

        // Step 2
        for (oldNode1 in graph1.nodesSet) {
            for (oldNode2 in graph2.nodesSet) {
                intersectNodes(oldNode1, oldNode2, oldToNewNode)
            }
        }
        gcNodes()
    }

    // Updates the graph to correctly represent a new node as the intersection
    // of two original nodes.
    fun intersectNodes(oldNode1: AliasNode?, oldNode2: AliasNode?, oldToNewNode: Map<Pair<AliasNode?, AliasNode?>, AliasNode>) {
        val newNode = oldToNewNode[Pair(oldNode1, oldNode2)]
        for ((label, oldNextNode1) in oldNode1!!.outEdgeMap) {
            val oldNextNode2 = oldNode2!!.outEdgeMap[label]
            if (oldNextNode2 != null) {
                val newNextNode =
                    oldToNewNode[Pair(oldNextNode1, oldNextNode2)]
                newNode!!.addOutEdge(label, newNextNode!!)
            }
        }
    }

    fun allAliases(accessPath: RealVariable): Set<RealVariableAndType>? {
        val startNode = getRVATfromRV(accessPath)?.let { lookupVar(it) } ?: return null
        return HashSet(startNode.variables)
    }

    fun removeVar(`var`: RealVariableAndType): Boolean {
        val nodeRV = lookupVar(`var`) ?: return false
        nodeRV.removeVariable(`var`)
        varToNode.remove(`var`)
        linkToRVAT.remove(`var`.variable)
        return true
    }

    fun sanityVariables(): Boolean {
        for (node in nodesSet) {
            for (`var` in node.variables) {
                assert(varToNode[`var`] == node)
            }
        }
        return true
    }

    // properties that should hold if we only have move instructions
    fun sanityForMovesOnly(): Boolean {
        for (node in nodesSet) {
            assert(node.variables.isNotEmpty())
        }
        assert(HashSet(varToNode.values).size == nodesSet.size)
        return true
    }

    fun sanityEdges2way(): Boolean {
        for (node in nodesSet) {
            for ((key, value) in node.inEdgeMap) {
                assert(value.outEdgeMap[key] == node)
            }
            for ((key, value) in node.outEdgeMap) {
                assert(value.inEdgeMap[key] == node)
            }
        }
        return true
    }

    override fun toString(): String {
        if (nodesSet.isEmpty()) return "{}"
        val strNodes = StringBuilder()
        for (node in nodesSet) {
            strNodes.append("{").append(node.toString()).append("}")
        }
        return strNodes.toString()
    }

    override fun hashCode(): Int {
        var result = nodesSet.hashCode()
        result = 31 * result + varToNode.hashCode()
        result = 31 * result + linkToRVAT.hashCode()
        return result
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

    //	public String getGraphEdges(String instruction) {

    //		String edges = "";
    //		for (AliasNode node: _nodes) {
    //			int nodeID1 = node.getNodeId();
    //			edges += node.getNodeOutEdges(instruction, nodeID1);
    //		}
    //		return edges;
    //	}
}