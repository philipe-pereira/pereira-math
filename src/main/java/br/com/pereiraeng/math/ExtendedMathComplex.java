package br.com.pereiraeng.math;

import br.com.pereiraeng.core.ExtendedMath;

public class ExtendedMathComplex {

	/**
	 * Função que retorna a exponencial de número complexo
	 * 
	 * @param c número complexo
	 * @return exponencial do número complexo
	 */
	public static Complex exp(Complex c) {
		return new Complex(Math.exp(c.getRe()), c.getIm(), false);
	}

	/**
	 * Função que retorna o logaritmo natural de número complexo
	 * 
	 * @param c número complexo
	 * @return logaritmo natural do número complexo
	 */
	public static Complex ln(Complex c) {
		return new Complex(Math.log(c.getMod()), c.getArg());
	}

	// FUNÇÕES TRIGONOMÉTRICAS

	/**
	 * Função que retorna o cosseno de um número complexo
	 * 
	 * @param c número complexo
	 * @return valor complexo do cosseno
	 */
	public static Complex cos(Complex c) {
		double re = c.getRe();
		double im = c.getIm();
		return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
	}

	/**
	 * Função que retorna o seno de um número complexo
	 * 
	 * @param c numero complexo
	 * @return valor complexo do seno
	 */
	public static Complex sin(Complex c) {
		double re = c.getRe();
		double im = c.getIm();
		return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
	}

	/**
	 * Função que retorna o tangente de um número complexo
	 * 
	 * @param c numero complexo
	 * @return valor complexo do tangente
	 */
	public static Complex tan(Complex c) {
		double re = c.getRe();
		double im = c.getIm();
		double mod = Math.cos(2 * re) + Math.cosh(2 * im);
		return new Complex(Math.sin(2 * re) / mod, Math.sinh(2 * im) / mod);
	}

	/**
	 * Função que retorna o cosseno hiperbólico de um número complexo
	 * 
	 * @param c número complexo
	 * @return valor complexo do cosseno hiperbólico
	 */
	public static Complex cosh(Complex c) {
		return new Complex(Math.cosh(c.getRe()) * Math.cos(c.getIm()), Math.sinh(c.getRe()) * Math.sin(c.getIm()));
	}

	/**
	 * Função que retorna o seno hiperbólico de um número complexo
	 * 
	 * @param c numero complexo
	 * @return valor complexo do seno hiperbólico
	 */
	public static Complex sinh(Complex c) {
		return new Complex(Math.sinh(c.getRe()) * Math.cos(c.getIm()), Math.cosh(c.getRe()) * Math.sin(c.getIm()));
	}

	/**
	 * Função que retorna a tangente hiperbólica de um número complexo
	 * 
	 * @param c numero complexo
	 * @return valor complexo da tangente hiperbólica
	 */
	public static Complex tanh(Complex c) {
		return tanh2(Complex.mult(2., c));
	}

	/**
	 * Função que retorna a tangente hiperbólica da metade de um número complexo
	 * 
	 * @param c numero complexo
	 * @return valor complexo da tangente hiperbólica da metade do número
	 */
	public static Complex tanh2(Complex c) {
		double den = 1. / (Math.cosh(c.getRe()) + Math.cos(c.getIm()));
		return Complex.mult(den, new Complex(Math.sinh(c.getRe()), Math.sin(c.getIm())));
	}

	// polinômio de Bessel

	/**
	 * Função que retorna o valor do polinômio de Bessel
	 * 
	 * @param x variável polinomial
	 * @param n ordem do polinômio
	 * @return valor do polinômio
	 */
	public static Complex besselPol(Complex x, int n) {
		Complex out = new Complex();

		Complex x2 = Complex.mult(.5, x);
		for (int k = 0; k <= n; k++)
			out.sum(Complex.mult(
					ExtendedMath.fatorial(n + k) / (ExtendedMath.fatorial(n - k) * ExtendedMath.fatorial(k)),
					Complex.pow(x2, k)));

		return out;
	}

	/**
	 * Função que retorna o valor do polinômio reverso de Bessel
	 * 
	 * @param x variável polinomial
	 * @param n ordem do polinômio
	 * @return valor do polinômio
	 */
	public static Complex revBesselPol(Complex x, int n) {
		Complex out = new Complex();

		for (int k = 0; k <= n; k++)
			out.sum(Complex.mult(
					ExtendedMath.fatorial(n + k)
							/ (Math.pow(2, k) * ExtendedMath.fatorial(n - k) * ExtendedMath.fatorial(k)),
					Complex.pow(x, n - k)));

		return out;
	}
}
