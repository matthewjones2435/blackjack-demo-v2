package edu.cnm.deepdive.blackjackdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import edu.cnm.deepdive.blackjackdemo.R;
import edu.cnm.deepdive.blackjackdemo.model.Card;
import edu.cnm.deepdive.blackjackdemo.view.HandAdapter.CardHolder;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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

    public CardHolder(@NonNull View itemView) {
      super(itemView);
      this.itemView = (ImageView) itemView;
    }

    private void bind(Card card) {
      new LoadImageTask().execute(card.getImageUrl());
    }

    private class LoadImageTask extends AsyncTask<URL, Void, Bitmap> {

      @Override
      protected void onPostExecute(Bitmap bitmap) {
        itemView.setImageBitmap(bitmap);
      }

      @Override
      protected Bitmap doInBackground(URL... urls) {
        try {
          return BitmapFactory.decodeStream(urls[0].openConnection().getInputStream());
        } catch (IOException e) {
          e.printStackTrace(); // FIXME We need to do something smart here.
          throw new RuntimeException(e);
        }
      }
    }

  }

}
