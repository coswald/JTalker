/*
 * TCPClient.java
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
 * <p>A TCP client for the JTalker application. This class defines everything
 * you would need to get a server and a client to talk, including the socket 
 * programming and the method that JTalker uses to exit and enter. There are two
 * things of note. The {@code Runnable} code in this class is used as the
 * sending portion of the client; the listening part of the client is defined
 * within the {@link #init() init} method. The listening {@code Thread} is
 * started as soon as the init method is called; the sending portion is only
 * initiated after the {@link #run() run} method is called within a thread. This
 * means that for every client, there are two threads running.</p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.0.1
 */
public class TCPClient implements Closeable, Initializable, Runnable
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
   * Constructs a {@code TCPClient} with the given input, output, client
   * identifier, host, and port number. No parameter can be null, and the
   * identifier <b>cannot</b> be the
   * {@link com.coswald.jtalker.net.ServerClientConstants#EXIT_MESSAGE exit}
   * message , which is
   * {@value com.coswald.jtalker.net.ServerClientConstants#EXIT_MESSAGE} (using
   * the
   * {@link java.lang.String#equalsIgnoreCase(String) equalsIgnoreCase} method).
   * The port must not only be a valid port, but it also must be within the 
   * port range specified by the
   * {@link com.coswald.jtalker.net.ServerClientConstants#MIN_PORT_NUMBER min}
   * and
   * {@link com.coswald.jtalker.net.ServerClientConstants#MAX_PORT_NUMBER max}
   * port numbers (
   * {@value com.coswald.jtalker.net.ServerClientConstants#MIN_PORT_NUMBER} and
   * {@value com.coswald.jtalker.net.ServerClientConstants#MAX_PORT_NUMBER}).
   * @param in The input stream to use when sending data over a socket.
   * @param out The output stream to send data to when the client receives it.
   * @param identifier The unique identifier to be used by the server.
   * @param host The host we are connecting to.
   * @param port The port number we are binding the TCP socket to.
   * @throws IllegalArgumentException If any parameter is {@code null}, or if
   *  the port is outside of the given bounds, or if the identifier is equal to
   *  the exit message.
   */
  public TCPClient(InputStream in, PrintStream out, String identifier,
    String host, int port)
  {
    if(in == null || out == null || identifier == null || host == null)
    {
      throw new IllegalArgumentException("No parameter can be null!");
    }
    if(port < ServerClientConstants.MIN_PORT_NUMBER ||
       port > ServerClientConstants.MAX_PORT_NUMBER)
    {
      throw new IllegalArgumentException("Port must be between " +
        ServerClientConstants.MIN_PORT_NUMBER +
        " and " + ServerClientConstants.MAX_PORT_NUMBER + ", inclusive!");
    }
    if(identifier.equalsIgnoreCase(ServerClientConstants.EXIT_MESSAGE))
    {
      throw new IllegalArgumentException("Identifier must not be the exit " +
        "message!");
    }
    this.identifier = identifier;
    this.host = host;
    this.port = port;
    this.running = false;
    
    this.in = in;
    this.out = out;
  }
  
  /**
   * Constructs a TCP client with {@link java.lang.System#in} as input, 
   * {@link java.lang.System#out} as output, and the rest of the parameters.
   * This will call the {@link #TCPClient(String, String, int) large}
   * constructor with the {@code in} and {@code out} fields as mentioned before.
   * @param identifier The unique identifier to be used by the server.
   * @param host The host we are connecting to.
   * @param port The port number we are binding the TCP socket to.
   * @see #TCPClient(InputStream, PrintStream, String, String, int)
   */
  public TCPClient(String identifier, String host, int port)
  {
    this(System.in, System.out, identifier, host, port);
  }
  
  /**
   * Initializes the client by constructing the socket, creating its input and
   * output streams, waits to be connected, receives a boolean value from the
   * server, sends our unique identifier to the server, and starts the listening
   * thread. Within the listening thread is the logic used by the client to
   * detect when connection has been lost, and within the socket/stream
   * creation is the logic used to detect when an unknown host or an I/O error
   * occurs. In both cases, the client will stop by printint out a message. No
   * exception is thrown, so if a client does not connect within this method, a
   * new client must be created. Now the {@code init} method may be called again
   * as long as the information is correct.
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
   * Runs the sending thread if we have been initialized. Note that the running
   * method will only work if the {@link #init() init} method has been called,
   * and there is a guard in place just in case the init method hasn't been
   * called. This method will automatically call the {@link #close() close}
   * method after the
   * {@link com.coswald.jtalker.net.ServerClientConstants#EXIT_MESSAGE exit}
   * message has been received.
   * @see com.coswald.jtalker.net.ServerOutputStream#writeUTF(String)
   */
  @Override
  public void run()
  {
    try
    {
      String line = "";
      while(this.running && 
        !ServerClientConstants.EXIT_MESSAGE.equalsIgnoreCase(line))
      {
        //System.out.print(this.identifier + ": ");
        line = this.input.readLine();
        if(line != null)
        {
          this.output.writeUTF(line);
        }
      }
      this.close();
    }
    catch(IOException i)
    {
      i.printStackTrace();
    }
  }
  
  /**
   * Closes all of the associated streams and sockets associated with a client.
   * This will make sure we are not {@code running}, close the
   * {@code BufferedReader} we created from the {@code in} parameter, the socket
   * output stream, the socket input stream, and the socket itself. It will
   * <b>not</b> close the {@code in} and {@code out} parameters that were given
   * to the {@code TCPClient} when constructing the object. This means that
   * there is a potential for a memory leak if a client is initialized over and
   * over again; because the {@code BufferedReader} is not closed, it is not
   * explicitly dealt with. It is up to the programmer to deal with this issue
   * and close the underlying {@code in} resource, and to handle any other
   * memory leaks that may occur.
   * @throws IOException If and I/O error occurs
   * @see #TCPClient(InputStream, PrintStream, String, String, int)
   */
  @Override
  public void close() throws IOException
  {
    //Any one of these lines may throw an IOException
    //this.input.close();
    this.output.close();
    this.serverInput.close();
    this.socket.close();
    this.running = false;
  }
  
  /**
   * Returns the host we want to or are already connected to.
   * @return The host.
   */
  public String getHost()
  {
    return this.host;
  }
  
  /**
   * Returns the unique identifier we will or are using on the host.
   * @return The unique identifier.
   */
  public String getID()
  {
    return this.identifier;
  }
  
  /**
   * Returns the port number our socket will or has binded to.
   * @return The port number.
   */
  public int getPort()
  {
    return this.port;
  }
  
  /**
   * Returns the boolean value that represents whether the client is running.
   * @return {@code true} when the client is running, {@code false} otherwise.
   */
  public boolean isRunning()
  {
    return this.running;
  }
  
  /**
   * Sets the host we want to or are already connected to. Note that this method
   * will not change the host if we are currently connected to a host.
   * @param host The host we are connecting to.
   */
  public void setHost(String host)
  {
    if(!this.running)
    {
      this.host = host;
    }
  }
  
  /**
   * Sets the unique identifier we will or are using on the host. Note that this
   * method will not change the identifier if we are currently connected to a
   * host.
   * @param identifier The unique identifier to be used by the server.
   */
  public void setID(String identifier)
  {
    if(!this.running)
    {
      this.identifier = identifier;
    }
  }
  
  /**
   * Sets the port number our socket will or has binded to. Note that this
   * method will not change the port if we are currently connected to a host.
   * @param port The port number we are binding the TCP socket to.
   */
  public void setPort(int port)
  {
    if(!this.running)
    {
      this.port = port;
    }
  }
}
