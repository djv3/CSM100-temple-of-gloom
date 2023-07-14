package student;

import game.NodeStatus;

/**
 * This record of Node A is data structure for representing node(tile) in more convenient way to
 * process A-star algorithm the more information about parameters of record is
 * https://en.wikipedia.org/wiki/A*_search_algorithm
 *
 * @param nodeStatus
 * @param f == fScore, costs to reach node with Orb from entrePoint node(start)
 * @param g == GScore, cost to reach the current node with NodeStatus from from entrePoint
 *     node(start)
 * @param parent == parent of the current node
 */
public record NodeA(NodeStatus nodeStatus, int f, int g, NodeA parent)
    implements Comparable<NodeA> {
  @Override
  public int compareTo(NodeA n) {
    return Integer.compare(this.f, n.f);
  }
}
