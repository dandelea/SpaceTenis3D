package tenis.objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Disposable;

public class RigidGameObject extends ModelInstance implements Disposable{
	public final btRigidBody body;
    public boolean moving;

    public RigidGameObject (Model model, String node, btRigidBody.btRigidBodyConstructionInfo constructionInfo) {
        super(model, node);
        body = new btRigidBody(constructionInfo);
    }

    @Override
    public void dispose () {
        body.dispose();
    }
}