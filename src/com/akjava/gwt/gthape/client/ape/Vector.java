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
- provide passible vectors for results. too much object creation happening here
- review the division by zero checks/corrections. why are they needed?
*/

package com.akjava.gwt.gthape.client.ape;

public class Vector {

public double x;
public double y;


//px=0,,py=0
public  Vector(){
	this(0,0);
}
public  Vector(double px,double py){
x = px;
y = py;
}


public void setTo(double px,double py){
x = px;
y = py;
}


public void copy(Vector v){
x = v.x;
y = v.y;
}


public double dot(Vector v){
return x * v.x + y * v.y;
}


public double cross(Vector v){
return x * v.y - y * v.x;
}


public Vector plus(Vector v){
return new Vector(x + v.x, y + v.y);
}


public Vector plusEquals(Vector v){
x += v.x;
y += v.y;
return this;
}


public Vector minus(Vector v){
return new Vector(x - v.x, y - v.y);
}


public Vector minusEquals(Vector v){
x -= v.x;
y -= v.y;
return this;
}


public Vector mult(double s){
return new Vector(x * s, y * s);
}


public Vector multEquals(double s){
x *= s;
y *= s;
return this;
}


public Vector times(Vector v){
return new Vector(x * v.x, y * v.y);
}


public Vector divEquals(double s){
if (s == 0) s = 0.0001;
x /= s;
y /= s;
return this;
}


public double magnitude(){
return Math.sqrt(x * x + y * y);
}


public double distance(Vector v){
Vector delta = this.minus(v);
return delta.magnitude();
}


public Vector normalize(){
double m = magnitude();
if (m == 0) m = 0.0001;
return mult(1 / m);
}


public String toString(){
return (x + " : " + y);
}
}
