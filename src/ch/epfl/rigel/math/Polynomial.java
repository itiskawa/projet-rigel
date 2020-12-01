package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

/**
 * A polynomial with real coefficients
 * @author Victor Borruat (300666)
 * @author Raphaël Selz (302980)
 */
public final class Polynomial {

	/**
	 * declaration of private final attribute
	 */
	private final double[] polynomial;

	/**
	 * private constructor
	 * @param p - coefficients of the polynomial in decreasing order
	 */
	private Polynomial(double[] p) {
		this.polynomial  = p;
	}

	/**
	 *
	 * @param coefficientN - coefficient of greatest power
	 * @param coefficients - rest of coefficients
	 * @return a polynomial with the given coefficients in decreasing order of powers
	 */
	public static Polynomial of(double coefficientN, double... coefficients) {
		Preconditions.checkArgument(coefficientN != 0);
		double[] polynomial = new double[coefficients.length + 1];
		polynomial[0] = coefficientN;
		System.arraycopy(coefficients, 0, polynomial, 1, coefficients.length);
		return new Polynomial(polynomial);
	}

	/**
	 *
	 * @param x - value at which the polynomial is evaluated
	 * @return the value of the polynomial for the numerical value x
	 */
	public double at(double x) {
		if (polynomial.length == 1) {
			return polynomial[0];
		}
		double result = x * polynomial[0];
		for(int i = 1; i < this.polynomial.length; ++i) {
			result = result + polynomial[i];
			if (i != polynomial.length - 1) {
				result *= x;
			}
		}
		return result;
	}

	/**
	 * redefinition of toString() method of Object class
	 * @return textual representation of the polynomial
	 */
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for(int i = 0; i < polynomial.length; ++i) {
			if(!(polynomial[i] == 0)) {
				if (i != 0) {
					out.append(polynomial[i]>0 ? "+" : "");
				}
				if (polynomial[i] == -1) {
					out.append('-');
				}
				else if (polynomial[i] != 1) {
					out.append(polynomial[i]);
				}
				if (polynomial.length - 1 - i == 1) {
					out.append('x');
				}
				else if (polynomial.length - 1 - i != 0){
					out.append("x^").append(polynomial.length-1-i);
				}
			}
		}
		return out.toString();
	}

	/**
	 * redefinition of equals
	 * @throws UnsupportedOperationException - can't compare real values
	 */
	@Override
	public boolean equals(Object o) { throw new UnsupportedOperationException(); }

	/**
	 * redefinition of hashCode
	 * @throws UnsupportedOperationException - can't compare real values
	 */
	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}
}
