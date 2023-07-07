package student;

import game.*;
import lombok.Data;

import java.io.IOException;
import java.util.*;

public class Explorer {

    public static ArrayList<Node> aStar(EscapeState state, Heuristic heuristic) {
        // a star formula for a node's weight is f = g + h
        // g is the distance from the start node to the current node
        // h is the heuristic - the estimated distance from the current node to the exit node
        HashMap<Node, Integer> open = new HashMap<>(); // k, v of node, g (distance from start)
        HashMap<Node, Integer> closed = new HashMap<>();
        HashMap<Node, Node> parent = new HashMap<>(); // k, v of node, parent node
        ArrayList<Node> path = new ArrayList<>();
        Node start = state.getCurrentNode();
        Node exit = state.getExit();
        open.put(start, 0);
        while (!open.isEmpty()) {
            Map.Entry<Node, Integer> current = open.entrySet()
                    .stream()
                    .min(Comparator.comparingInt(e -> e.getValue() + heuristic.estimate(e.getKey(), exit)))
                    .orElseThrow();

            Node currentNode = current.getKey();
            int currentG = current.getValue();

            Set<Node> neighbours = currentNode.getNeighbours();

            for (Node neighbour : neighbours) {
                if (neighbour.equals(exit)) {
                    Node node = currentNode;
                    while (node != null) {
                        path.add(0, node); // walk backwards from exit to start
                        node = parent.get(node);
                    }
                    path.add(exit);
                    return path;
                }
                if (closed.containsKey(neighbour)) {
                    continue;
                }
                int neighbourG = currentG + currentNode.getEdge(neighbour).length;
                if (open.containsKey(neighbour)) {
                    if (neighbourG < open.get(neighbour)) {
                        open.put(neighbour, neighbourG);
                        parent.put(neighbour, currentNode);
                    }
                } else {
                    open.put(neighbour, neighbourG);
                    parent.put(neighbour, currentNode);
                }
            }

            open.remove(currentNode);
            closed.put(currentNode, currentG);
            }
        return path;
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
        GreedyHeuristic g = new GreedyHeuristic();
        ArrayList<Node> path = aStar(state, g);
        path.remove(0);
        for (Node n : path) {
            System.out.println("Moving from " + state.getCurrentNode() + " to " + n);
            state.moveTo(n);
            if (state.getCurrentNode().getTile().getGold() > 0) {
                state.pickUpGold();
            }
        }
    }
}
