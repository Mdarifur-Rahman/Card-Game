import java.util.Objects;

public class Card implements Comparable<Card> {
    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
        TEN, JACK, QUEEN, KING, ACE;

        @Override
        public String toString() {
            switch (this) {
                case TWO:
                    return "Two";
                case THREE:
                    return "Three";
                case FOUR:
                    return "Four";
                case FIVE:
                    return "Five";
                case SIX:
                    return "Six";
                case SEVEN:
                    return "Seven";
                case EIGHT:
                    return "Eight";
                case NINE:
                    return "Nine";
                case TEN:
                    return "Ten";
                case JACK:
                    return "Jack";
                case QUEEN:
                    return "Queen";
                case KING:
                    return "King";
                case ACE:
                    return "Ace";
                default:
                    throw new IllegalArgumentException("Invalid rank");
            }
        }

        public static Rank fromString(String str) {
            for (Rank r : Rank.values()) {
                if (r.toString().equals(str)) {
                    return r;
                }
            }
            throw new IllegalArgumentException("Invalid rank: " + str);
        }
    }

    public enum Suit {
        SPADES, HEARTS, CLUBS, DIAMONDS;

        @Override
        public String toString() {
            switch (this) {
                case SPADES:
                    return "Spades";
                case HEARTS:
                    return "Hearts";
                case CLUBS:
                    return "Clubs";
                case DIAMONDS:
                    return "Diamonds";
                default:
                    throw new IllegalArgumentException("Invalid suit");
            }
        }

        public static Suit fromString(String str) {
            for (Suit s : Suit.values()) {
                if (s.toString().equals(str)) {
                    return s;
                }
            }
            throw new IllegalArgumentException("Invalid suit: " + str);
        }

        public Suit getNextSameColor() {
            switch (this) {
                case SPADES:
                    return CLUBS;
                case CLUBS:
                    return SPADES;
                case HEARTS:
                    return DIAMONDS;
                case DIAMONDS:
                    return HEARTS;
                default:
                    throw new IllegalArgumentException("Invalid suit");
            }
        }
    }

    private Rank rank;
    private Suit suit;

    public Card() {
        this.rank = Rank.TWO;
        this.suit = Suit.SPADES;
    }

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Suit getSuit(Suit trump) {
        return isLeftBower(trump) ? trump : suit;
    }

    public boolean isFaceOrAce() {
        return rank.ordinal() >= Rank.JACK.ordinal();
    }

    public boolean isRightBower(Suit trump) {
        return rank == Rank.JACK && suit == trump;
    }

    public boolean isLeftBower(Suit trump) {
        return rank == Rank.JACK && suit == trump.getNextSameColor();
    }

    public boolean isTrump(Suit trump) {
        return suit == trump || (rank == Rank.JACK && suit == trump.getNextSameColor());
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    public static Card fromString(String str) {
        String[] parts = str.split(" of ");
        Rank r = Rank.fromString(parts[0]);
        Suit s = Suit.fromString(parts[1]);
        return new Card(r, s);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Card))
            return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

    @Override
    public int compareTo(Card other) {
        int rankCmp = Integer.compare(this.rank.ordinal(), other.rank.ordinal());
        return (rankCmp != 0) ? rankCmp : this.suit.ordinal() - other.suit.ordinal();
    }

    public static boolean cardLess(Card a, Card b, Suit trump) {
        if (a.isRightBower(trump))
            return false;
        if (b.isRightBower(trump))
            return true;
        if (a.isLeftBower(trump) && b.isRightBower(trump))
            return true;
        if (b.isLeftBower(trump) && !a.isTrump(trump))
            return true;
        if (a.isLeftBower(trump) && !b.isTrump(trump))
            return false;
        if (b.isTrump(trump) && !a.isTrump(trump))
            return true;
        if (a.isTrump(trump) && !b.isTrump(trump))
            return false;
        return a.compareTo(b) < 0;
    }

    public static boolean cardLess(Card a, Card b, Card ledCard, Suit trump) {
        Suit led = ledCard.getSuit(trump);
        if (a.isRightBower(trump))
            return false;
        if (b.isRightBower(trump))
            return true;
        if (a.isLeftBower(trump))
            return false;
        if (b.isLeftBower(trump) && !a.isTrump(trump))
            return true;
        if (b.isTrump(trump) && !a.isTrump(trump))
            return true;
        if (b.getSuit(trump) == led && a.getSuit(trump) != led && !a.isTrump(trump))
            return true;
        if (a.getSuit(trump) == led && !b.isTrump(trump) && b.getSuit() != led)
            return false;

        boolean aIsLed = a.getSuit(trump) == led;
        boolean bIsLed = b.getSuit(trump) == led;

        if (aIsLed && bIsLed)
            return a.compareTo(b) < 0;
        if (a.isTrump(trump) && !b.isTrump(trump))
            return false;

        return a.compareTo(b) < 0;
    }
}
