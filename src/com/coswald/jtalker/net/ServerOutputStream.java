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
 * documentation <b>should</b> actually come form {@code DataOutputStream}. If
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * {@inheritDoc}
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
   * Removes a {@code DataOutputStream} to thelist of objects to use when
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
