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
- tearable, tearLength
- consider breaking the collidable (vs non collidable) functionality into another class
- get/set collidable, currently it is only get
- see if radian, angle, and center can be more efficient
- do we need a scaleToLength for non collidable?
- resolveCycles
*/
package com.akjava.gwt.gthape.client.ape;

//import com.akjava.gwt.gthape.client.display.DisplayObject;

/**
* A Spring-like constraint that connects two particles
*/
public class SpringConstraint extends AbstractConstraint {

private AbstractParticle p1;
private AbstractParticle p2;

private double restLength;
private boolean collidable;
private SpringConstraintParticle scp;

/**
* @param p1 The first particle this constraint is connected to.
* @param p2 The second particle this constraint is connected to.
* @param stiffness The strength of the spring. Valid values are between 0 and 1. Lower values
* result in softer springs. Higher values result in stiffer, stronger springs.
* @param collidable Determines if the constraint will be checked for collision
* @param rectHeight If the constraint is collidable, the height of the collidable area
* can be set in pixels. The height is perpendicular to the two attached particles.
* @param rectScale If the constraint is collidable, the scale of the collidable area
* can be set in value from 0 to 1. The scale is percentage of the distance between
* the the two attached particles.
* @param scaleToLength If the constraint is collidable and this value is true, the
* collidable area will scale based on changes in the distance of the two particles.
*/
//stiffness=0.5,collidable=false,rectHeight=1,rectScale=1,scaleToLength=false
public  SpringConstraint(AbstractParticle p1,AbstractParticle p2,double stiffness){
this(p1,p2,stiffness,false,1,1,false);
}

public  SpringConstraint(AbstractParticle p1,AbstractParticle p2,double stiffness,boolean collidable,double rectHeight){
this(p1,p2,stiffness,collidable,rectHeight,1,false);
}
public  SpringConstraint(AbstractParticle p1,AbstractParticle p2,double stiffness,boolean collidable,double rectHeight,double rectScale,boolean scaleToLength){

super(stiffness);

this.p1 = p1;
this.p2 = p2;
checkParticlesLocation();

restLength = currLength();
setCollidable(collidable, rectHeight, rectScale, scaleToLength);
}


/**
* The rotational value created by the positions of the two particles attached to this
* SpringConstraint. You can use this property to in your own painting methods, along with the
* <code>center</code> property.
*
* @returns A Number representing the rotation of this SpringConstraint in radians
*/
public double radian(){
Vector d = delta();
return Math.atan2(d.y, d.x);
}


/**
* The rotational value created by the positions of the two particles attached to this
* SpringConstraint. You can use this property to in your own painting methods, along with the
* <code>center</code> property.
*
* @returns A Number representing the rotation of this SpringConstraint in degrees
*/
public double angle(){
return radian() * MathUtil.ONE_EIGHTY_OVER_PI;
}


/**
* The center position created by the relative positions of the two particles attached to this
* SpringConstraint. You can use this property to in your own painting methods, along with the
* rotation property.
*
* @returns A Vector representing the center of this SpringConstraint
*/
public Vector center(){
return (p1.curr.plus(p2.curr)).divEquals(2);
}


/**
* If the <code>collidable</code> property is true, you can set the scale of the collidible area
* between the two attached particles. Valid values are from 0 to 1. If you set the value to 1, then
* the collision area will extend all the way to the two attached particles. Setting the value lower
* will result in an collision area that spans a percentage of that distance. Setting the value
* higher will cause the collision rectangle to extend past the two end particles.
*/
public void rectScale(double s){
if (scp == null) return;
scp.rectScale(s);
}


/**
* @private
*/
public double rectScale(){
return scp.rectScale();
}


/**
* Returns the length of the SpringConstraint, the distance between its two
* attached particles.
*/
public double currLength(){
return p1.curr.distance(p2.curr);
}


/**
* If the <code>collidable</code> property is true, you can set the height of the
* collidible rectangle between the two attached particles. Valid values are greater
* than 0. If you set the value to 10, then the collision rect will be 10 pixels high.
* The height is perpendicular to the line connecting the two particles
*/
public double rectHeight(){
return scp.rectHeight();
}


/**
* @private
*/
public void rectHeight(double h){
if (scp == null) return;
scp.rectHeight(h);
}


/**
* The <code>restLength</code> property sets the length of SpringConstraint. This value will be
* the distance between the two particles unless their position is altered by external forces.
* The SpringConstraint will always try to keep the particles this distance apart. Values must
* be > 0.
*/
public double restLength(){
return restLength;
}


/**
* @private
*/
public void restLength(double r){
if (r <= 0) throw new RuntimeException("restLength must be greater than 0");
restLength=r;
}


/**
* Determines if the area between the two particles is tested for collision. If this value is on
* you can set the <code>rectHeight</code> and <code>rectScale</code> properties
* to alter the dimensions of the collidable area.
*/
public boolean collidable(){
return collidable;
}


/**
* For cases when the SpringConstraint is <code>collidable</code> and only one of the
* two end particles are fixed. This value will dispose of collisions near the
* fixed particle, to correct for situations where the collision could never be
* resolved. Values must be between 0.0 and 1.0.
*/
public double fixedEndLimit(){
return scp.fixedEndLimit();
}


/**
* @private
*/
public void fixedEndLimit(double f){
if (scp == null) return;
scp.fixedEndLimit(f);
}


/**
*
*/
//scaleToLength=false
public void setCollidable(boolean b,double rectHeight,double rectScale,boolean scaleToLength){

collidable = b;
scp = null;

if (collidable) {
scp = new SpringConstraintParticle(p1, p2, this, rectHeight, rectScale, scaleToLength);
}
}


/**
* Returns true if the passed particle is one of the two particles attached to this SpringConstraint.
*/
public boolean isConnectedTo(AbstractParticle p){
return (p == p1 || p == p2);
}


/**
* Returns true if both connected particle's <code>fixed</code> property is true.
*/
public boolean fixed(){
return (p1.fixed() && p2.fixed());
}


/**
* Sets up the visual representation of this SpringContraint. This method is called
* automatically when an instance of this SpringContraint's parent Group is added to
* the APEngine, when  this SpringContraint's Composite is added to a Group, or this
* SpringContraint is added to a Composite or Group.
*/
public  void init(){
	
cleanup();
/*
if (collidable) {
scp.init();
} else if (displayObject != null) {
initDisplay();
}
paint();
*/
}


/**
* The default painting method for this constraint. This method is called automatically
* by the <code>APEngine.paint()</code> method. If you want to define your own custom painting
* method, then create a subclass of this class and override <code>paint()</code>.
*/
public  void paint(){
/*
if (collidable) {
scp.paint();
} else if (displayObject != null) {
Vector c = center();
if(sprite!=null){
sprite.setX(c.x);
sprite.setY(c.y);
sprite.setRotation(angle());
}
} else {
if(sprite!=null){
sprite.getGraphics().clear();
sprite.getGraphics().lineStyle(lineThickness, lineColor, lineAlpha);
sprite.getGraphics().moveTo(p1.px(), p1.py());
sprite.getGraphics().lineTo(p2.px(), p2.py());
}
}
*/
}


/**
* Assigns a DisplayObject to be used when painting this constraint.
*/
//offsetX=0,offsetY=0,rotation=0
/*
public void setDisplay(DisplayObject d,double offsetX,double offsetY,double rotation){

if (collidable) {
scp.setDisplay(d, offsetX, offsetY, rotation);
} else {
displayObject = d;
displayObjectRotation = rotation;
displayObjectOffset = new Vector(offsetX, offsetY);
}
}
*/


/**
* @private
*/
 void initDisplay(){
	/* 
if (collidable) {
scp.initDisplay();
} else {
displayObject.setX(displayObjectOffset.x);
displayObject.setY(displayObjectOffset.y);
displayObject.setRotation(displayObjectRotation);
sprite.addChild(displayObject);
}
*/
}


/**
* @private
*/
 Vector delta(){
return p1.curr.minus(p2.curr);
}


/**
* @private
*/
 SpringConstraintParticle scp(){
return scp;
}


/**
* @private
*/
void resolve(){

if (p1.fixed() && p2.fixed()) return;

double deltaLength = currLength();
double diff = (deltaLength - restLength) / (deltaLength * (p1.invMass() + p2.invMass()));
Vector dmds = delta().mult(diff * stiffness());

p1.curr.minusEquals(dmds.mult(p1.invMass()));
p2.curr.plusEquals (dmds.mult(p2.invMass()));
}


/**
* if the two particles are at the same location offset slightly
*/
private void checkParticlesLocation(){
if (p1.curr.x == p2.curr.x && p1.curr.y == p2.curr.y) {
p2.curr.x += 0.0001;
}
}
}
