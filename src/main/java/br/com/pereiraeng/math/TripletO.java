package br.com.pereiraeng.math;

/**
 * Função que representa um <strong>vetor</strong> com três objetos. Uma vez que
 * se trata de um vetor, a ordem é relevante para fins de discernimento do
 * elemento (em outras palavras, (x; y; z) <strong>não</strong> é o mesmo objeto
 * que (y; x; z))
 * 
 * @author Philipe PEREIRA
 *
 */
public class TripletO {
	
	private Object i1;
	
	private Object i2;
	
	private Object i3;

	/**
	 * Construtor do conjunto de dois objetos
	 * 
	 * @param i1 um objeto
	 * @param i2 outro objeto
	 * @param i3 outro objeto
	 */
	public TripletO(Object i1, Object i2, Object i3) {
		this.i1 = i1;
		this.i2 = i2;
		this.i3 = i3;
	}

	public Object get1() {
		return i1;
	}

	public void set1(Object i1) {
		this.i1 = i1;
	}

	public Object get2() {
		return i2;
	}

	public void set2(Object i2) {
		this.i2 = i2;
	}

	public Object get3() {
		return i3;
	}

	public void set3(Object i3) {
		this.i3 = i3;
	}

	public boolean contains(Object i) {
		return i1.equals(i) || i2.equals(i) || i3.equals(i);
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof TripletO) {
			TripletO t = (TripletO) anObject;
			return this.get1().equals(t.get1()) && this.get2().equals(t.get2()) && this.get3().equals(t.get3());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return i1.hashCode() + i2.hashCode() + i3.hashCode();
	}

	@Override
	public String toString() {
		return "{" + i1 + ";" + i2 + ";" + i3 + "}";
	}
}
