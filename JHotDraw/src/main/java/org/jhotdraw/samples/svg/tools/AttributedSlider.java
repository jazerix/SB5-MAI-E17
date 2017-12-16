/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.tools;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import javax.swing.JSlider;
import javax.swing.plaf.SliderUI;
import org.jhotdraw.app.JHotDrawFeatures;
import static org.jhotdraw.draw.AttributeKeys.FONT_SIZE;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.gui.FigureAttributeEditorHandler;
import org.jhotdraw.gui.JAttributeSlider;
import org.jhotdraw.gui.plaf.palette.PaletteSliderUI;

public class AttributedSlider {
    
    @FeatureEntryPoint(JHotDrawFeatures.AttributedSlider)
    public AttributedSlider() {
        
    }
    
    public JAttributeSlider fontSlider(DrawingEditor editor) {
        JAttributeSlider slider = new JAttributeSlider(JSlider.VERTICAL, 0, 100, 12);      
        slider.setUI((SliderUI) PaletteSliderUI.createUI(slider));
        slider.setScaleFactor(1d);      
        new FigureAttributeEditorHandler<Double>(FONT_SIZE, slider, editor);     
        return slider;
    }
}
