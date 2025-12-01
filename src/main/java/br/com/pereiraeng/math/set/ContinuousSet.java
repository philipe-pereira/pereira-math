package br.com.pereiraeng.math.set;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Classe dos objetos que representam um conjunto contínuo de elementos, sendo
 * formado, portanto, por um {@link Interval intervalo} ou pela união de mais de
 * um deles
 * 
 * @author Philipe PEREIRA
 *
 * @param <R> classe dos elementos contido no conjunto
 */
public class ContinuousSet<R extends Comparable<R>> extends TreeSet<Interval<R>> {
	private static final long serialVersionUID = 1L;

	/**
	 * Function that indicates if an element is inside this set
	 * 
	 * @param r element
	 * @return <code>true</code> if the element is inside, <code>false</code>
	 *         otherwise
	 */
	public boolean isIn(R r) {
		for (Interval<R> i : this)
			if (i.isIn(r))
				return true;
		return false;
	}

	/**
	 * Function that indicates if a set is included (or contained) in this set
	 * 
	 * @param cs set
	 * @return <code>true</code> if the set is included, <code>false</code>
	 *         otherwise
	 */
	public boolean includes(ContinuousSet<R> cs) {
		for (Interval<R> ss : cs) { // ver se cada subconjunto dado está...
			boolean included = false;
			for (Interval<R> i : this) { // ... incluído em algum subconjunto
				if (i.includes(ss)) {
					included = true;
					break;
				}
			}
			if (!included) // se este subconjunto não está incluído em nenhum...
				return false;
		}
		return true;
	}

	/**
	 * Function that indicates if a set and this one are disjoints (i.e., they have
	 * no element in common)
	 * 
	 * @param inter set
	 * @return <code>true</code> if they are disjoints, <code>false</code> otherwise
	 */
	public boolean isDisjoint(ContinuousSet<R> cs) {
		for (Interval<R> ss : cs) // ver se cada subconjunto dado está...
			for (Interval<R> i : this) // ... tem algum elemento em comum
				if (!i.isDisjoint(ss))
					return false;
		return true;
	}

	@Override
	public boolean add(Interval<R> newI) {
		boolean isDisj = true, check = false;
		Iterator<Interval<R>> it = this.iterator();

		while (it.hasNext()) {
			Interval<R> i = it.next();

			if (i.getLower().compareTo(newI.getUpper()) > 0)
				// se o limite superior do inserido for menor que o limite
				// inferior do existente, adiciona
				break;
			else if (i.getUpper().compareTo(newI.getLower()) > 0) {
				// se limite superior do existente for maior que o limite
				// inferior do inserido, estes intervalos não são disjuntos
				isDisj = false;

				// neste caso, estica-se nenhuma, uma ou as duas extremidades do
				// existente
				if (i.getLower().compareTo(newI.getLower()) > 0) {
					i.setLower(newI.getLower());
					check = true;
				}

				if (newI.getUpper().compareTo(i.getUpper()) > 0) {
					i.setUpper(newI.getUpper());
					check = true;
				}

				break;
			}
		}

		if (isDisj)
			// só cria um novo intervalo para o conjunto se este for disjunto
			// (i.e., de interseção nula) a todos os outros já existentes
			return this.addDisj(newI);
		else {
			if (check) {
				// se houve somente a expansão de um dos conjuntos existentes
				// remove intersecção formadas entre os existentes
				it = this.iterator();
				Interval<R> i0 = it.next();
				while (it.hasNext()) {
					Interval<R> i = it.next();

					if (i0.getUpper().compareTo(i.getLower()) > 0) {
						// se houve invasão do de trás, ou há fusão ou total
						// remoção do da frente. Em ambos os casos, o da frente
						// desaparece

						if (i.getUpper().compareTo(i0.getUpper()) > 0)
							i0.setUpper(i.getUpper());

						it.remove();
					} else
						i0 = i;
				}
			}
			return check;
		}
	}

	@Override
	public boolean addAll(Collection<? extends Interval<R>> c) {
		boolean out = false;
		for (Interval<R> i : c)
			out |= this.add(i);
		return out;
	}

	/**
	 * Função que adiciona um intervalo contínuo a este conjunto. <strong>Esta
	 * função só deve ser chamada caso o intervalo inserido é disjuntos a todos os
	 * demais intervalos (i.e., sua intersecção é nula)</strong>.
	 * 
	 * @param interval intervalo a ser inserido
	 * @return <code>true</code> if this set did not already contain the specified
	 *         element
	 */
	public boolean addDisj(Interval<R> interval) {
		return super.add(interval);
	}

	@Override
	public String toString() {
		if (this.size() == 0)
			return "";
		StringBuilder out = new StringBuilder();
		for (Interval<R> i : this) {
			out.append(i.toString());
			out.append(" U ");
		}
		return out.substring(0, out.length() - 3);
	}

	public double getLength() {
		double out = 0.;
		for (Interval<R> s : this)
			out += s.getLength();
		return out;
	}

	// ---------------------------- AUXILIARES ----------------------------

	/**
	 * Função que carrega no vetor de duas posições os objetos limítrofes de uma
	 * relação de períodos
	 * 
	 * @param out vetor de duas posições a ser carregado com os objetos mínimos e
	 *            máximos
	 * @param ps  relação de períodos
	 */
	public static <K extends Comparable<K>> void union(K[] out, Collection<K[]> ps) {
		if (ps != null)
			for (K[] p : ps)
				union(out, p);
	}

	/**
	 * Função que atualiza os limites de um dado período
	 * 
	 * @param out período a ser atualizado
	 * @param p   período indicando os possíveis novos limites
	 */
	public static <K extends Comparable<K>> void union(K[] out, K[] p) {
		if (p == null ? true : p.length < 2)
			return;
		if (p[0] == null ? false : (out[0] == null ? true : p[0].compareTo(out[0]) < 0))
			out[0] = p[0];
		if (p[1] == null ? false : (out[1] == null ? true : p[1].compareTo(out[1]) > 0))
			out[1] = p[1];
	}
}
