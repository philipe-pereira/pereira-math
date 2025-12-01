package br.com.pereiraeng.math.geometry;

import java.awt.geom.Point2D;

import br.com.pereiraeng.core.ExtendedMath;

/**
 * Classe do objeto que representa um arco de círculo
 * 
 * @author Philipe PEREIRA
 *
 */
public class Arc extends Circle {

	private double startAngle;

	private double arcAngle;

	/**
	 * Construtor do arco
	 * 
	 * @param c          ponto do centro
	 * @param r          raio
	 * @param startAngle ângulo inicial, em radianos
	 * @param arcAngle   ângulo do arco, no sentido horário, em radianos
	 */
	public Arc(Point2D.Double c, double r, double startAngle, double arcAngle) {
		super(c, r);
		this.startAngle = ExtendedMath.circularRadians(startAngle);
		this.arcAngle = arcAngle;
	}

	/**
	 * 
	 * @return ângulo inicial, em radianos
	 */
	public double getStartAngle() {
		return startAngle;
	}

	/**
	 * 
	 * @return ângulo do arco, no sentido horário, em radianos
	 */
	public double getArcAngle() {
		return arcAngle;
	}

	public Point2D.Double getMin() {
		double sa, ea;
		if (arcAngle > 0) {
			sa = startAngle;
			ea = ExtendedMath.circularRadians(startAngle + arcAngle);
		} else {
			sa = ExtendedMath.circularRadians(startAngle + arcAngle);
			ea = startAngle;
		}

		double xm, ym;
		if (sa < Math.PI && Math.PI < ea)
			xm = getC().x - getR();
		else
			xm = Math.min(Math.cos(sa), Math.cos(ea));
		if (startAngle < ExtendedMath.PI3_2 && ExtendedMath.PI3_2 < ea)
			ym = getC().y - getR();
		else
			ym = Math.min(Math.sin(sa), Math.sin(ea));

		return new Point2D.Double(xm, ym);
	}

	public Point2D.Double getMax() {
		double sa, ea;
		if (arcAngle > 0) {
			sa = startAngle;
			ea = ExtendedMath.circularRadians(startAngle + arcAngle);
		} else {
			sa = ExtendedMath.circularRadians(startAngle + arcAngle);
			ea = startAngle;
		}

		double xM, yM;
		if (sa < 0 && 0 < ea)
			xM = getC().x + getR();
		else
			xM = Math.max(Math.cos(sa), Math.cos(ea));
		if (startAngle < ExtendedMath.PI_2 && ExtendedMath.PI_2 < ea)
			yM = getC().y + getR();
		else
			yM = Math.max(Math.sin(sa), Math.sin(ea));

		return new Point2D.Double(xM, yM);
	}
}
