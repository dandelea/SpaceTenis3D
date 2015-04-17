package tenis.gamestates;

import tenis.managers.GameStateManager;
import tenis.managers.State;
import tenis.tween.SpriteAccessor;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashPublisherState extends GameState {
	private TweenManager tweenManager;

	private SpriteBatch batch;
	private Sprite usSplash;

	private String us = "img/splash_screen/us.png";

	public SplashPublisherState(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		batch = new SpriteBatch();

		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
	}

	@Override
	public void update(float delta) {
		handleInput();
		tweenManager.update(delta);
	}

	@Override
	public void draw() {
		Texture usTex = new Texture(Gdx.files.internal(us));
		usSplash = new Sprite(usTex);
		usSplash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Tween.set(usSplash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(usSplash, SpriteAccessor.ALPHA, 2).target(1)
				.start(tweenManager);
		// Do the same in the opposite direction (Yoyo). Delay of 1 second.
		Tween.to(usSplash, SpriteAccessor.ALPHA, 2).target(1).repeatYoyo(1, 1)
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						gsm.setState(State.MAIN_MENU);

					}
				}).start(tweenManager);
		
		batch.begin();
		usSplash.draw(batch);
		batch.end();
	}

	@Override
	public void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.isKeyJustPressed(Input.Buttons.LEFT)){
			gsm.setState(State.MAIN_MENU);
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
