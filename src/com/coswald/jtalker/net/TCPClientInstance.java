/*
 * TCPClientInstance.java
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

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;

import com.coswald.jtalker.Initializable;
import com.coswald.jtalker.net.ServerClientConstants;
import com.coswald.jtalker.net.ServerOutputStream;

/**
 * <p>A listener that broadcasts all of what it "hears" from one client to the
 * rest. This class determines the servers broadcast logic. An instance of this
 * class will listen on one socket (a client) and send whatever it hears back
 * to the other clients. That is why it is called a "client" instance: it is the
 * part of the server that is listening on to one client. How it does this is
 * very simplistic: it listens using the
 * {@link java.io.DataInputStream#readUTF() readUTF} method, and outputs the
 * data it receives via the
 * {@link com.coswald.jtalker.net.ServerOutputStream#writeUTF(String) writeUTF}
 * method. This is all done within the {@link #run() run} method, which will
 * automatically {@link #close() close} the listener. This closing only removes
 * the socket's output stream from the {@code ServerOutputStream}; it does not
 * close the {@code ServerOutputStream}. This is good, as there still may be
 * other client instances running besides this one, using the same
 * {@code ServerOutputStream}.</p>
 * <p>As an aside, please <b>do not</b> call the {@link #init() init} method.
 * This is called within the {@link #run() run} method.</p>
 * @author C. William Oswald
 * @version 0.0.2
 * @since JTalker 0.0.1
 */
public class TCPClientInstance implements Closeable, Initializable, Runnable
{
  /**
   * The socket used for data I/O.
   */
  protected Socket socket;
  
  /**
   * The input stream used for socket input.
   */
  protected DataInputStream input;
  
  /**
   * The output stream (where we send all the data we receive).
   */
  protected ServerOutputStream output;
  
  private DataOutputStream fakeOutput;
  private String identifier;
  
  /**
   * Constructs a client instance with the given socket and output stream.
   * @param socket The socket to use when listening.
   * @param output The output to resend all of our input to.
   */
  public TCPClientInstance(Socket socket, ServerOutputStream output)
  {
    this.socket = socket;
    this.output = output;
  }
  
  /**
   * Initializes the client instance. Note that this method <b>should not</b> be
   * called by anything other than the {@link #run() run} method. This method
   * will create a {@code DataInputStream} form the socket's input stream, a
   * {@code DataOutputStream} from the socket's output stream, and add that 
   * output stream to the {@code ServerOutputStream} associated with the server.
   * It will then write a boolean ({@code true}) to the output stream and listen
   * for a unique identifier. Once the identifier is received, it is ready for
   * listening within the {@link run() run} method.
   */
  @Override
  public final void init()
  {
    try
    {
      this.input = new DataInputStream(
        new BufferedInputStream(this.socket.getInputStream()));
      this.fakeOutput = new DataOutputStream(this.socket.getOutputStream());
      this.output.add(this.fakeOutput);
      
      //Tell the client it is connected
      this.fakeOutput.writeBoolean(true);
      
      //read the identifier from the client
      this.identifier = this.input.readUTF();
      //System.out.println(this.identifier + " has been accepted!");
    }
    catch(IOException i)
    {
      i.printStackTrace();
      return;
    }
  }
  
  /**
   * Runs the client instance by calling the {@link #init() init} method, and
   * handles message logic. This will wait until the exit message
   * {@value com.coswald.jtalker.net.ServerClientConstants#EXIT_MESSAGE} is
   * sent through the socket. Until this happens, the client instance will
   * listen for input from its client. When it receives input, it will send it
   * through a {@code ServerOutputStream} (which will send it to the rest of the
   * clients and including this one). Once this client receives the exit
   * message, it will call the {@link #close() close} method.
   * 
   * When it receives a message, the client instance will output the same
   * message; however, it adds the {@link #getID() identifier}, a colon, a
   * space, and then the message. It will then also append a carriage return and
   * a newline.
   * @see #init()
   * @see #close()
   * @see com.coswald.jtalker.net.ServerOutputStream
   */
  @Override
  public void run()
  {
    this.init();
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
  
  /**
   * Removes the {@code DataOutputStream} associated with this instance from
   * the {@code ServerOutputStream}, writes a goodbye message to the 
   * {@code ServerOUtputStream}, closes the socket and the socket input.
   * @throws IOException If any of the {@code close()} methods threw an error.
   */
  @Override
  public void close() throws IOException
  {
    if(this.socket != null && this.socket != null)
    {
      this.output.remove(this.fakeOutput);
      this.output.writeUTF(this.identifier + " has left the chat.\n\r");
      this.input.close();
      this.socket.close();
    }
  }
  
  /**
   * Returns the unique identifier of the client instance.
   * @return The unique identifier.
   */
  public String getID()
  {
    return this.identifier;
  }
}
