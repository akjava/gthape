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

package com.akjava.gwt.gthape.client.ape;

 final class MathUtil {

protected static double ONE_EIGHTY_OVER_PI = 180 / Math.PI; ;
protected static double PI_OVER_ONE_EIGHTY = Math.PI / 180; ;

/**
* Returns n clamped between min and max
*/
 static double clamp(double n,double min,double max){
if (n < min) return min;
if (n > max) return max;
return n;
}


/**
* Returns 1 if the value is >= 0. Returns -1 if the value is < 0.
*/
 static int sign(double val){
if (val < 0) return -1;
return 1;
}
}
