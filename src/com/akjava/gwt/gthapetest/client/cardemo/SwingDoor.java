package com.akjava.gwt.gthapetest.client.cardemo;

import com.akjava.gwt.gthape.client.ape.CircleParticle;
import com.akjava.gwt.gthape.client.ape.Group;
import com.akjava.gwt.gthape.client.ape.RectangleParticle;
import com.akjava.gwt.gthape.client.ape.SpringConstraint;

public class SwingDoor extends Group{
public SwingDoor(){
	super(false);
	
collideInternal ( true );
	
CircleParticle swingDoorP1 = new CircleParticle( 543,55,7, false, 1, 0.3, 0);
	swingDoorP1.mass ( 0.001 );

	addParticle(swingDoorP1);
	
	CircleParticle swingDoorP2 = new CircleParticle( 620,55,7,true, 1, 0.3, 0);

	addParticle(swingDoorP2);
	
	SpringConstraint swingDoor = new SpringConstraint( swingDoorP1, swingDoorP2, 1, true, 13, 1, false);
	
	addConstraint(swingDoor);
	
	CircleParticle 	swingDoorAnchor = new CircleParticle( 543,5,2,true, 1, 0.3, 0);
	swingDoorAnchor.visible ( false );
	swingDoorAnchor.collidable ( false );
	addParticle(swingDoorAnchor);
	
	SpringConstraint swingDoorSpring = new SpringConstraint( swingDoorP1, swingDoorAnchor, 0.02, false, 1, 1, false);
	swingDoorSpring.restLength ( 40 );
	swingDoorSpring.visible ( false );
	addConstraint(swingDoorSpring);
	
	CircleParticle stopperA = new CircleParticle( 550,-60,70,true, 1, 0.3, 0);
	stopperA.visible ( false );
	addParticle(stopperA);

	RectangleParticle stopperB = new RectangleParticle( 650,130,42,70,0,true, 1, 0.3, 0);
	stopperB.visible ( false );
	addParticle(stopperB);
}
}
