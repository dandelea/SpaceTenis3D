package tennis.references;

import com.badlogic.gdx.utils.Array;

public class Models {
	
	public static String MODEL_AMBIENT = "models/ambient/ambient4.g3dj";
	public static String MODEL_TABLE = "models/table/table8.g3db";


	private static String[] models = {
        MODEL_AMBIENT,
        MODEL_TABLE
	};
	
	public static Array<String> MODELS = new Array<String>(models);
	
	public static void setAmbient(String name){
		switch (name) {
		case "Clean":
			MODEL_AMBIENT = "models/ambient/ambient1.g3dj";
			break;
		case "Office":
			MODEL_AMBIENT = "models/ambient/ambient2.g3dj";
			break;
		case "Park":
			MODEL_AMBIENT = "models/ambient/ambient3.g3dj";
			break;
		case "Space 1":
			MODEL_AMBIENT = "models/ambient/ambient4.g3dj";
			break;
		case "Space 2":
			MODEL_AMBIENT = "models/ambient/ambient5.g3dj";
			break;
		default:
			MODEL_AMBIENT = "models/ambient/ambient4.g3dj";
			break;
		}
		models[0] = MODEL_AMBIENT;
		MODELS = new Array<String>(models);
	}

}
