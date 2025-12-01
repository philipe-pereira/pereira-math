package br.com.pereiraeng.math.geometry;

import java.awt.geom.Point2D;

public class Rectangle {

	private final Point2D.Double m, M;

	public Rectangle(Point2D.Double m, Point2D.Double M) {
		this.m = m;
		this.M = M;
	}

	public Point2D.Double getMin() {
		return m;
	}

	public Point2D.Double getMax() {
		return M;
	}
}
