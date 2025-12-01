package br.com.pereiraeng.math.geometry;

import java.awt.geom.Point2D;
import java.util.Collection;

import br.com.pereiraeng.math.Vec;

public class Triangle {

	private Point2D.Double p1, p2, p3;

	public Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		this(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2), new Point2D.Double(x3, y3));
	}

	public Triangle(Point2D.Float p1, Point2D.Float p2, Point2D.Float p3) {
		this(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
	}

	public Triangle(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
		this.setVertices(p1, p2, p3);
	}

	public Triangle(Collection<? extends Point2D.Float> t) {
		if (t.size() != 3)
			throw new IllegalArgumentException("");
		int i = 0;
		for (Point2D.Float v : t)
			setVertice(i++, v);
	}

	public void setVertices(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	public void setVertice(int i, Point2D.Double p) {
		switch (i % 3) {
		case 0:
			this.p1 = p;
			break;
		case 1:
			this.p2 = p;
			break;
		case 2:
			this.p3 = p;
			break;
		}
	}

	public void setVertice(int i, Point2D.Float p) {
		Point2D.Double pd = new Point2D.Double(p.x, p.y);
		switch (i % 3) {
		case 0:
			this.p1 = pd;
			break;
		case 1:
			this.p2 = pd;
			break;
		case 2:
			this.p3 = pd;
			break;
		}
	}

	public void setVertices(Triangle triangle) {
		for (int i = 0; i < 3; i++)
			this.setVertice(i, triangle.getVertice(i));
	}

	public Point2D.Double getVertice(int i) {
		switch (i % 3) {
		case 0:
			return this.p1;
		case 1:
			return this.p2;
		case 2:
			return this.p3;
		}
		return null;
	}

	public Point2D.Double[] getVertices() {
		return new Point2D.Double[] { this.p1, this.p2, this.p3 };
	}

	public boolean containsVertice(Point2D.Double v) {
		return this.p1.equals(v) || this.p2.equals(v) || this.p3.equals(v);
	}

	/**
	 * Função que retorna uma das arestas do triângulo
	 * 
	 * @param i inteiro maior ou igual a 0 e menor que 3
	 * @return aresta delimitada pelos {@link #getVertice(int) vértices} :
	 *         <ol start="0">
	 *         <li>0 e 1;</i>
	 *         <li>1 e 2;</i>
	 *         <li>2 e 0.</i>
	 *         </ol>
	 */
	public Point2D.Double[] getEdge(int i) {
		Point2D.Double p1 = getVertice(i);
		Point2D.Double p2 = getVertice((i + 1) % 3);
		return new Point2D.Double[] { p1, p2 };
	}

	@Override
	public String toString() {
		return "[" + String.format("%.2f,%.2f", p1.x, p1.y) + "; " + String.format("%.2f,%.2f", p2.x, p2.y) + "; "
				+ String.format("%.2f,%.2f", p3.x, p3.y) + "]";
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof Triangle) {
			Triangle aTriangle = (Triangle) anObject;
			return Vec.equalTernary(this.getVertice(0), this.getVertice(1), this.getVertice(2), aTriangle.getVertice(0),
					aTriangle.getVertice(1), aTriangle.getVertice(2));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return p1.hashCode() + p2.hashCode() + p3.hashCode();
	}

	/**
	 * Função que indica se um dado triângulo possui um dado ponto como vértice ou
	 * não
	 * 
	 * @param p ponto a ser analisado
	 * @return <code>true</code> se o ponto é vértice do triângulo,
	 *         <code>false</code> senão
	 */
	public boolean hasVertice(Point2D.Double p) {
		if (p == null)
			return false;
		for (int i = 0; i < 3; i++)
			if (p.equals(getVertice(i)))
				return true;
		return false;
	}

	/**
	 * Função que indica se um dado triângulo possui um dado segmento de reta como
	 * aresta ou não
	 * 
	 * @param edge par de pontos que definem uma aresta
	 * @return -1 se a aresta não pertence ao triângulo, inteiro maior ou igual a 0
	 *         e menor que três para o {@link #getEdge(int) índice da aresta}
	 */
	public int hasEdge(Point2D.Double[] edge) {
		for (int i = 0; i < 3; i++) {
			Point2D.Double[] e = getEdge(i);
			if (Vec.equalBinary(e[0], e[1], edge[0], edge[1]))
				return i;
		}
		return -1;
	}

	/**
	 * Função que retorna o ponto do triângulo que não pertence a uma de suas
	 * arestas
	 * 
	 * @param edge uma das arestas do triângulo
	 * @return vértice oposto a essa aresta
	 */
	public Point2D.Double getOppositeVertice(Point2D.Double[] edge) {
		for (int i = 0; i < 3; i++) {
			Point2D.Double p = this.getVertice(i);
			if (!p.equals(edge[0]) && !p.equals(edge[1]))
				return p;
		}
		return null;
	}

	/**
	 * Função que retorna a aresta (i.e. o par de pontos) que é compartilhado pelos
	 * dois triângulos
	 * 
	 * @param t1 um dos triângulo que é adjacento ao outro
	 * @param t2 o outro triângulo
	 * @return matriz que contém na sua primeira linha um vetor com duas posições
	 *         indicando o segmento de reta compartilhado pelos triângulos e na
	 *         segunda linha um vetor com os vértices não compartilhados
	 */
	public static Point2D.Double[][] getCommonEdge(Triangle t1, Triangle t2) {
		Point2D.Double[][] out = new Point2D.Double[2][2];

		// procurar na lista de vértices do primeiro triângulo os dois pontos em comum e
		// o ponto que não é comum
		int j = 0;
		for (int i = 0; i < 3; i++) {
			Point2D.Double p = t1.getVertice(i);
			if (t2.hasVertice(p))
				out[0][j++] = p;
			else
				out[1][0] = p;
		}

		if (j != 2)
			throw new IllegalArgumentException("Esses triângulos não tem uma aresta em comum.");

		// procurar na lista de vértices do segundo triângulo o segundo ponto que não é
		// comum
		out[1][1] = t2.getOppositeVertice(out[0]);

		return out;
	}

	/**
	 * Função que informa se um dado ponto está dentro do triângulo ou não
	 * 
	 * @param x1 coordenada x do ponto a ser analisado
	 * @param y1 coordenada y do ponto a ser analisado
	 * @return <code>true</code> se o ponto estiver dentro do triângulo,
	 *         <code>false</code> senão
	 */
	public boolean hasInside(double x1, double y1) {
		return hasInside(new Point2D.Double(x1, y1));
	}

	/**
	 * Função que informa se um dado ponto está dentro do triângulo ou não
	 * 
	 * @param v ponto a ser analisado
	 * @return <code>true</code> se o ponto estiver dentro do triângulo,
	 *         <code>false</code> senão
	 */
	public boolean hasInside(Point2D.Double v) {
		return hasInside(v.x, v.y, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
	}

	public boolean hasInside(Point2D.Float v) {
		return hasInside(v.x, v.y, p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
	}

	/**
	 * Função que informa se um dado ponto está dentro do triângulo ou não. O método
	 * a ser utilizado está descrito nesta
	 * <a href= "http://mathworld.wolfram.com/TriangleInterior.html" >página</a> .
	 * 
	 * @param vx coordenada x do ponto
	 * @param vy coordenada y do ponto
	 * @param x1 coordenada x do primeiro vértice
	 * @param y1 coordenada y do primeiro vértice
	 * @param x2 coordenada x do segundo vértice
	 * @param y2 coordenada y do segundo vértice
	 * @param x3 coordenada x do terceiro vértice
	 * @param y3 coordenada y do terceiro vértice
	 * @return <code>true</code> se o ponto estiver dentro do triângulo,
	 *         <code>false</code> senão
	 */
	public static boolean hasInside(double vx, double vy, double x1, double y1, double x2, double y2, double x3,
			double y3) {
		double v1x = x2 - x1;
		double v1y = y2 - y1;

		double v2x = x3 - x1;
		double v2y = y3 - y1;

		double detV1V2 = Vec.det2(v1x, v1y, v2x, v2y); // até aqui, igual à função Vec#area(double, double, double,
														// double, double, double)
		double a = (Vec.det2(vx, vy, v2x, v2y) - Vec.det2(x1, y1, v2x, v2y)) / detV1V2;
		double b = -(Vec.det2(vx, vy, v1x, v1y) - Vec.det2(x1, y1, v1x, v1y)) / detV1V2;

		return (a > 0) && (b > 0) && (a + b < 1);
	}

	/**
	 * Função que indica se um ponto pertence a uma das arestas do triângulo
	 * 
	 * @param p ponto a ser analisado
	 * @return -1 se o ponto não pertence a nenhuma das aresta; inteiro maior ou
	 *         igual a 0 e menor que três para o {@link #getEdge(int) índice da
	 *         aresta} que contém o ponto
	 */
	public int belongsToAEdge(Point2D.Double p) {
		int out = -1;
		for (int i = 0; i < 3; i++) {
			if (Geom.collinear(getEdge(i), p)) {
				out = i;
				break;
			}
		}
		return out;
	}

	public double[] getAngles() {
		double a = getAngle(0);
		double b = getAngle(1);
		double c = Math.PI - a - b;
		return new double[] { a, b, c };
	}

	public double getAngle(int i) {
		Point2D.Double b, d1, d2;

		switch (i) {
		case 0:
			b = this.p1;
			d1 = this.p2;
			d2 = this.p3;
			break;
		case 1:
			b = this.p2;
			d1 = this.p1;
			d2 = this.p3;
			break;
		case 2:
			b = this.p3;
			d1 = this.p1;
			d2 = this.p2;
			break;
		default:
			return Double.NaN;
		}

		double[] v1 = new double[] { d1.x - b.x, d1.y - b.y };
		double[] v2 = new double[] { d2.x - b.x, d2.y - b.y };

		return Vec.getAngle(v1, v2);
	}
}
