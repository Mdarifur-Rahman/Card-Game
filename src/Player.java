import java.util.*;

public abstract class Player {
    public static final int MAX_HAND_SIZE = 5;

    public abstract String getName();

    public abstract void addCard(Card c);

    public abstract boolean makeTrump(Card upcard, boolean isDealer, int round, Card.Suit[] orderUpSuit);

    public abstract void addAndDiscard(Card upcard);

    public abstract Card leadCard(Card.Suit trump);

    public abstract Card playCard(Card ledCard, Card.Suit trump);
}

class SimplePlayer extends Player {
    private final String name;
    private final List<Card> hand = new ArrayList<>();

    public SimplePlayer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addCard(Card c) {
        if (hand.size() >= MAX_HAND_SIZE)
            throw new IllegalStateException("Hand is full");
        hand.add(c);
    }

    @Override
    public boolean makeTrump(Card upcard, boolean isDealer, int round, Card.Suit[] orderUpSuit) {
        int count = 0;
        if (round == 1) {
            for (Card c : hand) {
                if (c.isFaceOrAce() && c.isTrump(upcard.getSuit())) {
                    count++;
                }
            }
            if (count >= 2) {
                orderUpSuit[0] = upcard.getSuit();
                return true;
            }
            return false;
        }

        Card.Suit nextSuit = upcard.getSuit().getNextSameColor();
        for (Card c : hand) {
            if (c.isFaceOrAce() && c.getSuit() == nextSuit) {
                count++;
            }
        }
        if (count >= 1 || isDealer) {
            orderUpSuit[0] = nextSuit;
            return true;
        }
        return false;
    }

    @Override
    public void addAndDiscard(Card upcard) {
        hand.add(upcard);
        Card least = hand.get(0);
        int index = 0;
        for (int i = 1; i < hand.size(); i++) {
            if (Card.cardLess(hand.get(i), least, upcard.getSuit())) {
                least = hand.get(i);
                index = i;
            }
        }
        hand.remove(index);
    }

    @Override
    public Card leadCard(Card.Suit trump) {
        int bestIdx = -1;
        for (int i = 0; i < hand.size(); i++) {
            if (!hand.get(i).isTrump(trump)) {
                if (bestIdx == -1 || hand.get(bestIdx).compareTo(hand.get(i)) < 0) {
                    bestIdx = i;
                }
            }
        }
        if (bestIdx != -1) {
            return hand.remove(bestIdx);
        }

        // all trump
        bestIdx = 0;
        for (int i = 1; i < hand.size(); i++) {
            if (Card.cardLess(hand.get(bestIdx), hand.get(i), trump)) {
                bestIdx = i;
            }
        }
        return hand.remove(bestIdx);
    }

    @Override
    public Card playCard(Card ledCard, Card.Suit trump) {
        Card.Suit ledSuit = ledCard.getSuit(trump);
        List<Integer> matchingIdx = new ArrayList<>();
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getSuit(trump) == ledSuit) {
                matchingIdx.add(i);
            }
        }

        if (!matchingIdx.isEmpty()) {
            int bestIdx = matchingIdx.get(0);
            for (int i : matchingIdx) {
                if (Card.cardLess(hand.get(bestIdx), hand.get(i), ledCard, trump)) {
                    bestIdx = i;
                }
            }
            return hand.remove(bestIdx);
        }

        // No cards match the led suit
        int leastIdx = 0;
        for (int i = 1; i < hand.size(); i++) {
            if (Card.cardLess(hand.get(i), hand.get(leastIdx), ledCard, trump)) {
                leastIdx = i;
            }
        }
        return hand.remove(leastIdx);
    }
}

class HumanPlayer extends Player {
    private final String name;
    private final List<Card> hand = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public HumanPlayer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addCard(Card c) {
        if (hand.size() >= MAX_HAND_SIZE)
            throw new IllegalStateException("Hand is full");
        hand.add(c);
        Collections.sort(hand);
    }

    @Override
    public boolean makeTrump(Card upcard, boolean isDealer, int round, Card.Suit[] orderUpSuit) {
        printHand();
        System.out.println("Human player " + name + ", please enter a suit, or \"pass\":");
        String input = scanner.next();
        if (!input.equalsIgnoreCase("pass")) {
            orderUpSuit[0] = Card.Suit.fromString(input);
            return true;
        }
        return false;
    }

    @Override
    public void addAndDiscard(Card upcard) {
        printHand();
        System.out.println("Discard upcard: [-1]");
        System.out.println("Human player " + name + ", please select a card to discard:");
        int index = scanner.nextInt();
        if (index == -1)
            return;
        hand.add(upcard);
        hand.remove(index);
        Collections.sort(hand);
    }

    @Override
    public Card leadCard(Card.Suit trump) {
        printHand();
        System.out.println("Human player " + name + ", please select a card:");
        int index = scanner.nextInt();
        return hand.remove(index);
    }

    @Override
    public Card playCard(Card ledCard, Card.Suit trump) {
        printHand();
        System.out.println("Human player " + name + ", please select a card:");
        int index = scanner.nextInt();
        return hand.remove(index);
    }

    private void printHand() {
        for (int i = 0; i < hand.size(); i++) {
            System.out.println("[" + i + "] " + hand.get(i));
        }
    }
}

class PlayerFactory {
    public static Player createPlayer(String name, String strategy) {
        switch (strategy) {
            case "Simple":
                return new SimplePlayer(name);
            case "Human":
                return new HumanPlayer(name);
            default:
                throw new IllegalArgumentException("Invalid player type: " + strategy);
        }
    }
}
