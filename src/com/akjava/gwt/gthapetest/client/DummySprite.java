package com.akjava.gwt.gthapetest.client;

import java.util.ArrayList;
import java.util.List;

import com.akjava.gwt.gthape.client.display.DisplayObject;
import com.akjava.gwt.gthape.client.display.Graphics;
import com.akjava.gwt.gthape.client.display.Sprite;

public class DummySprite implements Sprite{

	private Graphics graphics=new DummyGraphics();
	private String name="";
	public String getName() {
		return name;
	}

	private List<Object> objects=new ArrayList<Object>();
	@Override
	public void setX(double x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setY(double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setRotation(double rotattion) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getRotation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWidth(double w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeight(double h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Graphics getGraphics() {
		// TODO Auto-generated method stub
		return graphics;
	}

	@Override
	public void addChild(Object object) {
		objects.add(object);
	}

	@Override
	public void removeChildAt(int index) {
		objects.remove(index);
	}

	@Override
	public int numChildren() {
		return objects.size();
	}

	@Override
	public void setVisible(boolean bool) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setName(String name) {
		this.name=name;
	}

	@Override
	public DisplayObject getChildByName(String value) {
		for(Object obj:objects){
			if(obj instanceof DisplayObject){
				String name=((DisplayObject)obj).getName();
				if(name.equals(value)){
					return (DisplayObject)obj;
				}
			}
		}
		return null;
	}

}
