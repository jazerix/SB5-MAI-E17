package org.jhotdraw.samples.svg.tools;

import java.util.Map;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.CreationTool;

import org.jhotdraw.samples.svg.figures.SVGFontAwesome;

// #CHANGED (FILE IS CREATED)

// Only used ao 'instanceof' can be called to figure of which tool it is.

public class FontAwesomeCreationTool extends CreationTool {

    public FontAwesomeCreationTool(SVGFontAwesome prototype, Map<AttributeKey,Object> attributes) {
        super(prototype, attributes);
    }
}
