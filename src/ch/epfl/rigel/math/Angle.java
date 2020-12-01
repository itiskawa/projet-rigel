package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * Tool class representing angles & their manipulations
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class Angle {

	/**
	 * declaration & initialisation of useful constants
	 */
	public static final double TAU = Math.PI * 2.0;
	private static final RightOpenInterval POSITIVE_ANGLES = RightOpenInterval.of(0, TAU);
	private static final RightOpenInterval RANGE_TO_60 = RightOpenInterval.of(0, 60);

	/**
	 * private default constructor
	 */
	private Angle() {}


	/**
	 * reduces a given angle in radians to the corresponding angle in radians in the interval [0, 2PI[
	 * @param rad - angle in radians to be reduced
	 * @return the reduced angle
	 */
	public static double normalizePositive(double rad) {
		return POSITIVE_ANGLES.reduce(rad);
	}

	/**
	 * converts arcseconds to radians
	 * @param sec - arcseconds, can be negative
	 * @return corresponding angle in rad
	 */
	public static double ofArcsec(double sec) {
		double min = sec / 60.0;
		double deg = min / 60.0;
		return Angle.ofDeg(deg);
	}

	/**
	 * converts angle in deg, min, sec to radians
	 * @param deg - number (integer) of degrees
	 * @param min - number (integer) of arcminutes
	 * @param sec - real number of arcseconds
	 * @return corresponding angle in rad
	 */
	public static double ofDMS(int deg,int min, double sec) {
		Preconditions.checkArgument(deg>=0);
		Preconditions.checkArgument(RANGE_TO_60.contains(min) && RANGE_TO_60.contains(sec));
		double a = 1.0 / 60.0;
		double b = a / 60.0;
		return Angle.ofDeg(deg + a*min + b*sec);
	}

	/**
	 * converts degrees to radians
	 * @param deg - real number of degrees
	 * @return corresponding angle in rad
	 */
	public static double ofDeg(double deg) { 
		return Math.toRadians(deg);
	}

	/**
	 * converts radians to degrees
	 * @param rad - angle to convert in radians
	 * @return corresponding angle in degrees
	 */
	public static double toDeg(double rad) {
		return Math.toDegrees(rad);
	}

	/**
	 * converts hours to radians
	 * @param hr - real number of hours
	 * @return corresponding angle in rad
	 */
	public static double ofHr(double hr) {
		return Angle.ofDeg(15 * hr);
	}

	/**
	 * converts radians to hours
	 * @param rad - real number of rad
	 * @return corresponding angle in hours
	 */
	public static double toHr(double rad) {
		return Angle.toDeg(rad) / 15;
	}
}