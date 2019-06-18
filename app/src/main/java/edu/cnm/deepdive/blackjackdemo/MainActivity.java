package edu.cnm.deepdive.blackjackdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import edu.cnm.deepdive.blackjackdemo.model.Card;
import edu.cnm.deepdive.blackjackdemo.model.Deck;
import edu.cnm.deepdive.blackjackdemo.model.Draw;
import edu.cnm.deepdive.blackjackdemo.model.Hand;
import edu.cnm.deepdive.blackjackdemo.service.DeckOfCardsService;
import edu.cnm.deepdive.blackjackdemo.view.HandAdapter;
import java.io.IOException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

  private static final int DECKS_IN_SHOE = 6;

  private Deck deck;
  private Hand hand;
  private DeckOfCardsService service;
  private RecyclerView handView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener((view) -> {
      // TODO Handle click by drawing a card.
    });
    setupService();
    handView = findViewById(R.id.hand_view);
    new CreateDeckTask().execute(DECKS_IN_SHOE);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.shuffle_deck:
        // TODO Shuffle deck.
        break;
      case R.id.deal_hand:
        // TODO Deal a new hand.
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  private void setupService() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    service = retrofit.create(DeckOfCardsService.class);
  }

  private class CreateDeckTask extends AsyncTask<Integer, Void, Deck> {

    @Override
    protected void onPostExecute(Deck deck) {
      if (deck != null) {
        MainActivity.this.deck = deck;
        hand = new Hand();
        new DrawCardsTask(deck).execute(2);
      }
    }

    @Override
    protected Deck doInBackground(Integer... values) {
      int decksInShoe = values[0];
      Deck deck = null;
      try {
        Response<Deck> response = service.newDeck(decksInShoe).execute();
        if (response.isSuccessful()) {
          deck = response.body();
        }
      } catch (IOException e) {
        // deck is already null, which is fine.
        cancel(true);
      }
      return deck;
    }

  }

  private class DrawCardsTask extends AsyncTask<Integer, Void, Draw> {

    private Deck deck;

    public DrawCardsTask(Deck deck) {
      this.deck = deck;
    }

    @Override
    protected void onPostExecute(Draw draw) {
      for (Card card : draw.getCards()) {
        hand.addCard(card);
      }
      // TODO Make this smarter.
      HandAdapter adapter = new HandAdapter(MainActivity.this, hand.getCards());
      handView.setAdapter(adapter);
    }

    @Override
    protected Draw doInBackground(Integer... values) {
      int cardsToDraw = values[0];
      Draw draw = null;
      try {
        Response<Draw> response = service.draw(deck.getId(), cardsToDraw).execute();
        if (response.isSuccessful()) {
          draw = response.body();
        }
      } catch (IOException e) {
        cancel(true);
      }
      return draw;
    }

  }

}
