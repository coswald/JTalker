/*
 * Client.java
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

package com.coswald.jtalker.net;

import com.coswald.jtalker.Initializable;
import com.coswald.jtalker.net.ServerClientConstants;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.0.1
 */
public class Client implements Closeable, Initializable, Runnable
{
  private String identifier;
  private String host;
  private int port;
  private boolean running;
  
  private Socket socket;
  private BufferedReader input;
  private DataInputStream serverInput;
  private DataOutputStream output;
  
  private InputStream in;
  private PrintStream out;
  
  /**
   * 
   * @param in
   * @param out
   * @param identifier
   * @param host
   * @param port
   */
  public Client(InputStream in, PrintStream out, String identifier,
    String host, int port)
  {
    if(port < ServerClientConstants.MIN_PORT_NUMBER ||
       port > ServerClientConstants.MAX_PORT_NUMBER)
    {
      throw new IllegalArgumentException("Port must be between " +
        ServerClientConstants.MIN_PORT_NUMBER +
        " and " + ServerClientConstants.MAX_PORT_NUMBER + ", inclusive!");
    }
    this.identifier = identifier;
    this.host = host;
    this.port = port;
    this.running = false;
    
    this.in = in;
    this.out = out;
  }
  
  /**
   * 
   * @param identifier
   * @param host
   * @param port
   */
  public Client(String identifier, String host, int port)
  {
    this(System.in, System.out, identifier, host, port);
  }
  
  /**
   * 
   */
  @Override
  public void init()
  {
    try
    {
      this.socket = new Socket(this.host, this.port);
      
      this.input = new BufferedReader(new InputStreamReader(this.in));
      this.serverInput = new DataInputStream(
        new BufferedInputStream(this.socket.getInputStream()));
      this.output = new DataOutputStream(socket.getOutputStream());
      
      this.out.println("Waiting to be connected...");
      
      //wait for the server to accept us
      this.running = this.serverInput.readBoolean();
      
      this.out.println("Connected!");
      
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
              out.print(line);
            }
            catch(SocketException s)
            {
              out.println("Connection has been lost");
              running = false;
              //System.exit(0);
            }
            catch(IOException i)
            {
              //i.printStackTrace();
              out.println("Server has shutdown or another I/O error " +
                                 "happened.");
              running = false;
              //System.exit(1);
            }
          }
        }
      }).start();
    }
    catch(UnknownHostException u)
    {
      this.out.println("Unknown Host!");
      //System.exit(1);
    }
    catch(IOException i)
    {
      this.out.println("Error while initializing IO Stream!");
      //System.exit(2);
    }
  }
  
  /**
   * 
   */
  @Override
  public void run()
  {
    try
    {
      String line = "";
      while(this.running && 
        !line.equalsIgnoreCase(ServerClientConstants.EXIT_MESSAGE))
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
  
  /**
   * 
   * @throws IOException
   */
  @Override
  public void close() throws IOException
  {
    this.running = false;
    //Any one of these lines may throw an IOExceptio
    this.input.close();
    this.output.close();
    this.socket.close();
  }
  
  /**
   * 
   * @return 
   */
  public String getHost()
  {
    return this.host;
  }
  
  /**
   * 
   * @return
   */
  public String getID()
  {
    return this.identifier;
  }
  
  /**
   * 
   * @return
   */
  public int getPort()
  {
    return this.port;
  }
}
