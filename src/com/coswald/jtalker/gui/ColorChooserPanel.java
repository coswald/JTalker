/*
 * ColorChooserPanel.java
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
import com.coswald.jtalker.gui.Colorizer;
import com.coswald.jtalker.gui.GUIConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>A panel that allows the selection of one color. This is an redesign of
 * the {@link javax.swing.JColorChooser JColorChooser}. This isn't because the
 * {@code JColorChooser} is flawed; in fact, we re-use much of the design of the
 * {@code JColorChooser}. We just felt that only one color system needed to be
 * present, and that there was no way to visually detect what color had been
 * selected. We fixed this by only showing one color system at a time, and by
 * removing a select few parts of the {@code JColorChooser}. The parts we
 * removed from the chooser were the label and text field that allowed you to
 * manually enter a specific value. We felt this busied the screen, and was
 * unnecessary. The main purpose of this GUI is to get a
 * {@link java.awt.Color Color}, and we do so via the
 * {@link #getColor() getColor} method. When you display the panel, it will look
 * like this:</p>
 * <p align="center">
 * <img src="https://github.com/coswald/JTalker/blob/master/docs/img/ColorChooserPanel.png" alt="ColorChooserPanel" width="400">
 * </p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.1.5
 * @see javax.swing.JColorChooser
 */
public class ColorChooserPanel extends JPanel implements Initializable
{
  private static final long serialVersionUID = 3145007523589759584L;
  
  /**
   * The {@code JColorChooser} used to get the {@code Color}. Note that this
   * panel never actually displays the underlying {@code JColorChooser}; we only
   * use its components within this panel.
   */
  protected JColorChooser colorChooser;
  
  private int colorSystem;
  
  /**
   * Constructs a {@code ColorChooserPanel} with the color system. Note that
   * this does not complete the construction; the {@link #init() init} method
   * must be called. 
   * @param colorSystem The coloring system to use when selecting a color.
   * @see com.coswald.jtalker.gui.GUIConstants#COLOR_SYSTEM
   */
  public ColorChooserPanel(int colorSystem)
  {
    if(colorSystem < GUIConstants.MIN_COLOR_SYSTEM ||
      colorSystem > GUIConstants.MAX_COLOR_SYSTEM)
    {
      throw new IllegalArgumentException("Color system must be a known " +
        "coloring system!");
    }
    
    this.colorSystem = colorSystem;
    this.colorChooser = new JColorChooser();
  }
  
  /**
   * Constructs a {@code ColorChooerPanel} with the default color system. This
   * is currently set to
   * {@value com.coswald.jtalker.gui.GUIConstants#COLOR_SYSTEM}. See that class
   * for the possible values and what they mean.
   */
  public ColorChooserPanel()
  {
    this(GUIConstants.COLOR_SYSTEM);
  }
  
  /**
   * Adds all of the graphical and utility components to this panel. This will
   * add all of the graphical components, as well as the ability for the color
   * up top to change when the underlying {@code JColorChooser} changes its
   * color. A big thanks to stack overflow for this question; I have since lost
   * the link to the code (we have heavily modified it since). We are very proud
   * of the fact that this class just redraws a {@code JColorChooser} the way we
   * want to; however, in order to do so, we had to make sure the systems we
   * used had a compatible way to do so. This is why the zeroeth version of the
   * panels returned by {@code JColorChooser}s
   * {@link javax.swing.JColorChooser#getChooserPanels() panels} method (which
   * allows this hack to work) is not included in the given color systems; it is
   * just too different from the other panels. Due to the fact that each panels
   * components are drawn in the same manner, we are allowed to extract the data
   * from those {@code AbstractColorChooserPanel} using the
   * {@link javax.swing.colorchooser.AbstractColorChooserPanel#getComponents()}
   * method.
   * 
   * This will also set the width of the slider to
   * {@value com.coswald.jtalker.gui.GUIConstants#COLOR_SLIDER_WIDTH}. That was
   * the only GUI element which needed a size set; everything else is already
   * defined by the {@code JColorChooser}.
   */
  @Override
  public void init()
  {
    AbstractColorChooserPanel[] panels = this.colorChooser.getChooserPanels();
    
    panels[this.colorSystem].setBorder(new TitledBorder(
      panels[this.colorSystem].getDisplayName() + " Color Selector"));
    
    Component[] colorPanels = panels[this.colorSystem].getComponents();
    panels[this.colorSystem].setLayout(new BorderLayout());
    panels[this.colorSystem].add(colorPanels[0], BorderLayout.SOUTH);
    colorPanels[colorPanels.length - 2].setPreferredSize(
      new Dimension(GUIConstants.COLOR_SLIDER_WIDTH, 1));
    panels[this.colorSystem].add(colorPanels[colorPanels.length - 1],
      BorderLayout.CENTER);
    panels[this.colorSystem].add(colorPanels[colorPanels.length - 2],
      BorderLayout.EAST);
    
    JPanel banner = new JPanel();
    banner.setBackground(this.colorChooser.getColor());

    this.addChangeListener(new ColorUpdater(banner::setBackground));

    this.setLayout(new BorderLayout(1, 2));
    this.add(panels[this.colorSystem], BorderLayout.CENTER);
    this.add(banner, BorderLayout.NORTH);
    this.setOpaque(true);
  }
  
  /**
   * Adds a change listener to the underlying {@code JColorChooser}.
   * @param cl The new change listener.
   * @see javax.swing.colorchooser.ColorSelectionModel#addChangeListener(ChangeListener)
   */
  public void addChangeListener(ChangeListener cl)
  {
    this.colorChooser.getSelectionModel().addChangeListener(cl);
  }
  
  /**
   * Removes a change listener to the underlying {@code JColorChooser}.
   * @param cl The old change listener.
   * @see javax.swing.colorchooser.ColorSelectionModel#removeChangeListener(ChangeListener)
   */
  public void removeChangeListener(ChangeListener cl)
  {
    this.colorChooser.getSelectionModel().removeChangeListener(cl);
  }
  
  /**
   * Returns the chosen color.
   * @return The chosen color.
   */
  public Color getColor()
  {
    return this.colorChooser.getColor();
  }
  
  /**
   * Updates any color using the underlying
   * {@link #colorChooser chooser's} current color.
   * @author C. William Oswald
   * @version 0.0.1
   * @since JTalker 0.1.5
   */
  public class ColorUpdater implements ChangeListener
  {
    private Colorizer colorizer;
    /**
     * Constructs an updater that updates a given colorizer. This will call the
     * {@code Colorizer}'s
     * {@link com.coswald.jtalker.gui.Colorizer#setColor(Color) setColor} inside
     * of the {@link #stateChanged(ChangeEvent) stateChange} method.
     * @param colorizer The banner to update.
     */
    public ColorUpdater(Colorizer colorizer)
    {
      this.colorizer = colorizer;
    }
    
    /**
     * Updates the given colorizer to the given color. This method is called 
     * whenever the {@code JColorChooser} switches a color.
     * @param e Unused.
     * @see com.coswald.jtalker.gui.ColorChooserPanel#colorChooser
     * @see com.coswald.jtalker.gui.Colorizer
     */
    @Override
    public void stateChanged(ChangeEvent e)
    {
      this.colorizer.setColor(colorChooser.getColor());
    }
  }
}

