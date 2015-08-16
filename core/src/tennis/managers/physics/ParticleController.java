package tennis.managers.physics;

import tennis.managers.PFXPool;
import tennis.managers.Soundbox;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

/**
 * Custom controller class for managing the particleSystem. Creates particle
 * pools, particle effects and add them to the system simulator when necessary.
 * Also plays the sound of explosions.
 * 
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class ParticleController implements Disposable {

	private PerspectiveCamera cam;
	private ModelBatch batch;

	private static final String URL_PARTICLE1 = "particles/blue.pfx";
	private static final String URL_PARTICLE2 = "particles/red.pfx";
	private static final String URL_PARTICLE3 = "particles/blue-explosion.pfx";
	private static final String URL_DEFAULT_PARTICLE = "particles/pre_particle.png";

	private AssetManager particleManager;
	private BillboardParticleBatch particleBatch;
	private ParticleSystem particleSystem;
	private ParticleEffect originalEffect;
	private PFXPool particles1Hit;
	private PFXPool particles2Hit;
	private PFXPool explosions;

	public ParticleController(PerspectiveCamera cam, ModelBatch batch) {
		this.cam = cam;
		this.batch = batch;

		// PARTICLE SYSTEM
		particleSystem = ParticleSystem.get();
		particleManager = new AssetManager();
		particleBatch = new BillboardParticleBatch();
		particleBatch.setCamera(cam);
		particleSystem.add(particleBatch);

		// LOAD PARTICLES
		ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(
				particleSystem.getBatches());
		ParticleEffectLoader loader = new ParticleEffectLoader(
				new InternalFileHandleResolver());
		particleManager.setLoader(ParticleEffect.class, loader);
		particleManager.load(URL_DEFAULT_PARTICLE, Texture.class);
		particleManager.load(URL_PARTICLE1, ParticleEffect.class, loadParam);
		particleManager.load(URL_PARTICLE2, ParticleEffect.class, loadParam);
		particleManager.load(URL_PARTICLE3, ParticleEffect.class, loadParam);
		particleManager.finishLoading();
		particleBatch.setTexture(particleManager.get(URL_DEFAULT_PARTICLE,
				Texture.class));

		// CREATE EFFECT POOLS
		originalEffect = particleManager.get(URL_PARTICLE1);
		particles1Hit = new PFXPool(originalEffect);
		originalEffect = particleManager.get(URL_PARTICLE2);
		particles2Hit = new PFXPool(originalEffect);
		originalEffect = particleManager.get(URL_PARTICLE3);
		explosions = new PFXPool(originalEffect);
	}
	
	/**
	 * Updates the cam and batch of the scene.
	 */
	public void setBatch(PerspectiveCamera cam, ModelBatch batch){
		this.cam = cam;
		this.batch = batch;
	}

	/**
	 * Render the current particle effects in the particle system.
	 */
	public void renderParticleEffects() {
		particleSystem.update();
		particleBatch.setCamera(cam);
		particleSystem.begin();
		particleSystem.draw();
		particleSystem.end();
		batch.render(particleSystem);
	}

	/**
	 * Creates a hit particle effect of a given player (blue or red) in a
	 * certain location
	 * 
	 * @param player
	 *            Color of player 1 (blue) or player 2 (red).
	 * @param position
	 *            Location of effect.
	 */
	public void explodeHit(int player, Vector3 position) {
		ParticleEffect effect;
		switch (player) {
		case 1:
			effect = particles1Hit.obtain();
			break;
		case 2:
			effect = particles2Hit.obtain();
			break;
		default:
			effect = particles1Hit.obtain();
			break;
		}
		effect.translate(position);
		effect.init();
		effect.start();
		particleSystem.add(effect);
		Soundbox.play("laser");
	}

	/**
	 * Creates a endturn explosion in a certain location.
	 * 
	 * @param position
	 *            Location of effect.
	 */
	public void explosion(Vector3 position) {
		ParticleEffect effect = explosions.obtain();
		effect.translate(position);
		effect.init();
		effect.start();
		particleSystem.add(effect);
		Soundbox.play("explosion");
	}

	public void dispose() {
		originalEffect.dispose();
		batch.dispose();
		particles1Hit.dispose();
		particles2Hit.dispose();
		explosions.dispose();
	}

	public void disposeAll() {
		particleManager.dispose();
		dispose();
	}

}
