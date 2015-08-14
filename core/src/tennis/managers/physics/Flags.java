package tennis.managers.physics;

/**
 * Physics collision flags.
 * 
 * @see tennis.managers.Tools#spawn(com.badlogic.gdx.utils.ArrayMap,
 *      com.badlogic.gdx.utils.Array,
 *      com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld)
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class Flags {
	public final static short GROUND_FLAG = 1 << 8;
	public final static short OBJECT_FLAG = 1 << 9;
	public final static short ALL_FLAG = -1;
}
