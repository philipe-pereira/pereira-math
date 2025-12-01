package br.com.pereiraeng.math.timeseries;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * <strong>T</strong>em<strong>p</strong>oral <strong>D</strong>ata
 * 
 * @author Philipe PEREIRA
 *
 * @param <T> classe do objeto que indica o tempo em que o evento ocorreu
 * @param <V> classe do objeto que caracteriza o evento
 */
public abstract class TpD<T, V> extends TreeMap<T, V> {
	private static final long serialVersionUID = 1L;

	/**
	 * Vetor com a indicação das etiquetas das medições (mais que isso, o tamanho
	 * deste vetor é a referência do número de medições para cada instante (ver
	 * {@link SrT#length()})
	 */
	protected String[] labels;

	/**
	 * Construtor do objeto que representa dados etiquetados pelo tempo
	 * 
	 * @param regs número de grandezas registradas
	 */
	public TpD(int regs) {
		this(new String[regs]);
	}

	/**
	 * Construtor do objeto que representa dados etiquetados pelo tempo
	 * 
	 * @param labels etiquetas das grandezas
	 */
	public TpD(String[] labels) {
		this.labels = labels;
	}

	/**
	 * Função que retorna o número total de medições feitas a cada instante de tempo
	 * (Número de registros armazenados por horário, tamanho dos vetores de
	 * <code>float</code> que são associados a cada horário)
	 * 
	 * @return número de medições por tempo, sendo portanto igual ao tamanho do
	 *         vetor que é retornado pelo mapa
	 */
	public int length() {
		return this.labels.length;
	}

	/**
	 * Função que altera o número total de medições armazenadas a cada instante de
	 * tempo. Se o número for maior que o atual, as novas posições estarão em
	 * branco, enquanto que se ele for menor, truncarar-se-á os registros
	 * 
	 * @param regs novo número de medições em cada instante de tempo
	 */
	public void setRegs(int regs) {
		// corrigindo eventuais desvios da entrada
		if (regs < 0)
			regs = 0;
		else if (regs == length())
			return;

		// altera tamanho
		this.labels = Arrays.copyOf(this.labels, regs);
	}

	/**
	 * Função que altera o número total de medições armazenadas a cada instante de
	 * tempo, de moda a assegurar um dado tamanho mínimo do vetor. Se o número for
	 * maior que o atual, as novas posições estarão em branco, enquanto que se ele
	 * for menor, nada acontecerá.
	 * 
	 * @param regs
	 */
	public void ensureSize(int regs) {
		if (regs <= length())
			return;
		else
			setRegs(regs);
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for (java.util.Map.Entry<T, V> e : entrySet())
			out.append(String.format("%s|%s\r\n", toStringK(e.getKey()), toString(e.getValue())));
		return out.toString();
	}

	protected abstract String toStringK(T key);

	protected abstract String toString(V value);

	// -------------------------------- LABELS --------------------------------

	public String getLabel(int index) {
		return labels[index];
	}

	public String[] getLabels() {
		return labels;
	}

	public void setLabel(int index, String label) {
		if (index >= 0 && index < labels.length)
			labels[index] = label;
		else
			System.out.printf("Tentou-se inserir a etiqueta %s na posição %d, mas não há espaço (%d ao total)\n", label,
					index, labels.length);
	}

	public void setLabels(String[] labels) {
		if (labels.length == length())
			this.labels = labels;
		else
			throw new IllegalArgumentException("O index contém um número de etiquetas maior que o número de medições");
	}

	/**
	 * Função que procura no index o índice de uma medição que está designado por
	 * uma dada sequência de caracteres
	 * 
	 * @param label sequência de caracteres
	 * @return índice da medição
	 */
	public int getIndex(String label) {
		for (int i = 0; i < labels.length; i++)
			if (label.equals(labels[i]))
				return i;
		return -1;
	}

	public Map<String, Integer> createLabelTable() {
		Map<String, Integer> out = new HashMap<>();
		for (int i = 0; i < labels.length; i++)
			out.put(labels[i], i);
		return out;
	}

	/**
	 * Função que corrige o vetor de etiquetas dos dados quando se está removendo um
	 * desses dados
	 * 
	 * @param tpd registro
	 * @param p   posição do dado que está sendo removido
	 */
	protected static <T, V> void removeLabels(TpD<T, V> tpd, int p) {
		for (int i = p; i < tpd.length() - 1; i++)
			tpd.labels[i] = tpd.labels[i + 1];
		tpd.labels = Arrays.copyOf(tpd.labels, tpd.length() - 1);
	}
}
