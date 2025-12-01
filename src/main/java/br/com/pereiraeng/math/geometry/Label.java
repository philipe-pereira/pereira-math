package br.com.pereiraeng.math.geometry;

import java.awt.geom.Point2D;

public class Label extends Point2D.Float {
	private static final long serialVersionUID = 1L;

	private final String label;

	public Label(Point2D.Float p, String set) {
		super(p.x, p.y);
		this.label = set;
	}

	@Override
	public String toString() {
		return label;
	}
}
