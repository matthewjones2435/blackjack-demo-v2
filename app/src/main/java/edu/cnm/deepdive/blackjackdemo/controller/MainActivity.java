package edu.cnm.deepdive.blackjackdemo.controller;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import edu.cnm.deepdive.blackjackdemo.R;
import edu.cnm.deepdive.blackjackdemo.model.Card;
import edu.cnm.deepdive.blackjackdemo.model.Deck;
import edu.cnm.deepdive.blackjackdemo.model.Hand;
import edu.cnm.deepdive.blackjackdemo.service.DeckOfCardsService.CreateDeckTask;
import edu.cnm.deepdive.blackjackdemo.service.DeckOfCardsService.DrawCardsTask;
import edu.cnm.deepdive.blackjackdemo.service.DeckOfCardsService.ShuffleDeckTask;
import edu.cnm.deepdive.blackjackdemo.view.HandAdapter;

public class MainActivity extends AppCompatActivity {

  private static final int DECKS_IN_SHOE = 6;

  private Deck deck;
  private Hand hand;
  private RecyclerView handView;
  private HandAdapter handAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupToolbar();
    setupFloatingActionButton();
    setupRecyclerView();
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

  private void setupDeck() {
    new CreateDeckTask()
        .setOnCompletionListener((deck) -> this.deck = deck)
        .setOnSuccessListener((ignored) -> setupHand())
        .execute(DECKS_IN_SHOE);
  }

  private void shuffleDeck() {
    new ShuffleDeckTask()
        .setOnSuccessListener(
            (ignored) -> setupHand()
        )
        .execute(deck);
  }

  private void setupHand() {
    hand = new Hand();
    handAdapter = new HandAdapter(MainActivity.this, hand.getCards());
    handView.setAdapter(handAdapter);
    drawCards(2);
  }

  private void drawCards(int numCards) {
    new DrawCardsTask(deck)
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
