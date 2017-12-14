/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.tools;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 *
 * @author Tommy
 */
public class ItemPanel extends JPanel {
    
    private ResourceBundleUtil labels;    
    
    public ItemPanel () {
        super(new GridBagLayout());
        setOpaque(false);
        labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");             
    }
    
    public JPanel addFontSlider(DrawingEditor editor) {          
        add(new AttributedTextField().textField(labels, editor), new DesignConstraint().setText());
        add(new ButtonsPopUp().fontSize(labels, editor), new DesignConstraint().setFontSlider());
        return this;
    }
}
