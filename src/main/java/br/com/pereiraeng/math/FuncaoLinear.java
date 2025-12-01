package br.com.pereiraeng.math;

/**
 * Interface que caracteriza as classes que possuem uma função numérica disposta
 * de uma forma padronizada
 * 
 * @author Philipe PEREIRA
 *
 */
public abstract class FuncaoLinear implements FuncaoInt {

	@Override
	public double[] ft(double[] xt, double t, double[] vars) {
		double[][] a = getA();
		return a != null ? Vec.transf(a, vars) : vars;
	}

	@Override
	public double[] gt(double[] xt, double t, double[] vars) {
		double[][] b = getB();
		return b != null ? Vec.transf(b, vars) : vars;
	}

	public abstract double[][] getA();

	public abstract double[][] getB();
}
