package com.akjava.gwt.gthapetest.client;

import java.util.ArrayList;
import java.util.List;

import com.akjava.gwt.gthape.client.display.DisplayObjectContainer;

public class ArrayListDisplayObjectContainer implements DisplayObjectContainer{
List<Object> objects=new ArrayList<Object>();
	@Override
	public void addChild(Object object) {
		objects.add(object);
	}

}
