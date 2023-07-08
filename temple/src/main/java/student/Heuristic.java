package student;

import game.Node;

/**
 * This class represents a heuristic used for weighting a node.
 * The heuristic is used in the A* algorithm.
 * The heuristic is used to estimate the distance between the current node and the exit node.
 */
public abstract class Heuristic {
    /**
     * Given a node and an exit node, this method returns the estimated distance between the two nodes.
     *
     * @param currentNode The current node.
     * @param exitNode    The exit node.
     * @return The estimated distance between the two nodes.
     */
    public abstract int estimate(Node currentNode, Node exitNode);
}
