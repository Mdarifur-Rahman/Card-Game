import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("args.length = " + args.length);
        System.out.println(Arrays.toString(args));

        if (args.length != 12) { // C++ argc includes program name
            printUsageAndExit();
        }

        // Shift all indices by 1 to mimic C++ argv[1] = pack.txt
        String packFilename = args[1];
        String shuffleOption = args[2];
        int pointsToWin;

        try {
            pointsToWin = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            printUsageAndExit();
            return;
        }

        if (pointsToWin < 1 || pointsToWin > 100 ||
                !(shuffleOption.equals("shuffle") || shuffleOption.equals("noshuffle"))) {
            printUsageAndExit();
        }

        List<Player> players = new ArrayList<>();
        for (int i = 4; i < args.length; i += 2) {
            String name = args[i];
            String type = args[i + 1];
            if (!(type.equals("Simple") || type.equals("Human"))) {
                printUsageAndExit();
            }
            players.add(PlayerFactory.createPlayer(name, type));
        }

        try (FileInputStream fin = new FileInputStream(packFilename)) {
            System.out.print("./euchre.exe ");
            for (int i = 1; i < args.length; i++) {
                System.out.print(args[i] + " ");
            }
            System.out.println();

            Game game = new Game(fin, shuffleOption, pointsToWin, players);
            game.play();

        } catch (IOException e) {
            System.out.println("Error opening " + packFilename);
            System.exit(1);
        }
    }

    private static void printUsageAndExit() {
        System.out.println("Usage: euchre.exe PACK_FILENAME [shuffle|noshuffle] " +
                "POINTS_TO_WIN NAME1 TYPE1 NAME2 TYPE2 NAME3 TYPE3 NAME4 TYPE4");
        System.exit(1);
    }
}
