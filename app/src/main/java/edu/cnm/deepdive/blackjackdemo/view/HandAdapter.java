package edu.cnm.deepdive.blackjackdemo.view;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.State;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.blackjackdemo.R;
import edu.cnm.deepdive.blackjackdemo.model.Card;
import edu.cnm.deepdive.blackjackdemo.view.HandAdapter.CardHolder;
import java.util.List;

public class HandAdapter extends RecyclerView.Adapter<CardHolder> {

  private Context context;
  private List<Card> cards;

  public HandAdapter(Context context, List<Card> cards) {
    this.context = context;
    this.cards = cards;
  }

  @NonNull
  @Override
  public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    CardHolder holder;
    View itemView = LayoutInflater.from(context).inflate(R.layout.card_list_item, parent, false);
    holder = new CardHolder(itemView);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull CardHolder holder, int position) {
    holder.bind(cards.get(position));
  }

  @Override
  public int getItemCount() {
    return cards.size();
  }

  class CardHolder extends RecyclerView.ViewHolder {

    private ImageView itemView;

    private CardHolder(@NonNull View itemView) {
      super(itemView);
      this.itemView = (ImageView) itemView;
    }

    private void bind(Card card) {
      Picasso.get().load(card.getImageUrl().toString()).into(itemView);
    }

  }

  public static class OverlapDecoration extends RecyclerView.ItemDecoration {

    private final int verticalOverlap;
    private final int horizontalOverlap;

    public OverlapDecoration() {
      this(0, 0);
    }

    public OverlapDecoration(int horizontalOverlap, int verticalOverlap) {
      this.horizontalOverlap = horizontalOverlap;
      this.verticalOverlap = verticalOverlap;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
        @NonNull RecyclerView parent, @NonNull State state) {
      final int itemPosition = parent.getChildAdapterPosition(view);
      if (itemPosition == 0) {
        super.getItemOffsets(outRect, view, parent, state);
      } else {
        outRect.set(horizontalOverlap * itemPosition, verticalOverlap, 0, 0);
      }
    }

  }

}
