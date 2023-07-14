package student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import game.NodeStatus;
import java.lang.reflect.Field;
import java.util.*;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AStarExploreTests {
  NodeStatus currentNodeStatus;
  NodeA currentNode;
  AStarExplore explorePath;
  Collection<NodeStatus> neighboursToCurrent;

  /**
   * #1 testGetNextMoveWhenNodeNotInOpenAndNotInClosed
   *
   * <p>Test prerequisites: current node == entry node == node [id=1, distanceToTarget = 100, fScore
   * = 100, gScore = 0, parent = null] neighbours of current node = list: node [id=2,
   * distanceToTarget = 98, fScore = 99, gScore = 1, parent = node#1] node [id=3, distanceToTarget =
   * 97, fScore = 98, gScore = 1, parent = node#1] node [id=4, distanceToTarget = 96, fScore = 97,
   * gScore = 1, parent = node#1] node [id=5, distanceToTarget = 95, fScore = 99, gScore = 1, parent
   * = node#1] node [id=6, distanceToTarget = 94, fScore = 95, gScore = 1, parent = node#1]
   *
   * <p>Preconditions: neighbours are neither not in openSet nor in closeSet Expected results: node
   * [id=6, distanceToTarget = 94, fScore = 95, gScore = 1, parent = node#1]
   */
  @Before
  public void init1() {
    currentNodeStatus = new NodeStatus(1, 100);
    currentNode = new NodeA(currentNodeStatus, 100, 0, null);
    ExploreAlgorithm.entryPoint = new NodeStatus(1, 100);
    explorePath = new AStarExplore();
    neighboursToCurrent = new ArrayList<>();

    for (int i = 2; i < 7; i++) neighboursToCurrent.add(new NodeStatus(i, 100 - i));
  }

  @Test
  @Order(1)
  void testGetNextMoveWhenNodeNotInOpenAndNotInClosed() {
    init1();
    NodeStatus nodeStatusExpected = new NodeStatus(6, 94);
    NodeA nextMoveExpected = new NodeA(nodeStatusExpected, 95, 1, currentNode);

    NodeA nextMove = explorePath.getNextMove(neighboursToCurrent, currentNode);
    assertEquals(nextMoveExpected, nextMove);
  }

  /**
   * #2 testGetNextMoveWhenNodeNotInOpenAndInClosed
   *
   * <p>Test prerequisites: current node == node [id=6, distanceToTarget = 94, fScore = 95, gScore =
   * 1, parent = #node1] neighbours of current node = list: node [id=2, distanceToTarget = 98,
   * fScore = 100, gScore = 2, parent = node#6] node [id=3, distanceToTarget = 97, fScore = 99,
   * gScore = 2, parent = node#6] node [id=4, distanceToTarget = 96, fScore = 98, gScore = 2, parent
   * = node#6] node [id=5, distanceToTarget = 95, fScore = 97, gScore = 2, parent = node#6]
   *
   * <p>Preconditions: neighbours are in closedSet and are not in openSet Expected results: null
   *
   * <p>Notice: Use Java reflection to change data in closedSet & openSet
   */
  @Before
  public void init2() throws NoSuchFieldException, IllegalAccessException {

    ExploreAlgorithm.entryPoint = new NodeStatus(1, 100);
    explorePath = new AStarExplore();
    neighboursToCurrent = new ArrayList<>();

    // set neighbours
    for (int i = 2; i < 6; i++) neighboursToCurrent.add(new NodeStatus(i, 100 - i));

    NodeA previousNode = new NodeA(ExploreAlgorithm.entryPoint, 100, 0, null);
    List<NodeA> closedSetList = ExploreAlgorithm.getNeighborsA(neighboursToCurrent, previousNode);

    // set through Java reflection the following state: openSet is empty, closedSet is full of
    // neighbours
    PriorityQueue<NodeA> closedSet = new PriorityQueue<>();
    PriorityQueue<NodeA> openSet = new PriorityQueue<>();
    closedSet.addAll(closedSetList);

    //  change access to openSet and closedSet to updated states
    Field closedSetField = explorePath.getClass().getDeclaredField("closedSet");
    closedSetField.setAccessible(true);
    Field openSetField = explorePath.getClass().getDeclaredField("openSet");
    openSetField.setAccessible(true);

    // set new value of openSet and closedSet
    closedSetField.set(explorePath, closedSet);
    openSetField.set(explorePath, openSet);
  }

  @Test
  @Order(2)
  void testGetNextMoveWhenNodeNotInOpenAndInClosed()
      throws NoSuchFieldException, IllegalAccessException {
    init2();
    currentNodeStatus = new NodeStatus(6, 94);
    currentNode =
        new NodeA(currentNodeStatus, 95, 1, new NodeA(ExploreAlgorithm.entryPoint, 100, 0, null));

    NodeA nextMove = explorePath.getNextMove(neighboursToCurrent, currentNode);
    assertNull(nextMove);
  }

  /**
   * #3 testGetNextMoveWhenNodeInOpenAndNotInClosed
   *
   * <p>Test prerequisites: current node == node [id=6, distanceToTarget = 94, fScore = 95, gScore =
   * 1, parent = #node1] neighbours of current node = list: node [id=7, distanceToTarget = 93,
   * fScore = 95, gScore = 2, parent = node#6] node [id=8, distanceToTarget = 92, fScore = 94,
   * gScore = 2, parent = node#6] node [id=9, distanceToTarget = 91, fScore = 93, gScore = 2, parent
   * = node#6] node [id=10, distanceToTarget = 90, fScore = 92, gScore = 2, parent = node#6]
   *
   * <p>Preconditions: neighbours are in openSet and are not in closedSet Expected result: node
   * [id=10, distanceToTarget = 90, fScore = 92, gScore = 2, parent = node#6]
   *
   * <p>Notice: Use Java reflection to change data in closedSet & openSet
   */
  @Before
  public void init3() throws NoSuchFieldException, IllegalAccessException {

    ExploreAlgorithm.entryPoint = new NodeStatus(1, 100);
    explorePath = new AStarExplore();
    neighboursToCurrent = new ArrayList<>();

    // set neighbours
    for (int i = 7; i < 11; i++) neighboursToCurrent.add(new NodeStatus(i, 100 - i));

    currentNodeStatus = new NodeStatus(6, 94);
    currentNode =
        new NodeA(currentNodeStatus, 95, 1, new NodeA(ExploreAlgorithm.entryPoint, 100, 0, null));

    // NodeA previousNode = new NodeA(ExploreAlgorithm.entryPoint, 100,0,null);
    List<NodeA> openSetList = ExploreAlgorithm.getNeighborsA(neighboursToCurrent, currentNode);

    // set through Java reflection the following state: closedSet is empty, openSet is full of
    // neighbours
    PriorityQueue<NodeA> closedSet = new PriorityQueue<>();
    PriorityQueue<NodeA> openSet = new PriorityQueue<>();
    openSet.addAll(openSetList);

    //  change access to openSet and closedSet to updated states
    Field closedSetField = explorePath.getClass().getDeclaredField("closedSet");
    closedSetField.setAccessible(true);
    Field openSetField = explorePath.getClass().getDeclaredField("openSet");
    openSetField.setAccessible(true);

    // set new value of openSet and closedSet
    closedSetField.set(explorePath, closedSet);
    openSetField.set(explorePath, openSet);
  }

  @Test
  @Order(3)
  void testGetNextMoveWhenNodeInOpenAndNotInClosed()
      throws NoSuchFieldException, IllegalAccessException {
    init3();

    NodeStatus nodeStatusExpected = new NodeStatus(10, 90);
    NodeA nextMoveExpected = new NodeA(nodeStatusExpected, 92, 2, currentNode);

    NodeA nextMove = explorePath.getNextMove(neighboursToCurrent, currentNode);
    assertEquals(nextMoveExpected, nextMove);
  }

  /**
   * #4 testNextMoveTraceBackNodeNotInOpenAndInListClosed
   *
   * <p>Test prerequisites: current node == node [id=10, distanceToTarget = 90, fScore = 92, gScore
   * = 2, parent = node#6] neighbours of current node = list: node [id=6, distanceToTarget = 94,
   * fScore = 95, gScore = 1, parent = #node1] node [id=7, distanceToTarget = 93, fScore = 95,
   * gScore = 2, parent = node#6] node [id=8, distanceToTarget = 92, fScore = 94, gScore = 2, parent
   * = node#6] node [id=9, distanceToTarget = 91, fScore = 93, gScore = 2, parent = node#6]
   *
   * <p>Preconditions: neighbours are not in openSet and are in closedSet Expected result: null
   *
   * <p>Notice: Use Java reflection to change data in closedSet & openSet
   */
  @Before
  public void init4() throws NoSuchFieldException, IllegalAccessException {

    ExploreAlgorithm.entryPoint = new NodeStatus(1, 100);
    explorePath = new AStarExplore();
    neighboursToCurrent = new ArrayList<>();

    for (int i = 6; i < 10; i++) neighboursToCurrent.add(new NodeStatus(i, 100 - i));

    Field closedSetField = explorePath.getClass().getDeclaredField("closedSet");
    closedSetField.setAccessible(true);
    Field openSetField = explorePath.getClass().getDeclaredField("openSet");
    openSetField.setAccessible(true);

    NodeA previousNode = new NodeA(ExploreAlgorithm.entryPoint, 100, 0, null);

    List<NodeA> closedSetList = ExploreAlgorithm.getNeighborsA(neighboursToCurrent, previousNode);

    neighboursToCurrent.add(new NodeStatus(5, 95));
    closedSetList.add(new NodeA(new NodeStatus(5, 95), 96, 3, previousNode));

    PriorityQueue<NodeA> closedSet = new PriorityQueue<>();
    PriorityQueue<NodeA> openSet = new PriorityQueue<>();
    closedSet.addAll(closedSetList);

    closedSetField.set(explorePath, closedSet);
    openSetField.set(explorePath, openSet);
  }

  @Test
  @Order(4)
  void testNextMoveTraceBackNodeNotInOpenAndInListClosed()
      throws NoSuchFieldException, IllegalAccessException {
    init4();
    currentNodeStatus = new NodeStatus(10, 90);
    currentNode =
        new NodeA(
            currentNodeStatus,
            92,
            2,
            new NodeA(
                new NodeStatus(6, 94),
                95,
                1,
                new NodeA(ExploreAlgorithm.entryPoint, 100, 0, null)));

    NodeA nextMove = explorePath.getNextMoveTraceBack(neighboursToCurrent, currentNode);
    assertNull(nextMove);
  }

  /**
   * #5 testNextMoveTraceBackNodeNotInOpenAndNotInListClosed
   *
   * <p>Test prerequisites: current node == node [id=10, distanceToTarget = 90, fScore = 92, gScore
   * = 2, parent = node#6] neighbours of current node = list: node [id=6, distanceToTarget = 94,
   * fScore = 97, gScore = 3, parent = #node1] node [id=7, distanceToTarget = 93, fScore = 96,
   * gScore = 3, parent = node#6] node [id=8, distanceToTarget = 92, fScore = 95, gScore = 3, parent
   * = node#6] node [id=9, distanceToTarget = 91, fScore = 94, gScore = 3, parent = node#6]
   *
   * <p>Preconditions: neighbours are not in openSet and are not in closedSet Expected results: node
   * [id=9, distanceToTarget = 91, fScore = 94, gScore = 3, parent = node#6]
   *
   * <p>Notice: Use Java reflection to change data in closedSet & openSet
   */
  @Before
  public void init5() throws NoSuchFieldException, IllegalAccessException {

    ExploreAlgorithm.entryPoint = new NodeStatus(1, 100);
    explorePath = new AStarExplore();
    neighboursToCurrent = new ArrayList<>();

    for (int i = 6; i < 10; i++) neighboursToCurrent.add(new NodeStatus(i, 100 - i));
  }

  @Test
  @Order(5)
  void testNextMoveTraceBackNodeNotInOpenAndNotInListClosed()
      throws NoSuchFieldException, IllegalAccessException {
    init5();
    currentNodeStatus = new NodeStatus(10, 90);
    currentNode =
        new NodeA(
            currentNodeStatus,
            92,
            2,
            new NodeA(
                new NodeStatus(6, 94),
                95,
                1,
                new NodeA(ExploreAlgorithm.entryPoint, 100, 0, null)));

    NodeStatus nodeStatusExpected = new NodeStatus(9, 91);
    NodeA nextMoveExpected =
        new NodeA(
            nodeStatusExpected,
            94,
            3,
            new NodeA(
                new NodeStatus(10, 90),
                92,
                2,
                (new NodeA(
                    new NodeStatus(6, 94),
                    95,
                    1,
                    new NodeA(ExploreAlgorithm.entryPoint, 100, 0, null)))));

    NodeA nextMove = explorePath.getNextMoveTraceBack(neighboursToCurrent, currentNode);
    assertEquals(nextMoveExpected, nextMove);
  }
}
