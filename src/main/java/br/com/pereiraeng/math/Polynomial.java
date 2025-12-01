package br.com.pereiraeng.math;

import br.com.pereiraeng.math.probability.ProbEstat;
import br.com.pereiraeng.core.ExtendedMath;

public class Polynomial {

	/**
	 * Função que calcula a derivada de certa ordem da função:
	 * 
	 * x^n
	 * 
	 * @param n     expoente real
	 * @param order ordem da derivada
	 * @param x0    ponto para o qual a derivada é calculada
	 * @return valor calculado para a derivada
	 */
	public static double derivativeXn(double n, int order, double x0) {
		double m = 1.;
		for (int i = 0; i < order; i++)
			m *= (n - i);
		return m * Math.pow(x0, n - order);
	}

	/**
	 * Função que calcula a integral da função:
	 * 
	 * x^n
	 * 
	 * para n real, sobre o intervalo [a;b].
	 * 
	 * @param n expoente real
	 * @param a limite inferior do intervalo
	 * @param b limite superior do intervalo
	 * @return valor calculado para a integral
	 */
	public static double integralXn(double n, double a, double b) {
		return (Math.pow(b, n + 1.) - Math.pow(a, n + 1.)) / (n + 1.);
	}

	/**
	 * Função que calcula o valor de um polinômio num dado ponto
	 * 
	 * @param pol coeficientes do polinômio, sendo que cada índice do vetor
	 *            corresponde ao expoente da variável
	 * @param x   valor do argumento do polinômio
	 * @return valor no dado ponto
	 */
	public static double p(double[] pol, double x) {
		double p = 0;
		for (int i = 0; i < pol.length; i++)
			p += pol[i] * Math.pow(x, i);
		return p;
	}

	/**
	 * Função que calcula o polinômio obtido pela transformação afina de um dado
	 * polinômio. Ou seja, dado um polinômio p(x), representado pelos seus
	 * coeficientes, e uma tranformação x' = ax+b, a função retorna os coeficientes
	 * do polinômio p(ax+b).
	 * 
	 * @param pol coeficientes do polinômio, sendo que cada índice do vetor
	 *            corresponde ao expoente da variável
	 * @param a   coeficiente angular da transformação
	 * @param b   coeficiente linear da tranformação
	 * @return coeficientes do polinômio transformado, sendo que cada índice do
	 *         vetor corresponde ao expoente da variável
	 */
	public static double[] affineTransf(double[] pol, double a, double b) {
		double[] out = new double[pol.length];
		double m = b / a;
		for (int i = 0; i < out.length; i++) {
			// sum[ci(a xi + b)^i] = sum[ci ai^i (xi + b/a)^i]
			double c = pol[i] * Math.pow(a, i);
			// (xi + b/a)^i = sum[(i j) xi^j (b/a)^(i-j)]
			for (int j = 0; j <= i; j++)
				out[j] += c * ProbEstat.combinacao(i, j, false) * Math.pow(m, i - j);
		}
		return out;
	}

	/**
	 * Função que calcula os coefientes do polinômio de Legendre de uma dada ordem,
	 * porém num intervalo de integração do produto interno diferente de -1 a 1, ou
	 * seja, os polinômios obtidos são ortogonais com relação ao produto interno:
	 * 
	 * prodInt(x,y) = int(xy,a,b)
	 * 
	 * @param n ordem do polinômio de Legendre
	 * @param a limite inferior do intervalo
	 * @param b limite superior do intervalo
	 * @return coefientes do polinômio, sendo que cada índice do vetor corresponde
	 *         ao expoente da variável
	 */
	public static double[] getLegendre(int n, double a, double b) {
		double[] leg = getLegendre(n);
		leg = affineTransf(leg, 2. / (b - a), (a + b) / (a - b));
		return leg;
	}

	/**
	 * Função que calcula os coefientes do polinômio de Legendre de uma dada ordem.
	 * O conjunto de tais polinômio são ortogonais com respeito ao produto interno:
	 * 
	 * prodInt(x,y) = int(xy,-1,1)
	 * 
	 * @param n ordem do polinômio de Legendre
	 * @return coefientes do polinômio, sendo que cada índice do vetor corresponde
	 *         ao expoente da variável
	 */
	public static double[] getLegendre(int n) {
		switch (n) {
		case 0:
			return new double[] { 1 };
		case 1:
			return new double[] { 0, 1 };
		default:
			double[] out = new double[n + 1];

			double[] pn1 = getLegendre(n - 1);
			// multiplica o polinômio de ordem n-1 por (2n-1)/n e desloca uma
			// posição (i.e., multiplica por x)
			double c = (2. * n - 1.) / n;
			for (int i = 1; i <= pn1.length; i++)
				out[i] = c * pn1[i - 1];

			double[] pn2 = getLegendre(n - 2);
			// multiplica o polinômio de ordem n-2 por (n-1)/n
			c = (n - 1.) / n;
			for (int i = 0; i < pn2.length; i++)
				out[i] -= c * pn2[i];

			return out;
		}
	}

	/**
	 * Função que calcula os coefientes do polinômio obtido pela potência de um
	 * binômio de Newton, ou seja, os coeficientes obtidos pela expansão da
	 * expressão:
	 * 
	 * (x-x0)^exp
	 * 
	 * @param x0  valor que acompanha o argumento do binômio
	 * @param exp expoente do binômio
	 * @return coefientes do polinômio, sendo que cada índice do vetor corresponde
	 *         ao expoente da variável
	 */
	public static double[] binomioNewton(double x0, int exp) {
		double[] pol = new double[exp + 1];
		for (int i = 0; i < pol.length; i++)
			pol[i] = ProbEstat.combinacao(exp, i, false) * Math.pow(x0, exp - i);
		return pol;
	}

	/**
	 * Função que retorna os coeficientes do polinômio que melhor aproxima uma
	 * função do tipo x^n no intervalo [a;b].
	 * 
	 * @param order ordem do polinômio aproximador
	 * @param exp   expoente do argumento
	 * @param a     limite inferior do intervalo
	 * @param b     limite superior do intervalo
	 * @return coeficientes do polinômio aproximador, sendo que cada índice do vetor
	 *         corresponde ao expoente da variável
	 */
	public static double[] getPolAprXn(int order, double exp, double a, double b) {
		double[] pol = new double[order + 1];

		for (int k = 0; k < pol.length; k++) {
			double[] qk = Polynomial.getLegendre(k, a, b);

			double integral = 0.;

			for (int i = 0; i < qk.length; i++)
				integral += qk[i] * integralXn(exp + i, a, b);

			double ck = ((2 * k + 1) / (b - a)) * integral;

			for (int i = 0; i < qk.length; i++)
				pol[i] += ck * qk[i];
		}

		return pol;
	}

	/**
	 * Função que retorna os coeficientes do polinômio de Taylor que melhor aproxima
	 * uma função do tipo x^n nas imediações do ponto x0
	 * 
	 * @param order ordem do polinômio aproximador
	 * @param exp   expoente do argumento
	 * @param x0    ponto em volta do qual a aproximação é construída
	 * @return coeficientes do polinômio aproximador, sendo que cada índice do vetor
	 *         corresponde ao expoente da variável
	 */
	public static double[] getPolAprXn(int order, double exp, double x0) {
		double[] pol = new double[order + 1];

		for (int k = 0; k < pol.length; k++) {
			if (k == 0)
				pol[0] += Math.pow(x0, exp);
			else {
				double[] d = binomioNewton(-x0, k);
				d = Vec.mult(derivativeXn(exp, k, x0) / ExtendedMath.fatorial(k), d);
				for (int i = 0; i < d.length; i++)
					pol[i] += d[i];
			}
		}

		return pol;
	}

	/**
	 * Função que cria uma sequência de caracteres que indica um polinômio
	 * 
	 * @param coef vetor contendo os coeficientes do polinômio, sendo que cada
	 *             índice do vetor corresponde ao expoente da variável
	 * @param v    letra que indica a variável do polinômio
	 * @return <code>String</code> do polinômio
	 */
	public static String toString(double[] coef, char v) {
		String s = "";

		// termo independente e o de primeiro grau
		s += String.format("%.3E + %.3E.%c", coef[0], coef[1], v);

		// demais termos
		for (int i = 2; i < coef.length; i++)
			s += String.format(" + %.3E.%c^%d", coef[i], v, i);

		return s;
	}
}
