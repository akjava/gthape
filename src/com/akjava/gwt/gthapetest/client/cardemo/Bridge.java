package com.akjava.gwt.gthapetest.client.cardemo;

import com.akjava.gwt.gthape.client.ape.CircleParticle;
import com.akjava.gwt.gthape.client.ape.Group;
import com.akjava.gwt.gthape.client.ape.SpringConstraint;

public class Bridge extends Group {
public Bridge(){
	super(false);
	double bx=170;
	double by=40;
	double bsize=51.5;
	double yslope=2.4;
	double particleSize=4;

	CircleParticle bridgePAA = new CircleParticle(bx,by,particleSize,true,1,0.3,0);

	addParticle(bridgePAA);

	bx += bsize;
	by += yslope;
	CircleParticle 	bridgePA = new CircleParticle(bx,by,particleSize,false,1,0.3,0);
	
	addParticle(bridgePA);

	bx += bsize;
	by += yslope;
	CircleParticle bridgePB = new CircleParticle(bx,by,particleSize,false,1,0.3,0);

	addParticle(bridgePB);

	bx += bsize;
	by += yslope;
	CircleParticle bridgePC = new CircleParticle( bx,by,particleSize,false,1,0.3,0);

	addParticle(bridgePC);

	bx += bsize;
	by += yslope;
	CircleParticle bridgePD = new CircleParticle(bx,by,particleSize,false,1,0.3,0);
	
	addParticle(bridgePD);

	bx += bsize;
	by += yslope;
	CircleParticle bridgePDD = new CircleParticle(bx,by,particleSize,true,1,0.3,0);

	addParticle(bridgePDD);


	SpringConstraint bridgeConnA = new SpringConstraint(bridgePAA, bridgePA,
			0.9, true, 10, 0.8, false);

	// collision response on the bridgeConnA will be ignored on
	// on the first 1/4 of the constraint. this avoids blow ups
	// particular to springcontraints that have 1 fixed particle.
	bridgeConnA.fixedEndLimit(0.25);

	addConstraint(bridgeConnA);

	SpringConstraint bridgeConnB = new SpringConstraint(bridgePA, bridgePB,
			0.9, true, 10, 0.8, false);

	addConstraint(bridgeConnB);

	SpringConstraint bridgeConnC = new SpringConstraint(bridgePB, bridgePC,
			0.9, true, 10, 0.8, false);

	addConstraint(bridgeConnC);

	SpringConstraint bridgeConnD = new SpringConstraint(bridgePC, bridgePD,
			0.9, true, 10, 0.8, false);

	addConstraint(bridgeConnD);

	SpringConstraint bridgeConnE = new SpringConstraint(bridgePD, bridgePDD,
			0.9, true, 10, 0.8, false);
	bridgeConnE.fixedEndLimit(0.25);

	addConstraint(bridgeConnE);
}
}
