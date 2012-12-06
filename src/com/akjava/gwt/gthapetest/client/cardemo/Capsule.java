package com.akjava.gwt.gthapetest.client.cardemo;

import com.akjava.gwt.gthape.client.ape.CircleParticle;
import com.akjava.gwt.gthape.client.ape.Group;
import com.akjava.gwt.gthape.client.ape.SpringConstraint;

public class Capsule extends Group{

	public Capsule() {
		super(false);
		
		CircleParticle capsuleP1 = new CircleParticle(300,10,14,false,1.3,0.4,0);
		
		addParticle(capsuleP1);
		
		CircleParticle capsuleP2 = new CircleParticle(325,35,14,false,1.3,0.4,0);
		
		addParticle(capsuleP2);
		
		SpringConstraint capsule = new SpringConstraint(capsuleP1, capsuleP2, 1, true, 24, 1, false);
		
		addConstraint(capsule);
	}

}
