package com.akjava.gwt.gthapetest.client.cardemo;

import com.akjava.gwt.gthape.client.ape.CircleParticle;
import com.akjava.gwt.gthape.client.ape.Group;
import com.akjava.gwt.gthape.client.ape.RectangleParticle;
import com.akjava.gwt.gthape.client.ape.SpringConstraint;
import com.akjava.gwt.gthape.client.ape.Vector;

public class Rotator extends Group{

	private RectComposite rectComposite;
	private Vector ctr;

	public Rotator() {
		super(false);
		collideInternal ( true );
		
		ctr = new Vector (555,175);
		rectComposite = new RectComposite(ctr);
		addComposite(rectComposite);
		
		CircleParticle circA = new CircleParticle(ctr.x,ctr.y,5, false, 1, 0.3, 0);
		
		addParticle(circA);
		
		RectangleParticle rectA = new RectangleParticle(555,160,10,10,0,false,3, 0.3, 0);
		
		addParticle(rectA);
		
		SpringConstraint connectorA = new SpringConstraint(rectComposite.getCpA(), rectA, 1, false, 1, 1, false);
		
		addConstraint(connectorA);

		RectangleParticle rectB = new RectangleParticle(555,190,10,10,0,false,3, 0.3, 0);
		
		addParticle(rectB);
				
		SpringConstraint connectorB = new SpringConstraint(rectComposite.getCpC(), rectB, 1, false, 1, 1, false);
		
		addConstraint(connectorB);
	}
	
	void rotateByRadian(double a)
	{
		rectComposite.rotateByRadian(a, ctr);
	}

}
