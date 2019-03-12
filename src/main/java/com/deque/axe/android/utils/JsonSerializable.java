package com.deque.axe.android.utils;

import com.deque.axe.android.colorcontrast.AxeColor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public interface JsonSerializable {

  GsonBuilder DEFAULT_BUILDER = new GsonBuilder()
      .setPrettyPrinting()
      .serializeNulls()
      .setLongSerializationPolicy(LongSerializationPolicy.STRING)
      .registerTypeAdapter(AxeColor.class, new TypeAdapter<AxeColor>() {
        @Override
        public void write(JsonWriter out, AxeColor value) throws IOException {
          out.jsonValue('"' + value.toHex() + '"');
        }

        @Override
        public AxeColor read(JsonReader in) throws IOException {

          if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
          } else {
            return new AxeColor(in.nextString());
          }
        }
      });

  static GsonBuilder getDefaultBuilder() {
    return DEFAULT_BUILDER;
  }

  default Gson getGson() {
    return getDefaultBuilder().create();
  }

  default Gson getGsonComparison() {
    return getGson();
  }

  default String toJson() {
    return getGson().toJson(this);
  }

  /**
   * An implementation of hashCode for objects that want to be identified by their JSON
   * Serialization.
   * @param serializable The object to be serialized.
   * @return The hashCode of the JSON String representation.
   */
  static int hashCode(final JsonSerializable serializable) {

    if (serializable == null) {
      return 0;
    }

    return serializable.getGsonComparison().toJson(serializable).hashCode();
  }

  /**
   * An implementation of equals for objects that want to be identified by their JSON Serialization.
   * @param thiz The object representing this.
   * @param that The object being compared to.
   * @return Whether or not the two objects have the same JSON Serialization.
   */
  static boolean equals(final JsonSerializable thiz, final Object that) {

    if (thiz == that) {
      return true;
    }

    // Their pointers are different. If either pointer is null, return false;
    if (thiz == null ||  that == null) {
      return false;
    }

    if (!(that instanceof JsonSerializable)) {
      return false;
    }

    final Gson gson = thiz.getGsonComparison();

    return gson.toJson(thiz).equals(gson.toJson(that));
  }

  /**
   * An implementation of compareTo for objects that want to be identified by their JSON
   * Serialization.
   * @param thiz The object representing this.
   * @param that The object being compared to.
   * @return Whether or not the two objects have the same JSON Serialization.
   */
  static int compareTo(final JsonSerializable thiz, final Object that) {

    if (thiz == that) {
      return 0;
    }

    if (thiz == null) {
      return -1;
    }

    if (that == null) {
      return 1;
    }

    if (!(that instanceof JsonSerializable)) {
      return 1;
    }

    final Gson gson = thiz.getGsonComparison();

    return gson.toJson(thiz).compareTo(gson.toJson(that));
  }

  /**
   * Convert an a String to a JSON Object.
   * @param jsonString The String to convert.
   * @param clazz The class to serialize from.
   * @param <T> The class that will be returned.
   * @return  A new instance of standard T.
   */
  static <T> T fromJson(final String jsonString, Class<T> clazz) {
    if (clazz == null || jsonString == null || jsonString.isEmpty()) {
      return null;
    }

    return getDefaultBuilder().create().fromJson(jsonString, clazz);
  }
}