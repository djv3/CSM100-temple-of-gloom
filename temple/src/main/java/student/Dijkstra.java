package student;

import game.EscapeState;
import game.Node;

import java.util.*;

public class Dijkstra extends EscapeAlgorithm {
    public Dijkstra(EscapeState _escapeState) {
        escapeState = _escapeState;
    }

    public List<Node> bestPath(Node _startNode, Node _endNode) {
        List<Node> detourAlgorithm = pathWithDetours(_startNode, _endNode);
        List<Node> wanderingAlgorithm = wanderingPath(_startNode, _endNode);

        if (totalGoldOnPath(detourAlgorithm) > totalGoldOnPath(wanderingAlgorithm)) {
            return detourAlgorithm;
        } else {
            return wanderingAlgorithm;
        }
    }

    public List<Node> shortestPath(Node _startNode, Node _endNode) {
        // 2 maps, one for nodes that have been visited and one for nodes that haven't
        HashMap<Node, Integer> unvisitedNodes = new HashMap<>();
        HashMap<Node, Integer> visitedNodes = new HashMap<>();

        // Store all nodes in the first map
        for (Node n : escapeState.getVertices()) {
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

    public List<Node> pathWithDetours(Node _startNode, Node _endNode) {
        List<Node> path = shortestPath(_startNode, _endNode);
        int totalTime = escapeState.getTimeRemaining();
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

    public List<Node> wanderingPath(Node _startNode, Node _endNode) {
        // It's possible that the shortest path collects all gold anyway, so no wandering is required
        List<Node> path = shortestPath(_startNode, _endNode);
        if (totalGoldOnPath(path) == totalGoldOnMap()) {
            return path;
        }

        List<Node> wanderingPath = new ArrayList<>();

        boolean needToGetOut = false;

        while (!needToGetOut) {
            // Find the path to the nearest gold
            List<Node> pathFromStartToGold;
            if (wanderingPath.size() == 0) {
                pathFromStartToGold = findPathToClosestNodeWithGold(_startNode, wanderingPath);
            } else {
                pathFromStartToGold = findPathToClosestNodeWithGold(wanderingPath.get(wanderingPath.size() - 1), wanderingPath);
            }

            // Find the path from the nearest gold to the end
            List<Node> pathFromGoldToEnd = shortestPath(pathFromStartToGold.get(pathFromStartToGold.size() - 1), _endNode);

            // If there's gold left...
            if (totalGoldOnPath(wanderingPath) < totalGoldOnMap()
                // and there's time to go and get the gold...
                && (wanderingPath.size() == 0 || timeTakenToTraversePath(_startNode, wanderingPath) + timeTakenToTraversePath(wanderingPath.get(wanderingPath.size() - 1), pathFromStartToGold) + timeTakenToTraversePath(pathFromStartToGold.get(pathFromStartToGold.size() - 1), pathFromGoldToEnd) < escapeState.getTimeRemaining())) {
                // ... go and get it
                wanderingPath.addAll(pathFromStartToGold);
            } else {
                // Get to the exit
                wanderingPath.addAll(shortestPath(wanderingPath.get(wanderingPath.size() - 1), _endNode));
                needToGetOut = true;
            }
        }

        return wanderingPath;
    }

    public List<Node> findPathToClosestNodeWithGold(Node _startingNode, List<Node> _visitedNodes) {
        Set<Node> neighboursToCheck = _startingNode.getNeighbours();
        Node target = null;

        while (target == null) {
            for (Node neighbour : neighboursToCheck) {
                if (!_visitedNodes.contains(neighbour)) {
                    if (neighbour.getTile().getGold() > 0) {
                        target = neighbour;
                        break;
                    }
                }
            }

            if (target == null) {
                Set<Node> newNeighbours = new HashSet<>();
                for (Node neighbour : neighboursToCheck) {
                    for (Node neighbourNeighbour : neighbour.getNeighbours()) {
                        newNeighbours.add(neighbourNeighbour);
                    }
                }
                neighboursToCheck = newNeighbours;
            }
        }

        return shortestPath(_startingNode, target);
    }
}