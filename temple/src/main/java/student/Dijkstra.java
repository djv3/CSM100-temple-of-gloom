package student;

import game.Node;

import java.util.*;

/**
 * A maze escape algorithm using Dijkstra's algorithm to calculate the shortest path between 2 nodes.
 */
public class Dijkstra extends EscapeAlgorithm {

    /**
     * Constructor that takes the current game state (which should be post-orb-retrieval, pre-escape movement).
     *
     * @param _graph = the set of nodes comprising the map
     * @param _timeRemaining = the time in seconds that has been granted to escape the map
     */
    public Dijkstra(Set<Node> _graph, int _timeRemaining) {
        graph = _graph;
        timeRemaining = _timeRemaining;
    }

    @Override
    public List<Node> bestPath(Node _startNode, Node _endNode) {
        List<Node> detourAlgorithm = pathWithDetours(_startNode, _endNode);
        List<Node> wanderingAlgorithm = wanderingPath(_startNode, _endNode);

        if (totalGoldOnPath(detourAlgorithm) > totalGoldOnPath(wanderingAlgorithm)) {
            return detourAlgorithm;
        } else {
            return wanderingAlgorithm;
        }
    }

    @Override
    public List<Node> shortestPath(Node _startNode, Node _endNode) {
        // 2 maps, one for nodes that have been visited and one for nodes that haven't
        HashMap<Node, Integer> unvisitedNodes = new HashMap<>();
        HashMap<Node, Integer> visitedNodes = new HashMap<>();

        // Store all nodes in the first map
        for (Node n : graph) {
            unvisitedNodes.put(n, Integer.MAX_VALUE);
        }
        // Special case for the starting node, as we know the distance is 0
        unvisitedNodes.put(_startNode, 0);

        // Main algorithm loop
        boolean pathFound = false;
        while (!pathFound) {
            // Find a random non-visited node with the lowest tentative distance ('lowest node')
            Map.Entry<Node, Integer> minEntry = null;
            for (Map.Entry<Node, Integer> entry : unvisitedNodes.entrySet()) {
                if (minEntry == null || minEntry.getValue() > entry.getValue()) {
                    minEntry = entry;
                }
            }

            // Loop through lowest node's neighbours and update their tentative distances
            for (Node n : minEntry.getKey().getNeighbours()) {
                if (unvisitedNodes.containsKey(n)) {
                    int newDistance = minEntry.getValue() + minEntry.getKey().getEdge(n).length;
                    if (unvisitedNodes.get(n) > newDistance) {
                        unvisitedNodes.put(n, newDistance);
                    }
                }
                // If end node is one of the neighbours, then a path has been found so exit loop
                if (n.equals(_endNode)) {
                    pathFound = true;
                }
            }

            // Mark lowest node as visited
            visitedNodes.put(minEntry.getKey(), minEntry.getValue());
            unvisitedNodes.remove(minEntry.getKey());
        }

        // Create path from finish to start

        // LinkedList which will store the shortest path
        ArrayList<Node> shortestPath = new ArrayList<>();
        Node currentNode = _endNode;

        boolean startFound = false;
        while (!startFound) {
            // Get neighbour with the lowest distance unless it's the starting node, in which case finish
            Node lowestNeighbour = null;
            int lowestDistance = Integer.MAX_VALUE;
            for (Node n : currentNode.getNeighbours()) {
                if (visitedNodes.containsKey(n)) {
                    if (visitedNodes.get(n) < lowestDistance) {
                        lowestDistance = visitedNodes.get(n);
                        lowestNeighbour = n;
                    }
                }
            }

            // Add node to list
            shortestPath.add(currentNode);

            if (lowestNeighbour.equals(_startNode)) {
                startFound = true;
            } else {
                visitedNodes.remove(lowestNeighbour);
                currentNode = lowestNeighbour;
            }
        }

        Collections.reverse(shortestPath);
        return shortestPath;
    }

    /**
     * A method that takes Dijkstra's shortest path, checks if there are any tiles with gold adjacent to the path,
     * and modifies the path to collect that gold and return to the original path.
     *
     * @param _startNode = the node from which to calculate the path
     * @param _endNode   = the node to which to calculate the path (i.e. the exit)
     * @return           = the path to take from _startNode to _endNode that includes any 1-step gold detours
     */
    public List<Node> pathWithDetours(Node _startNode, Node _endNode) {
        List<Node> path = shortestPath(_startNode, _endNode);
        int totalTime = timeRemaining;
        int timeRemaining = totalTime - Dijkstra.timeTakenToTraversePath(_startNode, path);

        boolean detoursFound;

        do {
            detoursFound = false;

            // Loop through every node in the path except the exit
            for (int i = 0; i < path.size() - 1; i++) {
                // Loop through every neighbour...
                for (Node neighbour : path.get(i).getNeighbours()) {
                    // ... that isn't in the path itself
                    if (!path.contains(neighbour)) {
                        // If there's gold on the neighbouring tile...
                        if (neighbour.getTile().getGold() > 0) {
                            // ... and there's time to pick it up and get back to the path...
                            if (timeRemaining > (path.get(i).getEdge(neighbour).length * 2)) {
                                // ... then detour to the neighbour and back
                                path.add(i + 1, neighbour);
                                path.add(i + 2, path.get(i));

                                // Update the time remaining after this detour
                                timeRemaining = totalTime - Dijkstra.timeTakenToTraversePath(_startNode, path);

                                // We should skip over these newly created nodes in the main loop
                                i += 2;

                                detoursFound = true;
                            }
                        }
                    }
                }
            }
        } while (detoursFound);

        return path;
    }

    /**
     * A method that checks if there's extra time to collect gold after considering Dijkstra's shortest path to the
     * exit, and if so collects the nearest gold and repeats.
     *
     * @param _startNode = the node from which to calculate the path
     * @param _endNode   = the node to which to calculate the path (i.e. the exit)
     * @return           = the path to take from _startNode to _endNode that includes gold collected before the
     *                     shortest path has to be taken to avoid running out of time
     */
    public List<Node> wanderingPath(Node _startNode, Node _endNode) {
        // It's possible that the shortest path collects all gold anyway, so no wandering is required
        List<Node> path = shortestPath(_startNode, _endNode);
        if (totalGoldOnPath(path) == totalGoldOnMap()) {
            return path;
        }

        List<Node> wanderingPath = new ArrayList<>();

        boolean needToGetOut = false;

        while (!needToGetOut) {
            // If there's gold left...
            if (totalGoldOnPath(wanderingPath) < totalGoldOnMap()) {
                // ... find the path to the nearest gold

                List<Node> pathFromStartToGold;
                if (wanderingPath.size() == 0) {
                    pathFromStartToGold = findPathToClosestNodeWithGold(_startNode, _endNode, wanderingPath);
                } else {
                    pathFromStartToGold = findPathToClosestNodeWithGold(wanderingPath.get(wanderingPath.size() - 1), _endNode, wanderingPath);
                }

                // Find the path from the nearest gold to the end
                List<Node> pathFromGoldToEnd = shortestPath(pathFromStartToGold.get(pathFromStartToGold.size() - 1), _endNode);

                int timeForStartToEndOfCurrentPath, timeForCurrentPathToGold, timeForGoldToEnd;

                // Calculate the time taken for each leg of the proposed journey
                if (wanderingPath.size() == 0) {
                    timeForStartToEndOfCurrentPath = 0;
                    timeForCurrentPathToGold = timeTakenToTraversePath(_startNode, pathFromStartToGold);
                } else {
                    timeForStartToEndOfCurrentPath = timeTakenToTraversePath(_startNode, wanderingPath);
                    timeForCurrentPathToGold = timeTakenToTraversePath(wanderingPath.get(wanderingPath.size() - 1), pathFromStartToGold);
                }
                timeForGoldToEnd = timeTakenToTraversePath(pathFromStartToGold.get(pathFromStartToGold.size() - 1), pathFromGoldToEnd);

                // If there's time to go and get the gold...
                if (timeForStartToEndOfCurrentPath + timeForCurrentPathToGold + timeForGoldToEnd < timeRemaining) {
                    // ... go and get it
                    wanderingPath.addAll(pathFromStartToGold);
                } else {
                    // Get to the exit
                    if (wanderingPath.size() == 0) {
                        wanderingPath = shortestPath(_startNode, _endNode);
                    } else {
                        wanderingPath.addAll(shortestPath(wanderingPath.get(wanderingPath.size() - 1), _endNode));
                    }

                    needToGetOut = true;
                }
            } else {
                // Get to the exit
                wanderingPath.addAll(shortestPath(wanderingPath.get(wanderingPath.size() - 1), _endNode));
                needToGetOut = true;
            }
        }

        return wanderingPath;
    }

    /**
     * A breadth-first search to find the closest node that contains any gold.
     *
     * @param _startingNode = the node from which to start looking for nodes containing gold
     * @param _visitedNodes = a collection of nodes from which gold has already been collected (as this logic is
     *                        carried out before movement begins so gold still exists in the escape state)
     * @return              = a Node object that is the closest to _startingNode that contains gold
     */
    public List<Node> findPathToClosestNodeWithGold(Node _startingNode, Node _exit, List<Node> _visitedNodes) {
        Set<Node> neighboursToCheck = _startingNode.getNeighbours();
        Node target = null;

        while (target == null) {
            List<Node> neighboursWithGold = new ArrayList<>();
            for (Node neighbour : neighboursToCheck) {
                if (!_visitedNodes.contains(neighbour)) {
                    if (neighbour.getTile().getGold() > 0) {
                        neighboursWithGold.add(neighbour);
                    }
                }
            }

            if (neighboursWithGold.size() == 0) {
                Set<Node> newNeighbours = new HashSet<>();
                for (Node neighbour : neighboursToCheck) {
                    newNeighbours.addAll(neighbour.getNeighbours());
                }
                neighboursToCheck = newNeighbours;
            } else {
                int furthestDistance = 0;
                for (Node neighbour : neighboursWithGold) {
                    int nodeDistanceFromExit = timeTakenToTraversePath(neighbour, shortestPath(neighbour, _exit));
                    if (nodeDistanceFromExit > furthestDistance) {
                        furthestDistance = nodeDistanceFromExit;
                        target = neighbour;
                    }
                }
            }
        }

        return shortestPath(_startingNode, target);
    }
}