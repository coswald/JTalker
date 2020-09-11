/*
 * ClientTest.java
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

package com.coswald.jtalker.net.test;

import java.util.Scanner;

import com.coswald.jtalker.net.TCPClient;
import com.coswald.jtalker.net.ServerClientConstants;

/**
 * <p>A simple client test. When the {@link #main(String[]) main} method is
 * called, the application will prompt the user for a username, an IP to connect
 * to, and then try to connect to the server.</p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.0.1
 * @see #main(String[])
 */
public class ClientTest
{
  private ClientTest()
  {
  }
  
  /**
   * Runs the client test. This will prompt the user for a username, and IP to
   * connect to, and then try to connect to the server. This uses the 
   * {@link com.coswald.jtalker.net.ServerClientConstants#TCP_PORT port} number
   * defined in the <i>ServerClientConstants</i> class.
   * @param args No arguments are used; ignored parameter.
   */
  public static void main(String... args)
  {
    Scanner z = new Scanner(System.in);
    System.out.print("Give me a username: ");
    String identifier = z.nextLine();
    System.out.print("Give me an IP: ");
    String host = z.nextLine();
    TCPClient c = new TCPClient(identifier, host, ServerClientConstants.TCP_PORT);
    c.init();
    (new Thread(c)).run();
  }
}
