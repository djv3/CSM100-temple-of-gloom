package student;

import game.Cavern;
import game.EscapeState;
import game.Node;

import java.util.*;

public class AStar extends EscapeAlgorithm {
    public AStar(EscapeState _escapeState) {
        escapeState = _escapeState;
    }

    /**
     * Uses the A* algorithm to build a path to a node, prioritising nodes with gold.
     * Uses the shortestPath method to find the best path between important nodes.
     *
     * @param start the node to start from
     * @param exit  the node to end at
     * @return a list of nodes that make up the best path
     */
    @Override
    public List<Node> bestPath(Node start, Node exit) {
        int timeRemaining = escapeState.getTimeRemaining();
        System.out.println("Time remaining: " + timeRemaining);
        TreeSet<Node> nearestNodesWithGold = new TreeSet<>(Comparator.comparingInt(o -> estimate(o, start)));
        TreeSet<Node> nodesWithGold = new TreeSet<>(Comparator.comparingInt(o -> o.getTile().getGold()));
        escapeState.getVertices().forEach(n -> {
            if (n.getTile().getGold() > 0) {
                nodesWithGold.add(n);
            }
        });

        // Get the top 10 nodes with the most gold and add them to the nearestNodesWithGold set.
        for (int i = 0; i < 15; i++) {
            Node node = nodesWithGold.pollLast();
            if (node != null) {
                nearestNodesWithGold.add(node);
            }
        }

        List<Node> path = new ArrayList<>();

        path.add(start);

        for (Node node : nearestNodesWithGold) {
            Node lastNode = path.get(path.size() - 1);

            if (node.equals(exit)) {
                path.addAll(shortestPath(lastNode, node));
                break;
            }

            List<Node> pathToNextNode = shortestPath(lastNode, node);

            if (pathToNextNode.size() == 1 || pathToNextNode.size() == 0) {
                continue;
            }
            Node lastNodeInNextMove = pathToNextNode.get(pathToNextNode.size() - 1);

            List<Node> pathToExit = shortestPath(lastNodeInNextMove, exit);

            List<Node> escapePath = shortestPath(lastNode, exit);

            // if the time remaining is greater than the current path, plus the path to the next node, plus the path to the exit from that node, we are safe to add to the current path.
            if (timeRemaining > pathLength(path) + pathLength(pathToNextNode) + pathLength(pathToExit) + pathLength(escapePath)) {
                path.addAll(pathToNextNode);
            } else {
                path.addAll(escapePath);
                System.out.println("Escaping!, path length: " + pathLength(path));
                break;
            }
        }
        path.remove(0);
        return path;
    }

    /**
     * Calculates the length of a path.
     * @param path A list of nodes representing a path to another node.
     * @return The length of the path.
     */
    public int pathLength(List<Node> path) {
        if (path.size() == 1) {
            return 0;
        }
        int pathLength = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            pathLength += path.get(i).getEdge(path.get(i + 1)).length;
        }
        return pathLength;
    }

    /**
     * Estimates the distance between a node and another node.
     * The estimate is the Manhattan distance between the two nodes, weighted by the amount of gold in the current node.
     *
     * @param node the current node
     * @param other the other node
     * @return the estimated distance between the two nodes
     */
    public int estimate(Node node, Node other) {
        int currentColumn = node.getTile().getColumn();
        int currentRow = node.getTile().getRow();
        int exitColumn = other.getTile().getColumn();
        int exitRow = other.getTile().getRow();
        int manhattanDistance = Math.abs(currentColumn - exitColumn) + Math.abs(currentRow - exitRow);
        return manhattanDistance + Cavern.MAX_GOLD_VALUE - node.getTile().getGold();
    }

    /**
     * Finds the shortest path between two nodes using the A* algorithm.
     *
     * @param start the start node
     * @param exit the exit node
     * @return the shortest path between the two nodes
     */
    @Override
    public List<Node> shortestPath(Node start, Node exit) {
        HashMap<Node, Integer> open = new HashMap<>();
        HashMap<Node, Integer> closed = new HashMap<>();
        HashMap<Node, Node> parent = new HashMap<>();
        ArrayList<Node> path = new ArrayList<>();
        open.put(start, 0);

        while (!open.isEmpty()) {
            Node current = open.entrySet().stream().min(Comparator.comparingInt(Map.Entry::getValue)).get().getKey();
            open.remove(current);
            closed.put(current, estimate(current, exit));

            if (current.equals(exit)) {
                while (current != null) {
                    path.add(current);
                    current = parent.get(current);
                }
                Collections.reverse(path);
                path.remove(0);
                return path;
            }

            for (Node n : current.getNeighbours()) {
                if (!closed.containsKey(n)) {
                    int newDistance = closed.get(current) + current.getEdge(n).length;
                    if (!open.containsKey(n) || open.get(n) > newDistance) {
                        open.put(n, newDistance + estimate(n, exit));
                        parent.put(n, current);
                    }
                }
            }
        }
        return path;
    }
}
