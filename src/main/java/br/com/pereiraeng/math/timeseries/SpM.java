package br.com.pereiraeng.math.timeseries;

import br.com.pereiraeng.core.collections.map.MyEntry;

/**
 * <strong>S</strong>am<strong>p</strong>led <strong>M</strong>easurements
 * 
 * @author Philipe Pereira
 *
 */
public class SpM<V> extends DtD<Double, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * Construtor do objeto da sequência de eventos
	 * 
	 * @param labels tags digitais
	 */
	public SpM(String[] labels) {
		super(labels);
	}

	public SpM(int regs) {
		this(new String[regs]);
	}

	@Override
	protected Double stepUp(Double l) {
		return l + 1;
	}

	@Override
	protected String toStringK(Double key) {
		return String.format("%.4g", key);
	}

	@Override
	protected String toString(MyEntry<Integer, V> value) {
		return value.toString();
	}
}
