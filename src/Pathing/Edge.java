package Pathing;

/**
 * Created by robert on 3/26/2015.
 */
public class Edge {

    private double weight;
    private Node frontNode;
    private Node backNode;

    public Edge(Node newFrontNode, Node newBackNode){

        this.frontNode = newFrontNode;
        this.backNode = newBackNode;
        //calculates weight of the edge as the hypotenuse between the two nodes
        this.weight = Math.sqrt(Math.pow(Math.abs(newFrontNode.getX()-newBackNode.getX()),2) +
                Math.pow(Math.abs(newFrontNode.getY()-newBackNode.getY()),2));
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Node getFrontNode() {
        return frontNode;
    }

    public void setFrontNode(Node frontNode) {
        this.frontNode = frontNode;
    }

    public Node getBackNode() {
        return backNode;
    }

    public void setBackNode(Node backNode) {
        this.backNode = backNode;
    }

}
