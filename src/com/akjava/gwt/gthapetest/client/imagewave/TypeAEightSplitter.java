package com.akjava.gwt.gthapetest.client.imagewave;

import java.util.ArrayList;

import com.akjava.gwt.gthape.client.ape.APEngine;
import com.akjava.gwt.gthape.client.ape.CircleParticle;
import com.akjava.gwt.gthape.client.ape.Group;
import com.akjava.gwt.gthape.client.ape.SpringConstraint;
import com.akjava.gwt.gthape.client.ape.Vector;

public class TypeAEightSplitter extends AbstractEightSplitter {
	
	
	public void addForce(double x,double y){
		trueCenter.addForce(new Vector(x,y));
	}
	private Group group;
	public TypeAEightSplitter(double  springValue,int sx,int sy,int dw,int dh){
		super(springValue,sx,sy,dw,dh);
		// the argument here is the deltaTime value. Higher values result in faster simulations.
		APEngine.init((double)1/3);//特別な初期�?
		
		group=new Group(true);
		APEngine.addGroup(group);
		//重力な�?
		//APEngine.addMasslessForce(new Vector(0,3));
		

		
		
		
		
		//bridge
		// bridge
		
		ArrayList<CircleParticle> particles=new ArrayList<CircleParticle>();
		
		CircleParticle left1 = new CircleParticle(sx,sy+dh,4,true,1,0.3,0);
		group.addParticle(left1);
		
		CircleParticle particle;
		
		
		left2 = new CircleParticle(sx+dw/4,sy+dh/2,4,false,1,0.3,0);
		group.addParticle(left2);
		
		
		
		
		
		
		
		CircleParticle right1 = new CircleParticle(sx+dw,sy+dh,4,true,1,0.3,0);
		group.addParticle(right1);
		
		
		
		
		right2 = new CircleParticle(sx+dw/2+dw/4,sy+dh/2,4,false,1,0.3,0);
		group.addParticle(right2);
		
		
		
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
