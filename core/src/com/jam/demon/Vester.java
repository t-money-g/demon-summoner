package com.jam.demon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Vester
{
	static final int IDLE = 0;
	static final int MOVING = 1;

	//Map map; // will need this but need to query colision first
	Vector2 pos = new Vector2();
	Vector2 vel = new Vector2();
	Rectangle bounds = new Rectangle();
	int state = IDLE;
	float stateTime = 0;

	Vector2 target = new Vector2();

	public void update(final float deltaTime)
	{


	}
}
