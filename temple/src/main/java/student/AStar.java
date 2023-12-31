package student;

import game.Node;
import java.util.*;

public class AStar extends EscapeAlgorithm {
  public AStar(Set<Node> _graph, int _timeRemaining) {
    graph = _graph;
    timeRemaining = _timeRemaining;
  }

  /**
   * Uses the A* algorithm to build a path to a node, prioritising nodes with gold. Uses the
   * shortestPath method to find the best path between important nodes.
   *
   * @param start the node to start from
   * @param exit the node to end at
   * @return a list of nodes that make up the best path
   */
  @Override
  public List<Node> bestPath(Node start, Node exit) {
    ArrayList<Node> nodesWithGold = new ArrayList<>();
    ArrayList<Node> topNodesWithGold = new ArrayList<>();

    graph.forEach(
        n -> {
          if (n.getTile().getGold() > 0 && !n.equals(start)) {
            nodesWithGold.add(n);
          }
        });

    nodesWithGold.sort(Comparator.comparingInt(o -> o.getTile().getGold()));
    Collections.reverse(nodesWithGold);

    for (int i = 0; i < Math.min(10, nodesWithGold.size()); i++) {
      topNodesWithGold.add(nodesWithGold.get(i));
    }

    List<Node> path = new ArrayList<>();
    path.add(start);

    if (topNodesWithGold.isEmpty()) {
      path.addAll(shortestPath(start, exit));
    }

    for (Node node : topNodesWithGold) {

      Node lastNode = path.get(path.size() - 1);

      if (node.equals(exit)) {
        path.addAll(shortestPath(lastNode, node));
        break;
      }

      List<Node> pathToNextNode = shortestPath(lastNode, node);

      Node lastNodeInNextMove = pathToNextNode.get(pathToNextNode.size() - 1);

      List<Node> pathToExit = shortestPath(lastNodeInNextMove, exit);

      List<Node> escapePath = shortestPath(lastNode, exit);

      if (timeRemaining > pathLength(path) + pathLength(pathToNextNode) + pathLength(pathToExit)) {
        path.addAll(pathToNextNode);
      } else {
        path.addAll(escapePath);
        break;
      }
    }
    Node lastNode = path.get(path.size() - 1);
    if (!lastNode.equals(exit)) {
      path.addAll(shortestPath(lastNode, exit));
    }

    if (pathLength(path) > timeRemaining) {
      path = shortestPath(start, exit);
    }

    if (path.size() > 0 && path.get(0).equals(start)) {
      path.remove(0);
    }

    return path;
  }

  int getNeighboursGold(Node node) {
    int gold = 0;
    for (Node neighbour : node.getNeighbours()) {
      gold += neighbour.getTile().getGold();
    }
    return gold;
  }

  List<Node> getNeighboursWithGold(Node node) {
    List<Node> neighboursWithGold = new ArrayList<>();
    for (Node neighbour : node.getNeighbours()) {
      if (neighbour.getTile().getGold() > 200) {
        neighboursWithGold.add(neighbour);
      }
    }
    return neighboursWithGold;
  }

  /**
   * Calculates the length of a path.
   *
   * @param path A list of nodes representing a path to another node.
   * @return The length of the path.
   */
  public int pathLength(List<Node> path) {
    if (path.size() == 1) {
      return 0;
    }
    int total = 0;
    Node previous = null;

    for (Node node : path) {
      if (previous == null) {
        previous = node;
        continue;
      }
      total += previous.getEdge(node).length;
      previous = node;
    }
    return total;
  }

  /**
   * Estimates the distance between a node and another node. The estimate is the Manhattan distance
   * between the two nodes, weighted by the amount of gold in the current node.
   *
   * @param node the current node
   * @param other the other node
   * @return the estimated distance between the two nodes
   */
  public int estimate(Node node, Node other) {
    int currentColumn = node.getTile().getColumn();
    int currentRow = node.getTile().getRow();
    int exitColumn = other.getTile().getColumn();
    int exitRow = other.getTile().getRow();

    return Math.abs(currentColumn - exitColumn) + Math.abs(currentRow - exitRow);
  }

  /**
   * Finds the shortest path between two nodes using the A* algorithm.
   *
   * @param start the start node
   * @param exit the exit node
   * @return the shortest path between the two nodes
   */
  @Override
  public List<Node> shortestPath(Node start, Node exit) {
    HashMap<Node, Integer> open = new HashMap<>();
    HashMap<Node, Integer> closed = new HashMap<>();
    HashMap<Node, Node> parent = new HashMap<>();
    ArrayList<Node> path = new ArrayList<>();
    open.put(start, 0);

    while (!open.isEmpty()) {
      Node current =
          open.entrySet().stream().min(Comparator.comparingInt(Map.Entry::getValue)).get().getKey();
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
