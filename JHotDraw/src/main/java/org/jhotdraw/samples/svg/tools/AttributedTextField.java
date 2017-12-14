/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.tools;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import javax.swing.JTextField;
import org.jhotdraw.app.JHotDrawFeatures;
import static org.jhotdraw.draw.AttributeKeys.FONT_SIZE;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.gui.FigureAttributeEditorHandler;
import org.jhotdraw.gui.JAttributeTextField;
import org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI;
import org.jhotdraw.text.JavaNumberFormatter;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 *
 * @author Tommy
 */
public class AttributedTextField {
    
    @FeatureEntryPoint(JHotDrawFeatures.AttributedTextField)
    public AttributedTextField() {
        
    }
    
    public JAttributeTextField<Double> textField(ResourceBundleUtil label, DrawingEditor editor) {
        JAttributeTextField<Double> field = new JAttributeTextField<>();      
        field.setColumns(2);
        field.setToolTipText(label.getString("attribute.fontSize.toolTipText"));
        field.setHorizontalAlignment(JAttributeTextField.RIGHT);
        field.putClientProperty("Palette.Component.segmentPosition", "first");
        field.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(field));
        field.setFormatterFactory(JavaNumberFormatter.createFormatterFactory(0d, 1000d, 1d));
        field.setHorizontalAlignment(JTextField.LEADING);        
        new FigureAttributeEditorHandler<Double>(FONT_SIZE, field, editor);       
        return field;
    }
    
}
