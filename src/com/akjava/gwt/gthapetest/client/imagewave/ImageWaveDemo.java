package com.akjava.gwt.gthapetest.client.imagewave;

import com.akjava.gwt.gthapetest.client.ApeDemo;
import com.akjava.gwt.gthapetest.client.GTHApeTest;
import com.akjava.gwt.lib.client.CanvasUtils;
import com.akjava.gwt.lib.client.ImageElementListener;
import com.akjava.gwt.lib.client.ImageElementLoader;
import com.akjava.gwt.lib.client.LogUtils;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class ImageWaveDemo implements ApeDemo{
private AbstractEightSplitter splitter;
	@Override
	public void initialize() {
		splitter=new TypeAEightSplitter(0.005,0,0,200,200);
		//splitter=new TypeA2EightSplitter(0.005,0,0,200,200);
		//splitter=new TypeA3EightSplitter(0.005,0,0,200,200);
		//splitter=new TypeBEightSplitter(0.005,0,0,200,200);
		
		
		splitter.setWaveImagePainter(new CanvasImagePainter());
	}

	private Canvas src;
	private boolean loading;
	private static Canvas drawCanvas;
	@Override
	public void step() {
		if(loading){
			return;
		}
		if(src==null){
		loading=true;
		drawCanvas=CanvasUtils.createCanvas(240, 240);
		GTHApeTest.main.getSides().add(drawCanvas);
		//RootPanel.get().add(drawCanvas);
		
		
		ImageElementLoader loader=new ImageElementLoader();
		loader.load("img/largecat.jpg", new ImageElementListener() {
			
			@Override
			public void onLoad(ImageElement element) {
				src=CanvasUtils.createCanvas(element.getWidth(), element.getHeight());
				src.getContext2d().drawImage(element, 0, 0);
				loading=false;
			}
			
			@Override
			public void onError(String url, ErrorEvent event) {
				Window.alert(event.toString());
			}
		});
		}else{
			Canvas canvas=(Canvas) splitter.getWavedImage(src, 0, 0, src.getCoordinateSpaceWidth(), src.getCoordinateSpaceHeight());
			//LogUtils.log("size:"+canvas.getCoordinateSpaceWidth()+","+canvas.getCoordinateSpaceHeight());
			drawCanvas.getContext2d().drawImage(canvas.getCanvasElement(), 0, 0);
		}
		
		
	}

	@Override
	public void speed(double t) {
		
		if(t==0){
			return;
		}
		splitter.addForce(0, t*20);
	}

	@Override
	public void KeyUp(KeyUpEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyDown(KeyDownEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didpose() {
		drawCanvas.removeFromParent();
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ImageWave";
	}

}
