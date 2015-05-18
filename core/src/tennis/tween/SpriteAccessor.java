package tennis.tween;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteAccessor implements TweenAccessor<Sprite> {
	//Initial ALPHA value : DEACTIVATED
	public static final int ALPHA = 0;

	public int getValues(Sprite target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case ALPHA:
			returnValues[0] = target.getColor().a;
			// More than 1 value
			// returnValues[1] = tar
			return 1;
		default:
			assert false;
			return -1;
		}
	}

	public void setValues(Sprite target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case ALPHA:
			target.setColor(target.getColor().r, target.getColor().g,
					target.getColor().b, newValues[0]);
			break;
		default:
			assert false;
		}
	}

}
