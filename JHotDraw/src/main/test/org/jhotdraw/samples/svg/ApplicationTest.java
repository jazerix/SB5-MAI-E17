package org.jhotdraw.samples.svg;

import org.jhotdraw.app.Application;
import org.jhotdraw.app.DefaultSDIApplication;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.samples.svg.figures.SVGFontAwesome;
import org.jhotdraw.samples.svg.tools.FontAwesomeMeta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    Drawing drawingBoard;

    @BeforeEach
    public void setUp()
    {
        Application app  =new DefaultSDIApplication();
        SVGApplicationModel model = new SVGApplicationModel();
        model.setViewClassName("org.jhotdraw.samples.svg.SVGView");
        app.setModel(model);
        app.init();
        app.start();
        SVGView view = (SVGView) model.createView();
        view.init();
        model.initView(app, view);
        DrawingEditor editor = view.getEditor();


        drawingBoard = editor.getActiveView().getDrawing();


    }

    @Test
    public void addFontAwesomeToDrawing()
    {
        SVGFontAwesome trashCan = new SVGFontAwesome();
        drawingBoard.add(trashCan);
        assertTrue(drawingBoard.getChildren().contains(trashCan));
    }

    @Test
    public void changeFontSizeOfAddedElement()
    {
        SVGFontAwesome trashCan = new SVGFontAwesome();
        drawingBoard.add(trashCan);
        SVGFontAwesome figure = (SVGFontAwesome) drawingBoard.getChildren().get(0);
        figure.setFontSize(22);

        assertEquals(22, ((SVGFontAwesome) drawingBoard.getChildren().get(0)).getFontSize());
    }

    @Test
    public void changeIconOfAddedElements()
    {
        SVGFontAwesome trashCan = new SVGFontAwesome();
        drawingBoard.add(trashCan);
        SVGFontAwesome figure = (SVGFontAwesome) drawingBoard.getChildren().get(0);
        figure.setText("\uf27e");

        assertEquals("\uf27e", ((SVGFontAwesome) drawingBoard.getChildren().get(0)).getText());
    }

    @Test
    public void addAlotOfFontAwesomeElements()
    {
        FontAwesomeMeta meta = new FontAwesomeMeta();
        List<String> icons = meta.getIconAsString();


        for (int i = 0; i < 5; i++) {
            SVGFontAwesome figure = new SVGFontAwesome();
            figure.setText(icons.get(i));
            figure.setFontSize((float) Math.pow((i+1), 2));
            drawingBoard.add(figure);
        }

        for (int i = 0; i < 5; i++) {
            SVGFontAwesome imAFontAwesomeFigureNotisMe = (SVGFontAwesome) drawingBoard.getChildren().get(i);
            assertEquals(icons.get(i), imAFontAwesomeFigureNotisMe.getText());
            assertEquals((float) Math.pow((i+1), 2), imAFontAwesomeFigureNotisMe.getFontSize());
        }

    }

}