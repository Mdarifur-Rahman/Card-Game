import java.io.InputStream;
import java.util.Scanner;

public class Pack {
    public static final int PACK_SIZE = 24;
    private Card[] cards;
    private int next;

    public Pack() {
        cards = new Card[PACK_SIZE];
        next = 0;
        int index = 0;
        for (Card.Suit suit : Card.Suit.values()) {
            if (suit.ordinal() > 3)
                break; // Defensive, in case more suits added
            for (Card.Rank rank : Card.Rank.values()) {
                if (rank.ordinal() >= Card.Rank.NINE.ordinal()) {
                    cards[index++] = new Card(rank, suit);
                }
            }
        }
    }

    // REQUIRES: input is in format "Nine of Spades" etc.
    public Pack(InputStream inputStream) {
        cards = new Card[PACK_SIZE];
        next = 0;
        Scanner scanner = new Scanner(inputStream);
        for (int i = 0; i < PACK_SIZE; i++) {
            String rankStr = scanner.next();
            scanner.next(); // "of"
            String suitStr = scanner.next();
            cards[i] = new Card(Card.Rank.fromString(rankStr), Card.Suit.fromString(suitStr));
        }
        scanner.close();
    }

    public Card dealOne() {
        return cards[next++];
    }

    public void reset() {
        next = 0;
    }

    public void shuffle() {
        for (int j = 0; j < 7; ++j) {
            Card[] temp1 = new Card[PACK_SIZE / 2];
            Card[] temp2 = new Card[PACK_SIZE / 2];
            for (int i = 0; i < PACK_SIZE / 2; i++) {
                temp1[i] = cards[i];
                temp2[i] = cards[PACK_SIZE / 2 + i];
            }
            int idx = 0;
            for (int i = 0; i < PACK_SIZE; i += 2) {
                cards[i] = temp2[idx];
                cards[i + 1] = temp1[idx];
                idx++;
            }
        }
    }

    public boolean isEmpty() {
        return next >= PACK_SIZE;
    }
}
