package com.akjava.gwt.gthapetest.client;

import com.akjava.gwt.gthape.client.ape.APEngine;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class GTHApeTest implements EntryPoint {

	@Override
	public void onModuleLoad() {
		RootPanel.get().add(new Label("hello"));
		
		APEngine.container(new ArrayListDisplayObjectContainer());
		APEngine.init(1);
		APEngine.step();
		
	}

}
