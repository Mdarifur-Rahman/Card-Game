import java.io.InputStream;
import java.util.*;

public class Game {
    private final List<Player> players;
    private final Pack pack;
    private final boolean shuffle;
    private final int pointsToWin;

    private int hand;
    private int score;
    private int trumpMaker;
    // private boolean isDealer;
    private Card upcard;
    private Card ledCard;
    private Card highestCard;
    private Card.Suit orderUpSuit;

    private final int[] wins = new int[4];
    private final int[] teamScore = new int[2];
    private final int[] scoreVec = new int[2];
    private final int[] trickWins = new int[2];

    public Game(InputStream packInput, String shuffleOption, int pointsToWin, List<Player> players) {
        this.players = players;
        this.pack = new Pack(packInput);
        this.shuffle = shuffleOption.equals("shuffle");
        this.pointsToWin = pointsToWin;
    }

    public void play() {
        hand = 0;
        score = 0;

        while (score < pointsToWin) {
            shuffling();
            System.out.println("Hand " + hand);
            System.out.println(players.get(hand % 4).getName() + " deals");

            deal(hand);
            upcard = pack.dealOne();
            System.out.println(upcard + " turned up");

            orderUp(upcard);
            trickTaking();
        }

        announceWinners();
    }

    private void shuffling() {
        if (shuffle) {
            pack.shuffle();
        }
        pack.reset();
    }

    private void deal(int i) {
        int[] dealPattern = { 3, 2, 3, 2, 2, 3, 2, 3 };
        int[] playerOrder = { (i + 1) % 4, (i + 2) % 4, (i + 3) % 4, i % 4,
                (i + 1) % 4, (i + 2) % 4, (i + 3) % 4, i % 4 };

        for (int d = 0; d < dealPattern.length; d++) {
            for (int j = 0; j < dealPattern[d]; j++) {
                players.get(playerOrder[d]).addCard(pack.dealOne());
            }
        }
    }

    private void orderUp(Card upcard) {
        boolean ordered = false;
        int played = 0;

        for (int i = (hand + 1) % 4; played < 4; i = (i + 1) % 4, played++) {
            // isDealer = false;
            Card.Suit[] tempSuit = new Card.Suit[1];

            if (players.get(i).makeTrump(upcard, false, 1, tempSuit)) {
                orderUpSuit = tempSuit[0];
                System.out.println(players.get(i).getName() + " orders up " + orderUpSuit);
                trumpMaker = i;
                ordered = true;
                break;
            }
            System.out.println(players.get(i).getName() + " passes");
        }

        if (ordered) {
            players.get(hand % 4).addAndDiscard(upcard);
        } else {
            played = 0;
            for (int i = (hand + 1) % 4; played < 4; i = (i + 1) % 4, played++) {
                boolean dealer = (i == hand % 4);
                Card.Suit[] tempSuit = new Card.Suit[1];

                if (players.get(i).makeTrump(upcard, dealer, 2, tempSuit)) {
                    orderUpSuit = tempSuit[0];
                    System.out.println(players.get(i).getName() + " orders up " + orderUpSuit);
                    trumpMaker = i;
                    break;
                } else {
                    System.out.println(players.get(i).getName() + " passes");
                }
            }
        }

        System.out.println();
    }

    private void trickTaking() {
        resetWins();
        int leader = (hand + 1) % 4;
        int winner = leader;

        for (int i = 0; i < 5; i++) {
            ledCard = players.get(winner).leadCard(orderUpSuit);
            highestCard = ledCard;

            System.out.println(ledCard + " led by " + players.get(winner).getName());

            for (int j = 1; j < 4; j++) {
                int idx = (leader + j) % 4;
                Card current = players.get(idx).playCard(ledCard, orderUpSuit);

                System.out.println(current + " played by " + players.get(idx).getName());

                if (Card.cardLess(highestCard, current, ledCard, orderUpSuit)) {
                    highestCard = current;
                    winner = idx;
                }
            }

            wins[winner]++;
            System.out.println(players.get(winner).getName() + " takes the trick\n");
            leader = winner;
        }

        countScore();
        hand++;
    }

    private void resetWins() {
        Arrays.fill(wins, 0);
        Arrays.fill(teamScore, 0);
    }

    private boolean isOrderUpTeam(int i) {
        return trumpMaker == i || trumpMaker == (i + 2) % 4;
    }

    private void countScore() {
        trickWins[0] = wins[0] + wins[2];
        trickWins[1] = wins[1] + wins[3];

        for (int i = 0; i < 2; i++) {
            if (trickWins[i] >= 3 && trickWins[i] <= 4 && isOrderUpTeam(i)) {
                teamScore[i] = 1;
                System.out.println(players.get(i).getName() + " and " + players.get(i + 2).getName() + " win the hand");
            } else if (trickWins[i] == 5 && isOrderUpTeam(i)) {
                teamScore[i] = 2;
                System.out.println(
                        players.get(i).getName() + " and " + players.get(i + 2).getName() + " win the hand\nmarch!");
            } else if (trickWins[i] >= 3 && !isOrderUpTeam(i)) {
                teamScore[i] = 2;
                System.out.println(
                        players.get(i).getName() + " and " + players.get(i + 2).getName() + " win the hand\neuchred!");
            }

            scoreVec[i] += teamScore[i];
        }

        score = Math.max(scoreVec[0], scoreVec[1]);

        System.out.println(
                players.get(0).getName() + " and " + players.get(2).getName() + " have " + scoreVec[0] + " points");
        System.out.println(
                players.get(1).getName() + " and " + players.get(3).getName() + " have " + scoreVec[1] + " points\n");
    }

    private void announceWinners() {
        if (scoreVec[0] > scoreVec[1]) {
            System.out.println(players.get(0).getName() + " and " + players.get(2).getName() + " win!");
        } else {
            System.out.println(players.get(1).getName() + " and " + players.get(3).getName() + " win!");
        }
    }
}
