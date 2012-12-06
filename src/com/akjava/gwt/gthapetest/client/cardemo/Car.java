package com.akjava.gwt.gthapetest.client.cardemo;

import com.akjava.gwt.gthape.client.ape.Group;
import com.akjava.gwt.gthape.client.ape.SpringConstraint;
import com.akjava.gwt.gthape.client.ape.WheelParticle;

public class Car extends Group{

	private WheelParticle wheelParticleA;
	private WheelParticle wheelParticleB;

	public Car() {
		super(false);
		int offset=-0;
		wheelParticleA = new WheelParticle(140+offset,10,14,false,2, 0.3, 0, 1);
		addParticle(wheelParticleA);
		
		wheelParticleB = new WheelParticle(200+offset,10,14,false,2, 0.3, 0, 1);
		addParticle(wheelParticleB);
		
		SpringConstraint wheelConnector = new SpringConstraint(wheelParticleA, wheelParticleB,
				0.5, true, 8, 1, false);
		
		addConstraint(wheelConnector);
	}
	
	public void speed(double sp){
		wheelParticleA.angularVelocity ( sp );
		wheelParticleB.angularVelocity ( sp );
	}

}
