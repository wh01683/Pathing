package Pathing;

/**
 * Created by robert on 3/26/2015.
 */
public class Edge {

    private double weight;
    private Node fromNode;
    private Node toNode;


    /**
     * creates a new edge between two nodes. Node positions are ambiguous. Calculates the "weight" of the
     * edge as the distance (hypotenuse) between he two nodes using the X and Y coordinates of the front
     * and back nodes.
     * */
    public Edge(Node newFromNode, Node newToNode) {

        this.fromNode = newFromNode;
        this.toNode = newToNode;
        //calculates weight of the edge as the hypotenuse between the two nodes
        this.weight = Math.sqrt(Math.pow(Math.abs(newFromNode.getX() - newToNode.getX()), 2) +
                Math.pow(Math.abs(newFromNode.getY() - newToNode.getY()), 2));
    }

    /**
     * returns the weight (i.e. the distance between connecting nodes) of the edge. calculated as the hypotenuse
     *
     * @return weight of the node.
     * */
    public double getWeight() {
        return weight;
    }

    /**
     * returns the "front" node of the current edge. Note: front is ambiguous and the front node will not be
     * immediately discernable from the back node
     *
     * @return returns the front node of the current edge
     * */
    public Node getFromNode() {
        return fromNode;
    }

    /**
     * returns the "back" node of the current edge. Note: back is ambiguous and the back node will not be
     * immediately discernable from the front node.
     *
     * @return returns the back node of the current edge
     * */
    public Node getToNode() {
        return toNode;
    }

}
