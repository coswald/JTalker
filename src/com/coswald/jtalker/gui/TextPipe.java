/*
 * TextPipe.java
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

package com.coswald.jtalker.gui;

/**
 * <p>Sends text along from one are to another. This allows an implementation 
 * independent way for a class to send text between two areas. This interface
 * can be used to send a method as a parameter, like in
 * {@link com.coswald.jtalker.gui.TextEntryPanel this} class. 
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.1.5
 * @see #sendText(String)
 */
public interface TextPipe
{
  /**
   * Sends the given text along the pipe.
   * @param text The text to send along the pipe.
   */
  public void sendText(String text);
}
