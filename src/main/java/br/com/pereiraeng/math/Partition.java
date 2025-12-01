package br.com.pereiraeng.math;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Objecto que representa um conjunto de sub-conjuntos disjuntos (i.e., de
 * intersecção nula)
 * 
 * @author Philipe PEREIRA
 *
 * @param <K> Classe do objeto dos conjuntos
 */
public class Partition<K> extends LinkedHashSet<Set<K>> {
	private static final long serialVersionUID = 1L;

	public void addSet(K[] ks) {
		for (Set<K> s : this)
			for (K k : ks)
				if (s.contains(k))
					return;

		// se nenhum dos novos elementos está aqui, eles podem ser adicionados
		HashSet<K> newSet = new HashSet<>();
		for (K k : ks)
			newSet.add(k);
		super.add(newSet);
	}

	public Set<K> getElements() {
		Set<K> out = new LinkedHashSet<>();
		for (Set<K> s : this)
			for (K k : s)
				out.add(k);
		return out;
	}

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof Partition) {
			Partition<?> part = (Partition<?>) anObject;
			Iterator<?> it1 = part.iterator();

			boolean out = true;
			while (it1.hasNext()) {
				Set<?> set1 = (Set<?>) it1.next();

				Iterator<Set<K>> it2 = this.iterator();
				boolean s = false;
				while (it2.hasNext()) {
					Set<K> set2 = (Set<K>) it2.next();
					s |= set2.containsAll(set1) && set1.containsAll(set2);
				}
				out &= s;
			}
			return out;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int out = 0;
		for (Set<K> s : this)
			for (K k : s)
				out += k.hashCode();
		return out;
	}

	@Override
	public String toString() {
		String out = "";
		for (Set<K> s : this)
			out += s.toString() + ";";
		return out.substring(0, out.length() - 1);
	}
}
