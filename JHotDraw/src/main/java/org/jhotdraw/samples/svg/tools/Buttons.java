/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.tools;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import java.awt.Font;
import javax.swing.JButton;
import org.jhotdraw.app.JHotDrawFeatures;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.gui.plaf.palette.PaletteButtonUI;
import org.jhotdraw.samples.svg.figures.SVGFontAwesome;

/**
 *
 * @author Tommy
 */
public class Buttons extends JButton {
    
    private DrawingEditor editor;
    private String text;
    
    @FeatureEntryPoint(JHotDrawFeatures.FONT_AWESOME_BUTTON)
    public Buttons (String text, Font font, DrawingEditor editor) {
        super(text);
        this.text = text;
        this.editor = editor;
        setFont(font);
        setUI((PaletteButtonUI) PaletteButtonUI.createUI(this));
    }
    
    @FeatureEntryPoint(JHotDrawFeatures.FONT_AWESOME_BUTTON_FA)
    public JButton fontAwesome() {
        addActionListener((ActionEvent) -> {    
                for (Figure f : editor.getActiveView().getSelectedFigures()) {
                    if (f instanceof SVGFontAwesome) {
                        ((SVGFontAwesome) f).setIcon(text);
                    }
                }
            });
        return this;
    }
}
