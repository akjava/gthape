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
- review getProjection() for precomputing. radius can definitely be precomputed/stored
*/

package com.akjava.gwt.gthape.client.ape;


/**
* A rectangular shaped particle.
*/
public class RectangleParticle extends AbstractParticle {

protected double[] extents;
protected Vector[] axes;
protected double radian;


/**
* @param x The initial x position.
* @param y The initial y position.
* @param width The width of this particle.
* @param height The height of this particle.
* @param rotation The rotation of this particle in radians.
* @param fixed Determines if the particle is fixed or not. Fixed particles
* are not affected by forces or collisions and are good to use as surfaces.
* Non-fixed particles move freely in response to collision and forces.
* @param mass The mass of the particle
* @param elasticity The elasticity of the particle. Higher values mean more elasticity.
* @param friction The surface friction of the particle.
* <p>
* Note that RectangleParticles can be fixed but still have their rotation property
* changed.
* </p>
*/
//rotation=0,fixed=false,mass=1,elasticity=0.3,friction=0
public RectangleParticle (double x,double y,double width,double height,double rotation,boolean fixed){
	this(x,y,width,height,rotation,fixed,1,0.3,0);
}
public RectangleParticle (double x,double y,double width,double height,double rotation,boolean fixed,double mass,double elasticity,double friction){

super(x, y, fixed, mass, elasticity, friction);

extents = new double[]{width/2, height/2};
axes = new Vector[]{new Vector(0,0), new Vector(0,0)};
radian = rotation;
}


/**
* The rotation of the RectangleParticle in radians. For drawing methods you may
* want to use the <code>angle</code> property which gives the rotation in
* degrees from 0 to 360.
*
* <p>
* Note that while the RectangleParticle can be rotated, it does not have angular
* velocity. In otherwords, during collisions, the rotation is not altered,
* and the energy of the rotation is not applied to other colliding particles.
* </p>
*/
public double radian(){
return radian;
}


/**
* @private
*/
public void radian(double t){
radian = t;
setAxes(t);
}


/**
* The rotation of the RectangleParticle in degrees.
*/
public double angle(){
return radian * MathUtil.ONE_EIGHTY_OVER_PI;
}


/**
* @private
*/
public void angle(double a){
radian = a * MathUtil.PI_OVER_ONE_EIGHTY;
}


/**
* Sets up the visual representation of this RectangleParticle. This method is called
* automatically when an instance of this RectangleParticle's parent Group is added to
* the APEngine, when  this RectangleParticle's Composite is added to a Group, or the
* RectangleParticle is added to a Composite or Group.
*/
public  void init(){
cleanup();
if (displayObject != null) {
initDisplay();
} else {

double w = extents[0] * 2;
double h = extents[1] * 2;

sprite.getGraphics().clear();//TODO think something
sprite.getGraphics().lineStyle(lineThickness, lineColor, lineAlpha);
sprite.getGraphics().beginFill(fillColor, fillAlpha);
sprite.getGraphics().drawRect(-w/2, -h/2, w, h);
sprite.getGraphics().endFill();
}
paint();
}


/**
* The default painting method for this particle. This method is called automatically
* by the <code>APEngine.paint()</code> method. If you want to define your own custom painting
* method, then create a subclass of this class and override <code>paint()</code>.
*/
public  void paint(){
sprite.setX(curr.x);
sprite.setY(curr.y);
sprite.setRotation(angle());
}


public void width(double w){
extents[0] = w/2;
}


public double width(){
return extents[0] * 2;
}


public void height(double h){
extents[1] = h / 2;
}


public double height(){
return extents[1] * 2;
}


/**
* @private
*/
Vector[] axes(){
return axes;
}


/**
* @private
*/
double[] extents(){
return extents;
}


/**
* @private
*/
Interval getProjection(Vector axis){

double radius =
extents[0] * Math.abs(axis.dot(axes[0]))+
extents[1] * Math.abs(axis.dot(axes[1]));

double c = samp.dot(axis);

interval.min = c - radius;
interval.max = c + radius;
return interval;
}


/**
*
*/
private void setAxes(double t){
double s = Math.sin(t);
double c = Math.cos(t);

axes[0].x = c;
axes[0].y = s;
axes[1].x = -s;
axes[1].y = c;
}
}
