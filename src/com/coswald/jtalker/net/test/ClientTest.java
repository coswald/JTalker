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

import com.coswald.jtalker.net.ServerClientConstants;
import com.coswald.jtalker.net.TCPClient;

import java.util.Scanner;

/**
 * Tests {@link com.coswald.jtalker.net.TCPClient TCPClient}. This will test the
 * client until the user doesn't want to connect to any more servers.
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.1.0
 */
public final class ClientTest
{
  private static final int SLEEP_TIME = 5000;
  
  private static final String USER_PROMPT = "Give me a username: ";
  private static final String IP_PROMPT = "Give me an IP: ";
  private static final String YES = "y";

  private ClientTest()
  {
  }
  
  /**
   * Starts the test. This will ask the user for a user name, a host address,
   * and then automatically connect to the server specified. Once they exit, it
   * will ask if they want to join another server. If they do, the process
   * repeats. If not, the client shuts down and the application is exited. The
   * port the socket binds to is
   * {@value com.coswald.jtalker.net.ServerClientConstants#TCP_PORT}.
   * @param args Not used.
   */
  public static void main(String... args)
  {
    Scanner z = new Scanner(System.in);
    System.out.print(USER_PROMPT);
    String identifier = z.nextLine();
    System.out.print(IP_PROMPT);
    String host = z.nextLine();
    TCPClient c = new TCPClient(identifier, host,
      ServerClientConstants.TCP_PORT);
    c.init();
    
    String yesno = YES;
    while(yesno.equalsIgnoreCase(YES))
    {
      (new Thread(c)).run();
      while(c.isRunning())
      {
        try
        {
          Thread.sleep(SLEEP_TIME);
        }
        catch(InterruptedException ie)
        {
          ie.printStackTrace();
        }
      }
      System.out.println("Continue (Y/N)?: ");
      if(z.hasNextLine())
      {
        yesno = z.nextLine();
      }
      if(yesno.equalsIgnoreCase(YES))
      {
        System.out.print(USER_PROMPT);
        identifier = z.nextLine();
        System.out.print(IP_PROMPT);
        host = z.nextLine();
        c.setID(identifier);
        c.setHost(host);
        c.init();
      }
    }
  }
}
