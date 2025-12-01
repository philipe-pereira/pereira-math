package br.com.pereiraeng.math.set;

import java.util.Calendar;

/**
 * Classe dos objetos que representam um conjunto de tempo contínuo delimitado
 * por dois extremos
 * 
 * @author Philipe PEREIRA
 *
 */
public class TimeInterval extends Interval<Calendar> {

	/**
	 * Construtor do objeto do intervalo de tempo. Ambas extremidades estão contidas
	 * no conjunto.
	 * 
	 * @param lower extremo inferior
	 * @param upper extremo superior
	 */
	public TimeInterval(Calendar lower, Calendar upper) {
		this(lower, true, upper, true);
	}

	/**
	 * Construtor do objeto do intervalo de tempo
	 * 
	 * @param lower   extremo inferior
	 * @param lowerEp lower end point
	 * @param upper   extremo superior
	 * @param upperEp upper end point
	 */
	public TimeInterval(Calendar lower, boolean lowerEp, Calendar upper, boolean upperEp) {
		super(new Calendar[] { lower, upper }, lowerEp, upperEp);
	}

	@Override
	public String toString() {
		return toString(this);
	}

	public static String toString(Interval<Calendar> interval) {
		return toString(interval.getLower(), interval.isLowerOpen(), interval.getUpper(), interval.isUpperOpen());
	}

	protected static String toString(Calendar lower, boolean lowerEp, Calendar upper, boolean upperEp) {
		return String.format("%c%tDT%<tT;%tDT%<tT%c", lowerEp ? '[' : ']', lower, upper, upperEp ? ']' : '[');
	}
}
