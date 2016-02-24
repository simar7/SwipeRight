package com.ekc.swiperight.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import com.ekc.swiperight.R;

import static com.ekc.swiperight.ui.widget.MainFloatingActionButton.State.LIKE;
import static com.ekc.swiperight.ui.widget.MainFloatingActionButton.State.REFRESH;

public class MainFloatingActionButton extends FloatingActionButton {
  State state = LIKE;

  public void setRefreshState() {
    state = REFRESH;
    setImageResource(R.drawable.ic_refresh);
    setColorFilter(Color.WHITE);
  }

  public void setLikeState() {
    state = LIKE;
    setImageResource(R.drawable.ic_heart_unselected);
    setColorFilter(Color.WHITE);
  }

  public State getState() {
    return state;
  }

  public MainFloatingActionButton(Context context) {
    super(context);
  }

  public MainFloatingActionButton(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MainFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public enum State {
    LIKE,
    REFRESH
  }
}
