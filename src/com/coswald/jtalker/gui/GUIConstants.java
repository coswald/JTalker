/*
 * GUIConstants.java
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

import java.awt.Font;

/**
 * <p>A set of descriptions for specific GUI constants within a JTalker
 * application. The list of constants are continually growing, so instead of
 * listing them all out here in the class description, know that this class is a
 * utility class and cannot be extended. If you wish to add more constants,
 * create another utility class for your expanded JTalker application.</p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.1.5
 */
public final class GUIConstants
{
  /**
   * Defines the HSL coloring system.
   * @see #COLOR_SYSTEM
   */
  public static final int HSV_COLORING = 1;
  
  /**
   * Defines the HSL coloring system.
   * @see #COLOR_SYSTEM
   */
  public static final int HSL_COLORING = 2;
  
  /**
   * Defines the RGB coloring system.
   * @see #COLOR_SYSTEM
   */
  public static final int RGB_COLORING = 3;
  
  /**
   * Defines the CMYK coloring system.
   * @see #COLOR_SYSTEM
   */
  public static final int CMYK_COLORING = 4;
  
  /**
   * Describes the coloring system to be used by the default constructor in
   * {@link com.coswald.jtalker.gui.ColorChooserPanel ColorChooserPanel}. This
   * is used to determine what coloring scheme (HSV, HSL, RGB, or CMYK) is used
   * to select the color. For more documentation on this, see the 
   * {@link javax.swing.JColorChooser#getChooserPanels() this} documentation.
   * However, the constants are as follows: <ul>
   * <li>HSV = 1</li>
   * <li>HSL = 2</li>
   * <li>RGB = 3</li>
   * <li>CMYK = 4</li>
   * </ul>
   * The current value is {@value}.
   * @see javax.swing.JColorChooser
   * @see com.coswald.jtalker.gui.ColorChooserPanel#init()
   */
  public static final int COLOR_SYSTEM = RGB_COLORING;
  
  /**
   * The maximum number a coloring system can be ({@value}).
   */
  public static final int MAX_COLOR_SYSTEM = 4;
  
  /**
   * The minimum number a coloring system can be ({@value}).
   */
  public static final int MIN_COLOR_SYSTEM = 1;
  
  /**
   * The color chooser's color slider width. This value is {@value}.
   * @see com.coswald.jtalker.gui.ColorChooserPanel
   */
  public static final int COLOR_SLIDER_WIDTH = 25;
  
  /**
   * The size of the stroke on the {@code CanvasPanel}. The value is {@value},
   * and shouldn't be a value that is too large. See
   * {@link java.awt.BasicStroke this} class to understand the usage of this
   * number in its construction.
   * @see com.coswald.jtalker.gui.CanvasPanel
   */
  public static final int STROKE_SIZE = 5;
  
  /**
   * The default font used by the JTalker application. This is currently set to
   * a monospaced font with a font size of 16, and is plain.
   */
  public static final Font DEFAULT_FONT =
    new Font(Font.MONOSPACED, Font.PLAIN, 16);
  
  /**
   * The width of the send button in the JTalker application. This is currently
   * set to {@value}.
   */
  public static final int SEND_BUTTON_WIDTH = 75;
  
  /**
   * The height of the send button in the JTalker application. This is currently
   * set to {@value}.
   */
  public static final int SEND_BUTTON_HEIGHT = 40;
  
  private GUIConstants() {}
}
