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
 * 
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.0.1
 */
public class ClientTest
{
  private ClientTest()
  {
  }
  
  /**
   * 
   * @param args
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
