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
- should all getters for composites, particles, constraints arrays return
a copy of the array? do we want to give the user direct access to it?
- addConstraintList, addParticleList
- if get particles and get constraints returned members of the Groups composites
(as they probably should, the checkCollision... methods would probably be much
cleaner.
*/
package com.akjava.gwt.gthape.client.ape;

import java.util.ArrayList;


/**
* The Group class can contain Particles, Constraints, and Composites. Groups
* can be assigned to be checked for collision with other Groups or internally.
*/
public class Group extends AbstractCollection {

private ArrayList<Composite> composites;
private ArrayList<Group> collisionList;
private boolean collideInternal;


/**
* The Group class is the main organizational class for APE. Once groups are created and populated
* with particles, constraints, and composites, they are added to the APEngine. Groups may contain
* particles, constraints, and composites. Composites may only contain particles and constraints.
*/
//collideInternal=false
public  Group(boolean collideInternal){
composites = new ArrayList<Composite>();
collisionList = new ArrayList<Group>();
this.collideInternal = collideInternal;
}


/**
* Initializes every member of this Group by in turn calling
* each members <code>init()</code> method.
*/
public  void init(){
super.init();
for (int i = 0; i < composites.size(); i++) {
composites.get(i).init();
}
}


/**
* Returns an Array containing all the Composites added to this Group
*/
public ArrayList<Composite> composites(){
return composites;
}


/**
* Adds a Composite to the Group.
*
* @param c The Composite to be added.
*/
public void addComposite(Composite c){
composites.add(c);
c.isParented(true);
if (isParented()) c.init();
}


/**
* Removes a Composite from the Group.
*
* @param c The Composite to be removed.
*/
public void removeComposite(Composite c){
int cpos = composites.indexOf(c);
if (cpos == -1) return;
composites.remove(cpos);
c.isParented(false);
c.cleanup();
}


/**
* Paints all members of this Group. This method is called automatically
* by the APEngine class.
*/
public  void paint(){

super.paint();

int len = composites.size();
for (int i = 0; i < len; i++) {
Composite c = composites.get(i);
c.paint();
}
}


/**
* Adds an Group instance to be checked for collision against
* this one.
*/
public void addCollidable(Group g){
collisionList.add(g);
}


/**
* Removes a Group from the collidable list of this Group.
*/
public void removeCollidable(Group g){
int pos = collisionList.indexOf(g);
if (pos == -1) return;
collisionList.remove(pos);
}


/**
* Adds an array of AbstractCollection instances to be checked for collision
* against this one.
*/
public void addCollidableList(ArrayList<Group> list){
for (int i = 0; i < list.size(); i++) {
Group g = list.get(i);
collisionList.add(g);
}
}


/**
* Returns the array of every Group assigned to collide with
* this Group instance.
*/
public ArrayList<Group> collisionList(){
return collisionList;
}


/**
* Returns an array of every particle, constraint, and composite added to the Group.
*/
public  ArrayList<Object> getAll(){
	ArrayList<Object> all=super.getAll();
	all.addAll(composites);
return all;
}


/**
* Determines if the members of this Group are checked for
* collision with one another.
*/
public boolean collideInternal(){
return collideInternal;
}


/**
* @private
*/
public void collideInternal(boolean b){
collideInternal = b;
}


/**
* Calls the <code>cleanup()</code> method of every member of this Group.
* The cleanup() method is called automatically when an Group is removed
* from the APEngine.
*/
public  void cleanup(){
super.cleanup();
for (int i = 0; i < composites.size(); i++) {
composites.get(i).cleanup();
}
}


/**
* @private
*/
void integrate(double dt2){

super.integrate(dt2);

int len = composites.size();
for (int i = 0; i < len; i++) {
Composite cmp = composites.get(i);
cmp.integrate(dt2);
}
}


/**
* @private
*/
void satisfyConstraints(){

super.satisfyConstraints();

int len = composites.size();
for (int i = 0; i < len; i++) {
Composite cmp = composites.get(i);
cmp.satisfyConstraints();
}
}


/**
* @private
*/
void checkCollisions(){

if (collideInternal) checkCollisionGroupInternal();

int len = collisionList.size();
for (int i = 0; i < len; i++) {
Group g = collisionList.get(i);
checkCollisionVsGroup(g);
}
}


private void checkCollisionGroupInternal(){

// check collisions not in composites
checkInternalCollisions();

// for every composite in this Group..
int clen = composites.size();
for (int j = 0; j < clen; j++) {

Composite ca = composites.get(j);

// .. vs non composite particles and constraints in this group
ca.checkCollisionsVsCollection(this);

// ...vs every other composite in this Group
for (int i = j + 1; i < clen; i++) {
Composite cb = composites.get(i);
ca.checkCollisionsVsCollection(cb);
}
}
}


private void checkCollisionVsGroup(Group g){

// check particles and constraints not in composites of either group
checkCollisionsVsCollection(g);

int clen = composites.size();
int gclen = g.composites().size();

// for every composite in this group..
for (int i = 0; i < clen; i++) {

// check vs the particles and constraints of g
Composite c = composites.get(i);
c.checkCollisionsVsCollection(g);

// check vs composites of g
for (int j = 0; j < gclen; j++) {
Composite gc = g.composites().get(j);
c.checkCollisionsVsCollection(gc);
}
}

// check particles and constraints of this group vs the composites of g
for (int j = 0; j < gclen; j++) {
Composite gc = g.composites().get(j);
checkCollisionsVsCollection(gc);
}
}
}
