package edu.cnm.deepdive.blackjackdemo.controller;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import edu.cnm.deepdive.android.FluentAsyncTask;
import edu.cnm.deepdive.blackjackdemo.R;
import edu.cnm.deepdive.blackjackdemo.model.Card;
import edu.cnm.deepdive.blackjackdemo.model.Deck;
import edu.cnm.deepdive.blackjackdemo.model.Draw;
import edu.cnm.deepdive.blackjackdemo.model.Hand;
import edu.cnm.deepdive.blackjackdemo.service.DeckOfCardsService;
import edu.cnm.deepdive.blackjackdemo.view.HandAdapter;
import java.io.IOException;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

  private static final int DECKS_IN_SHOE = 6;

  private Deck deck;
  private Hand hand;
  private DeckOfCardsService service;
  private RecyclerView handView;
  private HandAdapter handAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupToolbar();
    setupFloatingActionButton();
    setupRecyclerView();
    setupService();
    setupDeck();
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
        shuffleDeck();
        break;
      case R.id.deal_hand:
        setupHand();
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
  }

  private void setupToolbar() {
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  private void setupFloatingActionButton() {
    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener((view) -> drawCards(1));
  }

  private void setupRecyclerView() {
    Resources res = getResources();
    handView = findViewById(R.id.hand_view);
    handView.addItemDecoration(new HandAdapter.OverlapDecoration(
        (int) res.getDimension(R.dimen.card_horizontal_spacing),
        (int) res.getDimension(R.dimen.card_vertical_spacing)));
    handView.setLayoutManager(new LinearLayoutManager(this));
  }

  private void setupService() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(getString(R.string.base_url))
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    service = retrofit.create(DeckOfCardsService.class);
  }

  private void setupDeck() {
    new FluentAsyncTask<Integer, Void, Void>()
        .setPerformer((values) -> {
          int decksInShoe = values[0];
          try {
            deck = service.newDeck(decksInShoe).execute().body();
            return null;
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        })
        .setOnSuccessListener((ignore) -> setupHand())
        .execute(DECKS_IN_SHOE);
  }

  private void shuffleDeck() {
    new FluentAsyncTask<Void, Void, Void>()
        .setPerformer((values) -> {
          try {
            service.shuffle(deck.getId()).execute();
            return null;
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        })
        .setOnSuccessListener((ignore) -> setupHand())
        .execute();
  }

  private void setupHand() {
    hand = new Hand();
    handAdapter = new HandAdapter(MainActivity.this, hand.getCards());
    handView.setAdapter(handAdapter);
    drawCards(2);
  }

  private void drawCards(int numCards) {
    new FluentAsyncTask<Integer, Void, Draw>()
        .setPerformer((values) -> {
          int cardsToDraw = values[0];
          try {
            return service.draw(deck.getId(), cardsToDraw).execute().body();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        })
        .setOnSuccessListener((draw) -> {
          int startPosition = hand.getCards().size();
          for (Card card : draw.getCards()) {
            hand.addCard(card);
          }
          handAdapter.notifyItemRangeInserted(startPosition, draw.getCards().size());
        })
        .execute(numCards);
  }

}
