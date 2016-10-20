package com.guardian.game.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class VectorUtil {

	/**
	 * vcetor1 - vector2。不影响原值
	 * @param vcetor1
	 * @param vector2
	 * @return 新的Vector2
	 */
	public static Vector2 sub(Vector2 vcetor1, Vector2 vector2){
		return new Vector2(vcetor1).sub(vector2);
	}
	
	/**
	 * vcetor1 - vector2。不影响原值
	 * 
	 * @param vcetor1
	 * @param vector2
	 * @return 新的Vector2
	 */
	public static Vector2 subVector2(Vector3 vcetor1, Vector3 vector2){
		return sub(toVector2(vcetor1), toVector2(vector2));
	}
	
	public static Vector2 toVector2(Vector3 vector3){
		return new Vector2(vector3.x, vector3.y);
	}
	
	public static Vector2 scl (Vector2 vector, float scalar) {
		return new Vector2(vector).scl(scalar);
	}
	
	//TODO 要一个Vector2的池化，new的地方太多了 
}
