package com.akjava.gwt.gthapetest.client.imagewave;

import java.util.ArrayList;

import com.akjava.gwt.gthape.client.ape.APEngine;
import com.akjava.gwt.gthape.client.ape.AbstractParticle;
import com.akjava.gwt.gthape.client.ape.CircleParticle;
import com.akjava.gwt.gthape.client.ape.Group;
import com.akjava.gwt.gthape.client.ape.SpringConstraint;
import com.akjava.gwt.gthape.client.ape.Vector;


public class TypeA3EightSplitter extends AbstractEightSplitter {
	
	
	public void addForce(double x,double y){
		trueCenter.addForce(new Vector(x,y));
	}
	
	private Group group;
	public TypeA3EightSplitter(double  springValue,int sx,int sy,int dw,int dh){
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
		
		
		CircleParticle topLeft = new CircleParticle(sx,sy,4,true,1,0.3,0);
		group.addParticle(topLeft);
		
		CircleParticle topRight = new CircleParticle(sx+dw,sy,4,true,1,0.3,0);
		group.addParticle(topRight);
	
		
		for (int i = 0; i < particles.size(); i++) {
			if(particles.get(i)!=trueCenter){
				SpringConstraint spring = new SpringConstraint(particles.get(i), trueCenter, springValue);
				spring.setCollidable(false,1,0.6,false);
				group.addConstraint(spring);
			}
			
		}
		
		addSpring(left1,right1,springValue);
		addSpring(left1,left2,springValue);
		addSpring(left2,center,springValue);
		addSpring(right2,center,springValue);
		addSpring(right1,right2,springValue);
		
		
		addSpring(topLeft,topRight,springValue);
		addSpring(topLeft,left1,springValue);
		addSpring(right1,topRight,springValue);
		
		addSpring(topLeft,left2,springValue);
		addSpring(right2,topRight,springValue);
		
		addSpring(topLeft,center,springValue);
		addSpring(center,topRight,springValue);
		addSpring(topLeft,trueCenter,springValue);
		addSpring(trueCenter,topRight,springValue);
		/*
		After adding all the particles and constraints, you can retrieve them using the 
		getXXX methods from the APEngine class. Then you can go through them and paint them
		when necessary. Alternatively you can keep track of them yourself by manually adding
		them to your own lists.
		*/
		
		//log.info("qsizei:"+paintQueue.size());
	}
	
	private void addSpring(AbstractParticle particle1,AbstractParticle particle2,double springValue){
		SpringConstraint spring = new SpringConstraint(particle1, particle2, springValue);
		
		spring.setCollidable(false,1,0.6,false);
		group.addConstraint(spring);
	}

	
}


