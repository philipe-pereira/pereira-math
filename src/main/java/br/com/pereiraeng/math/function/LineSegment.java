package br.com.pereiraeng.math.function;

import br.com.pereiraeng.math.set.RealInterval;

public class LineSegment implements LinearSegment {

	private RealInterval interval;

	private double slope, intercept;

	public LineSegment(double lower, double upper, double slope, double intercept) {
		this.interval = new RealInterval(lower, upper);
		this.slope = slope;
		this.intercept = intercept;
	}

	@Override
	public boolean containsX(double restraint) {
		return interval.isIn(restraint);
	}

	@Override
	public double y(double restraint) {
		return slope * restraint + intercept;
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
