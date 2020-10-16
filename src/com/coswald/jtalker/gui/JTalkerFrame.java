/*
 * JTalkerFrame.java
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
import com.coswald.jtalker.gui.CanvasPanel;
import com.coswald.jtalker.gui.ColorChooserPanel;
import com.coswald.jtalker.gui.ColoredTextPane;
import com.coswald.jtalker.gui.GUIConstants;
import com.coswald.jtalker.gui.JTalkerMenuBar;
import com.coswald.jtalker.gui.TextEntryPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * <p>The actual frame of the JTalker application. When it is displayed, it will
 * look like this:</p>
 * <p align="center">
 * <img src="../../../../img/JTalkerFrame.png" alt="" width="1200"></p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.1.5
 */
public class JTalkerFrame extends JFrame implements Initializable
{
  private static final long serialVersionUID = -331076725086090036L;
  
  /**
   * A list of every component within the frame that can be
   * {@link #init() initialized}. No initial components are added until
   * construction, and the list is not initialized until construction as well.
   */
  protected List<Initializable> initializableComponents;
  
  private ColoredTextPane coloredText;
  private TextEntryPanel textEntry;
  private CanvasPanel canvas;
  private ColorChooserPanel colorChooser;
  private JTalkerMenuBar menubar;
  
  /**
   * Constructs a frame with the underlying components given. This will also
   * generate a {@link com.coswald.jtalker.gui.JTalkerMenuBar} associated with
   * the underlying components. This class is an {@code Initializable}, and as
   * such, the {@code init} method should be called for ensuring the frame is
   * actually ready to do the work that it was intended to do.
   * @param coloredText The colored text panel.
   * @param textEntry The text entry panel.
   * @param canvas The canvas panel.
   * @param colorChooser The color chooser panel.
   */
  public JTalkerFrame(ColoredTextPane coloredText, TextEntryPanel textEntry,
    CanvasPanel canvas, ColorChooserPanel colorChooser)
  {
    this.coloredText = coloredText;
    this.textEntry = textEntry;
    this.canvas = canvas;
    this.colorChooser = colorChooser;
    this.menubar = new JTalkerMenuBar(this, this.coloredText, this.textEntry,
      this.canvas);
    
    this.initializableComponents = new ArrayList<Initializable>();
    this.initializableComponents.add(this.textEntry);
    this.initializableComponents.add(this.canvas);
    this.initializableComponents.add(this.colorChooser);
    this.initializableComponents.add(this.menubar);
  }
  
  /**
   * Constructs a frame with brand new underlying components.
   */
  public JTalkerFrame()
  {
    this(new ColoredTextPane(), new TextEntryPanel(), new CanvasPanel(),
      new ColorChooserPanel());
  }
  
  /**
   * Initializes the {@link #initializableComponents sub-components} and then
   * itself. This will set everything in its correct position, ensuring
   * dimensions for the canvas and scaling for the chat panel (to ensure that
   * the canvas doesn't change
   * {@link com.coswald.jtalker.gui.GUIConstants#CANVAS_SIZE size}). The menu
   * bar will have all of its functionality set up, as well as the frames
   * specific dimensions and functionality as well. The dimensions as of now are
   * found at the
   * {@link com.coswald.jtalker.gui.GUIConstants#JTALKER_WIDTH width} and 
   * {@link com.coswald.jtalker.gui.GUIConstants#JTALKER_HEIGHT height} values
   * in the {@code GUIConstants} class. However, this will <b>not</b> make the
   * frame visible.
   */
  @Override
  public void init()
  {
    this.setTitle("JTalker");
    for(Initializable i : this.initializableComponents)
    {
      i.init();
    }
    
    //TCP Panel
    JPanel right = new JPanel(new BorderLayout());
    JScrollPane jsp = new JScrollPane(this.coloredText);
    right.add(jsp, BorderLayout.CENTER);
    right.add(this.textEntry, BorderLayout.SOUTH);
    
    //UDP Panel
    JPanel left = new JPanel(new BorderLayout());
    
    this.colorChooser.addColorUpdater(this.canvas);
    this.colorChooser.setColor(Color.RED);
    
    Dimension canvasDimension = new Dimension(GUIConstants.CANVAS_SIZE,
      GUIConstants.CANVAS_SIZE);
    this.canvas.setSize(canvasDimension);
    this.canvas.setMinimumSize(canvasDimension);
    this.canvas.setMaximumSize(canvasDimension);
    this.canvas.setPreferredSize(canvasDimension);
    
    left.setPreferredSize(new Dimension(GUIConstants.CANVAS_SIZE,
      GUIConstants.JTALKER_HEIGHT));
    left.add(this.colorChooser, BorderLayout.CENTER);
    left.add(this.canvas, BorderLayout.NORTH);
    
    //Add both panels together
    JPanel jp = new JPanel(new BorderLayout());
    jp.add(left, BorderLayout.WEST);
    jp.add(right, BorderLayout.CENTER);
    
    this.setJMenuBar(this.menubar);
    this.getContentPane().add(jp, BorderLayout.CENTER);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(GUIConstants.JTALKER_WIDTH, GUIConstants.JTALKER_HEIGHT);
    //request that the focus be drawn to the text entry panel
    this.textEntry.requestFocus();
  }
}
