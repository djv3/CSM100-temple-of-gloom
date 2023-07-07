package student;

import game.Cavern;
import game.Node;

public class GreedyHeuristic extends Heuristic {
    @Override
    public int estimate(Node node, Node exitNode) {
        // calculate manhattan distance between current node and exit node
        int currentColumn = node.getTile().getColumn();
        int currentRow = node.getTile().getRow();
        int exitColumn = node.getTile().getColumn();
        int exitRow = node.getTile().getRow();
        int currentGold = node.getTile().getGold();
        int manhattanDistance = Math.abs(currentColumn - exitColumn) + Math.abs(currentRow - exitRow);

        return manhattanDistance + Cavern.MAX_GOLD_VALUE - currentGold;
    }
}
