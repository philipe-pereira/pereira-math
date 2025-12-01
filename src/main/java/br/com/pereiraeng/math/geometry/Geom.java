package br.com.pereiraeng.math.geometry;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import br.com.pereiraeng.math.Vec;

/**
 * Classe de funções geométricas
 * 
 * @author Philipe PEREIRA
 *
 */
public class Geom {

	/**
	 * Função que retorna a área do(s) paralelogramo(s) definido(s) por três de seus
	 * vértices (dito de outra maneira, a área do paralelogramo definido por dois
	 * vetores com um vértice em comum)
	 * 
	 * @param x1 abscissa do vértice em comum dos vetores
	 * @param y1 ordenada do vértice em comum dos vetores
	 * @param x2 abscissa do vértice oposto ao comum
	 * @param y2 ordenada do vértice oposto ao comum
	 * @param x3 abscissa do vértice oposto ao comum do outro vetor
	 * @param y3 ordenada do vértice oposto ao comum do outro vetor
	 * @return
	 */
	public static double area(double x1, double y1, double x2, double y2, double x3, double y3) {
		return Vec.det2(x2 - x1, y2 - y1, x3 - x1, y3 - y1);
	}

	/**
	 * Função que retorna a abscissa para a qual uma reta (definida por 2 pontos)
	 * tem como ordenada um dado valor
	 * 
	 * @param p1 ponto 1 que determina a reta
	 * @param p2 ponto 2 que determina a reta
	 * @param y  ordenada
	 * @return abscissa para a qual se tem a ordenada dada ({@link Double#NaN} se a
	 *         reta for horizontal)
	 */
	public static double retaX(Point2D.Double p1, Point2D.Double p2, double y) {
		double dy = p1.y - p2.y;
		if (dy == 0.)
			return Double.NaN;
		else
			return (p2.x * p1.y + p1.x * y - p1.x * p2.y - p2.x * y) / dy;
	}

	/**
	 * Função que retorna a ordenada para a qual uma reta (definida por 2 pontos)
	 * tem como abscissa um dado valor
	 * 
	 * @param p1 ponto 1 que determina a reta
	 * @param p2 ponto 2 que determina a reta
	 * @param x  abscissa
	 * @return ordenada para a qual se tem a abscissa dada ({@link Double#NaN} se a
	 *         reta for vertical)
	 */
	public static double retaY(Point2D.Double p1, Point2D.Double p2, double x) {
		double dx = p2.x - p1.x;
		if (dx == 0.)
			return Double.NaN;
		else
			return (p2.x * p1.y + x * p2.y - x * p1.y - p1.x * p2.y) / dx;
	}

	public static boolean collinear(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
		return Vec.det2(p2.x - p1.x, p2.y - p1.y, p3.x - p1.x, p3.y - p1.y) == 0.;
	}

	public static boolean collinear(Point2D.Double[] e, Point2D.Double p) {
		return Vec.det2(e[1].x - e[0].x, e[1].y - e[0].y, p.x - e[0].x, p.y - e[0].y) == 0.;
	}

	/**
	 * Função que calcula os coeficientes da equação do plano definido por três
	 * pontos no espaço
	 * 
	 * @param p1 vetor com as coordenadas do primeiro ponto
	 * @param p2 vetor com as coordenadas do segundo ponto
	 * @param p3 vetor com as coordenadas do terceiro ponto
	 * @return vetor {a,b,c} contendo os coeficientes da equação do plano: z =
	 *         ax+by+c
	 */
	public static double[] getPlano(double[] p1, double[] p2, double[] p3) {
		return Geom.getPlano(p1[0], p1[1], p1[2], p2[0], p2[1], p2[2], p3[0], p3[1], p3[2]);
	}

	/**
	 * Função que calcula os coeficientes da equação do plano definido por três
	 * pontos no espaço
	 * 
	 * @param x1 coordenada x do primeiro ponto
	 * @param y1 coordenada y do primeiro ponto
	 * @param z1 coordenada z do primeiro ponto
	 * @param x2 coordenada x do segundo ponto
	 * @param y2 coordenada y do segundo ponto
	 * @param z2 coordenada z do segundo ponto
	 * @param x3 coordenada x do terceiro ponto
	 * @param y3 coordenada y do terceiro ponto
	 * @param z3 coordenada z do terceiro ponto
	 * @return vetor {a,b,c} contendo os coeficientes da equação do plano: z =
	 *         ax+by+c
	 */
	public static double[] getPlano(double x1, double y1, double z1, double x2, double y2, double z2, double x3,
			double y3, double z3) {
		double[] ab = Geom.getDetPlano(x1, y1, z1, x2, y2, z2, x3, y3, z3);
		return new double[] { -ab[0], ab[1], ab[0] * x1 - ab[1] * y1 + z1 };
	}

	/**
	 * Função que calcula o valor da coordenada z de um determinado ponto
	 * pertencente a um plano cujas coordenadas x e y são fornecidas
	 * 
	 * @param x0 coordenada x do ponto cuja coordenada z está sendo calculada
	 * @param y0 coordenada y do ponto cuja coordenada z está sendo calculada
	 * @param x1 coordenada x do primeiro ponto do plano
	 * @param y1 coordenada y do primeiro ponto do plano
	 * @param z1 coordenada z do primeiro ponto do plano
	 * @param x2 coordenada x do segundo ponto do plano
	 * @param y2 coordenada y do segundo ponto do plano
	 * @param z2 coordenada z do segundo ponto do plano
	 * @param x3 coordenada x do terceiro ponto do plano
	 * @param y3 coordenada y do terceiro ponto do plano
	 * @param z3 coordenada z do terceiro ponto do plano
	 * @return coordenada z do ponto
	 */
	public static double getPlano(double x0, double y0, double x1, double y1, double z1, double x2, double y2,
			double z2, double x3, double y3, double z3) {
		double[] ab = Geom.getDetPlano(x1, y1, z1, x2, y2, z2, x3, y3, z3);
		return ab[0] * (x1 - x0) - ab[1] * (y1 - y0) + z1;
	}

	static double[] getDetPlano(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
			double z3) {
		double a = Vec.det2(y2 - y1, y3 - y1, z2 - z1, z3 - z1);
		double b = Vec.det2(x2 - x1, x3 - x1, z2 - z1, z3 - z1);
		double c = Vec.det2(x2 - x1, x3 - x1, y2 - y1, y3 - y1);
		return new double[] { a / c, b / c };
	}

	/**
	 * Comparador de pontos
	 * 
	 * @author Philipe PEREIRA
	 *
	 */
	public static class PointComparator implements Comparator<Point2D> {

		/**
		 * <code>true</code> para comparar na direção X, <code>false</code> na Y
		 */
		private final boolean dir;

		/**
		 * <code>true</code> para ordenar em ordem crescente, <code>false</code> para
		 * ordem decrescente
		 */
		private boolean asc;

		/**
		 * Construtor do comparador de pontos
		 * 
		 * @param dir <code>true</code> para comparar na direção X, <code>false</code>
		 *            na Y
		 * @param asc <code>true</code> para ordenar em ordem crescente,
		 *            <code>false</code> para ordem decrescente
		 */
		public PointComparator(boolean dir, boolean asc) {
			this.dir = dir;
			this.asc = asc;
		}

		@Override
		public int compare(Point2D point1, Point2D point2) {
			if (asc) {
				int out = dir ? Double.compare(point1.getX(), point2.getX())
						: Double.compare(point1.getY(), point2.getY());
				if (out == 0)
					return dir ? Double.compare(point1.getY(), point2.getY())
							: Double.compare(point1.getX(), point2.getX());
				else
					return out;
			} else {
				int out = dir ? Double.compare(point2.getX(), point1.getX())
						: Double.compare(point2.getY(), point1.getY());
				if (out == 0)
					return dir ? Double.compare(point2.getY(), point1.getY())
							: Double.compare(point2.getX(), point1.getX());
				else
					return out;
			}
		}
	}

	/**
	 * Função que retorna o ponto extremo em meio a um conjunto
	 * 
	 * @param points conjunto de pontos
	 * @param dir
	 *               <ol start="0">
	 *               <li>maior abscissa;</i>
	 *               <li>maior ordenada;</i>
	 *               <li>menor abscissa;</i>
	 *               <li>menor ordenada.</i>
	 *               </ol>
	 * @return ponto extremo
	 */
	public static Point2D.Double getExtreme(Collection<? extends Point2D.Double> points, final int dir) {
		Iterator<? extends Point2D.Double> it = points.iterator();
		Point2D.Double out = it.next();
		while (it.hasNext()) {
			Point2D.Double p = it.next();
			switch (dir) {
			case 0:
				if (p.x > out.x)
					out = p;
				break;
			case 1:
				if (p.y > out.y)
					out = p;
				break;
			case 2:
				if (p.x < out.x)
					out = p;
				break;
			case 3:
				if (p.y < out.y)
					out = p;
				break;
			}
		}
		return out;
	}

	public static Point2D.Double min(Collection<? extends Point2D> values) {
		if (values.size() == 0)
			return new Point2D.Double(0., 0.);

		Point2D.Double v = new Point2D.Double(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		for (Point2D n : values) {
			if (n.getX() < v.x)
				v.x = n.getX();
			if (n.getY() < v.y)
				v.y = n.getY();
		}
		return v;
	}

	public static Point2D.Double max(Collection<? extends Point2D> values) {
		if (values.size() == 0)
			return new Point2D.Double(0., 0.);

		Point2D.Double v = new Point2D.Double(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
		for (Point2D n : values) {
			if (n.getX() > v.x)
				v.x = n.getX();
			if (n.getY() > v.y)
				v.y = n.getY();
		}
		return v;
	}
}
