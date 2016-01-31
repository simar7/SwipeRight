package com.ekc.tinderlike.ui.main;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.ekc.tinderlike.R;
import com.ekc.tinderlike.model.Match;
import com.ekc.tinderlike.model.Recommendation;
import com.ekc.tinderlike.ui.main.RecommendationAdapterHolders.RowViewHolder;
import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import java.util.ArrayList;
import java.util.List;
import rx.subjects.BehaviorSubject;

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
  private final BehaviorSubject<Recommendation> recommendationSubject;

  public RecommendationAdapter(Context context,
      BehaviorSubject<Recommendation> recommendationSubject) {
    super(context);
    this.recommendationSubject = recommendationSubject;
  }

  @Override public int getItemCount() {
    return items.size();
  }

  @Override public void bindViewHolder(RowViewHolder vh, int position) {
    Recommendation rec = items.get(position);

    vh.status.setEnabled(rec.liked());
    vh.name.setText(String.format("%s (%s)", rec.name(), rec.bio()));
    vh.match.setEnabled(rec.matched());
  }

  public void updateList(final List<Recommendation> list) {
    notifyItemRangeRemoved(0, items.size());
    items.clear();
    if (list != null) {
      items.addAll(list);
      notifyItemRangeInserted(0, list.size());
    }
  }

  public void likeAll() {
    recommendationSubject.onNext(items.get(0));
  }

  public void likeResponse(Recommendation recommendation, Match match, MainActivity activity) {
    final int index = items.indexOf(recommendation);
    recommendation.setLiked();
    if (match.isMatch()) {
      recommendation.setMatched();
    }
    notifyItemChanged(index);

    if (index < items.size() - 1) {
      recommendationSubject.onNext(items.get(index + 1));
    } else {
      activity.refresh();
    }
  }

  public void clear() {
    notifyItemRangeRemoved(0, items.size());
    items.clear();
  }
}
