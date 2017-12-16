/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhotdraw.samples.svg.figures

import dk.sdu.mmmi.featuretracer.lib.FeatureEntryPoint
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.FontFormatException
import java.awt.Graphics2D
import java.awt.GridLayout
import java.awt.Shape
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.font.FontRenderContext
import java.awt.font.TextAttribute
import java.awt.font.TextLayout
import java.awt.geom.AffineTransform
import java.awt.geom.NoninvertibleTransformException
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import java.io.IOException
import java.util.ArrayList
import java.util.HashMap
import java.util.LinkedList
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import org.jhotdraw.draw.AttributeKeys.FILL_COLOR
import org.jhotdraw.draw.AttributeKeys.FONT_SIZE
import org.jhotdraw.draw.AttributeKeys.TEXT
import org.jhotdraw.draw.AttributeKeys.TRANSFORM
import org.jhotdraw.draw.BoundsOutlineHandle
import org.jhotdraw.draw.FontSizeHandle
import org.jhotdraw.draw.Handle
import org.jhotdraw.draw.MoveHandle
import org.jhotdraw.draw.RelativeLocator
import org.jhotdraw.draw.TextHolderFigure
import org.jhotdraw.geom.Geom
import org.jhotdraw.geom.Insets2D
import org.jhotdraw.samples.svg.SVGAttributeKeys
import javax.xml.stream.events.Characters

/**

 * @author Tommy
 */
class SVGFontAwesome : SVGAttributedFigure(), TextHolderFigure, SVGFigure {

    protected var coordinates = arrayOf(Point2D.Double())
    protected var rotates = doubleArrayOf(0.0)
    @Transient private var cachedBounds: Rectangle2D.Double? = null
    @Transient private var cachedDrawingArea: Rectangle2D.Double? = null
    @Transient private var cachedShape: Shape? = null

    private var fontIcons: Font? = null
    private var listIcons: List<String>? = null

    val icon = "\uF014"

    init {
        setIcon(icon)
        SVGAttributeKeys.setDefaults(this)
        setVars()
    }

    override fun createHandles(detailLevel: Int): Collection<Handle> {
        val handles = LinkedList<Handle>()
        when (detailLevel % 2) {
            0 -> {
                handles.add(BoundsOutlineHandle(this))
                handles.add(MoveHandle(this, RelativeLocator.northWest()))
                handles.add(MoveHandle(this, RelativeLocator.northEast()))
                handles.add(MoveHandle(this, RelativeLocator.southWest()))
                handles.add(MoveHandle(this, RelativeLocator.southEast()))
                handles.add(FontSizeHandle(this))
                handles.add(LinkHandle(this))
            }
            1 -> openPanel()
            else -> {
            }
        }
        return handles
    }

    private fun getIconsString(count: Int): List<String> {
        val icons = (0 until count + 1).map { Character.toString((it + 61400).toChar()) }
        return icons
    }

    fun setVars() {
        listIcons = getIconsString(35 * 20)
        try {
            fontIcons = Font.createFont(Font.TRUETYPE_FONT, Int::class.javaPrimitiveType!!.getResourceAsStream("org/jhotdraw/samples/svg/fonts/fontawesome-webfont.ttf")).deriveFont(20f)
        } catch (ex: FontFormatException) {
        } catch (ex: IOException) {
        }

    }

    fun openPanel() {
        val frame = JFrame("Icons")
        val panel = JPanel()

        panel.preferredSize = Dimension(60 * 20, 20 * 35)
        panel.layout = GridLayout(35, 20)

        for (i in listIcons!!) {
            val btn = JButton(i)
            btn.font = fontIcons!!.deriveFont(16f)
            btn.addActionListener { ae: ActionEvent ->
                setText(i)
                frame.dispose()
            }
            panel.add(btn)
        }

        frame.contentPane.add(panel)
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }

    fun setIcon(newText: String) {
        TEXT.set(this, newText)
    }

    override fun drawFill(g: Graphics2D) {
        g.fill(iconShape)
    }

    override fun drawStroke(g: Graphics2D) {
        g.draw(iconShape)
    }

    override fun getBounds(): Rectangle2D.Double {
        if (cachedBounds == null) {
            cachedBounds = Rectangle2D.Double()
            cachedBounds!!.setRect(iconShape?.bounds2D)
        }
        return cachedBounds!!.clone() as Rectangle2D.Double
    }

    override fun setBounds(anchor: Point2D.Double, lead: Point2D.Double) {
        coordinates = arrayOf(Point2D.Double(anchor.x, anchor.y))
        rotates = doubleArrayOf(0.0)
    }

    private val iconShape: Shape?
        get() {
            if (cachedShape == null) {
                var text: String? = text
                if (text == null || text.length == 0) {
                    text = icon
                }

                val frc = fontRenderContext
                val textAttributes = HashMap<TextAttribute, Any>()
                textAttributes.put(TextAttribute.FONT, this.font!!)
                val textLayout = TextLayout(text, textAttributes, frc)

                val tx = AffineTransform()
                tx.translate(coordinates[0].x, coordinates[0].y)
                tx.rotate(rotates[0])

                cachedShape = tx.createTransformedShape(textLayout.getOutline(tx))
                cachedShape = textLayout.getOutline(tx)
            }
            return cachedShape
        }

    override fun contains(p: Point2D.Double): Boolean {
        var p = p
        if (TRANSFORM.get(this) != null) {
            try {
                p = TRANSFORM.get(this).inverseTransform(p, Point2D.Double()) as Point2D.Double
            } catch (ex: NoninvertibleTransformException) {
                ex.printStackTrace()
            }

        }
        return iconShape!!.bounds2D.contains(p)
    }

    override fun getTransformRestoreData(): Any {
        val restoredCoordinates = this.coordinates.clone()
        for (i in this.coordinates.indices) {
            restoredCoordinates[i] = this.coordinates[i].clone() as Point2D.Double
        }
        return arrayOf(TRANSFORM.getClone(this), restoredCoordinates)
    }

    override fun restoreTransformTo(geometry: Any) {
        val restoreData = geometry as Array<Any>
        TRANSFORM.basicSetClone(this, restoreData[0] as AffineTransform)
        val restoredCoordinates = restoreData[1] as Array<Point2D.Double>
        for (i in this.coordinates.indices) {
            coordinates[i] = restoredCoordinates[i].clone() as Point2D.Double
        }
        invalidate()
    }

    override fun transform(tx: AffineTransform) {
        if (TRANSFORM.get(this) != null || tx.type != tx.type and AffineTransform.TYPE_TRANSLATION) {
            if (TRANSFORM.get(this) == null) {
                TRANSFORM.basicSet(this, tx.clone() as AffineTransform)
            } else {
                val t = TRANSFORM.getClone(this)
                t.preConcatenate(tx)
                TRANSFORM.basicSet(this, t)
            }
        } else {
            for (i in coordinates.indices) tx.transform(coordinates[i], coordinates[i])
        }
        invalidate()
    }

    override fun getDrawingArea(): Rectangle2D.Double {
        if (cachedDrawingArea == null) {
            val rx = bounds
            val r = rx ?: Rectangle2D.Double(rx.getX(), rx.getY(), rx.getWidth(), rx.getHeight())
            val g = SVGAttributeKeys.getPerpendicularHitGrowth(this)
            Geom.grow(r, g, g)
            if (TRANSFORM.get(this) == null) {
                cachedDrawingArea = r
            } else {
                cachedDrawingArea = Rectangle2D.Double()
                cachedDrawingArea!!.setRect(TRANSFORM.get(this).createTransformedShape(r).bounds2D)
            }
        }
        return cachedDrawingArea!!.clone() as Rectangle2D.Double
    }

    override fun getFont(): Font? {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, Int::class.javaPrimitiveType!!.getResourceAsStream("org/jhotdraw/samples/svg/fonts/fontawesome-webfont.ttf")).deriveFont(fontSize)
        } catch (ex: FontFormatException) {
            println(ex.message)
        } catch (ex: IOException) {
            println(ex.message)
        }

        return null
    }

    override fun setFontSize(size: Float) {
        val p = Point2D.Double(0.0, size.toDouble())
        val tx = TRANSFORM.get(this)
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
        FONT_SIZE.set(this, Math.abs(p.y))
    }

    override fun getFontSize(): Float {
        val p = Point2D.Double(0.0, FONT_SIZE.get(this))
        val tx = TRANSFORM.get(this)
        if (tx != null) {
            tx.transform(p, p)
            val p0 = Point2D.Double(0.0, 0.0)
            tx.transform(p0, p0)
            p.y -= p0.y
        }
        return Math.abs(p.y).toFloat()
    }

    public override fun invalidate() {
        super.invalidate()
        cachedDrawingArea = null
        cachedShape = null
        cachedBounds = null
    }

    override fun isTextOverflow(): Boolean {
        throw UnsupportedOperationException("Not supported yet isTextOverflow.") //To change body of generated methods, choose Tools | Templates.
    }

    override fun isEmpty(): Boolean {
        throw UnsupportedOperationException("Not supported yet isEmpty.") //To change body of generated methods, choose Tools | Templates.
    }

    override fun getInsets(): Insets2D.Double {
        return Insets2D.Double()
    }

    override fun setText(text: String) {
        TEXT.set(this, text)
    }

    override fun getBaseline(): Double {
        return coordinates[0].y - bounds.y
    }

    override fun getText(): String? {
        return getAttribute(TEXT)
    }

    override fun getTextColumns(): Int {
        return 4
    }

    override fun getTextColor(): Color {
        return FILL_COLOR.get(this)
    }

    override fun getFillColor(): Color {
        return if (FILL_COLOR.get(this) == null || FILL_COLOR.get(this) == Color.white) Color.black else Color.WHITE
    }

    override fun getTabSize(): Int {
        return 8
    }

    override fun isEditable(): Boolean {
        return false
    }

    override fun getLabelFor(): TextHolderFigure {
        return this
    }
}
