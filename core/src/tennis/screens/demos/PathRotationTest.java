package tennis.screens.demos;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.steer.utils.Path;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.*;

public class PathRotationTest implements Screen {
	PerspectiveCamera cam;
	CameraInputController inputController;
	ModelBatch modelBatch;
	Model model;
	Array<ModelInstance> instances;
	Environment environment;
	ModelInstance arrow;
	CatmullRomSpline path;
	
	final float GRID_MIN = -10f;
	final float GRID_MAX = 10f;
	final float GRID_STEP = 1f;
 
	@Override
	public void show() {
		modelBatch = new ModelBatch();
		
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
				-0.8f, -0.2f));
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0, 1, 0);
		cam.near = 0.1f;
		cam.far = 300f;
		cam.update();
 
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		
		Texture texture = new Texture(Gdx.files.internal("badlogic.jpg"));
		modelBuilder.manage(texture);
		
		modelBuilder.node().id = "arrow";
		modelBuilder.part("arrow", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.TextureCoordinates,
			new Material("diffuse-map", TextureAttribute.createDiffuse(texture)))
			.arrow(0, 0, 0, 2, 0, 0, 0.5f, 0.5f, 10);
		
		modelBuilder.node().id = "gridAndAxes";
		MeshPartBuilder builder = modelBuilder.part("grid", GL20.GL_LINES, Usage.Position | Usage.Color, new Material());
		builder.setColor(Color.LIGHT_GRAY);
		for (float t = GRID_MIN; t <= GRID_MAX; t += GRID_STEP) {
			builder.line(t, 0, GRID_MIN, t, 0, GRID_MAX);
			builder.line(GRID_MIN, 0, t, GRID_MAX, 0, t);
		}
		
		builder = modelBuilder.part("axes", GL20.GL_LINES, Usage.Position | Usage.Color, new Material());
		builder.setColor(Color.RED);
		builder.line(0, 0, 0, 100, 0, 0);
		builder.setColor(Color.GREEN);
		builder.line(0, 0, 0, 0, 100, 0);
		builder.setColor(Color.BLUE);
		builder.line(0, 0, 0, 0, 0, 100);
		
		model = modelBuilder.end();
		
		instances = new Array<ModelInstance>();
		instances.add(new ModelInstance(model, "gridAndAxes"));
		instances.add(arrow = new ModelInstance(model, "arrow"));
		
		Gdx.input.setInputProcessor(inputController = new CameraInputController(cam));
		
		path = new CatmullRomSpline(new Vector3[] {
			new Vector3(0, 0, 0),
			new Vector3(5, 1, 0),
			new Vector3(5, 3, -5),
			new Vector3(-5, 3, -5),
			new Vector3(-5, 5, 5),
			new Vector3(0, 2, 5)
		}, true);
	}
 
	float t = 0;
	Vector3 position = new Vector3();
	Vector3 direction = new Vector3();
	Vector3 right = new Vector3();
	Vector3 up = new Vector3();
	boolean directionOnly = false;
	@Override
	public void render (float delta) {
		t = (t + delta * 0.25f) % 1f;
		path.derivativeAt(direction, t);
		path.valueAt(position, t);
		direction.nor();
		if (directionOnly) {
			arrow.transform.setToRotation(Vector3.X, direction);
			arrow.transform.setTranslation(position);
		} else {
			right.set(Vector3.Y).crs(direction).nor();
			up.set(right).crs(direction).nor();
			arrow.transform.set(direction, up, right, position).rotate(Vector3.X, 180);
		}
		
		inputController.update();
 
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
 
		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}
 
	@Override
	public void dispose () {
		modelBatch.dispose();
		model.dispose();
	}
 
	public void resume () {
	}
 
	public void resize (int width, int height) {
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.update();
	}
 
	public void pause () {
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
}