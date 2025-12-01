package br.com.pereiraeng.math.set;

public class RealInterval extends Interval<Double> {

	public RealInterval(double a, double b) {
		this(a, true, b, true);
	}

	public RealInterval(double a, boolean aep, double b, boolean bep) {
		super(new Double[] { a, b }, aep, bep);
	}

	@Override
	public String toString() {
		return String.format("%c%g;%g%c", isLowerOpen() ? '[' : ']', getLower(), getUpper(), isUpperOpen() ? ']' : '[');
	}
}
