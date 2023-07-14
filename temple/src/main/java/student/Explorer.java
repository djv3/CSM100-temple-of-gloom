package student;

import game.EscapeState;
import game.ExplorationState;
import game.Node;
import game.NodeStatus;
import java.util.*;

public class Explorer {

  /**
   * Explore the cavern, trying to find the orb in as few steps as possible. Once you find the orb,
   * you must return from the function in order to pick it up. If you continue to move after finding
   * the orb rather than returning, it will not count. If you return from this function while not
   * standing on top of the orb, it will count as a failure.
   *
   * <p>There is no limit to how many steps you can take, but you will receive a score bonus
   * multiplier for finding the orb in fewer steps.
   *
   * <p>At every step, you only know your current tile's ID and the ID of all open neighbor tiles,
   * as well as the distance to the orb at each of these tiles (ignoring walls and obstacles).
   *
   * <p>To get information about the current state, use functions getCurrentLocation(),
   * getNeighbours(), and getDistanceToTarget() in ExplorationState. You know you are standing on
   * the orb when getDistanceToTarget() is 0.
   *
   * <p>Use function moveTo(long id) in ExplorationState to move to a neighboring tile by its ID.
   * Doing this will change state to reflect your new position.
   *
   * <p>A suggested first implementation that will always find the orb, but likely won't receive a
   * large bonus multiplier, is a depth-first search.
   *
   * @param state the information available at the current state
   */
  public void explore(ExplorationState state) {

    ExploreAlgorithm.entryPoint =
        new NodeStatus(state.getCurrentLocation(), state.getDistanceToTarget());
    AStarExplore explorePath = new AStarExplore();

    while (true) {

      NodeA current = explorePath.getCurrentNode();

      // skip first move because we are already on the entry title
      if (current.nodeStatus().nodeID() != ExploreAlgorithm.entryPoint.nodeID())
        state.moveTo(current.nodeStatus().nodeID());

      // checks if we reach the node wit the Orb and exists from while-cycle
      if (current.nodeStatus().distanceToTarget() == 0) break;

      // the best option to move next from the current node
      NodeA nextMove = explorePath.getNextMove(state.getNeighbours(), current);

      /*
       * In case of the high complexity of the curve, there should be a decision to move back
       * It happens when we do not have any possible node to move
       * we track back until we have found the next unvisited node(next Move not empty)
       * {@param backNode} is a current node on backtrack
       */
      while (nextMove == null) {
        NodeA backNode = current.parent();
        state.moveTo(backNode.nodeStatus().nodeID()); // makes one step on backtrack
        current = backNode;
        nextMove = explorePath.getNextMoveTraceBack(state.getNeighbours(), backNode);
      }
    }
  }

  /**
   * Escape from the cavern before the ceiling collapses, trying to collect as much gold as possible
   * along the way. Your solution must ALWAYS escape before time runs out, and this should be
   * prioritized above collecting gold.
   *
   * <p>You now have access to the entire underlying graph, which can be accessed through
   * EscapeState. getCurrentNode() and getExit() will return you Node objects of interest, and
   * getVertices() will return a collection of all nodes on the graph.
   *
   * <p>Note that time is measured entirely in the number of steps taken, and for each step the time
   * remaining is decremented by the weight of the edge taken. You can use getTimeRemaining() to get
   * the time still remaining, pickUpGold() to pick up any gold on your current tile (this will fail
   * if no such gold exists), and moveTo() to move to a destination node adjacent to your current
   * node.
   *
   * <p>You must return from this function while standing at the exit. Failing to do so before time
   * runs out or returning from the wrong location will be considered a failed run.
   *
   * <p>You will always have enough time to escape using the shortest path from the starting
   * position to the exit, although this will not collect much gold.
   *
   * @param state the information available at the current state
   */
  public void escape(EscapeState state) {
    int timeRemaining = state.getTimeRemaining();
    Set<Node> vertices = (Set<Node>) state.getVertices();

    Dijkstra dijkstra = new Dijkstra(vertices, timeRemaining);
    AStar astar = new AStar(vertices, timeRemaining);

    List<Node> dijkstraRoute = dijkstra.bestPath(state.getCurrentNode(), state.getExit());
    List<Node> aStarRoute = astar.bestPath(state.getCurrentNode(), state.getExit());
    List<Node> escapeRoute;

    if (EscapeAlgorithm.totalGoldOnPath(dijkstraRoute)
        > EscapeAlgorithm.totalGoldOnPath(aStarRoute)) {
      escapeRoute = dijkstraRoute;
    } else {
      escapeRoute = aStarRoute;
    }

    while (escapeRoute.size() > 0) {
      if (state.getCurrentNode().getTile().getGold() > 0) {
        state.pickUpGold();
      }
      state.moveTo(escapeRoute.remove(0));
    }
  }
}
