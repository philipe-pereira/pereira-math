package br.com.pereiraeng.math;

/**
 * Classe do objeto que representa um <strong>vetor</strong> com dois inteiros. 
 * Uma vez que se trata de um vetor, a ordem é relevante para fins de discernimento 
 * do elemento (em outras palavras, (x; y) <strong>não</strong> é o mesmo objeto
 * que (y; x))
 * 
 * @author Philipe PEREIRA
 *
 */
public class DuplaV {

	private int i1;

	private int i2;

	/**
	 * Construtor do vetor de dois inteiros
	 * 
	 * @param i1 um inteiro
	 * @param i2 outro inteiro
	 */
	public DuplaV(int i1, int i2) {
		this.i1 = i1;
		this.i2 = i2;
	}

	/**
	 * Construtor do conjunto de dois inteiros
	 * 
	 * @param is vetor com pelo menos duas posições
	 */
	public DuplaV(int[] is) {
		this(is[0], is[1]);
	}

	public int get1() {
		return i1;
	}

	public void set1(int i1) {
		this.i1 = i1;
	}

	public int get2() {
		return i2;
	}

	public void set2(int i2) {
		this.i2 = i2;
	}

	public boolean contains(int i) {
		return i1 == i || i2 == i;
	}

	public boolean contains(DuplaV d) {
		return i1 == d.i1 || i1 == d.i2 || i2 == d.i1 || i2 == d.i2;
	}

	/**
	 * Função que retorna o outro número da dupla que não é aquele passado como
	 * argumento da função
	 * 
	 * @param i um dos número inteiro da dupla
	 * @return se o inteiro passado foi o primeiro número, retorna-se o segundo e
	 *         vice-versa
	 */
	public int other(int i) {
		return i == i1 ? i2 : i1;
	}

	public boolean swap(int oldNumber, int newNumber) {
		if (oldNumber == i1) {
			i1 = newNumber;
			return true;
		} else if (oldNumber == i2) {
			i2 = newNumber;
			return true;
		} else
			return false;
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof DuplaV) {
			DuplaV dupla = (DuplaV) anObject;
			return dupla.get1() == i1 && dupla.get2() == i2;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(i1 + i2).hashCode();
	}

	@Override
	public String toString() {
		return "(" + i1 + ";" + i2 + ")";
	}
}
