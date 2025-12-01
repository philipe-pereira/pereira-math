package br.com.pereiraeng.math.geometry;

import java.awt.geom.Point2D;

import br.com.pereiraeng.core.ExtendedMath;

/**
 * Classe dos objetos que representam os ângulos de um sistema de coordenadas
 * polares, sendo tais ângulos expressos em <strong>graus</strong>
 * 
 * @author Philipe PEREIRA
 * 
 */
public abstract class Coordinate extends Point2D {

	public static class Float extends Point2D.Float {
		private static final long serialVersionUID = -3313671880508843261L;

		public Float() {
		}

		public Float(float theta, float phi) {
			setLocation(ExtendedMath.circularDegree(theta), ExtendedMath.circularDegree(phi));
		}

		public Float(double theta, double phi) {
			this((float) ExtendedMath.circularDegree(theta), (float) ExtendedMath.circularDegree(phi));
		}

		public Float(double[] thetaPhi) {
			this(thetaPhi[0], thetaPhi[1]);
		}

		public Float(Point2D.Float point) {
			this(point.x, point.y);
		}
	}

	public static class Double extends Point2D.Double {
		private static final long serialVersionUID = 1736709945385600756L;

		public Double(double theta, double phi) {
			setLocation(theta, phi);
		}

		public Double(double[] thetaPhi) {
			this((float) thetaPhi[0], (float) thetaPhi[1]);
		}

		public Double(Point2D.Double point) {
			this(point.x, point.y);
		}
	}
}