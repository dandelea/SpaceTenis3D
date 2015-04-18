package tenis.objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Disposable;

public class GameObject extends ModelInstance implements Disposable{
	public final String name;
	public final btCollisionObject body;
	public boolean moving;
	public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();
    public final float radius;
     
    private final static BoundingBox bounds = new BoundingBox();
     
    public GameObject(String name, Model model, String rootNode, boolean mergeTransform, btCollisionShape shape) {
        super(model, rootNode, mergeTransform);
        calculateBoundingBox(bounds);
        this.name = name;
        body = new btCollisionObject();
		body.setCollisionShape(shape);
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
        radius = dimensions.len() / 2f;
    }

	public GameObject(String name, Model model, btCollisionShape shape) {
		super(model);
		calculateBoundingBox(bounds);
		this.name = name;
		body = new btCollisionObject();
		body.setCollisionShape(shape);
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
        radius = dimensions.len() / 2f;
	}

	@Override
	public void dispose() {
		body.dispose();
	}

}
