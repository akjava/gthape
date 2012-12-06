package com.akjava.gwt.gthapetest.client;



import java.util.HashMap;
import java.util.Map;

import com.akjava.gwt.gthape.client.ape.APEngine;
import com.akjava.gwt.gthape.client.ape.AbstractConstraint;
import com.akjava.gwt.gthape.client.ape.AbstractParticle;
import com.akjava.gwt.gthape.client.ape.CircleParticle;
import com.akjava.gwt.gthape.client.ape.Composite;
import com.akjava.gwt.gthape.client.ape.Group;
import com.akjava.gwt.gthape.client.ape.RectangleParticle;
import com.akjava.gwt.gthape.client.ape.SpringConstraint;
import com.akjava.gwt.gthape.client.ape.Vector;
import com.akjava.gwt.gthape.client.ape.WheelParticle;
import com.akjava.gwt.gthapetest.client.cardemo.CarDemo;
import com.akjava.gwt.lib.client.CanvasUtils;
import com.akjava.gwt.lib.client.LogUtils;
import com.akjava.gwt.stats.client.Stats;
import com.akjava.gwt.three.client.THREE;
import com.akjava.gwt.three.client.cameras.Camera;
import com.akjava.gwt.three.client.core.Object3D;
import com.akjava.gwt.three.client.experiments.CSS3DObject;
import com.akjava.gwt.three.client.experiments.CSS3DRenderer;
import com.akjava.gwt.three.client.scenes.Scene;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ibm.icu.impl.CalendarAstronomer.Horizon;

public class GTHApeTest implements EntryPoint {

	//private Canvas canvas;
	
	

	

	private Camera camera;

	private CSS3DRenderer renderer;

	private Scene scene;
	int width=650;
	int height=400;
	Object3D objRoot;
	@Override
	public void onModuleLoad() {
		VerticalPanel root=new VerticalPanel();
		RootPanel.get().add(root);
		
		APEngine.container(new ArrayListDisplayObjectContainer());
		
		carDemo = new CarDemo();
		carDemo.initialize();
		
		//APEngine.step();
		
		
		scene = THREE.Scene();
		objRoot=THREE.Object3D();
		objRoot.setPosition(-width/2, 0, 0);
		scene.add(objRoot);
		
		camera = THREE.PerspectiveCamera(35,(double)width/height,.1,10000);
		scene.add(camera);
		camera.getPosition().setZ(700);
		camera.getPosition().setX(0);
		camera.getPosition().setY(-150);
		//camera.getRotation().setZ(Math.toRadians(180)); //fliphorizontaled
		renderer = THREE.CSS3DRenderer();
		renderer.setSize(width, height);
		
		HTMLPanel div=new HTMLPanel("");
		div.getElement().appendChild(renderer.getDomElement());
		focusPanel = new FocusPanel();
		focusPanel.add(div);
		root.add(focusPanel);
		focusPanel.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				carDemo.keyDown(event);
			}
		});
		focusPanel.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				carDemo.KeyUp(event);
			}
		});
		focusPanel.setFocus(true);
		
		/*
		canvas = Canvas.createIfSupported();
		canvas.setSize("600px", "600px");
		canvas.setCoordinateSpaceWidth(600);
		canvas.setCoordinateSpaceHeight(600);
		
		root.add(canvas);
		*/
		final Stats stats=Stats.insertStatsToRootPanel();
		//stats.setPosition(8, 0);
		
		Timer timer=new Timer(){

			@Override
			public void run() {
				stats.begin();
				if(doInit){
					LogUtils.log("init");
					init();
					doInit=false;
				}
				//objRoot.getRotation().incrementX(0.01);
				updateCanvas();
				stats.end();
			}

			
			
		};
		timer.scheduleRepeating(1000/60);
		
		
		
		updateCanvas();
		

	HorizontalPanel buttons=new HorizontalPanel();	
Button init=new Button("initialize position",new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				doInit=true;
				focusPanel.setFocus(true);
			}
		});
		root.add(buttons);
		buttons.add(init);
		
		Button prev=new Button("<- Back",new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				carDemo.speed(-0.2);
				Timer t=new Timer(){
					public void run(){
						carDemo.speed(0);
					}
				};
				t.schedule(500);
			}
		});
		
		buttons.add(prev);
		
		Button next=new Button("Foward ->",new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				carDemo.speed(0.2);
				Timer t=new Timer(){
					public void run(){
						carDemo.speed(0);
					}
				};
				t.schedule(500);
			}
		});
		
		buttons.add(next);
		
	}
	
	
	
	boolean doInit;
	
	private void init(){
		/*
		 * initialize faild.car something faild
		 */
		carDemo.initialize();
		threeObjects.clear();
		threeSprings.clear();
		
		renderer.gwtClear();
		
		scene = THREE.Scene();
		scene.add(camera);
		
		objRoot=THREE.Object3D();
		objRoot.setPosition(-width/2, 0, 0);
		scene.add(objRoot);
		
		//LogUtils.log("renderer:"+renderer.getCameraElement().getChildNodes().getLength());
	}
	
	private void updateCanvas() {
		renderer.render(scene, camera);
		
		int totalItem=0;
		//canvas.getContext2d().clearRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
		for(Group group:APEngine.getGroups()){
		
		for(Composite composite:group.composites()){
			for(AbstractParticle p:composite.particles()){
				
				if(p instanceof RectangleParticle){
					//log(p.toString());
					fillParticle((RectangleParticle)p);
					totalItem++;
				}else if(p instanceof WheelParticle){
					//log(p.toString());
					fillParticle((WheelParticle)p);
					totalItem++;
				}else if(p instanceof CircleParticle){
					//log(p.toString());
					fillParticle((CircleParticle)p);
					totalItem++;
				}
			}
			
			for(AbstractConstraint cons:composite.constraints()){
				if(cons instanceof SpringConstraint){
					fillConstraint((SpringConstraint)cons);
					totalItem++;
				}
			}
		}
			
		for(AbstractParticle p:group.particles()){
			
			if(p instanceof RectangleParticle){
				//log(p.toString());
				fillParticle((RectangleParticle)p);
				totalItem++;
			}else if(p instanceof WheelParticle){
				//log(p.toString());
				fillParticle((WheelParticle)p);
				totalItem++;
			}else if(p instanceof CircleParticle){
				//log(p.toString());
				fillParticle((CircleParticle)p);
				totalItem++;
			}
		}
		
		for(AbstractConstraint cons:group.constraints()){
			if(cons instanceof SpringConstraint){
				fillConstraint((SpringConstraint)cons);
				totalItem++;
			}
		}
		
		}
		
		//LogUtils.log("renderer:"+renderer.getCameraElement().getChildNodes().getLength());
		//LogUtils.log("obj:"+scene.objects().length());
		//LogUtils.log("total:"+totalItem);
		
		APEngine.step();
		carDemo.step();
	}
	
	private void fillConstraint(SpringConstraint cons) {
		Vector vec=cons.center();
		
		
		
		double length=cons.currLength();
		double angle=cons.radian();
		double height=1;
		if(cons.collidable()){
			height=cons.rectHeight();
		}
		Object3D obj=threeSprings.get(cons);
		if(obj==null){
			Image img=new Image(CanvasUtils.createColorRectImageDataUrl(0, 0, 128, 1, (int)length, (int)height));
			obj=CSS3DObject.createObject(img.getElement());
			objRoot.add(obj);
			threeSprings.put(cons, obj);
		}
		obj.setPosition(vec.x, -vec.y, 0);
		obj.setRotation(0, 0, -angle);
		obj.setVisible(cons.visible());//sprint support visible
	}

	Map<AbstractParticle,Object3D> threeObjects=new HashMap<AbstractParticle,Object3D>();
	Map<SpringConstraint,Object3D> threeSprings=new HashMap<SpringConstraint,Object3D>();

	


	private CarDemo carDemo;

	private FocusPanel focusPanel;
	
	private void fillParticle(CircleParticle partile) {
		double x=partile.px();
		double y=partile.py();
		double hw=partile.radius();
		
		
		
		Object3D obj=threeObjects.get(partile);
		if(obj==null){
			Image img=new Image(CanvasUtils.createCircleImageDataUrl(128, 0, 0, 1, (int)(hw), 3,false));
			obj=CSS3DObject.createObject(img.getElement());
			objRoot.add(obj);
			threeObjects.put(partile, obj);
		}
		obj.setPosition(x, -y, 0);
		
		
		//canvas.getContext2d().fillRect(x-hw, y-hw, hw*2, hw*2);//TODO draw circle
	}
	
	private void fillParticle(WheelParticle partile) {
		double x=partile.px();
		double y=partile.py();
		double hw=partile.radius();
		double a=partile.radian();
		
		
		Object3D obj=threeObjects.get(partile);
		if(obj==null){
			Image img=new Image(createWheelDataUrl(128, 0, 0, 1, (int)(hw), 2,true));
			obj=CSS3DObject.createObject(img.getElement());
			objRoot.add(obj);
			threeObjects.put(partile, obj);
		}
		obj.setPosition(x, -y, 0);
		obj.setRotation(0, 0, -a);
		
		//canvas.getContext2d().fillRect(x-hw, y-hw, hw*2, hw*2);//TODO draw circle
	}

	public void fillParticle(RectangleParticle partile){
		double x=partile.px();
		double y=partile.py();
		double hw=partile.width()/2;
		double hh=partile.height()/2;
		double a=partile.radian();
		
		
		Object3D obj=threeObjects.get(partile);
		if(obj==null){
			LogUtils.log("obj created");
			Image img=new Image(CanvasUtils.createColorRectImageDataUrl(128, 0, 0, 1, (int)(hw*2), (int)(hh*2)));
			
			obj=CSS3DObject.createObject(img.getElement());
			//obj=CSS3DObject.createObject(new Label("hello").getElement());
			objRoot.add(obj);
			threeObjects.put(partile, obj);
		}
		obj.setPosition(x, -y, 0);
		obj.setRotation(0, 0, -a);
	}
	

	public static String createWheelDataUrl(int r,int g,int b,double opacity,double radius,double lineWidth,boolean stroke){
		double center=radius+lineWidth;
		Canvas canvas=CanvasUtils.createCanvas((int)center*2,(int)center*2);
		if(stroke){
		canvas.getContext2d().setStrokeStyle("rgba("+r+","+g+","+b+","+opacity+")");
		canvas.getContext2d().setLineWidth(lineWidth);
		}else{
		canvas.getContext2d().setFillStyle("rgba("+r+","+g+","+b+","+opacity+")");	
		}
		canvas.getContext2d().beginPath();
		
		canvas.getContext2d().arc(center, center, radius, 0, 360);
		canvas.getContext2d().closePath();
		if(stroke){
			canvas.getContext2d().stroke();
		}else{
			canvas.getContext2d().fill();
		}
		canvas.getContext2d().beginPath();
		canvas.getContext2d().moveTo(0+lineWidth, center);
		canvas.getContext2d().lineTo(center*2-lineWidth, center);
		canvas.getContext2d().closePath();
		canvas.getContext2d().stroke();
		
		canvas.getContext2d().beginPath();
		canvas.getContext2d().moveTo(center, 0+lineWidth);
		canvas.getContext2d().lineTo(center, center*2-lineWidth);
		canvas.getContext2d().closePath();
		canvas.getContext2d().stroke();
		
		String image1=canvas.toDataUrl();
		return image1;
	}
}
