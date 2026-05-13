package br.com.pereiraeng.math.timeseries;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import br.com.pereiraeng.core.ExtendedMath;
import br.com.pereiraeng.core.Flow;
import br.com.pereiraeng.core.TimeUtils;
import br.com.pereiraeng.core.collections.ListUtils;
import br.com.pereiraeng.core.collections.MapUtils;
import br.com.pereiraeng.math.timeseries.unit.Med;
import br.com.pereiraeng.math.timeseries.unit.Meds;

/**
 * Classe do objeto que representa uma série de medições onde o instante de
 * tempo é identificado a partir de um {@link TimeUtils#toInt(Calendar) número
 * inteiro}
 * 
 * @author Philipe PEREIRA
 *
 */
public class Reg extends SrT<Integer> {
	private static final long serialVersionUID = 1L;

	public Reg(int regs) {
		this(new String[regs]);
	}

	public Reg(String[] labels) {
		super(labels);
	}

	public Reg(SrT<Integer> srt) {
		this(srt.getLabels());
		super.putAll(srt);
	}

	@Override
	public String toStringK(Integer key) {
		return String.format("%1$tH:%1$tM %1$td/%1$tm/%1$ty", TimeUtils.toCalendar(key));
	}

	// --------------------------- SETTER'S/PUTTER'S ---------------------------

	/**
	 * Função que adiciona uma medição feita num dado horário numa dada posição do
	 * vetor de registros.
	 * 
	 * @param c     horário da medição
	 * @param pos   posição no vetor de registros
	 * @param value valor da medição
	 * @return valor anterior que estava na posição
	 */
	public float put(Calendar c, int pos, float value) {
		return put(TimeUtils.toInt(c), pos, value);
	}

	/**
	 * Função que associa a um dado horário um vetor de medições
	 * 
	 * @param c      horário da medição
	 * @param values medições relativas ao horário
	 * @return vetor de medições que era associado a este horário
	 */
	public float[] put(Calendar c, float[] values) {
		return put(TimeUtils.toInt(c), values);
	}

	/**
	 * Função que carrega o objeto registro com o mapa de medições
	 * 
	 * @param pos posição no vetor de registros
	 * @param med mapa ordenado as medições
	 */
	public void set(int pos, Map<Calendar, Float> med) {
		Iterator<Entry<Calendar, Float>> it = med.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Calendar, Float> e = it.next();
			put(e.getKey(), pos, e.getValue());
		}
	}

	/**
	 * Função que adiciona uma medição numa dada posição na tabela de registros.
	 * 
	 * @param med      medição feita numa dada hora e onde obteve-se um dado valor
	 * @param position posição no vetor
	 * @return valor anterior que estava na posição
	 */
	public float put(Med med, int position) {
		return this.put(TimeUtils.toInt(med.getTime()), position, med.getValue());
	}

	/**
	 * Função que adiciona uma medição na tabela de registros.
	 * 
	 * @param med medição feita numa dada hora e onde obteve-se um dado valor (o
	 *            objeto Med deve também conter a informação do
	 *            {@link Med#getChannel() canal} preenchida de modo que o valor seja
	 *            alocado na posição correta)
	 * @return valor anterior que estava na posição
	 */
	public float put(Med med) {
		return this.put(TimeUtils.toInt(med.getTime()), med.getChannel(), med.getValue());
	}

	public float[] put(Meds meds) {
		return this.put(TimeUtils.toInt(meds.getTime()), meds.getValue());
	}

	/**
	 * Função que remove do registro as medições que tomadas para certos dias da
	 * semana
	 * 
	 * @param days vetor de <code>boolean</code> com no mínimo 7 posições, onde
	 *             <code>true</code> indica que o dia será mantida,
	 *             <code>false</code> que será apagado
	 */
	public void removeDays(boolean[] days) {
		if (days.length < 7)
			throw new IllegalArgumentException("Vetor de dias da semana com menos de 7 posições.");
		Iterator<Map.Entry<Integer, float[]>> it = this.entrySet().iterator();
		while (it.hasNext())
			if (!days[TimeUtils.toCalendar(it.next().getKey()).get(Calendar.DAY_OF_WEEK) - 1])
				it.remove();
	}

	public void removeMonth(boolean[] months) {
		if (months.length < 12)
			throw new IllegalArgumentException("Vetor de dias da semana com menos de 7 posições.");
		Iterator<Map.Entry<Integer, float[]>> it = this.entrySet().iterator();
		while (it.hasNext())
			if (!months[TimeUtils.toCalendar(it.next().getKey()).get(Calendar.MONTH)])
				it.remove();
	}

	/**
	 * Função que remove do registro as medições que estão fora de uma dada faixa
	 * <strong>horária</strong>
	 * 
	 * @param hours vetor com os objetos {@link Calendar} que delimitam a faixa de
	 *              horas a mantida no registro
	 */
	public void removeHours(Calendar[] hours) {
		Iterator<Map.Entry<Integer, float[]>> it = this.entrySet().iterator();

		while (it.hasNext()) {
			Calendar c = TimeUtils.toCalendar(it.next().getKey());
			if (TimeUtils.timeIsBefore(c, hours[0]) || TimeUtils.timeIsBefore(hours[1], c))
				it.remove();
		}
	}

	// --------------------------- GETTER'S - VALUES ---------------------------

	/**
	 * Função que retorna um dado valor de medição realizada num dado instante,
	 * estando alocado numa dada posição
	 * 
	 * @param c   horário da medição
	 * @param pos posição no vetor de registros
	 * @return valor da medição
	 */
	public float get(Calendar c, int pos) {
		return get(TimeUtils.toInt(c), pos);
	}

	/**
	 * Função que retorna os valores de medições realizadas num dado instante
	 * 
	 * @param c horário da medição
	 * @return valor da medição
	 */
	public float[] getC(Calendar c) {
		return get(TimeUtils.toInt(c));
	}

	// ------------------------------- GETTER'S ------------------------------

	/**
	 * Função que retorna registro numa dada posição
	 * 
	 * @param index inteiro que indica a ordem temporal do registro
	 * @return objeto {@link Calendar Calendar} do registro na ordem indicada
	 */
	public Calendar getTime(int index) {
		return TimeUtils.toCalendar(MapUtils.getEntryAt(this, index).getKey());
	}

	/**
	 * Função que retorna os instantes de tempo deste registro
	 * 
	 * @return vetor com os objetos {@link Calendar} dos instantes de tempo deste
	 *         registro
	 */
	public Calendar[] getTimes() {
		Calendar[] out = new Calendar[this.size()];
		int i = 0;
		for (Integer ci : this.keySet())
			out[i++] = TimeUtils.toCalendar(ci);
		return out;
	}

	public double[] getTimesD() {
		double[] out = new double[this.size()];
		int i = 0;
		for (Integer ci : this.keySet())
			out[i++] = ci;
		return out;
	}

	// extremidades

	/**
	 * Função que retorna o começo do registro
	 * 
	 * @return objeto {@link Calendar Calendar} da primeira medição
	 */
	public Calendar getBegin() {
		return TimeUtils.toCalendar(this.firstKey());
	}

	/**
	 * Função que retorna o final do registro
	 * 
	 * @return objeto {@link Calendar Calendar} da última medição
	 */
	public Calendar getEnd() {
		return TimeUtils.toCalendar(this.lastKey());
	}

	/**
	 * Função que retorna o começo e o final do registro
	 * 
	 * @return vetor com dois objetos {@link Calendar Calendar} da primeira e última
	 *         medição
	 */
	public Calendar[] getBeginEnd() {
		return new Calendar[] { getBegin(), getEnd() };
	}

	public Med getFirst(int pos) {
		return getValid(pos, false);
	}

	public Med getLast(int pos) {
		return getValid(pos, true);
	}

	private Med getValid(int pos, boolean order) {
		NavigableMap<Integer, float[]> dm = order ? this.descendingMap() : this;
		for (Entry<Integer, float[]> e : dm.entrySet()) {
			float v = e.getValue()[pos];
			if (!Float.isNaN(v))
				return new Med(TimeUtils.toCalendar(e.getKey()), v);
		}
		return null;
	}

	// limites

	// max

	public static Med getMax(Reg reg, int pos) {
		return getMax(reg, pos, false);
	}

	/**
	 * Função que registra o carregamento máximo de um dado registro.
	 * 
	 * @param reg registro de medições
	 * @param pos posição do registro onde serão analisados os dados
	 * @param abs <code>true</code> para considerar o valor em módulo,
	 *            <code>false</code> se não
	 * @return maior registro achado
	 */
	public static Med getMax(Reg reg, int pos, boolean abs) {
		return getLimit(reg, pos, true, abs);
	}

	// min

	public static Med getMin(Reg reg, int pos) {
		return getMin(reg, pos, false);
	}

	/**
	 * Função que registra o carregamento mínimo de um dado registro.
	 * 
	 * @param reg registro de medições
	 * @param pos posição do registro onde serão analisados os dados
	 * @param abs <code>true</code> para considerar o valor em módulo,
	 *            <code>false</code> se não
	 * @return menor registro achado
	 */
	public static Med getMin(Reg reg, int pos, boolean abs) {
		return getLimit(reg, pos, false, abs);
	}

	// max | min

	/**
	 * Função que calcula ou o máximo ou o mínimo de uma série temporal
	 * 
	 * @param reg série temporal
	 * @param pos posição
	 * @param max <code>true</code> para calcular o máximo, <code>false</code> para
	 *            o mínimo
	 * @param abs <code>true</code> para considerar o valor em módulo,
	 *            <code>false</code> se não
	 * @return
	 */
	private static Med getLimit(Reg reg, int pos, final boolean max, boolean abs) {
		// corrrigir desvios da entrada
		if (pos >= reg.length())
			pos = reg.length() - 1;

		// maior horário do mês
		float m = max ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
		int date = -1;

		for (Entry<Integer, float[]> e : reg.entrySet()) {
			float v = e.getValue()[pos];
			if (abs)
				v = Math.abs(v);

			if (max ? v > m : v < m) {
				m = v;
				date = e.getKey();
			}
		}
		return new Med(TimeUtils.toCalendar(date), m);
	}

	// max & min

	/**
	 * Função que calcula o máximo e o mínimo de um dado registro.
	 * 
	 * @param reg registro de medições
	 * @param pos posição do registro onde serão analisados os dados
	 * @param abs <code>true</code> para considerar o valor em módulo,
	 *            <code>false</code> se não
	 * @return vetor que contem duas posições, a primeira com menor {@link Med
	 *         registro} achado e a segunda com o maior
	 */
	public static Med[] getLimits(Reg reg, int pos, boolean abs) {
		// corrrigir desvios da entrada
		if (pos >= reg.length())
			pos = reg.length() - 1;

		if (reg.size() == 0)
			return new Med[2];

		// maior horário do mês
		float m = Float.POSITIVE_INFINITY;
		int datem = -1;

		float M = Float.NEGATIVE_INFINITY;
		int dateM = -1;

		for (Entry<Integer, float[]> e : reg.entrySet()) {
			float v = e.getValue()[pos];
			if (abs)
				v = Math.abs(v);

			// máximo
			if (v > M) {
				M = v;
				dateM = e.getKey();
			}
			// mínimo
			if (v < m) {
				m = v;
				datem = e.getKey();
			}
		}
		return new Med[] { new Med(TimeUtils.toCalendar(datem), m), new Med(TimeUtils.toCalendar(dateM), M) };
	}

	public static Med[] getLimits(Reg reg, int pos, int begin, int end) {
		// corrrigir desvios da entrada
		if (pos >= reg.length())
			pos = reg.length() - 1;

		// maior horário do mês
		float m = Float.POSITIVE_INFINITY;
		int datem = -1;

		float M = Float.NEGATIVE_INFINITY;
		int dateM = -1;

		SortedMap<Integer, float[]> subMap = reg.subMap(begin, end);
		for (Entry<Integer, float[]> e : subMap.entrySet()) {
			float v = e.getValue()[pos];

			// máximo
			if (v > M) {
				M = v;
				dateM = e.getKey();
			}
			// mínimo
			if (v < m) {
				m = v;
				datem = e.getKey();
			}
		}
		return new Med[] { new Med(TimeUtils.toCalendar(datem), m), new Med(TimeUtils.toCalendar(dateM), M) };
	}

	// média

	/**
	 * 
	 * @param reg
	 * @param pos
	 * @param freq
	 * @param per
	 *             <ul>
	 *             <li>{@link Calendar#DAY_OF_MONTH} para uma análise de um dia;</i>
	 *             <li>{@link Calendar#DAY_OF_WEEK} para uma análise de uma
	 *             semana.</i>
	 *             </ul>
	 * @return
	 */
	public static float[][] getMedia(Reg reg, int pos, int freq, int per) {
		boolean s = per == Calendar.DAY_OF_WEEK;
		float[][] soma = new float[2][(s ? 7 : 1) * 1440 / freq];
		int[] ns = new int[soma[0].length];

		Iterator<Map.Entry<Integer, float[]>> it = reg.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, float[]> e = it.next();

			Calendar c = TimeUtils.toCalendar(e.getKey());
			int min = c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE);
			if (s)
				min += (c.get(Calendar.DAY_OF_WEEK) - 1) * 1440;
			int p = min / freq;
			soma[0][p] = 60f * min;
			float v = e.getValue()[pos];
			if (!Float.isNaN(v)) {
				soma[1][p] += v;
				ns[p]++;
			}
		}

		for (int h = 0; h < soma[0].length; h++) {
			if (ns[h] != 0)
				soma[1][h] /= ns[h];
			else
				soma[1][h] = Float.NaN;
		}

		return soma;
	}

	// média para cada hora

	private static final float NULL_THRESHOLD = .02f;

	/**
	 * Função que calcula os valores médios de um dado conjunto de registros para
	 * cada uma das horas do dia
	 * 
	 * @param reg registro de medições
	 * @param pos posição no mapa de registros de onde serão tomados os dados para
	 *            cálculo da média
	 * @return vetor que contém 24 posições, cada uma com o registro da média da
	 *         hora correpondente
	 */
	public static float[] getMediaHoraria(Reg reg, int pos) {
		// determinar o threshold (i.e., o limite até o qual considera que a medição é
		// nula, logo não entrará na média)

		Iterator<Map.Entry<Integer, float[]>> it = reg.entrySet().iterator();
		int day = -1, count = 0;
		float sum = 0f, maxMedDiaria = Float.NEGATIVE_INFINITY;
		while (it.hasNext()) {
			Entry<Integer, float[]> e = it.next();
			Calendar c = TimeUtils.toCalendar(e.getKey());

			int d = c.get(Calendar.DAY_OF_MONTH);
			if (d != day) { // trocou de dia, confere a média
				if (count != 0) {
					float med = sum / count;
					if (med > maxMedDiaria)
						maxMedDiaria = med;
				}
				// troca o dia
				day = d;
				// reinicia a soma
				sum = 0f;
				// zera o contador
				count = 0;
			}

			float v = e.getValue()[pos];
			if (!Float.isNaN(v)) {
				sum += v;
				count++;
			}
		}

		final float threshold = maxMedDiaria * NULL_THRESHOLD;

		// calcular a média horária
		float[] out = new float[24];
		int[] ns = new int[24];

		it = reg.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, float[]> e = it.next();

			int h = TimeUtils.toCalendar(e.getKey()).get(Calendar.HOUR_OF_DAY);
			float v = e.getValue()[pos];
			if (!Float.isNaN(v) ? Math.abs(v) > threshold : false) {
				out[h] += v;
				ns[h]++;
			}
		}

		for (int h = 0; h < 24; h++) {
			if (ns[h] != 0)
				out[h] /= ns[h];
			else
				out[h] = Float.NaN;
		}

		return out;
	}

	/**
	 * Função que calcula os valores médios <strong>quadráticos</strong> de um dado
	 * conjunto de registros para cada uma das horas do dia
	 * 
	 * @param reg registro de medições
	 * @param pos posição no mapa de registros de onde serão tomados os dados para
	 *            cálculo da média
	 * @return vetor que contém 24 posições, cada uma com o registro da média
	 *         quadrática da hora correpondente
	 */
	public static float[] getMediaQuadHoraria(Reg reg, int pos) {
		// determinar o threshold (i.e., o limite até o qual considera que a medição é
		// nula, logo não entrará na média)

		Iterator<Map.Entry<Integer, float[]>> it = reg.entrySet().iterator();
		int day = -1, count = 0;
		float sum = 0f, maxMedDiaria = Float.NEGATIVE_INFINITY;
		while (it.hasNext()) {
			Entry<Integer, float[]> e = it.next();
			Calendar c = TimeUtils.toCalendar(e.getKey());

			int d = c.get(Calendar.DAY_OF_MONTH);
			if (d != day) { // trocou de dia, confere a média
				if (count != 0) {
					float med = sum / count;
					if (med > maxMedDiaria)
						maxMedDiaria = med;
				}
				// troca o dia
				day = d;
				// reinicia a soma
				sum = 0f;
				// zera o contador
				count = 0;
			}

			float v = e.getValue()[pos];
			if (!Float.isNaN(v)) {
				sum += v;
				count++;
			}
		}

		final float threshold = maxMedDiaria * NULL_THRESHOLD;

		// calcular a média quadrática horária
		float[] soma = new float[24];
		int[] ns = new int[24];

		it = reg.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, float[]> e = it.next();

			int h = TimeUtils.toCalendar(e.getKey()).get(Calendar.HOUR_OF_DAY);
			float v = e.getValue()[pos];
			if (!Float.isNaN(v) ? Math.abs(v) > threshold : false) {
				soma[h] += Math.pow(v, 2);
				ns[h]++;
			}
		}

		for (int h = 0; h < 24; h++) {
			if (ns[h] != 0) {
				soma[h] /= ns[h];
				soma[h] = (float) Math.sqrt(soma[h]);
			} else
				soma[h] = Float.NaN;
		}

		return soma;
	}

	/**
	 * Função que calcula os valores médios de um dado conjunto de registros para
	 * cada uma das horas do dia mas somente para alguns dias da semana
	 * 
	 * @param reg  registro de medições
	 * @param pos  posição no mapa de registros de onde serão tomados os dados para
	 *             cálculo da média
	 * @param week vetor com 7 posições, um para cada dia da semana, sendo que
	 *             aqueles com <code>true</code> representarão aqueles que serão
	 *             incluídos no cálculo
	 * @return lista que contém 24 posições, cada uma com o registro da média da
	 *         hora correpondente
	 */
	public static float[] getMediaHoraria(Reg reg, int pos, boolean[] week) {
		float[] out = new float[24];
		int[] ns = new int[24];

		Iterator<Map.Entry<Integer, float[]>> it = reg.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, float[]> e = it.next();

			Calendar c = TimeUtils.toCalendar(e.getKey());
			float v = e.getValue()[pos];

			if (week[c.get(Calendar.DAY_OF_WEEK) - 1] && !Float.isNaN(v)) {
				int h = c.get(Calendar.HOUR_OF_DAY);
				out[h] += v;
				ns[h]++;
			}
		}

		for (int h = 0; h < 24; h++) {
			if (ns[h] != 0)
				out[h] /= ns[h];
			else
				out[h] = Float.NaN;
		}

		return out;
	}

	// máximo e mínimo para cada hora

	public static Calendar[] getMinHoraria(Reg reg, int pos) {
		return getLimHoraria(reg, pos, false);
	}

	public static Calendar[] getMaxHoraria(Reg reg, int pos) {
		return getLimHoraria(reg, pos, true);
	}

	private static Calendar[] getLimHoraria(Reg reg, int pos, final boolean max) {
		Calendar[] out = new Calendar[24];

		float[] vs = new float[24];
		Arrays.fill(vs, max ? Float.MIN_VALUE : Float.MAX_VALUE);

		Iterator<Map.Entry<Integer, float[]>> it = reg.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, float[]> e = it.next();

			Calendar c = TimeUtils.toCalendar(e.getKey());
			float v = e.getValue()[pos];
			int h = c.get(Calendar.HOUR_OF_DAY);
			if (max ? v > vs[h] : v < vs[h]) {
				out[h] = c;
				vs[h] = v;
			}
		}

		return out;
	}

	/**
	 * 
	 * @param reg
	 * @param pos
	 * @return matriz com duas linhas e 24 colunas, sendo que na primeira linha
	 *         estão os horários dos máximos e na segunda linha os horários dos
	 *         mínimos
	 */
	public static Calendar[][] getLimHoraria(Reg reg, int pos) {
		Calendar[][] out = new Calendar[2][24];

		float[][] vs = new float[2][24];
		Arrays.fill(vs[0], Float.MIN_VALUE);
		Arrays.fill(vs[1], Float.MAX_VALUE);

		Iterator<Map.Entry<Integer, float[]>> it = reg.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, float[]> e = it.next();

			Calendar c = TimeUtils.toCalendar(e.getKey());
			float v = e.getValue()[pos];
			int h = c.get(Calendar.HOUR_OF_DAY);

			// máximo
			if (v > vs[0][h]) {
				out[0][h] = c;
				vs[0][h] = v;
			}

			// míximo
			if (v < vs[1][h]) {
				out[1][h] = c;
				vs[1][h] = v;
			}
		}

		return out;
	}

	// ------------------------- CADÊNCIA -------------------------

	public int getMinFreq() {
		boolean flag = false;
		int last = 0;
		HashSet<Integer> dist = new HashSet<>();
		for (Integer key : this.keySet()) {
			if (flag)
				dist.add(key - last);
			else
				flag = true;
			last = key;
		}
		return ExtendedMath.mdc(ListUtils.toIntArray(dist));
	}

	// ------------------------- EXPURGAR MEDIÇÕES -------------------------

	/**
	 * Valor máximo que um registro pode se desviar da média horária sem ser
	 * expurgado
	 */
	private static final float EXPURGA_SUP = 1.25f, EXPURGA_INF = 0.3f;

	public static void expurga(Reg reg, int pos) {
		expurga(reg, pos, 1);
	}

	/**
	 * Função que expurga de um conjunto os registros cujos valores maiores são
	 * maiores do que a média horária do {@link RegP#EXPURGA_SUP limite} estipulado.
	 * 
	 * @param reg lista cujos valores serão analisados e cujos valores distoantes
	 *            serão eliminados
	 * @param pos posição do registro onde serão analisados os dados
	 * @param lim
	 *            <ol start="0">
	 *            <li>{@link #EXPURGA_INF expurga inferior};</i>
	 *            <li>{@link #EXPURGA_SUP expurga superior};</i>
	 *            <li>expurga ambos.</i>
	 *            </ol>
	 */
	public static void expurga(Reg reg, int pos, final int lim) {
		// média horária
		float[][] media = new float[lim == 2 ? 2 : 1][];
		media[0] = Reg.getMediaHoraria(reg, pos);
		if (lim == 2)
			media[1] = new float[24];

		// porcentagem a partir da qual se expurga

		// limiares calculados a partir da média horária
		for (int h = 0; h < media[0].length; h++) {
			float m = Math.abs(media[0][h]);
			media[0][h] = (lim == 0 ? EXPURGA_INF : EXPURGA_SUP) * m;
			if (lim == 2)
				media[1][h] = EXPURGA_INF * m;
		}

		// se só houver um registro, apagar a entrada, senão somente anular a
		// medição
		final boolean remove = reg.length() == 1;

		Iterator<Map.Entry<Integer, float[]>> it = reg.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, float[]> e = it.next();

			int h = TimeUtils.toCalendar(e.getKey()).get(Calendar.HOUR_OF_DAY);
			float[] r = e.getValue();

			// se o registro for maior/menor que o limite
			float v = Math.abs(r[pos]);
			if ((lim == 0 ? v < media[0][h] : v > media[0][h]) || (lim == 2 ? v < media[1][h] : false)) {
				if (remove)
					it.remove();
				else
					r[pos] = Float.NaN;
			}
		}
	}

	/**
	 * Função que reparte um registro em dois (ou três) registros, diferenciando
	 * medições de dia de semana e de final de semana
	 * 
	 * @param out vetor a ser preenchido com os registros novos obtidos da partição.
	 *            O número de posições do vetor de registros determina a forma de se
	 *            particionar as medições:
	 *            <ol>
	 *            <li>DS: não há diferenciação entre final de semana e dia de
	 *            semana;</i>
	 *            <li>DU e FDS: há diferenciação entre final de semana e dia de
	 *            semana;</i>
	 *            <li>DU, SA e DO: há diferenciação entre dia de semana, sábado e
	 *            domingo.</i>
	 *            </ol>
	 * @param r   registro a ser repartido
	 */
	public static void splitSemana(Reg[] out, Reg r) {
		if (out.length == 1)
			out[0] = r;
		else {
			Iterator<Map.Entry<Integer, float[]>> it = r.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, float[]> e = it.next();
				int k = e.getKey();
				Calendar c = TimeUtils.toCalendar(k);

				// dia da semana
				int pos = c.get(Calendar.DAY_OF_WEEK);
				pos = pos == Calendar.SATURDAY ? 1 : (pos == Calendar.SUNDAY ? (out.length == 2 ? 1 : 2) : 0);

				out[pos].put(k, e.getValue());

				it.remove();
			}
		}
	}

	// ------------------------ GETTER'S - COLLECTIONS ------------------------

	/**
	 * Função que retorna um conjunto de medições ordenado pelo horário
	 * 
	 * @param pos posição no vetor de registros
	 * @return conjunto ordenado de medições
	 */
	public TreeSet<Med> getSet(int pos) {
		TreeSet<Med> out = null;
		if (pos >= 0 && pos < length()) {
			out = new TreeSet<>();

			for (Entry<Integer, float[]> iv : this.entrySet())
				out.add(new Med(TimeUtils.toCalendar(iv.getKey()), iv.getValue()[pos]));
		}
		return out;
	}

	/**
	 * Função que retorna um mapa de medições ordenado pelo horário
	 * 
	 * @param pos posição no vetor de registros
	 * @return mapa ordenado as medições
	 */
	public TreeMap<Calendar, Float> getMap(int pos) {
		TreeMap<Calendar, Float> out = null;
		if (pos >= 0 && pos < length()) {
			out = new TreeMap<>();

			for (Entry<Integer, float[]> c : this.entrySet())
				out.put(TimeUtils.toCalendar(c.getKey()), c.getValue()[pos]);
		}
		return out;
	}

	/**
	 * Função que retorna uma matriz com duas linhas, ordenada pelo horário, com a
	 * {@link TimeUtils#toInt(Calendar) chave inteira} do objeto {@link Reg} convertida
	 * em número decimal, podendo-se remover as medições não válidas (NaN).
	 * 
	 * @param pos posição no vetor de registros
	 * @param beg instante de tempo inicial
	 * @param end instante de tempo final
	 * @param nan <code>true</code> se for para manter os registros com NaN,
	 *            <code>false</code> se for para removê-los
	 * @return par de vetores, formando uma matriz, estando na primeira posição os
	 *         inteiros representado os instantes de tempo (<strong>cada unidade
	 *         representa um segundo</strong>) e na segunda os valores registrados
	 */
	public double[][] getMatrix(int pos, Calendar beg, Calendar end, boolean nan) {
		return getMatrix(this, pos, TimeUtils.toInt(beg), TimeUtils.toInt(end), nan);
	}

	/**
	 * 
	 * @param in
	 * @param out
	 * @param periods períodos de tempo, definidos por uma matriz de duas colunas,
	 *                onde os elementos da primeira coluna indicam o começo de um
	 *                subintervalo e os da segunda indicam seu final
	 */
	public static void subReg(Reg in, Reg out, Calendar[][] periods) {
		for (Calendar[] period : periods)
			out.putAll(in.subMap(TimeUtils.toInt(period[0]), TimeUtils.toInt(period[1])));
	}

	/**
	 * 
	 * @param in
	 * @param out
	 * @param periods períodos de tempo, definidos por uma matriz de duas colunas,
	 *                onde os elementos da primeira coluna indicam o começo de um
	 *                subintervalo e os da segunda indicam seu final
	 */
	public static void invSubReg(Reg in, Reg out, Calendar[][] periods) {
		if (in.size() == 0)
			return;
		int begin = in.firstKey();
		for (Calendar[] period : periods) {
			out.putAll(in.subMap(begin, TimeUtils.toInt(period[0])));
			begin = TimeUtils.toInt(period[1]);
		}
		if (begin <= in.lastKey())
			out.putAll(in.subMap(begin, in.lastKey()));
	}

	/**
	 * Função que cria um novo registro a partir de colunas deste registro
	 * 
	 * @param g colunas a serem copiadas
	 * @return novo registro com as colunas selecionadas
	 */
	public Reg select(int... g) {
		Reg out = new Reg(g.length);
		select(out, this, g);
		return out;
	}

	// --------------------- STATISTICS ---------------------

	/**
	 * Função que conta o número de vezes que um carregamento ultrapassou um valor
	 * dado.
	 * 
	 * @param pos        posição do registro onde serão analisados os dados
	 * @param ref        valor de referência
	 * @param above      <code>true</code> para contar valores acima da referência,
	 *                   <code>false</code> para abaixo
	 * @param thresholds porcentagens do valor de referência em que conta a
	 *                   ultrapassagem
	 * @return vetor com o número de vezes que os registros indicam uma
	 *         ultrapassagem do valor de referência
	 */
	public int[] countUlt(int pos, float ref, final boolean above, final float... thresholds) {
		return countUlt(pos, -1, -1, ref, above, thresholds);
	}

	public int[] countUlt(int pos, int begin, int end, float ref, final boolean above, final float... thresholds) {
		if (thresholds.length == 0)
			return new int[0];

		// conta o número de vezes que ultrapassou o limite
		int[] cont = new int[thresholds.length];

		float[] t = new float[thresholds.length + 2];
		t[0] = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < thresholds.length; i++)
			t[i + 1] = thresholds[i] * ref;
		t[thresholds.length + 1] = Float.POSITIVE_INFINITY;

		int r = above ? 0 : t.length - 2;
		float[] range = new float[] { t[r], t[r + 1] };
		Collection<float[]> values = (begin != -1 && end != -1 ? subMap(begin, end) : this).values();
		for (float[] e : values) {
			float v = e[pos];

			if (v > range[1]) {
				// ultrapassagem positiva
				if (above)
					cont[r]++;
				r++;
				range[0] = range[1];
				range[1] = t[r + 1];
			} else if (v < range[0]) {
				// ultrapassagem negativa
				r--;
				if (!above)
					cont[r]++;
				range[1] = range[0];
				range[0] = t[r];
			}
		}
		return cont;
	}
	


	// ------------------------------ FLOW ------------------------------

	/**
	 * Função que transfere os dados deste registro através de {@link Med blocos de
	 * medições} para um outro objeto que interfaceia {@link Flow}
	 * 
	 * @param flow    objeto que receberá as medições
	 * @param pos     posição dos dados neste registro
	 * @param channel endereço portado pelos {@link Med blocos de medições}
	 */
	public void transfer(Flow<Med> flow, int pos, int channel) {
		if (pos >= 0 && pos < length()) {
			for (Entry<Integer, float[]> iv : this.entrySet()) {
				Med m = new Med(TimeUtils.toCalendar(iv.getKey()), iv.getValue()[pos]);
				m.setChannel(channel);
				flow.incomingData(m);
			}
		}
	}

	/**
	 * Função que transfere todos os dados deste registro através de {@link Med
	 * blocos de medições} para um outro objeto que interfaceia {@link Flow}, sendo
	 * que a medida que este registro é enviado, ele vai sendo apagado.
	 * 
	 * @param flow objeto que receberá as medições
	 */
	public void send(Flow<Meds> flow) {
		Iterator<Entry<Integer, float[]>> it = entrySet().iterator();
		Meds m = new Meds();
		while (it.hasNext()) {
			Entry<Integer, float[]> iv = it.next();

			m.setTime(TimeUtils.toCalendar(iv.getKey()));
			m.setValue(iv.getValue());

			flow.incomingData(m);

			it.remove();
		}
	}

	public void sendMed(Flow<Med> flow, int... pos) {
		Iterator<Entry<Integer, float[]>> it = entrySet().iterator();
		Med m = new Med();
		while (it.hasNext()) {
			Entry<Integer, float[]> iv = it.next();

			m.setTime(TimeUtils.toCalendar(iv.getKey()));

			float[] values = iv.getValue();
			for (int i = 0; i < values.length; i++) {
				m.setValue(values[i]);
				m.setChannel(pos.length == 0 ? i : pos[i]);

				flow.incomingData(m);
			}

			it.remove();
		}
	}

	public void sendMed(Flow<Med> flow, Collection<Set<Integer>> pos) {
		if (pos.size() != this.length())
			throw new IllegalArgumentException(
					"A relação de posições não bate com o número de medições por instante de tempo");
		Iterator<Entry<Integer, float[]>> it = entrySet().iterator();
		Med m = new Med();
		while (it.hasNext()) {
			Entry<Integer, float[]> iv = it.next();

			m.setTime(TimeUtils.toCalendar(iv.getKey()));

			float[] values = iv.getValue();
			Iterator<Set<Integer>> it2 = pos.iterator();
			for (int i = 0; i < values.length; i++) {
				m.setValue(values[i]);

				Set<Integer> chs = it2.next();
				for (Integer ch : chs) {
					m.setChannel(ch);
					flow.incomingData(m);
				}
			}

			it.remove();
		}
	}
}