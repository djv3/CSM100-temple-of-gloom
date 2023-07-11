package student;

import static org.junit.jupiter.api.Assertions.*;

import game.Cavern;
import game.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AStarTest {

  Cavern cavern;
  AStar aStar;
  List<Node> shortestPath;
  int goldOnMap, goldOnShortestPath, goldOnBestPath;
  int timeRemaining, timeTakenForShortestPath;

  /**
   *  Setting seed 12345 with 5 rows and columns will generate the following cavern:
   * <p>
   *  1-2-3
   *  |   |
   *  4   5-6
   *  |   |
   *  7-8-9
   * <p>
   *  Tile 1 is the start position. Tile 6 is the exit.
   * <p>
   *  Tile 2 has 533 gold, tile 3 has 518 gold and tile 4 has 398 gold. Other tiles have no gold.
   * <p>
   *  Edges have the following weights:
   *  1-2: 11, 2-3:  9
   *  1-4:  7, 3-5:  6
   *  5-6: 11
   *  4-7:  3, 5-9:  7
   *  7-8: 14, 8-9:  7
   */

  @BeforeEach
  void setUp() {
    cavern = Cavern.digEscapeCavern(5, 5, 1, 1, new Random(12345));

    // Because we know the layout of the cavern we can hard-code the shortest path
    shortestPath = new ArrayList<>();
    shortestPath.add(cavern.getNodeAt(1,2));
    shortestPath.add(cavern.getNodeAt(1,3));
    shortestPath.add(cavern.getNodeAt(2,3));
    shortestPath.add(cavern.getNodeAt(2,4));

    // Because we know the layout of the cavern we can hard-code the gold on the map
    goldOnMap = 533 + 518 + 398;
    goldOnShortestPath = 533 + 518;
    goldOnBestPath = goldOnMap;

    // Because we know the layout of the cavern we can hard-code the time remaining
    // (using the formula used by the program: timeForShortestPath + 0.3 * (MAX_EDGE_WEIGHT + 1) * cavern.numOpenTiles() / 2)
    timeRemaining = 58;
    timeTakenForShortestPath = 37;

    aStar = new AStar(cavern.getGraph(), timeRemaining);
  }
  @Test
  void testBestPath() {
    List<Node> actualPath = aStar.bestPath(cavern.getNodeAt(1,1), cavern.getEntrance());
    List<Node> expectedPath = new ArrayList<>();
    expectedPath.add(cavern.getNodeAt(2,1));
    expectedPath.add(cavern.getNodeAt(1,1));
    expectedPath.add(cavern.getNodeAt(1,2));
    expectedPath.add(cavern.getNodeAt(1,3));
    expectedPath.add(cavern.getNodeAt(2,3));
    expectedPath.add(cavern.getNodeAt(2,4));
    assertEquals(expectedPath, actualPath);
  }

  @Test
  void testTotalGoldOnMap() {
    assertEquals(goldOnMap, aStar.totalGoldOnMap());
  }

  @Test
  void testTotalGoldOnPath() {
    assertEquals(goldOnShortestPath, EscapeAlgorithm.totalGoldOnPath(shortestPath));
  }

  @Test
  void testTimeTakenToTraversePath() {
    assertEquals(timeTakenForShortestPath, EscapeAlgorithm.timeTakenToTraversePath(cavern.getNodeAt(1, 1), shortestPath));
  }

//  @Test
//  void testFindPathToClosestNodeWithGold() {
//    // From 1,1 the method should return the path to 2,1 (i.e. just 1 node)
//    List<Node> actualPath = aStar.findPathToClosestNodeWithGold(cavern.getNodeAt(1,1), new ArrayList<>());
//    List<Node> expectedPath = new ArrayList<>();
//    expectedPath.add(cavern.getNodeAt(2,1));
//    assertEquals(expectedPath, actualPath);
//
//    // From 3,3 the method should return the path to 1,3 (i.e. 2,3 -> 1,3)
//    actualPath = dijkstra.findPathToClosestNodeWithGold(cavern.getNodeAt(3,3), new ArrayList<>());
//    expectedPath = new ArrayList<>();
//    expectedPath.add(cavern.getNodeAt(2,3));
//    expectedPath.add(cavern.getNodeAt(1,3));
//    assertEquals(expectedPath, actualPath);
//
//    // If all other gold is already collected, the method should route to 2,1 from 3,3
//    List<Node> visitedNodes = new ArrayList<>();
//    visitedNodes.add(cavern.getNodeAt(1,2));
//    visitedNodes.add(cavern.getNodeAt(1,3));
//    actualPath = dijkstra.findPathToClosestNodeWithGold(cavern.getNodeAt(3,3), visitedNodes);
//    expectedPath = new ArrayList<>();
//    expectedPath.add(cavern.getNodeAt(3,2));
//    expectedPath.add(cavern.getNodeAt(3,1));
//    expectedPath.add(cavern.getNodeAt(2,1));
//    assertEquals(expectedPath, actualPath);
//  }

}
