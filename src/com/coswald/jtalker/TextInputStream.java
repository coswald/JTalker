/*
 * TextInputStream.java
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

package com.coswald.jtalker;

import java.io.ByteArrayInputStream;

import java.util.Arrays;

/**
 * <p>A simple stream for reading text. This class is a simple addition to the
 * {@code ByteArrayInputStream} class that it implements: it allows the
 * addition of strings. This allows text to be appended to the stream after
 * creation, which can be helpful for a multitude of reasons. However, we will
 * be using it for the simple reason that it allows us to have an
 * {@code InputStream} that can receive a simple string as input, and 
 * dynamically modify that stream.</p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.2.0
 * @see java.io.ByteArrayInputStream;
 */
public class TextInputStream extends ByteArrayInputStream
{
  /**
   * Creates a {@code TextInputStream} so that it uses {@code buf} as its buffer
   * array. The buffer array is not copied. The initial value of {@code pos} is
   * 0 and the initial value of {@code count} is the length of {@code buf}.
   */
  public TextInputStream(byte[] buf)
  {
    super(buf);
  }
  
  /**
   * Creates a {@code TextInputStream} that uses {@code buf} as its buffer
   * array. The initial value of {@code pos} is {@code offset} and the initial
   * value of {@code count} is the minimum of {@code offset + length} and
   * {@code buf.length}. The buffer array is not copied. The buffer's mark is
   * set to the specified offset.
   */
  public TextInputStream(byte[] buf, int offset, int length)
  {
    super(buf, offset, length);
  }
  
  /**
   * Creates a stream with the given String as the starting bytes.
   * @param s The string to convert into bytes.
   */
  public TextInputStream(String s)
  {
    this(s.getBytes());
  }
  
  /**
   * Adds a string to the stream. This will update all of the necessary
   * variables in order to make reading from this stream possible again.
   * @param s The string to add.
   * @see java.util.Arrays#copyOf(byte[], int)
   */
  public void add(String s)
  {
    byte[] addition = s.getBytes();
    int oldLength = this.buf.length;
    this.buf = Arrays.copyOf(this.buf, this.buf.length + addition.length);
    for(int i = oldLength; i < this.buf.length; i++)
    {
      this.buf[i] = addition[i - oldLength];
    }
    this.count = this.buf.length;
  }
}
