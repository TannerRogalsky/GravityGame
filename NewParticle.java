package com.tanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class NewParticle extends Circle {
	
	private Color color;
	private ColoredPath path;
	private Vector2f velocity;
	private int mass;
	
	public NewParticle(int centerPointX, int centerPointY, float velX, float velY, int mass) {
		super(centerPointX, centerPointY, (float) Math.log(Math.E + mass / 1000));
		this.mass = mass;
		this.velocity = new Vector2f(velX, velY);		
		this.color = new Color(0xFF0000>>16, (0x00FF00>>8) / (int)(1 + mass/100000), 0x0000FF / (int)(1 + mass/10000));
		this.path = new ColoredPath(centerPointX, centerPointY, this.color);
	}
	
	public void update(GameContainer gc, StateBasedGame sb, int delta)
    {
		this.setX(this.getX() + velocity.getX());
		this.setY(this.getY() + velocity.getY());
		path.lineTo(this.getCenterX(), this.getCenterY());
    }
 
    public void render(GameContainer gc, StateBasedGame sb, Graphics gr)
    {
    	gr.setColor(color);
        gr.draw(this);
    }
	
    @Override
	public String toString(){
        return this.getCenterX() + ", " + this.getCenterY();
    }
}
