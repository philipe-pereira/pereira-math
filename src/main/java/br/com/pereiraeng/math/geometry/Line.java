package br.com.pereiraeng.math.geometry;

import java.awt.geom.Point2D;

import br.com.pereiraeng.math.Vec;

/**
 * Classe do objeto que representa uma aresta, um segmento de reta definido por
 * dois pontos no plano.
 * 
 * @author Philipe Pereira
 *
 */
public class Line {

	private Point2D.Double d1, d2;

	/**
	 * <ol start="-2">
	 * <li>incompleta: ambos pontos desconhecidos;</i>
	 * <li>incompleta: somente um ponto conhecido;</i>
	 * <li>segmento de reta: delimitado por dois pontos;</i>
	 * <li>semi-reta: um ponto delimitardor e uma direção;</i>
	 * <li>reta: um ponto e uma direção.</i>
	 * </ol>
	 */
	private int type;

	/**
	 * Construtor de uma reta incompleta (os pontos que a definem ainda não foram
	 * indicados)
	 */
	public Line() {
		this(null, null, -2);
	}

	/**
	 * Construtor de uma reta incompleta (somente um dos pontos que a definem foi
	 * indicado)
	 * 
	 * @param p
	 */
	public Line(Point2D.Double p) {
		this(p, null, -1);
	}

	/**
	 * Função que cria um segmento de reta a partir de seus dois vértices
	 * 
	 * @param from um dos vértices
	 * @param to   o outro vértice
	 */
	public Line(Point2D.Double from, Point2D.Double to) {
		this(from, to, 0);
	}

	/**
	 * Função que cria um segmento de reta a partir de seus dois vértices
	 * 
	 * @param from um dos vértices
	 * @param to   o outro vértice
	 */
	public Line(Point2D.Float from, Point2D.Float to) {
		this(new Point2D.Double(from.x, from.y), new Point2D.Double(to.x, to.y), 0);
	}

	/**
	 * 
	 * @param d1
	 * @param d2
	 * @param type
	 *             <ol start="-2">
	 *             <li>incompleta: ambos pontos desconhecidos;</i>
	 *             <li>incompleta: somente um ponto conhecido;</i>
	 *             <li>segmento de reta: delimitado por dois pontos;</i>
	 *             <li>semi-reta: um ponto delimitardor e uma direção;</i>
	 *             <li>reta: um ponto e uma direção.</i>
	 *             </ol>
	 */
	public Line(Point2D.Double d1, Point2D.Double d2, int type) {
		this.setFrom(d1);
		this.setTo(d2);
		this.setType(type);
	}

	// ---------------- getter n' setters ----------------

	public Point2D.Double getFrom() {
		return d1;
	}

	public Point2D.Double getTo() {
		return d2;
	}

	public void setFrom(Point2D.Double from) {
		this.d1 = from;
	}

	public void setTo(Point2D.Double to) {
		this.d2 = to;
	}

	public Point2D.Double[] getPointArray() {
		return new Point2D.Double[] { getFrom(), getTo() };
	}

	/**
	 * 
	 * @return
	 *         <ol start="-2">
	 *         <li>incompleta: ambos pontos desconhecidos;</i>
	 *         <li>incompleta: somente um ponto conhecido;</i>
	 *         <li>segmento de reta: delimitado por dois pontos;</i>
	 *         <li>semi-reta: um ponto delimitardor e uma direção;</i>
	 *         <li>reta: um ponto e uma direção.</i>
	 *         </ol>
	 */
	public int getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 *             <ol start="-2">
	 *             <li>incompleta: ambos pontos desconhecidos;</i>
	 *             <li>incompleta: somente um ponto conhecido;</i>
	 *             <li>segmento de reta: delimitado por dois pontos;</i>
	 *             <li>semi-reta: um ponto delimitardor e uma direção;</i>
	 *             <li>reta: um ponto e uma direção.</i>
	 *             </ol>
	 */
	public void setType(int type) {
		this.type = type;
	}

	// --------------------------------
	// SETTER'S ESPECIAIS

	public void addVertex(Point2D.Double v) {
		if (d1 == null) {
			setFrom(v);
			setType(-1);
		} else if (d2 == null) {
			setTo(v);
			setType(0);
		} else
			new IllegalArgumentException("Não cabe mais pontos.");
	}

	/**
	 * Função que, a partir de uma linha tipo -1 (ou seja, se conhece somente um dos
	 * pontos que a compõe) gera uma linha do tipo 1 (semi-reta infinita, definida
	 * por um ponto, que já foi dado, e uma direção orientada)
	 * 
	 * @param sense direção orientada
	 */
	public void setSense(Point2D.Double sense) {
		if (getType() == -1) {
			setTo(sense);
			setType(1);
		} else
			new IllegalArgumentException("Direção sem um ponto de partida.");
	}

	/**
	 * Função que, a partir de uma linha tipo -2 (ou seja, se desconhece quaisquer
	 * um dos pontos que a compõe) gera uma linha do tipo 2 (reta infinita, definida
	 * por um ponto e uma direção)
	 * 
	 * @param ori   direção
	 * @param point ponto
	 */
	public void setOrientation(Point2D.Double ori, Point2D.Double point) {
		if (getType() == -2) {
			setFrom(point);
			setTo(ori);
			setType(2);
		} else
			new IllegalArgumentException("Esta reta já tem um ponto de partida.");
	}

	// --------------------------------
	// OVERRIDES

	@Override
	public String toString() {
		switch (this.type) {
		case -1:
			return String.format("(%f;%f)?", getFrom().x, getFrom().y);
		case 0:
			return String.format("[(%f;%f)-(%f;%f)]", getFrom().x, getFrom().y, getTo().x, getTo().y);
		case 1:
			return String.format("[(%f;%f)-(%f;%f)>", getFrom().x, getFrom().y, getTo().x, getTo().y);
		case 2:
			return String.format("(%f;%f)-(%f;%f)>", getFrom().x, getFrom().y, getTo().x, getTo().y);
		default:
			return "?";
		}
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof Line) {
			Line eo = (Line) anObject;
			// os seus vértices forem os mesmos, então são iguais
			return Vec.equalBinary(this.getTo(), this.getFrom(), eo.getTo(), eo.getFrom());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getTo().hashCode() + this.getFrom().hashCode();
	}

	// ------------------- AUXILIAR -------------------

	public double distance() {
		return getTo().distance(getFrom());
	}

	public Point2D.Double getMidPoint() {
		return getMidPoint(getFrom(), getTo());
	}

	public static Point2D.Double getMidPoint(Point2D p1, Point2D p2) {
		return new Point2D.Double(.5 * (p1.getX() + p2.getX()), .5 * (p1.getY() + p2.getY()));
	}

	/**
	 * Função que retorna a mediatriz de dois pontos
	 * 
	 * @param p1   primeiro ponto
	 * @param p2   segundo ponto
	 * @param pmid ponto médio entre os dois pontos, a ser calculado nesta função,
	 *             por onde passa a mediatriz
	 * @return gradiente da bi coeficiente angular da mediatriz (que junto com o
	 *         ponto médio, define a reta mediatriz)
	 */
	public static double getBisector(Point2D.Double p1, Point2D.Double p2, Point2D.Double pmid) {
		pmid.setLocation(getMidPoint(p1, p2));
		return (p2.x - p1.x) / (p1.y - p2.y);
	}

	/**
	 * Função que calcula a intersecção entre este segmento de reta e outro segmento
	 * de reta
	 * 
	 * @param line outro segmento de reta
	 * @return vetor de números decimais que pode ter tamanho 0, 2 ou 3:
	 *         <ol start="0">
	 *         <li>retas paralelas (não há intersecção);</i>
	 *         <li value="2">retas se cruzam se os dois números do vetor pertencerem
	 *         ao intervalo [0;1];</i>
	 *         <li>retas colineares. Há sobreposição se os dois primeiros números do
	 *         vetor formarem um intervalo cuja intersecção com o intervalo [0;1]
	 *         não seja nula.</i>
	 *         </ol>
	 */
	public double[] intersection(Line line) {
		if (this.getType() != 0 || line.getType() != 0)
			new IllegalArgumentException("Este cálculo só vale para segmentos de reta.");
		return intersection(this.getTo(), this.getFrom(), line.getTo(), line.getFrom());
	}

	/**
	 * <p>
	 * Função que calcula a intersecção entre dois segmentos de reta.
	 * </p>
	 * <p>
	 * This method is the 2-dimensional specialization of the 3D line intersection
	 * algorithm from the article "Intersection of two lines in three-space" by
	 * Ronald Goldman, published in Graphics Gems, page 304
	 * </p>
	 * 
	 * @param p11 primeiro ponto da primeira reta
	 * @param p12 segunda ponto da primeira reta
	 * @param p21 primeiro ponto da segunda reta
	 * @param p22 segunda ponto da segunda reta
	 * @return vetor de números decimais que pode ter tamanho 0, 2 ou 3:
	 *         <ol start="0">
	 *         <li>retas paralelas (não há intersecção);</i>
	 *         <li value="2">retas se cruzam se os dois números do vetor pertencerem
	 *         ao intervalo [0;1];</i>
	 *         <li>retas colineares. Há sobreposição se os dois primeiros números do
	 *         vetor formarem um intervalo cuja intersecção com o intervalo [0;1]
	 *         não seja nula.</i>
	 *         </ol>
	 */
	public static double[] intersection(Point2D.Double p11, Point2D.Double p12, Point2D.Double p21,
			Point2D.Double p22) {
		double[] s = new double[] { p22.x - p21.x, p22.y - p21.y };
		double[] r = new double[] { p12.x - p11.x, p12.y - p11.y };

		// t = (q − p) X s / (r X s)
		// u = (q − p) X r / (r X s)

		double rXs = Vec.det2(r, s);
		double[] qp = new double[] { p21.x - p11.x, p21.y - p11.y };
		double qpXr = Vec.det2(qp, r);

		if (rXs == 0) {
			if (qpXr == 0) { // collinear
				// t0 = (q − p) . r / (r . r)
				double rr = r[0] * r[0] + r[1] * r[1];
				double t0 = Vec.prodEsc(qp, r) / rr;
				double sr = Vec.prodEsc(s, r);
				double t1 = t0 + sr / rr;
				return new double[] { sr > 0 ? t0 : t1, sr > 0 ? t1 : t0, 0 };
			} else // paralelo
				return new double[0];
		} else {
			double qpXs = Vec.det2(qp, s);
			return new double[] { qpXs / rXs, qpXr / rXs };
		}
	}

	/**
	 * Função que indica se este segmento de reta se intercepta com outro ou não
	 * 
	 * @param line outro segmento de reta
	 * @return <code>true</code> se as retas se interceptam, <code>false</code>
	 *         senão
	 */
	public boolean intersectionB(Line line) {
		return intersection(this.intersection(line));
	}

	/**
	 * Função que indica se dois segmentos de reta se interceptam ou não
	 * 
	 * @param p11 primeiro ponto da primeira reta
	 * @param p12 segunda ponto da primeira reta
	 * @param p21 primeiro ponto da segunda reta
	 * @param p22 segunda ponto da segunda reta
	 * @return <code>true</code> se as retas se interceptam, <code>false</code>
	 *         senão
	 */
	public static boolean intersectionB(Point2D.Double p11, Point2D.Double p12, Point2D.Double p21,
			Point2D.Double p22) {
		return intersection(intersection(p11, p12, p21, p22));
	}

	private static boolean intersection(double[] out) {
		if (out.length == 3)
			return out[0] <= 1. && out[1] >= 0.;
		else if (out.length == 2)
			return out[0] >= 0. && out[0] <= 1. && out[1] >= 0. && out[1] <= 1.;
		else
			return false;
	}
}
