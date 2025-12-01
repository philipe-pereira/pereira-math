package br.com.pereiraeng.math.set;

import java.util.Calendar;

/**
 * Classe dos objetos que representam um conjunto contínuo delimitado por dois
 * elementos, ou seja, um conjunto na forma:
 * 
 * X = {x in D | a < x < b}
 * 
 * @author Philipe PEREIRA
 *
 * @param <D> classe do elemento desse conjunto
 */
public class Interval<D extends Comparable<D>> implements Comparable<Interval<D>> {

	private D[] interval;

	private boolean[] endpoints;

	/**
	 * Constructor of the interval
	 * 
	 * @param interval two position array, indicating the lower and upper boundaries
	 * @param lep      lower end point
	 * @param uep      upper end point
	 */
	public Interval(D[] interval, boolean lep, boolean uep) {
		if (interval.length != 2)
			throw new IllegalArgumentException("Um intervalo é definido por dois pontos.");
		if (interval[0].compareTo(interval[1]) > 0)
			throw new IllegalArgumentException("O limite inferior do intervalo deve ser menor que o superior.");

		this.interval = interval;
		this.endpoints = new boolean[] { lep, uep };
	}

	/**
	 * Function that indicates if an element is inside this interval
	 * 
	 * @param r element
	 * @return <code>true</code> if the element is inside, <code>false</code>
	 *         otherwise
	 */
	public boolean isIn(D r) {
		int l = r.compareTo(interval[0]);
		int h = interval[1].compareTo(r);
		return (endpoints[0] ? l >= 0 : l > 0) && (endpoints[1] ? h >= 0 : h > 0);
	}

	/**
	 * Function that indicates if an interval is included (or contained) in this
	 * interval
	 * 
	 * @param inter interval
	 * @return <code>true</code> if the interval is included, <code>false</code>
	 *         otherwise
	 */
	public boolean includes(Interval<D> inter) {
		int l = inter.getLower().compareTo(interval[0]);
		int h = interval[1].compareTo(inter.getUpper());
		boolean[] eps = inter.getEndpoints();
		return (endpoints[0] || !eps[0] ? l >= 0 : l > 0) && (endpoints[1] || !eps[1] ? h >= 0 : h > 0);
	}

	/**
	 * Function that indicates if an interval and this one are disjoints (i.e., they
	 * have no element in common)
	 * 
	 * @param inter interval
	 * @return <code>true</code> if they are disjoints, <code>false</code> otherwise
	 */
	public boolean isDisjoint(Interval<D> inter) {
		boolean[] eps = inter.getEndpoints();

		int c = inter.getUpper().compareTo(interval[0]);
		if (endpoints[0] && eps[1] ? c < 0 : c <= 0)
			return true; // intervalo está a esquerda

		c = inter.getLower().compareTo(interval[1]);
		if (endpoints[1] && eps[0] ? c > 0 : c >= 0)
			return true; // intervalo está a direita

		return false;
	}

	public D[] getInterval() {
		return interval;
	}

	/**
	 * Function that returns que lower boundary
	 * 
	 * @return lower boundary
	 */
	public D getLower() {
		return interval[0];
	}

	/**
	 * Function that returns que upper boundary
	 * 
	 * @return upper boundary
	 */
	public D getUpper() {
		return interval[1];
	}

	public void setLower(D r) {
		this.interval[0] = r;
	}

	public void setUpper(D r) {
		this.interval[1] = r;
	}

	public boolean[] getEndpoints() {
		return endpoints;
	}

	public boolean isLowerOpen() {
		return endpoints[0];
	}

	public boolean isUpperOpen() {
		return endpoints[1];
	}

	public void setLowerOpen(boolean ep) {
		this.endpoints[0] = ep;
	}

	public void setUpperOpen(boolean ep) {
		this.endpoints[1] = ep;
	}

	@Override
	public int compareTo(Interval<D> o) {
		return this.getLower().compareTo(o.getLower());
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof Interval) {
			Interval<?> t = (Interval<?>) anObject;
			return t.getLower().equals(this.getLower()) && t.getUpper().equals(this.getUpper())
					&& !(t.isLowerOpen() ^ this.isLowerOpen()) && !(t.isUpperOpen() ^ this.isUpperOpen());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getLower().hashCode() + this.getUpper().hashCode();
	}

	@Override
	public String toString() {
		Object lower = getLower();
		Object upper = getUpper();
		boolean[] eps = getEndpoints();
		if (lower instanceof Calendar) // só para calendário que muda...
			return TimeInterval.toString((Calendar) lower, eps[0], (Calendar) upper, eps[1]);
		else
			return String.format("%c%s;%s%c", eps[0] ? '[' : ']', lower, upper, eps[1] ? ']' : '[');
	}

	public double getLength() {
		Object lower = getLower();
		Object upper = getUpper();
		if (lower instanceof Calendar) // só para calendário que muda...
			return ((Calendar) upper).getTimeInMillis() - ((Calendar) lower).getTimeInMillis();
		else if (lower instanceof Number)
			return ((Number) upper).doubleValue() - ((Number) lower).doubleValue();
		else
			return Double.NaN;
	}
}
