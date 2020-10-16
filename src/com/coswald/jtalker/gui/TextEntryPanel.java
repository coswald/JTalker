/*
 * TextEntryPanel.java
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

import com.coswald.jtalker.Initializable;
import com.coswald.jtalker.gui.GUIConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * <p>A panel that allows entry and sending of text. This consists of two parts:
 * entry and sending. Entry is simple: we use a {@link #entryField JTextField}
 * to enter in text, and said text is cleared when the enter button is pressed.
 * However, "sending" is more complicated. We have created an interface called
 * {@link com.coswald.jtalker.gui.TextPipe TextPipe}, in which we allow the
 * sending of specific text. If we were to visualize how this panel "sends"
 * text, it would be like a bunch of pipes branching out from a single entry
 * point. This entry point would be stopped by the "send" button, and when it is
 * pressed, the pipe is opened (and susequently shut). We allow the installation
 * of new pipes via the {@link #addTextUpdater(TextPipe)} method. When you
 * display the panel, it will look like this:</p>
 * <p align="center">
 * <img src="../../../../img/TextEntryPanel.png" alt="" width="800"></p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.1.5
 * @see com.coswald.jtalker.gui.TextPipe
 */
public class TextEntryPanel extends JPanel implements Initializable, KeyListener
{
  private static final long serialVersionUID = 9067867331672782532L;
  /**
   * The text entry field of the panel. This allows the text to be entered, and
   * when the class is initialized, its text will be sent along any
   * {@code TextPipe} given to this panel (and then subsiquently cleared).
   */
  protected JTextField entryField;
  
  /**
   * The send buttong of the panel. This allows the text to actually be sent
   * along the {@code TextPipe}s provided by the {@code TextUpdater}s that are
   * added to this button as {@code ActionListener}s.
   */
  protected JButton sendButton;
  
  /**
   * Constructs a {@code TextEntryPanel}. Note that you still have to call the
   * {@link #init() init} method in order for anything to work.
   */
  public TextEntryPanel()
  {
    this.entryField = new JTextField();
    this.sendButton = new JButton("Send");
  }
  
  /**
   * Initializes the entry panel by adding the components and making sure that
   * the enter key also pushes the button. The sizes for the button as well as
   * the height of the entry panel are defined in {@code GUIConstants}:
   * {@link com.coswald.jtalker.gui.GUIConstants#SEND_BUTTON_WIDTH width} and
   * {@link com.coswald.jtalker.gui.GUIConstants#SEND_BUTTON_HEIGHT height}.
   * The text within the {@link #entryField text} entry field is set to the
   * default {@link com.coswald.jtalker.gui.GUIConstants#DEFAULT_FONT font}.
   * 
   */
  @Override
  public void init()
  {
    this.setLayout(new BorderLayout());
    entryField.setFont(GUIConstants.DEFAULT_FONT);
    
    this.entryField.addKeyListener(this);
    
    this.sendButton.setPreferredSize(
      new Dimension(GUIConstants.SEND_BUTTON_WIDTH,
      GUIConstants.SEND_BUTTON_HEIGHT));
    this.setPreferredSize(new Dimension(1, GUIConstants.SEND_BUTTON_HEIGHT));
    
    this.add(this.entryField, BorderLayout.CENTER);
    this.add(this.sendButton, BorderLayout.EAST);
    
    //Okay. So we want to clear text no matter what, so add that actionlistener
    //this.sendButton.addActionListener(e -> this.entryField.setText(""));
  }
  
  /**
   * Invoked when a key has been pressed. When and enter is pressed, it will
   * click the {@link #sendButton send} button, therefore activating its
   * action performers.
   * @param e The key event (hopefully an enter).
   */
  @Override
  public void keyPressed(KeyEvent e)
  {
    if(e.getKeyCode() == KeyEvent.VK_ENTER)
    {
      this.sendButton.doClick();
      this.entryField.setText("");
    }
  }
  
  /**
   * Invoked when a key has been released. This is unimplemented.
   * @param e The key event to ignore.
   */
  @Override
  public void keyReleased(KeyEvent e)
  {
  }
  
  /**
   * Invoked when a key has been typed. This is unimplemented.
   * @param e the key event ot ignore.
   */
  @Override
  public void keyTyped(KeyEvent e)
  {
  }
  
  /**
   * Requests that this {@code TextEntryPanel} gets the input focus. This will
   * call the underlying {@code JTextField}'s {@code requestFocus} method.
   */
  @Override
  public void requestFocus()
  {
    this.entryField.requestFocus();
  }
  
  /**
   * Adds a {@code TextUpdater} to this {@code TextEntryPanel}.
   * @param pipe The text pipe to make a {@code TextUpdater} out of.
   */
  public void addTextUpdater(TextPipe pipe)
  {
    /*
    ActionListener[] actions = this.sendButton.getActionListeners();
    for(ActionListener action : actions)
    {
      this.sendButton.removeActionListener(action);
    }
    this.sendButton.addActionListener(actions[0]);
    */
    this.sendButton.addActionListener(new TextUpdater(pipe));
    /*
    for(int i = 1; i < actions.length; i++)
    {
      this.sendButton.addActionListener(actions[i]);
    }
    */
  }
  
  /**
   * Returns the {@code JTextField} that is used for text entry.
   * @return The {@code JTextField} that is used for text entry.
   */
  public JTextField getTextField()
  {
    return this.entryField;
  }
  
  /**
   * Returns the {@code JButton} that is used for sending text.
   * @return The {@code JButton} that is used for sending text.
   */
  public JButton getButton()
  {
    return this.sendButton;
  }
  
  /**
   * Updates any text pipe using the underlying text
   * {@link #entryField field's} current text. If you wish to use this
   * class within a {@code TextEntryPanel}, use
   * {@link com.coswald.jtalker.gui.TextEntryPanel#addTextUpdater(TextPipe) this}
   * method.
   * @author C. William Oswald
   * @version 0.0.1
   * @since JTalker 0.1.5
   */
  protected class TextUpdater implements ActionListener
  {
    private TextPipe pipe;
    
    /**
     * Constructs an updater that updates a given text pipe. This will call the
     * {@code TextPipe}'s
     * {@link com.coswald.jtalker.gui.TextPipe#sendText(String) send} method
     * inside of the {@link #actionPerformed(ActionEvent) action} method.
     * @param pipe The pipe to send the string along.
     */
    public TextUpdater(TextPipe pipe)
    {
      this.pipe = pipe;
    }
    
    /**
     * Sends a string to the given text pipe. This method is called 
     * whenever the send {@link #sendButton button} is pressed. However, it will
     * not send text along the pipe if the text is just an empty string.
     * @param e Unused.
     * @see com.coswald.jtalker.gui.TextEntryPanel#sendButton
     * @see com.coswald.jtalker.gui.TextPipe
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
      if(!"".equals(entryField.getText()))
      {
        this.pipe.sendText(entryField.getText());
        //entryField.setText("");
        /*
         * We originally cleared the text from the text panel, but this
         * disabled any other text updaters (because the text was cleared). As
         * such, we added an action to clear the text at the end of the actions,
         * and made sure that order was preserved.
         */
      }
    }
  }
}
