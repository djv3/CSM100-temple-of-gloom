package student;

import game.Node;

import java.util.List;

/**
 * An abstract class representing an algorithm to calculate paths in a maze.
 */
public abstract class Algorithm {
    /**
     * A method to calculate the shortest path between 2 nodes.
     *
     * @param _startNode = the node from which to calculate the path
     * @param _endNode   = the node to which to calculate the path
     * @return           = the path to take from _startNode to _endNode
     */
    public abstract List<Node> shortestPath(Node _startNode, Node _endNode);
}
