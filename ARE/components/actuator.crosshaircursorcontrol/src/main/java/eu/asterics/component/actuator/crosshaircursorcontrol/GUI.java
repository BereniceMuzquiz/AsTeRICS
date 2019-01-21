
/*
 *    AsTeRICS - Assistive Technology Rapid Integration and Construction Set
 * 
 * 
 *        d8888      88888888888       8888888b.  8888888 .d8888b.   .d8888b. 
 *       d88888          888           888   Y88b   888  d88P  Y88b d88P  Y88b
 *      d88P888          888           888    888   888  888    888 Y88b.     
 *     d88P 888 .d8888b  888   .d88b.  888   d88P   888  888         "Y888b.  
 *    d88P  888 88K      888  d8P  Y8b 8888888P"    888  888            "Y88b.
 *   d88P   888 "Y8888b. 888  88888888 888 T88b     888  888    888       "888
 *  d8888888888      X88 888  Y8b.     888  T88b    888  Y88b  d88P Y88b  d88P
 * d88P     888  88888P' 888   "Y8888  888   T88b 8888888 "Y8888P"   "Y8888P" 
 *
 *
 *                    homepage: http://www.asterics.org 
 *
 *       This project has been partly funded by the European Commission, 
 *                      Grant Agreement Number 247730
 *  
 *  
 *         Dual License: MIT or GPL v3.0 with "CLASSPATH" exception
 *         (please refer to the folder LICENSE)
 * 
 */

package eu.asterics.component.actuator.crosshaircursorcontrol;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import eu.asterics.mw.services.AstericsErrorHandling;
import eu.asterics.mw.services.ResourceRegistry;


/**
 * Implements the Graphical User Interface for the CrosshairCursorControl plugin
 * 
 * @author Chris, Date: 2019-01-20
 */
public class GUI extends JFrame {

    int width = 0, height = 0;
    int lineWidth = 0;
    boolean axis = true;
    Robot MouseRobot;
    BufferedImage image = null;
    int actTooltip = 0;
    float tooltipX = 0;
    volatile long tooltipTime = Long.MAX_VALUE;
    String tooltipFolder = "";
    String actImageFileName = "";
    JPanel xAxisPanel, yAxisPanel;

    double locX = 0;
    double locY = 0;
    // private JLabel myLabel;
    // add more GUI elements here

    /**
     * The class constructor, initialises the GUI
     * 
     * @param owner
     *            the owner class instance
     * @param dim
     *            the dimension of the screen
     * @param lineWidth
     *            the width of horizontal and vertial crosshair lines
     * 
     */
    public GUI(final CrosshairCursorControlInstance owner, final Dimension dim, final int lineWidth) {
        super("CursorMovementPanel");
        this.width = width;
        setUndecorated(true);
        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 0)); // transparent !
        setSize(dim);
        width = dim.width;
        height = dim.height;
        this.lineWidth = lineWidth;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setOpacity(0.5f);
        setVisible(true);

        try {
            MouseRobot = new Robot();
            MouseRobot.setAutoDelay(0);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        setLocation(0, 0);
        Point location = MouseInfo.getPointerInfo().getLocation();
        locX = location.x;
        locY = location.y;
    }

    void loadImage(String fn) {
        String tmpFileName = tooltipFolder + fn + ".png";
        
        try {
            URI myURI = ResourceRegistry.getInstance().getResource(tmpFileName, ResourceRegistry.RES_TYPE.DATA);
            File imageFile = new File(myURI);
            
            image = ImageIO.read(imageFile);
            actImageFileName = fn;
        } catch (Exception ex) {
            image = null;
            actTooltip = 0;
            actImageFileName = "";
            AstericsErrorHandling.instance.getLogger().warning(" *****  Can not open picture: " + ex.getMessage());
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        });
    }

    String getTooltipFilename() {
        return (actImageFileName);
    }

    boolean tooltipsActive() {
        if (actTooltip > 0) {
            return (true);
        }
        return (false);
    }

    void activateTooltips(String tooltipFolder) {
        this.tooltipFolder = tooltipFolder + "/";
        actTooltip = 1;
        tooltipX = 0;
        loadImage("1");
        tooltipTime = System.currentTimeMillis();
    }

    void deactivateTooltips() {
        actTooltip = 0;
        loadImage("");
    }

    void navigateTooltips(float dx) {

        if (System.currentTimeMillis() - tooltipTime > 100) {
            if (dx > 0)
                actTooltip++;
            else if (actTooltip > 1)
                actTooltip--;
            loadImage(Integer.toString(actTooltip));
        }
        tooltipTime = System.currentTimeMillis();
    }

    void setOnTop() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setAlwaysOnTop(false);
                repaint();
                setAlwaysOnTop(true);
                repaint();
            }
        });
    }

    void showCrosshair() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }

    void hideCrosshair() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(false);
            }
        });
    }

    void resetAxis() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                axis = true;
                repaint();
            }
        });
    }

    void changeAxis() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                axis = !axis;
                repaint();
            }
        });
    }

    synchronized void setCursor(float x, float y) {
        locX = x;
        locY = y;
        MouseRobot.mouseMove((int) locX, (int) locY);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        Color xAxisColor, yAxisColor;

        if (image != null) {
            xAxisColor = Color.GRAY;
            yAxisColor = Color.GRAY;
        } else {
            if (axis) {
                xAxisColor = Color.GRAY;
                yAxisColor = Color.RED;
            } else {
                xAxisColor = Color.RED;
                yAxisColor = Color.GRAY;
            }
        }

        g2.setColor(yAxisColor);
        g2.fillRect((int) locX - lineWidth / 2, 0, lineWidth, (int) locY - lineWidth / 2);
        g2.fillRect((int) locX - lineWidth / 2, (int) locY + lineWidth / 2, lineWidth, height - (int) locY);

        g2.setColor(xAxisColor);
        g2.fillRect(0, (int) locY - lineWidth / 2, (int) locX - lineWidth / 2, lineWidth);
        g2.fillRect((int) locX + lineWidth / 2, (int) locY - lineWidth / 2, width - (int) locX, lineWidth);

        if (image != null) {
            int toolX, toolY;
            if ((int) locY > 100)
                toolY = (int) locY - 90;
            else
                toolY = (int) locY + 10;
            if ((int) locX < width - image.getWidth())
                toolX = (int) locX + 10;
            else
                toolX = (int) locX - image.getWidth() - 10;

            g.drawImage(image, toolX, toolY, null);
        }

    }

}