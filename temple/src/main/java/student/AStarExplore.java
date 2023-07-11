package student;

import game.ExplorationState;
import game.Node;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Implements search logic of optimal path at Explore state  based on A-star algorithm
 * pls see theoretical foundation of algorithm https://en.wikipedia.org/wiki/A*_search_algorithm
 * The set of discovered nodes that may need to be (re-)expanded is implemented by priority que {@param openSet}
 * The set {@param closedSet} is used to store information about already visited nodes
 *
 */

public class AStarExplore extends ExploreAlgorithm{

    PriorityQueue<NodeA> openSet;
    PriorityQueue<NodeA> closedSet;

    /**
     * Constructor
     *  creates the first node(entry point based on the state)
     *  adds first node to {@param openSet} to start search
     */
    public AStarExplore(){
        closedSet = new PriorityQueue<>();
        openSet = new PriorityQueue<>();
        NodeA start = new NodeA( entrePoint, entrePoint.distanceToTarget(), 0, null);
        openSet.add(start);
    }

    /**
     * Polls the next node from set {@param openSet} with the best fScore value and assigns it as a current node {@param current}
     * Clears all others possible options in set {@param openSet}
     * Adds current node {@param current} to visited nodes set {@param closedSet}
     * @return current node {@param current} of the search
     * as a result we have received the best option to move
     */
    public NodeA getCurrentNode(){
        NodeA current = openSet.poll();
        openSet.clear();
        closedSet.add(current);
        return current;
    }

    /**
     * Searches of all neighbours and checks if the neighbour is a new option to move and to be added to {@param openSet}
     * If there are no any new option to move(no new neighbour added) we look for the best move(neighbour) based on
     * the minimal value of gScore
     * @param current = curren node on A-star search track
     * @return returns the new node to move
     */
    public NodeA getNextMove(ExplorationState state, NodeA current){

        List<NodeA> neighboursA = getNeighborsA(state.getNeighbours(),current);

        for (NodeA n:neighboursA){
            if(!is_node_in_list(n, openSet) && !is_node_in_list(n, closedSet)){
                // new unvisited neighbour found - add as a new option to move
                openSet.add(n);
            }else {
                int costPathToNode = current.g() + 1;
                if(costPathToNode < n.g() && is_node_in_list(n, closedSet)){
                    closedSet.remove(n);
                    openSet.add(n);
                }
            }
        }
        return openSet.peek();
    }

    /**
     * Search for the new unvisited nodes on traceback and puts it to set {@param openSet}
     * the search is based on getting all new possible neighbours of current node {@param backNode}
     * @param backNode = the current node on traceback
     * @return returns the best option to move from {@param openSet}
     */
    public NodeA getNextMoveTraceBack(ExplorationState state, NodeA backNode){
        // add neighbors in case they are not visited ones yet
        getNeighborsA(state.getNeighbours(),backNode).forEach(n -> {
            if (!is_node_in_list(n, closedSet)) openSet.add(n);
        });
        return openSet.peek();
    }

    /**
     * A method is never used for explore phase of the project
     * //TODO refactoring of abstract class Algorithm is required
     */

    @Override
    public List<Node> shortestPath(Node _startNode, Node _endNode) {
        return null;
    }

}
