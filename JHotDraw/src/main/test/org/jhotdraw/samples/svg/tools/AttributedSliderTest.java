package org.jhotdraw.samples.svg.tools;

import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.gui.FigureAttributeEditorHandler;
import org.jhotdraw.gui.JAttributeSlider;
import org.jhotdraw.gui.plaf.palette.PaletteSliderUI;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.plaf.SliderUI;

import static org.jhotdraw.draw.AttributeKeys.FONT_SIZE;
import static org.junit.jupiter.api.Assertions.*;

class AttributedSliderTest {
    @Test
    void fontSlider() {
        DefaultDrawingEditor lol = new DefaultDrawingEditor();
        AttributedSlider as = new AttributedSlider();
        JAttributeSlider actualSlider = as.fontSlider(lol);
        assertEquals(1d, actualSlider.getScaleFactor());
        assertEquals(0, actualSlider.getMinimum());
        assertEquals(100, actualSlider.getMaximum());
        assertEquals(12, actualSlider.getValue());
    }

}