package Pathing;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;

/**
 * Created by robert on 4/3/2015.
 */
public class PathingScreen extends JPanel {
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

    private Vector<Node> nodesOnScreen = new Vector<Node>(5);
    private Vector<Edge> edgesOnScreen = new Vector<Edge>(5);
    private Random r = new Random();



    public static void main(String[] args) {
        JFrame frame = new JFrame("bank_gui");
        frame.setContentPane(new PathingScreen().outerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }


    public PathingScreen() {

        displayPanel.setVisible(true);
        this.drawNodesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRandomNodes(displayPanel.getX() + displayPanel.getWidth(),
                        displayPanel.getY() + displayPanel.getHeight(), displayPanel.getX(), displayPanel.getY(),
                        Integer.parseInt(nodeNumber.getText()));

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
                        displayPanel.getX()+displayPanel.getWidth(), displayPanel.getY()+displayPanel.getHeight());
                nodesOnScreen.removeAllElements();
                edgesOnScreen.removeAllElements();
            }
        });

        this.resizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                Color background = new Color(60,63,65);
                Graphics2D graphics2D = (Graphics2D)displayPanel.getGraphics();
                graphics2D.setColor(background);
                paintEdgesToScreen(graphics2D);
                paintNodesToScreen(graphics2D);
                nodesOnScreen.removeAllElements();
                edgesOnScreen.removeAllElements();
                repaint();
            }
        });




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
     *                 ToDo: add graphical representation for the nodes.
     */
    public void addRandomNodes(int xHBounds, int yHBounds, int xLBounds, int yLBounds, int amount) {
        Node temp = new Node(1, 1);
        for (int i = 0; i < amount; i++) {
            this.nodesOnScreen.add(temp.getRandomNode(xHBounds, yHBounds, xLBounds, yLBounds));
        }
    }

    public void paintNodesToScreen(Graphics2D g2) {

        for (Node n : nodesOnScreen) {
            g2.fillOval(n.getX(), n.getY(), Integer.parseInt(nodeRadius.getText()), Integer.parseInt(nodeRadius.getText()));
        }
        repaint();
    }

    public void paintEdgesToScreen(Graphics2D g2) {
        for (Edge e : edgesOnScreen) {
            g2.drawLine(e.getFrontNode().getX()+(Integer.parseInt(nodeRadius.getText())/2),
                    e.getFrontNode().getY()+(Integer.parseInt(nodeRadius.getText())/2), e.getBackNode().getX(),
                    e.getBackNode().getY());
        }
        repaint();
    }

    /**
     * adds one to 3 edges to each node currently on the screen. The edge connected to the currently selected node is
     * chosen at random from the current nodes on screen vector by retrieving the node at a randomly generated index
     * between the beginning and the last index containing a node.
     * <p>
     * ToDo: add graphic representation for the edges.
     */
    public void addRandomEdges() {
        try {
            for (Node node : nodesOnScreen) {

                for (int i = 0; i < r.nextInt(2) + 1; i++) {
                    edgesOnScreen.add(new Edge(node, nodesOnScreen.elementAt(r.nextInt(nodesOnScreen.size()))));
                }
            }
        } catch (NullPointerException p) {
            System.out.printf("Null pointer caught in Screen : addRandomEdges.\nLikely attempting to create a new edge with" +
                    " a node which does not exist.");
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
