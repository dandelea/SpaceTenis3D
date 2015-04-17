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

public class SplashDevState extends GameState{
	private TweenManager tweenManager;

	private SpriteBatch batch;
	private Sprite devSplash;

	private String dev = "img/splash_screen/developer.png";

	public SplashDevState(GameStateManager gsm) {
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
		Texture devTex = new Texture(Gdx.files.internal(dev));
		devSplash = new Sprite(devTex);
		devSplash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Tween.set(devSplash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(devSplash, SpriteAccessor.ALPHA, 2).target(1).start(tweenManager);
		// Do the same in the opposite direction (Yoyo). Delay of 1 second.
		Tween.to(devSplash, SpriteAccessor.ALPHA, 2).target(1).repeatYoyo(1, 1).setCallback(new TweenCallback() {
			
			@Override
			public void onEvent(int arg0, BaseTween<?> arg1) {
				gsm.setState(State.SPLASH_PUBLISHER);
			}
		}).start(tweenManager);
		
		batch.begin();
		devSplash.draw(batch);
		batch.end();
	}

	@Override
	public void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)|| Gdx.input.isKeyJustPressed(Input.Buttons.LEFT)){
			gsm.setState(State.SPLASH_PUBLISHER);
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		devSplash.getTexture().dispose();
	}

}
