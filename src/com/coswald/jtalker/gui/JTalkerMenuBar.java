/*
 * JTalkerMenuBar.java
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
import com.coswald.jtalker.gui.ColoredTextPane;
import com.coswald.jtalker.gui.GUIConstants;
import com.coswald.jtalker.gui.TextEntryPanel;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * <p>A menu bar for the JTalker application. This menu bar has five menus: 
 * file, edit, view, settings, and help. Each menu is separated by the functions
 * of the software (file managment, editing of content, viewing of content, 
 * specific settings of content, and help with use of content). This is fairly
 * standard, and as such standard keyboard shortcuts are used for each action.
 * When you display the menu bar, it will look like this:</p>
 * <p align="center">
 * <img src="../../../../img/JTalkerMenuBar.png" alt="" width="1200"></p>
 * @author C. William Oswald
 * @version 0.0.1
 * @since JTalker 0.1.5
 */
public class JTalkerMenuBar extends JMenuBar implements Initializable
{
  /**
   * A map between the name of a menu item and the keystroke that activates it.
   * @see #createMenuItem(String, int, int, int, ActionListener)
   * @see #createButtonMenuItem(String, boolean, int, int, int, ActionListener)
   */
  protected static Map<String, KeyStroke> SHORTCUTS =
    new TreeMap<String, KeyStroke>();
  
  private static final long serialVersionUID = -2397164197833367724L;
  
  private JFrame frame;
  private ColoredTextPane coloredTextPane;
  private TextEntryPanel textEntryPanel;
  private CanvasPanel canvasPanel;
  
  /**
   * Initializes the menu bar. The four components that are needed for the menu
   * bar to work effectively are the {@code JFrame}, {@code ColoredTextPane},
   * {@code TextEntryPanel}, and the {@code CanvasPanel}. These things work in
   * tandom to make sure each action described by this class is able to work.
   * @param frame The frame of the application.
   * @param coloredTextPane The colored text pane of the application.
   * @param textEntryPanel The text entry panel of the application.
   * @param canvasPanel The canvas panel of the application.
   */
  public JTalkerMenuBar(JFrame frame, ColoredTextPane coloredTextPane,
    TextEntryPanel textEntryPanel, CanvasPanel canvasPanel)
  {
    this.frame = frame;
    this.coloredTextPane = coloredTextPane;
    this.textEntryPanel = textEntryPanel;
    this.canvasPanel = canvasPanel;
  }
  
  /**
   * Creates a {@code JMenuItem} to be added to the {@code JTalkerMenuBar}. This
   * will create a menu item using the given name, the keystroke, mnemonic, and
   * action associated with it. When it creates the menu item, it will add the
   * keystroke to a {@link #SHORTCUTS map} which can be used for later. If you
   * wish to not add a keystroke, just make sure that the accelerator and/or
   * mask variable is set to a number smaller than zero. <b>However</b>, the
   * mnemonic is <b>not</b> optional.
   * @param name The title of the menu item.
   * @param accelerator The accelerator of the menu item.
   * @param mask The mask of the menu item.
   * @param mnemonic The mnemonic of the menu item.
   * @param action The action to do when the menu item is pressed.
   * @return The new menu item.
   */
  protected static JMenuItem createMenuItem(String name, int accelerator,
    int mask, int mnemonic, ActionListener action)
  {
    JMenuItem menuItem = new JMenuItem(name);
    if(accelerator >= 0 && mask >= 0 && !SHORTCUTS.containsKey(name))
    {
      KeyStroke stroke = KeyStroke.getKeyStroke(accelerator, mask);
      menuItem.setAccelerator(stroke);
      SHORTCUTS.put(name, stroke);
    }
    menuItem.setMnemonic(mnemonic);
    if(action != null)
    {
      menuItem.addActionListener(action);
    }
    return menuItem;
  }
  
  /**
   * Creates a {@code JRadioButtonMenuItem} to be added to the
   * {@code JTalkerMenuBar}. This will create a menu item using the given name, 
   * whether it is enabled, the keystroke, mnemonic, and action associated with
   * it. When it creates the menu item, it will add the keystroke to a
   * {@link #SHORTCUTS map} which can be used for later. If you wish to not add
   * a keystroke, just make sure that the accelerator and/or mask variable is
   * set to a number smaller than zero. <b>However</b>, the mnemonic is
   * <b>not</b> optional.
   * @param name The title of the menu item.
   * @param enabled Whether the radio button is on.
   * @param accelerator The accelerator of the menu item.
   * @param mask The mask of the menu item.
   * @param mnemonic The mnemonic of the menu item.
   * @param action The action to do when the menu item is pressed.
   * @return The new menu item.
   */
  protected static JRadioButtonMenuItem createButtonMenuItem(String name,
    boolean enabled, int accelerator, int mask, int mnemonic,
    ActionListener action)
  {
    JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(name, enabled);
    if(accelerator >= 0 && mask >= 0 && !SHORTCUTS.containsKey(name))
    {
      KeyStroke stroke = KeyStroke.getKeyStroke(accelerator, mask);
      menuItem.setAccelerator(stroke);
      SHORTCUTS.put(name, stroke);
    }
    menuItem.setMnemonic(mnemonic);
    if(action != null)
    {
      menuItem.addActionListener(action);
    }
    return menuItem;
  }
  
  private static ActionListener openWebpage(String uri)
  {
    return e ->
    {
      try
      {
        if(Desktop.isDesktopSupported() &&
          Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
        {
          Desktop.getDesktop().browse(
            new URI(uri));
        }
      }
      catch(IOException io)
      {
        //TODO: show error dialog
        io.printStackTrace();
      }
      catch(URISyntaxException use)
      {
        //TODO: show error dialog
        use.printStackTrace();
      }
    };
  }
  
  private static String getSelection() throws UnsupportedFlavorException,
    IOException
  {
    Clipboard systemClipboard =
      (Toolkit.getDefaultToolkit()).getSystemClipboard();
    if(systemClipboard.isDataFlavorAvailable(DataFlavor.stringFlavor))
    {
      return (String)systemClipboard.getData(DataFlavor.stringFlavor);
    }
    return null;
  }
  
  /**
   * Initializes each menu within the menu bar (file, edit, view, settings, and
   * help). 
   */
  @Override
  public void init()
  {
    this.initFile();
    this.initEdit();
    this.initView();
    this.initSettings();
    this.initHelp();
  }
  
  /**
   * Saves the chat to a file specified by the user. This will open up a file
   * chooser, in which the user will select and confirm the saving of the file.
   * This will save the escape codes found in the chat, even if the color mode
   * for the chat is disabled.
   */
  public void saveChat()
  {
    JFileChooser c = new JFileChooser();
    //ImageIO.getWriterFormatNames()));
    int option = c.showSaveDialog(this.frame);
    if(option == JFileChooser.APPROVE_OPTION)
    {
      try
      {
        FileWriter writer = new FileWriter(c.getSelectedFile());
        writer.write(this.coloredTextPane.getHistory());
        writer.close();
      }
      catch(IOException io)
      {
        //TODO: show error dialog
        io.printStackTrace();
      }
    }
  }
  
  /**
   * Saves the canvas as a PNG. This will open a file chooser in which the user
   * can navigate to and save a png file that is described by the canvas object.
   * It does so by creating a {@code BufferedImage}, painting the graphics to
   * that image, and then writing that image using {@code ImageIO}.
   * @see javax.imageio.ImageIO
   */
  public void saveCanvas()  
  {
    JFileChooser c = new JFileChooser();
    //could do other formats other than png; but this was too much work.
    c.setFileFilter(new FileNameExtensionFilter("PNG files", "png"));
    //ImageIO.getWriterFormatNames()));
    int option = c.showSaveDialog(frame);
    if(option == JFileChooser.APPROVE_OPTION)
    {
      try
      {
        BufferedImage bi = new BufferedImage(this.canvasPanel.getWidth(),
          this.canvasPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.canvasPanel.paint(bi.getGraphics());
        
        ImageIO.write(bi, "png", c.getSelectedFile());
      }
      catch(IOException io)
      {
        //TODO: show error dialog
        io.printStackTrace();
      }
    }
  }
  
  /**
   * Sends the chat to the printer service. This will print the colored text
   * pane on a physical peice of paper (or to a file), exactly how the colored
   * text pane determines it.
   * @see com.coswald.jtalker.gui.ColoredTextPane#print(Graphics, PageFormat, int)
   */
  public void printChat()
  {
    PrinterJob job = PrinterJob.getPrinterJob();
    PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
    PageFormat pf = job.pageDialog(aset);
    job.setPrintable(this.coloredTextPane, pf);
    if(job.printDialog(aset))
    {
      try
      {
        job.print(aset);
      }
      catch(PrinterException ex)
      {
        //TODO: show error dialog
        ex.printStackTrace();
      }
    }
  }
  
  /**
   * A Java wrapper method for the system's "Copy" action.
   */
  public void systemCopy()
  {
    try
    {
      Clipboard systemClipboard =
        (Toolkit.getDefaultToolkit()).getSystemClipboard();
      systemClipboard.setContents(
        new StringSelection(JTalkerMenuBar.getSelection()), null);
    }
    catch(UnsupportedFlavorException ufe)
    {
      //TODO: show error dialog
      ufe.printStackTrace();
    }
    catch(IOException io)
    {
      //TODO: show error dialog
      io.printStackTrace();
    }
  }
  
  /**
   * A Java wrapper method for the system's "Cut" action. This will <b>not</b>
   * cut anything <b>except</b> for the text entry panel's
   * {@link com.coswald.jtalker.gui.TextEntryPanel#entryField JTextField}.
   */
  public void systemCut()
  {
    this.systemCopy();
    JTextField jtf = this.textEntryPanel.getTextField();
    if(textEntryPanel.isFocusOwner())
    {
      jtf.setText(jtf.getText().replace(jtf.getSelectedText(), ""));
    }
  }
  
  /**
   * A Java wrapper method for the system's "Paste" action. This will
   * automatically (and <b>only</b>) paste to the text entry panel's
   * {@link com.coswald.jtalker.gui.TextEntryPanel#entryField JTextField}. This
   * will not disable the normal paste; this will only work when this method
   * is called (via a menu item for example).
   */
  public void systemPaste()
  {
    Clipboard systemClipboard =
      (Toolkit.getDefaultToolkit()).getSystemClipboard();
    Transferable transfer = systemClipboard.getContents(this.textEntryPanel);
    if(transfer == null)
    {
      return;
    }
    try
    {
      (this.textEntryPanel.getTextField()).setText(
        (String)transfer.getTransferData(DataFlavor.stringFlavor));
    }
    catch(UnsupportedFlavorException ufe)
    {
      //TODO: show error dialog
      ufe.printStackTrace();
    }
    catch(IOException io)
    {
      //TODO: show error dialog
      io.printStackTrace();
    }
  }
  
  /**
   * Select all of the colored text pane's text. This is a wrapper method for
   * {@link javax.swing.JEditorPane#selectAll() this} method; however, it will
   * also ensure that the selection can be seen after by requesting focus to the
   * window manager.
   */
  public void selectColoredTextPane()
  {
    this.coloredTextPane.selectAll();
    //Now we need to show the selected text, so requestFocus.
    this.coloredTextPane.requestFocus();
  }
  
  /**
   * Opens a {@code JOptionPane} that displays the current keyboard shortcuts.
   * @see #SHORTCUTS
   */
  public void openKeyboardShortcuts()
  {
    final String title = "Keyboard Shortcuts";
    String[][] records = new String[SHORTCUTS.size()][2];
    int i = 0;
    for(String name : SHORTCUTS.keySet())
    {
      records[i] = new String[2];
      records[i][0] = name;
      String value = KeyEvent.getKeyText(SHORTCUTS.get(name).getKeyCode());
      int modifiers = SHORTCUTS.get(name).getModifiers();
      if((modifiers & ActionEvent.META_MASK) == ActionEvent.META_MASK)
      {
        value = "SUPER + " + value;
      }
      if((modifiers & ActionEvent.SHIFT_MASK) == ActionEvent.SHIFT_MASK)
      {
        value = "SHIFT + " + value;
      }
      if((modifiers & ActionEvent.ALT_MASK) == ActionEvent.ALT_MASK)
      {
        value = "ALT + " + value;
      }
      if((modifiers & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK)
      {
        value = "CTRL + " + value;
      }
      records[i++][1] = value;
    }
    JTable table = new JTable(records,
      new String[] {"Action", title});
    table.setEnabled(false);
    table.getTableHeader().setReorderingAllowed(false);
    JOptionPane.showMessageDialog(this.frame, new JScrollPane(table),
      title, JOptionPane.PLAIN_MESSAGE);
  }
  
  private void initFile()
  {
    JMenu file = new JMenu("File");
    file.setMnemonic(KeyEvent.VK_F);
    JMenuItem openConnection = JTalkerMenuBar.createMenuItem("Open Connection",
      KeyEvent.VK_O, ActionEvent.CTRL_MASK, KeyEvent.VK_O, null);
    file.add(openConnection);
    JMenuItem closeConnection = JTalkerMenuBar.createMenuItem(
      "Close Connection", KeyEvent.VK_W, ActionEvent.CTRL_MASK, KeyEvent.VK_C,
      null);
    file.add(closeConnection);
    file.addSeparator();
    JMenuItem saveChat = JTalkerMenuBar.createMenuItem("Save Chat As...",
      KeyEvent.VK_S, ActionEvent.CTRL_MASK, KeyEvent.VK_S,
      e -> this.saveChat());
    file.add(saveChat);
    JMenuItem saveCanvas = JTalkerMenuBar.createMenuItem("Save Canvas As...",
      KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK,
      KeyEvent.VK_V, e -> this.saveCanvas());
    file.add(saveCanvas);
    file.addSeparator();
    JMenuItem printChat = JTalkerMenuBar.createMenuItem("Print...",
      KeyEvent.VK_P, ActionEvent.CTRL_MASK, KeyEvent.VK_P,
      e -> this.printChat());
    file.add(printChat);
    file.addSeparator();
    JMenuItem quit = JTalkerMenuBar.createMenuItem("Quit", KeyEvent.VK_F4,
      ActionEvent.ALT_MASK, KeyEvent.VK_Q, e -> frame.dispose());
    file.add(quit);
    this.add(file);
  }
  
  private void initEdit()
  {
    JMenu edit = new JMenu("Edit");
    edit.setMnemonic(KeyEvent.VK_E);
    //setup copy (so we can doClick)
    JMenuItem copy = JTalkerMenuBar.createMenuItem("Copy", KeyEvent.VK_C,
      ActionEvent.CTRL_MASK, KeyEvent.VK_O, e -> this.systemCopy());
    JMenuItem cut = JTalkerMenuBar.createMenuItem("Cut", KeyEvent.VK_X,
      ActionEvent.CTRL_MASK, KeyEvent.VK_C, e -> this.systemCut());
    //Add cut first, then copy (which we already JTalkerMenuBar.created)
    edit.add(cut);
    edit.add(copy);
    JMenuItem paste = JTalkerMenuBar.createMenuItem("Paste", KeyEvent.VK_V,
      ActionEvent.CTRL_MASK, KeyEvent.VK_P, e -> this.systemPaste());
    edit.add(paste);
    JMenuItem selectAll = JTalkerMenuBar.createMenuItem("Select All",
      KeyEvent.VK_A, ActionEvent.CTRL_MASK, KeyEvent.VK_A,
      e -> this.selectColoredTextPane());
    edit.add(selectAll);
    edit.addSeparator();
    JMenuItem clear = JTalkerMenuBar.createMenuItem("Clear Text",
      KeyEvent.VK_X, ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK,
      KeyEvent.VK_T, e -> this.coloredTextPane.setText(""));
    edit.add(clear);
    this.add(edit);
  }
  
  private void initView()
  {
    JMenu view = new JMenu("View");
    view.setMnemonic(KeyEvent.VK_V);
    JRadioButtonMenuItem coloredTextPaneText =
      JTalkerMenuBar.createButtonMenuItem("Show Colored Text", true,
      KeyEvent.VK_F6, 0, KeyEvent.VK_C,
      e -> coloredTextPane.setColorMode(!coloredTextPane.getColorMode()));
    view.add(coloredTextPaneText);
    JRadioButtonMenuItem fullscreen = JTalkerMenuBar.createButtonMenuItem(
      "Fullscreen", false, KeyEvent.VK_F11, 0, KeyEvent.VK_F, e ->
    {
      if(e.getSource() instanceof JRadioButtonMenuItem)
      {
        //"fullscreen might not have been initialize" yeah I get it you hack
        //I wanted to do fullscreen.isSelected(), but NO
        frame.setExtendedState(
          ((JRadioButtonMenuItem)e.getSource()).isSelected() ?
            JFrame.MAXIMIZED_BOTH : JFrame.NORMAL);
        //TODO: look at this bug.
        //frame.setUndecorated(fullscreen.isSelected());
      }
    });
    view.add(fullscreen);
    this.add(view);
  }
  
  private void initSettings()
  {
    JMenu settings = new JMenu("Settings");
    settings.setMnemonic(KeyEvent.VK_S);
    JMenuItem preferredColor = JTalkerMenuBar.createMenuItem("Preferred Colors",
      KeyEvent.VK_P, -1, -1, null);
    settings.add(preferredColor);
    this.add(settings);
  }
  
  private void initHelp()
  {
    final String title = "Help";
    JMenu help = new JMenu(title);
    help.setMnemonic(KeyEvent.VK_H);
    JMenuItem helpMenu = JTalkerMenuBar.createMenuItem(title, KeyEvent.VK_F1,
      0, KeyEvent.VK_E, JTalkerMenuBar.openWebpage(GUIConstants.HELP_URL));
    help.add(helpMenu);
    JMenuItem keyBoardShortcuts = JTalkerMenuBar.createMenuItem(
      "Keyboard Shortcuts", KeyEvent.VK_F1,
      ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK, KeyEvent.VK_K,
      e -> this.openKeyboardShortcuts());
    help.add(keyBoardShortcuts);
    JMenuItem debug = JTalkerMenuBar.createMenuItem("Debug Messages",
      KeyEvent.VK_F7, 0, KeyEvent.VK_D, null);
    help.add(debug);
    help.addSeparator();
    JMenuItem website = JTalkerMenuBar.createMenuItem("Website", -1, -1,
      KeyEvent.VK_W, JTalkerMenuBar.openWebpage(GUIConstants.WEBSITE_URL));
    help.add(website);
    JMenuItem reportBug = JTalkerMenuBar.createMenuItem("Report Bug...", -1, -1,
      KeyEvent.VK_R, JTalkerMenuBar.openWebpage(GUIConstants.REPORT_URL));
    help.add(reportBug);
    help.addSeparator();
    JMenuItem about = JTalkerMenuBar.createMenuItem("About", KeyEvent.VK_F1,
      ActionEvent.SHIFT_MASK, KeyEvent.VK_A,
      JTalkerMenuBar.openWebpage(GUIConstants.ABOUT_URL));
    help.add(about);
    this.add(help);
  }
}
