/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.tools;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jhotdraw.app.JHotDrawFeatures;
import org.jhotdraw.draw.DrawingEditor;

/**
 *
 * @author Tommy
 */
public class ToolPanel extends JPanel {
    
    @FeatureEntryPoint(JHotDrawFeatures.ToolPanel)
    public ToolPanel () {
        super(new GridBagLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(5, 5, 5, 8));
    }
    
    public JPanel fontAwesome (List<String> list, Font fontIcons, DrawingEditor editor) {
        // Add the font slider tool.
        add(new ItemPanel().addFontSlider(editor), new DesignConstraint().setFontAwesomeToolBar());
        // Add all fontawesome buttons.
        for (int i = 0; i < list.size(); i++) {
            JButton button = new Buttons(list.get(i), fontIcons.deriveFont(16f), editor).fontAwesome();
            add(button, new DesignConstraint().setIcon(i));
        }
        return this;
    }
}
