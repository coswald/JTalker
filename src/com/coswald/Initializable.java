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

package com.coswald;

/**
 * Describes an object that must call {@link #init()} before the object can be
 * used. This interface serves as a tag and a methodology: in order for an
 * object to be used, it must be initialized. However, some work (like waiting
 * for a thread to start) must not be used within the constructor. Therefore,
 * this interface provides a way for an object to say "I can be a placeholder,
 * but I haven't been initilized yet". As such, the constructor of the object
 * MUST NOT call he {@link #init()} method. However, we have no such way of
 * checking this, so please consider this as a methodological coding style.
 * 
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker0.0.1
 * @see #init()
 */
public interface Initializable
{
  /**
   * Finishes the construction (or initialization) of an object. This is
   * obviously implementation dependent, but MUST NOT be called within the
   * constructor of a class.
   */
  public void init();
}
