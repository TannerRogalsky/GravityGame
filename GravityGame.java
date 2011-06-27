package com.tanner;

import java.awt.Dimension;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GravityGame extends StateBasedGame {

	public GravityGame(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initStatesList(GameContainer arg0) throws SlickException {
		addState(new GameplayState());

	}

	/**
	 * @param args
	 */	
	public static void main(String[] args) {
		try {
			Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			AppGameContainer app = new AppGameContainer(new GravityGame("Gravity Game"), screen.width, screen.height, true); 
			app.setSmoothDeltas(true); 
			app.setVSync(true); 
			app.setTargetFrameRate(60);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

}
