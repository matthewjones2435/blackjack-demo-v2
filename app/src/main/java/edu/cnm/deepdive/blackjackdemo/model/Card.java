package edu.cnm.deepdive.blackjackdemo.model;

import com.google.gson.annotations.SerializedName;
import java.net.URL;

public class Card implements Comparable<Card> {

  @SerializedName("value")
  private final Rank rank;
  private final Suit suit;
  @SerializedName("image")
  private final URL imageUrl;

  public Card(Rank rank, Suit suit, URL imageUrl) {
    this.rank = rank;
    this.suit = suit;
    this.imageUrl = imageUrl;
  }

  public Rank getRank() {
    return rank;
  }

  public Suit getSuit() {
    return suit;
  }

  public URL getImageUrl() {
    return imageUrl;
  }

  @Override
  public String toString() {
    return String.format("%s%s", rank.abbreviation(), suit.abbreviation());
  }

  @Override
  public int compareTo(Card other) {
    int result = suit.getColor().compareTo(other.suit.getColor());
    if (result == 0) {
      result = suit.compareTo(other.suit);
      if (result == 0) {
        result = rank.compareTo(other.rank);
      }
    }
    return result;
  }

  public int getSoftValue() {
    return rank.getSoftValue();
  }

  public int getHardValue() {
    return rank.getHardValue();
  }

}
