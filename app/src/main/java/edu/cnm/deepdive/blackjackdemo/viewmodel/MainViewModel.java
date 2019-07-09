package edu.cnm.deepdive.blackjackdemo.viewmodel;

import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.blackjackdemo.model.Card;
import edu.cnm.deepdive.blackjackdemo.model.Deck;
import edu.cnm.deepdive.blackjackdemo.model.Draw;
import edu.cnm.deepdive.blackjackdemo.model.Hand;
import edu.cnm.deepdive.blackjackdemo.service.DeckOfCardsService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class MainViewModel extends ViewModel implements LifecycleObserver {

  private static final int DECKS_IN_SHOE = 6;
  private static final int INITIAL_DRAW = 2;

  private MutableLiveData<Deck> deck = new MutableLiveData<>();
  private MutableLiveData<Hand> hand = new MutableLiveData<>();
  private MutableLiveData<List<Card>> cards = new MutableLiveData<>();
  private CompositeDisposable pending = new CompositeDisposable();

  {
    createDeck();
  }

  public LiveData<Deck> getDeck() {
    return deck;
  }
  public LiveData<Hand> getHand() {
    return hand;
  }
  public LiveData<List<Card>> getCards() {
    return cards;
  }

  @OnLifecycleEvent(Event.ON_STOP)
  public void disposePending () {
    pending.clear();
  }

  public void shuffle() {
    pending.add(
        DeckOfCardsService.getInstance().shuffle(deck.getValue().getId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(d -> deal())
    );
  }

  public void deal() {
    hand.setValue(new Hand());
    draw(INITIAL_DRAW);
  }

  public void draw() {
    draw(1);
  }

  private void draw(int numCards) {
    if(hand.getValue().getScore() < 21) {
      pending.add(
          DeckOfCardsService.getInstance().draw(deck.getValue().getId(), numCards)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(this::addToHand)
      );
    }
  }

  private void createDeck() {
    pending.add(
        DeckOfCardsService.getInstance().newDeck(DECKS_IN_SHOE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe((deck) -> {
              this.deck.setValue(deck);
              deal();
            })
    );
  }

  private void addToHand(Draw draw) {
    Hand hand = getHand().getValue();
    for (Card card : draw.getCards()) {
      hand.addCard(card);
    }

    cards.setValue(hand.getCards());
  }
}
