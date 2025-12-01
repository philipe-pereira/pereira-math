package br.com.pereiraeng.math.timeseries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import br.com.pereiraeng.core.collections.ArrayUtils;
import br.com.pereiraeng.core.collections.map.MyEntry;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <strong>D</strong>ifferent <strong>t</strong>ime <strong>D</strong>ata
 * 
 * @author Philipe Pereira
 *
 * @param <T> classe do objeto que indica o tempo em que o evento ocorreu
 * @param <V> classe do objeto que caracteriza o evento
 */
public abstract class DtD<T, V> extends TpD<T, MyEntry<Integer, V>> {
	private static final long serialVersionUID = 1L;

	public DtD(int regs) {
		this(new String[regs]);
	}

	public DtD(String[] labels) {
		super(labels);
	}

	@Override
	public void setRegs(int regs) {
		boolean truncate = regs < length();
		super.setRegs(regs);

		if (truncate) {
			Iterator<Entry<T, MyEntry<Integer, V>>> it = this.entrySet().iterator();
			while (it.hasNext())
				if (it.next().getValue().getKey() >= regs)
					it.remove();
		}
	}

	// putter's

	public void put(String label, TreeMap<T, V> data) {
		put(ArrayUtils.indexOf(labels, label), data);
	}

	public void put(int pos, TreeMap<T, V> data) {
		for (Entry<T, V> e : data.entrySet())
			put(e.getKey(), pos, e.getValue());
	}

	/**
	 * Função que insere no registro um dado
	 * 
	 * @param t     data e hora do evento
	 * @param label etiqueta do evento
	 * @param value valor associado ao dado
	 */
	public void put(T t, String label, V value) {
		put(t, ArrayUtils.indexOf(labels, label), value);
	}

	/**
	 * Função que insere no registro um dado
	 * 
	 * @param t     data e hora do evento
	 * @param pos   posição do registro
	 * @param value valor associado ao dado
	 */
	public void put(T t, int pos, V value) {
		MyEntry<Integer, V> entry = this.put(t, new MyEntry<>(pos, value));
		while (entry != null) {
			// se já existir um registro neste instante
			T t0 = stepUp(t);
			entry = this.put(t0, entry);
			t = t0;
		}
	}

	protected abstract T stepUp(T c);

	// remoção

	/**
	 * Função que remove uma coluna de medições deste registro
	 * 
	 * @param pos colunas a serem copiadas
	 */
	public void remove(int... pos) {
		remove(this, pos);
	}

	/**
	 * Função que remove uma coluna de medições de um dado registro
	 * 
	 * @param dtd registro a ser modificado
	 * @param pos colunas a serem copiadas
	 */
	public static <T, V> void remove(DtD<T, V> dtd, int... pos) {
		// remove um conjunto de medições por vez, logo a função chama a si própria com
		// uma coluna por vez
		if (pos.length > 1) {
			Arrays.sort(pos);

			// remover de trás para frente um conjunto por vez
			for (int i = pos.length - 1; i >= 0; i--)
				remove(dtd, new int[] { pos[i] });
		} else if (pos.length == 1) {
			int p = pos[0];

			// verificar os argumentos
			if (p < 0 || p >= dtd.length())
				return;

			// novo tamanho do vetor
			removeLabels(dtd, p);

			Iterator<Entry<T, MyEntry<Integer, V>>> it = dtd.entrySet().iterator();
			while (it.hasNext())
				if (it.next().getValue().getKey() == p)
					it.remove();
		}
	}

	public static <T, V> void transfer(DtD<T, V> from, int f, DtD<T, V> to, int t) {
		// verificar os argumentos
		if (f < 0 || f >= from.length())
			throw new IllegalArgumentException(String.format("O registro de origem não possui o índice %d.", f));

		// no máximo põe-se os registros na última posição
		if (t > to.length())
			t = to.length();

		// dados
		if (from.size() > 0) {
			for (Entry<T, MyEntry<Integer, V>> tv : from.entrySet()) {
				MyEntry<Integer, V> p2v = tv.getValue();
				if (p2v.getKey() == f)
					to.put(tv.getKey(), t, p2v.getValue());
			}
		}

		// index
		to.setLabel(t, from.getLabel(f));
	}

	// getters

	/**
	 * Função que retorna uma relação de todos os eventos de um dada etiqueta
	 * 
	 * @param label etiqueta do evento
	 * @return {@link TreeMap tabela de dispersão ordenada} que associa para cada
	 *         data e hora um objeto do evento
	 */
	public TreeMap<T, V> getValues(String label) {
		return getValues(ArrayUtils.indexOf(labels, label));
	}

	/**
	 * Função que retorna uma relação de todos os eventos de um dada posição
	 * 
	 * @param index posição do evento
	 * @return {@link TreeMap tabela de dispersão ordenada} que associa para cada
	 *         data e hora um objeto do evento
	 */
	public TreeMap<T, V> getValues(int index) {
		TreeMap<T, V> out = new TreeMap<>();
		for (Entry<T, MyEntry<Integer, V>> e : this.entrySet())
			if (index == e.getValue().getKey())
				out.put(e.getKey(), e.getValue().getValue());
		return out;
	}

	/**
	 * Função que retorna uma lista com a primeira ocorrência de cada uma das tags
	 * 
	 * @return lista com o primeiro valor de cada uma das tags
	 */
	public List<V> getFirsts() {
		return getNexts(null);
	}

	/**
	 * Função que retorna uma lista com a primeira ocorrência de cada uma das tags a
	 * partir de um dado instante de tempo
	 * 
	 * @param start instante de tempo
	 * @return lista com o valor de cada uma das tags
	 */
	public List<V> getNexts(T start) {
		List<V> out = new ArrayList<V>(labels.length);
		SortedMap<T, MyEntry<Integer, V>> sm = start == null ? this : this.tailMap(start);
		for (int i = 0; i < labels.length; i++) {
			V value = null;
			for (MyEntry<Integer, V> e : sm.values()) {
				if (e.getKey() == i) {
					value = e.getValue();
					break;
				}
			}
			out.add(value);
		}
		return out;
	}
}
