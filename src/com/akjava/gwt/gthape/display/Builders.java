package com.akjava.gwt.gthape.display;

public class Builders {
private static SpriteBuilder spliteBuilder;

public static SpriteBuilder getSpliteBuilder() {
	return spliteBuilder;
}

public static void setSpliteBuilder(SpriteBuilder spliteBuilder) {
	Builders.spliteBuilder = spliteBuilder;
}
}
