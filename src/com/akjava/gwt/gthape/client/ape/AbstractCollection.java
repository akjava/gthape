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
- get sprite() is duplicated in AbstractItem. Should be in some parent class.
- checkCollisionsVsCollection and checkInternalCollisions methods use SpringConstraint.
it should be AbstractConstraint but the isConnectedTo method is in SpringConstraint.
- same deal with the paint() method here -- needs to test connected particles state
using SpringConstraint methods but should really be AbstractConstraint. need to clear up
what an AbstractConstraint really means.
- would an explicit cast be more efficient in the paint() method here?
*/

package com.akjava.gwt.gthape.client.ape;

import java.util.ArrayList;
import java.util.List;

import com.akjava.gwt.gthape.client.display.Sprite;




/**
* The abstract base class for all grouping classes.
*
* <p>
* You should not instantiate this class directly -- instead use one of the subclasses.
* </p>
*/
public abstract class AbstractCollection {


private Sprite sprite;
protected ArrayList<AbstractParticle> particles;
protected ArrayList<AbstractConstraint> constraints;
private boolean isParented;


public  AbstractCollection() {

isParented(false);
particles = new ArrayList<AbstractParticle>();
constraints = new ArrayList<AbstractConstraint>();
}


/**
* The Array of all AbstractParticle instances added to the AbstractCollection
*/
public ArrayList<AbstractParticle> particles(){
return particles;
}


/**
* The Array of all AbstractConstraint instances added to the AbstractCollection
*/
public ArrayList<AbstractConstraint> constraints(){
return constraints;
}


/**
* Adds an AbstractParticle to the AbstractCollection.
*
* @param p The particle to be added.
*/
public void addParticle(AbstractParticle p){
particles.add(p);
if (isParented()) {
	p.init();
}
}


/**
* Removes an AbstractParticle from the AbstractCollection.
*
* @param p The particle to be removed.
*/
public void removeParticle(AbstractParticle p){
int ppos = particles.indexOf(p);
if (ppos == -1) return;
particles.remove(ppos);
p.cleanup();
}


/**
* Adds a constraint to the Collection.
*
* @param c The constraint to be added.
*/
public void addConstraint(AbstractConstraint c){
constraints.add(c);
if (isParented){
	c.init();
}

}


/**
* Removes a constraint from the Collection.
*
* @param c The constraint to be removed.
*/
public void removeConstraint(AbstractConstraint c){
int cpos = constraints.indexOf(c);
if (cpos == -1) return;
constraints.remove(cpos);
c.cleanup();
}


/**
* Initializes every member of this AbstractCollection by in turn calling
* each members <code>init()</code> method.
*/
public void init(){

for (int i = 0; i < particles.size(); i++) {
particles.get(i).init();
}
for (int i = 0; i < constraints.size(); i++) {
constraints.get(i).init();
}
}


/**
* paints every member of this AbstractCollection by calling each members
* <code>paint()</code> method.
*/
public void paint(){

AbstractParticle p;
int len = particles.size();
for (int i = 0; i < len; i++) {
p = particles.get(i);
if ((! p.fixed()) || p.alwaysRepaint()) p.paint();
}

AbstractConstraint c;//strange
len = constraints.size();
for (int i = 0; i < len; i++) {
c = constraints.get(i);

//if ((! c.fixed()) || c.alwaysRepaint()) c.paint();//constraint have no fixed.//TODO research it again later
if (c.alwaysRepaint()) c.paint();
}
}


/**
* Calls the <code>cleanup()</code> method of every member of this AbstractCollection.
* The cleanup() method is called automatically when an AbstractCollection is removed
* from its parent.
*/
public void cleanup(){

for (int i = 0; i < particles.size(); i++) {
particles.get(i).cleanup();
}
for (int i = 0; i < constraints.size(); i++) {
constraints.get(i).cleanup();
}
}


/**
* Provides a Sprite to use as a container for drawing or adding children. When the
* sprite is requested for the first time it is automatically added to the global
* container in the APEngine class.
*/
/*
 * AbstractItem had it.
public Sprite sprite(){

if (_sprite != null) return _sprite;

if (APEngine.container() == null) {
throw new RuntimeException("The container property of the APEngine class has not been set");
}

_sprite = new Sprite();
APEngine.container.addChild(_sprite);
return _sprite;
}
*/

/**
* Returns an array of every particle and constraint added to the AbstractCollection.
*/
public ArrayList<Object> getAll(){//this must be slow TODO think more
	ArrayList<Object> items=new ArrayList<Object>();
	items.addAll(particles);
	items.addAll(constraints);
return items;
}


/**
* @private
*/
 boolean isParented(){
return isParented;
}


/**
* @private
*/
 void isParented(boolean b){
isParented = b;
}


/**
* @private
*/
 void integrate(double dt2){
int len = particles.size();
for (int i = 0; i < len; i++) {
AbstractParticle p = particles.get(i);
p.update(dt2);
}
}


/**
* @private
*/
 void satisfyConstraints(){
int len = constraints.size();
for (int i = 0; i < len; i++) {
AbstractConstraint c = constraints.get(i);
c.resolve();
}
}


/**
* @private
*/
 void checkInternalCollisions(){

// every particle in this AbstractCollection
int plen = particles.size();
for (int j=0;j<plen;j++) {

AbstractParticle pa = particles.get(j);
if (! pa.collidable()) continue;

// ...vs every other particle in this AbstractCollection
for (int i = j + 1; i < plen; i++) {
AbstractParticle pb = particles.get(i);
if (pb.collidable()) CollisionDetector.test(pa, pb);
}

// ...vs every other constraint in this AbstractCollection
int clen = constraints.size();
for (int n = 0; n < clen; n++) {
SpringConstraint c = (SpringConstraint) constraints.get(n);//TODO fix but now sprint is only constraint
if (c.collidable() && ! c.isConnectedTo(pa)) {
c.scp().updatePosition();
CollisionDetector.test(pa, c.scp());
}
}
}
}


/**
* @private
*/
 void checkCollisionsVsCollection(AbstractCollection ac){

// every particle in this collection...
int plen = particles.size();
for (int j = 0; j < plen; j++) {

AbstractParticle pga = particles.get(j);
if (! pga.collidable()) continue;

// ...vs every particle in the other collection
int acplen = ac.particles.size();
for (int x = 0; x < acplen; x++) {
AbstractParticle pgb = ac.particles.get(x);
if (pgb.collidable()) CollisionDetector.test(pga, pgb);
}
// ...vs every constraint in the other collection
int acclen = ac.constraints.size();
for (int x = 0; x < acclen; x++) {
SpringConstraint cgb = (SpringConstraint) ac.constraints().get(x);
if (cgb.collidable() && ! cgb.isConnectedTo(pga)) {
cgb.scp().updatePosition();
CollisionDetector.test(pga, cgb.scp());
}
}
}

// every constraint in this collection...
int clen = constraints.size();
for (int j = 0; j < clen; j++) {
SpringConstraint cga = (SpringConstraint) constraints.get(j);
if (! cga.collidable()) continue;

// ...vs every particle in the other collection
int acplen = ac.particles.size();
for (int n = 0; n < acplen; n++) {
AbstractParticle pgb = ac.particles.get(n);
if (pgb.collidable() && ! cga.isConnectedTo(pgb)) {
cga.scp().updatePosition();
CollisionDetector.test(pgb, cga.scp());
}
}
}
}
}
