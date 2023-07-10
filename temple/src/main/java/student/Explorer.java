package student;

import game.*;
import java.util.*;

import game.EscapeState;
import game.ExplorationState;
import game.Node;
import game.NodeStatus;

import java.util.Collection;

public class Explorer {

    /**
     * Explore the cavern, trying to find the orb in as few steps as possible.
     * Once you find the orb, you must return from the function in order to pick
     * it up. If you continue to move after finding the orb rather
     * than returning, it will not count.
     * If you return from this function while not standing on top of the orb,
     * it will count as a failure.
     * <p>
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the orb in fewer steps.
     * <p>
     * At every step, you only know your current tile's ID and the ID of all
     * open neighbor tiles, as well as the distance to the orb at each of these tiles
     * (ignoring walls and obstacles).
     * <p>
     * To get information about the current state, use functions
     * getCurrentLocation(),
     * getNeighbours(), and
     * getDistanceToTarget()
     * in ExplorationState.
     * You know you are standing on the orb when getDistanceToTarget() is 0.
     * <p>
     * Use function moveTo(long id) in ExplorationState to move to a neighboring
     * tile by its ID. Doing this will change state to reflect your new position.
     * <p>
     * A suggested first implementation that will always find the orb, but likely won't
     * receive a large bonus multiplier, is a depth-first search.
     *
     * @param state the information available at the current state
     */
    public void explore(ExplorationState state)  {

        NodeStatus entrePoint = new NodeStatus(state.getCurrentLocation(),state.getDistanceToTarget());
        NodeA start = new NodeA( entrePoint, state.getDistanceToTarget(), 0, null);

        PriorityQueue<NodeA> openSet = new PriorityQueue<>();
        openSet.add(start);
        PriorityQueue<NodeA> closedSet = new PriorityQueue<>();

        while (true){
            // A* - algorithm
            NodeA current = openSet.poll();
            openSet.clear();
            closedSet.add(current);

            if(current.nodeStatus().nodeID() != entrePoint.nodeID()){
                state.moveTo(current.nodeStatus().nodeID());
            }

            // reached the ord
            if(current.nodeStatus().distanceToTarget() == 0)
                break;

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

            NodeA nextMove = openSet.peek();

            // Trace back in case openSet is empty
            while (nextMove == null){
                NodeA backNode = current.parent();
                state.moveTo(backNode.nodeStatus().nodeID());

                List<NodeA> neighboursBackNode = getNeighborsA(state.getNeighbours(),backNode);

                for (NodeA m:neighboursBackNode) {
                    if(!is_node_in_list(m, closedSet)){
                        openSet.add(m);
                    }
                }

                nextMove = openSet.peek();
                current = backNode;
            }
        }
    }

    //TODO refactor ???
    private List<NodeA> getNeighborsA(Collection<NodeStatus> neighbours, NodeA current){
        List<NodeA> neighboursA = new ArrayList<>();
        for (NodeStatus n:neighbours) {
            int g = current.g() + 1;
            int f = g + n.distanceToTarget();
            neighboursA.add(new NodeA(n, f, g, current));
        }
        return  neighboursA;
    }
    
    //TODO refactor ???
    private boolean is_node_in_list(NodeA m, PriorityQueue<NodeA> list){
        for (NodeA nA: list) {
            if(m.nodeStatus().nodeID() == nA.nodeStatus().nodeID())
                return true;
        }
        return false;
    }

    /**
     * Escape from the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS escape before time runs
     * out, and this should be prioritized above collecting gold.
     * <p>
     * You now have access to the entire underlying graph, which can be accessed through EscapeState.
     * getCurrentNode() and getExit() will return you Node objects of interest, and getVertices()
     * will return a collection of all nodes on the graph.
     * <p>
     * Note that time is measured entirely in the number of steps taken, and for each step
     * the time remaining is decremented by the weight of the edge taken. You can use
     * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
     * on your current tile (this will fail if no such gold exists), and moveTo() to move
     * to a destination node adjacent to your current node.
     * <p>
     * You must return from this function while standing at the exit. Failing to do so before time
     * runs out or returning from the wrong location will be considered a failed run.
     * <p>
     * You will always have enough time to escape using the shortest path from the starting
     * position to the exit, although this will not collect much gold.
     *
     * @param state the information available at the current state
     */
    public void escape(EscapeState state) {
        int timeRemaining = state.getTimeRemaining();
        Set<Node> vertices = (Set<Node>) state.getVertices();

        Dijkstra dijkstra = new Dijkstra(vertices, timeRemaining);
        AStar astar = new AStar(vertices, timeRemaining);

        List<Node> dijkstraRoute = dijkstra.bestPath(state.getCurrentNode(), state.getExit());
        List<Node> aStarRoute = astar.bestPath(state.getCurrentNode(), state.getExit());
        List<Node> escapeRoute;

        if (EscapeAlgorithm.totalGoldOnPath(dijkstraRoute) > EscapeAlgorithm.totalGoldOnPath(aStarRoute)) {
            escapeRoute = dijkstraRoute;
        } else {
            escapeRoute = aStarRoute;
        }

        while (escapeRoute.size() > 0) {
            if (state.getCurrentNode().getTile().getGold() > 0) {
                state.pickUpGold();
            }
            state.moveTo(escapeRoute.remove(0));
        }
    }
}