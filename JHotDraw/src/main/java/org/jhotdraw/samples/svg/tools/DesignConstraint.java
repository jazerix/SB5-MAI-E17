/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.tools;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import org.jhotdraw.app.JHotDrawFeatures;

/**
 *
 * @author Tommy
 */
public class DesignConstraint extends GridBagConstraints {
    
    @FeatureEntryPoint(JHotDrawFeatures.FONT_AWESOME_DESIGN)
    public DesignConstraint () {
        
    }
    
    public GridBagConstraints setFontAwesomeToolBar() {
        gridx = 0;
        gridy = 0;
        gridwidth = 2;
        fill = GridBagConstraints.BOTH;
        anchor = GridBagConstraints.FIRST_LINE_START;
        insets = new Insets(0, 0, 0, 0);
        return this;
    }
    
    public GridBagConstraints setText() {
        gridx = 0;
        gridy = 0;
        insets = new Insets(0, 0, 0, 0);
        anchor = GridBagConstraints.FIRST_LINE_START;
        gridwidth = 2;
        weightx = 1f;
        fill = GridBagConstraints.HORIZONTAL;
        return this;
    }
    
    public GridBagConstraints setIcon(int index) {
        gridx = (index + 8) / 4;
        gridy = (index + 8) % 4;
        insets = new Insets(2, 2, 2, 2);
        return this;
    }
    
    public GridBagConstraints setFontSlider(){
        gridx = 2;
        gridy = 0;
        anchor = GridBagConstraints.FIRST_LINE_START;
        insets = new Insets(0, 0, 0, 0);
        return this;
    }
}
