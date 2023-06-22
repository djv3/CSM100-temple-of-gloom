package student;

import game.*;

import java.util.*;

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
    public void explore(ExplorationState state) {
        //TODO : Explore the cavern and find the orb

        long currentLocation = state.getCurrentLocation();
        int distanceToTarget = state.getDistanceToTarget();
        Collection<NodeStatus> neighbours = state.getNeighbours();
        System.out.println("enter_location=" + currentLocation +
                "  distanceToTarget=" + distanceToTarget + " neigbours:" + neighbours);
        NodeStatus current = new NodeStatus(currentLocation,distanceToTarget);

        Stack<NodeStatus> nodeStack = new Stack<>();
        List<NodeStatus> visitedNodes = new ArrayList<>();
        nodeStack.push(current);

        while (!nodeStack.isEmpty()){
            current = nodeStack.pop();
            if(!visitedNodes.contains(current)){
                // skip moveTo() if this is an enter position
                if(current.nodeID() != currentLocation){
                    System.out.println(nodeStack);
                    state.moveTo(current.nodeID());
                    if(current.distanceToTarget() == 0)
                        break;
                }
                visitedNodes.add(current);
                int addedNewNeighbours = 0;
                for (NodeStatus nodeNeighbour:  state.getNeighbours()) {
                    if(!visitedNodes.contains(nodeNeighbour)){
                        nodeStack.push(nodeNeighbour);
                        addedNewNeighbours++;
                    }
                }
                System.out.println("addedNewNeighbours=" + addedNewNeighbours);
                // cycle graph need to go back
                if(addedNewNeighbours == 0){

                    Collections.reverse(visitedNodes);
                    visitedNodes.remove(0);
                    System.out.print("visited:"+visitedNodes);
                    for(NodeStatus node: visitedNodes){
                        System.out.println("node to step back" + node);
                        state.moveTo(node.nodeID());
                        if(state.getNeighbours().contains(nodeStack.peek())){
                            //state.moveTo(node.nodeID());
                            Collections.reverse(visitedNodes);
                            break;
                        }
                    }
                    //Collections.reverse(visitedNodesReversed);

                }
            }
        }
    }


/*
    private NodeStatus minimizeWeighAlgorithm(Collection<NodeStatus> neighbours, int distanceToTarget){
        NodeStatus newTileToMove;

        List<NodeStatus> optionsToMove = new ArrayList<>();
        for (NodeStatus node:neighboursN)
            if (node.distanceToTarget() < distanceToTarget)
                optionsToMove.add(node);
        newTileToMove = optionsToMove.get(0);

        for (NodeStatus node:optionsToMove)
            if(newTileToMove.distanceToTarget() > node.distanceToTarget())
                newTileToMove = node;

        return newTileToMove;
    }


 */
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
        //TODO: Escape from the cavern before time runs out
    }
}
