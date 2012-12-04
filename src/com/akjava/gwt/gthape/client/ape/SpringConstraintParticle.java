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
also the functions respective to each, for better OOD- clean up resolveCollision with submethods*/package org.cove.ape {import flash.display.Sprite;import flash.display.DisplayObject;internal class SpringConstraintParticle extends RectangleParticle {private AbstractParticle;private p1 AbstractParticle;private p2 Vector;private avgVelocity Vector;private lambda SpringConstraint;private parent Boolean;private scaleToLength Vector;private rca Vector;private rcb Number;private s Number;private _rectScale Number;private _rectHeight Number;public _fixedEndLimit null SpringConstraintParticle(AbstractParticle p1,AbstractParticle p2,SpringConstraint p,double rectHeight,double rectScale,boolean scaleToLength){

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



internal void rectScale(double s){
_rectScale = s;
}


/**
* @private
*/
internal double rectScale(){
return _rectScale;
}


internal void rectHeight(double r){
_rectHeight = r;
}


/**
* @private
*/
internal double rectHeight(){
return _rectHeight;
}


/**
* For cases when the SpringConstraint is both collidable and only one of the
* two end particles are fixed, this value will dispose of collisions near the
* fixed particle, to correct for situations where the collision could never be
* resolved.
*/
internal void fixedEndLimit(double f){
_fixedEndLimit = f;
}


/**
* @private
*/
internal double fixedEndLimit(){
return _fixedEndLimit;
}


/**
* returns the average mass of the two connected particles
*/
public override double mass(){
return (p1.mass + p2.mass) / 2;
}


/**
* returns the average elasticity of the two connected particles
*/
public override double elasticity(){
return (p1.elasticity + p2.elasticity) / 2;
}


/**
* returns the average friction of the two connected particles
*/
public override double friction(){
return (p1.friction + p2.friction) / 2;
}


/**
* returns the average velocity of the two connected particles
*/
public override Vector velocity(){
Vector p1v =  p1.velocity;
Vector p2v =  p2.velocity;

avgVelocity.setTo(((p1v.x + p2v.x) / 2), ((p1v.y + p2v.y) / 2));
return avgVelocity;
}


public override void init(){
if (displayObject != null) {
initDisplay();
} else {
Sprite inner = new Sprite();
parent.sprite.addChild(inner);
inner.name = "inner";

Number w = parent.currLength * rectScale;
Number h = rectHeight;

inner.graphics.clear();
inner.graphics.lineStyle(parent.lineThickness, parent.lineColor, parent.lineAlpha);
inner.graphics.beginFill(parent.fillColor, parent.fillAlpha);
inner.graphics.drawRect(-w/2, -h/2, w, h);
inner.graphics.endFill();
}
paint();
}


public override void paint(){

Vector c = parent.center;
Sprite s = parent.sprite;

if (scaleToLength) {
s.getChildByName("inner").width = parent.currLength * rectScale;
} else if (displayObject != null) {
s.getChildByName("inner").width = parent.restLength * rectScale;
}
s.x = c.x;
s.y = c.y;
s.rotation = parent.angle;
}


/**
* @private
*/
internal override void initDisplay(){
displayObject.x = displayObjectOffset.x;
displayObject.y = displayObjectOffset.y;
displayObject.rotation = displayObjectRotation;

Sprite inner = new Sprite();
inner.name = "inner";

inner.addChild(displayObject);
parent.sprite.addChild(inner);
}


/**
* @private
* returns the average inverse mass.
*/
internal override double invMass(){
if (p1.fixed && p2.fixed) return 0;
return 1 / ((p1.mass + p2.mass) / 2);
}


/**
* called only on collision
*/
internal void updatePosition(){
Vector c = parent.center;
curr.setTo(c.x, c.y);

width = (scaleToLength) ? parent.currLength * rectScale : parent.restLength * rectScale;
height = rectHeight;
radian = parent.radian;
}


internal override void resolveCollision(Vector mtd,Vector vel,Vector n,double d,int o,AbstractParticle p){

Number t = getContactPointParam(p);
Number c1 = (1 - t);
Number c2 = t;

// if one is fixed then move the other particle the entire way out of collision.
// also, dispose of collisions at the sides of the scp. The higher the fixedEndLimit
// value, the more of the scp not be effected by collision.
if (p1.fixed) {
if (c2 <= fixedEndLimit) return;
lambda.setTo(mtd.x / c2, mtd.y / c2);
p2.curr.plusEquals(lambda);
p2.velocity = vel;

} else if (p2.fixed) {
if (c1 <= fixedEndLimit) return;
lambda.setTo(mtd.x / c1, mtd.y / c1);
p1.curr.plusEquals(lambda);
p1.velocity = vel;

// else both non fixed - move proportionally out of collision
} else {
Number denom = (c1 * c1 + c2 * c2);
if (denom == 0) return;
lambda.setTo(mtd.x / denom, mtd.y / denom);

p1.curr.plusEquals(lambda.mult(c1));
p2.curr.plusEquals(lambda.mult(c2));

// if collision is in the middle of SCP set the velocity of both end particles
if (t == 0.5) {
p1.velocity = vel;
p2.velocity = vel;

// otherwise change the velocity of the particle closest to contact
} else {
AbstractParticle corrParticle = (t < 0.5) ? p1 : p2;
corrParticle.velocity = vel;
}
}
}


/**
* given point c, returns a parameterized location on this SCP. Note
* this is just treating the SCP as if it were a line segment (ab).
*/
private double closestParamPoint(Vector c){
Vector ab = p2.curr.minus(p1.curr);
Number t = (ab.dot(c.minus(p1.curr))) / (ab.dot(ab));
return MathUtil.clamp(t, 0, 1);
}


/**
* returns a contact location on this SCP expressed as a parametric value in [0,1]
*/
private double getContactPointParam(AbstractParticle p){

Number t

if (p is CircleParticle)  {
t = closestParamPoint(p.curr);
} else if (p is RectangleParticle) {

// go through the sides of the colliding rectangle as line segments
Number shortestIndex
Array paramList = new Array(4);
Number shortestDistance = Number.POSITIVE_INFINITY;

for (int i = 0; i < 4; i++) {
setCorners(p as RectangleParticle, i);

// check for closest points on SCP to side of rectangle
Number d = closestPtSegmentSegment();
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

Number rx = r.curr.x;
Number ry = r.curr.y;

Array axes = r.axes;
Array extents = r.extents;

Number ae0_x = axes[0].x * extents[0];
Number ae0_y = axes[0].y * extents[0];
Number ae1_x = axes[1].x * extents[1];
Number ae1_y = axes[1].y * extents[1];

Number emx = ae0_x - ae1_x;
Number emy = ae0_y - ae1_y;
Number epx = ae0_x + ae1_x;
Number epy = ae0_y + ae1_y;


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

Number t
Number a = d1.dot(d1);
Number e = d2.dot(d2);
Number f = d2.dot(r);

Number c = d1.dot(r);
Number b = d1.dot(d2);
Number denom = a * e - b * b;

if (denom != 0.0) {
s = MathUtil.clamp((b * f - c * e) / denom, 0, 1);
} else {
s = 0.5 // give the midpoint for parallel lines
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
}