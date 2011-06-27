package com.tanner;

import org.newdawn.slick.Color;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.math.Vector2f;


public class Particle extends Body {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7667890996319826698L;
	
	private Color colour;
	private ColoredPath path;

	public Particle(float x, float y, float velX, float velY, float mass) {
		super(new Circle ((float)Math.log(Math.E + mass / 1000)), mass);
		this.setPosition(x, y);
		this.adjustVelocity(new Vector2f(velX, velY));		
		this.colour = new Color(0xFF0000>>16, (0x00FF00>>8) / (int)(1 + mass/100000), 0x0000FF / (int)(1 + mass/10000));
		this.path = new ColoredPath(getCenterPosition().getX(), getCenterPosition().getY(), this.colour);
	}
	
	public Particle(int centerPointX, int centerPointY, float velX, float velY, int mass) {
		super(new Circle ((float)Math.log(Math.E + mass / 1000)), mass);
		this.setPosition(centerPointX - ((Circle)this.getShape()).getRadius(), centerPointY - ((Circle)this.getShape()).getRadius());
		this.adjustVelocity(new Vector2f(velX, velY));		
		this.colour = new Color(0xFF0000>>16, (0x00FF00>>8) / (int)(1 + mass/100000), 0x0000FF / (int)(1 + mass/10000));
		this.path = new ColoredPath(getCenterPosition().getX(), getCenterPosition().getY(), this.colour);
	}
	
	public Color getColor(){
        return colour;
    }
	
	public org.newdawn.slick.geom.Circle getDrawableCircle(){
		org.newdawn.slick.geom.Circle circle = new org.newdawn.slick.geom.Circle(0, 0, ((Circle)this.getShape()).getRadius());
		circle.setX(this.getPosition().getX());
		circle.setY(this.getPosition().getY());
		return circle;
	}
	
	public Vector2f getCenterPosition(){
		return new Vector2f (this.getPosition().getX() + ((Circle)this.getShape()).getRadius(), this.getPosition().getY() + ((Circle)this.getShape()).getRadius());
	}
	
	public ColoredPath getPath(){
		return path;
	}
	
	public void updatePath(){
		path.lineTo(getCenterPosition().getX(), getCenterPosition().getY());
	}
	
    @Override
	public String toString(){
        return this.getPosition().getX() + ", " + this.getPosition().getY();
    }
}
