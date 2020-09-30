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

import com.coswald.jtalker.Initializable;
import com.coswald.jtalker.net.ServerOutputStream;
import com.coswald.jtalker.net.TCPClientInstance;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors; 
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>A TCP server for the JTalker application. This class defines everything
 * you would need to get a server and a client to talk, including the socket 
 * programming and the method that JTalker uses to exit and enter. This class
 * mainly deals with adding {@code TCPClientInstance}s to a
 * {@link java.util.concurrent.ThreadPoolExecutor ThreadPoolExecutor}. The logic
 * that is used to send messages to multiple clients is handled within
 * {@code ServerOutputStream}, and this is passed to each
 * {@code TCPClientInstance} to make sure that they can send messages to each
 * other.</p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.0.1
 * @see com.coswald.jtalker.net.TCPClientInstance
 * @see com.coswald.jtalker.net.ServerOutputStream
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
   * Constructs a server with the given output stream and the given port. No
   * parameter can be null, and the port must not only be a valid port, but it
   * also must be within the port range specified by the
   * {@link com.coswald.jtalker.net.ServerClientConstants#MIN_PORT_NUMBER min}
   * and
   * {@link com.coswald.jtalker.net.ServerClientConstants#MAX_PORT_NUMBER max}
   * port numbers (
   * {@value com.coswald.jtalker.net.ServerClientConstants#MIN_PORT_NUMBER} and
   * {@value com.coswald.jtalker.net.ServerClientConstants#MAX_PORT_NUMBER}).
   * @param out The output stream to send server messages to.
   * @param port The port to bind to.
   * @throws IllegalARgumentException If any parameter is {@code null}, or the
   * port is outside the given bounds.
   */
  public TCPServer(PrintStream out, int port)
  {
    if(out == null)
    {
      throw new IllegalArgumentException("No argument can be null!");
    }
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
          out.println("Could not shutdown server!");
        }
      }
    });
  }
  
  
  /**
   * Constructs a server with {@link java.lang.System#out System.out} as the
   * default output and the given port number.
   * @param port The port to bind to.
   */
  public TCPServer(int port)
  {
    this(System.out, port);
  }
  
  /**
   * Initializes the server by binding a {@code ServerSocket} to a port. This
   * will also send messages to the output stream provided to us. These messages
   * will let the user know that the JTalker server has started.
   * @see java.net.ServerSocket
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
   * Waits for clients to connect and handles them when they do. This logic
   * will accept client up until the maximum amount of clients has been reached,
   * which is
   * {@value com.coswald.jtalker.net.ServerClientConstants#MAX_CLIENTS}.
   * With each new {@code Socket} that is created, it will add a
   * {@link com.coswald.jtalker.net.TCPClientInstance TCPClientInstance}
   * and add it to a fixed thread pool. This thread pool takes care of the logic
   * of switching between multiple clients and also helps with thread
   * management (it will call the {@code run} method). As to the output, the
   * {@link com.coswald.jtalker.net.ServerOutputStream ServerOutputStream} is
   * passed to the {@code TCPClientInstance}, and that class takes care of the
   * output and input.
   * @see com.coswald.jtalker.net.ServerOutputStream
   * @see com.coswald.jtalker.net.TCPClientInstance#run()
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
        TCPClientInstance ci = new TCPClientInstance(socket, this.sos);
      
        this.threadPool.execute(ci); 
      }
      //this.out.println(this.threadPool.getActiveCount());
    }
  }
  
  /**
   * Shuts down the threadpool associated with the clients, the server output
   * stream, as well as the server socket.
   * @see java.util.concurrent.ThreadPoolExecutor#shutdown()
   * @see java.net.ServerSocket#close()
   * @see com.coswald.jtalker.net.ServerOutputStream#close()
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
   * Returns the amount of active clients connected to the server.
   * @return The amount of active clients.
   */
  public int getActiveClients()
  {
    return this.threadPool.getActiveCount();
  }
  
  /**
   * Returns the port number our server will or has binded to.
   * @return The port number.
   */
  public int getPort()
  {
    return this.port;
  }
  
  /**
   * Returns the boolean value that represents whether the server is running.
   * @return {@code true} when the server is running, {@code false} otherwise.
   */
  public boolean isRunning()
  {
    return this.running;
  }
  
  /**
   * Sets the port number our server will or has binded to. Note that this
   * method will not change the port if we are currently bound to a port.
   * @param port The port number we are binding the TCP socket to.
   * @see #isRunning()
   */
  public void setPort(int port)
  {
    if(!this.running)
    {
      this.port = port;
    }
  }
}
