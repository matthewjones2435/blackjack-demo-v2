package edu.cnm.deepdive.blackjackdemo.model;

import com.google.gson.annotations.SerializedName;

public class Deck {

  @SerializedName("deck_id")
  private final String id;
  private int remaining;

  public Deck(String id, int remaining) {
    this.id = id;
    this.remaining = remaining;
  }

  public String getId() {
    return id;
  }

  public int getRemaining() {
    return remaining;
  }

  public void setRemaining(int remaining) {
    this.remaining = remaining;
  }

}
