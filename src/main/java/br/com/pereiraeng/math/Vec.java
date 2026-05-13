package br.com.pereiraeng.math;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import br.com.pereiraeng.core.ExtendedMath;
import br.com.pereiraeng.core.collections.ArrayUtils;

/**
 * Classe com funções que facilitam a manipulação de vetores e matrizes
 * 
 * @author Philipe PEREIRA
 *
 */
public class Vec {

	/**
	 * Função que determina, com uma certa {@link Mat#TOL tolerância}, se dois
	 * vetores são iguais
	 * 
	 * @param a um vetor de números decimais
	 * @param b outro vetor de números decimais
	 * @return <code>true</code> se os dois vetores são iguais, <code>false</code>
	 *         se são diferentes
	 */
	public static boolean equals(double[] a, double[] b) {
		for (int i = 0; i < a.length; i++)
			if (!ExtendedMath.equals(a[i], b[i]))
				return false;
		return true;
	}

	/**
	 * Função que determina, com uma certa {@link Mat#TOL tolerância}, se dois
	 * vetores são iguais
	 * 
	 * @param a um vetor de números decimais
	 * @param b outro vetor de números decimais
	 * @return <code>true</code> se os dois vetores são iguais, <code>false</code>
	 *         se são diferentes
	 */
	public static boolean equals(float[] a, float[] b) {
		for (int i = 0; i < a.length; i++)
			if (!ExtendedMath.equals(a[i], b[i]))
				return false;
		return true;
	}

	/**
	 * Função que determina se um vetor é nulo
	 * 
	 * @param ds vetor de números decimais
	 * @return <code>true</code> se o vetor for nulo, <code>false</code> se um dos
	 *         seus componentes não for nulo
	 */
	public static boolean isNull(double[] ds) {
		for (int i = 0; i < ds.length; i++)
			if (ds[i] != 0.)
				return false;
		return true;
	}

	public static double[][] copyOf(double[][] a) {
		double[][] out = new double[a.length][];
		for (int i = 0; i < a.length; i++)
			out[i] = Arrays.copyOf(a[i], a[i].length);
		return out;
	}

	// ------------------------- vetor -> matriz -------------------------

	/**
	 * Função que constrói uma matriz simétrica a partir de um vetor com os
	 * elementos da diagonal e da matriz triangular inferior. Elementos de uma
	 * matriz de ordem n devem estar na ordem:
	 * <p>
	 * 
	 * 1<br>
	 * n+1; 2<br>
	 * n+2; n+3; 3<br>
	 * ...<br>
	 * ... n(n+1)/2; n<br>
	 * 
	 * @param n ordem da matriz simétrica
	 * @param v vetor com os elementos da matriz simétrica
	 * @return matriz simétrica
	 */
	public static double[][] getSimetrica(int n, double[] v) {
		double[][] out = new double[n][n];
		for (int i = 0; i < v.length; i++) {
			if (i < n) {
				// elementos da diagonal
				out[i][i] = v[i];
			} else {
				// ordem do elemento não-diagonal
				int l = i - n;

				// coordenada cartesiana triangular
				int j = 1;
				while (l > 0) {
					j++;
					l -= j;
				}
				int k = l + j - 1;

				// elementos simétricos
				out[j][k] = v[i];
				out[k][j] = v[i];
			}
		}

		return out;
	}

	// ------------------------- operações com vetores -------------------------

	/**
	 * Função que efetua a soma de dois ou mais vetores
	 * 
	 * @param a lista com os vetores a serem somados
	 * @return vetor com a soma dos elementos
	 */
	public static float[] sum(float[]... a) {
		float[] out = new float[a[0].length];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				out[j] += a[i][j];
		return out;
	}

	/**
	 * Função que efetua a soma de dois ou mais vetores
	 * 
	 * @param a lista com os vetores a serem somados
	 * @return vetor com a soma dos elementos
	 */
	public static double[] sum(double[]... a) {
		double[] out = new double[a[0].length];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				out[j] += a[i][j];
		return out;
	}

	public static double[][] sum(double[][]... a) {
		double[][] out = new double[a[0].length][a[0][0].length];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				for (int k = 0; k < a[i][j].length; k++)
					out[j][k] += a[i][j][k];
		return out;
	}

	/**
	 * Função que efetua a soma de dois ou mais vetores de elementos inteiros
	 * 
	 * @param a lista com os vetores a serem somados
	 * @return vetor com a soma dos elementos
	 */
	public static int[] sum(int[]... a) {
		int[] out = new int[a[0].length];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				out[j] += a[i][j];
		return out;
	}

	/**
	 * Função que efetua a subtração termo a termo de dois vetores
	 * 
	 * @param a vetor minuendo
	 * @param b vetor subtraendo
	 * @return vetor diferença
	 */
	public static double[] sub(double[] a, double[] b) {
		double[] out = new double[a.length];
		for (int i = 0; i < out.length; i++)
			out[i] = a[i] - b[i];
		return out;
	}

	/**
	 * Função que efetua a subtração termo a termo de dois vetores
	 * 
	 * @param a vetor minuendo
	 * @param b vetor subtraendo
	 * @return vetor diferença
	 */
	public static float[] sub(float[] a, float[] b) {
		float[] out = new float[a.length];
		for (int i = 0; i < out.length; i++)
			out[i] = a[i] - b[i];
		return out;
	}

	/**
	 * Função que efetua a subtração de dois vetores de elementos inteiros
	 * 
	 * @param a vetor minuendo
	 * @param b vetor subtraendo
	 * @return vetor diferença
	 */
	public static int[] sub(int[] a, int[] b) {
		int[] out = new int[a.length];
		for (int i = 0; i < out.length; i++)
			out[i] = a[i] - b[i];
		return out;
	}

	/**
	 * Função que efetua a subtração de dois vetores de elementos inteiros
	 * 
	 * @param a vetor minuendo
	 * @param b vetor subtraendo
	 * @return vetor diferença
	 */
	public static double[][] sub(double[][] a, double[][] b) {
		double[][] out = new double[a.length][];
		for (int i = 0; i < out.length; i++)
			out[i] = sub(a[i], b[i]);
		return out;
	}

	/**
	 * Função que efetua a multiplicação entre um escalar e um vetor
	 * 
	 * @param a escalar
	 * @param b vetor
	 * @return produto entre o escalar e o vetor
	 */
	public static float[] mult(float a, float[] b) {
		float[] out = new float[b.length];

		for (int i = 0; i < b.length; i++)
			out[i] = a * b[i];

		return out;
	}

	/**
	 * Função que efetua a multiplicação entre um escalar e um vetor
	 * 
	 * @param a escalar
	 * @param b vetor
	 * @return produto entre o escalar e o vetor
	 */
	public static double[] mult(double a, double[] b) {
		double[] out = new double[b.length];

		for (int i = 0; i < b.length; i++)
			out[i] = a * b[i];

		return out;
	}

	/**
	 * Função que efetua a multiplicação entre um escalar e um vetor
	 * 
	 * @param a escalar
	 * @param b vetor
	 * @return produto entre o escalar e o vetor
	 */
	public static int[] mult(int a, int[] b) {
		int[] out = new int[b.length];

		for (int i = 0; i < b.length; i++)
			out[i] = a * b[i];

		return out;
	}

	/**
	 * Função que efetua a multiplicação termo a termo de dois vetores
	 * 
	 * @param a vetor 1
	 * @param b vetor 2
	 * @return vetor produto
	 */
	public static float[] mult(float[] a, float[] b) {
		float[] out = new float[a.length];
		for (int i = 0; i < out.length; i++)
			out[i] = a[i] * b[i];
		return out;
	}

	/**
	 * Função que efetua a multiplicação termo a termo de dois vetores
	 * 
	 * @param a vetor 1
	 * @param b vetor 2
	 * @return vetor produto
	 */
	public static double[] mult(double[] a, double[] b) {
		double[] out = new double[a.length];
		for (int i = 0; i < out.length; i++)
			out[i] = a[i] * b[i];
		return out;
	}

	/**
	 * Função que efetua a divisão termo a termo de dois vetores
	 * 
	 * @param a vetor numerador
	 * @param b vetor denominador
	 * @return vetor dividido
	 */
	public static double[] div(double[] a, double[] b) {
		double[] out = new double[a.length];
		for (int i = 0; i < out.length; i++)
			out[i] = a[i] / b[i];
		return out;
	}

	/**
	 * Função que inverte cada um dos termos de um vetor
	 * 
	 * @param a vetor cujos termos será invertido
	 * @return vetor invertido
	 */
	public static float[] inv(float[] a) {
		float[] out = new float[a.length];
		for (int i = 0; i < out.length; i++)
			out[i] = 1f / a[i];
		return out;
	}

	/**
	 * Função que efetua a subtração de um número menos um vetor de elementos
	 * inteiros
	 * 
	 * @param s  número inteiro
	 * @param in vetor subtraendo
	 * @return vetor diferença
	 */
	public static int[] complement(int s, int[] in) {
		int[] out = new int[in.length];
		for (int i = 0; i < in.length; i++)
			out[i] = s - in[i];
		return out;
	}

	/**
	 * Função que retorna um novo vetor com todas os seus valores somados de uma
	 * dada quantidade
	 * 
	 * @param in vetor a ser somado
	 * @param s  valor a ser somado a todos os elementos do vetor
	 * @return vetor somado
	 */
	public static int[] shift(int[] in, int s) {
		int[] out = new int[in.length];
		for (int i = 0; i < in.length; i++)
			out[i] += in[i] + s;
		return out;
	}

	/**
	 * Função que retorna um novo vetor com todas os seus valores somados de uma
	 * dada quantidade
	 * 
	 * @param in vetor a ser somado
	 * @param s  valor a ser somado a todos os elementos do vetor
	 * @return vetor somado
	 */
	public static float[] shift(float[] in, float s) {
		float[] out = new float[in.length];
		for (int i = 0; i < in.length; i++)
			out[i] += in[i] + s;
		return out;
	}

	/**
	 * Função que retorna um vetor contendo os menores valores de um vetor e outro
	 * 
	 * @param a um dado vetor
	 * @param b um outro vetor
	 * @return um contendo os menores valores termo a termo de cada vetor
	 */
	public static float[] max(float[] a, float[] b) {
		float[] out = new float[a.length];
		for (int i = 0; i < out.length; i++)
			out[i] = Math.max(a[i], b[i]);
		return out;
	}

	/**
	 * Função que retorna um vetor contendo os maiores valores de um vetor e outro
	 * 
	 * @param a um dado vetor
	 * @param b um outro vetor
	 * @return um contendo os maiores valores termo a termo de cada vetor
	 */
	public static float[] min(float[] a, float[] b) {
		float[] out = new float[a.length];
		for (int i = 0; i < out.length; i++)
			out[i] = Math.min(a[i], b[i]);
		return out;
	}

	/**
	 * Função que retorna um novo vetor com seus valores elevados a um dado expoente
	 * 
	 * @param in vetor a ser elevado a uma dada potência
	 * @param p  expoente
	 * @return vetor com os resultados
	 */
	public static float[] pow(float[] in, int p) {
		float[] out = new float[in.length];
		for (int i = 0; i < in.length; i++)
			out[i] = (float) Math.pow(in[i], p);
		return out;
	}

	/**
	 * Função que retorna um novo vetor com a raiz quadrada de cada um de seus
	 * componentes
	 * 
	 * @param in vetor contendo os valores
	 * @return vetor com contendo a raiz quadrada dos valores
	 */
	public static float[] sqrt(float[] in) {
		float[] out = new float[in.length];
		for (int i = 0; i < in.length; i++)
			out[i] = (float) Math.sqrt(in[i]);
		return out;
	}

	/**
	 * Função que calcula o logaritmo na base decimal de todos os componentes de um
	 * vetor
	 * 
	 * @param a vetor
	 * @return novo vetor, contendo o logaritmo decimal de cada um dos número
	 */
	public static double[] log10(double[] a) {
		double[] out = new double[a.length];

		for (int i = 0; i < a.length; i++)
			out[i] = Math.log10(a[i]);

		return out;
	}

	/**
	 * Função que calcula o logaritmo natural de todos os componentes de um vetor
	 * 
	 * @param a vetor
	 * @return novo vetor, contendo o logaritmo natural de cada um dos número
	 */
	public static double[] ln(double[] a) {
		double[] out = new double[a.length];

		for (int i = 0; i < a.length; i++)
			out[i] = Math.log(a[i]);

		return out;
	}

	public static boolean or(boolean[] array) {
		for (int i = 0; i < array.length; i++)
			if (array[i])
				return true;
		return false;
	}

	public static boolean and(boolean[] array) {
		for (int i = 0; i < array.length; i++)
			if (!array[i])
				return false;
		return true;
	}

	// ---------------------- distância entre vetores e norma ----------------------

	/**
	 * Função que retorna o produto escalar de dois vetores cujas coordenadas são
	 * dadas numa base ortonormal
	 * 
	 * @param v1 primeiro vetor
	 * @param v2 segundo vetor
	 * @return produto escalar entre os dois vetores
	 */
	public static double prodEsc(double[] v1, double[] v2) {
		double out = 0.;
		for (int i = 0; i < v1.length; i++)
			out += v1[i] * v2[i];
		return out;
	}

	/**
	 * Função que retorna o ângulo formado por dois vetores
	 * 
	 * @param v1 primeiro vetor
	 * @param v2 segundo vetor
	 * @return ângulo entre os vetores, em radianos (valor compreendido entre
	 *         -{@link Math#PI} e {@link Math#PI})
	 */
	public static double getAngle(double[] v1, double[] v2) {
		return Math.acos(Vec.prodEsc(v1, v2) / (Vec.norma(v1) * Vec.norma(v2)));
	}

	/**
	 * Função que retorna a norma de um vetor
	 * 
	 * @param a      vetor de double float
	 * @param metric
	 *               <ol>
	 *               <li>Manhattan distance;</i>
	 *               <li>Euclidean distance;</i>
	 *               <li value="2147483647">Max norm.</i>
	 *               </ol>
	 * @return norma do vetor
	 */
	public static double norma(double[] a, int metric) {
		switch (metric) {
		case 2:
			return norma(a);
		case 1:
			double out = 0.;
			for (int i = 0; i < a.length; i++)
				out += Math.abs(a[i]);
			return out;
		case Integer.MAX_VALUE:
			out = 0.;
			for (int i = 0; i < a.length; i++)
				out = Math.max(a[i], out);
			return out;
		default:
			out = 0.;
			for (int i = 0; i < a.length; i++)
				out += Math.pow(a[i], metric);
			return Math.pow(out, 1. / metric);
		}
	}

	/**
	 * Função que retorna a norma de um vetor
	 * 
	 * @param a      vetor de single float
	 * @param metric
	 *               <ol>
	 *               <li>Manhattan distance;</i>
	 *               <li>Euclidean distance;</i>
	 *               <li value="2147483647">Max norm.</i>
	 *               </ol>
	 * @return norma do vetor
	 */
	public static float norma(float[] a, int metric) {
		switch (metric) {
		case 2:
			return norma(a);
		case 1:
			float out = 0f;
			for (int i = 0; i < a.length; i++)
				out += Math.abs(a[i]);
			return out;
		case Integer.MAX_VALUE:
			out = 0f;
			for (int i = 0; i < a.length; i++)
				out = Math.max(a[i], out);
			return (float) out;
		default:
			out = 0f;
			for (int i = 0; i < a.length; i++)
				out += Math.pow(a[i], metric);
			return (float) Math.pow(out, 1. / metric);
		}
	}

	/**
	 * Função que retorna a norma de um vetor
	 * 
	 * @param a      vetor de números complexos
	 * @param metric
	 *               <ol>
	 *               <li>Manhattan distance;</i>
	 *               <li>Euclidean distance;</i>
	 *               <li value="2147483647">Max norm.</i>
	 *               </ol>
	 * @return norma do vetor
	 */
	public static double norma(Complex[] a, int metric) {
		return norma(Complex.norma(a), metric);
	}

	/**
	 * Função que retorna a norma euclidiana de um vetor
	 * 
	 * @param a vetor
	 * @return a raiz quadrada da soma do quadrados das coordenadas do vetor
	 */
	public static double norma(double[] a) {
		double out = 0.;
		for (int i = 0; i < a.length; i++)
			out += Math.pow(a[i], 2);
		return Math.sqrt(out);
	}

	/**
	 * Função que retorna a norma euclidiana de um vetor
	 * 
	 * @param a vetor de números single float
	 * @return a raiz quadrada da soma do quadrados dos módulos das coordenadas do
	 *         vetor
	 */
	public static float norma(float[] a) {
		double out = 0.;
		for (int i = 0; i < a.length; i++)
			out += Math.pow(a[i], 2);
		return (float) Math.sqrt(out);
	}

	/**
	 * Função que retorna a norma euclidiana de um vetor
	 * 
	 * @param a vetor de números complexos
	 * @return a raiz quadrada da soma do quadrados dos módulos das coordenadas do
	 *         vetor
	 */
	public static double norma(Complex[] a) {
		double out = 0.;
		for (int i = 0; i < a.length; i++)
			out += Math.pow(a[i].getMod(), 2);
		return Math.sqrt(out);
	}

	/**
	 * Função que retorna a norma de Frobenius de uma matrix
	 * 
	 * @param a matrix
	 * @return norma de Frobenius da matrix
	 */
	public static double norma(double[][] a) {
		double out = 0.;
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				out += Math.pow(a[i][j], 2);
		return Math.sqrt(out);
	}

	/**
	 * Função que normatiza um vetor, ou seja, retorna o vetor linearmente
	 * dependente àquele indicado que possua norma unitária
	 * 
	 * @param a      vetor a ser normatizado
	 * @param metric metric
	 *               <ol>
	 *               <li>Manhattan distance;</i>
	 *               <li>Euclidean distance;</i>
	 *               <li value="2147483647">Max norm.</i>
	 *               </ol>
	 */
	public static void normatize(double[] a, int metric) {
		double t = 1. / norma(a, metric);
		for (int i = 0; i < a.length; i++)
			a[i] *= t;
	}

	public static void normatize(float[] a, int metric) {
		final float t = 1f / norma(a, metric);
		for (int i = 0; i < a.length; i++)
			a[i] *= t;
	}

	/**
	 * Função que normatiza um vetor, ou seja, retorna o vetor linearmente
	 * dependente àquele indicado que possua norma unitária
	 * 
	 * @param a      vetor a ser normatizado
	 * @param metric metric
	 *               <ol>
	 *               <li>Manhattan distance;</i>
	 *               <li>Euclidean distance;</i>
	 *               <li value="2147483647">Max norm.</i>
	 *               </ol>
	 */
	public static void normatize(Complex[] a, int metric) {
		double t = 1. / norma(Complex.norma(a), metric);
		for (int i = 0; i < a.length; i++)
			a[i].mult(t);
	}

	private static final double NORM_THRESHOLD = 1E-7; // era 1E-8

	/**
	 * Função que normatiza um vetor, porém de maneira discreta: os valores serão
	 * truncados de modo que todos os componentes sejam múltiplos desse passo e que
	 * ele ainda continue com a norma unitária
	 * 
	 * @param a      vetor
	 * @param metric metric
	 *               <ol>
	 *               <li>Manhattan distance;</i>
	 *               <li>Euclidean distance;</i>
	 *               <li value="2147483647">Max norm.</i>
	 *               </ol>
	 * @param step   passo
	 */
	public static void normatize(double[] a, int metric, double step) {
		normatize(a, metric);
		double[] rest = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			double v = Math.round(a[i] / step) * step;
			rest[i] = a[i] - v;
			a[i] = v;
		}
		double r = 1. - Vec.norma(a, 1);
		int count = 0;
		while (Math.abs(r) > NORM_THRESHOLD && count < 100) {
			int ie = -1;
			double e = r > 0. ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
			for (int i = 0; i < a.length; i++) {
				if (r > 0.) {
					if (rest[i] > e) {
						e = rest[i];
						ie = i;
					}
				} else {
					if (rest[i] < e) {
						e = rest[i];
						ie = i;
					}
				}
			}
			if (r > 0.) {
				rest[ie] -= step;
				a[ie] += step;
				r -= step;
			} else {
				rest[ie] += step;
				a[ie] -= step;
				r += step;
			}
			count++;
		}
	}

	public static void normatize(float[] a, int metric, float step) {
		normatize(a, metric);
		float[] rest = new float[a.length];
		for (int i = 0; i < a.length; i++) {
			float v = Math.round(a[i] / step) * step;
			rest[i] = a[i] - v;
			a[i] = v;
		}
		double r = 1. - Vec.norma(a, 1);
		int count = 0;
		while (Math.abs(r) > NORM_THRESHOLD && count < 100) {
			int ie = -1;
			double e = r > 0. ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
			for (int i = 0; i < a.length; i++) {
				if (r > 0.) {
					if (rest[i] > e) {
						e = rest[i];
						ie = i;
					}
				} else {
					if (rest[i] < e) {
						e = rest[i];
						ie = i;
					}
				}
			}
			if (r > 0.) {
				rest[ie] -= step;
				a[ie] += step;
				r -= step;
			} else {
				rest[ie] += step;
				a[ie] -= step;
				r += step;
			}
			count++;
		}
	}

	// ------------------------- operações com matrizes -------------------------

	/**
	 * Função que retorna uma matriz identidade de uma dada ordem. É o elemento
	 * neutro da operação {@link #mult(double[][], double[][]) multiplicação de
	 * matrizes}
	 * 
	 * @param n ordem da matriz
	 * @return matriz de double com 1 na diagonal e 0 no resto
	 */
	public static double[][] id(int n) {
		double[][] out = new double[n][n];
		for (int i = 0; i < n; i++)
			out[i][i] = 1.;
		return out;
	}

	/**
	 * Função que efetua a multiplicação entre um escalar e uma matrix
	 * 
	 * @param a escalar
	 * @param b matrix
	 * @return produto entre o escalar e a matrix
	 */
	public static double[][] mult(double a, double[][] b) {
		double[][] out = new double[b.length][];

		for (int i = 0; i < b.length; i++) {
			out[i] = new double[b[i].length];
			for (int j = 0; j < b[i].length; j++)
				out[i][j] = a * b[i][j];
		}

		return out;
	}

	/**
	 * Função que efetua a multiplicação entre duas matrizes
	 * 
	 * @param a primeira matriz a ser multiplicada
	 * @param b segunda matriz a ser multiplicada
	 * @return produto entre as matrizes
	 */
	public static double[][] mult(double[][] a, double[][] b) {
		double[][] out = new double[a.length][b[0].length];

		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < b[0].length; j++)
				for (int k = 0; k < b.length; k++)
					out[i][j] += a[i][k] * b[k][j];

		return out;
	}

	public static double[][] power(double[][] a, int power) {
		double[][] out = id(a.length);

		if (power == 0)
			return out;

		out = a;
		for (int i = 1; i < power; i++)
			out = mult(out, a);

		return out;
	}

	/**
	 * Função que efetua a multiplicação entre duas matrizes
	 * 
	 * @param a primeira matriz a ser multiplicada
	 * @param b segunda matriz a ser multiplicada
	 * @return produto entre as matrizes
	 */
	public static int[][] mult(int[][] a, int[][] b) {
		int[][] out = new int[a.length][b[0].length];

		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < b[0].length; j++)
				for (int k = 0; k < b.length; k++)
					out[i][j] += a[i][k] * b[k][j];

		return out;
	}

	/**
	 * Função que efetua a multiplicação entre duas matrizes
	 * 
	 * @param a primeira matriz a ser multiplicada
	 * @param b segunda matriz a ser multiplicada
	 * @return produto entre as matrizes
	 */
	public static Complex[][] mult(Complex[][] a, Complex[][] b) {
		Complex[][] out = new Complex[a.length][b[0].length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				out[i][j] = new Complex();
				for (int k = 0; k < b.length; k++)
					out[i][j].sum(Complex.mult(a[i][k], b[k][j]));
			}
		}

		return out;
	}

	/**
	 * Função que efetua a multiplicação entre um escalar e uma matriz complexa
	 * 
	 * @param a escalar complexo
	 * @param b matriz a ser multiplicada
	 * @return produto entre o escalar e a matriz
	 */
	public static Complex[][] mult(Complex a, Complex[][] b) {
		Complex[][] out = new Complex[b.length][b[0].length];
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				out[i][j] = new Complex(b[i][j]);
				out[i][j].mult(a);
			}
		}
		return out;
	}

	/**
	 * Função que transpõe uma matriz (i.e. um vetor de duas dimensões)
	 * 
	 * @param in matriz a ser transposta
	 * @return nova matriz, transposta da fornecida
	 */
	public static double[][] transp(double[][] in) {
		double[][] out = new double[in[0].length][in.length];
		for (int i = 0; i < out.length; i++) {
			for (int j = 0; j < out[i].length; j++) {
				out[i][j] = in[j][i];
			}
		}
		return out;
	}

	/**
	 * Função que transpõe uma matriz complexa
	 * 
	 * @param in matriz complexa a ser transposta
	 * @return matriz transposta
	 */
	public static Complex[][] transp(Complex[][] in) {
		Complex[][] out = new Complex[in[0].length][in.length];
		for (int i = 0; i < out.length; i++) {
			for (int j = 0; j < out[i].length; j++)
				out[i][j] = in[j][i];
		}
		return out;
	}

	/**
	 * Função que transpõe uma matriz de objetos
	 * 
	 * @param in matriz complexa a ser transposta
	 * @return matriz transposta
	 */
	public static Object[][] transp(Object[][] in) {
		Object[][] out = new Object[in[0].length][in.length];
		for (int i = 0; i < out.length; i++) {
			for (int j = 0; j < out[i].length; j++)
				out[i][j] = in[j][i];
		}
		return out;
	}

	// --------------- operações com matrizes - determinantes ---------------

	public static double det2(double[][] a) {
		return a[0][0] * a[1][1] - a[0][1] * a[1][0];
	}

	/**
	 * Função que calcula o determinante de uma matriz complexa 2x2
	 * 
	 * @param a matriz de dimensões 2x2
	 * @return determinante da matriz
	 */
	public static Complex det2(Complex[][] a) {
		return Complex.sub(Complex.mult(a[0][0], a[1][1]), Complex.mult(a[0][1], a[1][0]));
	}

	/**
	 * Função que calcula o determinante da matriz composta pelas coordenadas de
	 * dois vetores
	 * 
	 * @param v1x coordenada x de um vetor
	 * @param v1y coordenada y de um vetor
	 * @param v2x coordenada x do outro vetor
	 * @param v2y coordenada y do outro vetor
	 * @return determinante da matriz das coordenadas
	 */
	public static double det2(double v1x, double v1y, double v2x, double v2y) {
		return v1x * v2y - v1y * v2x;
	}

	/**
	 * Função que calcula o determinante da matriz composta pelas coordenadas de
	 * dois vetores
	 * 
	 * @param r um vetor
	 * @param s outro vetor
	 * @return determinante da matriz das coordenadas
	 */
	public static double det2(double[] r, double[] s) {
		return det2(r[0], r[1], s[0], s[1]);
	}

	/**
	 * Função que calcula o determinante de uma matriz 3x3
	 * 
	 * @param m matriz de dimensões 3x3
	 * @return determinante da matriz
	 */
	public static double det3(double[][] m) {
		return (m[0][0] * m[1][1] * m[2][2] + m[1][0] * m[2][1] * m[0][2] + m[0][1] * m[1][2] * m[2][0])
				- (m[2][0] * m[1][1] * m[0][2] + m[0][0] * m[2][1] * m[1][2] + m[1][0] * m[0][1] * m[2][2]);
	}

	// --- operações com matrizes - inversão e resolução de sistemas lineares ---

	/**
	 * Função que inverte uma matriz de dimensões 2x2
	 * 
	 * @param a matriz a ser invertida
	 * @return matriz invertida
	 */
	public static double[][] inverte2(double[][] a) {
		double det = Vec.det2(a);
		return new double[][] { { a[1][1] / det, -a[0][1] / det }, { -a[1][0] / det, a[0][0] / det } };
	}

	/**
	 * Função que inverte uma matriz de dimensões 2x2
	 * 
	 * @param a matriz a ser invertida
	 * @return matriz invertida
	 */
	public static Complex[][] inverte2(Complex[][] a) {
		Complex det = Vec.det2(a);
		return new Complex[][] { { Complex.div(a[1][1], det), Complex.mult(-1., Complex.div(a[0][1], det)) },
				{ Complex.mult(-1., Complex.div(a[1][0], det)), Complex.div(a[0][0], det) } };
	}

	/**
	 * <a href= "https://www.sanfoundry.com/java-program-find-inverse-matrix/">Sem
	 * saco hoje para implementar um algoritmo para inverter matriz...</a>
	 * 
	 * @param a matriz a ser invertida
	 * @return matriz invertida
	 */
	public static double[][] invert(double a[][]) {
		int n = a.length;
		double x[][] = new double[n][n];
		double b[][] = new double[n][n];
		int index[] = new int[n];
		for (int i = 0; i < n; ++i)
			b[i][i] = 1;

		// Transform the matrix into an upper triangle
		gaussian(a, index);

		// Update the matrix b[i][j] with the ratios stored
		for (int i = 0; i < n - 1; ++i)
			for (int j = i + 1; j < n; ++j)
				for (int k = 0; k < n; ++k)
					b[index[j]][k] -= a[index[j]][i] * b[index[i]][k];

		// Perform backward substitutions
		for (int i = 0; i < n; ++i) {
			x[n - 1][i] = b[index[n - 1]][i] / a[index[n - 1]][n - 1];
			for (int j = n - 2; j >= 0; --j) {
				x[j][i] = b[index[j]][i];
				for (int k = j + 1; k < n; ++k) {
					x[j][i] -= a[index[j]][k] * x[k][i];
				}
				x[j][i] /= a[index[j]][j];
			}
		}
		return x;
	}

	/**
	 * Method to carry out the partial-pivoting Gaussian elimination. Here index[]
	 * stores pivoting order.
	 * 
	 * @param a
	 * @param index
	 */
	private static void gaussian(double a[][], int[] index) {
		int n = index.length;
		double c[] = new double[n];

		// Initialize the index
		for (int i = 0; i < n; ++i)
			index[i] = i;

		// Find the rescaling factors, one from each row
		for (int i = 0; i < n; ++i) {
			double c1 = 0;
			for (int j = 0; j < n; ++j) {
				double c0 = Math.abs(a[i][j]);
				if (c0 > c1)
					c1 = c0;
			}
			c[i] = c1;
		}

		// Search the pivoting element from each column
		int k = 0;
		for (int j = 0; j < n - 1; ++j) {
			double pi1 = 0;
			for (int i = j; i < n; ++i) {
				double pi0 = Math.abs(a[index[i]][j]);
				pi0 /= c[index[i]];
				if (pi0 > pi1) {
					pi1 = pi0;
					k = i;
				}
			}

			// Interchange rows according to the pivoting order
			int itmp = index[j];
			index[j] = index[k];
			index[k] = itmp;
			for (int i = j + 1; i < n; ++i) {
				double pj = a[index[i]][j] / a[index[j]][j];

				// Record pivoting ratios below the diagonal
				a[index[i]][j] = pj;

				// Modify other elements accordingly
				for (int l = j + 1; l < n; ++l)
					a[index[i]][l] -= pj * a[index[j]][l];
			}
		}
	}

	/**
	 * Função que efetua a transformação linear de um vetor segundo uma dada matriz.
	 * É equivalente ao produto de matrizes (considerando, assim, o vetor como uma
	 * matriz-coluna).
	 * 
	 * @param a matriz da transformação
	 * @param x vetor a ser transformado
	 * @return vetor de saída da transformação
	 */
	public static double[] transf(double[][] a, double[] x) {
		double[] out = new double[a.length];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				out[i] += a[i][j] * x[j];
		return out;
	}

	/**
	 * Função que efetua a transformação linear de um vetor segundo uma dada matriz
	 * complexa. É equivalente ao produto de matrizes (considerando, assim, o vetor
	 * como uma matriz-coluna).
	 * 
	 * @param a matriz complexa da transformação
	 * @param x vetor complexo a ser transformado
	 * @return vetor complexo de saída da transformação
	 */
	public static Complex[] transf(Complex[][] a, Complex[] x) {
		Complex[] out = new Complex[a.length];
		for (int i = 0; i < a.length; i++) {
			out[i] = new Complex(0, 0);
			for (int j = 0; j < a[i].length; j++)
				out[i].sum(Complex.mult(a[i][j], x[j]));
		}
		return out;
	}

	/**
	 * Função que computa o Kernel de uma transformação linear pelo <a href=
	 * "https://en.wikipedia.org/wiki/Kernel_(linear_algebra)#Computation_by_Gaussian_elimination">método
	 * de eliminação de Gauss</a>
	 * 
	 * 
	 * @param a matriz da transformação linear
	 * @return vetores que definem o subespaço vetorial do kernel da transformação
	 */
	public static double[][] kernel(double[][] a) {
		if (FLAG_PRINT)
			ArrayUtils.show(a);

		double[][] id = Vec.id(a[0].length);
		columnEchelon(a, id, false, false);

		if (FLAG_PRINT) {
			System.out.print("Escalonada\n");
			ArrayUtils.show(a);
		}

		for (int i = 0; i < a.length; i++)
			if (ExtendedMath.equals(a[i][i], 0))
				return ArrayUtils.getColumns(id, ArrayUtils.progVec(i, a[0].length - i));
		return ArrayUtils.getColumns(id, ArrayUtils.progVec(a.length, a[0].length - a.length));
	}

	/**
	 * Função que converte a matriz de quadripolo do seu formato 'a^-1' para o
	 * formato 'h'. (ver <a href=
	 * "http://en.wikipedia.org/wiki/Two-port_network#Interrelation_of_parameters"
	 * >relações entre as matrizes de representação dos quadripolos</a>).
	 * 
	 * @param in matriz complexa no formato 'a^-1'
	 * @return matriz complexa no formato 'h'
	 */
	public static Complex[][] aInv2h(Complex[][] in) {
		Complex det = det2(in);
		Complex[][] out = new Complex[][] { { det, in[0][1] }, { Complex.mult(-1, in[1][0]), new Complex(1, 0) } };
		out = mult(Complex.inv(in[1][1]), out);
		return out;
	}

	/**
	 * Função que converte a matriz de quadripolo do seu formato 'a' para o formato
	 * de admitâncias 'y'. (ver <a href=
	 * "http://en.wikipedia.org/wiki/Two-port_network#Interrelation_of_parameters"
	 * >relações entre as matrizes de representação dos quadripolos</a>).
	 * 
	 * @param in matriz complexa no formato 'a'
	 * @return matriz complexa de admitâncias 'y'
	 */
	public static Complex[][] a2y(Complex[][] in) {
		Complex det = det2(in);
		Complex[][] out = new Complex[][] { { in[1][1], Complex.mult(-1, det) }, { new Complex(-1, 0), in[0][0] } };
		out = mult(Complex.inv(in[0][1]), out);
		return out;
	}

	// ---------------------- RESOLUÇÃO DE SISTEMAS LINEARES ----------------------

	// ---------------------- ANALÍTICO ----------------------

	/**
	 * Função que resolve um sistema de equações lineares de com
	 * <code>duas incógnitas</code>. Dada a simplicidade do problema, esta função
	 * vale-se da solução analítica.
	 * 
	 * @param a matriz dos coeficientes
	 * @param y vetor dos termos independentes
	 * @return vetor com duas posições com a solução
	 */
	public static double[] solve2x2(double[][] a, double[] y) {
		double det = Vec.det2(a);
		return new double[] { (a[1][1] * y[0] - a[0][1] * y[1]) / det, (-a[1][0] * y[0] + a[0][0] * y[1]) / det };
	}

	// ---------------------- GAUSS ----------------------

	private static final boolean FLAG_PRINT = false;

	private static final boolean FLAG_FILE = false;

	/**
	 * Função que resolve um sistema linear de variáveis reais pelo método de Gauss
	 * (primeira se escalona a matriz e em seguida se faz a substituição regressiva)
	 * 
	 * @param a matriz dos coeficientes
	 * @param y vetor dos termos independentes
	 * @return vetor da solução do sistema
	 */
	public static double[] solveGauss(double[][] a, double[] y) {
		if (FLAG_PRINT)
			ArrayUtils.show(a, y);

		if (FLAG_FILE) {
			Vec.saveMtx(new File("a.mtx"), a);
			Vec.saveVec(new File("b.vec"), y);
		}

		rowEchelon(a, y, true, false);

		if (FLAG_PRINT) {
			System.out.print("Escalonada (com os multiplicadores)\n");
			ArrayUtils.show(a, y);
		}

		double[] x = new double[y.length];

		// substituição regressiva
		x[y.length - 1] = y[y.length - 1] / a[y.length - 1][y.length - 1];
		for (int i = y.length - 2; i >= 0; i--) {
			if (!ExtendedMath.equals(a[i][i], 0.)) {
				for (int j = y.length - 1; j > i; j--)
					y[i] -= a[i][j] * x[j];
				x[i] = y[i] / a[i][i];
			}
		}

		if (FLAG_PRINT) {
			System.out.print("Solucao:\n\n");
			for (int i = 0; i < y.length; i++)
				System.out.print(String.format("%g\n", x[i]));
		}

		if (FLAG_FILE)
			Vec.saveVec(new File("x.vec"), x);

		return x;
	}

	/**
	 * Função que resolve um sistema linear de variáveis complexas pelo método de
	 * Gauss (primeira se escalona a matriz e em seguida se faz a substituição
	 * regressiva)
	 * 
	 * @param a matriz dos coeficientes
	 * @param y vetor dos termos independentes
	 * @return vetor da solução do sistema
	 */
	public static Complex[] solveGauss(Complex[][] a, Complex[] y) {

		rowEchelon(a, y, true, false);

		Complex[] x = new Complex[y.length];

		// substituição regressiva
		x[y.length - 1] = Complex.div(y[y.length - 1], a[y.length - 1][y.length - 1]);
		for (int i = y.length - 2; i >= 0; i--) {
			if (!Complex.isNull(a[i][i])) {
				for (int j = y.length - 1; j > i; j--)
					y[i].sub(Complex.mult(a[i][j], x[j]));
				x[i] = Complex.div(y[i], a[i][i]);
			}
		}

		return x;
	}

	/**
	 * Função que escalona por linhas uma matriz
	 * 
	 * @param a         matriz a escalonada
	 * @param y         vetor que sofrerá as mesmas operações que a matriz a ser
	 *                  escalonada (<code>null</code> se não houver tal vetor)
	 * @param mult      <code>true</code> para armazenar na matriz triangular
	 *                  inferior os multiplicadores utilizados
	 * @param canonical <code>true</code> para que a matriz escalonada fique na
	 *                  forma canônica (com o elemento mais a esquerda sempre
	 *                  valendo 1; neste caso, os multiplicadores serão os próprios
	 *                  elementos que já estão na matriz)
	 */
	private static void rowEchelon(double[][] a, double[] y, boolean mult, boolean canonical) {
		for (int j = 0; j < a.length; j++) {
			// para cada linha...

			if (ExtendedMath.equals(a[j][j], 0)) {
				// se na diagonal houver um elemento nulo, troca com o primeiro não nulo da
				// mesma coluna
				for (int i = j + 1; i < a.length; i++) {
					if (!ExtendedMath.equals(a[i][j], 0)) {
						swapRow(i, j, a, y);
						if (FLAG_PRINT) {
							System.out.print(String.format("Troca de linha - %d pela %d\n", i, j));
							ArrayUtils.show(a, y);
						}
						break;
					}
				}
				if (ExtendedMath.equals(a[j][j], 0)) // se o pivô continuar nulo, pular...
					continue;
			} else if (canonical && !ExtendedMath.equals(a[j][j], 1)) {
				for (int k = j + 1; k < a[j].length; k++)
					a[j][k] /= a[j][j];
				if (y != null)
					y[j] /= a[j][j];
				a[j][j] = 1.;
				if (FLAG_PRINT)
					ArrayUtils.show(a, y);
			}
			for (int i = j + 1; i < a.length; i++) {
				// para cada linha abaixo da diagonal
				if (!canonical)
					a[i][j] /= a[j][j];
				for (int k = j + 1; k < a[j].length; k++)
					a[i][k] -= a[i][j] * a[j][k];
				if (y != null)
					y[i] -= a[i][j] * y[j];
				if (!mult)
					a[i][j] = 0.;
				if (FLAG_PRINT)
					ArrayUtils.show(a, y);
			}
		}
	}

	private static void swapRow(int i, int k, double[][] a, double[] y) {
		for (int j = 0; j < a[i].length; j++) {
			// para cada coluna...
			double aux = a[i][j];
			a[i][j] = a[k][j];
			a[k][j] = aux;
		}
		if (y != null) {
			double aux = y[i];
			y[i] = y[k];
			y[k] = aux;
		}
	}

	/**
	 * Função que escalona por linhas uma matriz
	 * 
	 * @param a         matriz a escalonada
	 * @param y         vetor que sofrerá as mesmas operações que a matriz a ser
	 *                  escalonada (<code>null</code> se não houver tal vetor)
	 * @param mult      <code>true</code> para armazenar na matriz triangular
	 *                  inferior os multiplicadores utilizados
	 * @param canonical <code>true</code> para que a matriz escalonada fique na
	 *                  forma canônica (com o elemento mais a esquerda sempre
	 *                  valendo 1; neste caso, os multiplicadores serão os próprios
	 *                  elementos que já estão na matriz)
	 */
	private static void rowEchelon(Complex[][] a, Complex[] y, boolean mult, boolean canonical) {
		for (int j = 0; j < a.length; j++) {
			// para cada linha...

			if (Complex.isNull(a[j][j])) {
				// se na diagonal houver um elemento nulo, troca com o primeiro não nulo da
				// mesma coluna
				for (int i = j + 1; i < a.length; i++) {
					if (!Complex.isNull(a[i][j])) {
						swapRow(i, j, a, y);
						break;
					}
				}
				if (Complex.isNull(a[j][j])) // se o pivô continuar nulo, pular...
					continue;
			} else if (canonical && !Complex.equals(a[j][j], new Complex(1, 0))) {
				for (int k = j + 1; k < a[j].length; k++)
					a[j][k].div(a[j][j]);
				if (y != null)
					y[j].div(a[j][j]);
				a[j][j] = new Complex(1, 0);
			}
			for (int i = j + 1; i < a.length; i++) {
				// para cada linha abaixo da diagonal
				if (!canonical)
					a[i][j].div(a[j][j]);
				for (int k = j + 1; k < a[j].length; k++)
					a[i][k].sub(Complex.mult(a[i][j], a[j][k]));
				if (y != null)
					y[i].sub(Complex.mult(a[i][j], y[j]));
				if (!mult)
					a[i][j] = new Complex();
			}
		}
	}

	private static void swapRow(int i, int k, Complex[][] a, Complex[] y) {
		for (int j = 0; j < a[i].length; j++) {
			// para cada coluna...
			Complex aux = a[i][j];
			a[i][j] = a[k][j];
			a[k][j] = aux;
		}
		if (y != null) {
			Complex aux = y[i];
			y[i] = y[k];
			y[k] = aux;
		}
	}

	/**
	 * Função que escalona por colunas uma matriz
	 * 
	 * @param a         matriz a escalonada
	 * @param y         matriz que sofrerá as mesmas operações que a matriz a ser
	 *                  escalonada (<code>null</code> se não houver tal matriz)
	 * @param mult      <code>true</code> para armazenar na matriz triangular
	 *                  superior os multiplicadores utilizados
	 * @param canonical <code>true</code> para que a matriz escalonada fique na
	 *                  forma canônica (com o elemento mais acima sempre valendo 1;
	 *                  neste caso, os multiplicadores serão os próprios elementos
	 *                  que já estão na matriz)
	 */
	private static void columnEchelon(double[][] a, double[][] y, boolean mult, boolean canonical) {
		for (int j = 0; j < a.length; j++) {
			// para cada linha...

			if (ExtendedMath.equals(a[j][j], 0)) {
				// se na diagonal houver um elemento nulo, troca com o primeiro não nulo da
				// mesma coluna
				for (int i = j + 1; i < a[j].length; i++)
					if (!ExtendedMath.equals(a[j][i], 0)) {
						swapColumn(i, j, a, y);
						System.out.print(String.format("Troca de coluna - %d pela %d\n", i, j));
						ArrayUtils.show(a, y);
						break;
					}
			} else if (canonical && !ExtendedMath.equals(a[j][j], 1)) {
				for (int k = j + 1; k < a.length; k++)
					a[k][j] /= a[j][j];
				if (y != null)
					for (int k = 0; k < y.length; k++)
						y[k][j] /= a[j][j];
				a[j][j] = 1.;
				ArrayUtils.show(a, y);
			}
			for (int i = j + 1; i < a[j].length; i++) {
				// para cada coluna à direita da diagonal
				if (!canonical)
					a[j][i] /= a[j][j];
				for (int k = j + 1; k < a.length; k++)
					a[k][i] -= a[j][i] * a[k][j];
				if (y != null)
					for (int k = 0; k < y.length; k++)
						y[k][i] -= a[j][i] * y[k][j];
				if (!mult)
					a[j][i] = 0.;
				ArrayUtils.show(a, y);
			}
		}
	}

	private static void swapColumn(int i, int k, double[][] a, double[][] y) {
		for (int j = 0; j < a.length; j++) {
			// para cada linha...
			double aux = a[j][i];
			a[j][i] = a[j][k];
			a[j][k] = aux;
		}
		if (y != null) {
			for (int j = 0; j < y.length; j++) {
				// para cada linha...
				double aux = y[j][i];
				y[j][i] = y[j][k];
				y[j][k] = aux;
			}
		}
	}

	// ---------------------- GAUSS - CASOS PARTICULARES ----------------------

	/**
	 * Resolve o sistema linear <strong>tridiagonal</strong> pelo método da
	 * eliminação de Gauss
	 * 
	 * @param dss diagonal secundária inferior
	 * @param dp  diagonal principal
	 * @param dsi diagonal secundária superior
	 * @param y   vetor dos termos independentes
	 * @return vetor da solução do sistema
	 */
	public static double[] solveGaussTri(double[] dsi, double[] dp, double[] dss, double[] y) {
		int n = dp.length;
		double[] out = new double[n];

		for (int i = 1; i < n; i++) {
			// calcula o multiplicador, atualiza a diagonal principal e o
			// vetor dos escalares
			dsi[i] /= dp[i - 1];
			dp[i] -= dsi[i] * dss[i - 1];
			y[i] -= dsi[i] * y[i - 1];
		}

		// Acha as soluções do sistema
		out[n - 1] = y[n - 1] / dp[n - 1];

		// substituição regressiva
		for (int i = n - 2; i >= 0; i--)
			out[i] = (y[i] - dss[i] * out[i + 1]) / dp[i];

		return out;
	}

	/**
	 * Resolve o sistema linear pelo método da eliminação de Gauss para matrizes
	 * tridiagonais periódicas
	 * 
	 * @param n
	 * @param dss
	 * @param dp
	 * @param dsi
	 * @param ys
	 * @param Solucao
	 */
	public static double[] solveGaussPer(double[] dsi, double[] dp, double[] dss, double[] ys) {
		int n = dp.length;

		// Ãy=(a1,...,0)T
		double[] si = Arrays.copyOf(dsi, n);
		double[] p = Arrays.copyOf(dp, n);
		double[] ss = Arrays.copyOf(dss, n);

		double[] a = new double[n];
		for (int i = 0; i < n; i++)
			a[i] = 0;
		a[0] = dsi[0];

		double[] y = Vec.solveGaussTri(si, p, ss, a);

		// Ãz=(0,...,cn)T
		si = Arrays.copyOf(dsi, n);
		p = Arrays.copyOf(dp, n);
		ss = Arrays.copyOf(dss, n);
		for (int i = 0; i < n; i++)
			a[i] = 0;
		a[n - 1] = dss[n - 1];

		double[] z = Vec.solveGaussTri(si, p, ss, a);

		// Ãw=d
		double[] w = Vec.solveGaussTri(dsi, dp, dss, ys);

		// resolve o sistema linear 2x2
		double[] sol = Vec.solve2x2(new double[][] { { 1 + z[0], y[0] }, { z[n - 1], 1 + y[n - 1] } },
				new double[] { w[0], w[n - 1] });

		// solução do sistema tridiagonal periódico
		double[] x = new double[n];
		x[0] = sol[0];
		x[n - 1] = sol[1];

		for (int i = 1; i < n - 1; i++)
			x[i] = w[i] - x[n - 1] * y[i] - x[0] * z[i];

		return x;
	}

	// ---------------------- ITERATIVOS ----------------------

	private static final int MAX = 1000;

	private static final double TOL = 1E-6;

	/**
	 * Função que resolve um sistema linear de variáveis reais pelo método da
	 * iteração (também conhecido como <i>aproximações sucessivas</i>, de
	 * <i>Jacobi</i> ou dos <i>deslocamentos simultâneos</i>)
	 * 
	 * @param a matriz dos coeficientes
	 * @param y vetor dos termos independentes
	 * @return vetor da solução do sistema
	 */
	public static double[] solveIte(double[][] a, double[] y) {
		double[][] g = new double[y.length][a[0].length];
		double[] h = new double[y.length];

		gh(a, y, g, h, true);

		if (FLAG_PRINT) {
			ArrayUtils.show(a);
			ArrayUtils.show(g);
		}

		double[] out = Arrays.copyOf(h, h.length); // x_0

		double eps = Double.MAX_VALUE;
		int ite = 0;
		while (ite < MAX) {
			// x_i+1 = g x_i ...
			double[] xi = transf(g, out);
			// ... + h
			xi = sum(xi, h);

			double epsi = norma(sub(out, xi)); // erro
			if (epsi < TOL /* convergência */ || epsi > eps /* aumento do erro */)
				break;
			out = xi;
			eps = epsi;
			ite++;
		}

		System.out.println("Gauss-Jacobi convergiu em " + ite + " iterações");

		return out;
	}

	/**
	 * Função que resolve um sistema linear de variáveis reais pelo método de
	 * Gauss-Seidel (também conhecido como dos <i>deslocamentos sucessivos</i>). Ele
	 * é similar ao {@link #solveIte(double[][], double[]) método da iteração},
	 * porém utiliza-se os valores calculados ao longo da iteração para calcular a
	 * aproximação da iteração seguinte.
	 * 
	 * @param a matriz dos coeficientes
	 * @param y vetor dos termos independentes
	 * @return vetor da solução do sistema
	 */
	public static double[] solveSeidel(double[][] a, double[] y) {
		double[][] g = new double[y.length][a[0].length];
		double[] h = new double[y.length];

		gh(a, y, g, h, true);

		double[] out = Arrays.copyOf(h, h.length); // x_0

		int ite = 0;
		while (ite < MAX) {
			double[] xi = Arrays.copyOf(out, out.length); // x_i

			// x_i+1=g x_i + h
			for (int i = 0; i < g.length; i++) {
				double v = 0.;
				for (int j = 0; j < g[i].length; j++)
					v += g[i][j] * out[j];
				out[i] = v + h[i];
			}

			double epsi = norma(sub(out, xi)); // erro
			if (epsi < TOL /* convergência */ )
				break;
			ite++;
		}

		System.out.println("Gauss-Seidel convergiu em " + ite + " iterações");

		return out;
	}

	/**
	 * Função que resolve um sistema linear de variáveis reais pelo método da
	 * sobre-relaxação sucessiva. Ele é similar ao
	 * {@link #solveSeidel(double[][], double[]) método de Gauss-Seidel}, porém
	 * sobrepondo a cada iteração os valores calculados por Gauss-Seidel com aqueles
	 * da iteração anterior.
	 * 
	 * @param a matriz dos coeficientes
	 * @param y vetor dos termos independentes
	 * @param w peso
	 * @return vetor da solução do sistema
	 */
	public static double[] solveSOR(double[][] a, double[] y, double w) {
		double[][] g = new double[y.length][a[0].length];
		double[] h = new double[y.length];

		gh(a, y, g, h, true);

		double[] out = Arrays.copyOf(h, h.length); // x_0

		int ite = 0;
		while (ite < MAX) {
			double[] xi = Arrays.copyOf(out, out.length); // x_i

			// x_i+1=g x_i + h
			for (int i = 0; i < g.length; i++) {
				double v = 0.;
				for (int j = 0; j < g[i].length; j++)
					v += g[i][j] * out[j];
				out[i] = (1 - w) * out[i] + w * (v + h[i]);
			}

			double epsi = norma(sub(out, xi)); // erro
			if (epsi < TOL /* convergência */ )
				break;
			ite++;
		}

		System.out.println("SOR convergiu em " + ite + " iterações");

		return out;
	}

	/**
	 * Função que resolve um sistema linear de variáveis reais pelo método do
	 * relaxamento (também conhecido como de <i>Southwell</i>)
	 * 
	 * @param a matriz dos coeficientes
	 * @param y vetor dos termos independentes
	 * @return vetor da solução do sistema
	 */
	public static double[] solveRelax(double[][] a, double[] y) {
		double[][] g = new double[y.length][a[0].length];
		double[] h = new double[y.length];

		gh(a, y, g, h, false);

		double[] out = Arrays.copyOf(h, h.length); // x_0
		// r_0 = g x_0 ...
		double[] r = transf(g, out);
		// ... + h
		r = sum(r, h);

		int ite = 0;
		while (ite < MAX) {

			// procura pelo maior resíduo
			double max = Double.MIN_VALUE;
			int s = -1;
			for (int i = 0; i < r.length; i++) {
				double ra = Math.abs(r[i]);
				if (ra > max) {
					max = ra;
					s = i;
				}
			}

			if (max < TOL)
				break;

			out[s] += r[s]; // incremento
			// atualiza resíduos...
			for (int i = 0; i < r.length; i++)
				if (i != s) // ... exceto daquele que será zerado
					r[i] += g[i][s] * r[s];
			r[s] = 0.; // zera resíduo

			ite++;
		}

		System.out.println("Southwell convergiu em " + ite + " iterações");

		return out;
	}

	/**
	 * Função que calcula a matriz <code>g</code> e o vetor <code>h</code> para os
	 * métodos {@link #solveIte(double[][], double[]) da iteração},
	 * {@link #solveSeidel(double[][], double[]) de Seidel} e
	 * {@link #solveRelax(double[][], double[]) do relaxamento} que são usados para
	 * calcular a próxima aproximação do método iterativo
	 * 
	 * @param a      matriz dos coeficientes
	 * @param y      vetor dos termos independentes
	 * @param g      matriz da iteração
	 * @param h      vetor da iteração
	 * @param seidel <code>true</code> para os métodos
	 *               {@link #solveIte(double[][], double[]) da iteração} e
	 *               {@link #solveSeidel(double[][], double[]) de Seidel} (diagonais
	 *               nulas), <code>false</code> para o método
	 *               {@link #solveRelax(double[][], double[]) do relaxamento}
	 *               (diagonais com valor negativo unitário)
	 */
	private static void gh(double[][] a, double[] y, double[][] g, double[] h, boolean seidel) {
		for (int r = 0; r < a.length; r++) {
			h[r] = y[r] / a[r][r];
			for (int col = 0; col < a[r].length; col++)
				if (col != r)
					g[r][col] = -a[r][col] / a[r][r];
				else if (!seidel)
					g[r][col] = -1.;
		}
	}

	// ======================= auto-valores e auto-vetores =======================

	/**
	 * Função que retorna o polinômio característico uma matriz det(xI-A)
	 * 
	 * @param a matriz quadrada
	 * @return coeficientes do polinômio característico, sendo que cada índice do
	 *         vetor corresponde ao expoente da variável
	 */
	public static double[] polCarac(double[][] a) {
		double[] out = new double[a.length + 1];
		out[a.length] = 1.; // polinômio mônico
		if (a.length == 2) {
			out[0] = det2(a);
			out[1] = -a[0][0] - a[1][1]; // -traço
		} else {
			// TODO Auto-generated method stub
		}
		return out;
	}

	public static double spectralRadius(double[][] a) {
		// TODO Auto-generated method stub
		return 0;
	}

	// ============================== IO ==============================

	// ------------------------- Java storage -------------------------

	/**
	 * Função que salva um vetor de número decimais em um arquivo
	 * 
	 * @param file objeto {@link File} com o nome e caminho do arquivo a ser
	 *             carregado
	 * @param y    vetor de números decimais a ser salvo
	 */
	public static void saveVec(File file, double[] y) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(y);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Função que salva uma matriz de número decimais em um arquivo
	 * 
	 * @param file objeto {@link File} com o nome e caminho do arquivo a ser
	 *             carregado
	 * @param a    matriz de números decimais a ser salva
	 */
	public static void saveMtx(File file, double[][] a) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(a);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Função que carrega um vetor de número decimais de um arquivo
	 * 
	 * @param file objeto {@link File} com o nome e caminho do arquivo a ser
	 *             carregado
	 * @return vetor de número decimais
	 */
	public static double[] loadVec(File file) {
		double[] objects = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			objects = (double[]) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return objects;
	}

	/**
	 * Função que carrega uma matriz de número decimais de um arquivo
	 * 
	 * @param file objeto {@link File} com o nome e caminho do arquivo a ser
	 *             carregado
	 * @return matriz de números decimais
	 */
	public static double[][] loadMtx(File file) {
		double[][] objects = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			objects = (double[][]) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return objects;
	}

	// ------------------------- XML -------------------------

	/**
	 * Função que gera um código XML com uma matriz de números complexos
	 * 
	 * @param matrix matriz de de números complexos
	 * @return código XML
	 */
	public static String toXml(Complex[][] matrix) {
		StringBuilder out = new StringBuilder("<matrix>\n");
		for (int i = 0; i < matrix.length; i++) {
			out.append("<row>");
			for (int j = 0; j < matrix[i].length; j++)
				out.append(String.format("<cell value=\"%g;%g\"/>", matrix[i][j].getRe(), matrix[i][j].getIm()));
			out.append("</row>\n");
		}
		out.append("</matrix>\n");
		return out.toString();
	}

	// =========================================================================
	// ================================ N-UPLAS ================================
	// =========================================================================

	/**
	 * Função que afirma se um <strong>conjunto</strong> binário é igual a outro
	 * 
	 * @param obj11 um dos objetos do primeiro conjunto
	 * @param obj12 outro objeto do primeiro conjunto
	 * @param obj21 um dos objetos do segundo conjunto
	 * @param obj22 outro objeto do segundo conjunto
	 * @return <code>true</code> se os dois objetos são iguais, <code>false</code>
	 *         senão
	 */
	public static boolean equalBinary(Object obj11, Object obj12, Object obj21, Object obj22) {
		if (obj11 == null || obj12 == null || obj21 == null || obj22 == null)
			return false;

		boolean b1 = obj21.equals(obj11);
		boolean b2 = obj22.equals(obj11);
		boolean b3 = obj21.equals(obj12);
		boolean b4 = obj22.equals(obj12);

		return (b1 && b4) || (b2 && b3);
	}

	/**
	 * Função que afirma se um <strong>conjunto</strong> binário de inteiros é igual
	 * a outro
	 * 
	 * @param i11 um dos inteiros do primeiro conjunto
	 * @param i12 outro inteiro do primeiro conjunto
	 * @param i21 um dos inteiros do segundo conjunto
	 * @param i22 outro inteiro do segundo conjunto
	 * @return <code>true</code> se os dois inteiros são iguais, <code>false</code>
	 *         senão
	 */
	public static boolean equalBinary(int i11, int i12, int i21, int i22) {
		boolean b1 = i11 == i21;
		boolean b2 = i12 == i22;

		boolean b3 = i12 == i21;
		boolean b4 = i11 == i22;

		return (b1 && b2) || (b3 && b4);
	}

	/**
	 * Função que afirma se um <strong>conjunto</strong> ternário é igual a outro
	 * 
	 * @param obj11 um dos objetos do primeiro conjunto
	 * @param obj12 outro objeto do primeiro conjunto
	 * @param obj13 terceiro objeto do primeiro conjunto
	 * @param obj21 um dos objetos do segundo conjunto
	 * @param obj22 outro objeto do segundo conjunto
	 * @param obj23 terceiro objeto do segundo conjunto
	 * @return <code>true</code> se os três objetos são iguais, <code>false</code>
	 *         senão
	 */
	public static boolean equalTernary(Object obj11, Object obj12, Object obj13, Object obj21, Object obj22,
			Object obj23) {
		if (obj11 == null || obj12 == null || obj13 == null || obj21 == null || obj22 == null || obj23 == null)
			return false;

		boolean b1 = obj11.equals(obj21);
		boolean b2 = obj12.equals(obj22);
		boolean b3 = obj13.equals(obj23);

		boolean b4 = obj11.equals(obj22);
		boolean b5 = obj12.equals(obj23);
		boolean b6 = obj13.equals(obj21);

		boolean b7 = obj11.equals(obj23);
		boolean b8 = obj12.equals(obj21);
		boolean b9 = obj13.equals(obj22);

		return (b1 && b2 && b3) || (b4 && b5 && b6) || (b7 && b8 && b9) || (b1 && b5 && b9) || (b2 && b6 && b7)
				|| (b3 && b4 && b8);
	}
}