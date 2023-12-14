/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.luke.app.desktop.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import javax.swing.JTextArea;

/** PrintStream for text areas */
public final class TextAreaPrintStream extends PrintStream implements AutoCloseable {

//  private final ByteArrayOutputStream baos;

  private final JTextArea textArea;

//  public TextAreaPrintStream(JTextArea textArea) {
//    super(new ByteArrayOutputStream(), false, StandardCharsets.UTF_8);
////    this.baos = (ByteArrayOutputStream) out;
//    this.textArea = textArea;
////    baos.reset();
//  }
//
//  @Override
//  public void flush() {
//    ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
//    textArea.append(baos.toString(StandardCharsets.UTF_8));
//  }
  private final ByteArrayOutputStream buffer;
  private final int bufferSize;

  public TextAreaPrintStream(JTextArea textArea, int bufferSize) throws IOException {

  // Use a dummy OutputStream as the argument for the super constructor
  super(new OutputStream() {
    @Override
    public void write(int b) throws IOException {
      // Do nothing
    }
  }, true, StandardCharsets.UTF_8); // Enable auto-flushing
  this.textArea = textArea;
  this.bufferSize = bufferSize;
  this.buffer = new ByteArrayOutputStream(bufferSize); // Create a buffer with the given size
}

  @Override
  public void write(int b) {
    buffer.write(b); // Write to the buffer instead of the textArea
    if (buffer.size() >= bufferSize) { // If the buffer is full
      flush(); // Flush the buffer to the textArea
    }
  }

  @Override
  public void write(byte[] b, int off, int len) {
    buffer.write(b, off, len); // Write to the buffer instead of the textArea
    if (buffer.size() >= bufferSize) { // If the buffer is full
      flush(); // Flush the buffer to the textArea
    }
  }

  @Override
  public void flush() {
    textArea.append(buffer.toString(StandardCharsets.UTF_8)); // Append the buffer contents to the textArea
    buffer.reset(); // Reset the buffer
  }
}
