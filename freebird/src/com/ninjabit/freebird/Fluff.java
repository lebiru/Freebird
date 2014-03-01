package com.ninjabit.freebird;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Fluff 
{

	float fluffHeight;
	float fluffWidth;
	Rectangle fluffRectangle;
	AnimatedSprite a;
	boolean isWhiteFluff;
	float speed;
	float angle;
	float scale;
	Random ran = new Random();

	public Fluff(AnimatedSprite fluffAnimation, boolean isWhite)
	{
		a = fluffAnimation;
		fluffWidth = a.getWidth() / 2;
		fluffHeight = a.getHeight() / 2;
		fluffRectangle = new Rectangle();
		fluffRectangle.x = 800;
		fluffRectangle.y = MathUtils.random(0, 480 - fluffHeight);
		fluffRectangle.setWidth(fluffWidth/2);
		fluffRectangle.setHeight(fluffHeight/2);
		scale = betweenTwo(0.5f, 1);
		
		speed = betweenTwo(200, 400);
		angle = betweenTwo(0, 360);
		if(isWhite == true)
		{
			isWhiteFluff = true;
		}
		else
		{
			isWhiteFluff = false;
		}


	}
	
	public float betweenTwo(float min, float max)
	{
		float sub = max - min;
		return ran.nextFloat() * sub + min;
	}

}


