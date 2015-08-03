package tennis.managers.physics;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;

public class Ball extends GameObject{
	public int lastPlayer;
	public boolean bounced;
	public boolean hitted;

	public Ball(Model model, String rootNode,
			btRigidBodyConstructionInfo constructionInfo) {
		super(model, rootNode, constructionInfo);
	}
	
	

}
