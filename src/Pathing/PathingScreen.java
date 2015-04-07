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
    private static PriorityQueue<Node> dijkstraPriorityQueue;
    private static PriorityQueue<Node> primsPriorityQueue;
    private static HashSet<Node> mstNodes;
    private static HashSet<Node> dijkstraNodes;
    private int verticalLines = 800, horizontalLines = 1200;
    private JTextField width;
    private JTextField height;
    private JButton drawNodesButton;
    private JButton clearButton;
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
    private JButton primsSpanningTree;
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

        this.delayedDjikstraSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                delayedDijkstraTraversal(Long.parseLong(delayInMilliseconds.getText()));
            }
        });

        this.primsSpanningTree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                delayedPrimsTraversal(Long.parseLong(delayInMilliseconds.getText()));
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

    public void paintSingleEdge(Edge e, Color newColor) {
        Graphics2D red = (Graphics2D) displayPanel.getGraphics();
        red.setColor(newColor);
        red.draw(new Line2D.Double((e.getFromNode().getX()) + (Integer.parseInt(nodeRadius.getText()) / 2),
                (e.getFromNode().getY() + (Integer.parseInt(nodeRadius.getText()) / 2)),
                (e.getToNode().getX() + (Integer.parseInt(nodeRadius.getText()) / 2)),
                (e.getToNode().getY()) + (Integer.parseInt(nodeRadius.getText()) / 2)));
    }

    public void paintSingleNode(Node n, Color newColor) {
        Graphics2D red = (Graphics2D) displayPanel.getGraphics();
        red.setColor(newColor);
        red.fillOval(n.getX(), n.getY(), n.getRadius(), n.getRadius());

    }

    public void paintEdgesToScreen(Graphics2D g2) {
        for (Edge e : edgesOnScreen) {
            g2.draw(new Line2D.Double(e.getFromNode().getX() + (Integer.parseInt(nodeRadius.getText()) / 2),
                    (e.getFromNode().getY() + (Integer.parseInt(nodeRadius.getText()) / 2)), (e.getToNode().getX() + (Integer.parseInt(nodeRadius.getText()) / 2)),
                    (e.getToNode().getY() + (Integer.parseInt(nodeRadius.getText()) / 2))));
        }
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
                    Node tempToNode = nodesOnScreen.elementAt(r.nextInt(nodesOnScreen.size()));
                    Edge tempEdge = new Edge(node, tempToNode);

                    node.addNeighbor(tempToNode);
                    node.addPathOut(tempEdge);

                    tempToNode.addNeighbor(node);
                    tempToNode.addPathIn(tempEdge);

                    edgesOnScreen.add(tempEdge);
                }
            }
        } catch (NullPointerException p) {
            System.out.printf("Null pointer caught in Screen : addRandomEdges.\nLikely attempting to create a new edge with" +
                    " a node which does not exist.");
        }
    }

    public void delayedPrimsTraversal(long delayTimer) {
        try {
            Comparator<Node> lowestDistance = new Comparator<Node>() {
                @Override
                public int compare(Node node, Node t1) {
                    return (int) (node.getDistanceFromSource() - t1.getDistanceFromSource());
                }
            };

            primsPriorityQueue = new PriorityQueue<>(nodesOnScreen.size(), lowestDistance);
            primsPriorityQueue.addAll(nodesOnScreen);

            mstNodes = new HashSet<>(nodesOnScreen.size());
            Node previous = null;

            Node source = primsPriorityQueue.peek();
            source.setDistanceFromSource(0);

            for (Node n : nodesOnScreen) {
                if (!(n.equals(source))) {
                    n.setDistanceFromSource(Double.POSITIVE_INFINITY);
                }

            }

            boolean start = true;

            while (!mstNodes.containsAll(nodesOnScreen)) {

                Node u = primsPriorityQueue.remove();

                if (!mstNodes.contains(u)) {

                    mstNodes.add(u);

                    if (start) {
                        paintSingleNode(u, Color.BLUE);
                        start = false;
                    } else {
                        paintSingleNode(u, Color.GREEN);
                    }

                    nodesOnScreen.remove(u);

                    if (!(previous == null)) {
                        paintSingleEdge(new Edge(previous, u), Color.GREEN);
                    }

                    for (Node n : u.getNeighbors()) {
                        if (new Edge(u, n).getWeight() < n.getDistanceFromSource()) {
                            n.setDistanceFromSource(new Edge(u, n).getWeight());
                        }
                    }
                    previous = u;
                }
                TimeUnit.MILLISECONDS.sleep(delayTimer);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void delayedDijkstraTraversal(long delayTimer) {

        try {
            dijkstraNodes = new HashSet<>(nodesOnScreen.size());

            Comparator<Node> lowestDistance = new Comparator<Node>() {
                @Override
                public int compare(Node node1, Node node2) {
                    return (int) (node1.getDistanceFromSource() - node2.getDistanceFromSource());
                }
            };
            dijkstraPriorityQueue = new PriorityQueue<>(nodesOnScreen.size(), lowestDistance);

            Node currentNode = nodesOnScreen.firstElement();
            nodesOnScreen.remove(currentNode);
            dijkstraPriorityQueue.add(currentNode);
            Node previous = null;

            for (Node v : nodesOnScreen) {
                v.setDistanceFromSource(Double.POSITIVE_INFINITY);
            }

            dijkstraPriorityQueue.addAll(nodesOnScreen);

            boolean start = true;

            while (!(dijkstraNodes.containsAll(nodesOnScreen))) {

                if (!(previous == null)) {
                    currentNode = dijkstraPriorityQueue.remove();
                    Edge temp = previous.specificEdge(currentNode);
                    if (!(temp == null)) {
                        paintSingleEdge(previous.specificEdge(currentNode), Color.RED);
                    }
                }

                if (!(dijkstraNodes.contains(currentNode))) {

                    if (start) {
                        paintSingleNode(currentNode, Color.CYAN);
                        start = false;
                    } else {
                        paintSingleNode(currentNode, Color.RED);
                    }
                    dijkstraNodes.add(currentNode);

                    for (Node neighborNode : currentNode.getNeighbors()) {
                        if ((currentNode.getDistanceFromSource() + distance(currentNode, neighborNode))
                                < neighborNode.getDistanceFromSource()) {
                            neighborNode.setDistanceFromSource(distance(currentNode, neighborNode) + currentNode.getDistanceFromSource());
                            }
                        }
                    }
                previous = currentNode;

                TimeUnit.MILLISECONDS.sleep(delayTimer);

                }


        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private double distance(Node node1, Node node2) {

        return Math.sqrt(Math.pow(Math.abs(node1.getX() - node2.getX()), 2) +
                Math.pow(Math.abs(node1.getY() - node2.getY()), 2));

    }

    private Edge minEdge(Vector<Edge> edges) {

        Comparator<Edge> lowWeight = new Comparator<Edge>() {
            @Override
            public int compare(Edge edge1, Edge edge2) {
                return (int) Math.abs(edge1.getWeight() - edge2.getWeight());
            }
        };

        return Collections.min(edges, lowWeight);
    }
}
