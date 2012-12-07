package com.akjava.gwt.gthapetest.client.cardemo;

import com.akjava.gwt.gthape.client.ape.CircleParticle;
import com.akjava.gwt.gthape.client.ape.Group;
import com.akjava.gwt.gthape.client.ape.RectangleParticle;
import com.akjava.gwt.gthapetest.client.GTHApeTest;

public class Surfaces extends Group{
public Surfaces(){
	super(false);
	RectangleParticle floor=new RectangleParticle(340,327,550,50,0,true, 1.0, 0.3, 0);
	
	addParticle(floor);
	
	RectangleParticle ceil=new RectangleParticle(325,-33,649,80,0,true, 1.0, 0.3, 0);
	
	addParticle(ceil);

	RectangleParticle rampRight=new RectangleParticle(375,220,390,20,0.405,true, 1.0, 0.3, 0);
	
	addParticle(rampRight);
	
	RectangleParticle rampLeft=new RectangleParticle(90,200,102,20,-.7,true, 1.0, 0.3, 0);
	
	addParticle(rampLeft);
	
	RectangleParticle rampLeft2=new RectangleParticle(96,129,102,20,-.7,true, 1.0, 0.3, 0);

	addParticle(rampLeft2);
	
	CircleParticle rampCircle = new CircleParticle(175,190,60,true, 1.0, 0.3, 0);
	
	addParticle(rampCircle);
	
	CircleParticle floorBump = new CircleParticle(600,660,400,true, 1.0, 0.3, 0);

	addParticle(floorBump);
	
	RectangleParticle bouncePad=new RectangleParticle(30,370,32,60,0,true, 1.0, 0.3, 0);
	
	bouncePad.elasticity(4);
	GTHApeTest.colorMap.put(bouncePad, 0xff0000);
	addParticle(bouncePad);
	
	RectangleParticle leftWall=new RectangleParticle(1,99,30,500,0,true, 1.0, 0.3, 0);
	
	addParticle(leftWall);
	
	RectangleParticle leftWallChannelInner=new RectangleParticle(54,300,20,150,0,true, 1.0, 0.3, 0);
	
	addParticle(leftWallChannelInner);
	
	RectangleParticle leftWallChannel=new RectangleParticle(54,122,20,94,0,true, 1.0, 0.3, 0);
	
	addParticle(leftWallChannel);
	
	RectangleParticle leftWallChannelAng=new RectangleParticle(75,65,60,25,- 0.7,true, 1.0, 0.3, 0);
	
	addParticle(leftWallChannelAng);
	
	RectangleParticle topLeftAng=new RectangleParticle(23,11,65,40,-0.7,true, 1.0, 0.3, 0);
	
	addParticle(topLeftAng);
	
	RectangleParticle rightWall=new RectangleParticle(654,230,50,500,0,true, 1.0, 0.3, 0);
	
	addParticle(rightWall);

	RectangleParticle bridgeStart=new RectangleParticle(127,49,75,25,0,true, 1.0, 0.3, 0);
	
	addParticle(bridgeStart);
	
	RectangleParticle bridgeEnd=new RectangleParticle(483,55,100,15,0,true, 1.0, 0.3, 0);
	
	addParticle(bridgeEnd);
}
}
