package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * a right-open interval
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class RightOpenInterval extends Interval {

	/**
	 * private constructor
	 * @param a - lower bound
	 * @param b - upper bound
	 */
	private RightOpenInterval(double a, double b) {
		super(a,b);
	}

	/**
	 * creates a right-open interval using the given bounds
	 * @param low
	 * 		lower bound
	 * @param high
	 * 		upper bound
	 * @return the right-open interval
	 * @throws IllegalArgumentException if low >= high
	 */
	public static RightOpenInterval of(double low, double high) {
		Preconditions.checkArgument(low < high);
		return new RightOpenInterval(low, high);
	}

	/**
	 * creates a right-open interval of the given size centered around 0
	 * @param size
	 * 		size of the interval
	 * @return right-open interval centered around 0
	 * @throws IllegalArgumentException if the size isn't strictly positive
	 */
	public static RightOpenInterval symmetric(double size) {
		Preconditions.checkArgument(size > 0);
			return new RightOpenInterval(-(size / 2.0), (size / 2.0));
	}

	/**
	 * reduces the value to the interval
	 * @param v
	 * 		value to reduce
	 * @return reduced value
	 */
	public double reduce(double v) {
		double a = v - this.low();
		double b = this.size();
		return this.low() + (a - b * (Math.floor(a/b)));
	}

	/**
	 * checks if the given value belongs to the interval or not
	 * @param v
	 * 		value to be checked
	 * @return true if it belongs, false otherwise
	 */
	@Override
	public boolean contains(double v) {
		return (v >= this.low() && v < this.high());
	}

	/**
	 * textual representation of the right-open interval
	 * @return string representation of the interval
	 */
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "[%f , %f[",
				this.low(), this.high());
	}
}