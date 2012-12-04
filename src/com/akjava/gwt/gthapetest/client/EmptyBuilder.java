package com.akjava.gwt.gthapetest.client;

import com.akjava.gwt.gthape.client.display.Sprite;
import com.akjava.gwt.gthape.client.display.SpriteBuilder;

public class EmptyBuilder implements SpriteBuilder {
	@Override
	public Sprite createSprite() {
		return new DummySprite();
	}

}
