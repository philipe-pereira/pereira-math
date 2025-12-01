package br.com.pereiraeng.math;

/**
 * Classe das funções para cálculos com funções quadráticas<br>
 * f(x1,...,xn) = a00 + a01x1 + a02x2 + ... + a0nxn + a11x11^2 + a12x1x2 + ... +
 * annxnn^2
 * 
 * @author Philipe Pereira
 *
 */
public class QuadFunc {

	// ===== POSICIONAMENTO DOS COEFICIENTES DA FUNÇÃO QUADRÁTICA NUM VETOR =====

	/**
	 * Função que converte uma posição no vetor de coeficientes quadráticos (1, x1,
	 * ..., xn, x1^2, x1x2, ..., x1xn, ..., xnxn) nos índices das variáveis. É a
	 * função inversa de {@link #pos(int, int, int)}.
	 * 
	 * @param index  posição no vetor de coeficientes quadráticos
	 * @param terms  número de variáveis + 1
	 * @param minus1 <code>true</code> para subtrair 1 de cada índice da variável
	 * @return nesta posição, está o coeficente do(a):
	 *         <ul>
	 *         <li>0,0: termo independente;</i>
	 *         <li>0,x: variável de primeiro grau x;</i>
	 *         <li>x,x: variável de segundo grau x;</i>
	 *         <li>x,y: produto da variável x com a variável y.</i>
	 *         </ul>
	 */
	public static int[] pos(int index, int terms, boolean minus1) {
		int n = index + 1;

		int row = terms;
		while (n > terms) {
			row--;
			n -= row;
		}
		if (minus1)
			return new int[] { terms - row - 1, n - 2 };
		else
			return new int[] { terms - row, n - 1 };
	}

	/**
	 * Função que converte os índices das variáveis numa posição no vetor de
	 * coeficientes quadráticos (1, x1, ..., xn, x1^2, x1x2, ..., x1xn, ..., xnxn).
	 * É a função inversa de {@link #pos(int, int, boolean)}.
	 * 
	 * @param x     índice de uma variável
	 * @param y     índice da outra variável
	 * @param terms número de variáveis + 1
	 * @return posição no vetor de coeficientes quadráticos (1, x1, ..., xn, x1^2,
	 *         x1x2, ..., x1xn, ..., xnxn)
	 */
	public static int pos(int x, int y, int terms) {
		if (x == 0 && y == 0) {
			return 0;
		} else {
			int m = Math.min(x, y);
			y = Math.max(x, y);
			if (m == 0)
				return y;
			else {
				int out = 0;
				for (int i = 0; i < m; i++) {
					out += terms;
					terms--;
				}
				return out + y - m;
			}
		}
	}

	// ===== CÁLCULO DO VALOR DAS FUNÇÕES QUADRÁTICAS E SUAS DERIVADAS =====

	/**
	 * Função que calcula o valor de uma função quadrática em função dos
	 * coeficientes e das variáveis
	 * 
	 * @param coef coeficientes de cada uma das funções quadráticas, dispostos na
	 *             matriz da seguinte maneira:
	 *             <ol start="0">
	 *             <li>cada uma das funções</i>
	 *             <li>coeficientes da função quadrática, dados na seguinte ordem:
	 *             1, x1, ..., xn, x1^2, x1x2, ..., x1xn, ..., xnxn.</i>
	 *             </ol>
	 * @param x    x1, ..., xn
	 * @return valor da função quadrática
	 */
	public static double fq(double[] coef, double[] x) {
		int terms = x.length + 1;

		double out = 0.;
		int c = 0;
		for (int i = 0; i < terms; i++) {
			if (i == 0) {
				// termo independente
				out += coef[0];
				// termos do primeiro grau
				for (int j = 1; j < terms; j++)
					out += coef[j] * x[j - 1];
				c += terms;
			} else {
				// termos do segundo grau de mesma variável
				out += coef[c] * x[i - 1] * x[i - 1];
				int t = terms - i;
				// termos do segundo grau de variáveis diferentes
				for (int k = 1; k < t; k++)
					out += coef[c + k] * x[i - 1] * x[i + k - 1];
				c += t;
			}
		}
		return out;
	}

	/**
	 * Função que calcula o valor de uma função quadrática em função dos
	 * coeficientes e das variáveis, porém <strong>truncando os termos independente
	 * e de primeira ordem, ou seja, usando somente os termos quadráticos (x1^2,
	 * x1x2, ..., x1xn, ..., xnxn)</strong>
	 * 
	 * @param coef coeficientes de cada uma das funções quadráticas, dispostos na
	 *             matriz da seguinte maneira:
	 *             <ol start="0">
	 *             <li>cada uma das funções</i>
	 *             <li>coeficientes da função quadrática, dados na seguinte ordem:
	 *             1, x1, ..., xn, x1^2, x1x2, ..., x1xn, ..., xnxn.</i>
	 *             </ol>
	 * @param x    x1, ..., xn
	 * @return valor da função quadrática
	 */
	private static double fqq(double[] coef, double[] x) {
		int terms = x.length + 1;

		double out = 0.;
		int c = terms;
		for (int i = 1; i < terms; i++) {
			// termos do segundo grau de mesma variável
			out += coef[c] * x[i - 1] * x[i - 1];
			int t = terms - i;
			// termos do segundo grau de variáveis diferentes
			for (int k = 1; k < t; k++)
				out += coef[c + k] * x[i - 1] * x[i + k - 1];
			c += t;
		}
		return out;
	}

	/**
	 * Função que calcula a jacobiana de uma função vetorial quadrática
	 * 
	 * @param coef coeficientes de cada uma das funções quadráticas, dispostos na
	 *             matriz da seguinte maneira:
	 *             <ol start="0">
	 *             <li>cada uma das funções</i>
	 *             <li>coeficientes da função quadrática, dados na seguinte ordem:
	 *             1, x1, ..., xn, x1^2, x1x2, ..., x1xn, ..., xnxn.</i>
	 *             </ol>
	 * @param x    x1, ..., xn
	 * @return matriz jacobiana
	 */
	public static double[][] jacobianaQuad(double[][] coef, double[] x) {
		double[][] out = new double[coef.length][x.length];

		for (int i = 0; i < coef.length; i++) {
			double[] cs = coef[i];
			for (int j = 0; j < x.length; j++) {
				// termo do primeiro grau
				out[i][j] += cs[j + 1];
				// termo do segundo grau da mesma variável
				out[i][j] += 2 * cs[QuadFunc.pos(j + 1, j + 1, x.length + 1)] * x[j];
				// termos do segundo grau de variáveis diferentes
				for (int k = 0; k < x.length; k++) {
					if (k != j)
						out[i][j] += cs[QuadFunc.pos(j + 1, k + 1, x.length + 1)] * x[k];
				}
			}
		}

		return out;
	}

	// ===== MÉTODOS DE SOLUÇÃO DE SISTEMAS DE EQUAÇÕES QUADRÁTICAS =====

	private static final double TOL = 1E-10;

	private static final double ITE = 1000;

	/**
	 * Função que resolve pelo método de Newton-Raphson um sistema de equações
	 * quadráticas
	 * 
	 * @param coefs coeficientes de cada uma das funções quadráticas, dispostos na
	 *              matriz da seguinte maneira:
	 *              <ol start="0">
	 *              <li>cada uma das funções</i>
	 *              <li>coeficientes da função quadrática, dados na seguinte ordem:
	 *              1, x1, ..., xn, x1^2, x1x2, ..., x1xn, ..., xnxn.</i>
	 *              </ol>
	 * @param xv    vetor com a solução inicial proposta E local onde será alocada a
	 *              solução
	 * @return número de iterações para se chegar à solução
	 */
	public static int solveNRquad(double[][] coefs, double[] xv) {
		double[] fx = new double[xv.length];
		for (int i = 0; i < xv.length; i++)
			fx[i] = QuadFunc.fq(coefs[i], xv);

		int count = 0;
		while (Vec.norma(fx) > TOL && count < ITE) {
			double[][] j = jacobianaQuad(coefs, xv);
			if (xv.length == 2)
				j = Vec.inverte2(j);
			else
				j = Vec.invert(j);
			fx = Vec.transf(j, fx);

			for (int i = 0; i < xv.length; i++)
				xv[i] -= fx[i];

			for (int i = 0; i < xv.length; i++)
				fx[i] = QuadFunc.fq(coefs[i], xv);

			count++;
		}

		return count;
	}

	/**
	 * Função que resolve pelo método de second-order um sistema de equações
	 * quadráticas
	 * 
	 * @param coefs coeficientes de cada uma das funções quadráticas, dispostos na
	 *              matriz da seguinte maneira:
	 *              <ol start="0">
	 *              <li>cada uma das funções</i>
	 *              <li>coeficientes da função quadrática, dados na seguinte ordem:
	 *              1, x1, ..., xn, x1^2, x1x2, ..., x1xn, ..., xnxn.</i>
	 *              </ol>
	 * @param xv    vetor com a solução inicial proposta E local onde será alocada a
	 *              solução
	 * @return número de iterações para se chegar à solução
	 */
	public static int solveSOquad(double[][] coefs, double[] xv) {
		double[] fx = new double[coefs.length];
		for (int i = 0; i < coefs.length; i++)
			fx[i] = -QuadFunc.fq(coefs[i], xv);

		// TODO no artigo do Iwamoto, 1978, ele não calcula a inversa, só a triangular
		// superior. Parece que economiza tempo...
		double[][] j = jacobianaQuad(coefs, xv);
		if (xv.length == 1)
			j[0][0] = 1. / j[0][0];
		else if (xv.length == 2)
			j = Vec.inverte2(j);
		else
			j = Vec.invert(j);

		double[] dy = new double[coefs.length];
		double[] dx = new double[xv.length];
		double erro = 1.;
		int count = 0;
		while (erro > TOL && count < ITE) {
			for (int i = 0; i < coefs.length; i++)
				dy[i] = fx[i] - QuadFunc.fqq(coefs[i], dx);

			double[] dx1 = Vec.transf(j, dy);
			erro = Vec.norma(Vec.sub(dx1, dx));
			dx = dx1;
			count++;
		}

		for (int i = 0; i < xv.length; i++)
			xv[i] += dx[i];

		return count;
	}

	public static void main(String[] args) {
		// exemplos para testes

		double[][] coefs = null;
		double[] xv = null;

		int s = 5;
		switch (s) {
		case 0:
			// 2x1^2 - 2x1x2 + 2x2^2 - 2,24
			// -2x1^2 - 1x1x2 + 2x2^2 - 0,64
			// x1 = 0,8; x2 = 1,2
			coefs = new double[][] { { -2.24, 0, 0, 2, -2, 2 }, { -0.64, 0, 0, -2, -1, 2 } };
			xv = new double[] { 1, 1 };
			break;
		case 1:
			// x^2 - 1.44
			// x1 = 1,2
			coefs = new double[][] { { -1.44, 0, 1 } };
			xv = new double[] { 1 };
			break;
		case 2:
			// x1 = 5; x2 = 5
			coefs = new double[][] { { -40, 0, -2, 1, 1, 0 }, { -15, 3, 0, 0, -1, 1 } };
			xv = new double[] { 7, 7 };
			break;
		case 3:
			// x1 = 1; x2 = 2; x3 = 3
			coefs = new double[][] { { 3, 1, -1, 0.5, 2, 3, -1.5, -1, -2, 1 },
					{ -17, -1, -1, -0.5, 1, 4, 0.5, 2, 2, -1 }, { -10.5, 8, 0, -5, 3, -6, 0.5, 1, 2, 1 } };
			xv = new double[] { 4, 4, 4 };
			break;
		case 4:
			// x1 = 1; x2 = 2; x3 = 3; x4 = 4
			coefs = new double[][] { { -47, 1, -1, 0.5, 5, 2, 3, -1.5, -1, -1, -2, 1, 1, -0.5, 2 },
					{ -17, -1, -1, -0.5, -4, 1, 4, 0.5, 6, 2, 2, 1, -1, -2, 0.5 },
					{ -10.5, 8, 0, -5, 2, 3, -6, 0.5, 1, 1, 2, -7, 1, 5, -1 },
					{ 24.5, 5, 1, -0.5, 1, 1, 7, 8, -5, -7, 2, -3, -3, -1.5, 2 } };
			xv = new double[] { 5, 5, 5, 5 };
			break;
		}

		// Newton-Raphson
//		int count = QuadFunc.solveNRquad(coefs, xv);

		// Second-order
		int count = QuadFunc.solveSOquad(coefs, xv);

		// -----------------------------------------------------

		System.out.println("iterações " + count);

		// mostra a solução
		for (int i = 0; i < xv.length; i++)
			System.out.println(xv[i]);

		// mostra que essa é a solução
		for (int i = 0; i < xv.length; i++)
			System.out.println(QuadFunc.fq(coefs[i], xv));

		
	}
}
