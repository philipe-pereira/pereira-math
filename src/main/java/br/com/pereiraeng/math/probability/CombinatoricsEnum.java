package br.com.pereiraeng.math.probability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import br.com.pereiraeng.core.collections.ListUtils;

/**
 * Objeto que enumera todas as sub-coleções de uma dada coleção de elementos
 * 
 * @author Philipe PEREIRA
 *
 * @param <E> classe do elementos
 */
public class CombinatoricsEnum<E> implements Enumeration<Collection<E>> {

	private final Collection<E> elements;
	private final int selection;
	private final boolean order;
	private final boolean repetition;
	private final boolean derangement;

	private final int total;

	/**
	 * Construtor do objeto que enumera todas as sub-coleções de uma dada coleção de
	 * elementos
	 * 
	 * @param elements   coleção de elementos
	 * @param selection  número de elementos da sub-coleção
	 * @param order      <code>true</code> se a ordem dos elementos é levada em
	 *                   conta para distinguir duas sub-coleções; <code>false</code>
	 *                   senão
	 * @param repetition <code>true</code> se as sub-coleções podem abrigar mais de
	 *                   uma vez o mesmo elemento; <code>false</code> senão
	 */
	public CombinatoricsEnum(Collection<E> elements, int selection, boolean order, boolean repetition) {
		this.elements = elements;
		this.selection = selection;
		this.order = order;
		this.repetition = repetition;
		this.derangement = false;
		if (order)
			total = (int) ProbEstat.arranjo(elements.size(), selection, repetition);
		else
			total = (int) ProbEstat.combinacao(elements.size(), selection, repetition);
	}

	/**
	 * Construtor do objeto que enumera todas as sub-coleções de mesmo tamanho de
	 * uma dada coleção de elementos
	 * 
	 * @param elements    coleção de elementos
	 * @param permutation <code>true</code> para as permutações; <code>false</code>
	 *                    para os desarranjos
	 */
	public CombinatoricsEnum(Collection<E> elements, boolean permutation) {
		this.elements = elements;
		this.selection = this.elements.size();
		this.order = true;
		this.repetition = false;
		this.derangement = !permutation;
		if (permutation)
			this.total = (int) ProbEstat.arranjo(elements.size(), selection, repetition);
		else {// desarranjos
			this.total = (int) ProbEstat.desarranjo(elements.size());
			this.totalD = (int) ProbEstat.arranjo(elements.size(), selection, repetition);
		}
	}

	public int getTotal() {
		return total;
	}

	// --------------------------------------------------------------------

	private transient int index = 0;

	private transient int totalD = 0, indexD = 0;

	@Override
	public boolean hasMoreElements() {
		return index < total;
	}

	@Override
	public Collection<E> nextElement() {
		Collection<E> out;
		if (order) { // arranjo
			if (repetition) { // arranjo com repetição - n^p
				out = new ArrayList<>(selection);

				int block = total, i = this.index;
				for (int j = 0; j < this.selection; j++) {
					block /= this.elements.size();
					int k = i / block;

					out.add(ListUtils.getElementAt(this.elements, k));

					// próximo índice
					i %= block;
				}
			} else { // arranjo sem repetição - n!/(n-p)!
				out = new LinkedHashSet<>(selection);

				int block = derangement ? this.totalD : this.total, i = derangement ? this.indexD : this.index;
				for (int j = 0; j < this.selection; j++) {
					block /= (this.elements.size() - out.size());
					int k = i / block;

					// getElementAt, mas pulando aqueles que já estão em out
					for (E e : this.elements) {
						if (!out.contains(e)) {
							if (k-- == 0) {
								out.add(e);
								break;
							}
						}
					}

					// próximo índice
					i %= block;
				}

				if (derangement) {
					this.indexD++;
					if (!isDerangement(this.elements, out))
						return nextElement();
				}
			}
		} else {
			if (repetition) { // Combinação com repetição - (n+p-1)!/p!/(n-1)!
				out = new ArrayList<>(selection);
				// TODO
				System.err.println("Incompleto!4587");
			} else { // Combinação simples - n!/p!/(n-p)!
				out = new HashSet<>(selection);

				int c = total, i = this.index;
				for (int j = 0; j < this.selection; j++) {
					int l = 0, block = 0;
					while (c > i) {
						block = (int) ProbEstat.combinacao(this.selection - 1 + l - j, l, false);
						c -= block;
						l++;
					}
					l = this.elements.size() - this.selection + 1 - l;

					// getElementAt, mas pulando aqueles que já estão em out
					for (E e : this.elements) {
						if (!out.contains(e)) {
							if (l-- == 0) {
								out.add(e);
								break;
							}
						}
					}

					// próximo índice
					i -= c;
					c = block;
				}
			}
		}

		index++;
		return out;
	}

	private boolean isDerangement(Collection<E> elements, Collection<E> derangement) {
		Iterator<E> dit = derangement.iterator();
		Iterator<E> eit = elements.iterator();
		while (dit.hasNext() && eit.hasNext()) {
			E d = dit.next();
			E e = eit.next();
			if (d.equals(e))
				return false;
		}
		return true;
	}

	// ------------------------- MÉTODOS ESTÁTICOS -------------------------

	/**
	 * Função que retorna todas as combinações possíveis de tamanho <code>p</code> a
	 * partir de um conjunto de <code>n</code> elementos
	 * 
	 * @param <E>  the type of elements held in this list
	 * @param p    número de elementos
	 * @param list lista de <code>n</code> elementos a partir dos quais serão feitas
	 *             as combinações
	 * @return <code>n!/p!/(n-p)!</code> conjuntos de elementos
	 */
	public static <E> Set<Set<E>> getPerms(int p, final List<E> list) {
		if (p < 0)
			throw new IllegalArgumentException("Não existe conjunto de tamanho negativo.");
		if (p > list.size())
			throw new IllegalArgumentException(
					String.format("Não dá para fazer conjuntos de cardinalidade %d com %d elementos.", p, list.size()));
		if (p == 1) {
			Set<Set<E>> out = new HashSet<>();
			for (E e : list)
				out.add(new LinkedHashSet<E>(Arrays.asList(e)));
			return out;
		} else {
			Set<Set<E>> om1 = getPerms(p - 1, list);
			Set<Set<E>> out = new HashSet<>();
			for (Set<E> s : om1) {
				List<E> ss = list.subList(list.indexOf(ListUtils.getLast(s)) + 1, list.size());
				for (E e : ss) {
					Set<E> nset = new LinkedHashSet<>(s);
					nset.add(e);
					out.add(nset);
				}
			}
			return out;
		}
	}

	/**
	 * Função que retorna todas as combinações possíveis de tamanho <code>p</code> a
	 * partir de um conjunto de <code>n</code> elementos
	 * 
	 * @param <E> the type of elements held in this set
	 * @param p   número de elementos
	 * @param set conjunto de <code>n</code> elementos a partir dos quais serão
	 *            feitas as combinações
	 * @return <code>n!/p!/(n-p)!</code> conjuntos de elementos
	 */
	public static <E> Set<Set<E>> getPerms(int p, final Set<E> set) {
		return CombinatoricsEnum.getPerms(p, new ArrayList<>(set));
	}

	/**
	 * Função que retorna todas as combinações possíveis de tamanho <code>p</code> a
	 * partir de um conjunto de <code>n</code> elementos <strong>ordenáveis</strong>
	 * 
	 * @param <E> the type of elements held in this set
	 * @param p   número de elementos
	 * @param set conjunto de <code>n</code> elementos a partir dos quais serão
	 *            feitas as combinações
	 * @return <code>n!/p!/(n-p)!</code> conjuntos de elementos
	 */
	public static <E> Set<TreeSet<E>> getPermsOrd(int p, final TreeSet<E> set) {
		if (p < 0)
			throw new IllegalArgumentException("Não existe conjunto de tamanho negativo.");
		if (p > set.size())
			throw new IllegalArgumentException(
					String.format("Não dá para fazer conjuntos de cardinalidade %d com %d elementos.", p, set.size()));
		if (p == 1) {
			Set<TreeSet<E>> out = new HashSet<>();
			for (E e : set)
				out.add(new TreeSet<E>(Arrays.asList(e)));
			return out;
		} else {
			Set<TreeSet<E>> om1 = getPermsOrd(p - 1, set);
			Set<TreeSet<E>> out = new HashSet<>();
			for (TreeSet<E> s : om1) {
				SortedSet<E> ss = set.tailSet(s.last(), false);
				for (E e : ss) {
					TreeSet<E> nset = new TreeSet<>(s);
					nset.add(e);
					out.add(nset);
				}
			}
			return out;
		}
	}

	/**
	 * Função que tentamos utilizar para calcular o desarranjo...
	 */
	public static void get() {
		int size = 7;
		CombinatoricsEnum<Integer> it = new CombinatoricsEnum<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6), size, true, false);
		List<int[]> ss = new ArrayList<>(it.getTotal());
		main: while (it.hasMoreElements()) {
			Collection<Integer> s = it.nextElement();
			int[] cs = ListUtils.toIntArray(s);
			for (int i = 0; i < cs.length; i++)
				if (cs[i] == i)
					continue main;
			ss.add(ListUtils.toIntArray(s));
		}

		Map<Integer, Map<Integer, Integer>> count2 = new TreeMap<>();
		Map<Integer, Map<Integer, Map<Integer, Integer>>> count3 = new TreeMap<>();
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Integer>>>> count4 = new TreeMap<>();
		Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Integer>>>>> count5 = new TreeMap<>();

		for (int[] s : ss) {

			// print
			for (Integer k : s)
				System.out.print(k + "\t");
			System.out.println();

			// ------------------------------------------

			Map<Integer, Integer> w1 = count2.get(s[0]);
			if (w1 == null) {
				count2.put(s[0], w1 = new TreeMap<>());
				for (int i = 0; i < size; i++)
					if (s[0] != i)
						w1.put(i, 0);
			}

			Integer c = w1.get(s[1]);
			w1.put(s[1], c + 1);

			// ------------------------------------------

			Map<Integer, Map<Integer, Integer>> p1 = count3.get(s[0]);
			if (p1 == null)
				count3.put(s[0], p1 = new TreeMap<>());

			Map<Integer, Integer> p2 = p1.get(s[1]);
			if (p2 == null) {
				p1.put(s[1], p2 = new TreeMap<>());
				for (int i = 0; i < size; i++)
					if (s[1] != i && s[0] != i)
						p2.put(i, 0);
			}

			c = p2.get(s[2]);
			p2.put(s[2], c + 1);

			// ------------------------------------------

			Map<Integer, Map<Integer, Map<Integer, Integer>>> q1 = count4.get(s[0]);
			if (q1 == null)
				count4.put(s[0], q1 = new TreeMap<>());

			Map<Integer, Map<Integer, Integer>> q2 = q1.get(s[1]);
			if (q2 == null)
				q1.put(s[1], q2 = new TreeMap<>());

			Map<Integer, Integer> q3 = q2.get(s[2]);
			if (q3 == null) {
				q2.put(s[2], q3 = new TreeMap<>());
				for (int i = 0; i < size; i++)
					if (s[2] != i && s[1] != i && s[0] != i)
						q3.put(i, 0);
			}

			c = q3.get(s[3]);
			q3.put(s[3], c + 1);

			// ------------------------------------------

			Map<Integer, Map<Integer, Map<Integer, Map<Integer, Integer>>>> t1 = count5.get(s[0]);
			if (t1 == null)
				count5.put(s[0], t1 = new TreeMap<>());

			Map<Integer, Map<Integer, Map<Integer, Integer>>> t2 = t1.get(s[1]);
			if (t2 == null)
				t1.put(s[1], t2 = new TreeMap<>());

			Map<Integer, Map<Integer, Integer>> t3 = t2.get(s[2]);
			if (t3 == null)
				t2.put(s[2], t3 = new TreeMap<>());

			Map<Integer, Integer> t4 = t3.get(s[3]);
			if (t4 == null) {
				t3.put(s[3], t4 = new TreeMap<>());
				for (int i = 0; i < size; i++)
					if (s[3] != i && s[2] != i && s[1] != i && s[0] != i)
						t4.put(i, 0);
			}

			c = t4.get(s[4]);
			t4.put(s[4], c + 1);
		}

//		for (Entry<Integer, Map<Integer, Integer>> e1 : count2.entrySet()) {
//			System.out.print(e1.getKey());
//
//			for (Entry<Integer, Integer> e2 : e1.getValue().entrySet())
//				System.out.println("\t" + e2.getKey() + "\t" + e2.getValue());
//
//		}

//		for (Entry<Integer, Map<Integer, Map<Integer, Integer>>> e1 : count3.entrySet()) {
//			System.out.print(e1.getKey());
//
//			for (Entry<Integer, Map<Integer, Integer>> e2 : e1.getValue().entrySet()) {
//				System.out.print("\t" + e2.getKey());
//				int k = 0;
//				for (Entry<Integer, Integer> e3 : e2.getValue().entrySet()) {
//					System.out.println((k == 0 ? "\t" : "\t\t") + e3.getKey() + "\t" + e3.getValue());
//					k++;
//				}
//			}
//		}

		for (Entry<Integer, Map<Integer, Integer>> e1 : count2.entrySet()) {
			int k = 0;
			for (Entry<Integer, Integer> e2 : e1.getValue().entrySet()) {
				System.out.print((k == 0 ? "" : "|") + e2.getValue());
				k++;
			}
			System.out.println();
		}

		for (Entry<Integer, Map<Integer, Map<Integer, Integer>>> e1 : count3.entrySet()) {

			for (Entry<Integer, Map<Integer, Integer>> e2 : e1.getValue().entrySet()) {
				int k = 0;
				for (Entry<Integer, Integer> e3 : e2.getValue().entrySet()) {
					System.out.print((k == 0 ? "" : ",") + e3.getValue());
					k++;
				}
				System.out.print("|");
			}
			System.out.println();
		}

		for (Entry<Integer, Map<Integer, Map<Integer, Map<Integer, Integer>>>> e1 : count4.entrySet()) {

			for (Entry<Integer, Map<Integer, Map<Integer, Integer>>> e2 : e1.getValue().entrySet()) {
				int j = 0;
				for (Entry<Integer, Map<Integer, Integer>> e3 : e2.getValue().entrySet()) {
					System.out.print(j == 0 ? "" : ";");
					int k = 0;
					for (Entry<Integer, Integer> e4 : e3.getValue().entrySet()) {
						System.out.print((k == 0 ? "" : ",") + e4.getValue());
						k++;
					}
					j++;
				}
				System.out.print("|");
			}
			System.out.println();
		}

		for (Entry<Integer, Map<Integer, Map<Integer, Map<Integer, Map<Integer, Integer>>>>> e1 : count5.entrySet()) {

			for (Entry<Integer, Map<Integer, Map<Integer, Map<Integer, Integer>>>> e2 : e1.getValue().entrySet()) {
				int j = 0;
				for (Entry<Integer, Map<Integer, Map<Integer, Integer>>> e3 : e2.getValue().entrySet()) {
					System.out.print(j == 0 ? "" : ";");
					int k = 0;
					for (Entry<Integer, Map<Integer, Integer>> e4 : e3.getValue().entrySet()) {
						System.out.print(k == 0 ? "" : ",");
						int l = 0;
						for (Entry<Integer, Integer> e5 : e4.getValue().entrySet()) {
							System.out.print((l == 0 ? "" : ".") + e5.getValue());
							l++;
						}
						k++;
					}
					j++;
				}
				System.out.print("|");
			}
			System.out.println();
		}
	}
}
