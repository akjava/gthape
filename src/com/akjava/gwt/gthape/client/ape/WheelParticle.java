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
- review how the WheelParticle needs to have the o value passed during collision
- clear up the difference between speed and angularVelocity
- can the wheel rotate steadily using speed? angularVelocity causes (unwanted?) acceleration
*/
package com.akjava.gwt.gthape.client.ape;


/**
* A particle that simulates the behavior of a wheel
*/
public class WheelParticle extends CircleParticle {

private RimParticle rp;
private Vector tan;
private Vector normSlip;
private Vector orientation;

private double traction;


/**
* @param x The initial x position.
* @param y The initial y position.
* @param radius The radius of this particle.
* @param fixed Determines if the particle is fixed or not. Fixed particles
* are not affected by forces or collisions and are good to use as surfaces.
* Non-fixed particles move freely in response to collision and forces.
* @param mass The mass of the particle
* @param elasticity The elasticity of the particle. Higher values mean more elasticity.
* @param friction The surface friction of the particle.
* @param traction The surface traction of the particle.
* <p>
* Note that WheelParticles can be fixed but rotate freely.
* </p>
*/
//fixed=false,mass=1,elasticity=0.3,friction=0,traction=1
public  WheelParticle(double x,double y,double radius,boolean fixed,double mass,double elasticity,double friction,double traction){

super(x,y,radius,fixed, mass, elasticity, friction);
tan = new Vector(0,0);
normSlip = new Vector(0,0);
rp = new RimParticle(radius, 2);

this.traction = traction;
orientation = new Vector();
}


/**
* The speed of the WheelParticle. You can alter this value to make the
* WheelParticle spin.
*/
public double speed(){
return rp.speed();
}


/**
* @private
*/
public void speed(double s){
rp.speed(s);
}


/**
* The angular velocity of the WheelParticle. You can alter this value to make the
* WheelParticle spin.
*/
public double angularVelocity(){
return rp.angularVelocity();
}


/**
* @private
*/
public void angularVelocity(double a){
rp.angularVelocity(a);
}


/**
* The amount of traction during a collision. This property controls how much traction is
* applied when the WheelParticle is in contact with another particle. If the value is set
* to 0, there will be no traction and the WheelParticle will behave as if the
* surface was totally slippery, like ice. Values should be between 0 and 1.
*
* <p>
* Note that the friction property behaves differently than traction. If the surface
* friction is set high during a collision, the WheelParticle will move slowly as if
* the surface was covered in glue.
* </p>
*/
public double traction(){
return 1 - traction;
}


/**
* @private
*/
public void traction(double t){
traction = 1 - t;
}


/**
* The default paint method for the particle. Note that you should only use
* the default painting methods for quick prototyping. For anything beyond that
* you should always write your own classes that either extend one of the
* APE particle and constraint classes, or is a composite of them. Then within that
* class you can define your own custom painting method.
*/
public  void paint(){
sprite.setX(curr.x);
sprite.setY(curr.y);
sprite.setRotation(angle());
}


/**
* Sets up the visual representation of this particle. This method is automatically called when
* an particle is added to the engine.
*/
public  void init(){
cleanup();
if (displayObject != null) {
initDisplay();
} else {

sprite.getGraphics().clear();
sprite.getGraphics().lineStyle(lineThickness, lineColor, lineAlpha);

// wheel circle
sprite.getGraphics().beginFill(fillColor, fillAlpha);
sprite.getGraphics().drawCircle(0, 0, radius());
sprite.getGraphics().endFill();

// spokes
sprite.getGraphics().moveTo(-radius(), 0);
sprite.getGraphics().lineTo( radius(), 0);
sprite.getGraphics().moveTo(0, -radius());
sprite.getGraphics().lineTo(0, radius());
}
paint();
}


/**
* The rotation of the wheel in radians.
*/
public double radian(){
orientation.setTo(rp.curr.x, rp.curr.y);
return Math.atan2(orientation.y, orientation.x) + Math.PI;
}


/**
* The rotation of the wheel in degrees.
*/
public double angle(){
return radian() * MathUtil.ONE_EIGHTY_OVER_PI;
}


/**
*
*/
public  void update(double dt){
super.update(dt);
rp.update(dt);
}


/**
* @private
*/
void resolveCollision(Vector mtd,Vector vel,Vector n,double d,int o,AbstractParticle p){

// review the o (order) need here - its a hack fix
super.resolveCollision(mtd, vel, n, d, o, p);
resolve(n.mult(MathUtil.sign(d * o)));
}


/**
* simulates torque/wheel-ground interaction - n is the surface normal
* Origins of this code thanks to Raigan Burns, Metanet software
*/
private void resolve(Vector n){

// this is the tangent vector at the rim particle
tan.setTo(-rp.curr.y, rp.curr.x);

// normalize so we can scale by the rotational speed
tan = tan.normalize();

// velocity of the wheel's surface
Vector wheelSurfaceVelocity = tan.mult(rp.speed());

// the velocity of the wheel's surface relative to the ground
Vector combinedVelocity = velocity().plusEquals(wheelSurfaceVelocity);

// the wheel's comb velocity projected onto the contact normal
double cp = combinedVelocity.cross(n);

// set the wheel's spinspeed to track the ground
tan.multEquals(cp);
rp.prev.copy(rp.curr.minus(tan));

// some of the wheel's torque is removed and converted into linear displacement
double slipSpeed = (1 - traction) * rp.speed();
normSlip.setTo(slipSpeed * n.y, slipSpeed * n.x);
curr.plusEquals(normSlip);
rp.speed(rp.speed()*traction);
}
}
