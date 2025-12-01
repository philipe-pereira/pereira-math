package br.com.pereiraeng.math;

/**
 * Classe do objeto que representa um <strong>conjunto</strong> com dois objetos. 
 * Uma vez que se trata de um conjunto, a ordem não é relevante para fins de
 * discernimento do elemento (em outras palavras, {x; y} é o mesmo objeto que
 * {y; x})
 * 
 * @author Philipe Pereira
 *
 */
public class DuplaO<O> {
	private O i1;
	private O i2;

	/**
	 * Construtor do conjunto de dois objetos
	 * 
	 * @param i1 um objeto
	 * @param i2 outro objeto
	 */
	public DuplaO(O i1, O i2) {
		this.i1 = i1;
		this.i2 = i2;
	}

	public O get1() {
		return i1;
	}

	public void set1(O i1) {
		this.i1 = i1;
	}

	public O get2() {
		return i2;
	}

	public void set2(O i2) {
		this.i2 = i2;
	}

	public boolean contains(O i) {
		return i1 == i || i2 == i;
	}

	/**
	 * Função que retorna o outro número da dupla que não é aquele passado como
	 * argumento da função
	 * 
	 * @param i um dos número inteiro da dupla
	 * @return se o inteiro passado foi o primeiro número, retorna-se o segundo e
	 *         vice-versa
	 */
	public O other(O i) {
		return i == i1 ? i2 : i1;
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof DuplaO) {
			DuplaO<?> dupla = (DuplaO<?>) anObject;
			return Vec.equalBinary(dupla.get1(), dupla.get2(), i1, i2);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return i1.hashCode() + i2.hashCode();
	}

	@Override
	public String toString() {
		return "{" + i1 + ";" + i2 + "}";
	}
}