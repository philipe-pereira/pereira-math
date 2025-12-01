package br.com.pereiraeng.math;

/**
 * Classe dos objeto que representam um <strong>vetor</strong> com duas
 * posições, sendo que a primeira posição é ocupada por um
 * <strong>conjunto</strong> com dois números inteiros e a segunda com um
 * inteiro, ou seja, um vetor na forma ({x;y};z), armazenando portanto três
 * números, porém que se discernem de outros vetores de maneira particular.
 * 
 * @author Philipe PEREIRA
 *
 */
public class DuplaEmeio extends Dupla {

	protected int i3;

	/**
	 * booleano que indica se há uma relação de ordem entre os dois elementos do
	 * conjunto x e y
	 * 
	 * <p>
	 * Do ponto de vista computacional, não se altera as funções
	 * {@link #equals(Object)} e {@link #hashCode()}. No entanto, nas interpretações
	 * matemáticas aplicáveis deste objeto, pode-se considerar que ele deixa de
	 * representar um vetor de duas posições:
	 * </p>
	 * 
	 * <p>
	 * ({x;y};z)
	 * </p>
	 * 
	 * <p>
	 * e passa a representar um vetor de três posições:
	 * </p>
	 * 
	 * <p>
	 * (x;y;z)
	 * </p>
	 * 
	 * <p>
	 * Para aplicações em grafos, isso pode ser utilizado para informar que o
	 * vértice representado por este objeto é orientado.
	 * </p>
	 */
	protected boolean ordered = false;

	public DuplaEmeio(int i1, int i2, int i3) {
		super(i1, i2);
		this.set3(i3);
	}

	public DuplaEmeio(Dupla d, int i3) {
		this(d.get1(), d.get2(), i3);
	}

	public DuplaEmeio(int[] is) {
		this(is[0], is[1], is[2]);
	}

	public int get3() {
		return i3;
	}

	public void set3(int i3) {
		this.i3 = i3;
	}

	public int[] getArray() {
		return new int[] { get1(), get2(), i3 };
	}

	public Dupla getDupla() {
		return new Dupla(get1(), get2());
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof DuplaEmeio) {
			DuplaEmeio duplaEmeio = (DuplaEmeio) anObject;
			return super.equals(duplaEmeio) && this.get3() == duplaEmeio.get3();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(super.hashCode() + i3).hashCode();
	}

	@Override
	public String toString() {
		return "(" + super.toString() + ";" + i3 + ")";
	}

	public String toString2() {
		return String.format("%d\t%d\t%d\n", get1(), get2(), get3());
	}

	/**
	 * Função que estabelece se há uma relação de ordem entre os dois elementos do
	 * conjunto x e y
	 * 
	 * @param ordered <code>true</code> se houver relação de ordem,
	 *                <code>false</code> se não
	 */
	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}

	/**
	 * Função que retorna se há uma relação de ordem entre os dois elementos do
	 * conjunto x e y
	 * 
	 * @return <code>true</code> se houver relação de ordem, <code>false</code> se
	 *         não
	 */
	public boolean isOrdered() {
		return ordered;
	}

}
