package com.akjava.gwt.gthapetest.client;



import com.akjava.gwt.gthape.client.ape.APEngine;
import com.akjava.gwt.gthape.client.ape.AbstractParticle;
import com.akjava.gwt.gthape.client.ape.CircleParticle;
import com.akjava.gwt.gthape.client.ape.Group;
import com.akjava.gwt.gthape.client.ape.RectangleParticle;
import com.akjava.gwt.gthape.client.ape.SpringConstraint;
import com.akjava.gwt.gthape.client.ape.Vector;
import com.akjava.gwt.gthape.client.ape.WheelParticle;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GTHApeTest implements EntryPoint {

	private Canvas canvas;
	
	

	private Group defaultGroup;

	private CircleParticle circle;
	@Override
	public void onModuleLoad() {
		VerticalPanel root=new VerticalPanel();
		RootPanel.get().add(root);
		
		APEngine.container(new ArrayListDisplayObjectContainer());
		APEngine.init(1.0);
		
		APEngine.addMasslessForce(new Vector(0,2));
		defaultGroup = new Group(true);
				
		circle = new CircleParticle(250,10,5);
		defaultGroup.addParticle(circle);         	
		WheelParticle wp = new WheelParticle(280,10,5,false,1,0.3,0.1,1);
		defaultGroup.addParticle(wp);
		RectangleParticle rp = new RectangleParticle(250,300,200,10,-0.52,true);
		defaultGroup.addParticle(rp);      
		RectangleParticle rp2 = new RectangleParticle(150,200,200,10,0.52,true);
		defaultGroup.addParticle(rp2);        
		RectangleParticle rp3 = new RectangleParticle(250,50,200,10,-0.52,true);
		defaultGroup.addParticle(rp3);
		WheelParticle wa  = new WheelParticle(160,20,10,false,2);
		defaultGroup.addParticle(wa);		
		WheelParticle wb = new WheelParticle(200,20,10,false,2);
		defaultGroup.addParticle(wb);		
		SpringConstraint wc  = new SpringConstraint(wa, wb, 0.5, true, 3);
		defaultGroup.addConstraint(wc); 
		
		APEngine.addGroup(defaultGroup);
		
		//APEngine.step();
		
		canvas = Canvas.createIfSupported();
		canvas.setSize("600px", "600px");
		canvas.setCoordinateSpaceWidth(600);
		canvas.setCoordinateSpaceHeight(600);
		
		root.add(canvas);
		
		/*
		Timer timer=new Timer(){

			@Override
			public void run() {
				updateCanvas();
			}

			
			
		};
		timer.scheduleRepeating(1000/30);
		*/
		
		
		updateCanvas();
		
		Button bt=new Button("step",new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				updateCanvas();
			}
		});
		root.add(bt);
		
	}
	
	private void updateCanvas() {
		canvas.getContext2d().clearRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
		
		for(AbstractParticle p:defaultGroup.particles()){
			if(p instanceof RectangleParticle){
				//log(p.toString());
				fillParticle((RectangleParticle)p);
			}else if(p instanceof CircleParticle){
				//log(p.toString());
				fillParticle((CircleParticle)p);
			}
		}
		
		APEngine.log(circle.px()+":"+circle.py());
		
		APEngine.step();
	}
	
	private void fillParticle(CircleParticle partile) {
		double x=partile.px();
		double y=partile.py();
		double hw=partile.radius();
		
		//log(x+","+y+" "+hw+":"+hh);
		canvas.getContext2d().setFillStyle("#080");
		
		canvas.getContext2d().fillRect(x-hw, y-hw, hw*2, hw*2);//TODO draw circle
	}

	public void fillParticle(RectangleParticle partile){
		double x=partile.px();
		double y=partile.py();
		double hw=partile.width()/2;
		double hh=partile.height()/2;
		//log(x+","+y+" "+hw+":"+hh);
		canvas.getContext2d().setFillStyle("#800");
		canvas.getContext2d().fillRect(x-hw, y-hh, hw*2, hh*2);
	}
	


}
