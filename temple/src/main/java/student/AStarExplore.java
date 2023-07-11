package student;

import game.ExplorationState;
import game.Node;
import java.util.List;
import java.util.PriorityQueue;

public class AStarExplore extends ExploreAlgorithm{

    PriorityQueue<NodeA> openSet;
    PriorityQueue<NodeA> closedSet;


    public AStarExplore(){
        NodeA start = new NodeA( entrePoint, entrePoint.distanceToTarget(), 0, null);
        closedSet = new PriorityQueue<>();
        openSet = new PriorityQueue<>();
        openSet.add(start);
    }

    public NodeA getCurrentNode(){

        NodeA current = openSet.poll();
        openSet.clear();
        closedSet.add(current);
        return current;
    }

    public NodeA getNextMove(ExplorationState state, NodeA current){

        List<NodeA> neighboursA = getNeighborsA(state.getNeighbours(),current);

        for (NodeA m:neighboursA){
            if(!is_node_in_list(m, openSet) && !is_node_in_list(m, closedSet)){
                openSet.add(m);
            }else {
                int costPathToNode = current.g() + 1;
                if(costPathToNode < m.g() && is_node_in_list(m, closedSet)){
                    closedSet.remove(m);
                    openSet.add(m);
                }
            }
        }
        return openSet.peek();
    }


    public NodeA getNextMoveTraceBack(ExplorationState state, NodeA backNode){
        getNeighborsA(state.getNeighbours(),backNode).forEach(n -> {
            if (!is_node_in_list(n, closedSet)) openSet.add(n);
        });
        return openSet.peek();
    }

    /**
     * A method to calculate the shortest path between 2 nodes.
     *
     * @param _startNode = the node from which to calculate the path
     * @param _endNode   = the node to which to calculate the path
     * @return = the path to take from _startNode to _endNode
     */

    @Override
    public List<Node> shortestPath(Node _startNode, Node _endNode) {
        return null;
    }

}
