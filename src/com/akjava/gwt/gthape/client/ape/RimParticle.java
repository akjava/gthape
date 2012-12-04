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
- use vector methods in update()
*/

package com.akjava.gwt.gthape.client.ape;

 class RimParticle {

 Vector curr;
 Vector prev;

private double wr;
private double av;
private double sp;
private double maxTorque;


/**
* The RimParticle is really just a second component of the wheel model.
* The rim particle is simulated in a coordsystem relative to the wheel's
* center, not in worldspace.
*
* Origins of this code are from Raigan Burns, Metanet Software
*/
public  RimParticle(double r,double mt){

curr = new Vector(r, 0);
prev = new Vector(0, 0);

sp = 0;
av = 0;

maxTorque = mt;
wr = r;
}

 double speed(){
return sp;
}

 void speed(double s){
sp = s;
}

 double angularVelocity(){
return av;
}

 void angularVelocity(double s){
av = s;
}

/**
* Origins of this code are from Raigan Burns, Metanet Software
*/
 void update(double dt){

//clamp torques to valid range
sp = Math.max(-maxTorque, Math.min(maxTorque, sp + av));

//apply torque
//this is the tangent vector at the rim particle
double dx = -curr.y;
double dy =  curr.x;

//normalize so we can scale by the rotational speed
double len = Math.sqrt(dx * dx + dy * dy);
dx /= len;
dy /= len;

curr.x += sp * dx;
curr.y += sp * dy;

double ox = prev.x;
double oy = prev.y;
double px = prev.x = curr.x;
double py = prev.y = curr.y;

curr.x += APEngine.damping() * (px - ox);
curr.y += APEngine.damping() * (py - oy);

// hold the rim particle in place
double clen = Math.sqrt(curr.x * curr.x + curr.y * curr.y);
double diff = (clen - wr) / clen;

curr.x -= curr.x * diff;
curr.y -= curr.y * diff;
}
}
