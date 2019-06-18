package edu.cnm.deepdive.blackjackdemo.model;

import com.google.gson.annotations.SerializedName;

public enum Rank {

  ACE(1, 11),
  @SerializedName("2")
  TWO(2),
  @SerializedName("3")
  THREE(3),
  @SerializedName("4")
  FOUR(4),
  @SerializedName("5")
  FIVE(5),
  @SerializedName("6")
  SIX(6),
  @SerializedName("7")
  SEVEN(7),
  @SerializedName("8")
  EIGHT(8),
  @SerializedName("9")
  NINE(9),
  @SerializedName("10")
  TEN(10),
  JACK(10),
  QUEEN(10),
  KING(10);

  private final int hardValue;
  private final int softValue;

  Rank(int value) {
    hardValue = value;
    softValue = value;
  }

  Rank(int hardValue, int softValue) {
    this.hardValue = hardValue;
    this.softValue = softValue;
  }

  public int getHardValue() {
    return hardValue;
  }

  public int getSoftValue() {
    return softValue;
  }

  private static final String[] ABBREVIATIONS = {
      "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"
  };

  public String abbreviation() {
    return ABBREVIATIONS[ordinal()];  
  }
  
}
