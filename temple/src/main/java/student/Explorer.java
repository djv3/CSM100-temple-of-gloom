package student;

import game.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;


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
    public void explore(ExplorationState state) throws IOException {
        //TODO : Explore the cavern and find the orb

        // add logging
        //Logger logger = LogHelper.getLogger("Explorer");

        long entreLocation = state.getCurrentLocation();
        int distanceToTarget = state.getDistanceToTarget();

        System.out.println("entreLocation=" + entreLocation);
        NodeStatus start = new NodeStatus(entreLocation,distanceToTarget);
        NodeA startA = new NodeA(0, start);
        PriorityQueue<NodeA> openQ = new PriorityQueue<>();
        Map<NodeA, NodeA> cameFrom = new HashMap<>();
        Map<NodeA,Integer> costSoFar = new HashMap<>();
        openQ.add(startA);
        cameFrom.put(startA,null);
        costSoFar.put(startA,0);

        while (!openQ.isEmpty()){
            NodeA current = openQ.poll();
            if(current.nodeStatus().nodeID() != entreLocation)
                state.moveTo(current.nodeStatus().nodeID());

            Collection<NodeStatus> neighbours = state.getNeighbours();

            for(NodeStatus n:neighbours){
                int newCost = costSoFar.get(current) + state.getDistanceToTarget();
            }
        }




        //PriorityQueue<start> closedList = new PriorityQueue<>();


        //PriorityQueue<NodeA> closedList = new PriorityQueue<>();
        //List <NodeA> closedList = new ArrayList<>();
        //PriorityQueue<NodeA> closedList
        //start.setF(0);
        //start.setNeighbors(state);

        //openList.add(start);


/*
        while(!openList.isEmpty()){

            NodeA nodeA = openList.peek();

            if(nodeA.getId() != entreLocation){
                System.out.println("open list="+ openList);
                System.out.println("move to node ID=" + nodeA.getId());

                state.moveTo(nodeA.getId());
            }
            nodeA.setNeighbors(state);
            //List<Long> neighbors = state.getNeighbours().stream()
            //System.out.println("stream="+ state.getNeighbours().stream());


            System.out.println("neighbours=" + nodeA.getNeighbors().stream().filter(neighbor -> !closedList.contains(neighbor.nodeID())));

            if(nodeA.getH() == 0)
                break;

            int num_neighbours = 0;
            for (NodeStatus n: nodeA.getNeighbors()){
                int gNode = nodeA.getG() + 1;
                NodeA nodeN = new NodeA(n);
                if (!openList.contains(nodeN) && !closedList.contains(nodeN)) {
                    nodeN.setParent(nodeA);
                    nodeN.setG(gNode);
                    nodeN.setF(nodeN.getG()+nodeN.getH());
                    openList.add(nodeN);
                }else {
                    if(gNode < nodeN.getG()){
                        nodeN.setParent(nodeA);
                        nodeN.setG(gNode);
                        nodeN.setF(nodeN.getG()+nodeN.getH());

                        if(closedList.contains(nodeN)){
                            closedList.remove(nodeN);
                            openList.add(nodeN);
                        }
                    }
                }
                num_neighbours++;
            }

            System.out.println("num_neigh=" +num_neighbours);

            openList.remove(nodeA);
            closedList.add(nodeA);
            System.out.println("open list =" + closedList);
            System.out.println("closed list =" + closedList);
        }


    }

    private  boolean is_new_in_Neigh(NodeA currentNodeA, NodeA newNodeA){
        for (NodeStatus n:currentNodeA.getNeighbors()){
            if(newNodeA.getId() == n.nodeID())
                return true;
        }
        return false;

 */
    }
    private boolean is_node_in_list(NodeStatus m, PriorityQueue<NodeA> list){
        for (NodeA nA: list) {
            if(m.nodeID() == nA.nodeStatus().nodeID())
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
        //TODO: Escape from the cavern before time runs out
    }
}
