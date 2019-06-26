package edu.cnm.deepdive.blackjackdemo.controller;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import edu.cnm.deepdive.blackjackdemo.R;
import edu.cnm.deepdive.blackjackdemo.model.Card;
import edu.cnm.deepdive.blackjackdemo.model.Hand;
import edu.cnm.deepdive.blackjackdemo.view.HandAdapter;
import edu.cnm.deepdive.blackjackdemo.viewmodel.MainViewModel;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private FloatingActionButton fab;
  private RecyclerView handView;
  private MainViewModel model;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setupToolbar();
    setupFloatingActionButton();
    setupRecyclerView();
    setupViewModel();
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
        model.shuffle();
        break;
      case R.id.deal_hand:
        model.deal();
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
    fab = findViewById(R.id.fab);
    fab.setOnClickListener((view) -> model.draw(1));
  }

  private void setupRecyclerView() {
    Resources res = getResources();
    handView = findViewById(R.id.hand_view);
    handView.addItemDecoration(new HandAdapter.OverlapDecoration(
        (int) res.getDimension(R.dimen.card_horizontal_spacing),
        (int) res.getDimension(R.dimen.card_vertical_spacing)));
    handView.setLayoutManager(new LinearLayoutManager(this));
  }

  private void setupViewModel() {
    model = ViewModelProviders.of(this).get(MainViewModel.class);
    model.getCards().observe(this, this::updateCards);
  }

  private void updateCards(List<Card> cards) {
    HandAdapter handAdapter = new HandAdapter(this, cards);
    handView.setAdapter(handAdapter);
    if (model.getHand().getValue().getScore() >= 21) {
      fab.hide();
    } else {
      fab.show();
    }
  }
}