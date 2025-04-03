# Euchre ♠️♥️♣️♦️

Euchre is a fast-paced, trick-taking card game played with four players in teams of two. This project is a Java-based implementation of the classic game, following the rules used in [this guide](https://bicyclecards.com/how-to-play/euchre/)

---

## 🧠 How to Play

- **Players:** 4 (2 teams: players 0 & 2 vs 1 & 3)
- **Deck:** 24 cards (9–A in each suit)
- **Goal:** Be the first team to reach a specified number of points

### 🃏 Gameplay Flow

1. **Deal**: Each player is dealt 5 cards.
2. **Upcard**: One card is turned up from the deck to determine trump.
3. **Trump Selection**: Players go around twice:
   - **Round 1**: Choose to accept the upcard's suit as trump.
   - **Round 2**: Choose any other suit (except the upcard).
4. **Dealer picks up the upcard** if trump is accepted in round 1.
5. **Trick Taking**:
   - Each player plays one card per trick (starting with the player after the dealer).
   - Players must follow suit if possible.
   - The highest card in the led suit or trump wins the trick.
6. **Scoring**:
   - Team that ordered trump:
     - 3–4 tricks = 1 point
     - 5 tricks = 2 points (march)
   - Defending team wins 3+ = 2 points (euchred)

---

## ♟️ Strategies & Player Types

- **Simple Player**: Uses basic logic to make decisions (e.g., orders trump with face cards, plays highest led-suit card).
- **Human Player**: Interactive mode where you select trump and play cards manually.
- **Trump Strategy**: Players want to declare trump when they have strong cards in that suit.

---

## 🎮 Ways to Play

You can simulate different types of games:
- All **Simple Players**: Run a game with no interaction, good for automated testing.
- Mix of **Simple** and **Human** players: Take control of one or more players and play manually.
- Try different **point goals** (1–100) to test short vs long games.
- Run with or without **shuffling** to test deterministic behavior.

---

## 🚀 Running This Java Version (Example)

```bash
javac src/*.java
java -cp src Main dummy pack.txt noshuffle 5 Alice Simple Bob Simple Charlie Simple Dana Simple
