/*
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

package com.coswald.jtalker.net;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;

/**
 * <p>A data output stream that lets a
 * {@link com.coswald.jtalker.net.TCPServer TCPServer} treat multiple
 * {@code DataOutputStream}s as one stream. This class is a wrapper for an
 * {@code ArrayList} of {@code DataOutputStream}s. It is used by a
 * {@link com.coswald.jtalker.net.TCPServer TCPServer} and passed to a 
 * {@link com.coswald.jtalker.net.TCPClientInstance TCPClientInstance} to make
 * sure the server can talk to multiple clients at the same time.</p>
 * <p>In this class you will see a lot of inherited documentation. This is
 * because I did not want to rewrite the wheel when documenting; however, this
 * inherited documentation comes from {@code FilerOutputStream}. Thus, any
 * documentation <b>should</b> actually come from {@code DataOutputStream}. If
 * there are any misconstrued documentation, please refer to
 * {@code DataOutputStream}.
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.0.1
 * @see java.io.DataOutputStream
 */
public final class ServerOutputStream extends FilterOutputStream implements
  DataOutput
{
  private ArrayList<DataOutputStream> outputStreams;
  
  /**
   * Creates a {@code ServerOutputStream} with the original output stream and
   * no others. The {@code out} parameter is not used by any of the methods in
   * this class, and {@code super} doesn't get called at all, so there is no
   * repercussions to adding and them removing it. However, if {@code out} is
   * {@code null}, then it will not be added. Also note that if {@code out} is
   * not a {@code DataOutputStream}, it will be wrapped as one (and therefore
   * can't be removed).
   * @param out The original output stream.
   */
  public ServerOutputStream(OutputStream out)
  {
    super(out);
    this.outputStreams = new ArrayList<DataOutputStream>();
    if(out != null)
    {
      this.outputStreams.add((out instanceof DataOutputStream) ?
        (DataOutputStream)out : new DataOutputStream(out));
    }
  }
  
  /**
   * Flushes this output stream. This forces any buffered output bytes to be
   * written out to the stream. The {@code flush} method does <b>not</b> call
   * the flush method of its underlying output stream, <b>unless</b> it is part
   * of the list of output streams.
   * @throws IOException if an I/O error occurs.
   * @see java.io.DataOutputStream#flush()
   */
  @Override
  public void flush() throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.flush();
    }
  }
  
  /**
   * Writes {@code b.length} bytes to the output streams. The {@code write}
   * method of {@code ServerOutputStream} calls its {@code write} method of
   * three arguments with the arguments {@code b}, {@code 0}, and
   * {@code b.length}. Note that this method only calls the {@code write}
   * method of its underlying stream with the single argument {@code b} if it is
   * within the output stream list.
   * @param b The data to be written.
   * @throws IOException If an I/O error occurs.
   * @see java.io.DataOutputStream#write(byte[])
   */
  @Override
  public void write(byte[] b) throws IOException
  {
    this.write(b, 0, b.length);
  }
  
  /**
   * Writes {@code len} bytes from the specified {@code byte} array starting at
   * offset {@code off} to the output streams. This calls the 
   * {@link java.io.DataOutputStream#write(byte[], int, int) write} method for
   * each {@code DataOutputStream} within the output stream list. Note that this
   * method does <b>not</b> call the {@code write} method of the underlying
   * output stream <b>unless</b> it is within the output stream list.
   * @param b The data.
   * @param off The start offset in the data.
   * @param len The number of bytes to write.
   * @throws IOException If an I/O error occurs.
   * @see java.io.DataOutputStream#write(byte[], int, int)
   */
  @Override
  public void write(byte[] b, int off, int len) throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.write(b, off, len);
    }
  }  
  
  /**
   * Writes the specified {@code byte} to the output streams. The {@code write}
   * method of {@code ServerOutputStream} does <b>not</b> call the underlying
   * output streams {@code write} method, <b>unless</b> it is part of the 
   * output stream list.
   * @param b The byte to write.
   * @throws IOException If an I/O error occurs.
   * @see java.io.DataOutputStream#write(int)
   */
  @Override
  public void write(int b) throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.write(b);
    }
  }
  
  /**
   * Writes a {@code boolean} value to the output streams. If the argument v is
   * {@code true}, the value ({@code byte}) 1 is written; if v is {@code false},
   * the value ({@code byte}) 0 is written. The byte written by this method may
   * be read by the {@link java.io.DataInput#readBoolean() readBoolean} method
   * of the {@code DataInput} interface, which will then return a
   * {@code boolean} value equal to v.
   * @param v The boolean to be written.
   * @throws IOException If an I/O error occurs.
   * @see java.io.DataOutputStream#writeBoolean(boolean)
   */
  @Override
  public void writeBoolean(boolean v) throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.writeBoolean(v);
    }
  }
  
  /**
   * Writes to the output streams the eight low-order bits of the argument v.
   * The 24 high-order bits of v are ignored. The byte written by this method
   * may be read by the {@link java.io.DataInput#readByte() readByte} method of
   * the {@code DataInput} interface, which will then return a byte equal to 
   * ({@code byte})v.
   * @param v The byte value to be written
   * @throws IOException If an I/O error occurs.
   * @see java.io.DataOutputStream#writeByte(int)
   */
  @Override
  public void writeByte(int v) throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.writeByte(v);
    }
  }
  
  /**
   * Writes a {@code String} to the output streams. For every character in the
   * string s, taken in order, one byte is written to the output stream. If s
   * is {@code null}, a {@code NullPointerException} is thrown. If s.length is
   * zero, then no bytes are written. Otherwise, the character s[0] is written
   * first, then s[1], and so on; the last character written is s[s.length - 1].
   * For each character, one byte is written, the low-order byte, in exactly the
   * manner of the {@link #writeByte(int) writeByte} method. The high-order
   * eight bits of each character in the string are ignored.
   * @param s The String of bytes to be written.
   * @throws IOException If an I/O error occurs.
   * @see java.io.DataOutputStream#writeBytes(String)
   */
  @Override
  public void writeBytes(String s) throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.writeBytes(s);
    }
  }

  /**
   * Writes a {@code char} value, which is comprised of two bytes, to the output
   * streams. The byte values to be written are shown in the order they are
   * written in at {@link java.io.DataOutput#writeChar(int) this} link. The
   * bytes written by this method may be read by the
   * {@link java.io.DataInput#readChar() readChar} method of the
   * {@code DataInput} interface, which will then return a {@code char} equal to
   * ({@code char})v.
   * @param v The char value to be written.
   * @throws IOException If an I/O error occurs.
   * @see java.io.DataOutputStream#writeChar(int)
   */
  @Override
  public void writeChar(int v) throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.writeChar(v);
    }
  }

  /**
   * Writes every character in the string s, to the output streams, in order,
   * two bytes per character. If s i {@code null}, a
   * {@code NullPointerException} is thrown. If {@code s.length} is zero, then
   * no bytes are written. Otherwise, the character s[0] is written first, then
   * s[1], and so on; the last character written is s[s.length - 1]. For each
   * character, one byte is written, the low-order byte, in exactly the manner
   * of the {@link #writeByte(int) writeByte} method. The high-order eight bits
   * of each character in the string are ignored.
   * @param s The string of bytes to be written
   * @throws IOException If an I/O error occurs.
   * @see java.io.DataOutputStream#writeChars(String)
   */
  @Override
  public void writeChars(String s) throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.writeChars(s);
    }
  }

  /**
   * Writes a double value, which is comprised of eight bytes, to the output
   * streams. It does this as if it first converts this {@code double} value to
   * a long in exactly the manner of the
   * {@link java.lang.Double#doubleToLongBits(double) doubleToLongBits} method
   * and then writes the long value in exactly the manner of the
   * {@link #writeLong writeLong} method. The bytes written by this method may
   * be read by the {@link java.io.DataInput#readDouble() readDouble} method of
   * the {@code DataInput} interface, which will then return a {@code double}
   * equal to v.
   * @param v The double value to be written
   * @throws IOException If an I/O error occurs.
   * @see java.io.DataOutputStream#writeDouble(double)
   */
  @Override
  public void writeDouble(double v) throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.writeDouble(v);
    }
  }

  /**
   * Writes a {@code float} value, which is comprised of four bytes, to the
   * output streams. It does this as if it first converts this {@code float}
   * value to an {@code int} in exactly the manner of the
   * {@link java.lang.Float#floatToIntBits(float) floatToIntBits} method. The
   * bytes written by this method may be read by the
   * {@link java.io.DataInput#readFloat() readFloat} method of the
   * {@code DataInput} interface, which will then return a {@code float} equal
   * to v.
   * @param v The float value to be written.
   * @throws IOException If an I/O error occurs.
   * @see java.io.DataOutputStream#writeFloat(float)
   */
  @Override
  public void writeFloat(float v) throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.writeFloat(v);
    }
  }

  /**
   * Writes an {@code int} value, which is comprised of four bytes, to the
   * output streams. The byte values to be written are shown in the order they
   * are written in at {@link java.io.DataOutput#writeInt(int) this} link. The
   * bytes written by this method may be read by the
   * {@link java.io.DataInput#readInt() readInt} method of the
   * {@code DataInput} interface, which will then return an {@code int} equal to
   * v.
   * @param v The int value to be written.
   * @throws IOException If an I/O error occurs.
   * @see java.io.DataOutputStream#writeInt(int)
   */
  @Override
  public void writeInt(int v) throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.writeInt(v);
    }
  }

  /**
   * Writes a {@code long} value, which is comprised of eight bytes, to the
   * output streams. The byte values to be written are shown in the order they
   * are written in at {@link java.io.DataOutput#writeLong(long) this} link. The
   * bytes written by this method may be read by the
   * {@link java.io.DataInput#readLong() readLong} method of the
   * {@code DataInput} interface, which will then return a {@code long} equal to
   * v.
   * @param v The long value to be written.
   * @throws IOException If an I/O error occurs.
   * @see java.io.DataOutputStream#writeLong(long)
   */
  @Override
  public void writeLong(long v) throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.writeLong(v);
    }
  }

  /**
   * Writes a {@code short} value, which is comprised of two bytes, to the
   * output streams. The byte values to be written are shown in the order they
   * are written in at {@link java.io.DataOutput#writeShort(int) this} link. The
   * bytes written by this method may be read by the
   * {@link java.io.DataInput#readShort() readShort} method of the
   * {@code DataInput} interface, which will then return a {@code short} equal
   * to ({@code short})v.
   * @param v The short value to be written.
   * @throws IOException If an I/O error occurs.
   * @see java.io.DataOutputStream#writeShort(int)
   */
  @Override
  public void writeShort(int v) throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.writeShort(v);
    }
  }

  /**
   * Writes two bytes of length information to the output streams, followed by
   * the
   * <a href="https://docs.oracle.com/javase/7/docs/api/java/io/DataInput.html#modified-utf-8">
   * modified UTF-8</a> representation of every character in the string s. If s
   * is {@code null}, a {@code NullPointerException} is thrown. Each character
   * in the string s is converted to a group of one, two, or three bytes, 
   * depending on the value of the character. See
   * {@link java.io.DataOutputStream#writeUTF(String) this} documentation for
   * more details.
   * @param line The string value to be written.
   * @throws IOException If an I/O error occurs.
   */
  @Override
  public void writeUTF(String line) throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.writeUTF(line);
    }
  }
  
  /**
   * Closes all of the output streams within the output stream list. It is
   * preferred to not call this method unless the server is shutting down, as
   * this output stream should always be available to be running. If this 
   * method is called, and we add another {@code DataOutputStream}, there is no
   * code that blocks us from allowing us to do this, and no way of knowing 
   * which {@code DataOutputStream}s are closed and which ones are open. If at
   * all possible, avoid calling this method.
   * @throws IOException If an I/O error occurs.
   */
  @Override
  public void close() throws IOException
  {
    for(DataOutputStream dos : this.outputStreams)
    {
      dos.close();
    }
  }
  
  /**
   * Adds a {@code DataOutputStream} to the list of objects to use when writing.
   * This is a wrapper method for {@code ArrayList}s
   * {@link java.util.ArrayList#add(Object) add} method. However, {@code null}
   * cannot be added to the list, so if this method is called with {@code null},
   * nothing will happen.
   * @param dos The output stream to add to the list of outputs.
   * @see java.util.ArrayList#add(Object)
   * @see #remove(DataOutputStream)
   */
  public void add(DataOutputStream dos)
  {
    if(dos != null)
    {
      this.outputStreams.add(dos);
    }
  }
  
  /**
   * Removes a {@code DataOutputStream} to the list of objects to use when
   * writing. This is a wrapper method for {@code ArrayList}s
   * {@link java.util.ArrayList#remove(Object) remove} method. However, because
   * a {@code null DataOutputStream} cannot be added, when this method is called
   * with {@code null} as a parameter, nothing happens.
   * @param dos The output stream to remove from the list of outputs.
   * @see java.util.ArrayList#remove(Object)
   * @see #add(DataOutputStream)
   */
  public void remove(DataOutputStream dos)
  {
    if(dos != null)
    {
      this.outputStreams.remove(dos);
    }
  }
  
  /**
   * Returns the current value of the
   * {@link java.io.DataOutputStream#written written} counter for each active
   * {@code DataOutputStream}. This is in effect the same as adding the 
   * {@link java.io.DataOutputStream#written written} counter for each active
   * {@code DataOutputStream}, <b>unless</b> the counter overflows. If this 
   * happens, then the maximum size is not added at all. Thus, if each
   * {@code DataOutputStream}'s {@link java.io.DataOutputStream#size() size}
   * method returns the {@link java.lang.Integer#MAX_VALUE max} value, this
   * method will return 0.
   * @return The cumulative byte's written.
   */
  public int size()
  {
    int size = 0;
    for(DataOutputStream dos : this.outputStreams)
    {
      size += (dos.size() == Integer.MAX_VALUE) ? 0 : dos.size();
    }
    return size;
  }
}
