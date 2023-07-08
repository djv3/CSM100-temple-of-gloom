package student;

import game.EscapeState;
import game.Node;

import java.util.List;

public abstract class EscapeAlgorithm extends Algorithm {
    EscapeState escapeState;

    public abstract List<Node> bestPath(Node _startNode, Node _endNode);

    public int totalGoldOnMap() {
        int totalGold = 0;

        for (Node n : escapeState.getVertices()) {
            totalGold += n.getTile().getGold();
        }

        return totalGold;
    }

    public int totalGoldOnPath(List<Node> _path) {
        int totalGold = 0;

        for (Node n : _path) {
            totalGold += n.getTile().getGold();
        }

        return totalGold;
    }

    public static int timeTakenToTraversePath(Node _startingPosition, List<Node> _path) {
        int timeTaken = 0;

        timeTaken += _startingPosition.getEdge(_path.get(0)).length;

        for (int i = 0; i < _path.size() - 1; i++) {
            timeTaken += _path.get(i).getEdge(_path.get(i + 1)).length;
        }

        return timeTaken;
    }
}
