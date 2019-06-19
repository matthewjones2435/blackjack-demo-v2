package edu.cnm.deepdive.blackjackdemo;

import android.app.Application;
import com.squareup.picasso.Picasso;

public class BlackjackDemoApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Picasso.setSingletonInstance(new Picasso.Builder(this).build());
  }

}
