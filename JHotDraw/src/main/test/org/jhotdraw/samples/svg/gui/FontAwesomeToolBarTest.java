package org.jhotdraw.samples.svg.gui;

import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.samples.svg.SVGApplicationModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by jazer on 16-Dec-17.
 */
class FontAwesomeToolBarTest {

    FontAwesomeToolBar fontAwesomeToolBar;

    @BeforeEach
    void setUp()
    {
        fontAwesomeToolBar = new FontAwesomeToolBar();
    }

    @Test
    void getID() {
        assertEquals("font", fontAwesomeToolBar.getID());
    }

    @Test
    void getDefaultDisclosureState() {
        assertEquals(0, fontAwesomeToolBar.getDefaultDisclosureState());
    }

}