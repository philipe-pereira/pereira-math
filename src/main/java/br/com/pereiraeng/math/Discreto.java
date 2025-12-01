package br.com.pereiraeng.math;

import java.util.Arrays;

/**
 * Classe dos objetos que representam um número inteiro cuja representação
 * necessita de mais de 64 bits (ou seja, um número que não pode ser
 * representado como um <code>long</code>), sendo maior que 2^63 ou menor que
 * -2^63+1.
 * 
 * @author Philipe PEREIRA
 *
 */
public class Discreto extends Number {
	private static final long serialVersionUID = 1L;

	private long[] blocks;

	public Discreto(int bytes) {
		if (bytes < 9)
			throw new IllegalArgumentException("Para 8 bytes ou menos, usa-se long");
		this.blocks = new long[(int) Math.ceil(bytes / 8f)];
	}

	public Discreto(Discreto d) {
		blocks = Arrays.copyOf(d.blocks, d.blocks.length);
	}

	@Override
	public double doubleValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float floatValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int intValue() {
		return (int) blocks[0];
	}

	@Override
	public long longValue() {
		return blocks[0];
	}

	// EQUALS

	public static boolean equals(Discreto c, Discreto d) {
		return Arrays.equals(c.blocks, d.blocks);
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof Discreto) {
			Discreto c = (Discreto) anObject;
			return equals(this, c);
		}
		return false;
	}

	// SOMA

	public static Discreto sum(Discreto... c) {
		Discreto out = new Discreto(c[0]);

		for (int i = 1; i < c.length; i++)
			out.sum(c[i]);

		return out;
	}

	public void sum(Discreto c) {
		// TODO this + c
	}

	// SUBTRAÇÃO

	public static Discreto sub(Discreto c, Discreto d) {
		Discreto out = new Discreto(c);
		out.sub(d);
		return out;
	}

	public void sub(Discreto c) {
		// TODO this - c
	}

	// MULTIPLICAÇÃO POR ESCALAR

	public static Discreto mult(double a, Discreto c) {
		Discreto out = new Discreto(c);
		out.mult(a);
		return out;
	}

	public void mult(double a) {
		// TODO this * a
	}

	// MULTIPLICAÇÃO

	public static Discreto mult(Discreto... c) {
		Discreto out = new Discreto(c[0]);

		for (int i = 1; i < c.length; i++)
			out.mult(c[i]);

		return out;
	}

	public void mult(Discreto c) {
		// TODO this * c
	}

	// DIVISÃO

	/**
	 * Função que retorna o resultado da divisão de um número Discretoo por
	 * outro
	 * 
	 * @param num
	 *            número a ser dividido
	 * @param den
	 *            número divisor
	 * @return resultado da divisão
	 */
	public static Discreto div(Discreto num, Discreto den) {
		Discreto out = new Discreto(num);
		out.div(den);
		return out;
	}

	/**
	 * Função que divide este número por outro
	 * 
	 * @param den
	 *            número divisor
	 */
	public void div(Discreto den) {
		// TODO this / den
	}

	// POTÊNCIA

	public static Discreto pow(Discreto c, double power) {
		Discreto out = new Discreto(c);
		out.pow(power);
		return out;
	}

	public void pow(double power) {
		// TODO this ^ power
	}

	// RAIZ QUADRADA

	/**
	 * Função que retorna a raiz quadrada de um número Discretoo
	 * 
	 * @param c
	 *            número Discretoo
	 * @return raiz quadrada do número
	 */
	public static Discreto sqrt(Discreto c) {
		Discreto out = new Discreto(c);
		out.sqrt();
		return out;
	}

	/**
	 * Função que converte este número o em sua raiz quadrada
	 */
	public void sqrt() {
		// TODO sqrt(this)
	}
}