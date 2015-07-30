package tennis.managers.physics;

import tennis.managers.PFXPool;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class ParticleController implements Disposable {
	private PerspectiveCamera cam;
	private ModelBatch batch;

	private static final String URL_PARTICLE1 = "particles/hit.pfx";
	private static final String URL_PARTICLE2 = "particles/hit2.pfx";

	private AssetManager particleManager;
	private BillboardParticleBatch particleBatch;
	private ParticleSystem particleSystem;
	private ParticleEffect originalEffect;
	private PFXPool particles1Hit;
	private PFXPool particles2Hit;

	public ParticleController(PerspectiveCamera cam, ModelBatch batch) {
		this.cam = cam;
		this.batch = batch;

		// PARTICLES
		particleSystem = ParticleSystem.get();
		particleManager = new AssetManager();
		particleBatch = new BillboardParticleBatch();
		particleBatch.setCamera(cam);
		particleSystem.add(particleBatch);

		ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(
				particleSystem.getBatches());
		ParticleEffectLoader loader = new ParticleEffectLoader(
				new InternalFileHandleResolver());
		particleManager.setLoader(ParticleEffect.class, loader);
		particleManager.load(URL_PARTICLE1, ParticleEffect.class, loadParam);
		particleManager.load(URL_PARTICLE2, ParticleEffect.class, loadParam);
		particleManager.finishLoading();

		originalEffect = particleManager.get(URL_PARTICLE1);
		particles1Hit = new PFXPool(originalEffect);
		originalEffect = particleManager.get(URL_PARTICLE2);
		particles2Hit = new PFXPool(originalEffect);
	}

	public void renderParticleEffects() {
		batch.begin(cam);
		particleSystem.update();
		particleBatch.setCamera(cam);
		particleSystem.begin();
		particleSystem.draw();
		particleSystem.end();
		batch.render(particleSystem);
		batch.end();
	}

	public void explodeParticle(int player, Vector3 position) {
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
	}

	@Override
	public void dispose() {
		particles1Hit.clear();
		particles2Hit.clear();
	}

}
