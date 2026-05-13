package br.com.pereiraeng.math.function;

import br.com.pereiraeng.math.set.RealInterval;

public class HorizontalSegment implements LinearSegment {

	private RealInterval interval;

	private double value;

	public HorizontalSegment(double lower, double upper, double value) {
		this.interval = new RealInterval(lower, upper);
		this.value = value;
	}

	@Override
	public boolean containsX(double restraint) {
		return this.interval.isIn(restraint);
	}

	@Override
	public double y(double restraint) {
		return this.value;
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
