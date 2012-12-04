package com.akjava.gwt.gthape.display;

public interface Sprite extends DisplayObject{
public Graphics getGraphics();
public void addChild(Object object);//TODO should i displayobject?
public void removeChildAt(int index);
public int numChildren();
public void setVisible(boolean bool);
public void setName(String string);
public DisplayObject getChildByName(String string);

}
