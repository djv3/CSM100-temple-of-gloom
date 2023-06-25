package student;

import game.EscapeState;
import game.ExplorationState;
import game.Node;
import lombok.Data;

import java.util.*;

@Data
class Distance {
    private int g;
    private int h;

    public Distance(int g, int h) {
        this.g = g;
        this.h = h;
    }
    public static Distance of(int g, int h) {
        return new Distance(g, h);
    }

    public int getF() {
        return this.g + this.h;
    }
}

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
        // We can use different heuristics to greedily capture as much gold as possible!
        Manhattan m = new Manhattan();
        Collection<Node> path = aStar(state, m);

        // currently just finding the shortest path and collecting any gold along the way.
        for (Node n : path) {
            state.moveTo(n);
            if (state.getCurrentNode().getTile().getGold() > 0) {
                state.pickUpGold();
            }
        }
    }


    public static Collection<Node> aStar(EscapeState state, Heuristic heuristic) {
        // a star formula for a node's weight is f = g + h
        // g is the distance from the start node to the current node
        // h is the heuristic - the estimated distance from the current node to the exit node

        // add start node to open list
        // loop on the following:
        // get lowest f value node from open list and add to closed list
        // for each neighbour of the current node
        // if neighbour is in closed list, continue
        // if neighbour is not in open list, add to open list with a g value of current node g + 1
        // else if neighbour is in open list, check if current node g + 1 is less than neighbour g
        // stop when exit node is added to closed list or if open list is empty
        int size = state.getVertices().size();
        LinkedHashMap<Node, Distance> openList = LinkedHashMap.newLinkedHashMap(size);
        LinkedList<Node> closedList = new LinkedList<>();

        // add start node to open list
        Node startNode = state.getCurrentNode();
        Distance d = Distance.of(0, heuristic.getHeuristic(startNode, state.getExit()));
        openList.put(startNode, d);
        // While the open list is not empty
        while (!openList.isEmpty()) {
            // get the node with the lowest f value from the open list
            Map.Entry<Node, Distance> currentNodeEntry = openList.entrySet().stream()
                    .min(Comparator.comparingInt(e -> e.getValue().getF()))
                    .get();

            Node currentNode = currentNodeEntry.getKey();
            Distance currentDistance = currentNodeEntry.getValue();

            openList.remove(currentNode);
            closedList.add(currentNode);

            if (currentNode.equals(state.getExit())) {
                break;
            }

            Collection<Node> neighbours = currentNode.getNeighbours();

            for (Node neighbour : neighbours) {
                if (closedList.contains(neighbour)) {
                    continue;
                }
                // add the neighbour to the open list if it is not already in the open list and it is traversable
                if (neighbour.getTile().getType().isOpen() && !openList.containsKey(neighbour)) {
                    int g = currentDistance.getG() + 1;
                    int h = heuristic.getHeuristic(neighbour, state.getExit());
                    openList.put(neighbour, Distance.of(g, h));
                } else if (openList.containsKey(neighbour)) {
                    // if the neighbour is already in the open list, check if the current node g + 1 is less than the neighbour g
                    int currentG = currentDistance.getG() + 1;
                    int existingG = openList.get(neighbour).getG();
                    if (currentG < existingG) {
                        openList.put(
                                neighbour,
                                Distance.of(
                                        currentG,
                                        heuristic.getHeuristic(neighbour, state.getExit()
                                        )
                                )
                        );
                    }
                }

            }
        }
        ArrayList<Node> path = new ArrayList<>(closedList.size());
        closedList.forEach(node -> path.add(0, node));
        return path;
    }
}
