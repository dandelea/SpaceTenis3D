package tenis.objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class GameObject extends ModelInstance {
	public final String name;
	public final Vector3 center = new Vector3();
    public final Vector3 dimensions = new Vector3();
    public final float radius;
     
    private final static BoundingBox bounds = new BoundingBox();
     
    public GameObject(String name, Model model, String rootNode, boolean mergeTransform) {
        super(model, rootNode, mergeTransform);
        calculateBoundingBox(bounds);
        this.name = name;
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
        radius = dimensions.len() / 2f;
    }

	public GameObject(String name, Model model) {
		super(model);
		calculateBoundingBox(bounds);
		this.name = name;
        bounds.getCenter(center);
        bounds.getDimensions(dimensions);
        radius = dimensions.len() / 2f;
	}
}
