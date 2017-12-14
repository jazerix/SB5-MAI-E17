/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.figures;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import static org.jhotdraw.draw.AttributeKeys.FILL_COLOR;
import static org.jhotdraw.draw.AttributeKeys.FONT_SIZE;
import static org.jhotdraw.draw.AttributeKeys.TEXT;
import static org.jhotdraw.draw.AttributeKeys.TRANSFORM;
import org.jhotdraw.draw.Handle;
import org.jhotdraw.draw.TextHolderFigure;
import org.jhotdraw.geom.Geom;
import org.jhotdraw.geom.Insets2D;
import org.jhotdraw.samples.svg.SVGAttributeKeys;
import org.jhotdraw.samples.svg.tools.HandleCreator;

// #CHANGED (FILE IS CREATED)

// This figure is almost identical with TextHolderFigure with less functionality.
// All direct text change have been removed and the default 'text' have been changed to a unicode character
// Since the original 'FONT'-toolbar have been changed, the ability of changing font is not possible.

public class SVGFontAwesome extends SVGAttributedFigure implements TextHolderFigure, SVGFigure {
    
    protected Point2D.Double[] coordinates = new Point2D.Double[] { new Point2D.Double() };
    protected double[] rotates = new double[] { 0 };
    private transient Rectangle2D.Double cachedBounds;
    private transient Rectangle2D.Double cachedDrawingArea;
    private transient Shape cachedShape;
    
    private Font fontIcons;
    private List<String> listIcons;
    
    public SVGFontAwesome() {
        TEXT.set(this, "\uF014");
        SVGAttributeKeys.setDefaults(this);
    }
    
    @Override
    public Collection<Handle> createHandles(int state) {
        LinkedList<Handle> handles = new LinkedList<>();
        if (state == 0) handles = new HandleCreator().textHandler(handles, this);
        return handles;
    }
    
    public void setIcon(String text) {
        TEXT.set(this, text);
    }

    protected void drawFill(Graphics2D g) {
        g.fill(getIconShape());
    }

    protected void drawStroke(Graphics2D g) {
        g.draw(getIconShape());
    }

    @Override
    public Rectangle2D.Double getBounds() {
        if (cachedBounds == null) {
            cachedBounds = new Rectangle2D.Double();
            cachedBounds.setRect(getIconShape().getBounds2D());
        }
        return (Rectangle2D.Double) cachedBounds.clone();
    }
    
    public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
        coordinates = new Point2D.Double[] {
            new Point2D.Double(anchor.x, anchor.y)
        };
        rotates = new double[] { 0d };
    }
    
    private Shape getIconShape() {       
        if (cachedShape == null) {
            String text = getText();
            if (text == null || text.length() == 0) { text = "\uF014"; }
            
            FontRenderContext frc = getFontRenderContext();
            HashMap<TextAttribute,Object> textAttributes = new HashMap<TextAttribute,Object>();
            textAttributes.put(TextAttribute.FONT, getFont());
            TextLayout textLayout = new TextLayout(text, textAttributes, frc);
            
            AffineTransform tx = new AffineTransform();
            tx.translate(coordinates[0].x, coordinates[0].y);
            tx.rotate(rotates[0]);
            
            cachedShape = tx.createTransformedShape(textLayout.getOutline(tx));
            cachedShape = textLayout.getOutline(tx);
        }
        return cachedShape;
    }

    @Override
    public boolean contains(Point2D.Double p) {
        if (TRANSFORM.get(this) != null) {
            try {
                p = (Point2D.Double) TRANSFORM.get(this).inverseTransform(p, new Point2D.Double());
            } catch (NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }
        }
        return getIconShape().getBounds2D().contains(p);
    }

    @Override
    public Object getTransformRestoreData() {
        Point2D.Double[] restoredCoordinates = (Point2D.Double[]) this.coordinates.clone();
        for (int i = 0; i < this.coordinates.length; i++) {
            restoredCoordinates[i] = (Point2D.Double) this.coordinates[i].clone();
        }
        return new Object[] {
            TRANSFORM.getClone(this),
            restoredCoordinates,
        };
    }

    @Override
    public void restoreTransformTo(Object geometry) {
        Object[] restoreData = (Object[]) geometry;
        TRANSFORM.basicSetClone(this, (AffineTransform) restoreData[0]);
        Point2D.Double[] restoredCoordinates = (Point2D.Double[]) restoreData[1];
        for (int i = 0; i < this.coordinates.length; i++) {
            coordinates[i] = (Point2D.Double) restoredCoordinates[i].clone();
        }
        invalidate();
    }

    @Override
    public void transform(AffineTransform tx) {
        if (TRANSFORM.get(this) != null || tx.getType() != (tx.getType() & AffineTransform.TYPE_TRANSLATION)) {
            if (TRANSFORM.get(this) == null) {
                TRANSFORM.basicSet(this, (AffineTransform) tx.clone());
            } else {
                AffineTransform t = TRANSFORM.getClone(this);
                t.preConcatenate(tx);
                TRANSFORM.basicSet(this, t);
            }
        } else {
            for (int i = 0; i < coordinates.length; i++) tx.transform(coordinates[i], coordinates[i]);
        }
        invalidate();
    }
    
    @Override public Rectangle2D.Double getDrawingArea() {
        if (cachedDrawingArea == null) {
            Rectangle2D rx = getBounds();
            Rectangle2D.Double r = (rx instanceof Rectangle2D.Double) ?
                (Rectangle2D.Double) rx :
                new Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight());
            double g = SVGAttributeKeys.getPerpendicularHitGrowth(this);
            Geom.grow(r, g, g);
            if (TRANSFORM.get(this) == null) {
                cachedDrawingArea = r;
            } else {
                cachedDrawingArea = new Rectangle2D.Double();
                cachedDrawingArea.setRect(TRANSFORM.get(this).createTransformedShape(r).getBounds2D());
            }
        }
        return (Rectangle2D.Double) cachedDrawingArea.clone();
    }
  
    @Override
    public Font getFont() {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, int.class.getResourceAsStream("org/jhotdraw/samples/svg/fonts/fontawesome-webfont.ttf")).deriveFont(getFontSize());
        } catch (FontFormatException | IOException ex) { }
        return null;
    }    

    @Override
    public void setFontSize(float size) {
        Point2D.Double p = new Point2D.Double(0, size);
        AffineTransform tx =  TRANSFORM.get(this);
        if (tx != null) {
            try {
                tx.inverseTransform(p, p);
                Point2D.Double p0 = new Point2D.Double(0, 0);
                tx.inverseTransform(p0, p0);
                p.y -= p0.y;
            } catch (NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }
        }
        FONT_SIZE.set(this, Math.abs(p.y));
    }

    @Override
    public float getFontSize() {
        Point2D.Double p = new Point2D.Double(0, FONT_SIZE.get(this));
        AffineTransform tx =  TRANSFORM.get(this);
        if (tx != null) {
            tx.transform(p, p);
            Point2D.Double p0 = new Point2D.Double(0, 0);
            tx.transform(p0, p0);
            p.y -= p0.y;
        }
        return (float) Math.abs(p.y);
    }
    
    @Override public void invalidate() {
        super.invalidate();
        cachedDrawingArea = null;
        cachedShape = null;
        cachedBounds = null;
    }  

    @Override
    public boolean isTextOverflow() {
        throw new UnsupportedOperationException("Not supported yet isTextOverflow."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet isEmpty."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override public Insets2D.Double getInsets() { return new Insets2D.Double(); }
    @Override public void setText(String text) { TEXT.set(this, text); }
    @Override public double getBaseline() { return coordinates[0].y - getBounds().y;}
    @Override public String getText() { return (String) getAttribute(TEXT); }
    @Override public int getTextColumns() { return 4; }
    @Override public Color getTextColor() { return FILL_COLOR.get(this); }
    @Override public Color getFillColor() { return FILL_COLOR.get(this) == null || FILL_COLOR.get(this).equals(Color.white) ? Color.black : Color.WHITE; }
    @Override public int getTabSize() { return 8; }
    @Override public boolean isEditable() { return false; }
    @Override public TextHolderFigure getLabelFor() { return this; } 
}
