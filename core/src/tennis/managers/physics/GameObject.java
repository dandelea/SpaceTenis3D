package tennis.managers.physics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

/**
 * Model Instance subclass that add some methods for easy management in the game
 * scene.
 * 
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class GameObject extends ModelInstance implements Disposable {

	// PHYSICS
	private final Vector3 center = new Vector3();
	private final Vector3 dimensions = new Vector3();
	private final float radius;
	public final static BoundingBox bounds = new BoundingBox();
	private final Vector3 position = new Vector3();
	public final btRigidBody body;
	public final MyMotionState motionState;

	// BALL
	public int lastPlayer;
	public int bounces;
	public boolean hitted;
	public boolean disposed;
	public static final int MAX_BOUNCES = 50;

	public GameObject(Model model, String rootNode,
			btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
		super(model, rootNode);
		calculateBoundingBox(bounds);
		bounds.getCenter(center);
		bounds.getDimensions(dimensions);
		radius = dimensions.len() / 2f;
		motionState = new MyMotionState();
		motionState.transform = transform;
		body = new btRigidBody(constructionInfo);
		body.setMotionState(motionState);
		bounces = 0;
		disposed = false;
	}

	public Vector3 getPosition() {
		transform.getTranslation(position);
		return position;
	}

	public BoundingBox getBoundingBox() {
		calculateBoundingBox(bounds);
		return bounds;
	}

	public boolean isVisible(Camera cam) {
		return cam.frustum.sphereInFrustum(transform.getTranslation(position)
				.add(center), radius);
	}

	/**
	 * @return -1 on no intersection, or when there is an intersection: the
	 *         squared distance between the center of this object and the point
	 *         on the ray closest to this object when there is intersection.
	 */
	public float intersects(Ray ray) {
		transform.getTranslation(position).add(center);
		final float len = ray.direction.dot(position.x - ray.origin.x,
				position.y - ray.origin.y, position.z - ray.origin.z);
		if (len < 0f)
			return -1f;
		float dist2 = position.dst2(ray.origin.x + ray.direction.x * len,
				ray.origin.y + ray.direction.y * len, ray.origin.z
						+ ray.direction.z * len);
		return (dist2 <= radius * radius) ? dist2 : -1f;
	}

	@Override
	public void dispose() {
		body.dispose();
		motionState.dispose();
	}

}
