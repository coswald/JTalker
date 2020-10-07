/*
 * ColoredTextPane.java
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

import com.coswald.jtalker.gui.ANSIColorConstants;
import com.coswald.jtalker.gui.GUIConstants;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTextPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * <p>A {@code JTextPane} that supports ANSI colors. This pane adds an
 * {@link #append(String) append} method that will append a given string to the
 * pane; however, it will also understand certain ANSI color codes and 
 * manipulate the pane to these colors. See the
 * {@link com.coswald.jtalker.gui.ANSIColorConstants constants} class to see
 * what codes have been interpreted by this pane. This also makes the pane
 * ignore word wrap.</p>
 * <p>The method for which ANSI color codes can be derived was mainly received
 * by
 * <a href="https://stackoverflow.com/questions/6899282/ansi-colors-in-java-swing-text-fields">this</a>
 * stack overflow question. However, we have since improved on it. When you
 * display the panel, it will look like this:</p>
 * <p align="center">
 * <img src="https://github.com/coswald/JTalker/blob/master/docs/img/ColoredTextPane.png" alt="ColorChooserPanel" width="800">
 * </p></p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.1.5
 * @see com.coswald.jtalker.gui.ANSIColorConstants
 */
public class ColoredTextPane extends JTextPane
{
  private static final long serialVersionUID = 5972869629177225150L;
  
  /**
   * The current color we are using when we are scanning. This is an instance
   * variable because the {@link #append(String) append} method needs a variable
   * to store where it is at while scanning the string. This can be a background
   * color or a foreground color, as determined by {@link #isBackground}.
   */
  protected Color currentColor = ANSIColorConstants.COLOR_RESET;
  
  /**
   * Whether the current color is a background color. This is an instance
   * variable because the {@link #append(String) append} method needs a variable
   * to store whether the current color is a background color while scanning
   * the string.
   */
  protected boolean isBackground = false;
  
  private String remaining = "";
  
  /**
   * Constructs a {@code ColoredTextPane}. This pane is uneditable, and has the
   * default background and foreground as defined in the
   * {@link com.coswald.jtalker.gui.ANSIColorConstants constants} class.
   * Moreover, it also uses the font as defined in the
   * {@link com.coswald.jtalker.gui.GUIConstants#DEFAULT_FONT gui} constants
   * class.
   */
  public ColoredTextPane()
  {
    this.setFont(GUIConstants.DEFAULT_FONT);
    this.setBackground(ANSIColorConstants.BACKGROUND_RESET);
    this.setForeground(ANSIColorConstants.COLOR_RESET);
    this.setEditable(false);
  }
  
  /**
   * Returns {@code true} if a viewport should always force the width of this
   * {@code Scrollable} to match the width of the viewport.
   * @return {@code false} if the {@code ComponentUI}'s preferred width is less
   *   than or equal to the parents width, {@code true} otherwise.
   */
  @Override
  public boolean getScrollableTracksViewportWidth()
  {
    Component parent = this.getParent();
    ComponentUI ui = this.getUI();
    
    return parent != null ? (ui.getPreferredSize(this).width <=
      parent.getSize().width) : true;
  }
  
  /**
   * Appends the given text to the pane. This string may contain ANSI color
   * codes; we interpret them and change the pane accordingly.
   * @param s The string that may contain ANSI color codes.
   */
  public void append(String s)
  {
    this.setEditable(true);
    //currentColor char position in addString
    int aPos = 0;
    //index of next Escape sequence
    int aIndex = 0;
    //index of "m" terminating Escape sequence
    int mIndex = 0;
    String tmpString = "";
    //true until no more Escape sequences
    boolean stillSearching = true;
    String addString = this.remaining + s;
    this.remaining = "";
    
    if(addString.length() > 0)
    {
      //find first escape
      aIndex = addString.indexOf(ANSIColorConstants.ESCAPE_TEXT);
      if(aIndex == -1)
      {
        //no escape code in this string, so just send it with currentColor color
        this.append(addString, this.currentColor, isBackground);
        return;
      }
      
      //otherwise there is an escape character in the string, so we process it
      if(aIndex > 0)
      {
        //Escape is not first char, so send text up to first escape
        tmpString = addString.substring(0, aIndex);
        this.append(tmpString, this.currentColor, isBackground);
        aPos = aIndex;
      }
      
      //aPos is now at the beginning of the first escape sequence
      stillSearching = true;
      while(stillSearching)
      {
        //find the end of the escape sequence
        mIndex = addString.indexOf(ANSIColorConstants.ESCAPE_TEXT_END, aPos); 
        if(mIndex < 0)
        {
          //the buffer ends halfway through the ansi string!
          this.remaining = addString.substring(aPos, addString.length());
          stillSearching = false;
          continue;
        }
        else
        {
          tmpString = addString.substring(aPos, mIndex + 1);
          this.currentColor = ANSIColorConstants.isReset(tmpString) ?
            ANSIColorConstants.COLOR_RESET :
            ANSIColorConstants.getANSIColor(tmpString);
          isBackground = ANSIColorConstants.isBackgroundEscape(tmpString);
          
          if(ANSIColorConstants.isReset(tmpString))
          {
            this.isBackground = false;
            this.append("", ANSIColorConstants.COLOR_RESET, false);
            this.append("", ANSIColorConstants.BACKGROUND_RESET, true);
          }
        }
        
        aPos = mIndex + 1;
        //now we have the color, send text that is in that color
        aIndex = addString.indexOf(ANSIColorConstants.ESCAPE_TEXT, aPos);

        if(aIndex == -1)
        {
          //if that was the last sequence of the input, send remaining text
          tmpString = addString.substring(aPos, addString.length());
          this.append(tmpString, this.currentColor, this.isBackground);
          stillSearching = false;
          //jump out of loop early, as the whole string has been sent
          continue;
        }

        //there is another escape sequence, so send part of the string
        tmpString = addString.substring(aPos, aIndex);
        aPos = aIndex;
        this.append(tmpString, this.currentColor, isBackground);
        //while there's text in the input buffer
      }
    }
    this.setEditable(false);
  }
  
  /**
   * Appends the text to the pane using the given color. This is the main 
   * backbone to the {@link #append(String) append} method. This will use the
   * given color (and whether it is a background color) and the current 
   * {@code AttributeSet} to draw the given text.
   * @param s The text to draw.
   * @param c The color of the text.
   * @param background Whether this is a background or foreground color.
   */
  protected void append(String s, Color c, boolean background)
  {
    StyleContext sc = StyleContext.getDefaultStyleContext();
    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
      background ? StyleConstants.Background : StyleConstants.Foreground, c);
    int len = this.getDocument().getLength();//same value as getText().length();
    this.setCaretPosition(len); //place caret at the end (with no selection)
    this.setCharacterAttributes(aset, false);
    this.replaceSelection(s); //there is no selection, so inserts at caret
  }
}
