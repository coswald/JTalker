/*
 * Server.java
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
import com.coswald.jtalker.net.ClientInstance;
import com.coswald.jtalker.net.ServerOutputStream;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors; 
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.0.1
 */
public class Server implements Closeable, Initializable, Runnable
{
  private boolean running;
  private int port;
  private ServerSocket server;
  private ServerOutputStream sos;
  private ThreadPoolExecutor threadPool; 
  
  /**
   * 
   * @param port
   */
  public Server(int port)
  {
    if(port < 0 || port > 65535)
    {
      throw new IllegalArgumentException("Port must be between 0 " +
        "and 65535, inclusive!");
    }
    this.port = port;
    this.running = false;
    this.sos = new ServerOutputStream(System.out);
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
          System.out.println("Could not initialize server!");
        }
      }
    });
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
      System.out.println("JTalker Server started!\nWaiting for a clients...");
      System.out.println("Use standard exiting procedures to quit the server.");
      this.running = true;
    }
    catch(IOException i)
    {
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
        System.out.println("Stopping Server");
        //System.exit(0);
      }
      catch(IOException i)
      {
        i.printStackTrace();
      }  
      
      ClientInstance ci = new ClientInstance(socket, this.sos);
      ci.init();
      
      this.threadPool.execute(ci); 
      System.out.println(this.threadPool.getActiveCount());
    }
  }
  
  /**
   * 
   */
  @Override
  public void close() throws IOException
  {
    System.out.println("\rClosing connection");
    if(this.server != null)
    {
      this.threadPool.shutdown();
      this.server.close();
    }
    this.sos.close();
    //this.socket.close(); //May throw IOException
    //this.input.close(); //May throw IOException
  }
}
