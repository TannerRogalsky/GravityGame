package com.tanner;

import org.newdawn.slick.geom.Path;
import org.newdawn.slick.Color;

public class ColoredPath extends Path {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3248129787719850201L;
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
}
