package tennis.screens.splash;

import tennis.SpaceTennis3D;
import tennis.managers.Assets;
import tennis.screens.MainMenuScreen;
import tennis.tween.SpriteAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashDeveloperScreen implements Screen {
	private Assets assets;

	private TweenManager tweenManager;

	private SpriteBatch batch;
	private Sprite devSplash;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		devSplash.draw(batch);
		batch.end();

		tweenManager.update(delta);
	}

	@Override
	public void show() {
		assets = new Assets();
		assets.loadScreen(Assets.SPLASH_SCREEN_DEVELOPER);

		batch = new SpriteBatch();

		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());

		Texture devTex = assets.get(Assets.URL_SPLASH_DEV, Texture.class);
		devSplash = new Sprite(devTex);
		devSplash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Tween.set(devSplash, SpriteAccessor.ALPHA).target(0)
				.start(tweenManager);
		Tween.to(devSplash, SpriteAccessor.ALPHA, 2).target(1)
				.start(tweenManager);
		// DO THE SAME IN THE OPPOSITE DIRECTION (YOYO). DELAY FOR 1 SECOND.
		Tween.to(devSplash, SpriteAccessor.ALPHA, 2).target(1).repeatYoyo(1, 1)
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						SpaceTennis3D.goTo(new MainMenuScreen());

					}
				}).start(tweenManager);
	}

	@Override
	public void resize(int width, int height) {
		devSplash.setSize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		batch.dispose();
		devSplash.getTexture().dispose();
		assets.dispose();
	}

}
