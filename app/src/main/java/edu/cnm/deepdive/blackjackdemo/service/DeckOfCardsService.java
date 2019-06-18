package edu.cnm.deepdive.blackjackdemo.service;

import edu.cnm.deepdive.blackjackdemo.model.Deck;
import edu.cnm.deepdive.blackjackdemo.model.Draw;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DeckOfCardsService {

  @GET("deck/new/shuffle")
  Call<Deck> newDeck(@Query("deck_count") int count);

  @GET("deck/{deckId}/draw")
  Call<Draw> draw(@Path("deckId") String deckId, @Query("count") int count);

  @GET("deck/{deckId}/shuffle")
  Call<Deck> shuffle(@Path("deckId") String deckId);

}
