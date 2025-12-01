package br.com.pereiraeng.math.set;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import br.com.pereiraeng.core.collections.comparison.ArrayComparator;


/**
 * Classe dos objetos que representam um conjunto discreto de elementos
 * 
 * @author Philipe PEREIRA
 */
public class DiscretSet extends TreeSet<int[]> {
	private static final long serialVersionUID = 1L;

	public DiscretSet() {
		super(new ArrayComparator.Integer());
	}

	public DiscretSet(Collection<Integer> is) {
		this();
		for (Integer i : is)
			add(i);
	}

	public boolean isIn(int r) {
		for (int[] i : this) // TODO não dá para achar mais rápido? ver como ir para o meio da lista
								// (busca binária)
			if (r >= i[0] && r <= i[1])
				return true;
		return false;
	}

	public boolean add(int ni) {
		boolean isDisj = true, check = false;
		Iterator<int[]> it = this.iterator();

		while (it.hasNext()) {
			int[] i = it.next();

			if (i[0] - ni > 1)
				// se o inserido for menor que o limite inferior do existente,
				// adiciona
				break;
			else if (ni - i[1] < 2) {
				// se limite superior do existente for maior que o inserido,
				// estes intervalos não são disjuntos
				isDisj = false;

				// neste caso, estica-se nenhuma, uma ou as duas extremidades do
				// existente
				if (i[0] > ni) {
					i[0] = ni;
					check = true;
				}

				if (ni > i[1]) {
					i[1] = ni;
					check = true;
				}
				break;
			}
		}

		if (isDisj)
			// só cria um novo intervalo para o conjunto se este for disjunto
			// (i.e., de interseção nula) a todos os outros já existentes
			return super.add(new int[] { ni, ni });
		else {
			if (check)
				check();
			return check;
		}
	}

	public int pop() {
		int[] first = this.first();
		int out = first[0];
		if (first[0] == first[1])
			this.remove(first);
		else
			first[0]++;
		return out;
	}

	public int pop(int threshold) {
		Iterator<int[]> it = this.iterator();
		int out = -1;
		while (it.hasNext()) {
			int[] i = it.next();
			if (i[0] > threshold) {
				out = i[0];
				if (i[0] == i[1])
					it.remove();
				else
					i[0]++;
				break;
			}
		}
		return out;
	}

	public boolean add(int lb, int ub) {
		return add(new int[] { lb, ub });
	}

	@Override
	public boolean add(int[] newI) {
		boolean isDisj = true, check = false;
		Iterator<int[]> it = this.iterator();

		while (it.hasNext()) {
			int[] i = it.next();

			if (i[0] - newI[1] > 1)
				// se o inserido for menor que o limite inferior do existente,
				// adiciona
				break;
			else if (newI[0] - i[1] < 2) {
				// se limite superior do existente for maior que o inserido,
				// estes intervalos não são disjuntos
				isDisj = false;

				// neste caso, estica-se nenhuma, uma ou as duas extremidades do
				// existente
				if (i[0] > newI[0]) {
					i[0] = newI[0];
					check = true;
				}

				if (newI[1] > i[1]) {
					i[1] = newI[1];
					check = true;
				}
				break;
			}
		}

		if (isDisj)
			// só cria um novo intervalo para o conjunto se este for disjunto
			// (i.e., de interseção nula) a todos os outros já existentes
			return super.add(newI);
		else {
			if (check)
				check();
			return check;
		}
	}

	@Override
	public boolean addAll(Collection<? extends int[]> c) {
		boolean out = false;
		for (int[] i : c)
			out |= add(i);
		return out;
	}

	private void check() {
		// se houve somente a expansão de um dos conjuntos existentes
		// remove intersecção formadas entre os existentes
		Iterator<int[]> it = this.iterator();
		int[] i0 = it.next();
		while (it.hasNext()) {
			int[] i = it.next();

			if (i[0] - i0[1] < 2) {
				// se houve invasão do de trás, ou há fusão ou total
				// remoção do da frente. Em ambos os casos, o da frente
				// desaparece

				if (i[1] - i0[1] > 0)
					i0[1] = i[1];

				it.remove();
			} else
				i0 = i;
		}
	}

	@Override
	public String toString() {
		if (this.size() == 0)
			return "";
		StringBuilder out = new StringBuilder();
		for (int[] i : this) {
			if (i[0] == i[1]) // conjunto unitário
				out.append(String.format("{%d} U ", i[0]));
			else if (i[1] - i[0] == 1) // conjunto binário
				out.append(String.format("{%d, %d} U ", i[0], i[1]));
			else // conjunto com mais de dois elementos
				out.append(String.format("{x in Z | %d <= x <= %d} U ", i[0], i[1]));
		}
		return out.substring(0, out.length() - 3);
	}
}
