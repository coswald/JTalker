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

package com.coswald.net;

import com.coswald.Initializable;
import com.coswald.net.ServerClientConstants;
import com.coswald.net.ServerOutputStream;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.0.1
 */
public class ClientInstance implements Closeable, Initializable, Runnable
{
  protected Socket socket;
  
  private DataInputStream input;
  private DataOutputStream fakeOutput;
  private ServerOutputStream output;
  private String identifier;
  private boolean goodToRun;
  
  public ClientInstance(Socket socket, ServerOutputStream output)
  {
    this.socket = socket;
    this.output = output;
    this.goodToRun = false;
  }
  
  @Override
  public void init()
  {
    try
    {
      this.input = new DataInputStream(
        new BufferedInputStream(this.socket.getInputStream()));
      this.fakeOutput = new DataOutputStream(this.socket.getOutputStream());
      this.output.add(this.fakeOutput);
      
      this.identifier = this.input.readUTF();
      System.out.println(this.identifier + " has been accepted!");
    }
    catch(IOException i)
    {
      i.printStackTrace();
      return;
    }
    this.goodToRun = true;
  }
  
  @Override
  public void run()
  {
    if(this.goodToRun)
    {
      try
      {
        String line = "";
        while(!line.equalsIgnoreCase(ServerClientConstants.EXIT_MESSAGE))
        {
          line = this.input.readUTF();
          if(!line.equalsIgnoreCase(ServerClientConstants.EXIT_MESSAGE))
          {
            this.output.writeUTF(this.identifier + ": " + line + "\n\r");
          }
        }
        this.close();
      }
      catch(SocketException s)
      {
        //Do NOTHING
      }
      catch(IOException i)
      {
        i.printStackTrace();
      }
    }
  }
  
  @Override
  public void close() throws IOException
  {
    if(this.socket != null && this.socket != null)
    {
      this.goodToRun = false;
      this.output.remove(this.fakeOutput);
      this.output.writeUTF(this.identifier + " has left the chat.\n\r");
      this.socket.close();
      this.input.close();
    }
  }
}
