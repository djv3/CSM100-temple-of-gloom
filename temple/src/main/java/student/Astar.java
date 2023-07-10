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
//        int timeRemaining = escapeState.getTimeRemaining();
//        TreeSet<Node> nearestNodesWithGold = new TreeSet<>(Comparator.comparingInt(o -> estimate(o, start)));
//        escapeState.getVertices().forEach(n -> {
//            if (n.getTile().getGold() > 0) {
//                nearestNodesWithGold.add(n);
//            }
//        });
//        List<Node> path = new ArrayList<>();
//
//        int pathLength = timeTakenToTraversePath(start, path);
//
//        while (pathLength < timeRemaining) {
//            Node nearestNodeWithGold = nearestNodesWithGold.pollFirst();
//            List<Node> pathChunk = bestPath(start, nearestNodeWithGold);
//            Node lastNodeInChunk = pathChunk.get(pathChunk.size() - 1);
//            Node lastNodeInPath = path.get(path.size() - 1);
//            pathLength = timeTakenToTraversePath(start, path);
//        }
        return getPath(start, exit);
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
