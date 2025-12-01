package br.com.pereiraeng.math.timeseries;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import br.com.pereiraeng.core.ExtendedMath;
import br.com.pereiraeng.core.TimeUtils;
import br.com.pereiraeng.core.collections.ArrayUtils;

/**
 * Classe do objeto que representa uma série de medições onde se é
 * {@link RegP#fitFreq(int) forçada} uma periodicidade dos instantes de tempo
 * (ou seja, as medições tem-se de manter uma {@link RegP#getFreq() distância
 * temporal mínima} uns dos outros)
 * 
 * @author Philipe PEREIRA
 *
 */
public class RegP extends Reg {
	private static final long serialVersionUID = 1L;

	/**
	 * Frequência com que os registros são guardados
	 */
	private int freq;

	/**
	 * Frequência padrão com que os registros são guardados (15')
	 */
	private static final int FREQ_DEFAULT = 15;

	/**
	 * Construtor de um novo objeto de registro de medições. A distância de tempo
	 * mínima entre duas medições é de {@link RegP#FREQ_DEFAULT 15} minutos.
	 * 
	 * @param regs número de medições por horário
	 */
	public RegP(int regs) {
		this(regs, FREQ_DEFAULT);
	}

	/**
	 * 
	 * Construtor de um novo objeto de registro de medições
	 * 
	 * @param regs número de medições por horário
	 * @param freq frequência de tempo, em minutos, em que são inseridos na tabela
	 */
	public RegP(int regs, int freq) {
		this(new String[regs], freq);
	}

	public RegP(String[] labels, int freq) {
		super(labels);
		this.setFreq(freq);
	}

	public RegP(SrT<Integer> srt, int freq) {
		super(srt);
		this.setFreq(freq);
	}

	// --------------------------- SETTER'S/PUTTER'S ---------------------------

	@Override
	public float put(Integer ci, int pos, float value) {
		return super.put(fitFreq(ci), pos, value);
	}

	@Override
	public float[] put(Integer key, float[] values) {
		return super.put(fitFreq(key), values);
	}

	// ------------------------------- GETTER'S ------------------------------

	@Override
	public float get(Integer ci, int pos) {
		return super.get(fitFreq(ci), pos);
	}

	// ------------------------------- TIME FIT ------------------------------

	/**
	 * Função que retorna o intervalo mínimo de tempo, em minutos, entre duas
	 * medições
	 * 
	 * @return inteiro que indica a cadência de medições, em minutos
	 */
	public int getFreq() {
		return freq;
	}

	/**
	 * Função que altera o intervalo mínimo de tempo, em minutos, entre duas
	 * medições
	 * 
	 * @param freq inteiro que indica a cadência de medições, em minutos
	 */
	public void setFreq(int freq) {
		this.freq = freq;
	}

	/**
	 * Função que faz com que o número inteiro que representa a data seja múltiplo
	 * de um dado valor
	 * 
	 * @param ic inteiro que representa a data (ver {@link TimeUtils#toInt(Calendar)
	 *           conversão para inteiro})
	 * @return inteiro múltiplo da {@link RegP#freq frequência} mais próximo do
	 *         inteiro dado
	 */
	private int fitFreq(int ic) {
		return fitFreq(ic, getFreq());
	}

	/**
	 * Função que faz com que o número inteiro que representa a data seja múltiplo
	 * de um dado valor
	 * 
	 * @param ic   inteiro que representa a data (ver {@link TimeUtils#toInt(Calendar)
	 *             conversão para inteiro})
	 * @param freq frequência dada
	 * @return inteiro múltiplo da frequência mais próximo do inteiro dado
	 */
	private static int fitFreq(int ic, int freq) {
		// passar a frequência para segundos
		freq *= 60;
		// desloca metade da frequência (centraliza o intervalo)
		int o = ic + freq / 2;
		// arredonda para o maior múltiplo da frequência menor que o dado valor
		return o - ExtendedMath.mod(o, freq);
	}

	/**
	 * Função que indica o número de blocos em que há medições e em que há espaçõs
	 * vazios num registro periódico
	 * 
	 * @param range vetor com duas posições que indica o intervalo de tempo em que
	 *              deveria haver medições no registro
	 * @return matriz com duas colunas, sendo que na primeira estão os números de
	 *         {@link getFreq() intervalos} do registro em que há medições válidas;
	 *         na segunda coluna, os intervalos vazios
	 */
	public int[][] getContinuity(Calendar[] range) {
		int step = getFreq() * 60;
		int start, end;
		if (range != null) {
			start = TimeUtils.toInt(range[0]);
			end = TimeUtils.toInt(range[1]);
		} else {
			start = this.firstKey();
			end = this.lastKey();
		}
		if (this.size() == 0)
			return new int[][] { { 0, (end - start) / step + 1 } };

		List<int[]> out = new LinkedList<>();

		// leva em conta todas as colunas (ou seja, vê a distância entre as Entry)
		Iterator<Integer> it = this.keySet().iterator();

		// primeira iteração
		int ci = it.next();
		int dist = ci - start;
		if (dist != 0) // se o registro já não começa no começo...
			out.add(new int[] { 0, dist / step });

		if (it.hasNext()) {
			// demais (se houver...)
			int cont = 1, i = ci;
			while (it.hasNext()) {
				ci = it.next();
				dist = ci - i;

				if (dist == step)
					cont++;
				else {
					out.add(new int[] { cont, dist / step - 1 });
					cont = 1;
				}
				i = ci;
			}
			out.add(new int[] { cont, (end - i) / step });
		}
		return out.toArray(new int[out.size()][2]);
	}

	public int[][][] getContinuity(Calendar[] range, int... pos) {
		int step = getFreq() * 60;
		int start, end;

		int[][][] out = new int[pos.length][][];

		if (range != null) {
			start = TimeUtils.toInt(range[0]);
			end = TimeUtils.toInt(range[1]);

			if (this.size() == 0) {
				int[][] empty = { { 0, (end - start) / step + 1 } };
				for (int i = 0; i < out.length; i++)
					out[i] = empty;
				return out;
			}
		} else {
			if (this.size() < 2) {
				int[][] empty = { { 0, 0 } };
				for (int i = 0; i < out.length; i++)
					out[i] = empty;
				return out;
			}

			start = this.firstKey();
			end = this.lastKey();
		}

		for (int p = 0; p < pos.length; p++) {
			List<int[]> op = new LinkedList<>();

			// leva em conta somente as colunas indicadas (tem de ver se está vazia)
			Iterator<Entry<Integer, float[]>> it = this.entrySet().iterator();

			// primeira iteração
			Entry<Integer, float[]> e = it.next();
			int ci = e.getKey();
			while (it.hasNext() && Float.isNaN(e.getValue()[pos[p]])) {
				e = it.next();
				ci = e.getKey();
			}

			int dist = ci - start;
			if (dist != 0) // se o registro já não começa no começo...
				op.add(new int[] { 0, dist / step });

			if (it.hasNext()) {
				// demais (se houver...)

				int cont = 1, i = ci;
				while (it.hasNext()) {
					do {
						e = it.next();
						ci = e.getKey();
					} while (it.hasNext() && Float.isNaN(e.getValue()[pos[p]]));
					dist = ci - i;

					if (dist == step) {
						cont++;
					} else {
						op.add(new int[] { cont, dist / step - 1 });
						cont = it.hasNext() ? 1 : 0;
					}
					i = ci;
				}
				if (cont > 0)
					op.add(new int[] { cont, (end - i) / step });
			} else if (op.size() == 0)
				op.add(new int[] { 1, (end - ci) / step });

			out[p] = op.toArray(new int[op.size()][2]);
		}
		return out;
	}

	public void removeMed(int pos, int start, int length) {
		Iterator<Entry<Integer, float[]>> it = tailMap(start).entrySet().iterator();
		int i = 0;
		while (it.hasNext() && i <= length) {
			it.next().getValue()[pos] = Float.NaN;
			i++;
		}
	}

	// ------------------------ GETTER'S - COLLECTIONS ------------------------

	public RegP subReg(Calendar[] period) {
		return subReg(new Calendar[][] { period });
	}

	/**
	 * 
	 * @param periods períodos de tempo, definidos por uma matriz de duas colunas,
	 *                onde os elementos da primeira coluna indicam o começo de um
	 *                subintervalo e os da segunda indicam seu final
	 * @return
	 */
	public RegP subReg(Calendar[][] periods) {
		RegP out = new RegP(length(), getFreq());
		super.subReg(this, out, periods);
		return out;
	}

	public RegP invSubReg(Calendar[] period) {
		return invSubReg(new Calendar[][] { period });
	}

	/**
	 * 
	 * @param periods períodos de tempo, definidos por uma matriz de duas colunas,
	 *                onde os elementos da primeira coluna indicam o começo de um
	 *                subintervalo e os da segunda indicam seu final
	 * @return
	 */
	public RegP invSubReg(Calendar[][] periods) {
		RegP out = new RegP(length(), getFreq());
		super.invSubReg(this, out, periods);
		return out;
	}

	/**
	 * 
	 * @param fds
	 *            <ol>
	 *            <li>DS: não há diferenciação entre final de semana e dia de
	 *            semana;</i>
	 *            <li>DU e FDS: há diferenciação entre final de semana e dia de
	 *            semana;</i>
	 *            <li>DU, SA e DO: há diferenciação entre dia de semana, sábado e
	 *            domingo.</i>
	 *            </ol>
	 * @return
	 */
	public RegP[] splitSemana(int dias) {
		RegP[] out = new RegP[dias];
		if (dias == 1)
			out[0] = this;
		else {
			for (int i = 0; i < out.length; i++)
				out[i] = new RegP(this.length(), this.getFreq());
			super.splitSemana(out, this);
		}
		return out;
	}

	/**
	 * Função que cria um novo registro a partir de colunas deste registro
	 * 
	 * @param g colunas a serem copiadas
	 * @return novo registro com as colunas selecionadas
	 */
	@Override
	public RegP select(int... g) {
		RegP out = new RegP(g.length, getFreq());
		select(out, this, g);
		return out;
	}

	private static float[] nullArray = null;

	public void fillEmptySpaces(int begin, int end) {
		if (nullArray == null ? true : nullArray.length != length()) {
			nullArray = new float[length()];
			Arrays.fill(nullArray, Float.NaN);
		}

		int step = getFreq() * 60;
		for (int ci = begin; ci <= end; ci += step)
			if (!containsKey(ci))
				put(ci, nullArray);
	}

	public void persistUntilChange() {
		if (this.size() == 0)
			return;

		float[] lastValid = ArrayUtils.floatVec(Float.NaN, length());

		int freq = 60 * getFreq();

		Entry<Integer, float[]> e0 = this.firstEntry();
		int cin = this.lastKey();

		float[] values = e0.getValue();
		for (int i = 0; i < length(); i++)
			if (!Float.isNaN(values[i]))
				lastValid[i] = values[i];

		int ci0 = e0.getKey();
		for (int ci = ci0 + freq; ci <= cin; ci += freq) {
			values = this.get(ci);
			if (values == null) {
				float[] newValues = new float[length()];
				System.arraycopy(lastValid, 0, newValues, 0, length());
				this.put(ci, newValues);
			} else {
				for (int i = 0; i < length(); i++) {
					if (Float.isNaN(values[i])) {
						if (!Float.isNaN(lastValid[i]))
							values[i] = lastValid[i];
					} else
						lastValid[i] = values[i];
				}
			}
		}
	}
}