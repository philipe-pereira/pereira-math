package br.com.pereiraeng.math.function;

import br.com.pereiraeng.math.Polynomial;
import br.com.pereiraeng.math.set.RealInterval;

public class CubicSplineSegment implements CurveSegment {

	private RealInterval interval;

	private double[] polynomial;

	public CubicSplineSegment(double lower, double upper, double[] polynomial) {
		this.interval = new RealInterval(lower, upper);
		this.polynomial = polynomial;
	}

	@Override
	public boolean containsX(double x) {
		return this.interval.isIn(x);
	}

	@Override
	public double y(double x) {
		return Polynomial.p(this.polynomial, x);
	}

	@Override
	public double getLower() {
		return interval.getLower();
	}

	@Override
	public double getUpper() {
		return interval.getUpper();
	}
}
