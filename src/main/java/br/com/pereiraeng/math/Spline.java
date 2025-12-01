package br.com.pereiraeng.math;

import br.com.pereiraeng.core.collections.ArrayUtils;

public class Spline {

	public enum SplineType {
		NATURAL, COMPLETO, NOT_A_KNOT, PERIODICO;
	}

	/**
	 * Função que obtém os coeficientes do spline pelo método
	 * {@link SplineType#NOT_A_KNOT}
	 * 
	 * @param xs abscissas (<code>float</code>)
	 * @param ys ordenadas (<code>float</code>)
	 * @return vetor com as segundas derivadas
	 */
	public static double[] getSpline(float[] xs, float[] ys) {
		return getSpline(SplineType.NOT_A_KNOT, ArrayUtils.castDouble(xs), ArrayUtils.castDouble(xs));
	}

	/**
	 * Função que obtém os coeficientes do spline interpolador pelo método
	 * {@link SplineType#NOT_A_KNOT}
	 * 
	 * @param xs abscissas
	 * @param ys ordenadas
	 * @return vetor com as segundas derivadas
	 */
	public static double[] getSpline(double[] xs, double[] ys) {
		return getSpline(SplineType.NOT_A_KNOT, xs, ys);
	}

	/**
	 * Função que obtém os coeficientes do spline
	 * 
	 * @param xs abscissas
	 * @param ys ordenadas
	 * @return vetor com as segundas derivadas
	 */
	public static double[] getSpline(SplineType st, double[] xs, double[] ys) {
		int n = xs.length - 1;

		if (st == SplineType.PERIODICO && ys[0] != ys[n])
			throw new IllegalArgumentException("Se for spline periódico, os valores inicial e final devem ser iguais.");

		// monta o sistema linear com a matriz tridiagonal
		double[][] sis = buildLinSys(st, xs, ys);

		// Resolve o sistema linear pelo método da eliminação de Gauss para
		// matrizes tridiagonais
		double[] sol = Vec.solveGaussTri(sis[0], sis[1], sis[2], sis[3]);

		double[] m = null;
		switch (st) {
		case NOT_A_KNOT:
			m = new double[sol.length + 2];
			System.arraycopy(sol, 0, m, 1, sol.length);

			m[0] = ((xs[2] - xs[0]) / (xs[2] - xs[1])) * m[1] - ((xs[1] - xs[0]) / (xs[2] - xs[1])) * m[2];
			m[n] = ((xs[n] - xs[n - 2]) / (xs[n - 1] - xs[n - 2])) * m[n - 1]
					- ((xs[n] - xs[n - 1]) / (xs[n - 1] - xs[n - 2])) * m[n - 2];
			break;
		case PERIODICO:
			m = new double[sol.length + 1];
			System.arraycopy(sol, 0, m, 1, sol.length);

			m[0] = m[sol.length];
			break;
		default:
			// TODO
			break;
		}

		return m;
	}

	/**
	 * Função que calcula os splines para uma funçã (x,y)=f(t)
	 * 
	 * @param xs abscissas
	 * @param ys ordenadas
	 * @return
	 */
	public static double[][] getSplineLoop(double[] xs, double[] ys) {
		// gera a sequências dos parâmetros
		double[] ts = parameter(xs, ys);

		// para x...
		double[] m1 = getSpline(SplineType.PERIODICO, ts, xs);

		// para y...
		double[] m2 = getSpline(SplineType.PERIODICO, ts, ys);

		return new double[][] { ts, m1, m2 };
	}

	/**
	 * Função que monta o sistema linear tridiagonal cuja resolução dá as segundas
	 * derivadas que caracterizam os splines
	 * 
	 * @param st type de condição de contorno
	 * @param xs abscissas
	 * @param ys ordenadas
	 * @return vetor com as segundas derivadas
	 */
	private static double[][] buildLinSys(SplineType st, double[] xs, double[] ys) {
		int n = xs.length - 1;
		// no "not a knot" há uma equação a menos
		int eq = n + (st == SplineType.PERIODICO ? 0 : -1);

		if (eq < 0)
			throw new IllegalArgumentException("Vetor de entrada de tamanho menor que 0.");

		double[] dss = new double[eq], dp = new double[eq], dsi = new double[eq], d = new double[eq];

		for (int i = 1; i <= eq; i++) {
			// tanto para o exemplo 1 quanto para o exemplo 2, as linhas do
			// sistema são...

			double hi = xs[i] - xs[i - 1];

			double hip1 = Double.NaN, ynp1 = Double.NaN;
			if (i == n) {
				hip1 = xs[1] - xs[0];
				ynp1 = ys[1];
			} else {
				hip1 = xs[i + 1] - xs[i];
				ynp1 = ys[i + 1];
			}
			double hiphip1 = hi + hip1;

			if (i >= 2 && i <= eq - 1) {
				dsi[i - 1] = hi / hiphip1;
				dp[i - 1] = 2;
				dss[i - 1] = hip1 / hiphip1;
			}

			// ... e os termos independentes são...
			d[i - 1] = (6 / hiphip1) * ((ynp1 - ys[i]) / hip1 - (ys[i] - ys[i - 1]) / hi);
		}

		switch (st) {
		case NOT_A_KNOT:
			// a condição "not a knot" altera as equações 1 e n-1 para...

			double hh = (xs[1] - xs[0]) / (xs[2] - xs[1]);
			dsi[0] = Double.NaN;
			dp[0] = 2 + hh;
			dss[0] = 1 - hh;

			hh = (xs[n] - xs[n - 1]) / (xs[n - 1] - xs[n - 2]);
			dsi[n - 2] = 1 - hh;
			dp[n - 2] = 2 + hh;
			dss[n - 2] = Double.NaN;
			break;
		case PERIODICO:
			// os splines periódicos fazem com que as as equações 1 e n
			// fiquem...

			double h1 = xs[1] - xs[0];
			double h2 = xs[2] - xs[1];
			double h1ph2 = h1 + h2;

			dsi[0] = h1 / h1ph2;
			dp[0] = 2;
			dss[0] = h2 / h1ph2;

			double hn = xs[n] - xs[n - 1];
			h1ph2 = h1 + hn;

			dsi[n - 1] = hn / h1ph2;
			dp[n - 1] = 2;
			dss[n - 1] = h1 / h1ph2;
			break;
		default:
			// TODO falta o natural e o completo
			break;
		}

		return new double[][] { dsi, dp, dss, d };
	}

	/**
	 * Calcula o valor do spline num dado ponto. O spline é caracterizado pelos
	 * pontos que a ele deram origem (vetor de pontos x e y) e às segundas derivadas
	 * nos pontos (vetor m)
	 * 
	 * @param x  valor para o qual o spline será calculado
	 * @param xs abscissas dos pontos interpolados
	 * @param ys ordenadas dos pontos interpolados
	 * @param ms vetor com as segundas derivadas do spline
	 * @return valor do spline para o valor indicado
	 */
	public static double sx(double x, double[] xs, double[] ys, double[] ms) {
		int i = ArrayUtils.contains(x, xs);
		while (i == 0) {
			x = xs[xs.length - 1] - (xs[0] - x);
			i = ArrayUtils.contains(x, xs);
		}
		while (i == xs.length) {
			x = xs[0] + (x - xs[xs.length - 1]);
			i = ArrayUtils.contains(x, xs);
		}
		double h = xs[i] - xs[i - 1];
		double a = (xs[i] - x) / h;
		double b = (x - xs[i - 1]) / h;

		return a * ys[i - 1] + b * ys[i]
				+ (h * h / 6) * ((Math.pow(a, 3) - a) * ms[i - 1] + (Math.pow(b, 3) - b) * ms[i]);
	}

	public static double solve(double[] xs, double[] ys, double[] ms, int i) {
		if (i <= 0) {
			// se não se indicou em parte do spline em que se encontra a solução...
			boolean ok = false;
			for (int j = 1; j < ys.length; j++) {
				ok = ys[j] > 0 ^ ys[j - 1] > 0;
				if (ok) { // se há a troca de sinal na ordenada -> solução
					i = j;
					break;
				}
			}
			if (!ok)
				return Double.NaN;
		}

		// largura do spline
		double h = xs[i] - xs[i - 1];
		// período onde se encontra a solução
		double[][] p = new double[][] { { xs[i - 1], xs[i] }, { ys[i - 1], ys[i] } };
		// valor no ponto médio
		double y0 = Double.NaN;

		while (true) {
			double x0 = (p[0][0] + p[0][1]) * .5;
			if (p[0][1] - p[0][0] < 1E-6)
				return x0;

			double a = (xs[i] - x0) / h;
			double b = (x0 - xs[i - 1]) / h;
			y0 = a * ys[i - 1] + b * ys[i]
					+ (h * h / 6) * ((Math.pow(a, 3) - a) * ms[i - 1] + (Math.pow(b, 3) - b) * ms[i]);

			if (y0 > 0 ^ p[1][0] > 0) {
				p[0][1] = x0;
				p[1][1] = y0;
			} else {
				p[0][0] = x0;
				p[1][0] = y0;
			}
		}
	}

	/**
	 * Calcula o valor do spline para uma série de pontos. O spline é caracterizado
	 * pelos pontos que a ele deram origem (vetor de pontos x e y) e às segundas
	 * derivadas nos pontos (vetor m)
	 * 
	 * @param x  vetor com os valores para os quais o spline será calculado
	 * @param xs abscissas dos pontos interpolados
	 * @param ys ordenadas dos pontos interpolados
	 * @param ms vetor com as segundas derivadas do spline
	 * @return vetor com os valores calculados
	 */
	public static double[] sxs(double[] x, double[] xs, double[] ys, double[] ms) {
		double[] out = new double[x.length];
		for (int j = 0; j < x.length; j++)
			out[j] = sx(x[j], xs, ys, ms);
		return out;
	}

	/**
	 * Gera a seqüência dos parâmetros da curva
	 * 
	 * @param x abscissas dos pontos da curva
	 * @param y ordenadas dos pontos da curva
	 * @return variável da representação paramétrica da curva
	 */
	private static double[] parameter(double[] x, double[] y) {
		double[] t = new double[x.length];
		for (int i = 1; i < x.length; i++)
			t[i] = t[i - 1] + Math.sqrt(Math.pow(x[i] - x[i - 1], 2) + Math.pow(y[i] - y[i - 1], 2));
		return t;
	}

	// ===============================================================================

	public static void main(String[] args) {
		test();
	}

	public static void test() {
		// carregando os valores para o exemplo 1
		double[] x = { 1, 21, 42, 63, 126, 252, 504, 756 };
		double[] y = { 13.67, 13.5939296874674, 13.5093, 13.4175698997828, 13.1934935685505, 13.1118765655795,
				13.3169856504568, 13.449763373259 };

		double[] m = getSpline(x, y);

		cdb(x, y, m);

		// --------------------------------------------------------------

		// carregando os valores para o exemplo 2
		x = new double[] { 25, 19, 13, 9, 5, 2.2, 1, 3, 8, 13, 18, 25 };
		y = new double[] { 5, 7.5, 9.1, 9.4, 9, 7.5, 5, 2.1, 2, 3.5, 4.5, 5 };

		double[][] ms = getSplineLoop(x, y);

		curva(ms[0], x, y, ms[1], ms[2]);
	}

	private static void cdb(double x[], double y[], double m[]) {
		int n = x.length - 1;
		double taxa, valor;

		double dia = 62.;
		taxa = sx(dia, x, y, m);
		valor = 100000 * Math.pow(1 + taxa / 100, dia / 252);

		System.out.printf(
				"\n%25CERTIFICADO DE DEPOSITO BANCARIO\n\n       Data da aplicacao: 30/10/2006\n       Data do resgate:   30/01/2007\n       Taxa de juros:     %.5f%%\n       Valor aplicado:    R$ 100000.00\n       Valor de resgate:  R$ %.2f\n",
				'C', taxa, valor);

		System.out.printf("\n       Spline obtido (n = %d):\n\n      x          y             m\n", n);
		for (int i = 0; i <= n; i++)
			System.out.printf("     %3.5g      %2.3f     %3.5g\n", x[i], y[i], m[i]);
	}

	private static void curva(double t[], double x[], double y[], double m1[], double m2[]) {
		int n = x.length;

		System.out.printf(
				"\n\n%29CURVA FECHADA NO PLANO\n\n       Spline obtido (n = %d):\n\n    t    %c   x    %c       m(x)     %c   y    %c      m(y)\n",
				'C', n, '|', '|', '|', '|');
		for (int i = 0; i < n; i++)
			System.out.printf(" %7.5g %c %3.5g %c %14.5g %c %3.5g %c %14.5g\n", t[i], '|', x[i], '|', m1[i], '|', y[i],
					'|', m2[i]);
	}

	/**
	 * Função que reconstroi uma série de dados a partir de splines interpoladores
	 * pelo método {@link SplineType#NOT_A_KNOT}
	 * 
	 * @param values matriz de duas linhas, sendo a primeira das abscissas e a
	 *               segunda das ordenadas
	 * @return ordenadas da série reconstruída por splines
	 */
	public static double[] getSplineValues(double[][] values) {
		// calcula os splines (representados pela segunda derivada nos
		// pontos de controle)
		double[] sp = Spline.getSpline(values[0], values[1]);

		// período da análise
		int begin = (int) values[0][0]; // primeira ordenada
		double dtfl = values[0][values[0].length - 1] - begin; // última ordenada - primeira = período - dt

		// reconstroi
		double[] y = new double[(int) dtfl];
		for (int j = 0; j < dtfl; j++) {
			int x = begin + j;
			y[j] = Spline.sx(x, values[0], values[1], sp);
		}
		return y;
	}
}
