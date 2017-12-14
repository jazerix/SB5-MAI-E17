/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.tools;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import javax.swing.SwingConstants;
import org.jhotdraw.app.JHotDrawFeatures;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.action.SelectionComponentRepainter;
import org.jhotdraw.gui.JAttributeSlider;
import org.jhotdraw.gui.JPopupButton;
import org.jhotdraw.gui.plaf.palette.PaletteButtonUI;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 *
 * @author Tommy
 */
public class ButtonsPopUp extends JPopupButton {
   
    @FeatureEntryPoint(JHotDrawFeatures.ButtonsPopUp)
    public ButtonsPopUp () {
        setUI((PaletteButtonUI) PaletteButtonUI.createUI(this));
        setPopupAnchor(SwingConstants.SOUTH_EAST);      
    }
    
    public JPopupButton fontSize(ResourceBundleUtil label, DrawingEditor editor) {
        JAttributeSlider slider = new AttributedSlider().fontSlider(editor);   
        add(slider);   
        label.configureToolBarButton(this, "attribute.fontSize");
        new SelectionComponentRepainter(editor, this);       
        return this;
    }
}
