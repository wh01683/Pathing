package Pathing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by robert on 3/26/2015.
 */
public class Node implements Comparator<Node>, Comparable<Node> {

    private static int radius = 16;
    private int x;
    private int y;
    private boolean discovered;
    private Node predecessor;
    private double distanceFromSource = 0;
    private ArrayList<Node> neighbors = new ArrayList<>(3);
    private ArrayList<Edge> edges = new ArrayList<>(3);
    private Random r = new Random();

    public Node(int newX, int newY){
        this.x = newX;
        this.y = newY;
    }

    public static double distance(Node node1, Node node2) {
        return Math.sqrt(Math.pow(Math.abs(node1.getX() - node2.getX()), 2) +
                Math.pow(Math.abs(node1.getY() - node2.getY()), 2));
    }

    public Node getRandomNode(int xHBounds, int yHBounds, int xLBounds, int yLBounds){
        return new Node((r.nextInt(xHBounds)+xLBounds), (r.nextInt(yHBounds)+yLBounds));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius(){
        return radius;
    }

    public void setRadius(int newRadius){
        radius = newRadius;
    }

    public ArrayList<Node> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Node newNeighbor) {
        this.neighbors.add(newNeighbor);
    }

    public void addEdge(Edge newEdge) {
        this.edges.add(newEdge);
    }

    public ArrayList getEdges() {
        return this.edges;
    }

    public Edge minEdge() {
        Edge minEdge = edges.get(0);
        for (Edge e : edges) {
            if (e.getWeight() < minEdge.getWeight()) {
                minEdge = e;
            }
        }
        return minEdge;
    }


    public double getDistanceFromSource() {
        return distanceFromSource;
    }

    public void setDistanceFromSource(double distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
    }

    @Override
    public int compareTo(Node otherNode) {
        return (int) (this.distanceFromSource - otherNode.distanceFromSource);
    }

    @Override
    public int compare(Node node1, Node node2) {
        return (int) ((node1.getDistanceFromSource() - node2.getDistanceFromSource()));
    }

    public Edge specificEdge(Node to) {

        for (Edge e : this.edges) {
            if (e.getToNode() == to) {
                return e;
            }
        }
        return null;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }
}
