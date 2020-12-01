package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import java.util.Locale;

/**
 * a closed interval [a,b]
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class ClosedInterval extends Interval{

	/**
	 * private constructor
	 * @param a - lower bound
	 * @param b - upper bound
	 */
	private ClosedInterval(double a, double b) {
		super(a, b);
	}

	/**
	 * creates a closed interval with the given bounds
	 * @param low - lower bound
	 * @param high - upper bound
	 * @return the new closed interval
	 * @throws IllegalArgumentException if low >= high
	 */
	public static ClosedInterval of(double low, double high) {
		Preconditions.checkArgument(low < high);
		return new ClosedInterval(low, high);
	}

	/**
	 * returns a closed interval of the given size centered around 0
	 * @param size - size of the interval to be created
	 * @return the closed interval
	 * @throws IllegalArgumentException if the size isn't strictly positive
	 */
	public static ClosedInterval symmetric(double size) {
		Preconditions.checkArgument(size > 0);
		return new ClosedInterval(-(size / 2.0), (size / 2.0));
	}

	/**
	 * checks if a value belongs to the closed interval
	 * @param v - value to be checked
	 * @return true if it is in, false otherwise
	 */
	@Override
	public boolean contains(double v) {
		return (v <= high() && v >= low());
	}

	/**
	 * clips the given value using the closed interval
	 * @param v - value to be clipped
	 * @return v if it is in .this, or the nearest bound of .this
	 */
	public double clip(double v) {
		if(this.contains(v)) {
			return v;
		}
		else if(v > this.high()) {
			return this.high();
		}
		else{
			return this.low();
		}
	}

	/**
	 * textual representation of the closed interval
	 * @return string representation of the closed interval
	 */
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "[%f , %f]",
				this.low(), this.high());

	}
}