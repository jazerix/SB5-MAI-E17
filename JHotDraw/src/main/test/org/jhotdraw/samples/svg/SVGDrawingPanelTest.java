package org.jhotdraw.samples.svg;

import org.jhotdraw.samples.svg.gui.FontAwesomeToolBar;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by jazer on 16-Dec-17.
 */
class SVGDrawingPanelTest {

    @Test
    public void components_contains_font_awesome()
    {
        SVGDrawingPanel svgDrawingPanel = new SVGDrawingPanel();
        JPanel pane = svgDrawingPanel.getToolsPane();
        Component[] components = pane.getComponents();
        boolean containsFontAwesome = false;
        for (Component component : components) {
            if (component instanceof FontAwesomeToolBar)
                containsFontAwesome = true;

        }
        assertTrue(containsFontAwesome);
    }

}