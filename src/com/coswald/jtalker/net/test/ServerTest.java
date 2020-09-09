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

import com.coswald.jtalker.net.Server;

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
    Server s = new Server(5000);
    s.init();
    (new Thread(s)).run();
  }
}
