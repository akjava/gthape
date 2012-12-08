package com.akjava.gwt.gthapetest.client.imagewave;

import java.util.ArrayList;

public interface WaveImageCreator {
	public Object getWavedImage(Object srcImage,int x,int y,int width,int height);
	public void setWaveImagePainter(WaveImagePainter painter);
	public void addForce(double xforce,double yforce);
	//public ArrayList getPaintQueue();
}
