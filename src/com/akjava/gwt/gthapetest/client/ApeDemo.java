package com.akjava.gwt.gthapetest.client;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;

public interface ApeDemo {
	public void initialize();
	public void didpose();
	public void step();
	public void speed(double t);
	public void KeyUp(KeyUpEvent event);
	public void keyDown(KeyDownEvent event);
	public String getName();
}
