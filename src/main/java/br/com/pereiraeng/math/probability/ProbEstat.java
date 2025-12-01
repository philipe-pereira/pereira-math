package br.com.pereiraeng.math.probability;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;

import br.com.pereiraeng.math.QuadFunc;
import br.com.pereiraeng.math.Vec;
import br.com.pereiraeng.core.ExtendedMath;
import br.com.pereiraeng.core.collections.ArrayUtils;

/**
 * Conjunto de função matemáticas para cálculos de probabilidade e estatística
 * 
 * @author Philipe Pereira
 * 
 */
public class ProbEstat {

	public static double media(Collection<? extends Number> list) {
		double out = 0.;
		int n = 0;
		for (Number num : list) {
			double d = num.doubleValue();
			if (!Double.isNaN(d)) {
				out += d;
				n++;
			}
		}
		return out / n;
	}

	/**
	 * Função que calcula a média generalizada de um dado conjunto de valores
	 * 
	 * @param list lista de valores
	 * @param p    parâmetro da média (ver
	 *             <a href="https://en.wikipedia.org/wiki/Generalized_mean" > média
	 *             generalizada</a>)
	 * @return valor médio calculado
	 */
	public static double media(Collection<? extends Number> list, double p) {
		if (p == 1.) {
			// média aritmética
			return media(list);
		} else if (p == 0.) {
			// média geométrica

			double out = 1.;
			int n = 0;

			for (Number num : list) {
				double d = num.doubleValue();
				if (!Double.isNaN(d)) {
					out *= d;
					n++;
				}
			}
			return Math.pow(out, 1. / n);
		} else {
			// média generalizada

			double out = 0.;
			int n = 0;

			for (Number num : list) {
				double d = num.doubleValue();
				if (!Double.isNaN(d)) {
					out += Math.pow(d, p);
					n++;
				}
			}
			return Math.pow(out / n, 1. / p);
		}
	}

	public static float media(float[] fs) {
		float out = 0f;
		int n = 0;
		for (float f : fs) {
			if (!Float.isNaN(f)) {
				out += f;
				n++;
			}
		}
		return out / n;
	}

	public static double media(double[] ds) {
		double out = 0.;
		int n = 0;
		for (Double d : ds) {
			if (!Double.isNaN(d)) {
				out += d;
				n++;
			}
		}
		return out / n;
	}

	public static double desvioPadraoAmostral(Collection<? extends Number> values) {
		return desvioPadraoAmostral(values, media(values));
	}

	public static double desvioPadraoAmostral(Collection<? extends Number> values, double media) {
		double out = 0.;
		for (Number num : values)
			out += Math.pow(num.doubleValue() - media, 2.);
		return Math.sqrt(out / (values.size() - 1));
	}

	public static double desvioPadraoAmostral(double[] values) {
		return desvioPadraoAmostral(values, media(values));
	}

	public static double desvioPadraoAmostral(double[] values, double media) {
		double out = 0.;
		for (double v : values)
			out += Math.pow(v - media, 2.);
		return Math.sqrt(out / (values.length - 1));
	}

	// ------------------------- ANÁLISE COMBINATÓRIA ------------------------

	/**
	 * Ordem é levada em conta
	 * 
	 * @param elements   quantidade de elementos
	 * @param selection  quantidade de elementos selecionados
	 * @param repetition se <code>true</code> pode repetir (arranjo com repetição),
	 *                   se <code>false</code> não pode (arranjo simples - se
	 *                   <code>elements==selection</code>, temos uma
	 *                   <i>permutação</i>)
	 * @return número de seleções possíveis
	 */
	public static long arranjo(int elements, int selection, boolean repetition) {
		long out = 1L;
		if (repetition) { // n^p
			for (int i = 1; i <= selection; i++)
				out *= elements;
		} else { // n!/(n-p)!
			for (int i = elements - selection + 1; i <= elements; i++)
				out *= i;
		}
		return out;
	}

	/**
	 * Ordem não é levada em conta
	 * 
	 * @param elements   quantidade de elementos
	 * @param selection  quantidade de elementos selecionados
	 * @param repetition se <code>true</code> pode repetir (combinação com
	 *                   repetição), se <code>false</code> não pode (combinação
	 *                   simples)
	 * @return número de seleções possíveis
	 */
	public static long combinacao(int elements, int selection, boolean repetition) {
		long out;
		if (repetition) // (n+p-1)!/p!/(n+p-1-p)= (n+p-1)!/p!/(n-1)!
			out = combinacao(elements + selection - 1, selection, false);
		else { // n!/p!/(n-p)!
			out = 1L;
			if (selection > elements / 2)
				selection = elements - selection;
			out = arranjo(elements, selection, false); // arranjo é n!/(n-p)!
			for (int i = 2; i <= selection; i++) // dividir por p!
				out /= i;
		}
		return out;
	}

	/**
	 * Number of permutations, such that no element appears in its original position
	 * (ver <a href="https://en.wikipedia.org/wiki/Derangement">Derangement</a>)
	 * 
	 * @param elements número de elementos da permutação
	 * @return número de desarranjos
	 */
	public static int desarranjo(int elements) {
		if (elements == 1)
			return 0;
		else if (elements == 2)
			return 1;
		else {
			int pu = 0, u = 1;
			for (int i = 3; i <= elements; i++) {
				int v = (i - 1) * (pu + u);
				pu = u; // último vira penúltimo
				u = v; // atual vira último
			}
			return u;
		}
	}

	// ---------------------------- FITTING ----------------------------

	/**
	 * Função que retorna os coeficientes do polinômio obtido por regressão
	 * polinomial sobre uma série de pontos representados por suas coordenadas x e y
	 * 
	 * @param grau grau do polinômio interpolador
	 * @param x    vetor contendo as abcissas dos pontos
	 * @param y    vetor contendo as ordenadas dos pontos
	 * @return vetor contendo os coeficientes do polinômio, sendo que cada índice do
	 *         vetor corresponde ao expoente da variável
	 */
	public static double[] regPol(int grau, double[] x, double[] y) {
		grau++;
		double xtx[][] = new double[grau][grau];
		double xty[] = new double[grau];

		// montar as matrizes do sistema linear
		for (int i = 0; i < grau; i++) {
			for (int j = 0; j < grau; j++) {
				if (j >= i) {
					for (int k = 0; k < y.length; k++)
						xtx[i][j] += Math.pow(x[k], i) * Math.pow(x[k], j);
					if (j != i)
						xtx[j][i] = xtx[i][j];
				}
			}
			for (int k = 0; k < y.length; k++)
				xty[i] += Math.pow(x[k], i) * y[k];
		}

		// resolver o sistema linear
		return Vec.solveGauss(xtx, xty);
	}

	/**
	 * Função que retorna os coeficientes da função obtida por regressão linear
	 * multidimensional sobre uma série de pontos representados por suas coordenadas
	 * x e y
	 * 
	 * @param x matriz das variáveis x, onde o primeiro índice da matriz indica a
	 *          dimensão e o segundo a numeração dos pontos.
	 * @param y vetor dos valores y
	 * @return coeficientes da regressão, sendo aquele na posição 0 o termo
	 *         independente e os demais os coeficientes para cada dimensão.
	 */
	public static double[] regMultLin(double[][] x, double[] y) {
		int size = x.length + 1;

		double xtx[][] = new double[size][size];
		double xty[] = new double[size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (j >= i) {
					for (int k = 0; k < y.length; k++)
						xtx[i][j] += (i == 0 ? 1 : x[i - 1][k]) * (j == 0 ? 1 : x[j - 1][k]);

					if (j != i)
						xtx[j][i] = xtx[i][j];
				}
			}
			for (int k = 0; k < y.length; k++)
				xty[i] += (i == 0 ? 1 : x[i - 1][k]) * y[k];
		}

		return Vec.solveGauss(xtx, xty);
	}

	/**
	 * Função que retorna os coeficientes da função obtida por regressão quadrática
	 * multidimensional sobre uma série de pontos representados por suas coordenadas
	 * x e y
	 * 
	 * @param x matriz das variáveis x, onde o primeiro índice da matriz indica a
	 *          dimensão e o segundo a numeração dos pontos.
	 * @param y vetor dos valores y
	 * @return coeficientes da regressão, dados na seguinte ordem: 1, x1, ..., xn,
	 *         x1^2, x1x2, ..., x1xn, ..., xnxn (ver
	 *         {@link QuadFunc#fq(double[], double[])})
	 */
	public static double[] regMultQdr(double[][] x, double[] y) {
		int terms = x.length + 1;
		int size = (int) combinacao(terms, 2, true);

		double xtx[][] = new double[size][size];
		double xty[] = new double[size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (j >= i) {
					int[] is = QuadFunc.pos(i, terms, true), js = QuadFunc.pos(j, terms, true);
					for (int k = 0; k < y.length; k++)
						xtx[i][j] += (is[0] < 0 ? 1 : x[is[0]][k]) * (is[1] < 0 ? 1 : x[is[1]][k])
								* (js[0] < 0 ? 1 : x[js[0]][k]) * (js[1] < 0 ? 1 : x[js[1]][k]);
					if (j != i)
						xtx[j][i] = xtx[i][j];
				}
			}
			int[] is = QuadFunc.pos(i, terms, true);
			for (int k = 0; k < y.length; k++)
				xty[i] += (is[0] < 0 ? 1 : x[is[0]][k]) * (is[1] < 0 ? 1 : x[is[1]][k]) * y[k];
		}

		return Vec.solveGauss(xtx, xty);
	}

	// ---------------------------- WEIBULL ----------------------------

	/**
	 * Função que retorna o valor da função de distribuição acumulada de
	 * Weibull.<br>
	 * 
	 * F(x;k,y) = 1-exp(-(x/lambda)^k)
	 * 
	 * @param x      parâmetro
	 * @param k      fator de forma
	 * @param lambda fator de escala
	 * @return densidade de probabilidade
	 */
	public static double distAcumWeibull(double x, double k, double lambda) {
		return 1. - Math.exp(-Math.pow(x / lambda, k));
	}

	/**
	 * Função que retorna o valor médio da distribuição Weibull
	 * 
	 * @param k      fator de forma
	 * @param lambda fator de escala
	 * @return média de distribuição
	 */
	public static double mediaWeibull(double k, double lambda) {
		return lambda * ExtendedMath.gama(1 + 1 / k);
	}

	// -------------------- GERADOR DE HISTOGRAMAS --------------------

	/**
	 * Função que conta o número de ocorrências de um valor num intervalo. Se um dos
	 * valores não cair dentro dos intervalos, ele não será contado.
	 * 
	 * @param values valores a serem analisados
	 * @param begin  limite inferior do primeiro intervalo
	 * @param step   largura dos intervalos
	 * @param n      número de intervalos
	 * @return vetor de inteiros com um número de posições igual ao de intervalos,
	 *         indicando o número de ocorrências dentro de cada intervalo
	 */
	public static int[] hist(float[] values, float begin, float step, int n) {
		int[] out = new int[n];
		for (float v : values) {
			int pos = (int) ((v - begin) / step);
			if (pos >= 0 && pos < n)
				out[pos]++;
		}
		return out;
	}

	private static final int NUM_FREEZE = 2;

	/**
	 * Função que mapeia os instantes de tempo em que as medições ficaram constantes
	 * 
	 * @param min número mínimo de medições para se considerar que ela congelou
	 * @param pos posição das medições no registro que serão analisadas
	 * @param set
	 * @return matriz com número variável de linhas e 2 colunas, onde cada linha
	 *         indica um bloco de medições constantes (a primeira coluna indica o
	 *         instante de tempo em que começou o congelamento, o segundo o número
	 *         de medições congeladas, necessariamente maior que o argumento
	 *         <code>min</code>)
	 */
	public static int[][] mapConst(int min, int pos, TreeMap<Integer, float[]> reg) {
		Iterator<Entry<Integer, float[]>> it = reg.entrySet().iterator();

		List<int[]> out = new LinkedList<>();

		Entry<Integer, float[]> e = it.next();
		int ci = e.getKey();

		float[] v0 = ArrayUtils.floatVec(Float.NaN, NUM_FREEZE);
		v0[0] = e.getValue()[pos];

		boolean freeze = false;
		int start = ci, length = 0;
		while (it.hasNext()) {

			e = it.next();
			ci = e.getKey();
			float vi = e.getValue()[pos];

			int p = ArrayUtils.indexOf(v0, vi);
			if (Float.isNaN(vi) ? false : p < 0) {
				// veio um valor diferente...
				if (!Float.isNaN(v0[v0.length - 1])) {
					// se o buffer está cheio, então é diferente mesmo
					if (freeze) {
						freeze = false;
						out.add(new int[] { start, length });
					}
					start = ci;
					length = 0;
				} else
					length++;
				ArrayUtils.shiftedArray(v0, 1);
				v0[0] = vi;
			} else {
				// veio um mesmo valor...
				if (!freeze ? length > min : false) // se ainda não congelou e for constante POR MAIS DE 4 HORAS
					freeze = true;
				length++;
			}
		}
		if (freeze)
			out.add(new int[] { start, length });
		return out.toArray(new int[out.size()][2]);
	}

	public static int[][] mapConst(int min, double[] time, double[] values) {
		if (time.length == 0)
			return new int[0][0];

		List<int[]> out = new LinkedList<>();

		int ci = (int) time[0];

		double[] v0 = ArrayUtils.doubleVec(Float.NaN, NUM_FREEZE);
		v0[0] = values[0];

		boolean freeze = false;
		int start = ci, length = 0;
		int i = 1;
		while (i < time.length) {

			ci = (int) time[i];
			double vi = values[i];

			int p = ArrayUtils.indexOf(v0, vi);
			if (Double.isNaN(vi) ? false : p < 0) {
				// veio um valor diferente...
				if (!Double.isNaN(v0[v0.length - 1])) {
					// se o buffer está cheio, então é diferente mesmo
					if (freeze) {
						freeze = false;
						out.add(new int[] { start, length });
					}
					start = ci;
					length = 0;
				} else
					length++;
				ArrayUtils.shiftedArray(v0, 1);
				v0[0] = vi;
			} else {
				// veio um mesmo valor...
				if (!freeze ? length > min : false) // se ainda não congelou e for constante POR MAIS DE 4 HORAS
					freeze = true;
				length++;
			}
			i++;
		}
		if (freeze)
			out.add(new int[] { start, length });
		return out.toArray(new int[out.size()][2]);
	}

	// ----------------- FUNÇÕES GERADORAS DE NÚMEROS ALETÓRIOS -----------------

	/**
	 * Função que gera número inteiros não-negativos segundo uma distribuição de
	 * Poisson de parâmetro <code>lambda</code>
	 * 
	 * @param lambda parâmetro distribuição de Poisson e valor médio dos termos
	 *               gerados
	 * @return número aleatório
	 */
	public static int poisson(double lambda) {
		double l = Math.exp(-lambda);
		double p = 1;
		int out = 0;
		do {
			out++;
			p *= Math.random();
		} while (p > l);
		return out - 1;
	}

	/**
	 * Função que gera número inteiros não-negativos segundo uma distribuição
	 * geométrica de parâmetro <code>p</code>
	 * 
	 * @param p parâmetro distribuição geométrica (e inverso do valor médio dos
	 *          termos gerados), valor compreendido entre 0 e 1
	 * @return número aleatório
	 */
	public static int geometric(double p) {
		return (int) Math.ceil(Math.log(Math.random()) / Math.log(1 - p));
	}
}
