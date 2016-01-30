package com.automated.taxinow.utils;

public final class MathUtils {
	public static float degreesToRadians(float paramFloat) {
		return (float) (3.141592653589793D * paramFloat) / 180.0F;
	}

	public static float distance(float paramFloat1, float paramFloat2,
			float paramFloat3, float paramFloat4) {
		return (float) Math.sqrt(Math.pow(paramFloat1 - paramFloat3, 2.0D)
				+ Math.pow(paramFloat2 - paramFloat4, 2.0D));
	}

	public static float normalizeRadians(float paramFloat) {
		if ((paramFloat < 0.0F) || (paramFloat > 6.283185307179586D))
			paramFloat = (float) Math.abs(6.283185307179586D)
					- Math.abs(paramFloat);
		return paramFloat;
	}

	public static float radiansToDegrees(float paramFloat) {
		return 180.0F * paramFloat / 3.141593F;
	}

	public static String getRound(double d) {
		return String.format("%.1f", d);
	}
}