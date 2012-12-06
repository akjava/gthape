package com.akjava.gwt.gthapetest.client.cardemo;

import com.akjava.gwt.gthape.client.ape.APEngine;
import com.akjava.gwt.gthape.client.ape.Vector;
import com.akjava.gwt.lib.client.LogUtils;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;

public class CarDemo {

	private Car car;
	private Rotator rotator;
	public void initialize(){
		/*
		int colA =		SDL_MapRGB(display.format, 51, 58, 51);
		int colB =		SDL_MapRGB(display.format, 51, 102, 170);
		int colC =		SDL_MapRGB(display.format, 170, 187, 187);
		int colD =		SDL_MapRGB(display.format, 102, 153, 170);
		int colE =		SDL_MapRGB(display.format, 119, 136, 119);
		int colPad	=	SDL_MapRGB(display.format, 153,102,51);
		 */
		
		APEngine.init((double)1/4);

		Vector massLessForces=new Vector (0, 3);
		APEngine.addMasslessForce(massLessForces);

		
		Surfaces surfaces=new Surfaces();
		APEngine.addGroup(surfaces);
		
		Bridge bridge=new Bridge();
		APEngine.addGroup(bridge);
		car = new Car();
		APEngine.addGroup(car);

		Capsule capsule=new Capsule();
		APEngine.addGroup(capsule);
		
		rotator = new Rotator();
		APEngine.addGroup(rotator);
		
		SwingDoor swingDoor=new SwingDoor();
		APEngine.addGroup(swingDoor);
		
		car.addCollidable(surfaces);
		car.addCollidable(bridge);
		car.addCollidable(capsule);
		car.addCollidable(rotator);
		car.addCollidable(swingDoor);
		
		
		capsule.addCollidable(surfaces);
		capsule.addCollidable(bridge);
		capsule.addCollidable(rotator);
		capsule.addCollidable(swingDoor);
		
	}
	public void step(){
		rotator.rotateByRadian(.02);
	}
	public void keyDown(KeyDownEvent event){
		double keySpeed=0.2;
		if(event.getNativeEvent().getKeyCode()==KeyCodes.KEY_RIGHT){
			car.speed(keySpeed);
		}else if(event.getNativeEvent().getKeyCode()==KeyCodes.KEY_LEFT){
			car.speed(-keySpeed);
		}
	}
	
	public void speed(double t){
		car.speed(t);
	}
	public void KeyUp(KeyUpEvent event) {
		
		car.speed(0);
	}
}
