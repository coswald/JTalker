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
 *
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.0.1
 */
public class ServerOutputStream extends FilterOutputStream implements DataOutput
{
  private ArrayList<DataOutputStream> outputStreams;
  
  /**
   * 
   * @param out
   */
  public ServerOutputStream(OutputStream out)
  {
    super(out);
    this.outputStreams = new ArrayList<DataOutputStream>();
    this.outputStreams.add(new DataOutputStream(out));
  }
  
  /**
   * 
   * @throws IOException
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
   * 
   * @return
   */
  public int size()
  {
    int size = 0;
    for(DataOutputStream dos : this.outputStreams)
    {
      size += dos.size();
    }
    return size;
  }
  
  /**
   * 
   * @param b
   * @param off
   * @param len
   * @throws IOException
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
   * 
   * @param b
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
   * 
   * @param v
   * @throws IOException
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
   * 
   * @param v
   * @throws IOException
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
   * 
   * @param s
   * @throws IOException
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
   * 
   * @param v
   * @throws IOException
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
   * 
   * @param s
   * @throws IOException
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
   * 
   * @param v
   * @throws IOException
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
   * 
   * @param v
   * @throws IOException
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
   * 
   * @param v
   * @throws IOException
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
   * 
   * @param v
   * @throws IOException
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
   * 
   * @param v
   * @throws IOException
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
   * 
   * @param line
   * @throws IOException
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
   * 
   * @throws IOException
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
   * 
   * @param dos
   */
  public void add(DataOutputStream dos)
  {
    this.outputStreams.add(dos);
  }
  
  /**
   * 
   * @param dos
   */
  public void remove(DataOutputStream dos)
  {
    this.outputStreams.remove(dos);
  }
}
