package com.ekc.swiperight.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.ekc.swiperight.R;
import com.ekc.swiperight.model.Match;
import com.ekc.swiperight.model.Recommendation;
import com.ekc.swiperight.ui.main.RecommendationAdapterHolders.RowViewHolder;
import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import java.util.ArrayList;
import java.util.List;

public class RecommendationAdapter extends SupportAnnotatedAdapter
    implements RecommendationAdapterBinder {
  @ViewType(
      layout = R.layout.item_rec,
      views = {
          @ViewField(id = R.id.status, name = "status", type = ImageView.class),
          @ViewField(id = R.id.name, name = "name", type = TextView.class),
          @ViewField(id = R.id.match, name = "match", type = ImageView.class),
      }
  ) public final int row = 0;

  private final List<Recommendation> items = new ArrayList<>();
  private final MainPresenter presenter;

  public RecommendationAdapter(Context context, MainPresenter presenter) {
    super(context);
    this.presenter = presenter;
  }

  @Override public int getItemCount() {
    return items.size();
  }

  @Override public void bindViewHolder(RowViewHolder vh, int position) {
    Recommendation rec = items.get(position);

    vh.status.setEnabled(rec.liked());
    vh.match.setEnabled(rec.matched());

    final String trimmedBio = rec.bio().trim().replaceAll("\n", " ");
    if (TextUtils.isEmpty(trimmedBio)) {
      vh.name.setText(String.format("%s", rec.name()));
    } else {
      vh.name.setText(String.format("%s (%s)", rec.name(), trimmedBio));
    }
  }

  @Override public void onBindViewHolder(
      ViewHolder vh, int position,
      List<Object> payloads) {
    if (vh instanceof RowViewHolder) {
      if (payloads == null || payloads.isEmpty()) {
        bindViewHolder((RowViewHolder) vh, position);
        return;
      }

      for (Object obj : payloads) {
        if (obj instanceof Recommendation) {
          ((RowViewHolder) vh).status.setEnabled(((Recommendation) obj).liked());
          ((RowViewHolder) vh).match.setEnabled(((Recommendation) obj).matched());
        }
      }
    }
  }

  public void updateList(final List<Recommendation> list) {
    notifyItemRangeRemoved(0, items.size());
    items.clear();
    if (list != null) {
      items.addAll(list);
      notifyItemRangeInserted(0, list.size());
    }
  }

  public void likeResponse(Match match, boolean likeAllInProgress) {
    Recommendation recommendation = match.recommendation();
    if (recommendation == null) {
      return;
    }

    final int index = items.indexOf(recommendation);
    recommendation.setLiked();
    if (match != Match.NO_MATCH) {
      recommendation.setMatched();
    }

    notifyItemChanged(index, recommendation);

    if (likeAllInProgress) {
      // Continue the like chain
      if (index >= 0 && index < items.size() - 1) {
        presenter.like(items.get(index + 1));
      } else {
        presenter.refresh();
      }
    }
  }

  public void clear() {
    notifyItemRangeRemoved(0, items.size());
    items.clear();
  }

  public void likeAll() {
    if (!items.isEmpty()) {
      presenter.likeAll(items.get(0));
    }
  }
}
