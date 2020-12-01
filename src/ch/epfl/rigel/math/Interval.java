package ch.epfl.rigel.math;

/**
 * representation of an interval
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public abstract class Interval {

	/**
	 * declaration of private final attributes
	 */
	private final double borneInf;
	private final double borneSup;

	/**
	 * constructs an interval with the given bounds
	 * @param a - lower bound
	 * @param b - upper bound
	 */
	protected Interval(double a, double b) {
		
		this.borneInf = a;
		this.borneSup = b;
	}

	/**
	 * getter for lower bound
	 * @return the lower bound
	 */
	public double low() {
		return borneInf;
	}

	/**
	 * getter for upper bound
	 * @return the upper bound
	 */
	public double high() {
		return borneSup;
	}

	/**
	 * gives difference between bounds
	 * @return size of the interval
	 */
	public double size() {
		return (borneSup-borneInf);
	}

	/**
	 * override of equals, is undefined
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * override of hashCode, is undefined
	 * @throws UnsupportedOperationException
	 */
	@Override
	public final int hashCode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * abstract method to be overridden by subclasses
	 * @param v - value
	 * @return true if v belongs to the interval, false otherwise
	 */
	public abstract boolean contains(double v);
}
