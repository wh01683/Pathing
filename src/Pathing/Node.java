package Pathing;

import java.util.Vector;
import java.util.Random;

/**
 * Created by robert on 3/26/2015.
 */
public class Node {

    private int x;
    private int y;
    private static int radius = 16;
    private Vector<Node> neighbors;
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
}
