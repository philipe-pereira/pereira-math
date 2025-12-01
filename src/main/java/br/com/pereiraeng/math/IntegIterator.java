package br.com.pereiraeng.math;

import java.util.Iterator;
import java.util.Locale;

/**
 * Classe do objeto que integra numericamente uma {@link #fint função}
 * 
 * @author Philipe PEREIRA
 * @version September 1st, 2020
 */
public class IntegIterator implements Iterator<double[][]> {

	/**
	 * função a ser integrada
	 */
	private final FuncaoInt fint;

	/**
	 * número de iterações
	 */
	private final int n;

	/**
	 * passo de tempo
	 */
	private final double dt;

	/**
	 * constantes (i.e., valores válidos para qualquer {@link #getT() valor de
	 * tempo}
	 */
	private final double[] ctes;

	/**
	 * matriz com duas linhas:
	 * <ol start="0">
	 * <li>estado atual;</i>
	 * <li>{@link FuncaoInt#gt(double[], double, double[]) saída atual}.</i>
	 * </ol>
	 */
	private final double[][] xo;

	/**
	 * <ol start="0">
	 * <li>
	 * {@link Integrador#integraRungeKutta4fast(Funcao, double[], double, double, double[])
	 * Runge-Kutta 4 (rápido)}; </i>
	 * <li>
	 * {@link Integrador#integraDormandPrince(Funcao, double[], double, double, double[])
	 * Dormand-Prince (ode45)}; </i>
	 * </ol>
	 */
	private final int it;

	/**
	 * Construtor do integrador numérico
	 * 
	 * @param fint função a ser integrada
	 * @param n    número de iterações
	 * @param dt   passo de tempo
	 * @param x0   condições iniciais
	 * @param ctes constantes do cálculo
	 * @param vars número de variáveis intermediárias (dimensão do vetor
	 *             <code>vars</code> na
	 *             {@link FuncaoInt#refreshVars(double[], double, double[], double[])
	 *             função})
	 * @param it   tipo de integração (ver {@link #it})
	 */
	public IntegIterator(FuncaoInt fint, int n, double dt, double[] x0, double[] ctes, int vars, int it) {
		this.fint = fint;
		this.n = n;
		this.dt = dt;
		this.ctes = ctes;

		this.fint.refreshVars(x0, t, dt, ctes, this.vars = new double[vars]);

		this.xo = new double[2][];
		this.xo[0] = x0;
		this.xo[1] = this.fint.gt(x0, this.t, this.vars);

		this.it = it;
	}

	/**
	 * Construtor do integrador numérico
	 * 
	 * @param fint   função a ser integrada
	 * @param period tempo total da simulação
	 * @param dt     passo de tempo
	 * @param x0     condições iniciais
	 * @param ctes   constantes do cálculo
	 * @param vars   número de variáveis intermediárias (dimensão do vetor
	 *               <code>vars</code> na
	 *               {@link FuncaoInt#refreshVars(double[], double, double[], double[])
	 *               função})
	 * @param it     tipo de integração (ver {@link #it})
	 */
	public IntegIterator(FuncaoInt fint, double period, double dt, double[] x0, double[] ctes, int vars, int it) {
		this(fint, (int) (period / dt + 1), dt, x0, ctes, vars, it);
	}

	public double[][] getXo() {
		return xo;
	}

	/**
	 * Número de instantes de tempo para os quais há valores
	 * 
	 * @return número de iterações mais um
	 */
	public int getNo() {
		return n + 1;
	}

	public String print(double[][] ds, int... pos) {
		StringBuilder out = new StringBuilder(String.format(new Locale("pt", "BR"), "%g", ds[0][i]));
		ds[0][i] = t;
		for (int j = 1; j < ds.length; j++) {
			int k = 2 * (j - 1);
			ds[j][i] = xo[pos[k]][pos[k + 1]];
			out.append(String.format(new Locale("pt", "BR"), "\t%g", ds[j][i]));
		}
		return out.toString();
	}

	// ---------------------------------------------

	private transient int i = 0;

	private transient double t = 0;

	/**
	 * Vetor de variáveis temporárias, usadas para calcular as
	 * {@link FuncaoInt#ft(double[], double, double[]) derivadas} e a
	 * {@link FuncaoInt#gt(double[], double, double[]) saída}
	 */
	private transient double[] vars;

	public double getT() {
		return t;
	}

	@Override
	public boolean hasNext() {
		return i < n;
	}

	@Override
	public double[][] next() {
		double[] x;
		switch (it) {
		case 0: // Runge-kutta 4 rápido
			x = Integrador.integraRungeKutta4fast(this.fint, this.xo[0], this.t, this.dt, this.vars);
			break;
		case 1: // ode45
			x = Integrador.integraDormandPrince(this.fint, this.xo[0], this.t, this.dt, this.vars);
			break;
		default: // nenhum: repete a entrada
			x = null;
			break;
		}
		if (x != null)
			System.arraycopy(x, 0, this.xo[0], 0, x.length);
		this.t += this.dt;
		this.i++;

		this.fint.refreshVars(this.xo[0], this.t, this.dt, this.ctes, this.vars);

		this.xo[1] = this.fint.gt(this.xo[0], this.t, this.vars);
		return this.xo;
	}

	@Override
	public void remove() {
	}
}