package edu.cnm.deepdive.blackjackdemo.service;

import androidx.annotation.Nullable;
import edu.cnm.deepdive.android.FluentAsyncTask;
import edu.cnm.deepdive.blackjackdemo.BuildConfig;
import edu.cnm.deepdive.blackjackdemo.model.Deck;
import edu.cnm.deepdive.blackjackdemo.model.Draw;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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

  class InstanceHolder {

    private static final DeckOfCardsService INSTANCE;

    static {
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(BuildConfig.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .build();
      INSTANCE = retrofit.create(DeckOfCardsService.class);
    }

  }

  class CreateDeckTask extends FluentAsyncTask<Integer, Void, Deck> {

    @Nullable
    @Override
    protected Deck perform(Integer... values) {
      Deck deck = null;
      int decksInShoe = values[0];
      try {
        deck = InstanceHolder.INSTANCE.newDeck(decksInShoe).execute().body();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return deck;
    }

  }

  class ShuffleDeckTask extends FluentAsyncTask<Deck, Void, Void> {

    @Nullable
    @Override
    protected Void perform(Deck... decks) {
      try {
        InstanceHolder.INSTANCE.shuffle(decks[0].getId()).execute();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return null;
    }

  }

  class DrawCardsTask extends FluentAsyncTask<Integer, Void, Draw> {

    private Deck deck;

    public DrawCardsTask(Deck deck) {
      this.deck = deck;
    }

    @Nullable
    @Override
    protected Draw perform(Integer... values) {
      int cardsToDraw = values[0];
      Draw draw = null;
      try {
        draw = InstanceHolder.INSTANCE.draw(deck.getId(), cardsToDraw).execute().body();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return draw;

    }

  }

}
