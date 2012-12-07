package com.akjava.gwt.gthape.client.ape;

//import com.akjava.gwt.gthape.client.display.Builders;
//import com.akjava.gwt.gthape.client.display.Sprite;

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

- scale the post collision velocity by both the position *and* mass of each particle.
currently only the position and average inverse mass is used. as with the velocity,
it might be a problem since the contact point is not available when the mass is
needed.

- review all p1 p2 getters (eg get mass). can it be stored instead of computed everytime?

- consider if the API should let the user set the SCP's properties directly. elasticity,
friction, mass, etc are all inherited from the attached particles

- consider a more accurate velocity getter. should use a parameterized value
to scale the velocity relative to the contact point. one problem is the velocity is
needed before the contact point is established.

- setCorners is a duplicate from the updateCornerPositions method in the RectangleParticle class,
it needs to be placed back in that class but use the displacement as suggested by Jim B. Its here
because of the way RectangleParticle calculates the corners -- once on, they are calculated
constantly. that should be fixed too.

- getContactPointParam should probably belong to the rectangleparticle and circleparticle classes.
also the functions respective to each, for better OOD- clean up resolveCollision with submethods*
*/
class SpringConstraintParticle extends RectangleParticle{

	private AbstractParticle p1;
	private AbstractParticle p2;

	private Vector avgVelocity;
	private Vector lambda;
	private SpringConstraint parent;
	private boolean scaleToLength;

	private Vector rca;
	private Vector rcb;
	private double s;

	private double rectScale;
	private double rectHeight;
	private double fixedEndLimit;

	public  SpringConstraintParticle(AbstractParticle p1,AbstractParticle p2,SpringConstraint p,double rectHeight,double rectScale,boolean scaleToLength){
	
super(0,0,0,0,0,false);

this.p1 = p1;
this.p2 = p2;

lambda = new Vector(0,0);
avgVelocity = new Vector(0,0);

parent = p;
this.rectScale = rectScale;
this.rectHeight = rectHeight;
this.scaleToLength = scaleToLength;

fixedEndLimit = 0;
rca = new Vector();
rcb = new Vector();
}



 void rectScale(double s){
rectScale = s;
}


/**
* @private
*/
double rectScale(){
return rectScale;
}


 void rectHeight(double r){
rectHeight = r;
}


/**
* @private
*/
 double rectHeight(){
return rectHeight;
}


/**
* For cases when the SpringConstraint is both collidable and only one of the
* two end particles are fixed, this value will dispose of collisions near the
* fixed particle, to correct for situations where the collision could never be
* resolved.
*/
 void fixedEndLimit(double f){
fixedEndLimit = f;
}


/**
* @private
*/
 double fixedEndLimit(){
return fixedEndLimit;
}


/**
* returns the average mass of the two connected particles
*/
public  double mass(){
return (p1.mass() + p2.mass()) / 2;
}


/**
* returns the average elasticity of the two connected particles
*/
public  double elasticity(){
return (p1.elasticity() + p2.elasticity()) / 2;
}


/**
* returns the average friction of the two connected particles
*/
public  double friction(){
return (p1.friction() + p2.friction()) / 2;
}


/**
* returns the average velocity of the two connected particles
*/
public  Vector velocity(){
Vector p1v =  p1.velocity();
Vector p2v =  p2.velocity();

avgVelocity.setTo(((p1v.x + p2v.x) / 2), ((p1v.y + p2v.y) / 2));
return avgVelocity;
}


public  void init(){
/*	
if (displayObject != null) {
initDisplay();
} else {

Sprite inner = Builders.getSpliteBuilder().createSprite();
parent.sprite.addChild(inner);
inner.setName("inner");

double w = parent.currLength() * rectScale;
double h = rectHeight;

inner.getGraphics().clear();
inner.getGraphics().lineStyle(parent.lineThickness, parent.lineColor, parent.lineAlpha);
inner.getGraphics().beginFill(parent.fillColor, parent.fillAlpha);
inner.getGraphics().drawRect(-w/2, -h/2, w, h);
inner.getGraphics().endFill();

}
paint();
*/
}


public  void paint(){/*

Vector c = parent.center();
Sprite s = parent.sprite;
if(s==null){
	return;
}
if (scaleToLength) {
s.getChildByName("inner").setWidth(parent.currLength() * rectScale);
} else if (displayObject != null) {
s.getChildByName("inner").setWidth( parent.restLength() * rectScale);
}
s.setX(c.x);
s.setY(c.y);
s.setRotation(parent.angle());
*/
}


/**
* @private
*/
void initDisplay(){
	/*
displayObject.setX(displayObjectOffset.x);
displayObject.setY(displayObjectOffset.y);
displayObject.setRotation(displayObjectRotation);

Sprite inner = Builders.getSpliteBuilder().createSprite();
inner.setName("inner");

inner.addChild(displayObject);
parent.sprite.addChild(inner);
*/
}


/**
* @private
* returns the average inverse mass.
*/
double invMass(){
if (p1.fixed() && p2.fixed()) return 0;
return 1 / ((p1.mass() + p2.mass()) / 2);
}


/**
* called only on collision
*/
 void updatePosition(){
Vector c = parent.center();
curr.setTo(c.x, c.y);

width((scaleToLength) ? parent.currLength() * rectScale : parent.restLength() * rectScale);
height(rectHeight);
radian(parent.radian());
}


void resolveCollision(Vector mtd,Vector vel,Vector n,double d,int o,AbstractParticle p){

double t = getContactPointParam(p);
double c1 = (1 - t);
double c2 = t;

// if one is fixed then move the other particle the entire way out of collision.
// also, dispose of collisions at the sides of the scp. The higher the fixedEndLimit
// value, the more of the scp not be effected by collision.
if (p1.fixed()) {
if (c2 <= fixedEndLimit) return;
lambda.setTo(mtd.x / c2, mtd.y / c2);
p2.curr.plusEquals(lambda);
p2.velocity(vel);

} else if (p2.fixed()) {
if (c1 <= fixedEndLimit) return;
lambda.setTo(mtd.x / c1, mtd.y / c1);
p1.curr.plusEquals(lambda);
p1.velocity(vel);

// else both non fixed - move proportionally out of collision
} else {
double denom = (c1 * c1 + c2 * c2);
if (denom == 0) return;
lambda.setTo(mtd.x / denom, mtd.y / denom);

p1.curr.plusEquals(lambda.mult(c1));
p2.curr.plusEquals(lambda.mult(c2));

// if collision is in the middle of SCP set the velocity of both end particles
if (t == 0.5) {
p1.velocity(vel);
p2.velocity(vel);

// otherwise change the velocity of the particle closest to contact
} else {
AbstractParticle corrParticle = (t < 0.5) ? p1 : p2;
corrParticle.velocity(vel);
}
}
}


/**
* given point c, returns a parameterized location on this SCP. Note
* this is just treating the SCP as if it were a line segment (ab).
*/
private double closestParamPoint(Vector c){
Vector ab = p2.curr.minus(p1.curr);
double t = (ab.dot(c.minus(p1.curr))) / (ab.dot(ab));
return MathUtil.clamp(t, 0, 1);
}


/**
* returns a contact location on this SCP expressed as a parametric value in [0,1]
*/
private double getContactPointParam(AbstractParticle p){

double t=0;

if (p instanceof CircleParticle)  {
t = closestParamPoint(p.curr);
} else if (p instanceof RectangleParticle) {

// go through the sides of the colliding rectangle as line segments
int shortestIndex=0;
double[] paramList = new double[4];
double shortestDistance = Double.POSITIVE_INFINITY;

for (int i = 0; i < 4; i++) {
setCorners((RectangleParticle)p, i);

// check for closest points on SCP to side of rectangle
double d = closestPtSegmentSegment();
if (d < shortestDistance) {
shortestDistance = d;
shortestIndex = i;
paramList[i] = s;
}
}
t = paramList[shortestIndex];
}
return t;
}


/**
*
*/
private void setCorners(RectangleParticle r,int i){

double rx = r.curr.x;
double ry = r.curr.y;

Vector[] axes = r.axes;
double[] extents = r.extents;

double ae0_x = axes[0].x * extents[0];
double ae0_y = axes[0].y * extents[0];
double ae1_x = axes[1].x * extents[1];
double ae1_y = axes[1].y * extents[1];

double emx = ae0_x - ae1_x;
double emy = ae0_y - ae1_y;
double epx = ae0_x + ae1_x;
double epy = ae0_y + ae1_y;


if (i == 0) {
// 0 and 1
rca.x = rx - epx;
rca.y = ry - epy;
rcb.x = rx + emx;
rcb.y = ry + emy;

} else if (i == 1) {
// 1 and 2
rca.x = rx + emx;
rca.y = ry + emy;
rcb.x = rx + epx;
rcb.y = ry + epy;

} else if (i == 2) {
// 2 and 3
rca.x = rx + epx;
rca.y = ry + epy;
rcb.x = rx - emx;
rcb.y = ry - emy;

} else if (i == 3) {
// 3 and 0
rca.x = rx - emx;
rca.y = ry - emy;
rcb.x = rx - epx;
rcb.y = ry - epy;
}
}


/**
* pp1-pq1 will be the SCP line segment on which we need parameterized s.
*/
private double closestPtSegmentSegment(){

Vector pp1 = p1.curr;
Vector pq1 = p2.curr;
Vector pp2 = rca;
Vector pq2 = rcb;

Vector d1 = pq1.minus(pp1);
Vector d2 = pq2.minus(pp2);
Vector r = pp1.minus(pp2);

double t;
double a = d1.dot(d1);
double e = d2.dot(d2);
double f = d2.dot(r);

double c = d1.dot(r);
double b = d1.dot(d2);
double denom = a * e - b * b;

if (denom != 0.0) {
s = MathUtil.clamp((b * f - c * e) / denom, 0, 1);
} else {
s = 0.5; // give the midpoint for parallel lines
}
t = (b * s + f) / e;

if (t < 0) {
t = 0;
s = MathUtil.clamp(-c / a, 0, 1);
} else if (t > 0) {
t = 1;
s = MathUtil.clamp((b - c) / a, 0, 1);
}

Vector c1 = pp1.plus(d1.mult(s));
Vector c2 = pp2.plus(d2.mult(t));
Vector c1mc2 = c1.minus(c2);
return c1mc2.dot(c1mc2);
}
}
