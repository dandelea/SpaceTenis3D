package tennis.managers;

import com.badlogic.gdx.utils.Array;

public class Models {
	
	public static String MODEL_AMBIENT = "models/ambient/space1.g3dj";
	public static String MODEL_TABLE = "models/table/table8.g3db";


	private static String[] models = {
        MODEL_AMBIENT,
        MODEL_TABLE
	};
	
	public static Array<String> MODELS = new Array<String>(models);
	
	public static void setAmbient(String name){
		switch (name) {
		case "Space 1":
			MODEL_AMBIENT = "models/ambient/space1.g3dj";
			break;
		case "Space 2":
			MODEL_AMBIENT = "models/ambient/space2.g3dj";
			break;
		case "Space 3":
			MODEL_AMBIENT = "models/ambient/space3.g3dj";
			break;
		case "Clean":
			MODEL_AMBIENT = "models/ambient/clean.g3dj";
			break;
		default:
			MODEL_AMBIENT = "models/ambient/space1.g3dj";
			break;
		}
		models[0] = MODEL_AMBIENT;
		MODELS = new Array<String>(models);
	}

}
