/*
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

import com.coswald.jtalker.net.Client;

import java.util.Scanner;

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
    Client c = new Client(identifier, host, 5000);
    c.init();
    (new Thread(c)).run();
  }
}
