package Pathing;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by robert on 4/3/2015.
 */
public class PathingScreen extends JPanel {
    private static Vector<Node> nodesOnScreen = new Vector<Node>(5);
    private static Vector<Edge> edgesOnScreen = new Vector<Edge>(5);
    private static PriorityQueue<Node> nodePriorityQueue;
    private static PriorityQueue<Node> priorityQueue;
    private static HashSet<Node> unvisitedNodes;
    private int verticalLines = 800, horizontalLines = 1200;
    private JTextField width;
    private JTextField height;
    private JButton drawNodesButton;
    private JButton clearButton;
    private JButton steppingDjikstraSButton;
    private JTextField nodeNumber;
    private JButton drawEdgesButton;
    private JTextField delayInMilliseconds;
    private JButton delayedDjikstraSButton;
    private JPanel displayPanel;
    private JPanel dashboardPanel;
    private JPanel boundsPanel;
    private JLabel yLabel;
    private JLabel xLabel;
    private JPanel outerPanel;
    private JTextField nodeRadius;
    private JButton resizeButton;
    private Random r = new Random();


    public PathingScreen() {

        displayPanel.setVisible(true);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                displayPanel.setBounds(e.getComponent().getX(), e.getComponent().getY(), e.getComponent().getWidth() - 20, e.getComponent().getHeight() - 20);
            }
        });
        displayPanel.setBounds(displayPanel.getX(), displayPanel.getY(), Integer.parseInt(width.getText()),
                Integer.parseInt(height.getText()));
        this.drawNodesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRandomNodes((displayPanel.getX() + displayPanel.getWidth() - Integer.parseInt(nodeRadius.getText())),
                        (displayPanel.getY() + displayPanel.getHeight() - 200),
                        displayPanel.getX(), displayPanel.getY(), Integer.parseInt(nodeNumber.getText()));

                paintNodesToScreen((Graphics2D) displayPanel.getGraphics());
            }
        });

        this.drawEdgesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent p) {
                addRandomEdges();

                paintEdgesToScreen((Graphics2D) displayPanel.getGraphics());
            }
        });

        this.clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent l) {
                displayPanel.getGraphics().clearRect(displayPanel.getX(), displayPanel.getY(),
                        displayPanel.getX() + displayPanel.getWidth(), displayPanel.getY() + displayPanel.getHeight());
                nodesOnScreen.removeAllElements();
                edgesOnScreen.removeAllElements();
            }
        });

        this.resizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                displayPanel.setBounds(displayPanel.getX(), displayPanel.getY(), Integer.parseInt(width.getText()),
                        Integer.parseInt(height.getText()));
                nodesOnScreen.removeAllElements();
                edgesOnScreen.removeAllElements();
            }
        });

        this.steppingDjikstraSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dijkstraTraversal();
            }
        });

        this.delayedDjikstraSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                delayedDijkstraTraversal(Long.parseLong(delayInMilliseconds.getText()));
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Djikstra Simulation");
        frame.setContentPane(new PathingScreen().outerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)displayPanel.getGraphics();
        g2.setColor(Color.BLACK);
        paintNodesToScreen(g2);
        paintEdgesToScreen(g2);
        displayPanel.paint(g2);
    }

    /**
     * updates the size of the window
     *
     * @param width  new width of the window
     * @param height new height of the window
     */
    public void updateSize(int width, int height) {
        verticalLines = width;
        horizontalLines = height;
        repaint();
    }

    /**
     * adds a random number of nodes to the screen's vector of nodes
     *
     * @param xHBounds x coordinates of the new node will be lesser than this value
     * @param yHBounds  y coordinates of the new node will be lesser than this value
     * @param xLBounds x coordinates of the new node will be greater than this value
     * @param yLBounds y coordinates of the new node will be greater than this value
     * @param amount   number of Nodes the method will generate and add to the vector
     *                 <p>
     */
    public void addRandomNodes(int xHBounds, int yHBounds, int xLBounds, int yLBounds, int amount) {
        Node temp = new Node(1, 1);
        for (int i = 0; i < amount; i++) {
            nodesOnScreen.add(temp.getRandomNode(xHBounds, yHBounds, xLBounds, yLBounds));
        }
    }

    public void paintNodesToScreen(Graphics2D g2) {

        for (Node n : nodesOnScreen) {
            g2.fillOval(n.getX(), n.getY(), Integer.parseInt(nodeRadius.getText()), Integer.parseInt(nodeRadius.getText()));
        }
        repaint();
    }

    public void paintSingleEdge(Edge e) {
        Graphics2D red = (Graphics2D) displayPanel.getGraphics();
        red.setColor(Color.RED);
        red.draw(new Line2D.Double((e.getFrontNode().getX()) + (Integer.parseInt(nodeRadius.getText()) / 2),
                (e.getFrontNode().getY() + (Integer.parseInt(nodeRadius.getText()) / 2)),
                (e.getBackNode().getX() + (Integer.parseInt(nodeRadius.getText()) / 2)),
                (e.getBackNode().getY()) + (Integer.parseInt(nodeRadius.getText()) / 2)));
    }

    public void paintSingleNode(Node n) {
        Graphics2D red = (Graphics2D) displayPanel.getGraphics();
        red.setColor(Color.RED);
        red.fillOval(n.getX(), n.getY(), n.getRadius(), n.getRadius());

    }

    public void paintEdgesToScreen(Graphics2D g2) {
        for (Edge e : edgesOnScreen) {
            g2.draw(new Line2D.Double(e.getFrontNode().getX() + (Integer.parseInt(nodeRadius.getText()) / 2),
                    (e.getFrontNode().getY() + (Integer.parseInt(nodeRadius.getText()) / 2)), (e.getBackNode().getX() + (Integer.parseInt(nodeRadius.getText()) / 2)),
                    (e.getBackNode().getY() + (Integer.parseInt(nodeRadius.getText()) / 2))));
        }

        populateDijsktraData();
        repaint();
    }

    /**
     * adds one to 3 edges to each node currently on the screen. The edge connected to the currently selected node is
     * chosen at random from the current nodes on screen vector by retrieving the node at a randomly generated index
     * between the beginning and the last index containing a node.
     * <p>
     */
    public void addRandomEdges() {
        try {
            for (Node node : nodesOnScreen) {

                for (int i = 0; i < r.nextInt(2) + 1; i++) {
                    int targetNodeIndex = r.nextInt(nodesOnScreen.size());
                    Edge tempEdge = new Edge(node, nodesOnScreen.elementAt(targetNodeIndex));
                    node.addNeighbor(nodesOnScreen.elementAt(targetNodeIndex));
                    node.addEdge(tempEdge);
                    nodesOnScreen.elementAt(targetNodeIndex).addNeighbor(node);
                    nodesOnScreen.elementAt(targetNodeIndex).addEdge(tempEdge);
                    edgesOnScreen.add(tempEdge);
                }
            }
        } catch (NullPointerException p) {
            System.out.printf("Null pointer caught in Screen : addRandomEdges.\nLikely attempting to create a new edge with" +
                    " a node which does not exist.");
        }
    }


    public void delayedDijkstraTraversal(long delayTimer) {

        try {
            Node source = priorityQueue.peek();
            Node previousNode;
            for (Node v : priorityQueue) {
                if (!(v.equals(source))) {
                    v.setDistanceFromSource(2000000000);
                }
            }

            while (!priorityQueue.isEmpty()) {
                Node current = priorityQueue.remove();

                for (Node n : current.getNeighbors()) {
                    double alt = current.getDistanceFromSource() + new Edge(current, n).getWeight();

                    if (alt < n.getDistanceFromSource()) {
                        paintSingleNode(current);
                        paintSingleEdge(new Edge(current, n));
                        paintSingleNode(n);
                        priorityQueue.remove(n);
                        n.setDistanceFromSource(alt);
                        priorityQueue.add(n);
                        current = n;
                    }
                }
                TimeUnit.MILLISECONDS.sleep(delayTimer);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void dijkstraTraversal() {


        dijkstraStep();

    }

    public void dijkstraStep() {

        if (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.remove();

            for (Node n : current.getNeighbors()) {
                current = dijkstraSecondStep(current, n);
            }

        }
    }

    public Node dijkstraSecondStep(Node current, Node n) {
        double alt = current.getDistanceFromSource() + new Edge(current, n).getWeight();

        if (alt < n.getDistanceFromSource()) {
            paintSingleNode(current);
            paintSingleEdge(new Edge(current, n));
            paintSingleNode(n);
            priorityQueue.remove(n);
            n.setDistanceFromSource(alt);
            priorityQueue.add(n);
            return n;
        } else return current;
    }

    /**
     * populates static node priority queue with nodes from the current nodesOnScreen vector
     */
    public void populateDijsktraData() {
        Comparator<Node> lowestWeight = new Comparator<Node>() {
            @Override
            public int compare(Node node1, Node node2) {
                double minEdgeWeightNode1 = ((Edge) node1.getEdges().firstElement()).getWeight();
                for (Edge e : (Vector<Edge>) node1.getEdges()) {
                    if (e.getWeight() <= minEdgeWeightNode1) {
                        minEdgeWeightNode1 = e.getWeight();
                    }
                }
                double minEdgeWeightNode2 = ((Edge) node2.getEdges().firstElement()).getWeight();

                for (Edge e : (Vector<Edge>) node2.getEdges()) {
                    if (e.getWeight() <= minEdgeWeightNode2) {
                        minEdgeWeightNode2 = e.getWeight();
                    }
                }



                return (int) Math.round(Math.abs(minEdgeWeightNode1 - minEdgeWeightNode2));
            }
        };

        Comparator<Node> lowestDistance = new Comparator<Node>() {
            @Override
            public int compare(Node node, Node t1) {
                return (int) Math.abs(node.getDistanceFromSource() - node.getDistanceFromSource());
            }
        };

        nodePriorityQueue = new PriorityQueue<>(nodesOnScreen.size(),
                lowestWeight);
        priorityQueue = new PriorityQueue<>(nodesOnScreen.size(), lowestDistance);
        nodePriorityQueue.addAll(nodesOnScreen);
        priorityQueue.addAll(nodesOnScreen);
        unvisitedNodes = new HashSet<>(nodePriorityQueue);

        Node source = priorityQueue.peek();
        Node previousNode;

        for (Node v : priorityQueue) {
            if (!(v.equals(source))) {
                v.setDistanceFromSource(2000000000);
            }
        }


    }

    /**
     * gets the minimum edge of the given edge vector
     *
     * @param edges edge vector passed to method
     * @return returns edge with the minimum weight
     */
    public Edge minEdge(Vector<Edge> edges) {
        Edge minEdge = edges.firstElement();

        for (Edge e : edges) {
            if (e.getWeight() < minEdge.getWeight()) {
                minEdge = e;
            }
        }
        return minEdge;
    }

}
