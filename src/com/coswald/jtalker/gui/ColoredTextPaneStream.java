/*
 * ColoredTextPaneStream.java
 * 
 * Copyright 2020 Coved W. Oswald <coswald@uni.edu>
 * 
 * This file is part of JTalker.
 *
 * JTalker is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * JTalker is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * JTalker. If not, see <https://www.gnu.org/licenses/>.
 */

package com.coswald.jtalker.gui;

import com.coswald.jtalker.gui.ColoredTextPane;

import java.io.ByteArrayOutputStream;

/**
 * <p>A simple stream for outputting a byte stream. This class is a simple
 * addition to the {@code ByteArrayOutputStream} class that it implements: 
 * whenever something is written to this stream, it will
 * {@link com.coswald.jtalker.gui.ColoredTextPane#append(String) append} the
 * bytes. This allows the {@code ColoredTextPane} to be written to via an
 * {@code OutputStream}, while still ensuring the color properties of the
 * panel.</p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.2.0
 * @see com.coswald.jtalker.gui.ColoredTextPane
 * @see java.io.ByteArrayOutputStream
 */
public class ColoredTextPaneStream extends ByteArrayOutputStream
{
  private ColoredTextPane text;
  
  /**
   * Creates a new byte array output stream, with the buffer capacity of the
   * specified size, in bytes.
   * @param text The pane to append to.
   * @param size The initial size.
   */
  public ColoredTextPaneStream(ColoredTextPane text, int size)
  {
    super(size);
    this.text = text;
  }
  
  /**
   * Creates a new bytes array output stream, with a buffer capacity of the
   * specified size, in bytes.
   * @param text The pane to append to.
   */
  public ColoredTextPaneStream(ColoredTextPane text)
  {
    this(text, 32);
  }
  
  /**
   * Writes {@code len} bytes from the specified byte array starting off at
   * offset {@code off} to this byte array output stream, while appending the
   * byte array selection to the {@code ColoredTextPane} provided.
   * @param b The data.
   * @param off The start offset in the data.
   * @param len The number of bytes to write.
   */
  @Override
  public void write(byte[] b, int off, int len)
  {
    super.write(b, off, len);
    this.text.append(new String(b, off, len));
  }
  
  /**
   * Writes the specified byte to this byte array output stream, while appending
   * the byte to the {@code ColoredTextPane} provided.
   * @param b The byte to be written.
   */
  @Override
  public void write(int b)
  {
    super.write(b);
    this.text.append(new String(new byte[] {(byte)b}));
  }
}
