/*
 * Initializable.java
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

/**
 * <p>A set of descriptions for specific constants within a JTalker application.
 * The list of constants are continually growing, so instead of listing them all
 * out here in the class description, know that this class is a utility class
 * and cannot be extended. If you wish to add more constants, create another
 * utility class for your expanded JTalker application.</p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.0.1
 */
public final class ServerClientConstants
{
  /**
   * Describes the exit command sent from the
   * {@link com.coswald.jtalker.net.TCPClient} to the
   * {@link com.coswald.jtalker.net.TCPServer}.
   */
  public static final String EXIT_MESSAGE = "EXIT";
  
  /**
   * Determines the maximum amount of clients a server can have.
   */
  public static final int MAX_CLIENTS = 8;
  
  /**
   * Shows the maximum port number for a JTalker application.
   */
  public static final int MAX_PORT_NUMBER = 65535;
  
  /**
   * Shows the minimum port number for a JTalker application.
   */
  public static final int MIN_PORT_NUMBER = 0;
  
  /**
   * 
   */
  public static final int TCP_PORT = 5000;
  
  private ServerClientConstants() {}
}
