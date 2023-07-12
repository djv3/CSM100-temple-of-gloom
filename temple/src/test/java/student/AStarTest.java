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
    // The shortest path in this case is also the best path for the aStar algorithm.
    // This is because we are making the best effort to collect from the nodes with the most gold first.
    // This means that because tile 4 has the lowest gold it is de-prioritised, so by the time we reach
    // tile 3 we no longer have enough time to make it to tile 2 and then to the exit. It's worth noting
    // that we could improve the algorithm by getting all permutations of the top nodes list and then
    // recursively determine the best path for each permutation. This would be very expensive though, as
    // the number of permutations is factorial(n) where n is the number of nodes with gold.
    List<Node> actualPath = aStar.bestPath(cavern.getNodeAt(1,1), cavern.getEntrance());
    assertEquals(shortestPath, actualPath);
  }

  @Test
  void testShortestPath() {
    assertEquals(shortestPath, aStar.shortestPath(cavern.getNodeAt(1,1), cavern.getEntrance()));
  }

}
