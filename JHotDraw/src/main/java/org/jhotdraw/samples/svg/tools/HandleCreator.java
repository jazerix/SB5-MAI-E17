/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.tools;

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint;
import java.util.LinkedList;
import org.jhotdraw.app.JHotDrawFeatures;
import org.jhotdraw.draw.BoundsOutlineHandle;
import org.jhotdraw.draw.FontSizeHandle;
import org.jhotdraw.draw.Handle;
import org.jhotdraw.draw.MoveHandle;
import org.jhotdraw.draw.RelativeLocator;
import org.jhotdraw.draw.TextHolderFigure;
import org.jhotdraw.samples.svg.figures.LinkHandle;

/**
 *
 * @author Tommy
 */
public class HandleCreator {
    
    @FeatureEntryPoint(JHotDrawFeatures.HandleCreator)
    public LinkedList<Handle> textHandler (LinkedList<Handle> handles, TextHolderFigure figure) {
        handles.add(new BoundsOutlineHandle(figure));
        handles.add(new MoveHandle(figure, RelativeLocator.northWest()));
        handles.add(new MoveHandle(figure, RelativeLocator.northEast()));
        handles.add(new MoveHandle(figure, RelativeLocator.southWest()));
        handles.add(new MoveHandle(figure, RelativeLocator.southEast()));
        handles.add(new FontSizeHandle(figure));
        handles.add(new LinkHandle(figure));
        
        return handles;
    }
}
