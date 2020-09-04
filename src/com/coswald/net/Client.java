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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client implements Closeable, Initializable, Runnable
{
  private String identifier;
  private String host;
  private int port;
  private Socket socket;
  private BufferedReader input;
  private DataInputStream serverInput;
  private DataOutputStream output;
  private boolean running;
  
  public Client(String identifier, String host, int port)
  {
    if(port < 0 || port > 65535)
    {
      throw new IllegalArgumentException("Port must be between 0 " +
        "and 65535, inclusive!");
    }
    this.identifier = identifier;
    this.host = host;
    this.port = port;
    this.running = false;
  }
  
  @Override
  public void init()
  {
    try
    {
      this.socket = new Socket(this.host, this.port);
      System.out.println("Connected");
      
      this.input = new BufferedReader(new InputStreamReader(System.in));
      this.serverInput = new DataInputStream(
        new BufferedInputStream(this.socket.getInputStream()));
      this.output = new DataOutputStream(socket.getOutputStream());
      
      this.running = true;
      
      // Send out our identifier
      this.output.writeUTF(this.identifier);
      
      // Start Listening Thread
      (new Thread()
      {
        public void run()
        {
          String line = "";
          while(running)
          {
            try
            {
              line = serverInput.readUTF();
              System.out.print(line);
            }
            catch(SocketException s)
            {
              System.out.println("Connection has been lost");
              System.exit(0);
            }
            catch(IOException i)
            {
              //i.printStackTrace();
              System.out.println("Server has shutdown or another I/O error " +
                                 "happened.");
              System.exit(1);
            }
          }
        }
      }).start();
    }
    catch(UnknownHostException u)
    {
      System.out.println("Unknown Host!");
      System.exit(1);
    }
    catch(IOException i)
    {
      System.out.println("Error while initializing IO Stream!");
      System.exit(2);
    }
  }
  
  @Override
  public void run()
  {
    try
    {
      String line = "";
      while(!line.equalsIgnoreCase(ServerClientConstants.EXIT_MESSAGE))
      {
        //System.out.print(this.identifier + ": ");
        line = this.input.readLine();
        this.output.writeUTF(line);
      }
      this.close();
    }
    catch(IOException i)
    {
      i.printStackTrace();
    }
  }
  
  @Override
  public void close() throws IOException
  {
    this.running = false;
    this.input.close();
    this.output.close();
    this.socket.close(); //Any one of these lines may throw an IOException
  }
  
  public String getHost()
  {
    return this.host;
  }
  
  public String getID()
  {
    return this.identifier;
  }
  
  public int getPort()
  {
    return this.port;
  }
}
