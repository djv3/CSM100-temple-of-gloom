package student;

import game.Node;

import java.util.List;
import java.util.Set;

/**
 * An abstract class representing an algorithm to calculate paths in an escape maze (i.e. with full visibility and
 * gold present on some tiles).
 */
public abstract class EscapeAlgorithm extends Algorithm {
    Set<Node> graph;
    int timeRemaining;

    /**
     * A method to calculate the most profitable path between 2 nodes.
     *
     * @param _startNode = the node from which to calculate the path
     * @param _endNode   = the node to which to calculate the path
     * @return           = the path to take from _startNode to _endNode
     */
    public abstract List<Node> bestPath(Node _startNode, Node _endNode);

    /**
     * A method to calculate how much gold is present on a map. This assumes no movement has yet been made (as should
     * be the case when using an algorithm to determine the best route to take).
     *
     * @return = the total value of all gold present
     */
    public int totalGoldOnMap() {
        int totalGold = 0;

        for (Node n : graph) {
            totalGold += n.getTile().getGold();
        }

        return totalGold;
    }

    /**
     * A method to calculate how much gold is present on a given path (i.e. collection of Node objects).
     *
     * @param _path = the collection of Node objects whose gold should be summed
     * @return      = the amount of gold present on the path
     */
    public static int totalGoldOnPath(List<Node> _path) {
        int totalGold = 0;

        for (Node n : _path) {
            totalGold += n.getTile().getGold();
        }

        return totalGold;
    }

    /**
     * A method to calculate how much time it will take to traverse a given path (i.e. collection of contiguous Node
     * objects). This is calculated using the Edge lengths between each node in sequence.
     *
     * @param _startingPosition = the Node object from which the path departs
     * @param _path             = the collection of contiguous Node objects on the path
     * @return                  = the length of time in seconds the path will take to traverse
     */
    public static int timeTakenToTraversePath(Node _startingPosition, List<Node> _path) {
        int timeTaken = 0;

        timeTaken += _startingPosition.getEdge(_path.get(0)).length;

        for (int i = 0; i < _path.size() - 1; i++) {
            timeTaken += _path.get(i).getEdge(_path.get(i + 1)).length;
        }

        return timeTaken;
    }
}
