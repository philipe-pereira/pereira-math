package br.com.pereiraeng.math.geometry;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import br.com.pereiraeng.core.ExtendedMath;
import br.com.pereiraeng.math.Vec;

public class Elipse {

	private Point2D.Double c;

	private double a, b;

	public Elipse(Double c, double a, double b) {
		this.c = c;
		this.a = a;
		this.b = b;
	}

	public Point2D.Double getC() {
		return c;
	}

	public double getA() {
		return a;
	}

	public double getB() {
		return b;
	}

	public static Point2D.Double[] getCenter(double x0, double y0, double x1, double y2, double axisX, double axisY,
			double axisXangle) {
		if (axisX == axisY) {
			// círculo! despreza 'axisXangle'
			return Circle.getCenter(x0, y0, x1, y2, axisX);
		} else {
			System.err.println("Tem de trabalhar mais...");
			return null;
		}
	}

	/**
	 * Função que retorna as informações sobre o arco de elipse que passa por dois
	 * dados pontos e possui tamanhos dados para seus dois eixos
	 * 
	 * @param x1         abscissa do primeiro ponto
	 * @param y1         ordenada do primeiro ponto
	 * @param x2         abscissa do segundo ponto
	 * @param y2         ordenada do segundo ponto
	 * @param rx         eixo principal da elipse
	 * @param ry         eixo secundário da elipse
	 * @param axisXangle ângulo do eixo primário com a horizontal
	 * @param large
	 * @param sweep
	 * @return vetor com quatro números decimais, segundo as duas primeiras posições
	 *         são a abscissa e a ordenada do centro da elipse, a terceira posição
	 *         com o ângulo inicial e a quarta com o ângulo do arco (essas duas
	 *         últimas grandeza dadas em radianos)
	 */
	public static double[] getArc(double x1, double y1, double x2, double y2, double rx, double ry, double axisXangle,
			boolean large, boolean sweep) {
		Point2D.Double[] c = Elipse.getCenter(x1, y1, x2, y2, rx, ry, axisXangle);

		double[] v1 = new double[] { x1 - c[0].x, y1 - c[0].y }, v2 = new double[] { x2 - c[0].x, y2 - c[0].y };

		double det1 = Vec.det2(new double[][] { v1, v2 });
		int ci; // centro a ser utilizado
		if (large ^ sweep)
			ci = det1 >= 0 ? 0 : 1;
		else
			ci = det1 >= 0 ? 1 : 0;

		// ponto de partida para desenhar o ângulo inicial
		double angStart = Math.atan2((sweep ? y1 : y2) - c[ci].y, (sweep ? x1 : x2) - c[ci].x);

		// ângulo maior ou menor
		double ang = Vec.getAngle(v1, v2);
		if (ang == Math.PI) { // menos ou mais?
			if (ExtendedMath.isMinusZero(det1))
				ang *= -1.;
		} else if (ang > Math.PI ^ large)
			ang = ExtendedMath.TWO_PI - ang;

		return new double[] { c[ci].x, c[ci].y, angStart, ang };
	}
}
