package student;

import game.Node;

public class Chebyshev extends Heuristic {
    @Override
    public int estimate(Node node, Node exitNode) {
        int currentColumn = node.getTile().getColumn();
        int currentRow = node.getTile().getRow();
        int exitColumn = exitNode.getTile().getColumn();
        int exitRow = exitNode.getTile().getRow();
        return Math.max(Math.abs(currentColumn - exitColumn), Math.abs(currentRow - exitRow));
    }
}