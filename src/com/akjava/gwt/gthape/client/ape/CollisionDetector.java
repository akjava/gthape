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
- Get rid of all the object testing and use the double dispatch pattern
- There's some physical differences in collision response for multisampled
particles, probably due to prev/curr differences.
*/
package com.akjava.gwt.gthape.client.ape;


 final class CollisionDetector {


/**
* Tests the collision between two objects. If there is a collision it is passed off
* to the CollisionResolver class.
*/
 static void test(AbstractParticle objA,AbstractParticle objB){
	
if (objA.fixed() && objB.fixed()) return;

if (objA.multisample() == 0 && objB.multisample() == 0) {
normVsNorm(objA, objB);

} else if (objA.multisample() > 0 && objB.multisample() == 0) {
sampVsNorm(objA, objB);

} else if (objB.multisample() > 0 && objA.multisample() == 0) {
sampVsNorm(objB, objA);

} else if (objA.multisample() == objB.multisample()) {
sampVsSamp(objA, objB);

} else {
normVsNorm(objA, objB);
}
}


/**
* default test for two non-multisampled particles
*/
private static void normVsNorm(AbstractParticle objA,AbstractParticle objB){
objA.samp.copy(objA.curr);
objB.samp.copy(objB.curr);
testTypes(objA, objB);
}


/**
* Tests two particles where one is multisampled and the other is not. Let objectA
* be the multisampled particle.
*/
private static void sampVsNorm(AbstractParticle objA,AbstractParticle objB){

double s = 1 / (objA.multisample() + 1);
double t = s;

objB.samp.copy(objB.curr);

for (int i = 0; i <= objA.multisample(); i++) {
objA.samp.setTo(objA.prev.x + t * (objA.curr.x - objA.prev.x),
objA.prev.y + t * (objA.curr.y - objA.prev.y));

if (testTypes(objA, objB)) return;
t += s;
}
}


/**
* Tests two particles where both are of equal multisample rate
*/
private static void sampVsSamp(AbstractParticle objA,AbstractParticle objB){

double s = 1 / (objA.multisample() + 1);
double t = s;

for (int i = 0; i <= objA.multisample(); i++) {

objA.samp.setTo(objA.prev.x + t * (objA.curr.x - objA.prev.x),
objA.prev.y + t * (objA.curr.y - objA.prev.y));

objB.samp.setTo(objB.prev.x + t * (objB.curr.x - objB.prev.x),
objB.prev.y + t * (objB.curr.y - objB.prev.y));

if (testTypes(objA, objB)) return;
t += s;
}
}


/**
*
*/
private static boolean testTypes(AbstractParticle objA,AbstractParticle objB){

if (objA instanceof RectangleParticle && objB instanceof RectangleParticle) {
return testOBBvsOBB((RectangleParticle)objA, (RectangleParticle)objB);

} else if (objA instanceof CircleParticle && objB instanceof CircleParticle) {
return testCirclevsCircle((CircleParticle)objA, (CircleParticle)objB);

} else if (objA instanceof RectangleParticle && objB instanceof CircleParticle) {
return testOBBvsCircle((RectangleParticle)objA, (CircleParticle)objB);

} else if (objA instanceof CircleParticle && objB instanceof RectangleParticle)  {
return testOBBvsCircle((RectangleParticle)objB, (CircleParticle)objA);
}

return false;
}


/**
* Tests the collision between two RectangleParticles (aka OBBs). If there is a collision it
* determines its axis and depth, and then passes it off to the CollisionResolver for handling.
*/
private static boolean testOBBvsOBB(RectangleParticle ra,RectangleParticle rb){

Vector collisionNormal=new Vector();
double collisionDepth = Double.POSITIVE_INFINITY;

for (int i = 0; i < 2; i++) {

Vector axisA = ra.axes[i];
double depthA = testIntervals(ra.getProjection(axisA), rb.getProjection(axisA));
if (depthA == 0) return false;

Vector axisB = rb.axes[i];
double depthB = testIntervals(ra.getProjection(axisB), rb.getProjection(axisB));
if (depthB == 0) return false;

double absA = Math.abs(depthA);
double absB = Math.abs(depthB);

if (absA < Math.abs(collisionDepth) || absB < Math.abs(collisionDepth)) {
Boolean altb = absA < absB;
collisionNormal = altb ? axisA : axisB;
collisionDepth = altb ? depthA : depthB;
}
}
CollisionResolver.resolveParticleParticle(ra, rb, collisionNormal, collisionDepth);
return true;
}


/**
* Tests the collision between a RectangleParticle (aka an OBB) and a CircleParticle.
* If there is a collision it determines its axis and depth, and then passes it off
* to the CollisionResolver.
*/
private static boolean testOBBvsCircle(RectangleParticle ra,CircleParticle ca){

Vector collisionNormal=new Vector();
double collisionDepth = Double.POSITIVE_INFINITY;
double[] depths = new double[2];

// first go through the axes of the rectangle
for (int i = 0; i < 2; i++) {

Vector boxAxis = ra.axes[i];
double depth = testIntervals(ra.getProjection(boxAxis), ca.getProjection(boxAxis));
if (depth == 0) return false;

if (Math.abs(depth) < Math.abs(collisionDepth)) {
collisionNormal = boxAxis;
collisionDepth = depth;
}
depths[i] = depth;
}

// determine if the circle's center is in a vertex region
double r = ca.radius();
if (Math.abs(depths[0]) < r && Math.abs(depths[1]) < r) {

Vector vertex = closestVertexOnOBB(ca.samp, ra);

// get the distance from the closest vertex on rect to circle center
collisionNormal = vertex.minus(ca.samp);
double mag = collisionNormal.magnitude();
collisionDepth = r - mag;

if (collisionDepth > 0) {
// there is a collision in one of the vertex regions
collisionNormal.divEquals(mag);
} else {
// ra is in vertex region, but is not colliding
return false;
}
}
CollisionResolver.resolveParticleParticle(ra, ca, collisionNormal, collisionDepth);
return true;
}


/**
* Tests the collision between two CircleParticles. If there is a collision it
* determines its axis and depth, and then passes it off to the CollisionResolver
* for handling.
*/
private static boolean testCirclevsCircle(CircleParticle ca,CircleParticle cb){

double depthX = testIntervals(ca.getIntervalX(), cb.getIntervalX());
if (depthX == 0) return false;

double depthY = testIntervals(ca.getIntervalY(), cb.getIntervalY());
if (depthY == 0) return false;

Vector collisionNormal = ca.samp.minus(cb.samp);
double mag = collisionNormal.magnitude();
double collisionDepth = (ca.radius() + cb.radius()) - mag;

if (collisionDepth > 0) {
collisionNormal.divEquals(mag);
CollisionResolver.resolveParticleParticle(ca, cb, collisionNormal, collisionDepth);
return true;
}
return false;
}


/**
* Returns 0 if intervals do not overlap. Returns smallest depth if they do.
*/
private static double testIntervals(Interval intervalA,Interval intervalB){

if (intervalA.max < intervalB.min) return 0;
if (intervalB.max < intervalA.min) return 0;

double lenA = intervalB.max - intervalA.min;
double lenB = intervalB.min - intervalA.max;

return (Math.abs(lenA) < Math.abs(lenB)) ? lenA : lenB;
}


/**
* Returns the location of the closest vertex on r to point p
*/
private static Vector closestVertexOnOBB(Vector p,RectangleParticle r){

Vector d = p.minus(r.samp);
Vector q = new Vector(r.samp.x, r.samp.y);

for (int i = 0; i < 2; i++) {
	double dist = d.dot(r.axes[i]);

if (dist >= 0) dist = r.extents[i];
else if (dist < 0) dist = -r.extents[i];

q.plusEquals(r.axes[i].mult(dist));
}
return q;
}
}
