package com.tanner;

import org.newdawn.slick.geom.Path;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class ColoredPath extends Path {
	private Color color;
	
	/**
	 * Constructor for objects of class ColoredPath
	 */
	public ColoredPath(float sx, float sy, Color color)
	{
		super(sx, sy);
		this.color = color;
	}
	
	public ColoredPath(float sx, float sy){
		this(sx, sy, null);
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr)
	{
		gr.setColor(color);
		gr.draw(this);
	}
}
