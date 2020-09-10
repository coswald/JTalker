/*
 * ServerTest.java
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

import com.coswald.jtalker.net.TCPServer;
import com.coswald.jtalker.net.ServerClientConstants;

/**
 * 
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.0.1
 */
public class ServerTest
{
  private ServerTest()
  {
  }
  
  /**
   * 
   * @param args
   */
  public static void main(String... args)
  {
    TCPServer s = new TCPServer(ServerClientConstants.TCP_PORT);
    s.init();
    (new Thread(s)).run();
  }
}
