package br.com.pereiraeng.math.set;

public class RealInterval extends Interval<Double> {

	/**
	 * Construtor do intervalo <strong>fechado</strong>
	 * 
	 * @param a limite inferior
	 * @param b limite superior
	 */
	public RealInterval(double a, double b) {
		this(a, true, b, true);
	}

	/**
	 * Construtor do intervalo
	 * 
	 * @param a   limite inferior
	 * @param aep <code>true</code> para o limite inferior pertencer ao conjunto
	 * @param b   limite superior
	 * @param bep <code>true</code> para o limite superior pertencer ao conjunto
	 */
	public RealInterval(double a, boolean aep, double b, boolean bep) {
		super(new Double[] { a, b }, aep, bep);
	}

	@Override
	public String toString() {
		return String.format("%c%g;%g%c", isLowerOpen() ? '[' : ']', getLower(), getUpper(), isUpperOpen() ? ']' : '[');
	}
}
