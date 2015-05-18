package tennis.screens.splash;

import tennis.tween.SpriteAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashUsScreen implements Screen {

	private TweenManager tweenManager;

	private SpriteBatch batch;
	private Sprite usSplash;

	private String us = "img/splash_screen/us.png";

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		usSplash.draw(batch);
		batch.end();
		
		tweenManager.update(delta);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		Texture usTex = new Texture(Gdx.files.internal(us));
		usSplash = new Sprite(usTex);
		usSplash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		Tween.set(usSplash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(usSplash, SpriteAccessor.ALPHA, 2).target(1).start(tweenManager);
		// Do the same in the opposite direction (Yoyo). Delay of 1 second.
		Tween.to(usSplash, SpriteAccessor.ALPHA, 2).target(1).repeatYoyo(1, 1).setCallback(new TweenCallback() {
			
			@Override
			public void onEvent(int arg0, BaseTween<?> arg1) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new SplashDeveloperScreen());
				
			}
		}).start(tweenManager);
	}

	@Override
	public void resize(int width, int height) {
		usSplash.setSize(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();
		usSplash.getTexture().dispose();
	}

}
