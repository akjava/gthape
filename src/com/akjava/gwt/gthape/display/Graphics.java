package com.akjava.gwt.gthape.display;

public interface Graphics {
public void clear();

public void endFill();

public void drawRect(double d, double e, double w, double h);

public void beginFill(int fillColor, double fillAlpha);

public void lineStyle(double lineThickness, int lineColor, double lineAlpha);
}
