/*
 * TCPServer.java
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

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors; 
import java.util.concurrent.ThreadPoolExecutor;

import com.coswald.jtalker.Initializable;
import com.coswald.jtalker.net.TCPClientInstance;
import com.coswald.jtalker.net.ServerOutputStream;

/**
 *
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.0.1
 */
public class TCPServer implements Closeable, Initializable, Runnable
{
  private boolean running;
  private int port;
  private ServerSocket server;
  private ServerOutputStream sos;
  private ThreadPoolExecutor threadPool; 
  
  private PrintStream out;
  
  /**
   * 
   * @param out
   * @param port
   */
  public TCPServer(PrintStream out, int port)
  {
    if(port < ServerClientConstants.MIN_PORT_NUMBER ||
       port > ServerClientConstants.MAX_PORT_NUMBER)
    {
      throw new IllegalArgumentException("Port must be between " +
        ServerClientConstants.MIN_PORT_NUMBER +
        " and " + ServerClientConstants.MAX_PORT_NUMBER + ", inclusive!");
    }
    this.out = out;
    this.port = port;
    this.running = false;
    this.sos = new ServerOutputStream(this.out);
    this.threadPool = (ThreadPoolExecutor)Executors.newFixedThreadPool(
      ServerClientConstants.MAX_CLIENTS);
    
    //Control c will shutdown the server
    Runtime.getRuntime().addShutdownHook(new Thread()
    {
      public void run()
      {
        running = false;
        try
        {
          close();
        }
        catch(IOException i)
        {
          out.println("Could not initialize server!");
        }
      }
    });
  }
  
  
  /**
   * 
   * @param port
   */
  public TCPServer(int port)
  {
    this(System.out, port);
  }
  
  /**
   * 
   */
  @Override
  public void init()
  {
    try
    {
      this.server = new ServerSocket(port);
      this.out.println("JTalker TCP Server started!\nWaiting for a clients...");
      this.out.println("Use standard exiting procedures to quit the server.");
      this.running = true;
    }
    catch(IOException i)
    {
      this.out.println("Could not initialize the server!");
      i.printStackTrace();
    }
  }
  
  /**
   * 
   */
  @Override
  public void run()
  {
    while(this.running)
    {
      Socket socket = null;
      try
      {
        socket = this.server.accept();
      }
      catch(SocketException s)
      {
        this.out.println("Stopping TCP Server");
        //System.exit(0);
      }
      catch(IOException i)
      {
        i.printStackTrace();
      }  
      
      if(socket != null)
      {
        TCPClientInstance ci = new TCPClientInstance(this.out, socket, this.sos);
      
        this.threadPool.execute(ci); 
      }
      //this.out.println(this.threadPool.getActiveCount());
    }
  }
  
  /**
   * 
   */
  @Override
  public void close() throws IOException
  {
    this.out.println("\rClosing connection");
    if(this.server != null)
    {
      this.threadPool.shutdown();
      this.server.close();
    }
    this.sos.close();
  }
  
  /**
   * 
   * @return
   */
  public int getActiveClients()
  {
    return this.threadPool.getActiveCount();
  }
}