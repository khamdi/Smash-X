package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {
	private	final	int				nbFrames;
	private			Texture			sheet;
	private			TextureRegion[]	frames; 
	private			Animation		animation;
	private			TextureRegion	currentFrame;
	
	private 		float			stateTime;
	private 		float			interval;
	
	public Animator (int cols, int rows, String image_path, float interval) {
		nbFrames = rows * cols;
		this.interval = interval;
		sheet = new Texture(Gdx.files.internal(image_path));
		TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth()/cols, sheet.getHeight()/rows);
		frames = new TextureRegion[rows * cols];
		int i, j;
		int index = 0;
		
		for (i = 0 ; i < rows ; i++) {
			for (j = 0 ; j < cols ; j++) {
				frames[index++] = tmp[i][j];
			}
		}
		
		animation = new Animation (interval, frames);
		stateTime = 0;
	}
	
	public Animator (float interval, TextureRegion[] frames) {
		nbFrames = frames.length;
		this.interval = interval;
		animation = new Animation (interval, frames);
		stateTime = 0;
	}
	
	public Animator[] createAnimations (int numberOfAnimations) {
		if (nbFrames % numberOfAnimations != 0) {
			throw (new IllegalArgumentException ("It is impossible to create an equally split number of animations."));
		}
		
		int i, j;
		int nbFramesSplit = nbFrames / numberOfAnimations;
		Animator[] animations = new Animator[numberOfAnimations];
		
		for (i = 0 ; i < numberOfAnimations ; i++) {
			TextureRegion[] frames = new TextureRegion [nbFramesSplit];
			for (j = 0 ; j < nbFramesSplit ; j++) {
				frames[j] = this.frames[i*nbFramesSplit + j];
			}
			animations[i] = new Animator (interval, frames);
		}
		
		return animations;
	}
	
	public TextureRegion firstFrame () {
		stateTime = 0f;
		currentFrame = animation.getKeyFrame(stateTime, true);
		return currentFrame;
	}
	
	public TextureRegion nextFrame () {
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animation.getKeyFrame(stateTime, true);
		return currentFrame;
	}
}
