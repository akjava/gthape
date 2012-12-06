package com.akjava.gwt.gthapetest.client.cardemo;

import com.akjava.gwt.gthape.client.ape.CircleParticle;
import com.akjava.gwt.gthape.client.ape.Composite;
import com.akjava.gwt.gthape.client.ape.SpringConstraint;
import com.akjava.gwt.gthape.client.ape.Vector;

public class RectComposite extends Composite{
private CircleParticle cpA;
public CircleParticle getCpA() {
	return cpA;
}

public CircleParticle getCpC() {
	return cpC;
}

private CircleParticle cpC;

public RectComposite(Vector ctr){
	double rw= 75;
	double rh= 18;
	double rad= 4;

	cpA = new CircleParticle(ctr.x-rw/2, ctr.y-rh/2, rad, true, 1, 0.3, 0);
	CircleParticle cpB = new CircleParticle(ctr.x+rw/2, ctr.y-rh/2, rad, true, 1, 0.3, 0);
	cpC = new CircleParticle(ctr.x+rw/2, ctr.y+rh/2, rad, true, 1, 0.3, 0);
	CircleParticle cpD = new CircleParticle(ctr.x-rw/2, ctr.y+rh/2, rad, true, 1, 0.3, 0);

	

	

	SpringConstraint	sprA = new SpringConstraint(cpA,cpB,0.5,true,rad * 2, 1, false);
	SpringConstraint sprB = new SpringConstraint(cpB,cpC,0.5,true,rad * 2, 1, false);
	SpringConstraint sprC = new SpringConstraint(cpC,cpD,0.5,true,rad * 2, 1, false);
	SpringConstraint sprD = new SpringConstraint(cpD,cpA,0.5,true,rad * 2, 1, false);
	
	
	// by default all fixed SpringConstraints are not repainted as well. A
	// SpringConstraint will be fixed if both its attached Particles are
	// fixed.
	

	addParticle(cpA);
	addParticle(cpB);
	addParticle(cpC);
	addParticle(cpD);
	
	addConstraint(sprA);
	addConstraint(sprB);
	addConstraint(sprC);
	addConstraint(sprD);
}
}
