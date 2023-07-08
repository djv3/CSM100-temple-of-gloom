package student;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import game.Cavern;
import game.Node;

class HeuristicTest {
    Cavern cavern;
    Node currentNode, exitNode;

    Random random;
    int currentRow, currentColumn;
    int exitRow, exitColumn;

    @BeforeEach
    void setUp() {
        random = new Random();
        currentRow = random.nextInt(8) + 1;
        currentColumn = random.nextInt(8) + 1;
        cavern = Cavern.digEscapeCavern(10, 10, currentRow, currentColumn, random);
        currentNode = cavern.getNodeAt(currentRow, currentColumn);
        exitNode = cavern.getEntrance();
        exitRow = exitNode.getTile().getRow();
        exitColumn = exitNode.getTile().getColumn();
    }

    @RepeatedTest(10)
    @DisplayName("Test Chebyshev distance on randomised escape cavern layouts")
    void chebyshev() {
        int expectedValue = Math.max(Math.abs(currentColumn - exitColumn), Math.abs(currentRow - exitRow));
        int actualValue = new Chebyshev().estimate(currentNode, exitNode);

        assertEquals(expectedValue, actualValue);
    }

    @RepeatedTest(10)
    @DisplayName("Test Euclidean distance on randomised escape cavern layouts")
    void euclidean() {
        int expectedValue = (int) Math.sqrt(Math.pow(currentColumn - exitColumn, 2) + Math.pow(currentRow - exitRow, 2));
        int actualValue = new Euclidean().estimate(currentNode, exitNode);

        assertEquals(expectedValue, actualValue);
    }

    @RepeatedTest(10)
    @DisplayName("Test Greedy heuristic distance on randomised escape cavern layouts")
    void greedy() {
        int expectedValue = Math.abs(currentColumn - exitColumn) + Math.abs(currentRow - exitRow) + Cavern.MAX_GOLD_VALUE - currentNode.getTile().getGold();
        int actualValue = new Greedy().estimate(currentNode, exitNode);

        assertEquals(expectedValue, actualValue);
    }

    @RepeatedTest(10)
    @DisplayName("Test Manhattan distance on randomised escape cavern layouts")
    void manhattan() {
        int expectedValue = Math.abs(currentColumn - exitColumn) + Math.abs(currentRow - exitRow);
        int actualValue = new Manhattan().estimate(currentNode, exitNode);

        assertEquals(expectedValue, actualValue);
    }
}