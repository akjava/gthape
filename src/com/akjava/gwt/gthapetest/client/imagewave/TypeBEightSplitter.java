package com.akjava.gwt.gthapetest.client.imagewave;

import java.util.ArrayList;

import com.akjava.gwt.gthape.client.ape.APEngine;
import com.akjava.gwt.gthape.client.ape.CircleParticle;
import com.akjava.gwt.gthape.client.ape.Group;
import com.akjava.gwt.gthape.client.ape.SpringConstraint;

public class TypeBEightSplitter extends AbstractEightSplitter {
	
	
	private Group group;
	public TypeBEightSplitter(double  springValue,int sx,int sy,int dw,int dh){
		super(springValue,sx,sy,dw,dh);
		// the argument here is the deltaTime value. Higher values result in faster simulations.
		APEngine.init((double)1/4);
		
		group=new Group(true);
		APEngine.addGroup(group);
		
		
		//bridge
		// bridge
		
		ArrayList<CircleParticle> particles=new ArrayList<CircleParticle>();
		
		CircleParticle left1 = new CircleParticle(sx,sy+dh,4,true,1,0.3,0);
		group.addParticle(left1);
		
		
		CircleParticle particle=new CircleParticle(sx+dw/8,sy+dh/4,4,false,1,0.3,0);
		group.addParticle(particle);
		particles.add(particle);
		
		left2 = new CircleParticle(sx+dw/4,sy+dh/2,4,false,1,0.3,0);
		group.addParticle(left2);
		
		
		particle=new CircleParticle(sx+dw/4+dw/8,sy+dh/8,4,false,1,0.3,0);
		group.addParticle(particle);
		particles.add(particle);
		
		
		
		
		
		CircleParticle right1 = new CircleParticle(sx+dw,sy+dh,4,true,1,0.3,0);
		group.addParticle(right1);
		
		
		particle=new CircleParticle(sx+dw/2+dw/8,sy+dh/8,4,false,1,0.3,0);
		group.addParticle(particle);
		particles.add(particle);
		
		
		right2 = new CircleParticle(sx+dw/2+dw/4,sy+dh/2,4,false,1,0.3,0);
		group.addParticle(right2);
		
		particle=new CircleParticle(sx+dw/8+dw/2+dw/4,sy+dh/4,4,false,1,0.3,0);
		group.addParticle(particle);
		particles.add(particle);
		
		
		center = new CircleParticle(sx+dw/2,sy,4,false,1,0.3,0);
		group.addParticle(center);
		
		
		trueCenter=new CircleParticle(sx+dw/2,sy+dh/2,4,false,1,0.3,0);
		group.addParticle(trueCenter);
		particles.add(trueCenter);
		
		particles.add(center);
		particles.add(left1);
		particles.add(left2);
		particles.add(right1);
		particles.add(right2);
		
		
		
		
	
		
		for (int i = 0; i < particles.size(); i++) {
			for (int j = i+1; j < particles.size(); j++) {
				SpringConstraint spring = new SpringConstraint(particles.get(i), particles.get(j), springValue);
				spring.setCollidable(false,1,0.6,false);
				group.addConstraint(spring);
			}
		}
		
		
	}

	
}
