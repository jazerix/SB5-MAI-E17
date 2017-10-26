/*
 * @(#)SVGText.java  2.1.1  2008-05-15
 *
 * Copyright (c) 1996-2008 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */

package org.jhotdraw.samples.svg.figures

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint
import java.awt.*
import java.awt.event.*
import java.awt.font.*
import java.awt.geom.*
import java.io.*
import java.util.*
import javax.swing.*
import org.jhotdraw.app.JHotDrawFeatures
import org.jhotdraw.draw.*
import org.jhotdraw.geom.*
import org.jhotdraw.samples.svg.*
import org.jhotdraw.samples.svg.SVGConstants
import org.jhotdraw.util.*
import org.jhotdraw.xml.*
import org.jhotdraw.samples.svg.SVGAttributeKeys.*
import java.beans.Transient

/**
 * SVGText.
 *
 *
 * XXX - At least on Mac OS X - Always draw text using TextLayout.getOutline(),
 * because outline layout does not match with TextLayout.draw() output.
 * Cache outline to improve performance.
 *
 * @author Werner Randelshofer
 * @version 2.1.1 Rectangle returned by getDrawingArea needs to be cloned.
 * <br></br>2.1 2007-05-13 Fixed transformation issues.
 * <br></br>2.0 2007-04-14 Adapted for new AttributeKeys.TRANSFORM support.
 * <br></br>1.0 July 8, 2006 Created.
 */
class SVGFontAwesomeFigure @FeatureEntryPoint(JHotDrawFeatures.TEXT_TOOL)
constructor(text: String) : SVGAttributedFigure(), TextHolderFigure, SVGFigure {

    protected var coordinates: Array<Point2D.Double?> = arrayOf(Point2D.Double())
        get() {
            return field.clone()
        }
        set(value) {
            field = value
            invalidate()
        }

    protected var rotates = doubleArrayOf(0.0)
        get() {
            return field.clone()
        }
        set(value) {
            field = value
            invalidate()
        }
    private var editable = true

    /**
     * This is used to perform faster drawing and hit testing.
     */
    private var cachedTextShape: Shape? = null
    private var cachedBounds: Rectangle2D.Double? = null
    private var cachedDrawingArea: Rectangle2D.Double? = null

    /** Creates a new instance.  */
    constructor() : this("Text") {}

    init {
        setText(text)
        SVGAttributeKeys.setDefaults(this)
    }

    // DRAWING
    override fun drawText(g: java.awt.Graphics2D) {}

    override fun drawFill(g: Graphics2D) {
        g.fill(textShape)
    }

    @FeatureEntryPoint(JHotDrawFeatures.TEXT_TOOL)
    override fun drawStroke(g: Graphics2D) {
        g.draw(textShape)
    }

    private val textShape: Shape?
        get() {
            if (cachedTextShape == null) {
                var text = text
                if (text == null || text.length == 0) {
                    text = " "
                }

                val frc = fontRenderContext
                val textAttributes = HashMap<TextAttribute, Any>()
                textAttributes.put(TextAttribute.FONT, SVGAttributeKeys.getFont(this))
                if (AttributeKeys.FONT_UNDERLINE.get(this)!!) {
                    textAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)
                }
                val textLayout = TextLayout(text, textAttributes, frc)

                val tx = AffineTransform()
                tx.translate(coordinates[0]!!.x, coordinates[0]!!.y)
                when (TEXT_ANCHOR.get(this)) {
                    SVGAttributeKeys.TextAnchor.END -> tx.translate((-textLayout.advance).toDouble(), 0.0)
                    SVGAttributeKeys.TextAnchor.MIDDLE -> tx.translate(-textLayout.advance / 2.0, 0.0)
                    SVGAttributeKeys.TextAnchor.START -> {
                    }
                }
                tx.rotate(rotates[0])

                cachedTextShape = tx.createTransformedShape(textLayout.getOutline(tx))
                cachedTextShape = textLayout.getOutline(tx)
            }
            return cachedTextShape
        }

    override fun getBounds(): Rectangle2D.Double {
        if (cachedBounds == null) {
            cachedBounds = Rectangle2D.Double()
            cachedBounds!!.setRect(textShape?.bounds2D)
        }
        return cachedBounds!!.clone() as Rectangle2D.Double
    }
    override fun setBounds(anchor: Point2D.Double, lead: Point2D.Double) {
        coordinates = arrayOf(Point2D.Double(anchor.x, anchor.y))
        rotates = doubleArrayOf(0.0)
    }

    override fun getDrawingArea(): Rectangle2D.Double {
        if (cachedDrawingArea == null) {
            val rx = bounds
            val r = rx as? Rectangle2D.Double ?: Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight())
            val g = SVGAttributeKeys.getPerpendicularHitGrowth(this)
            Geom.grow(r, g, g)
            if (AttributeKeys.TRANSFORM.get(this) == null) {
                cachedDrawingArea = r
            } else {
                cachedDrawingArea = Rectangle2D.Double()
                cachedDrawingArea!!.setRect(AttributeKeys.TRANSFORM.get(this)!!.createTransformedShape(r).bounds2D)
            }
        }
        return cachedDrawingArea!!.clone() as Rectangle2D.Double
    }

    /**
     * Checks if a Point2D.Double is inside the figure.
     */
    override fun contains(p: Point2D.Double): Boolean {
        var p = p
        if (AttributeKeys.TRANSFORM.get(this) != null) {
            try {
                p = AttributeKeys.TRANSFORM.get(this)!!.inverseTransform(p, Point2D.Double()) as Point2D.Double
            } catch (ex: NoninvertibleTransformException) {
                ex.printStackTrace()
            }

        }
        return textShape!!.bounds2D.contains(p)
    }

    /**
     * Transforms the figure.
     *
     * @param tx the transformation.
     */
    override fun transform(tx: AffineTransform) {
        if (AttributeKeys.TRANSFORM.get(this) != null || tx.type != tx.type and AffineTransform.TYPE_TRANSLATION) {
            if (AttributeKeys.TRANSFORM.get(this) == null) {
                AttributeKeys.TRANSFORM.basicSet(this, tx.clone() as AffineTransform)
            } else {
                val t = AttributeKeys.TRANSFORM.getClone(this)
                t!!.preConcatenate(tx)
                AttributeKeys.TRANSFORM.basicSet(this, t)
            }
        } else {
            for (i in coordinates.indices) {
                tx.transform(coordinates[i], coordinates[i])
            }
            if (FILL_GRADIENT.get(this) != null && !FILL_GRADIENT.get(this)!!.isRelativeToFigureBounds) {
                val g = FILL_GRADIENT.getClone(this)
                g!!.transform(tx)
                FILL_GRADIENT.basicSet(this, g)
            }
            if (STROKE_GRADIENT.get(this) != null && !STROKE_GRADIENT.get(this)!!.isRelativeToFigureBounds) {
                val g = STROKE_GRADIENT.getClone(this)
                g!!.transform(tx)
                STROKE_GRADIENT.basicSet(this, g)
            }
        }
        invalidate()
    }

    override fun restoreTransformTo(geometry: Any) {
        val restoreData = geometry as Array<Any>
        AttributeKeys.TRANSFORM.basicSetClone(this, restoreData[0] as AffineTransform)
        val restoredCoordinates = restoreData[1] as Array<Point2D.Double>
        for (i in this.coordinates.indices) {
            coordinates[i] = restoredCoordinates[i].clone() as Point2D.Double
        }
        FILL_GRADIENT.basicSetClone(this, restoreData[2] as Gradient)
        STROKE_GRADIENT.basicSetClone(this, restoreData[3] as Gradient)
        invalidate()
    }

    override fun getTransformRestoreData(): Any {
        val restoredCoordinates = this.coordinates.clone()
        for (i in this.coordinates.indices) {
            restoredCoordinates[i] = this.coordinates[i]?.clone() as Point2D.Double
        }
        return arrayOf<Any>(AttributeKeys.TRANSFORM.getClone(this), restoredCoordinates, FILL_GRADIENT.getClone(this), STROKE_GRADIENT.getClone(this))
    }

    // ATTRIBUTES
    /**
     * Gets the text shown by the text figure.
     */
    override fun getText(): String? {
        return getAttribute(AttributeKeys.TEXT)
    }

    override fun <T> setAttribute(key: AttributeKey<T>, newValue: T) {
        if (key == SVGAttributeKeys.TRANSFORM ||
                key == SVGAttributeKeys.FONT_FACE ||
                key == SVGAttributeKeys.FONT_BOLD ||
                key == SVGAttributeKeys.FONT_ITALIC ||
                key == SVGAttributeKeys.FONT_SIZE) {
            invalidate()
        }
        super.setAttribute(key, newValue)
    }

    /**
     * Sets the text shown by the text figure.
     */
    override fun setText(newText: String) {
        AttributeKeys.TEXT.set(this, newText)
    }

    override fun isEditable(): Boolean {
        return editable
    }

    override fun getTextColumns(): Int {
        //return (getText() == null) ? 4 : Math.min(getText().length(), 4);
        return 4
    }

    override fun getFont(): Font? {
        return SVGAttributeKeys.getFont(this)
    }

    override fun getTextColor(): Color? {
        return AttributeKeys.FILL_COLOR.get(this)
        //   return TEXT_COLOR.get(this);
    }

    override fun getFillColor(): Color {
        return if (AttributeKeys.FILL_COLOR.get(this) == null || AttributeKeys.FILL_COLOR.get(this) == Color.white) Color.black else Color.WHITE
        //  return FILL_COLOR.get(this);
    }

    override fun setFontSize(size: Float) {
        // FONT_SIZE.basicSet(this, new Double(size));
        val p = Point2D.Double(0.0, size.toDouble())
        val tx = AttributeKeys.TRANSFORM.get(this)
        if (tx != null) {
            try {
                tx.inverseTransform(p, p)
                val p0 = Point2D.Double(0.0, 0.0)
                tx.inverseTransform(p0, p0)
                p.y -= p0.y
            } catch (ex: NoninvertibleTransformException) {
                ex.printStackTrace()
            }

        }
        AttributeKeys.FONT_SIZE.set(this, Math.abs(p.y))
    }

    override fun getFontSize(): Float {
        //   return FONT_SIZE.get(this).floatValue();
        val p = Point2D.Double(0.0, AttributeKeys.FONT_SIZE.get(this)!!)
        val tx = AttributeKeys.TRANSFORM.get(this)
        if (tx != null) {
            tx.transform(p, p)
            val p0 = Point2D.Double(0.0, 0.0)
            tx.transform(p0, p0)
            p.y -= p0.y
            /*
            try {
                tx.inverseTransform(p, p);
            } catch (NoninvertibleTransformException ex) {
                ex.printStackTrace();
            }*/
        }
        return Math.abs(p.y).toFloat()
    }

    public override fun invalidate() {
        super.invalidate()
        cachedTextShape = null
        cachedBounds = null
        cachedDrawingArea = null
    }

    override fun getPreferredSize(): Dimension2DDouble {
        val b = bounds
        return Dimension2DDouble(b.width, b.height)
    }

    override fun createHandles(detailLevel: Int): Collection<Handle> {
        val handles = LinkedList<Handle>()
        when (detailLevel % 2) {
            -1 // Mouse hover handles
            -> handles.add(BoundsOutlineHandle(this, false, true))
            0 -> {
                handles.add(BoundsOutlineHandle(this))
                handles.add(MoveHandle(this, RelativeLocator.northWest()))
                handles.add(MoveHandle(this, RelativeLocator.northEast()))
                handles.add(MoveHandle(this, RelativeLocator.southWest()))
                handles.add(MoveHandle(this, RelativeLocator.southEast()))
                handles.add(FontSizeHandle(this))
                handles.add(LinkHandle(this))
            }
            1 -> TransformHandleKit.addTransformHandles(this, handles)
        }
        return handles
    }

    // CONNECTING
    override fun canConnect(): Boolean {
        return false // SVG does not support connecting
    }

    override fun findConnector(p: Point2D.Double, prototype: ConnectionFigure): Connector? {
        return null // SVG does not support connectors
    }

    override fun findCompatibleConnector(c: Connector, isStartConnector: Boolean): Connector? {
        return null // SVG does not support connectors
    }

    /**
     * Returns a specialized tool for the given coordinate.
     *
     * Returns null, if no specialized tool is available.
     */
    override fun getTool(p: Point2D.Double): Tool? {
        return if (isEditable && contains(p)) {
            TextEditingTool(this)
        } else null
    }

    override fun getBaseline(): Double {
        return coordinates[0]!!.y - bounds.y
    }

    /**
     * Gets the number of characters used to expand tabs.
     */
    override fun getTabSize(): Int {
        return 8
    }

    override fun getLabelFor(): TextHolderFigure {
        return this
    }

    override fun getInsets(): Insets2D.Double {
        return Insets2D.Double()
    }

    override fun clone(): SVGFontAwesomeFigure {
        val that = super.clone() as SVGFontAwesomeFigure
        that.coordinates = arrayOfNulls(this.coordinates.size)
        for (i in this.coordinates.indices) {
            that.coordinates[i] = this.coordinates[i]!!.clone() as Point2D.Double
        }
        that.rotates = this.rotates.clone()
        that.cachedBounds = null
        that.cachedDrawingArea = null
        that.cachedTextShape = null
        return that
    }

    override fun isEmpty(): Boolean {
        return text == null || text!!.length == 0
    }

    override fun isTextOverflow(): Boolean {
        return false
    }
}
