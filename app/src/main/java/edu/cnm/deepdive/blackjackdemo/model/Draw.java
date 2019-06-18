package edu.cnm.deepdive.blackjackdemo.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Draw {

  private final List<Card> cards;
  private final int remaining;
  @SerializedName("success")
  private final boolean successful;

  public Draw(List<Card> cards, int remaining, boolean successful) {
    this.cards = cards;
    this.remaining = remaining;
    this.successful = successful;
  }

  public List<Card> getCards() {
    return cards;
  }

  public int getRemaining() {
    return remaining;
  }

  public boolean isSuccessful() {
    return successful;
  }

}
