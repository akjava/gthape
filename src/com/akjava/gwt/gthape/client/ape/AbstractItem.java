/*
Copyright (c) 2006, 2007 Alec Cove

Permission is hereby granted, free of charge, to any person obtaining a copy of this
software and associated documentation files (the "Software"), to deal in the Software
without restriction, including without limitation the rights to use, copy, modify,
merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be included in all copies
or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

/*
TODO:
*/

package com.akjava.gwt.gthape.client.ape;

import flash.display.Sprite;
import flash.display.DisplayObject;

/**
* The base class for all constraints and particles
*/
public class AbstractItem {

private Sprite _sprite
private Boolean _visible
private Boolean _alwaysRepaint


/** @private */
internal Number lineThickness
/** @private */
internal uint lineColor
/** @private */
internal Number lineAlpha
/** @private */
internal uint fillColor
/** @private */
internal Number fillAlpha
/** @private */
internal DisplayObject displayObject
/** @private */
internal Vector displayObjectOffset
/** @private */
internal Number displayObjectRotation


public function AbstractItem() {
_visible = true;
_alwaysRepaint = false;
}


/**
* This method is automatically called when an item's parent group is added to the engine,
* an item's Composite is added to a Group, or the item is added to a Composite or Group.
*/
public void init(){}


/**
* The default painting method for this item. This method is called automatically
* by the <code>APEngine.paint()</code> method.
*/
public void paint(){}


/**
* This method is called automatically when an item's parent group is removed
* from the APEngine.
*/
public void cleanup(){
sprite.graphics.clear();
for (int i = 0; i < sprite.numChildren; i++) {
sprite.removeChildAt(i);
}
}


/**
* For performance, fixed Particles and SpringConstraints don't have their <code>paint()</code>
* method called in order to avoid unnecessary redrawing. A SpringConstraint is considered
* fixed if its two connecting Particles are fixed. Setting this property to <code>true</code>
* forces <code>paint()</code> to be called if this Particle or SpringConstraint <code>fixed</code>
* property is true. If you are rotating a fixed Particle or SpringConstraint then you would set
* it's repaintFixed property to true. This property has no effect if a Particle or
* SpringConstraint is not fixed.
*/
public final boolean alwaysRepaint(){
return _alwaysRepaint;
}


/**
* @private
*/
public final void alwaysRepaint(boolean b){
_alwaysRepaint = b;
}


/**
* The visibility of the item.
*/
public boolean visible(){
return _visible;
}


/**
* @private
*/
public void visible(boolean v){
_visible = v;
sprite.visible = v;
}


/**
* Sets the line and fill of this Item.
*/
//lineThickness=0,lineColor=0x000000,lineAlpha=1,fillColor=0xffffff,fillAlpha=1
public void setStyle(double lineThickness,int lineColor,double lineAlpha,int fillColor,double fillAlpha){

setLine(lineThickness, lineColor, lineAlpha);
setFill(fillColor, fillAlpha);
}


/**
* Sets the style of the line for this Item.
*/
//thickness=0,color=0x000000,alpha=1
public void setLine(double thickness,int color,double alpha){
lineThickness = thickness;
lineColor = color;
lineAlpha = alpha;
}


/**
* Sets the style of the fill for this Item.
*/
//color=0xffffff,alpha=1
public void setFill(int color,double alpha){
fillColor = color;
fillAlpha = alpha;
}


/**
* Provides a Sprite to use as a container for drawing or adding children. When the
* sprite is requested for the first time it is automatically added to the global
* container in the APEngine class.
*/
public Sprite sprite(){

if (_sprite != null) return _sprite;

if (APEngine.container == null) {
throw new Error("The container property of the APEngine class has not been set");
}

_sprite = new Sprite();
APEngine.container.addChild(_sprite);
return _sprite;
}
}
