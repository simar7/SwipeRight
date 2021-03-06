package com.ekc.swiperight.data.converter;

import com.ekc.swiperight.model.Match;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class MatchAdapterFactory implements TypeAdapterFactory {
  private final Class<Match> matchClass = Match.class;

  @Override public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    return type.getRawType() == matchClass
           ? (TypeAdapter<T>) matchAdapter(gson, (TypeToken<Match>) type)
           : null;
  }

  private TypeAdapter<Match> matchAdapter(Gson gson, TypeToken<Match> type) {
    final TypeAdapter<Match> delegate = gson.getDelegateAdapter(this, type);
    final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
    return new TypeAdapter<Match>() {
      @Override public void write(JsonWriter out, Match value) throws IOException {
        JsonElement tree = delegate.toJsonTree(value);
        elementAdapter.write(out, tree);
      }

      @Override public Match read(JsonReader in) throws IOException {
        // If match is a single boolean, then it's not a match
        if (in.peek() == JsonToken.BOOLEAN) {
          in.nextBoolean();
          return Match.NO_MATCH;
        }

        JsonElement tree = elementAdapter.read(in);
        return delegate.fromJsonTree(tree);
      }
    };
  }
}