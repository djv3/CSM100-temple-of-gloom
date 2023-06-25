package student;

import game.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


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

        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT]  %5$s %n");
        Logger logger = Logger.getLogger(Explorer.class.getName());
        FileHandler fh = new FileHandler("C:\\Users\\an78d\\az\\projects\\uol\\scm100sdp\\coursework\\CSM100-temple-of-gloom\\log.txt");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);


        //TODO : Explore the cavern and find the orb

        long entreLocation = state.getCurrentLocation();
        int distanceToTarget = state.getDistanceToTarget();
        Collection<NodeStatus> neighbours = state.getNeighbours();

        NodeStatus current = new NodeStatus(entreLocation,distanceToTarget);
        Stack<NodeStatus> nodeStack = new Stack<>();
        List<NodeStatus> visitedNodes = new ArrayList<>();
        visitedNodes.add(current);
        nodeStack.push(current);

        while (true){
            // TODO del
            //System.out.println("CURR_POS =" + state.getCurrentLocation());
            //System.out.println("Current before POP:" + current);
            //System.out.println("Stack before POP" + nodeStack);

            current = nodeStack.pop();

            // TODO del
           //System.out.println("Stack after" + nodeStack);
            //System.out.println("Current after POP:" + current);
            //System.out.println("Stack after POP" + nodeStack);

            if(neighbours.contains(current) || entreLocation == current.nodeID()){

                if(current.nodeID() != entreLocation){
                    state.moveTo(current.nodeID());
                    neighbours = state.getNeighbours();

                    if(current.distanceToTarget() == 0)
                        break;

                    if(!visitedNodes.contains(current)){
                        visitedNodes.add(current);
                    }
                }

                for (NodeStatus neighbour : neighbours) {
                    if (!visitedNodes.contains(neighbour)) {
                        nodeStack.push(neighbour);
                    }
                }
            }

            // comeback
            if(nodeStack.isEmpty()){

                boolean stackEmpty = true;

                // go to visited back nodes step by step
                for (int i = visitedNodes.size() - 2; i>=0;i--){

                    // check if the node we move is on enter node
                    if(visitedNodes.get(i).nodeID() == entreLocation){
                        logger.info("ZERO");
                        // TODO del
                        logger.info("i=" + i);
                        logger.info("CURR=" + state.getCurrentLocation());
                        //System.out.println("N TO MOVE="+state.getNeighbours());
                        logger.info("N2 TO MOVE="+neighbours);
                        logger.info("New Node=" + visitedNodes.get(i));
                        //neighbours.remove(visitedNodes.get(i));
                        //System.out.println("N TO MOVE="+state.getNeighbours());
                        logger.info("N2 TO MOVE="+neighbours);
                        //for(NodeStatus nodeN:neighbours){
                            //nodeStack.push(nodeN);
                        //}
                        //state.moveTo(nodeStack.pop().nodeID());
                        //neighbours = state.getNeighbours();
                        System.out.println("stack:" + nodeStack);
                    }

                    // if the node is not on enter
                    if(neighbours.contains(visitedNodes.get(i))){
                        state.moveTo(visitedNodes.get(i).nodeID());
                        neighbours = state.getNeighbours();
                    }
                    // TODO del
                    logger.info("stack:" + nodeStack);

                    for (NodeStatus node:neighbours) {
                        // TODO del
                        logger.info("neighNoce=" + node);
                        logger.info("visitedNodes.get(i).nodeID()=" + visitedNodes.get(i).nodeID());
                        logger.info("entreLocation=" + entreLocation);

                        if(!visitedNodes.contains(node)){
                            System.out.println("ADDED TO STACK");
                            nodeStack.add(node);
                            stackEmpty = false;
                            break;
                        }

                        /*
                        if(visitedNodes.get(i).nodeID() == entreLocation){
                            // TODO del
                            System.out.println("NODE="+visitedNodes.get(i));
                            System.out.println("ENTER_NODE="+entreLocation);
                            System.out.println("*****¨VISITED******");
                            System.out.print(visitedNodes);


                            for (NodeStatus nodeVisited:visitedNodes) {

                                if (nodeVisited.nodeID() != visitedNodes.get(i).nodeID()){
                                    // TODO del
                                    System.out.println("nodeVisited.nodeID()="+nodeVisited.nodeID());

                                    state.moveTo(nodeVisited.nodeID());
                                    neighbours = state.getNeighbours();
                                    for (NodeStatus neigbor: neighbours) {
                                        if(!visitedNodes.contains(neigbor)){
                                            nodeStack.add(node);
                                            stackEmpty = false;
                                            break;
                                        }
                                    }
                                    if (!stackEmpty)
                                        break;
                                }

                                //nodeVisited.
                            }
                            //TODO del
                            System.out.println(" ");

                            //nodeStack.add(node);
                            //stackEmpty = false;
                            //break;
                        }
*/
                    }
                    // TODO del
                    logger.info("STACK="+nodeStack);

                    if (!stackEmpty)
                        break;
                }



            }
        }
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
