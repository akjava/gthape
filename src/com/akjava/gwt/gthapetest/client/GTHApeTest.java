package com.akjava.gwt.gthapetest.client;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.akjava.gwt.gthapetest.client.imagewave.ImageWaveDemo;
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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GTHApeTest implements EntryPoint {

	//private Canvas canvas;
	
	

	

	private Camera camera;

	private CSS3DRenderer renderer;

	private Scene scene;
	int width=650;
	int height=400;
	Object3D objRoot;
	
	List<ApeDemo> demos=new ArrayList<ApeDemo>();
	@Override
	public void onModuleLoad() {
		VerticalPanel root=new VerticalPanel();
		RootPanel.get().add(root);
		
		demos.add(new CarDemo());
		demos.add(new ImageWaveDemo());
		//APEngine.container(new ArrayListDisplayObjectContainer());
		
		//apeDemo = new CarDemo();
		//apeDemo=new ImageWaveDemo();
		
		//apeDemo.initialize();
		
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
				apeDemo.keyDown(event);
			}
		});
		focusPanel.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				apeDemo.KeyUp(event);
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
			public void run() {//wait?
				long t=System.currentTimeMillis();
				//LogUtils.log(""+(t-last));
				last=t;
				stats.begin();
				if(doInit){
					//LogUtils.log("init");
					init();
					doInit=false;
				}
				
				updateCanvas();
				stats.end();
			}

			
			
		};
		timer.scheduleRepeating(1000/60);
		
		
		
		updateCanvas();
		

	HorizontalPanel buttons=new HorizontalPanel();	
	
	
	ValueListBox<ApeDemo> demosList=new ValueListBox<ApeDemo>(new Renderer<ApeDemo>() {

		@Override
		public String render(ApeDemo object) {
			if(object==null){
				return null;
			}
			return object.getName();
		}

		@Override
		public void render(ApeDemo object, Appendable appendable)
				throws IOException {
			
		}
	});
	demosList.addValueChangeHandler(new ValueChangeHandler<ApeDemo>() {
		
		@Override
		public void onValueChange(ValueChangeEvent<ApeDemo> event) {
			
			init();//remove
			apeDemo=event.getValue();
			apeDemo.initialize();
		}
	});
	demosList.setValue(demos.get(0));
	demosList.setAcceptableValues(demos);
	
	buttons.add(demosList);
	
	apeDemo=demos.get(0);
	apeDemo.initialize();
	
	
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
				apeDemo.speed(-1);
				Timer t=new Timer(){
					public void run(){
						apeDemo.speed(0);
					}
				};
				t.schedule(500);
			}
		});
		
		buttons.add(prev);
		
		Button next=new Button("Foward ->",new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				apeDemo.speed(1);
				Timer t=new Timer(){
					public void run(){
						apeDemo.speed(0);
					}
				};
				t.schedule(500);
			}
		});
		
		buttons.add(next);
		
	}
	
	long last;
	
	boolean doInit;
	public static Map<Object,Integer> colorMap=new HashMap<Object, Integer>();
	private void init(){
		/*
		 * initialize faild.car something faild
		 */
		if(apeDemo!=null){
		apeDemo.initialize();
		}
		
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
		if(apeDemo==null){
			return;//useless
		}
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
			apeDemo.step();
		
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
			Integer color=colorMap.get(cons);
			int r=128;
			int g=0;
			int b=0;
			if(color!=null){
				int[]rgb=com.akjava.lib.common.utils.ColorUtils.toRGB(color);
				r=rgb[0];
				g=rgb[1];
				b=rgb[2];
			}
			Image img=new Image(CanvasUtils.createColorRectImageDataUrl(r, g, b, 1, (int)length, (int)height));
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

	


	private ApeDemo apeDemo;

	private FocusPanel focusPanel;
	
	private void fillParticle(CircleParticle particle) {
		double x=particle.px();
		double y=particle.py();
		double hw=particle.radius();
		
		
		
		Object3D obj=threeObjects.get(particle);
		if(obj==null){
			Image img=new Image(CanvasUtils.createCircleImageDataUrl(128, 0, 0, 1, (int)(hw), 3,false));
			obj=CSS3DObject.createObject(img.getElement());
			objRoot.add(obj);
			threeObjects.put(particle, obj);
		}
		obj.setPosition(x, -y, 0);
		
		
		//canvas.getContext2d().fillRect(x-hw, y-hw, hw*2, hw*2);//TODO draw circle
	}
	
	private void fillParticle(WheelParticle particle) {
		double x=particle.px();
		double y=particle.py();
		double hw=particle.radius();
		double a=particle.radian();
		
		
		Object3D obj=threeObjects.get(particle);
		if(obj==null){
			Image img=new Image(createWheelDataUrl(128, 0, 0, 1, (int)(hw), 2,true));
			obj=CSS3DObject.createObject(img.getElement());
			objRoot.add(obj);
			threeObjects.put(particle, obj);
		}
		obj.setPosition(x, -y, 0);
		obj.setRotation(0, 0, -a);
		
		//canvas.getContext2d().fillRect(x-hw, y-hw, hw*2, hw*2);//TODO draw circle
	}

	public void fillParticle(RectangleParticle particle){
		double x=particle.px();
		double y=particle.py();
		double hw=particle.width()/2;
		double hh=particle.height()/2;
		double a=particle.radian();
		
		
		Object3D obj=threeObjects.get(particle);
		if(obj==null){
			Integer color=colorMap.get(particle);
			int r=128;
			int g=0;
			int b=0;
			if(color!=null){
				int[]rgb=com.akjava.lib.common.utils.ColorUtils.toRGB(color);
				r=rgb[0];
				g=rgb[1];
				b=rgb[2];
				
			}
			//LogUtils.log("obj created");
			Image img=new Image(CanvasUtils.createColorRectImageDataUrl(r, g, b, 1, (int)(hw*2), (int)(hh*2)));
			
			obj=CSS3DObject.createObject(img.getElement());
			//obj=CSS3DObject.createObject(new Label("hello").getElement());
			objRoot.add(obj);
			threeObjects.put(particle, obj);
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
