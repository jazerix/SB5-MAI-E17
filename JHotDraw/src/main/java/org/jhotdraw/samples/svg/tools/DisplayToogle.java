/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.tools;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import java.util.Collection;
import org.jhotdraw.app.JHotDrawFeatures;
import org.jhotdraw.draw.CompositeFigure;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.samples.svg.figures.SVGFontAwesome;
import org.jhotdraw.samples.svg.gui.AbstractToolBar;
import org.jhotdraw.samples.svg.gui.SelectionComponentDisplayer;

/**
 *
 * @author Tommy
 */
public class DisplayToogle {
    
    @FeatureEntryPoint(JHotDrawFeatures.DisplayToogle)
    public DisplayToogle () {
        
    }
    
    public void FontAwesome(DrawingEditor editor, AbstractToolBar toolbar) {
        new SelectionComponentDisplayer(editor, toolbar) {

                @Override
                public void updateVisibility() {
                    boolean state       = editor != null && editor.getActiveView() != null;
                    boolean visible     = editor.getTool() instanceof FontAwesomeCreationTool || containsFontAwesomeFigure(editor.getActiveView().getSelectedFigures());                 
                    component.setVisible(state && visible);
                    component.revalidate();
                }

                private boolean containsFontAwesomeFigure(Collection<Figure> figures) {
                    for (Figure f : figures) {
                        if (f instanceof SVGFontAwesome) return true;
                        if (f instanceof CompositeFigure) containsFontAwesomeFigure(((CompositeFigure) f).getChildren());
                    }
                    return false;
                }
            };
    }
    
}
