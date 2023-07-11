package main;

import static org.junit.jupiter.api.Assertions.*;

import game.GameState;
import java.util.Arrays;
import lombok.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;

class TXTmainTest {

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @RepeatedTest(24)
  void givenSeed_whenRun_ThenReturnScore(RepetitionInfo repetitionInfo) {
    var seeds =
        Arrays.asList(
            new LeagueResult(-4152836868077314850L, 47912, 1.26, 60134),
            new LeagueResult(-3967848802208875438L, 48597, 1.17, 56887),
            new LeagueResult(5864101433891852061L, 42769, 1.24, 52898),
            new LeagueResult(7445652272991402161L, 43477, 1.2, 52289),
            new LeagueResult(8781946738346443336L, 40091, 1.28, 51174),
            new LeagueResult(-8753562310865996698L, 41402, 1.22, 50372),
            new LeagueResult(-757868709594414956L, 38420, 1.29, 49720),
            new LeagueResult(-5747184872657058727L, 37534, 1.28, 47982),
            new LeagueResult(-7761980840912806448L, 40881, 1.17, 47837),
            new LeagueResult(-4501867144509231625L, 36893, 1.29, 47755),
            new LeagueResult(9178600685835736767L, 37206, 1.28, 47670),
            new LeagueResult(8849755165154918804L, 37967, 1.25, 47637),
            new LeagueResult(-8795875982559746259L, 37748, 1.26, 47483),
            new LeagueResult(8207908124709091172L, 37165, 1.26, 46776),
            new LeagueResult(4218948394500449828L, 36480, 1.27, 46402),
            new LeagueResult(3694314465540184459L, 37244, 1.23, 45988),
            new LeagueResult(-5936268151507118028L, 35483, 1.27, 45142),
            new LeagueResult(-4779223688972917879L, 34216, 1.29, 44300),
            new LeagueResult(-8522969912440837840L, 33997, 1.3, 44196),
            new LeagueResult(6496594554013205192L, 34103, 1.29, 44120),
            new LeagueResult(6629009396325103285L, 35320, 1.25, 44084),
            new LeagueResult(2832876979625815005L, 35962, 1.22, 43873),
            new LeagueResult(-5845531988250598653L, 43564, 1, 43564),
            new LeagueResult(-1906048792819286095L, 43164, 1, 43164));
    int repetition = repetitionInfo.getCurrentRepetition();
    long seed = seeds.get(repetition - 1).getSeed();
    int score = seeds.get(repetition - 1).getScore();
    int result = GameState.runNewGame(seed, false);
    assertTrue(result >= score / 2);
  }
}

@Data
class LeagueResult {
  public long seed;
  public int gold;
  public double bonus;
  public int score;

  public LeagueResult(long seed, int gold, double bonus, int score) {
    this.seed = seed;
    this.gold = gold;
    this.bonus = bonus;
    this.score = score;
  }
}
