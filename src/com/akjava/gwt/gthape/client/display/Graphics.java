package com.akjava.gwt.gthape.client.display;

public interface Graphics {
public void clear();

public void endFill();

public void drawRect(double d, double e, double w, double h);

public void beginFill(int fillColor, double fillAlpha);

public void lineStyle(double lineThickness, int lineColor, double lineAlpha);

public void moveTo(double px, double py);

public void lineTo(double px, double py);

public void drawCircle(int i, int j, double radius);
}
