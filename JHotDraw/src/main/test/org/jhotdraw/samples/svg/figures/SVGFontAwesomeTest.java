package org.jhotdraw.samples.svg.figures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by jazer on 16-Dec-17.
 */
class SVGFontAwesomeTest {

    SVGFontAwesome fontAwesomeFigure;

    @BeforeEach
    void setUp() {
        fontAwesomeFigure = new SVGFontAwesome();
    }

    @Test
    void setIcon() {
        fontAwesomeFigure.setIcon("\uf165");
        assertEquals("\uf165", fontAwesomeFigure.getText());
    }

    @Test
    void setFontSize() {
        fontAwesomeFigure.setFontSize(32);
        assertEquals(32, fontAwesomeFigure.getFontSize());
    }

}