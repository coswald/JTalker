/*
 * CanvasPanel.java
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * <p>A panel that allows drawing on it using a mouse. This class is basically a
 * {@code JPanel} whos {@link #paintComponent(Graphics) paint} method has been
 * altered to allow the ability to draw colors on it using mouse input. There is
 * a lot of code used and edited with permission from
 * <a href="https://cs.lmu.edu/~ray/notes/javagraphics/">Ray Toal</a> (this 
 * class is essentially the code provided in the link, but edited to work with
 * multiple line strokes and colors). When the canvas is drawn it looks like
 * this:</p>
 * <p align="center">
 * <img src="../../../../img/CanvasPanel.png" alt="" width="400"></p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.1.5
 * @see javax.swing.JPanel
 */
public class CanvasPanel extends JPanel implements Initializable, Colorizer
{
  private static final long serialVersionUID = 1275054493859916224L;
  
  private List<List<Point>> curves;
  private List<Color> colors;
  private Color currentColor;

  /**
   * Creates a {@code CanvasPanel}.
   */
  public CanvasPanel()
  {
    this.curves = new ArrayList<List<Point>>();
    this.colors = new ArrayList<Color>();
    this.currentColor = Color.BLACK;
  }
  
  /**
   * Adds a {@code CurveCreator} to the {@code CanvasPanel}.
   */
  @Override
  public void init()
  {
    CurveCreator cc = new CurveCreator();
    this.addMouseListener(cc);
    this.addMouseMotionListener(cc);
  }
  
  /**
   * Sets the color to draw with. 
   * @param currentColor
   */
  @Override
  public void setColor(Color currentColor)
  {
    this.currentColor = currentColor;
  }
  
  /**
   * Paints the {@code CanvasPanel}. This is where a lot of the graphical heavy
   * lifting is done. Every time a new point is added to a curve, the canvas
   * will redraw itself (calling this method). This will draw all of the points
   * that have been added to the canvas, as well as draw them with the correct
   * colors. It will draw them using lines between each point, and will use the
   * stroke size defined in {@code GUIConstants},
   * {@value com.coswald.jtalker.gui.GUIConstants#STROKE_SIZE}. This code is
   * inspired by
   * <a href="https://programming.guide/java/increasing-line-width.html">this</a>
   * article.
   * @param g The graphics context to use.
   */
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    g2.setStroke(new BasicStroke(GUIConstants.STROKE_SIZE));
    
    /*
     * Note that the line between the zeroeth point and itself is intentional:
     * the code will error out if I were to do zeroeth to first and it didn't
     * have a second point (or it wouldn't draw anything). This will be useful
     * when we use UDP to get point data.
     */
    for(int i = 0; i < this.curves.size(); i++)
    {
      Point previousPoint = this.curves.get(i).get(0);
      g2.setColor(this.colors.get(i));
      for(Point point: this.curves.get(i))
      {
        g2.draw(new Line2D.Float(previousPoint.x, previousPoint.y, point.x,
          point.y));
        previousPoint = point;
      }
    }
  }
  
  /**
   * <p>Creates a curve represented by a set of points based off of user input
   * with the mouse. This is done by two methods:
   * {@link #mousePressed(MouseEvent) pressing} and
   * {@link #mouseDragged(MouseEvent) dragging}. A new curve will start when the
   * mouse button is <b>pressed</b>, and will continue until a new <b>press</b>
   * event is generated. While <b>dragging</b>, points will be added to the
   * curve.</p>
   * @author C. William Oswald
   * @version 0.0.1
   * @since JTalker 0.1.5
   * @see #mousePressed(MouseEvent)
   * @see #mouseDragged(MouseEvent)
   */
  protected class CurveCreator implements MouseListener, MouseMotionListener
  {
    /**
     * Constructs a {@code CurveCreator}.
     */
    public CurveCreator()
    {
    }
    
    /**
     * Invoked when the mouse button has been clicked (pressed and released) on
     * a component. This method is unimplemented (i. e. nothing currently
     * happens).
     * @param e The mouse event.
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    /**
     * Invoked when a mouse button is pressed on a component and then dragged.
     * This will add more {@code Point}s to the curve, and with each point, it
     * will repaint the {@code CanvasPanel}.
     * @param e The mouse event.
     */
    public void mouseDragged(MouseEvent e)
    {
      curves.get(curves.size() - 1).add(new Point(e.getX(), e.getY()));
      repaint(0, 0, getWidth(), getHeight());
    }
     
    /**
     * Invoked when the mouse enters a component. This method is unimplemented
     * (i. e. nothing currently happens).
     * @param e The mouse event.
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    /**
     * Invoked when the mouse exits a component. This method is unimplemented
     * (i. e. nothing currently happens).
     * @param e The mouse event.
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
    }
    
    /**
     * Invoked when the mouse button has been moved on a component (with no
     * buttons no down). This method is unimplemented (i. e. nothing currently
     * happens).
     */
    public void mouseMoved(MouseEvent e)
    {
    }
    
    /**
     * Invoked when a mouse button has been pressed on a component. This will
     * start a new curve, and makes sure the current color is saved as the color
     * of the curve.
     * @param e The mouse event.
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
      ArrayList<Point> newCurve = new ArrayList<Point>();
      newCurve.add(new Point(e.getX(), e.getY()));
      
      curves.add(newCurve);
      colors.add(currentColor);
      
      /*
       * Repaint, becuase if the curve is a single point, it will not be drawn
       * until the next curve is created.
       */
      repaint(0, 0, getWidth(), getHeight());
    }
    
    /**
     * Invoked when a mouse button has been released on a component. This method
     * is unimplemented (i. e. nothing currently happens).
     * @param e The mouse event.
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
    }
  }
}
