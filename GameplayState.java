package com.tanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import net.phys2d.raw.World;
import net.phys2d.raw.Body;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.strategies.BruteCollisionStrategy;
import net.phys2d.raw.CollisionListener;
import net.phys2d.raw.CollisionEvent;

import java.awt.Point;
import java.util.ArrayList;

public class GameplayState extends BasicGameState implements CollisionListener {
	final static float H = 0.005f; //provides a constant so that acceleration can be scaled to a more managable size
	
	private World world;
	private Point leftMousePressed;
	private boolean rightMousePressed;
	private String typed;
	private int mass;
	private Vector2f translate;
	private ArrayList<ColoredPath> deadPaths;
	private int[] sizes = {1, 1000, 10000, 100000, 1000000, 10000000};
	
	public GameplayState() {
		world = new World(new Vector2f(0.0f, 0.0f), 10, new BruteCollisionStrategy());
		world.addListener(this);
		mass = 1000;
		translate = new Vector2f(0,0);
		deadPaths = new ArrayList<ColoredPath>();
	}
	
	@Override
	public void mousePressed(int button, int x, int y){
		x -= translate.x;
		y -= translate.y;
		if (button == Input.MOUSE_LEFT_BUTTON){
			leftMousePressed = new Point(x, y);
		} else if (button == Input.MOUSE_RIGHT_BUTTON){
			rightMousePressed = true;
		}
	}
	
	@Override
	public void mouseReleased(int button, int x, int y){
		x -= translate.x;
		y -= translate.y;
		if (button == Input.MOUSE_LEFT_BUTTON){
			if (leftMousePressed != null){
				world.add(new Particle(leftMousePressed.x, leftMousePressed.y, (x - leftMousePressed.x), (y - leftMousePressed.y), mass));
			}
			leftMousePressed = null;
		}else if (button == Input.MOUSE_RIGHT_BUTTON){
			rightMousePressed = false;
		}
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy){
		if(rightMousePressed){
			translate.add(new Vector2f(newx - oldx, newy - oldy));
		}
	}
	
	@Override
	public void mouseWheelMoved(int change){
		int current = 0;
		for(int size : sizes){
			if (mass == size){
				break;
			}
			current++;
		}
		current += change/120;
		if (current < 0){
			current += sizes.length;
		}else if (current >= sizes.length){
			current -= sizes.length;
		}
		mass = sizes[current];
	}
	
	@Override
	public void keyPressed(int key, char c){
		if((typed == null) && (key == Input.KEY_ENTER)){
			typed = new String("");
		} else if ((typed != null) && (key == Input.KEY_ENTER)){
			try {
				if(typed.equalsIgnoreCase("exit")){
					System.exit(0);
				}else if(typed.equalsIgnoreCase("clear")){
					world.getBodies().clear();
					deadPaths.clear();
				}else{
					mass = Integer.parseInt(typed);
				}
			} catch (NumberFormatException e){
				System.err.println(typed + " is not a valid integer.");
			}finally{
				typed = null;
			}
		} else if ((typed != null) && (key == Input.KEY_BACK) || (typed != null) && (key == Input.KEY_DELETE)){
			if(typed.length() - 1 >= 0){
				typed = typed.substring(0, typed.length() - 1);
			}
		} else if (typed != null){
			typed += c;
		}
	}
	
	public void protoDisk(float centerX, float centerY){
		java.util.Random r = new java.util.Random();
		for (int i = 0; i < 1000; i++){
			float rand = r.nextFloat()*2*(float)Math.PI;
			float rand2 = r.nextFloat();
			float x = (100*rand2)*(float)Math.cos(rand);
			float y = (100*rand2)*(float)Math.sin(rand);
			float mag = (float)Math.sqrt(x*x+y*y);
			Particle particle = new Particle(centerX+x, centerY+y, y*(mag/70), -x*(mag/70), 1000);
			world.add(particle);
		}
	}
	
	@Override
	public void collisionOccured(CollisionEvent event){
		Particle bodyA = (Particle)event.getBodyA();
		Particle bodyB = (Particle)event.getBodyB();
		world.remove(bodyA);
		world.remove(bodyB);
		float massSum = (int)bodyA.getMass() + bodyB.getMass();
		Particle newBody = new Particle((bodyA.getPosition().getX() * bodyA.getMass() + bodyB.getPosition().getX() * bodyB.getMass()) / massSum,
				(bodyA.getPosition().getY() * bodyA.getMass() + bodyB.getPosition().getY() * bodyB.getMass()) / massSum,
				(bodyA.getVelocity().getX() * bodyA.getMass() + bodyB.getVelocity().getX() * bodyB.getMass()) / massSum,
				(bodyA.getVelocity().getY() * bodyA.getMass() + bodyB.getVelocity().getY() * bodyB.getMass()) / massSum,
				massSum);
		world.add(newBody);
		bodyA.getPath().lineTo(newBody.getCenterPosition().getX(), newBody.getCenterPosition().getY());
		bodyB.getPath().lineTo(newBody.getCenterPosition().getX(), newBody.getCenterPosition().getY());
		deadPaths.add(bodyA.getPath());
		deadPaths.add(bodyB.getPath());
	}	

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame sb, Graphics g)
			throws SlickException {
		g.setColor(Color.magenta);
		g.drawString("Mass: " + mass, 0, container.getHeight() - (g.getFont().getLineHeight() + 2) * 2);
		g.drawString("Particles: " + world.getBodies().size(), 0, container.getHeight() - (g.getFont().getLineHeight() + 2) * 3);
		g.drawString("Memory used: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "MB", 0, container.getHeight() - (g.getFont().getLineHeight() + 2) * 4);
				
		if(typed != null){
			g.setColor(Color.magenta);
			g.drawString("> " + typed, 0, container.getHeight() - (g.getFont().getLineHeight() + 2));
		}
		
		// anything drawn below this line will move
		g.translate(translate.getX(), translate.getY());
		
		if((container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) && (leftMousePressed != null)){
			g.setColor(Color.blue);
			g.drawLine(leftMousePressed.x, leftMousePressed.y, container.getInput().getMouseX() - translate.x, container.getInput().getMouseY() - translate.y);
		}
		
		for(ColoredPath deadPath : deadPaths){
			g.setColor(deadPath.getColor());
			g.draw(deadPath);
		}
		
		for (int x = 0; x < world.getBodies().size(); x++){
			Particle particle = (Particle)world.getBodies().get(x);
			g.setColor(particle.getColor());
			g.draw(particle.getPath());
		}
		
		for (int x = 0; x < world.getBodies().size(); x++){
			Particle particle = (Particle)world.getBodies().get(x);
			g.setColor(particle.getColor());
			g.fill(particle.getDrawableCircle());
		}		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		for (int i = 0; i < world.getBodies().size(); i++){
			Body bodyA = world.getBodies().get(i);
			for (int j = 0; j < world.getBodies().size(); j++){
				Body bodyB = world.getBodies().get(j);
				if(!bodyA.equals(bodyB)){
					float xDist = bodyB.getPosition().getX() - bodyA.getPosition().getX();
					float yDist = bodyB.getPosition().getY() - bodyA.getPosition().getY();
					float distance = bodyA.getPosition().distance(bodyB.getPosition());
					float componentAccel = bodyB.getMass() / (distance * distance);
					bodyA.adjustVelocity(new Vector2f(componentAccel * (xDist / distance) * H, componentAccel * (yDist / distance) * H));
				}
			}
			((Particle)bodyA).updatePath();
		}
		
		world.step();		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}
}