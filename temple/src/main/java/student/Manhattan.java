package student;

import game.Node;

/**
 * This class represents a heuristic used for weighting a node.
 * The heuristic is used in the A* algorithm.
 * The heuristic is used to estimate the distance between the current node and the exit node.
 */
public class Manhattan extends Heuristic {
    /**
     * Given a node and an exit node, this method returns the estimated distance between the
     * two nodes using Manhattan distance (<a href="https://en.wikipedia.org/wiki/Taxicab_geometry">...</a>).
     *
     * @param currentNode The current node.
     * @param exitNode    The exit node.
     * @return The estimated distance between the two nodes.
     */
    @Override
    public int estimate(Node currentNode, Node exitNode) {
        int currentColumn = currentNode.getTile().getColumn();
        int currentRow = currentNode.getTile().getRow();
        int exitColumn = exitNode.getTile().getColumn();
        int exitRow = exitNode.getTile().getRow();
        return Math.abs(currentColumn - exitColumn) + Math.abs(currentRow - exitRow);
    }
}
