package student;

import game.*;
import lombok.Data;

import java.io.IOException;
import java.util.*;

public class Explorer {

    public static Collection<NodeContainer> aStar(EscapeState state, Heuristic heuristic) {
        // a star formula for a node's weight is f = g + h
        // g is the distance from the start node to the current node
        // h is the heuristic - the estimated distance from the current node to the exit node
        Queue<NodeContainer> openSet = new PriorityQueue<>();
        Queue<NodeContainer> closedSet = new PriorityQueue<>();
        Node startingNode = state.getCurrentNode();
        Node exitNode = state.getExit();
        NodeContainer start = new NodeContainer(startingNode, null, 0, heuristic.estimate(startingNode, state.getExit()));
        openSet.add(start);
        System.out.println("Starting NodeContainer: " + start);

        while (!openSet.isEmpty()) {
            NodeContainer current = openSet.poll();
            closedSet.add(current);

            if (current.getNode().equals(state.getExit())) {
                closedSet.forEach(nc -> System.out.println(nc.getF()));
                System.out.println("Found exit node!");
                return closedSet;
            }

            // Now we check each neighbour's value
            for (Edge e : current.getNode().getExits()) {
                Node neighbour = e.getOther(current.getNode());
                NodeContainer neighbourContainer = new NodeContainer(
                        neighbour,
                        current.getNode(),
                        current.getG() + e.length(),
                        heuristic.estimate(neighbour, state.getExit())
                );
                if (closedSet.contains(neighbourContainer)) {
                    continue;
                }
//                if (neighbour.equals(exitNode)) {
//                    System.out.println("Found exit node!");
//                    neighbourContainer.setParent(current.getNode());
//                    closedSet.add(neighbourContainer);
//                    return closedSet;
//                }

                if (!openSet.contains(neighbourContainer)) {
                    openSet.add(neighbourContainer);
                } else {
                    for (NodeContainer n : openSet) {
                        if (n.equals(neighbourContainer) && n.getG() > neighbourContainer.getG()) {
                            openSet.remove(n);
                            openSet.add(neighbourContainer);
                            break;
                        }
                    }
                }
            }
        }
        // Now we need to turn the closedPath into a list of nodes that the explorer can follow
        System.out.println("Closed set size: " + closedSet.size());
        closedSet.forEach(nc -> System.out.println(nc.getF()));
        return closedSet;
    }

    /**
     * Explore the cavern, trying to find the orb in as few steps as possible.
     * Once you find the orb, you must return from the function in order to pick
     * it up. If you continue to move after finding the orb rather
     * than returning, it will not count.
     * If you return from this function while not standing on top of the orb,
     * it will count as a failure.
     * <p>
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the orb in fewer steps.
     * <p>
     * At every step, you only know your current tile's ID and the ID of all
     * open neighbor tiles, as well as the distance to the orb at each of these tiles
     * (ignoring walls and obstacles).
     * <p>
     * To get information about the current state, use functions
     * getCurrentLocation(),
     * getNeighbours(), and
     * getDistanceToTarget()
     * in ExplorationState.
     * You know you are standing on the orb when getDistanceToTarget() is 0.
     * <p>
     * Use function moveTo(long id) in ExplorationState to move to a neighboring
     * tile by its ID. Doing this will change state to reflect your new position.
     * <p>
     * A suggested first implementation that will always find the orb, but likely won't
     * receive a large bonus multiplier, is a depth-first search.
     *
     * @param state the information available at the current state
     */
    public void explore(ExplorationState state) throws IOException {
        //TODO : Explore the cavern and find the orb

        // add logging
        //Logger logger = LogHelper.getLogger("Explorer");

        long entreLocation = state.getCurrentLocation();
        int distanceToTarget = state.getDistanceToTarget();
        Collection<NodeStatus> neighbours = state.getNeighbours();

        NodeStatus current = new NodeStatus(entreLocation, distanceToTarget);
        Stack<NodeStatus> nodeStack = new Stack<>();
        List<NodeStatus> visitedNodes = new ArrayList<>();
        List<NodeStatus> trackNodes = new ArrayList<>();
        visitedNodes.add(current);
        nodeStack.push(current);

        while (true) {
            current = nodeStack.pop();

            // dfs search
            if (neighbours.contains(current) || entreLocation == current.nodeID()) {

                if (current.nodeID() != entreLocation) {
                    state.moveTo(current.nodeID());
                    trackNodes.add(current);
                    neighbours = state.getNeighbours();

                    if (current.distanceToTarget() == 0)
                        break;

                    if (!visitedNodes.contains(current))
                        visitedNodes.add(current);

                }
                for (NodeStatus neighbour : neighbours) {
                    if (!visitedNodes.contains(neighbour))
                        nodeStack.push(neighbour);
                }
            }

            // comeback in case stack empty
            if (nodeStack.isEmpty()) {

                boolean stackEmpty = true;

                // come back to the visited nodes step by step
                for (int i = trackNodes.size() - 2; i >= 0; i--) {

                    // check if the node we move is on enter node
                    if (trackNodes.get(i).nodeID() == entreLocation) {

                        for (NodeStatus visitedN : trackNodes) {
                            state.moveTo(visitedN.nodeID());
                            neighbours = state.getNeighbours();
                            for (NodeStatus nodeN : neighbours) {
                                if (!visitedNodes.contains(nodeN)) {
                                    nodeStack.push(nodeN);
                                    stackEmpty = false;
                                    break;
                                }
                            }
                            if (!stackEmpty)
                                break;
                        }
                    } else if (neighbours.contains(trackNodes.get(i))) {// if the node is not on enter
                        state.moveTo(trackNodes.get(i).nodeID());
                        neighbours = state.getNeighbours();
                        for (NodeStatus node : neighbours) {
                            if (!visitedNodes.contains(node)) {
                                nodeStack.add(node);
                                stackEmpty = false;
                                break;
                            }
                        }
                    }
                    if (!stackEmpty)
                        break;
                }
            }
        }
    }

    /**
     * Escape from the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS escape before time runs
     * out, and this should be prioritized above collecting gold.
     * <p>
     * You now have access to the entire underlying graph, which can be accessed through EscapeState.
     * getCurrentNode() and getExit() will return you Node objects of interest, and getVertices()
     * will return a collection of all nodes on the graph.
     * <p>
     * Note that time is measured entirely in the number of steps taken, and for each step
     * the time remaining is decremented by the weight of the edge taken. You can use
     * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
     * on your current tile (this will fail if no such gold exists), and moveTo() to move
     * to a destination node adjacent to your current node.
     * <p>
     * You must return from this function while standing at the exit. Failing to do so before time
     * runs out or returning from the wrong location will be considered a failed run.
     * <p>
     * You will always have enough time to escape using the shortest path from the starting
     * position to the exit, although this will not collect much gold.
     *
     * @param state the information available at the current state
     */
    public void escape(EscapeState state) {
        // We can use different heuristics to greedily capture as much gold as possible!
//        Manhattan m = new Manhattan();
        Euclidean e = new Euclidean();
        aStar(state, e);
        // currently just finding the shortest path and collecting any gold along the way.
//        for (Node n : path) {
//            System.out.println("Moving to " + n.getId());
//            System.out.println(n.getId());
//            state.moveTo(n);
//            if (state.getCurrentNode().getTile().getGold() > 0) {
//                state.pickUpGold();
//            }
//        }
    }
}

@Data
class NodeContainer implements Comparable<NodeContainer> {
    private Node node;
    private Node parent;
    private int g;
    private int h;

    private int f;

    public NodeContainer(Node n, Node p, int g, int h) {
        this.node = n;
        this.parent = p;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    @Override
    public int compareTo(NodeContainer o) {
        return Integer.compare(this.f, o.f);
    }
}