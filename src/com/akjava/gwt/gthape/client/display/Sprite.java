package com.akjava.gwt.gthape.client.display;
/**
 * @deprecated sprite removed
 * @author aki
 *
 */
public interface Sprite extends DisplayObject{
public Graphics getGraphics();
public void addChild(Object object);//TODO should i displayobject?
public void removeChildAt(int index);
public int numChildren();
public void setVisible(boolean bool);

public DisplayObject getChildByName(String string);

}
