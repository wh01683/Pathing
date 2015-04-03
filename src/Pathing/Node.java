package Pathing;

import java.util.Random;
import java.util.Vector;

/**
 * Created by robert on 3/26/2015.
 */
public class Node {

    private static int radius = 16;
    private int x;


    private int y;
    private double distanceFromSource = 0;
    private Vector<Node> neighbors = new Vector<>(3);
    private Vector<Edge> edges = new Vector<>(3);
    private Random r = new Random();

    public Node(int newX, int newY){
        this.x = newX;
        this.y = newY;
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

    public Vector<Node> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Node newNeighbor) {
        this.neighbors.add(newNeighbor);
    }

    public void addEdge(Edge newEdge) {
        this.edges.add(newEdge);
    }

    public Vector getEdges() {
        return this.edges;
    }


    public double getDistanceFromSource() {
        return distanceFromSource;
    }

    public void setDistanceFromSource(double distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
    }
}
