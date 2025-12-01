package br.com.pereiraeng.math.geometry;

import java.awt.geom.Point2D;

import br.com.pereiraeng.math.Vec;
import br.com.pereiraeng.core.ExtendedMath;

/**
 * Classe do objeto que representa um círculo
 * 
 * @author Philipe PEREIRA
 *
 */
public class Circle {

	private Point2D.Double c;

	private double r;

	/**
	 * Construtor do objeto que representa um círculo que é circunscrito em um
	 * triângulo
	 * 
	 * @param tri objeto que representa o triângulo
	 */
	public Circle(Triangle tri) {
		this(tri.getVertice(0), tri.getVertice(1), tri.getVertice(2));
	}

	/**
	 * Construtor do objeto que representa um círculo a partir de três de seus
	 * pontos
	 * 
	 * @param p1 primeiro dos pontos
	 * @param p2 segundo ponto
	 * @param p3 último ponto
	 */
	public Circle(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
		this.c = new Point2D.Double();
		this.r = Math.abs(getCircle(p1, p2, p3, this.c));
	}

	public Circle(Point2D.Double c, double r) {
		this.c = c;
		this.r = r;
	}

	public Point2D.Double getC() {
		return c;
	}

	public double getR() {
		return r;
	}

	public boolean hasInside(Point2D.Double v) {
		return Math.hypot(v.x - c.x, v.y - c.y) < r;
	}

	/**
	 * ver <a href=
	 * "https://www.qc.edu.hk/math/Advanced%20Level/circle%20given%203%20points.htm">Equation
	 * of circle passing through 3 given points</a>
	 * <ol>
	 * <li value="3">The perpendicular bisectors of two chords meet at the
	 * centre;</i>
	 * <li>Converse of Angle in semi-circle;</i>
	 * <li value="7">Determinant method.</i>
	 * </ol>
	 */
	private static final int METHOD = 4;

	/**
	 * Função que retorna o raio e o ponto do centro de um círculo que passa por
	 * três pontos dados
	 * 
	 * @param p1     primeiro dos pontos
	 * @param p2     segundo ponto
	 * @param p3     último ponto
	 * @param center objeto representando o ponto do centro do círculo, onde as
	 *               coordenadas serão inseridas
	 * @return raio do círculo ({@link Double#POSITIVE_INFINITY infinito} se os três
	 *         pontos dados forem colineares, número real positivo se os pontos
	 *         dados estiverem em sentido anti-horário, número real negativo se os
	 *         pontos estiverem em sentido horário)
	 */
	public static double getCircle(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3, Point2D.Double center) {
		switch (METHOD) {
		case 3: // bissetriz
			Point2D.Double ma = new Point2D.Double(), mb = new Point2D.Double();
			double ga = 0., gb = 0.;
			if (p1.y != p2.y) {
				ga = Line.getBisector(p1, p2, ma);
				if (p2.y != p3.y)
					gb = Line.getBisector(p2, p3, mb);
				else
					gb = Line.getBisector(p3, p1, mb);
			} else {
				ga = Line.getBisector(p2, p3, ma);
				gb = Line.getBisector(p3, p1, mb);
			}

			center.x = (-ma.y + mb.y + ma.x * ga - mb.x * gb) / (ga - gb);
			center.y = ga * (center.x - ma.x) + ma.y;
			return Polygon.clockwise(p1, p2, p3) * Math.hypot(p1.x - center.x, p1.y - center.y);
		case 4: // Converse of Angle in semi-circle.
			Point2D.Double p = null;
			if (p1.y != p2.y) {
				ga = (p2.x - p1.x) / (p1.y - p2.y);
				if (p1.y != p3.y) {
					gb = (p1.x - p3.x) / (p3.y - p1.y);
					ma = p2;
					mb = p3;
					p = p1;
				} else {
					gb = (p3.x - p2.x) / (p2.y - p3.y);
					ma = p1;
					mb = p3;
					p = p2;
				}
			} else {
				ga = (p1.x - p3.x) / (p3.y - p1.y);
				gb = (p3.x - p2.x) / (p2.y - p3.y);
				ma = p1;
				mb = p2;
				p = p3;
			}

			double sx = (-ma.y + mb.y + ma.x * ga - mb.x * gb) / (ga - gb);
			double sy = ga * (sx - ma.x) + ma.y;
			center.setLocation(.5 * (sx + p.x), .5 * (sy + p.y));
			return Polygon.clockwise(p1, p2, p3) * Math.hypot(p1.x - center.x, p1.y - center.y);
		case 7: // Determinant method
			double a = Vec.det3(new double[][] { { p1.x, p1.y, 1. }, { p2.x, p2.y, 1. }, { p3.x, p3.y, 1. } });
			if (a == 0.)
				return Double.POSITIVE_INFINITY;

			double x12y12 = Math.pow(p1.x, 2) + Math.pow(p1.y, 2);
			double x22y22 = Math.pow(p2.x, 2) + Math.pow(p2.y, 2);
			double x32y32 = Math.pow(p3.x, 2) + Math.pow(p3.y, 2);

			double bx = -Vec.det3(new double[][] { { x12y12, p1.y, 1. }, { x22y22, p2.y, 1. }, { x32y32, p3.y, 1. } });
			double by = Vec.det3(new double[][] { { x12y12, p1.x, 1. }, { x22y22, p2.x, 1. }, { x32y32, p3.x, 1. } });
			double c = -Vec
					.det3(new double[][] { { x12y12, p1.x, p1.y }, { x22y22, p2.x, p2.y }, { x32y32, p3.x, p3.y } });

			center.setLocation(-bx / 2. / a, -by / 2. / a);
			return Math.sqrt(Math.pow(bx, 2) + Math.pow(by, 2) - 4 * a * c) / (2. * a);
		}
		return 0.;
	}

	/**
	 * Função que localiza os dois centros dos dois círculo que passam por dois
	 * pontos e tem um dado raio
	 * 
	 * @param x0 abscissa do ponto 1
	 * @param y0 ordenada do ponto 1
	 * @param x1 abscissa do ponto 2
	 * @param y1 ordenada do ponto 2
	 * @param r  raio
	 * @return par de pontos com as coordenadas dos dois possíveis centros
	 */
	public static Point2D.Double[] getCenter(double x0, double y0, double x1, double y1, double r) {
		double q = Point2D.distance(x0, y0, x1, y1);
		Point2D.Double mid = new Point2D.Double((x0 + x1) * 0.5, (y0 + y1) * 0.5);

		double sqx = Math.sqrt(r * r - Math.pow(q / 2, 2)) * (y0 - y1) / q;
		double sqy = Math.sqrt(r * r - Math.pow(q / 2, 2)) * (x1 - x0) / q;
		return new Point2D.Double[] { new Point2D.Double(mid.x + sqx, mid.y + sqy),
				new Point2D.Double(mid.x - sqx, mid.y - sqy) };
	}

	public static Point2D.Double[] circleCircleIntersection(double x0, double y0, double r0, double x1, double y1,
			double r1) {
		// dx and dy are the vertical and horizontal distances between the circle
		// centers.
		double dx = x1 - x0;
		double dy = y1 - y0;

		// Determine the straight-line distance between the centers.
		// d = sqrt((dy*dy) + (dx*dx));
		double d = Math.hypot(dx, dy); // Suggested by Keith Briggs

		// Check for solvability.
		if (d > (r0 + r1)) // no solution. circles do not intersect.
			return new Point2D.Double[0];

		if (d < Math.abs(r0 - r1)) // no solution. one circle is contained in the other
			return new Point2D.Double[0];

		// 'point 2' is the point where the line through the circle intersection points
		// crosses the line between the circle centers.

		// Determine the distance from point 0 to point 2.
		double a = ((r0 * r0) - (r1 * r1) + (d * d)) / (2.0 * d);

		// Determine the coordinates of point 2.
		double x2 = x0 + (dx * a / d);
		double y2 = y0 + (dy * a / d);

		// Determine the distance from point 2 to either of the intersection points.
		double h = Math.sqrt((r0 * r0) - (a * a));

		// Now determine the offsets of the intersection points from point 2.
		double rx = -dy * (h / d);
		double ry = dx * (h / d);

		// Determine the absolute intersection points.
		return new Point2D.Double[] { new Point2D.Double(x2 + rx, y2 + ry), new Point2D.Double(x2 - rx, y2 - ry) };
	}

	public static Point2D.Double[] circleLineIntersection(double x0, double y0, double r0, double al, double bl) {
		double a = 1 + al * al;
		double b = -2 * x0 + 2 * al * bl - 2 * al * y0;
		double c = x0 * x0 + bl * bl + y0 * y0 - r0 * r0 - 2 * bl * y0;

		double[] xs = ExtendedMath.bhaskara(a, b, c);

		return new Point2D.Double[] { new Point2D.Double(xs[0], al * xs[0] + bl),
				new Point2D.Double(xs[1], al * xs[1] + bl) };
	}
}
