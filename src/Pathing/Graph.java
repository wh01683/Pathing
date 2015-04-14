package Pathing;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by robert on 4/13/2015.
 */
public class Graph {

    private static ArrayList<Node> nodeSet;
    private static ArrayList<Edge> edgeSet;
    Random r = new Random();
    private PathingScreen pathingScreen;

    public Graph(int numberNodes, int xHBounds, int yHBounds, int xLBounds, int yLBounds) {
        if (nodeSet == null) {
            nodeSet = new ArrayList<>(numberNodes);
            addRandomNodes(numberNodes, xHBounds, yHBounds, xLBounds, yLBounds);
        }
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
    private void addRandomNodes(int amount, int xHBounds, int yHBounds, int xLBounds, int yLBounds) {
        Node temp = new Node(1, 1);

        for (int i = 0; i < amount; i++) {
            nodeSet.add(temp.getRandomNode(xHBounds, yHBounds, xLBounds, yLBounds));
        }
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

                    Node tempToNode = nodeSet.get(r.nextInt(nodeSet.size()));
                    Edge tempEdge = new Edge(node, tempToNode);

                    node.addNeighbor(tempToNode);
                    node.addEdge(tempEdge);

                    edgeSet.add(tempEdge);
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
