package tennis.managers;

import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

/**
 * Particle Effects Pool for creating, managing and disposing them in the game
 * scene. This method saves memory space.
 * 
 * @see <a
 *      href="https://github.com/libgdx/libgdx/wiki/3D-Particle-Effects">Libgdx
 *      - 3D Particle Effects</a>
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class PFXPool extends Pool<ParticleEffect> implements Disposable {
	private ParticleEffect sourceEffect;

	public PFXPool(ParticleEffect sourceEffect) {
		this.sourceEffect = sourceEffect;
	}

	public void free(ParticleEffect pfx) {
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
