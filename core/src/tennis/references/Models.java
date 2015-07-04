package tennis.references;

import com.badlogic.gdx.utils.Array;

public class Models {
	
	public static String MODEL_AMBIENT = "models/ambient/ambient1.g3dj";
	public static String MODEL_TABLE = "models/table/table6.g3db";


	private static String[] models = {
        MODEL_AMBIENT,
        MODEL_TABLE
	};
	
	public static Array<String> MODELS = new Array<String>(models);

}
