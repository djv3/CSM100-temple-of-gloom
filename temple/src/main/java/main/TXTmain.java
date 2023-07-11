package main;

import game.GameState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Runs the program via the text (console) interface.
 */
public class TXTmain {
    public static void main(String[] args) {
        List<String> argList = new ArrayList<>(Arrays.asList(args));
        int repeatNumberIndex = argList.indexOf("-n");
        int numTimesToRun = 1;
        if (repeatNumberIndex >= 0) {
            try {
                numTimesToRun = Math.max(Integer.parseInt(argList.get(repeatNumberIndex + 1)), 1);
            } catch (Exception e) {
                System.err.println("Couldn't parse argument for -n option");
            }
        }
        Optional<Long> seed = Utilities.parseSeedArgs(args);

        int totalScore = 0;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("logger.csv"));
            writer.write("Seed,Algorithm used,Gold on map,Gold collected,Escape succeeded,Escape errored");
            writer.close();
        } catch (IOException _e) {
            System.out.println("There was a problem logging the run results.");
        }

        for (int i = 0; i < numTimesToRun; i++) {
            totalScore += GameState.runNewGame((seed.isPresent() ? seed.get() : 0), false);
            System.out.println();
        }

        if (totalScore != 0) {
            System.out.println("Average score : " + totalScore / numTimesToRun);
        }
    }
}
