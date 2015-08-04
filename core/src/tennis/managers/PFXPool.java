package tennis.managers;

import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class PFXPool extends Pool<ParticleEffect> implements Disposable{
	private ParticleEffect sourceEffect;
	
	public PFXPool(ParticleEffect sourceEffect) {
		this.sourceEffect = sourceEffect;
	}

	public void free (ParticleEffect pfx){
		pfx.reset();
		super.free(pfx);
	}

	@Override
	protected ParticleEffect newObject() {
		return sourceEffect.copy();
	}

	public void dispose() {
		clear();
		sourceEffect.dispose();
	}

}
