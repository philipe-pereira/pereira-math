package br.com.pereiraeng.math.timeseries;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.pereiraeng.core.collections.map.MyEntry;

/**
 * Classe dos objeto que representa uma <strong>seq</strong>uência de eventos
 * que ocorrem em diferentes instantes de tempo.
 * 
 * @author Philipe PEREIRA
 *
 * @param <V> classe do objeto que caracteriza o evento
 */
public class Seq<V> extends DtD<Calendar, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * Construtor do objeto da sequência de eventos
	 * 
	 * @param labels tags digitais
	 */
	public Seq(String[] labels) {
		super(labels);
	}

	public Seq(int regs) {
		this(new String[regs]);
	}

	@Override
	protected String toStringK(Calendar key) {
		return String.format("%1$tH:%1$tM:%1$tS.%1$tL %1$td/%1$tm/%1$ty", key);
	}

	@Override
	protected String toString(MyEntry<Integer, V> value) {
		return value.toString();
	}

	/**
	 * Função que retorna os períodos onde esta sequência de dados este num dado
	 * valor
	 * 
	 * @param tag etiqueta de uma dada sequência
	 * @return períodos de tempo, definidos por uma matriz de duas colunas, onde os
	 *         elementos da primeira coluna indicam o começo de um subintervalo e os
	 *         da segunda indicam seu final
	 */
	public Calendar[][] getPeriods(String tag) {
		return getPeriods(getValues(tag));
	}

	/**
	 * Função que retorna os períodos onde esta sequência de dados este num dado
	 * valor
	 * 
	 * @param index índice de uma dada sequência
	 * @return períodos de tempo, definidos por uma matriz de duas colunas, onde os
	 *         elementos da primeira coluna indicam o começo de um subintervalo e os
	 *         da segunda indicam seu final
	 */
	public Calendar[][] getPeriods(int index) {
		return getPeriods(getValues(index));
	}

	private Calendar[][] getPeriods(TreeMap<Calendar, V> values) {
		List<Calendar[]> out = new LinkedList<>();

		V state = null;
		Calendar[] cs = null;
		for (Entry<Calendar, V> e : values.entrySet()) {
			V si = e.getValue();
			if (si != state) { // se houve uma mudança de estado
				if (cs == null)
					out.add(cs = new Calendar[] { e.getKey(), null });
				else {
					cs[1] = e.getKey();
					cs = null;
				}
				state = si;
			}
		}
		if (cs != null)
			cs[1] = this.lastKey();

		return out.toArray(new Calendar[out.size()][2]);
	}

	@Override
	protected Calendar stepUp(Calendar t) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(t.getTimeInMillis() + 1);
		return c;
	}

	// ------------------------- IN FILE -------------------------

	private transient int fileCount = 0;

	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	public int getFileCount() {
		return fileCount;
	}

	public boolean hasFile() {
		return this.fileCount > 0;
	}
}