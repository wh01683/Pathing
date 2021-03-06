package Pathing;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

/**
 * Created by robert on 4/13/2015.
 */
public class Graph {

    private static ArrayList<Node> nodeSet = new ArrayList<Node>(30);
    private static ArrayList<Edge> edgeSet = new ArrayList<Edge>(30);
    private static Hashtable<Edge, Integer> usedEdgeSet;
    private static Integer numberTraversedEdges = new Integer(0);
    private static Integer numberUnusedEdges = new Integer(0);
    private static Integer totalNodes = new Integer(0);
    private static Integer totalEdges = new Integer(0);

    Random r = new Random();
    private PathingScreen pathingScreen;

    public Graph(int numberNodes, int xHBounds, int yHBounds, int xLBounds, int yLBounds) {
        if (nodeSet == null) {
            nodeSet = new ArrayList<>(numberNodes);
        }
        addRandomNodes(numberNodes, xHBounds, yHBounds, xLBounds, yLBounds);
    }

    public static ArrayList<Node> getNodeSet() {
        return nodeSet;
    }

    public static ArrayList<Edge> getEdgeSet() {
        return edgeSet;
    }

    /**
     * adds a random number of nodes to the screen's vector of nodes
     *
     * @param amount   number of Nodes the method will generate and add to the vector
     * @param xHBounds x coordinates of the new node will be lesser than this value
     * @param yHBounds y coordinates of the new node will be lesser than this value
     * @param xLBounds x coordinates of the new node will be greater than this value
     * @param yLBounds y coordinates of the new node will be greater than this value
     */
    public static void addRandomNodes(int amount, int xHBounds, int yHBounds, int xLBounds, int yLBounds) {
        Node temp = new Node(1, 1);

        for (int i = 0; i < amount; i++) {
            nodeSet.add(temp.getRandomNode(xHBounds, yHBounds, xLBounds, yLBounds));
            totalNodes++;
        }
    }

    public static void removeEdge(Edge e) {
        edgeSet.remove(e);
    }

    public static void clear() {
        edgeSet.removeAll(edgeSet);
        nodeSet.removeAll(nodeSet);
        usedEdgeSet.clear();
        numberUnusedEdges = 0;
        numberTraversedEdges = 0;
        totalNodes = 0;
        totalEdges = 0;
    }

    public static void addUsedEdge(Edge usedEdge) {
        if (usedEdgeSet == null) {
            usedEdgeSet = new Hashtable<Edge, Integer>(5);
        }
        if (!usedEdgeSet.containsKey(usedEdge)) {
            usedEdgeSet.putIfAbsent(usedEdge, 0);
        } else {
            int uses = usedEdgeSet.get(usedEdge);
            uses++;
            usedEdgeSet.put(usedEdge, uses);
        }

        numberTraversedEdges++;
    }

    public static Hashtable<Edge, Integer> getUsedEdgeSet() {
        return usedEdgeSet;
    }

    public static String[] getData() {

        int usedEdges = usedEdgeSet.keySet().size();
        int traversedEdges = 0;
        double weightOfGraph = 0;
        double weightUsed = 0;

        for (Edge e : edgeSet) {
            weightOfGraph += e.getWeight();
        }
        for (Edge k : usedEdgeSet.keySet()) {
            weightUsed += k.getWeight();
        }
        for (Edge e : usedEdgeSet.keySet()) {
            traversedEdges += usedEdgeSet.get(e);
        }

        String[] data = {totalNodes + "", totalEdges + "", traversedEdges + "", usedEdges + "", (int) weightOfGraph + "", (int) weightUsed + ""};

        return data;

    }

    /**
     * adds one to 3 edges to each node currently on the screen. The edge connected to the currently selected node is
     * chosen at random from the current nodes on screen vector by retrieving the node at a randomly generated index
     * between the beginning and the last index containing a node.
     */
    private void addRandomEdges() {
        try {
            for (Node node : nodeSet) {
                for (int i = 0; i < r.nextInt(2) + 1; i++) {

                    totalEdges++;
                    Node tempToNode = nodeSet.get(r.nextInt(nodeSet.size()));

                    while (node == tempToNode) {
                        tempToNode = nodeSet.get(r.nextInt(nodeSet.size()));
                    }
                    Edge tempEdge = new Edge(node, tempToNode);
                    Edge tempEdge2 = new Edge(tempToNode, node);
                    tempEdge.setColor("WHITE");
                    tempEdge2.setColor("WHITE");

                    node.addNeighbor(tempToNode);
                    node.addEdge(tempEdge);

                    tempToNode.addNeighbor(node);
                    tempToNode.addEdge(tempEdge2);

                    edgeSet.add(tempEdge);
                    edgeSet.add(tempEdge2);
                }
            }

        } catch (NullPointerException p) {
            System.out.printf("Null pointer caught in Screen : addRandomEdges.\nLikely attempting to create a new edge with" +
                    " a node which does not exist.");
        }
    }

    public void addEdges() {
        if (edgeSet == null) {
            edgeSet = new ArrayList<>(nodeSet.size() * 2);
            addRandomEdges();
        } else {
            addRandomEdges();
        }
    }
}
