package com.deque.axe.android.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class AxeFile {

  public final File file;

  public AxeFile(File file) {
    this.file = file;
  }

  public AxeFile(String resource) {
    this(new File("src/test/resources/" + resource));
  }

  public String path() {
    return file.getAbsolutePath();
  }
}
