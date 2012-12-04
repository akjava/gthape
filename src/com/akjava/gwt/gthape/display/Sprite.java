package com.akjava.gwt.gthape.display;

public interface Sprite {
public Graphics getGraphics();
public void addChild(Object object);
public void removeChildAt(int index);
public int numChildren();
public void setVisible(boolean bool);
}
