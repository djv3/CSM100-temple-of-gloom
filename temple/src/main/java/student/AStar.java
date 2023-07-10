package student;

import game.Cavern;
import game.EscapeState;
import game.Node;

import java.util.*;

public class AStar extends EscapeAlgorithm {
    public AStar(EscapeState _escapeState) {
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

            Node nextNode = nearestNodesWithGold.pollFirst();

            if (nextNode == null) {
                path.addAll(pathToExit);
                break;
            }

            List<Node> nearestNodeWithGoldPath = shortestPath(lastNodeInPath, nextNode);

            if (nearestNodeWithGoldPath.size() == 0) {
                path.addAll(pathToExit);
                break;
            }

            Node lastNodeInNewestPath = nearestNodeWithGoldPath.get(nearestNodeWithGoldPath.size() - 1);

            if (lastNodeInNewestPath.equals(exit)) {
                path.addAll(nearestNodeWithGoldPath);
                break;
            }


            if (pathLength(path) + pathLength(nearestNodeWithGoldPath) + pathLength(shortestPath(lastNodeInNewestPath, exit)) + pathLength(pathToExit) > timeRemaining) {
                path.addAll(pathToExit);
                break;
            }

            path.addAll(nearestNodeWithGoldPath);

            pathLength = pathLength(path);
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
        int manhattanDistance = Math.abs(currentColumn - exitColumn) + Math.abs(currentRow - exitRow);
        return manhattanDistance + Cavern.MAX_GOLD_VALUE - node.getTile().getGold();
    }

    @Override
    public List<Node> shortestPath(Node start, Node exit) {
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
