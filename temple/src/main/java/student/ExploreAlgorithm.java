package student;

import game.NodeStatus;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * Contains three methods to be re-used during implementation of particular search algorithm {@param
 * entrePoint} is the start node for search
 */
public abstract class ExploreAlgorithm extends Algorithm {

  public static NodeStatus entryPoint;

  public abstract NodeA getCurrentNode();

  /**
   * Converts data<NodeStatus> Collection of neighbours{@param neighbours} to list of node records
   * NodeA Uses current node record {@param current} as a parent in record NodeA for all neighbours
   *
   * @param current = node which has neighbours
   * @return list of the records NodeA of neighbours of the current node
   */
  public static List<NodeA> getNeighborsA(Collection<NodeStatus> neighbours, NodeA current) {

    return neighbours.stream().toList().stream()
        .map(
            n -> {
              int g = current.g() + 1;
              int f = g + n.distanceToTarget();
              return new NodeA(n, f, g, current);
            })
        .collect(Collectors.toList());
  }

  /**
   * Validates if the concrete node {@param m} is in the given list {@param list} Checks records by
   * nodeID parameter
   */
  public static boolean is_node_in_list(NodeA m, PriorityQueue<NodeA> list) {
    return list.stream().anyMatch(n -> m.nodeStatus().nodeID() == n.nodeStatus().nodeID());
  }
}
