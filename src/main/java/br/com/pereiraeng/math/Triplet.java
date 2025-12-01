package br.com.pereiraeng.math;

/**
 * Função que representa um <strong>vetor</strong> com três inteiros. Uma vez
 * que se trata de um vetor, a ordem é relevante para fins de discernimento do
 * elemento (em outras palavras, (x; y; z) <strong>não</strong> é o mesmo objeto
 * que (y; x; z))
 * 
 * @author Philipe PEREIRA
 *
 */
public class Triplet {

	private int a;
	
	private int b;
	
	private int c;

	/**
	 * Construtor do triplet
	 * 
	 * @param a primeiro inteiro
	 * @param b segundo inteiro
	 * @param c terceiro inteiro
	 */
	public Triplet(int a, int b, int c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof Triplet) {
			Triplet t = (Triplet) anObject;
			return this.a == t.a && this.b == t.b && this.c == t.c;
		}
		return false;
	}

	public int get1() {
		return a;
	}

	public int get2() {
		return b;
	}

	public int get3() {
		return c;
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(a + b + c).hashCode();
	}

	@Override
	public String toString() {
		return "(" + a + ";" + b + ";" + c + ")";
	}
}
