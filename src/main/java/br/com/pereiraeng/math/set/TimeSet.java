package br.com.pereiraeng.math.set;

import java.util.Calendar;

/**
 * Classe dos objetos que representam conjuntos contínuos disjuntos de tempo
 * 
 * @author Philipe PEREIRA
 *
 */
public class TimeSet extends ContinuousSet<Calendar> {
	private static final long serialVersionUID = 729807306475991004L;

	/**
	 * 
	 * @param css períodos de tempo, definidos por uma lista de vetores de duas
	 *            posições, onde os elementos da primeira posição indicam o começo
	 *            de um subintervalo e os da segunda indicam seu final
	 */
	public TimeSet(Calendar[]... css) {
		this(css, false);
	}

	/**
	 * 
	 * @param css  períodos de tempo, definidos por uma matriz de duas colunas, onde
	 *             os elementos da primeira coluna indicam o começo de um
	 *             subintervalo e os da segunda indicam seu final
	 * @param disj <code>true</code> se os vetores linhas formarem conjuntos
	 *             disjuntos (o que permite a união direta), <code>false</code>
	 *             senão
	 */
	public TimeSet(Calendar[][] css, boolean disj) {
		if (disj) {
			for (Calendar[] cs : css)
				super.addDisj(new TimeInterval(cs[0], true, cs[1], true));
		} else
			for (Calendar[] cs : css)
				this.add(cs);
	}

	/**
	 * 
	 * @param cs
	 * @return
	 */
	public boolean add(Calendar[] cs) {
		return super.add(new TimeInterval(cs[0], true, cs[1], true));
	}

	public Calendar[] getLimits() {
		return new Calendar[] { first().getLower(), last().getUpper() };
	}

	@Override
	public String toString() {
		if (this.size() == 0)
			return "";
		StringBuilder out = new StringBuilder();
		for (Interval<Calendar> i : this) {
			out.append(TimeInterval.toString(i));
			out.append(" U ");
		}
		return out.substring(0, out.length() - 3);
	}

	/**
	 * Função que adiciona um intervalo temporal a um dado conjunto de tempo
	 * 
	 * @param ts    conjunto de tempo
	 * @param lower limite inferior do intervalo a ser adicionado
	 * @param upper limite superior do intervalo a ser adicionado
	 * @param mili  limiar a partir do qual dois intervalos são considerados
	 *              contíguos
	 */
	public static void merge(TimeSet ts, Calendar lower, Calendar upper, long mili) {
		boolean merge = false;
		for (Interval<Calendar> interval : ts) {
			if (Math.abs(lower.getTimeInMillis() - interval.getUpper().getTimeInMillis()) <= mili) {
				interval.setUpper(upper);
				merge = true;
				break;
			} else if (Math.abs(interval.getLower().getTimeInMillis() - upper.getTimeInMillis()) <= mili) {
				interval.setLower(lower);
				merge = true;
				break;
			}
		}
		if (!merge)
			ts.add(new Calendar[] { lower, upper });
	}

	/**
	 * Função que adiciona um intervalo temporal a um dado conjunto de tempo
	 * 
	 * @param ts   conjunto de tempo
	 * @param ti   intervalo a ser adicionado
	 * @param mili limiar a partir do qual dois intervalos são considerados
	 *             contíguos
	 */
	public static void merge(TimeSet ts, TimeInterval ti, long mili) {
		merge(ts, ti.getLower(), ti.getUpper(), mili);
	}
}
