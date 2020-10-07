/*
 * ANSIColorConstants.java
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

import java.awt.Color;

/**
 * <p>A set of descriptions for specific color constants within the ANSI 
 * standard. This is a transcription class that allows for Java to understand
 * <a href="https://en.wikipedia.org/wiki/ANSI_escape_code">ANSI color code.</a>
 * For specific values, we use the standards defined within the Windows console
 * version of the ANSI color codes (because it is fairly algorithmic and 
 * understandable). This class also provides a method for allowing the
 * transcription of an escape string to a Java {@code Color} via the
 * {@link #getANSIColor(String) getANSIColor} method.</p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.1.5
 */
public final class ANSIColorConstants
{
  /**
   * The dark black color that is described by ANSI code 30 (or 40 for
   * backgrounds). This has the RGB value of (0, 0, 0).
   */
  public static final Color DARK_BLACK = new Color(0, 0, 0);
  
  /**
   * The dark red color that is described by ANSI code 31 (or 41 for
   * backgrounds). This has the RGB value of (128, 0, 0).
   */
  public static final Color DARK_RED = new Color(128, 0, 0);
  
  /**
   * The dark green color that is described by ANSI code 32 (or 42 for
   * backgrounds). This has the RGB value of (0, 128, 0).
   */
  public static final Color DARK_GREEN = new Color(0, 128, 0);
  
  /**
   * The dark yellow color that is described by ANSI code 33 (or 33 for
   * backgrounds). This has the RGB value of (128, 128, 0).
   */
  public static final Color DARK_YELLOW  = new Color(128, 128, 0);
  
  /**
   * The dark blue color that is described by ANSI code 34 (or 44 for
   * backgrounds). This has the RGB value of (0, 0, 128).
   */
  public static final Color DARK_BLUE = new Color(0, 0, 128);
  
  /**
   * The dark magenta color that is described by ANSI code 35 (or 45 for
   * backgrounds). This has the RGB value of (128, 0, 128).
   */
  public static final Color DARK_MAGENTA = new Color(128, 0, 128);
  
  /**
   * The dark cyan color that is described by ANSI code 36 (or 46 for
   * backgrounds). This has the RGB value of (0, 128, 128).
   */
  public static final Color DARK_CYAN = new Color(0, 128, 128);
  
  /**
   * The dark white color that is described by ANSI code 37 (or 47 for
   * backgrounds). This has the RGB value of (192, 192, 192).
   */
  public static final Color DARK_WHITE = new Color(192, 192, 192);
  
  /**
   * The bright black color that is described by ANSI code 1;30 (or 1;40 for
   * backgrounds). This has the RGB value of (128, 128, 128).
   */
  public static final Color BRIGHT_BLACK = new Color(128, 128, 128);
  
  /**
   * The bright red color that is described by ANSI code 1;31 (or 1;41 for
   * backgrounds). This has the RGB value of (255, 0, 0).
   */
  public static final Color BRIGHT_RED = new Color(255, 0, 0);
  
  /**
   * The bright green color that is described by ANSI code 1;32 (or 1;42 for
   * backgrounds). This has the RGB value of (0, 255, 0).
   */
  public static final Color BRIGHT_GREEN = new Color(0, 255, 0);
  
  /**
   * The bright yellow color that is described by ANSI code 1;33 (or 1;43 for
   * backgrounds). This has the RGB value of (255, 255, 0).
   */
  public static final Color BRIGHT_YELLOW = new Color(255, 255, 0);
  
  /**
   * The bright blue color that is described by ANSI code 1;34 (or 1;44 for
   * backgrounds). This has the RGB value of (0, 0, 255).
   */
  public static final Color BRIGHT_BLUE = new Color(0, 0, 255);
  
  /**
   * The bright magenta color that is described by ANSI code 1;35 (or 1;45 for
   * backgrounds). This has the RGB value of (255, 0, 255).
   */
  public static final Color BRIGHT_MAGENTA = new Color(255, 0, 255);
  
  /**
   * The bright cyan color that is described by ANSI code 1;36 (or 1;46 for
   * backgrounds). This has the RGB value of (0, 255, 255).
   */
  public static final Color BRIGHT_CYAN = new Color(0, 255, 255);
  
  /**
   * The bright white color that is described by ANSI code 1;37 (or 1;47 for
   * backgrounds). This has the RGB value of (255, 255, 255).
   */
  public static final Color BRIGHT_WHITE = new Color(255, 255, 255);
  
  /**
   * The reset color dictated by ANSI code 0 (this dictates the foreground). It
   * is currently set to RGB values (255, 255, 255).
   */
  public static final Color COLOR_RESET = BRIGHT_WHITE;
  
  /**
   * The reset color dictated by ANSI code 0 (this dictates the background) It
   * is currently set to RGB values (16, 16, 16).
   */
  public static final Color BACKGROUND_RESET = new Color(16, 16, 16);
  
  /**
   * The escape character. This character is present before each escape sequence
   * and ensures that we can understand the ANSI code after it.
   */
  public static final String ESCAPE_TEXT = "\u001B";
  
  /**
   * The end of the escape sequence. This is also known as "m".
   */
  public static final String ESCAPE_TEXT_END = "m";
  
  private static final int BACKGROUND_NUMBER = 4;
  
  private ANSIColorConstants()
  {
  }
  
  /**
   * Transforms a character code as a string into an actual {@code Color}. This
   * is done by exact comparison: the parameter given must only be the escape
   * code. By default, {@link #BRIGHT_WHITE white} will be returned. 
   * @param ansiColor The color code.
   * @return A color represented by the given ANSI color code.
   */
  public static Color getANSIColor(String ansiColor)
  {
    switch(ansiColor)
    {
      case ESCAPE_TEXT + "[30m":
      case ESCAPE_TEXT + "[0;30m":
      case ESCAPE_TEXT + "[40m":
      case ESCAPE_TEXT + "[0;40m":
        return DARK_BLACK;
      case ESCAPE_TEXT + "[31m":
      case ESCAPE_TEXT + "[0;31m":
      case ESCAPE_TEXT + "[41m":
      case ESCAPE_TEXT + "[0;41m":
        return DARK_RED;
      case ESCAPE_TEXT + "[32m":
      case ESCAPE_TEXT + "[0;32m":
      case ESCAPE_TEXT + "[42m":
      case ESCAPE_TEXT + "[0;42m":
        return DARK_GREEN;
      case ESCAPE_TEXT + "[33m":
      case ESCAPE_TEXT + "[0;33m":
      case ESCAPE_TEXT + "[43m":
      case ESCAPE_TEXT + "[0;43m":
        return DARK_YELLOW;
      case ESCAPE_TEXT + "[34m":
      case ESCAPE_TEXT + "[0;34m":
      case ESCAPE_TEXT + "[44m":
      case ESCAPE_TEXT + "[0;44m":
        return DARK_BLUE;
      case ESCAPE_TEXT + "[35m":
      case ESCAPE_TEXT + "[0;35m":
      case ESCAPE_TEXT + "[45m":
      case ESCAPE_TEXT + "[0;45m":
        return DARK_MAGENTA;
      case ESCAPE_TEXT + "[36m":
      case ESCAPE_TEXT + "[0;36m":
      case ESCAPE_TEXT + "[46m":
      case ESCAPE_TEXT + "[0;46m":
        return DARK_CYAN;
      case ESCAPE_TEXT + "[37m":
      case ESCAPE_TEXT + "[0;37m":
      case ESCAPE_TEXT + "[47m":
      case ESCAPE_TEXT + "[0;47m":
        return DARK_WHITE;
      case ESCAPE_TEXT + "[1;30m":
      case ESCAPE_TEXT + "[1;40m":
        return BRIGHT_BLACK;
      case ESCAPE_TEXT + "[1;31m":
      case ESCAPE_TEXT + "[1;41m":
        return BRIGHT_RED;
      case ESCAPE_TEXT + "[1;32m":
      case ESCAPE_TEXT + "[1;42m":
        return BRIGHT_GREEN;
      case ESCAPE_TEXT + "[1;33m":
      case ESCAPE_TEXT + "[1;43m":
        return BRIGHT_YELLOW;
      case ESCAPE_TEXT + "[1;34m":
      case ESCAPE_TEXT + "[1;44m":
        return BRIGHT_BLUE;
      case ESCAPE_TEXT + "[1;35m":
      case ESCAPE_TEXT + "[1;45m":
        return BRIGHT_MAGENTA;
      case ESCAPE_TEXT + "[1;36m":
      case ESCAPE_TEXT + "[1;46m":
        return BRIGHT_CYAN;
      case ESCAPE_TEXT + "[1;37m":
      case ESCAPE_TEXT + "[1;47m":
      default:
        return BRIGHT_WHITE;
    }
  }
  
  /**
   * Determines whether a given escape code is a background color escape code.
   * This is done by exact comparison: the parameter given must only be the
   * escape code. If no ANSI escape code is given as a parameter, then 
   * {@code false} is returned.
   * @param ansiColor The color code.
   * @return Whether a given color code is a background code.
   */
  public static boolean isBackgroundEscape(String ansiColor)
  {
    return ansiColor.indexOf(ESCAPE_TEXT_END, 0) > 1 ? 
      Character.getNumericValue(
        ansiColor.charAt(ansiColor.indexOf(ESCAPE_TEXT_END, 0) - 2)) ==
        BACKGROUND_NUMBER : false;
  }
  
  /**
   * Determines whether a given escape code is the reset escape code. This is
   * done by exact comparison: the parameter given must only the the escape
   * code. If a given string is not an ANSI escape code, this method will return
   * {@code false}
   * @param ansiColor The color code.
   * @return Whether a given code is the escape code.
   * @see #COLOR_RESET
   * @see #BACKGROUND_RESET
   */
  public static boolean isReset(String ansiColor)
  {
    return (ESCAPE_TEXT + "[0m").equals(ansiColor);
  }
}
