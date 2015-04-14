package Pathing;

import java.awt.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by robert on 4/13/2015.
 */
public class PrimsMST {

    public PrimsMST(Graph newGraph, Node root, PathingScreen screen, long delayTimer) throws NullPointerException {

        Graph graph = newGraph;

        try {
        /*Comparator for Priority Queue*/
            Comparator<Node> lowestDistance = new Comparator<Node>() {
                @Override
                public int compare(Node node, Node t1) {
                    return (int) (node.getDistanceFromSource() - t1.getDistanceFromSource());
                }
            };

        /*Initialize*/

            for (Node u : Graph.getNodeSet()) {
                u.setDistanceFromSource(Double.POSITIVE_INFINITY);
                screen.paintSingleNode(u, Color.WHITE);
                u.setDiscovered(false);
            }
            root.setDistanceFromSource(0); //starting at root
            screen.paintSingleNode(root, Color.PINK);
            root.setPredecessor(null);

            PriorityQueue<Node> vertices = new PriorityQueue<>(lowestDistance);
            vertices.addAll(Graph.getNodeSet());

            while (!(vertices.isEmpty())) {
                Node u = vertices.remove();

                for (Node v : u.getNeighbors()) {
                    if (!v.isDiscovered() && (u.specificEdge(v).getWeight() < v.getDistanceFromSource())) {
                        v.setDistanceFromSource(u.specificEdge(v).getWeight()); //update distance
                        vertices.add(v); //add vertices to
                        screen.paintSingleEdge(u.specificEdge(v), Color.BLACK);
                        u.specificEdge(v).setColor("BLACK");
                        v.setPredecessor(u);
                    }
                }


                u.setDiscovered(true);
                if (u == root) {
                    screen.paintSingleNode(u, Color.GREEN);
                } else {
                    screen.paintSingleNode(u, Color.BLACK);
                }

                TimeUnit.MILLISECONDS.sleep(delayTimer);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
