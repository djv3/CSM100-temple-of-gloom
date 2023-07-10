package student;

import game.EscapeState;
import game.Node;

import java.util.*;

public class Astar extends EscapeAlgorithm {
    public Astar(EscapeState _escapeState) {
        escapeState = _escapeState;
    }

    @Override
    public List<Node> bestPath(Node start, Node exit) {
        int timeRemaining = escapeState.getTimeRemaining();
        System.out.println("time remaining: " + timeRemaining);
        TreeSet<Node> nearestNodesWithGold = new TreeSet<>(Comparator.comparingInt(o -> estimate(o, start)));

        escapeState.getVertices().forEach(n -> {
            if (n.getTile().getGold() > 0) {
                nearestNodesWithGold.add(n);
            }
        });

        List<Node> path = new ArrayList<>();

        path.add(start);

        int pathLength = pathLength(path);

        while (pathLength <= timeRemaining) {
            Node lastNodeInPath = path.get(path.size() - 1);
            if (lastNodeInPath.equals(exit)) {
                break;
            }
            List<Node> pathToExit = shortestPath(lastNodeInPath, exit);

            if (pathLength(path) + pathLength(pathToExit) == timeRemaining) {
                path.addAll(pathToExit);
                break;
            }

            Node nearestNodeWithGold = nearestNodesWithGold.pollFirst();

            if (nearestNodeWithGold == null) {
                System.out.println("There's no more gold. The path length is: " + pathLength);
                path.addAll(pathToExit);
                break;
            }

            List<Node> nearestNodeWithGoldPath = shortestPath(lastNodeInPath, nearestNodeWithGold);

            if (nearestNodeWithGoldPath.size() == 0) {
                System.out.println("The path to the nearest node with gold is of zero length");
                path.addAll(pathToExit);
                break;
            }

            Node lastNodeInNewPath = nearestNodeWithGoldPath.get(nearestNodeWithGoldPath.size() - 1);
            var pathToExitFromNearestNodeWithGold = shortestPath(lastNodeInNewPath, exit);

            int totalPathLengths = (pathLength(path) + pathLength(nearestNodeWithGoldPath) + pathLength(pathToExitFromNearestNodeWithGold));

            if (totalPathLengths > timeRemaining) {
                System.out.println("The total path length is greater than the time remaining");
                path.addAll(pathToExit);
                break;
            }

            path.addAll(nearestNodeWithGoldPath);

            pathLength = pathLength(path);
            System.out.println("path length: " + pathLength);
        }
        path.remove(0);
        return path;
    }

    public int pathLength(List<Node> path) {
        int pathLength = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            pathLength += path.get(i).getEdge(path.get(i + 1)).length;
        }
        return pathLength;
    }

    public int estimate(Node node, Node exit) {
        int currentColumn = node.getTile().getColumn();
        int currentRow = node.getTile().getRow();
        int exitColumn = exit.getTile().getColumn();
        int exitRow = exit.getTile().getRow();

        return Math.max(Math.abs(currentColumn - exitColumn), Math.abs(currentRow - exitRow));
    }

    @Override
    public List<Node> shortestPath(Node start, Node exit) {
        return getPath(start, exit);
    }

    public List<Node> getPath(Node start, Node exit) {
        HashMap<Node, Integer> open = new HashMap<>();
        HashMap<Node, Integer> closed = new HashMap<>();
        HashMap<Node, Node> parent = new HashMap<>();
        ArrayList<Node> path = new ArrayList<>();
        open.put(start, 0);

        while (!open.isEmpty()) {
            Node current = open.entrySet().stream().min(Comparator.comparingInt(Map.Entry::getValue)).get().getKey();
            open.remove(current);
            closed.put(current, estimate(current, exit));

            if (current.equals(exit)) {
                while (current != null) {
                    path.add(current);
                    current = parent.get(current);
                }
                Collections.reverse(path);
                path.remove(0);
                return path;
            }

            for (Node n : current.getNeighbours()) {
                if (!closed.containsKey(n)) {
                    int newDistance = closed.get(current) + current.getEdge(n).length;
                    if (!open.containsKey(n) || open.get(n) > newDistance) {
                        open.put(n, newDistance + estimate(n, exit));
                        parent.put(n, current);
                    }
                }
            }
        }
        return path;
    }
}
