package com.taslitsky.reader;

import java.io.InputStream;
import java.util.Properties;

public abstract class PropertiesReader {
  protected static final String PROPS_FILE_NAME = "config.properties";
  protected final Properties properties;

  protected PropertiesReader() {
    properties = new Properties();
    try {
      InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(PROPS_FILE_NAME);
      properties.load(inputStream);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
