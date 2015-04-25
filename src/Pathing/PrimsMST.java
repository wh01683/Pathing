package Pathing;

import java.awt.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by robert on 4/13/2015.
 */
public class PrimsMST {

    public PrimsMST(Node root, PathingScreen screen, long delayTimer) throws NullPointerException {

        try {
        /*Comparator for Priority Queue*/
            Comparator<Node> lowestDistance = new Comparator<Node>() {
                @Override
                public int compare(Node node1, Node node2) {
                    return (int) (node1.getDistanceFromSource() - node2.getDistanceFromSource());
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
            root.setPredecessor(root);

            PriorityQueue<Node> vertices = new PriorityQueue<>(lowestDistance);
            vertices.addAll(Graph.getNodeSet());

            while (!(vertices.isEmpty())) {
                Node u = vertices.remove();

                for (Node v : u.getNeighbors()) {
                    if (!v.isDiscovered() && (u.specificEdge(v).getWeight() < v.getDistanceFromSource())) {
                        v.setDistanceFromSource(u.specificEdge(v).getWeight()); //update distance
                        vertices.add(v); //add vertices to
                        v.setPredecessor(u);
                    }

                }
                u.setDiscovered(true);
                if (u == root) {
                    screen.paintSingleNode(u, Color.GREEN);
                } else {
                    screen.paintSingleNode(u, Color.BLACK);
                    screen.paintSingleEdge(u.specificEdge(u.getPredecessor()), Color.BLACK);

                    Graph.addUsedEdge(u.specificEdge(u.getPredecessor()));
                }
                TimeUnit.MILLISECONDS.sleep(delayTimer);
            }

            for (Node n : Graph.getNodeSet()) {
                screen.paintSingleEdge(n.specificEdge(n.getPredecessor()), Color.BLACK);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (NullPointerException p) {
            //don't care 'bout no null pointer
        }
    }
}
