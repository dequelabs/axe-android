package com.deque.axe.android.utils;

import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngReader;

import com.deque.axe.android.colorcontrast.AxeColor;
import com.deque.axe.android.colorcontrast.AxeImage;
import com.deque.axe.android.wrappers.AxeRect;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


/**
 * The stupid Android SDK doesn't include any reasonable Image parsing for unit tests.
 * Instead of using Android Instrumented tests, which suck, we build a cute little PNG Parser
 * from an Open Source library that doesn't rely on AWT.
 */
public class AxeJankyPng extends AxeImage {

  private List<List<AxeColor>> pixels = new ArrayList<>();

  AxeJankyPng(PngReader pngReader) {
    while (pngReader.hasMoreRows()) {

      final ImageLineInt imageLineInt = (ImageLineInt) pngReader.readRow();

      final List<AxeColor> pixelRow = new ArrayList<>();

      pixels.add(pixelRow);

      for (int i = 0; i < imageLineInt.getSize(); i += 4) {

        final int red = imageLineInt.getElem(i);
        final int green = imageLineInt.getElem(i + 1);
        final int blue = imageLineInt.getElem(i + 2);
        final int alpha = imageLineInt.getElem(i + 3);

        pixelRow.add(new AxeColor(alpha, red, green, blue));
      }
    }
  }

  @Override
  public Gson getGson() {
    return JsonSerializable.getDefaultBuilder()
        .registerTypeAdapter(AxeImage.class, new TypeAdapter<AxeImage>() {
          @Override
          public void write(JsonWriter out, AxeImage value) throws IOException {
            throw new RuntimeException("Writing out is not supported for testing.");
          }

          @Override
          public AxeImage read(JsonReader in) throws IOException {

            if (in.peek() == JsonToken.NULL) {
              in.nextNull();
              return null;
            } else {
              return new AxeJankyPng(in.nextString());
            }
          }
        }).create();
  }

  public AxeJankyPng(AxeFile axeFile) {
    this(new PngReader(axeFile.file));
  }

  public AxeJankyPng(final String base64png) {
    this(new PngReader(new ByteArrayInputStream(Base64.getMimeDecoder()
            .decode(base64png.getBytes(StandardCharsets.UTF_8)))));
  }

  @Override
  public AxeRect frame() {
    return new AxeRect(0, pixels.get(0).size() - 1, 0, pixels.size() - 1);
  }

  @Override
  public AxeColor pixel(int x, int y) {
    return pixels.get(y).get(x);
  }

  @Override
  public String toBase64Png() {
    throw new RuntimeException("Don't do this, it won't work and I don't want to implement it.");
  }
}
