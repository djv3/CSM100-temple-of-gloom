package student;

import game.Node;

public class Euclidean extends Heuristic {
    /**
     * Given a node and an exit node, this method returns the estimated distance between the
     * two nodes using Euclidean distance (<a href="https://en.wikipedia.org/wiki/Euclidean_distance">...</a>).
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
        return (int) Math.sqrt(Math.pow(currentColumn - exitColumn, 2) + Math.pow(currentRow - exitRow, 2));
    }
}
