package com.akjava.gwt.gthapetest.client;



import com.akjava.gwt.gthape.client.ape.APEngine;
import com.akjava.gwt.gthape.client.ape.AbstractParticle;
import com.akjava.gwt.gthape.client.ape.CircleParticle;
import com.akjava.gwt.gthape.client.ape.Group;
import com.akjava.gwt.gthape.client.ape.RectangleParticle;
import com.akjava.gwt.gthape.client.ape.Vector;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GTHApeTest implements EntryPoint {

	private Canvas canvas;
	private Group group;
	private CircleParticle ball;
	@Override
	public void onModuleLoad() {
		VerticalPanel root=new VerticalPanel();
		RootPanel.get().add(root);
		
		APEngine.container(new ArrayListDisplayObjectContainer());
		APEngine.init(1.0);
		APEngine.addMasslessForce(new Vector(0,3));
		
		group = new Group(true);
		group.addParticle(new RectangleParticle(160,500,320,40,0,true,1,0.5,0));
		group.addParticle(new RectangleParticle(160,50,320,40,0,true,1,0.5,0));
		
		ball = new CircleParticle(200,100,0.5);
		//ball.addForce(new Vector(10,-200));
		group.addParticle(ball);
		
		final RectangleParticle ball2 = new RectangleParticle(100,400,5,5,0,false);
		ball.addForce(new Vector(10,-10));
		group.addParticle(ball2);
		
		APEngine.addGroup(group);
		
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
		
		for(AbstractParticle p:group.particles()){
			if(p instanceof RectangleParticle){
				//log(p.toString());
				fillParticle((RectangleParticle)p);
			}else if(p instanceof CircleParticle){
				//log(p.toString());
				fillParticle((CircleParticle)p);
			}
		}
		
		APEngine.log(ball.px()+":"+ball.py());
		
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
