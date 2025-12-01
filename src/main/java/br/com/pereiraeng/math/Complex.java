package br.com.pereiraeng.math;

import br.com.pereiraeng.core.ExtendedMath;

/**
 * Classe do objeto que representa um número complexo, trazendo também uma série
 * de métodos com as principais operações com tais números.
 * 
 * @author Philipe PEREIRA
 */
public class Complex extends Number implements Comparable<Complex> {
	private static final long serialVersionUID = 1L;

	/**
	 * 1e^j120
	 */
	public static final Complex ALPHA = new Complex(1, ExtendedMath.PI_23, false);

	/**
	 * 1e^j240
	 */
	public static final Complex ALPHA_2 = new Complex(1, -ExtendedMath.PI_23, false);

	/**
	 * infinito complexo
	 */
	public static final Complex INFINITY = new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

	/**
	 * Número imáginário
	 */
	public static final Complex i = new Complex(0, 1);

	/**
	 * se a variável {@link Complex#cartesian cartesian} é <code>true</code>, esse
	 * número representa a parte real, senão o módulo do número
	 */
	private double x0;

	/**
	 * se a variável {@link Complex#cartesian cartesian} é <code>true</code>, esse
	 * número representa a parte imaginária, senão o argumento da representação
	 * polar
	 */
	private double x1;

	/**
	 * <code>true</code> se o número complexo está escrito na forma cartesina,
	 * <code>false</code> se estiver escrito na forma polar
	 */
	private boolean cartesian;

	/**
	 * Cria um objeto do tipo <code>Complex</code> igual a um outro número fornecido
	 * 
	 * @param c número a ser copiado
	 */
	public Complex(Complex c) {
		this(c.getX0(), c.getX1(), c.isCartesian());
	}

	/**
	 * Cria um objeto do tipo <code>Complex</code> que representa um número complexo
	 * escrito em coordenadas cartesianas
	 * 
	 * @param x0 parte real
	 * @param x1 parte imaginária
	 */
	public Complex(double x0, double x1) {
		this(x0, x1, true);
	}

	/**
	 * Cria um objeto do tipo <code>Complex</code> que representa um número complexo
	 * 
	 * @param x0        se {@link Complex#cartesian cartesian} é <code>true</code>,
	 *                  é a parte real do número, senão é o módulo
	 * @param x1        se {@link Complex#cartesian cartesian} é <code>true</code>,
	 *                  é a parte imaginária do número, senão é o argumento em
	 *                  radianos
	 * @param cartesian <code>true</code>, os argumentos estão em coordenadas
	 *                  cartesianas, <code>false</code> em coordenadas polares.
	 */
	public Complex(double x0, double x1, boolean cartesian) {
		this.setCartesian(cartesian);
		this.setX0(x0);
		this.setX1(x1);
	}

	/**
	 * Cria um objeto do tipo <code>Complex</code> que representa um número complexo
	 * nulo
	 */
	public Complex() {
		this(0., 0., true);
	}

	/**
	 * Cria um objeto do tipo <code>Complex</code> a partir de um vetor que contém
	 * na primeira posição a parte real e na segunda a parte imaginária do número
	 * 
	 * @param ri vetor com a parte real e imaginária do número
	 */
	public Complex(double[] ri) {
		this(ri[0], ri[1], true);
	}

	// ------------------------------- GETTERS -------------------------------

	@Override
	public double doubleValue() {
		return getMod();
	}

	@Override
	public float floatValue() {
		return (float) getMod();
	}

	@Override
	public int intValue() {
		return (int) getMod();
	}

	@Override
	public long longValue() {
		return (long) getMod();
	}

	@Override
	public int compareTo(Complex o) {
		return Double.compare(this.getMod(), o.getMod());
	}

	/**
	 * Função que indica se o número complexo está representado na forma cartesiana
	 * ou polar
	 * 
	 * @return <code>true</code> se o número complexo está escrito na forma
	 *         cartesina, <code>false</code> se estiver escrito na forma polar
	 */
	public boolean isCartesian() {
		return cartesian;
	}

	private void setCartesian(boolean cartesian) {
		this.cartesian = cartesian;
	}

	// as funções getX0 e getX1 são 'protected' pois oferece-se a possibilidade
	// de reescrevê-las em uma subclasse, de modo a 'alduterar' intencionalmente
	// todas as funcionalidades do número complexo

	protected double getX0() {
		return x0;
	}

	protected double getX1() {
		return x1;
	}

	private void setX0(double x0) {
		this.x0 = x0;
	}

	private void setX1(double x1) {
		this.x1 = x1;
		if (!this.isCartesian() ? (this.x1 < -Math.PI || this.x1 > Math.PI) : false)
			this.x1 = ExtendedMath.circularRadians(this.x1);
	}

	/**
	 * Retorna um vetor de duas posições com a parte real e imaginária do número
	 * complexo
	 * 
	 * @return vetor com o número em coordenadas cartesianas
	 */
	public double[] getArray() {
		return new double[] { this.getRe(), this.getIm() };
	}

	/**
	 * Função que retorna a parte real do número complexo
	 * 
	 * @return parte real do número complexo
	 */
	public double getRe() {
		if (isCartesian())
			return this.getX0();
		else
			return this.getX0() * Math.cos(this.getX1());
	}

	public void set(Complex c) {
		if (c.isCartesian()) {
			this.setRe(c.getRe());
			this.setIm(c.getIm());
		} else {
			this.setMod(c.getMod());
			this.setArg(c.getArg());
		}
	}

	/**
	 * Função que altera a parte real do número complexo, mantendo sua parte
	 * imaginária
	 * 
	 * @param re nova parte real do número complexo
	 */
	public void setRe(double re) {
		if (isCartesian())
			this.setX0(re);
		else {
			double im = this.getIm();
			double mod = Math.hypot(re, im), arg = Math.atan2(im, re);
			this.setX0(mod);
			this.setX1(arg);
		}
	}

	/**
	 * Função que retorna a parte imaginária do número complexo
	 * 
	 * @return parte imaginária do número complexo
	 */
	public double getIm() {
		if (isCartesian())
			return this.getX1();
		else
			return this.getX0() * Math.sin(this.getX1());
	}

	/**
	 * Função que altera a parte imaginária do número complexo, mantendo sua parte
	 * real
	 * 
	 * @param im nova parte imaginária do número complexo
	 */
	public void setIm(double im) {
		if (isCartesian())
			this.setX1(im);
		else {
			double re = this.getRe();
			double mod = Math.hypot(re, im), arg = Math.atan2(im, re);
			this.setX0(mod);
			this.setX1(arg);
		}
	}

	/**
	 * Função que retorna o módulo de um dado número
	 * 
	 * @return módulo do número complexo
	 */
	public double getMod() {
		return norma(this);
	}

	/**
	 * Função que altera o módulo do número complexo, mantendo seu argumento
	 * 
	 * @param mod novo módulo do número complexo
	 */
	public void setMod(double mod) {
		if (this.isCartesian()) {
			double oldMod = this.getMod();
			if (oldMod == 0.)
				new IllegalArgumentException("Não se pode determinar o módulo de um complexo nulo");
			double r = mod / oldMod;
			this.setX0(getX0() * r);
			this.setX1(getX1() * r);
		} else
			this.setX0(mod);
	}

	/**
	 * Função que retorna o quadrado do módulo de um dado número complexo
	 * 
	 * @return módulo do número complexo ao quadrado
	 */
	public double getMod2() {
		if (isCartesian())
			return Math.pow(this.getX0(), 2) + Math.pow(this.getX1(), 2);
		else
			return Math.pow(this.getX0(), 2);
	}

	/**
	 * Função que retorna o argumento do número complexo
	 * 
	 * @return argumento do número complexo, em radianos
	 */
	public double getArg() {
		if (this.isCartesian())
			return Math.atan2(this.getX1(), this.getX0());
		else
			return this.getX1();
	}

	/**
	 * Função que altera o argumento do número complexo, mantendo seu módulo
	 * 
	 * @param arg novo argumento do número complexo, em radianos
	 */
	public void setArg(double arg) {
		if (this.isCartesian()) {
			double mod = this.getMod();
			this.setX0(mod * Math.cos(arg));
			this.setX1(mod * Math.sin(arg));
		} else
			this.setX1(ExtendedMath.circularRadians(arg));
	}

	@Override
	public String toString() {
		if (isCartesian())
			return String.format("(%.5g; %.5g)", this.getX0(), this.getX1());
		else
			return String.format("%.5g \u2220%.4g\u00B0", this.getX0(), Math.toDegrees(this.getX1()));
	}

	public boolean isNull() {
		return equals(this, new Complex());
	}

	// CONVERSÃO CARTESIANO -> POLAR

	/**
	 * Função que gera outro número complexo, com o mesmo valor numérico deste, mas
	 * em coordenadas cartesianas
	 * 
	 * @param n
	 * @return
	 */
	public static Complex toCartesian(Complex n) {
		return new Complex(n.getRe(), n.getIm());
	}

	public Complex toCartesian() {
		return toCartesian(this);
	}

	public static Complex[] toCartesian(Complex... c) {
		Complex[] out = new Complex[c.length];
		for (int i = 0; i < c.length; i++)
			out[i] = toCartesian(c[i]);
		return out;
	}

	public void convert2cartesian() {
		if (!isCartesian()) {
			double x = this.getRe();
			double y = this.getIm();

			this.setCartesian(true);
			this.setX0(x);
			this.setX1(y);
		}
	}

	// CONVERSÃO POLAR -> CARTESIANO

	public static Complex toPolar(Complex n) {
		return new Complex(n.getMod(), n.getArg(), false);
	}

	public Complex toPolar() {
		return toPolar(this);
	}

	public static Complex[] toPolar(Complex... c) {
		Complex[] out = new Complex[c.length];
		for (int i = 0; i < c.length; i++)
			out[i] = toPolar(c[i]);
		return out;
	}

	public void convert2polar() {
		if (isCartesian()) {
			double mod = this.getMod();
			double arg = this.getArg();

			this.setCartesian(false);
			this.setX0(mod);
			this.setX1(arg);
		}
	}

	// EQUALS

	public static boolean equals(Complex c, Complex d) {
		Complex[] a = toCartesian(c, d);
		return ExtendedMath.equals(a[0].getX0(), a[1].getX0()) && ExtendedMath.equals(a[0].getX1(), a[1].getX1());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Complex) {
			Complex c = (Complex) obj;
			return equals(this, c);
		}
		return false;
	}

	// INFINITO

	public static boolean isInfinity(Complex c) {
		Complex a = toCartesian(c);
		return Double.isInfinite(a.getRe()) || Double.isInfinite(a.getIm());
	}

	// NULO

	public static boolean isNull(Complex value) {
		return ExtendedMath.equals(value.getMod(), 0.);
	}

	// NORMA

	public static double norma(Complex a) {
		if (a.isCartesian())
			return Math.hypot(a.getX0(), a.getX1());
		else
			return a.getX0();
	}

	public static double[] norma(Complex... as) {
		double[] out = new double[as.length];
		for (int i = 0; i < out.length; i++)
			out[i] = as[i].getMod();
		return out;
	}

	// CONJUGADO

	/**
	 * Função que retorna um novo número complexo, inverso daquele que foi dado como
	 * argumento
	 * 
	 * @param c número complexo dado
	 * @return novo número complexo, inverso daquele que foi dado
	 */
	public static Complex conj(Complex c) {
		Complex out = new Complex(c);
		out.conj();
		return out;
	}

	public void conj() {
		this.setX1(-this.getX1());
	}

	// SOMA

	public static Complex sum(Complex... c) {
		Complex out = new Complex(c[0]);

		for (int i = 1; i < c.length; i++)
			out.sum(c[i]);

		return out;
	}

	public void sum(Complex c) {
		if (!this.isCartesian())
			this.convert2cartesian();

		this.setX0(this.getX0() + c.getRe());
		this.setX1(this.getX1() + c.getIm());
	}

	// SUBTRAÇÃO

	public static Complex sub(Complex c, Complex d) {
		Complex out = new Complex(c);
		out.sub(d);
		return out;
	}

	public void sub(Complex c) {
		if (!this.isCartesian())
			this.convert2cartesian();

		this.setX0(this.getX0() - c.getRe());
		this.setX1(this.getX1() - c.getIm());
	}

	// MULTIPLICAÇÃO POR ESCALAR

	public static Complex mult(double a, Complex c) {
		Complex out = new Complex(c);
		out.mult(a);
		return out;
	}

	public void mult(double a) {
		this.setX0(a * this.getX0());
		if (this.isCartesian())
			this.setX1(a * this.getX1());
		else if (this.getX0() < 0) {
			this.setX0(-this.getX0());
			this.setX1(this.getX1() + Math.PI);
		}
	}

	// MULTIPLICAÇÃO

	public static Complex mult(Complex... c) {
		Complex out = new Complex(c[0]);

		for (int i = 1; i < c.length; i++)
			out.mult(c[i]);

		return out;
	}

	public void mult(Complex c) {
		if (c.isCartesian() && this.isCartesian()) {
			double x = c.getX0() * this.getX0() - c.getX1() * this.getX1();
			double y = c.getX0() * this.getX1() + c.getX1() * this.getX0();

			this.setX0(x);
			this.setX1(y);
		} else {
			if (this.isCartesian())
				this.convert2polar();

			this.setX0(this.getX0() * c.getMod());
			this.setArg(this.getX1() + c.getArg());
		}
	}

	// DIVISÃO

	/**
	 * Função que retorna o resultado da divisão de um número complexo por outro
	 * 
	 * @param num número a ser dividido
	 * @param den número divisor
	 * @return resultado da divisão
	 */
	public static Complex div(Complex num, Complex den) {
		Complex out = new Complex(num);
		out.div(den);
		return out;
	}

	public static Complex div(Complex num, double den) {
		Complex out = new Complex(num);
		out.div(den);
		return out;
	}

	private static final double ZERO = 1E-20;

	public void div(double den) {
		if (den < ZERO)
			throw new IllegalArgumentException("Não é possível dividir por um número complexo nulo.");

		if (this.isCartesian()) {
			if (Double.isInfinite(den)) {
				this.setX0(0.);
				this.setX1(0.);
			} else {
				this.setX0(this.getX0() / den);
				this.setX1(this.getX1() / den);
			}
		} else {
			if (Double.isInfinite(den))
				this.setX0(0.);
			else
				this.setX0(this.getX0() / den);
		}
	}

	/**
	 * Função que divide este número por outro
	 * 
	 * @param den número divisor
	 */
	public void div(Complex den) {
		if (this.isCartesian() && den.isCartesian()) {
			double norma2 = den.getMod2();

			if (norma2 < ZERO)
				throw new IllegalArgumentException("Não é possível dividir por um número complexo nulo.");

			if (Double.isInfinite(norma2)) {
				this.setX0(0.);
				this.setX1(0.);
			} else {
				double x = (this.getRe() * den.getRe() + this.getIm() * den.getIm()) / norma2;
				double y = (this.getIm() * den.getRe() - this.getRe() * den.getIm()) / norma2;

				this.setX0(x);
				this.setX1(y);
			}
		} else {
			if (this.isCartesian())
				this.convert2polar();

			this.setX0(this.getX0() / den.getMod());
			this.setArg(this.getX1() - den.getArg());
		}
	}

	// INVERSÃO

	/**
	 * Função que retorna o inverso de um número complexo, descrito a partir de suas
	 * coordenadas em coordenadas cartesianas
	 * 
	 * @param x parte real
	 * @param y parte imaginária
	 * @return inverso do número complexo
	 */
	public static Complex inv(double x, double y) {
		double norma2 = Math.pow(x, 2) + Math.pow(y, 2);
		return new Complex(x / norma2, -y / norma2);
	}

	/**
	 * Função que retorna o inverso de um número complexo
	 * 
	 * @param c número complexo
	 * @return inverso do número
	 */
	public static Complex inv(Complex c) {
		Complex out = new Complex(c);
		out.inv();
		return out;
	}

	/**
	 * Função que converte este número complexo em seu inverso
	 */
	public void inv() {
		if (this.isCartesian()) {
			double norma2 = this.getMod2();
			if (Double.isInfinite(norma2)) {
				this.setX0(0.);
				this.setX1(0.);
			} else {
				this.setX0(this.getX0() / norma2);
				this.setX1(-this.getX1() / norma2);
			}
		} else {
			this.setX0(1 / this.getX0());
			this.setX1(-this.getX1());
		}
	}

	// POTÊNCIA

	public static Complex pow(Complex c, double power) {
		Complex out = new Complex(c);
		out.pow(power);
		return out;
	}

	public void pow(double power) {
		if (this.isCartesian())
			this.convert2polar();

		this.setX0(Math.pow(this.getX0(), power));
		this.setArg(this.getX1() * power);
	}

	// RAIZ QUADRADA

	/**
	 * Função que retorna a raiz quadrada de um número complexo
	 * 
	 * @param c número complexo
	 * @return raiz quadrada do número
	 */
	public static Complex sqrt(Complex c) {
		Complex out = new Complex(c);
		out.sqrt();
		return out;
	}

	/**
	 * Função que converte este número complexo em sua raiz quadrada
	 */
	public void sqrt() {
		if (this.isCartesian()) {
			double norma = this.getMod();
			double re = this.getX0();

			this.setX0(Math.sqrt((re + norma) / 2));
			this.setX1(Math.signum(this.getX1()) * Math.sqrt((-re + norma) / 2));
		} else {
			this.setX0(Math.sqrt(this.getX0()));
			this.setX1(this.getX1() / 2);
		}
	}

	// VERSOR

	/**
	 * Função que retorna o número complexo normalizado, ou seja:
	 * 
	 * u/|u|
	 * 
	 * @param c número complexo
	 * @return versor associado ao número complexo
	 */
	public static Complex getUnitVector(Complex c) {
		Complex out = new Complex(c);
		out.getUnitVector();
		return out;
	}

	/**
	 * Função que divide este número complexo pela sua norma, de modo que seu módulo
	 * seja 1
	 */
	public void getUnitVector() {
		if (this.isCartesian()) {
			double norma = this.getMod();
			this.setX0(this.getX0() / norma);
			this.setX1(this.getX1() / norma);
		} else
			this.setX0(1);
	}

	/**
	 * Função que retorna uma matriz identidade complexa de uma dada ordem
	 * 
	 * @param n ordem da matriz
	 * @return matriz de Complex com (1,0) na diagonal e (0,0) no resto
	 */
	public static Complex[][] idComplex(int n) {
		Complex[][] out = new Complex[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == j)
					out[i][j] = new Complex(1., 0.);
				else
					out[i][j] = new Complex();
			}
		}
		return out;
	}

	/**
	 * Função que imprime no Console uma matriz de número complexos
	 * 
	 * @param mat       matriz de número complexo
	 * @param cartesian <code>true</code> para mostrar na forma cartesiana,
	 *                  <code>false</code> para mostrar na forma polar
	 * @param split     <code>true</code> para exibir em duas matrizes distintas
	 *                  cada um dos número reais (seja a parte real ou imaginária,
	 *                  seja o módulo e o argumento), <code>false</code> para
	 *                  mostrar em uma só matriz
	 */
	public static void show(Complex[][] mat, boolean cartesian, boolean split) {
		if (split) {
			for (int i = 0; i < mat.length; i++) {
				for (int j = 0; j < mat.length; j++) {
					Complex c = mat[Math.max(i, j)][Math.min(i, j)];
					if (c != null)
						System.out.printf("%f\t", cartesian ? c.getRe() : c.getMod());
					else
						System.out.print("X\t");
				}
				System.out.println();
			}
			System.out.println("\n\n");
			for (int i = 0; i < mat.length; i++) {
				for (int j = 0; j < mat.length; j++) {
					Complex c = mat[Math.max(i, j)][Math.min(i, j)];
					if (c != null)
						System.out.printf("%f\t", cartesian ? c.getIm() : c.getArg());
					else
						System.out.print("X\t");
				}
				System.out.println();
			}
		} else {
			for (int i = 0; i < mat.length; i++) {
				for (int j = 0; j < mat[i].length; j++) {
					if (cartesian)
						System.out.print(mat[i][j].toCartesian());
					else
						System.out.print(mat[i][j].toPolar());
					if (j != mat[i].length - 1)
						System.out.print("\t");
				}
				System.out.println();
			}
		}
	}
}