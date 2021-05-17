package dicepoker.gamestate;

/**
 * Enum class that represents a <a href="https://en.wikipedia.org/wiki/Poker_dice">Poker hand</a>
 * and it's weight compared to the other's for determining the thrown value.
 */
public enum Hand {

    BUST ("Bust", 0),
    ONE_PAIR ("One pair", 1000),
    TWO_PAIR ("Two pair", 2000),
    THREE_OF_A_KIND ("Three of a kind", 3000),
    SMALL_STRAIGHT ("Small straight", 4000),
    BIG_STRAIGHT ("Big straight", 5000),
    FULL_HOUSE ("Full house", 6000),
    FOUR_OF_A_KIND ("Four of a kind", 7000),
    FIVE_OF_A_KIND ("Five of a kind", 8000);

    public String name;
    public int baseValue;

    Hand(String name, int baseValue) {
        this.name = name;
        this.baseValue = baseValue;
    }
}
