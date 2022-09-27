package com.taslitsky;

import java.io.BufferedWriter;
import java.io.FileWriter;

import java.io.IOException;

public class DataWriter {
  public void writeData(StringBuilder stringBuilder, String fileName) {
    try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
      bufferedWriter.write(stringBuilder.toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
