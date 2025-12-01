package br.com.pereiraeng.math.timeseries;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.pereiraeng.core.TimeUtils;
import br.com.pereiraeng.core.collections.MapUtils;

/**
 * Classe que representa uma <strong>s</strong>é<strong>r</strong>ie
 * <strong>t</strong>emporal de um ou mais valores decimais.
 * 
 * @author Philipe PEREIRA
 *
 * @param <T> classe do número que representa o instante de tempo
 */
public class SrTD<T extends Number> extends TpD<T, double[]> {
	private static final long serialVersionUID = 1L;

	/**
	 * Construtor do objeto que representa dados etiquetados pelo tempo
	 * 
	 * @param regs número de grandezas registradas
	 */
	public SrTD(int regs) {
		this(new String[regs]);
	}

	/**
	 * Construtor do objeto que representa dados etiquetados pelo tempo
	 * 
	 * @param labels etiquetas das grandezas
	 */
	public SrTD(String[] labels) {
		super(labels);
	}

	@Override
	protected String toStringK(T key) {
		return key == null ? "" : key.toString();
	}

	@Override
	protected String toString(double[] value) {
		return Arrays.toString(value);
	}

	// -------------------------- NÚMERO DE REGISTROS --------------------------

	@Override
	public void setRegs(int regs) {
		super.setRegs(regs);

		for (Entry<T, double[]> tv : this.entrySet()) {
			// para cada horário, recria os vetores

			double[] oldD = tv.getValue();
			double[] newD = Arrays.copyOf(oldD, this.length());

			// completar o vetor com NaN caso o tamanho final seja maior que atual
			for (int i = oldD.length; i < newD.length; i++)
				newD[i] = Double.NaN;

			// troca a chave
			this.put(tv.getKey(), newD);
		}
	}

	// --------------------------- SETTER'S/PUTTER'S ---------------------------

	/**
	 * Função que adiciona uma medição feita num dado horário numa dada posição do
	 * vetor de registros. Caso não exista nenhuma medição neste horário, cria-se um
	 * novo vetor para ele.
	 * 
	 * @param ci    inteiro equivalente ao horário (ver
	 *              {@link TimeUtils#toInt(Calendar) conversão para inteiro})
	 * @param pos   posição no vetor de registros
	 * @param value valor da medição
	 * @return valor anterior que estava na posição
	 */
	public double put(T ci, int pos, double value) {
		if (pos >= length())
			// se não tem espaço, alarga...
			insert(pos);
		double[] oldF = super.get(ci);
		if (oldF != null) {
			double old = oldF[pos];
			oldF[pos] = value;
			return old;
		} else {
			oldF = new double[length()];
			for (int i = 0; i < length(); i++) {
				if (i == pos)
					oldF[i] = value;
				else
					oldF[i] = Double.NaN;
			}
			super.put(ci, oldF);
			return Double.NaN;
		}
	}

	/**
	 * Função que associa a um dado horário um vetor de medições
	 * 
	 * @param key    inteiro equivalente ao horário (ver
	 *               {@link TimeUtils#toInt(Calendar) conversão para inteiro})
	 * @param values medições relativas ao horário
	 * @return vetor de medições que era associado a este horário
	 */
	@Override
	public double[] put(T key, double[] values) {
		if (values.length != length())
			throw new IllegalArgumentException("O tamanho do vetor de medições deve ser igual a " + length() + ".");
		return super.put(key, values);
	}

	/**
	 * Função que carrega o objeto registro com o mapa de medições
	 * 
	 * @param med mapa ordenado as medições
	 * @param pos posição no vetor de registros
	 */
	public void set(Map<T, Double> med, int pos) {
		Iterator<Entry<T, Double>> it = med.entrySet().iterator();
		while (it.hasNext()) {
			Entry<T, Double> e = it.next();
			put(e.getKey(), pos, e.getValue());
		}
	}

	/**
	 * Função que adiciona mais uma série de registros
	 * 
	 * @param med tabela de dispersão que associa os inteiros que representam os
	 *            instantes de tempo às medições
	 */
	public void add(TreeMap<T, Double> med) {
		// insere uma coluna
		insert(length());
		for (Entry<T, Double> e : med.entrySet())
			put(e.getKey(), length() - 1, e.getValue());
	}

	// --------------------------- GETTER'S - VALUES ---------------------------

	/**
	 * Função que retorna um dado valor de medição realizada num dado instante,
	 * estando alocado numa dada posição
	 * 
	 * @param t   número que indica o instante dos registro (chave da tabela)
	 * @param pos posição no vetor de registros
	 * @return valor da medição
	 */
	public double get(T t, int pos) {
		double[] ms = this.get(t);
		if (ms != null)
			return ms[pos];
		else
			return Double.NaN;
	}

	/**
	 * Função que retorna um vetor com os valores para diferentes instantes de um
	 * dos registros
	 * 
	 * @param pos posição do registro no objeto
	 * @return vetor com as medidas de um dos registros
	 */
	public double[] getV(int pos) {
		double[] out = new double[this.size()];
		int j = 0;
		for (double[] fs : this.values())
			out[j++] = fs[pos];
		return out;
	}

	/**
	 * Função que retorna registro numa dada posição temporal
	 * 
	 * @param index inteiro que indica a ordem temporal do registro
	 * @return objeto vetor com as medidas de umd dado horário
	 */
	public double[] getP(int index) {
		if (index < this.size())
			return MapUtils.getEntryAt(this, index).getValue();
		return null;
	}

	/**
	 * Função que retorna o valor numa dada posição temporal
	 * 
	 * @param index inteiro que indica a ordem temporal do registro
	 * @param pos   posição do registro no objeto
	 * @return valor decimal
	 */
	public double getP(int index, int pos) {
		double[] vs = getP(index);
		return vs != null ? vs[pos] : Double.NaN;
	}

	/**
	 * Função que retorna um mapa de valores ordenado pelo horário
	 * 
	 * @param pos posição no vetor de registros
	 * @return mapa ordenado dos valores
	 */
	public TreeMap<T, Double> getMapI(int pos) {
		TreeMap<T, Double> out = null;
		if (pos >= 0 && pos < length()) {
			out = new TreeMap<>();

			for (Entry<T, double[]> e : this.entrySet())
				out.put(e.getKey(), e.getValue()[pos]);
		}
		return out;
	}

	/**
	 * Função que retorna um mapa de medições ordenado pelo horário
	 * 
	 * @param s rótulo da medição (caso o {@link #getIndex(String) index} esteja
	 *          habilitado)
	 * @return mapa ordenado as medições
	 */
	public TreeMap<T, Double> getMapI(String s) {
		return getMapI(getIndex(s));
	}

	// ------------------------------ OPERATIONS ------------------------------

	// inserção

	/**
	 * Função que insere novo(s) registro(s) vazio(s) nesta sequência de dados
	 * 
	 * @param pos posições do(s) novo(s) registro(s) de medições
	 */
	public void insert(int... pos) {
		insert(this, pos);
	}

	/**
	 * Função que insere um novo registro vazio na sequência de dados
	 * 
	 * @param label etiqueta dos dados
	 */
	public void insert(String label) {
		int p = length();
		insert(p);
		labels[p] = label;
	}

	/**
	 * Função que insere novo(s) registro(s) vazio(s) na sequência de dados
	 * 
	 * @param <T> classe do número que representa o instante de tempo
	 * @param st  sequência de dados
	 * @param pos posições do(s) novo(s) registro(s) de medições
	 */
	public static <T extends Number> void insert(SrTD<T> st, int... pos) {
		// insere um conjunto de medições por vez, logo a função chama a si
		// própria com uma coluna por vez
		if (pos.length > 1) {
			Arrays.sort(pos);

			// inserir de trás para frente um conjunto por vez
			for (int i = pos.length - 1; i >= 0; i--)
				insert(st, pos[i]);
		} else if (pos.length == 1) {
			int p = pos[0];

			// corrigindo eventuais desvios da entrada
			if (p < 0)
				p = 0;
			else if (p > st.length())
				p = st.length();

			// altera tamanho e desloca o vetor de etiquetas, deixando um null na posição
			// recém-inserida
			st.labels = Arrays.copyOf(st.labels, st.length() + 1);
			for (int i = st.length() - 1; i > p; i--)
				st.labels[i] = st.labels[i - 1];
			st.labels[p] = null;

			for (Entry<T, double[]> tv : st.entrySet()) {
				// para cada horário, recria os vetores

				double[] oldD = tv.getValue();
				double[] newD = new double[st.length()];

				// transferir dados de um vetor para outro, pulando a nova casa
				// que está sendo inserida
				int k = 0;
				for (int i = 0; i < oldD.length; i++) {
					if (i == p)
						newD[k++] = Double.NaN;
					newD[k++] = oldD[i];
				}

				st.put(tv.getKey(), newD);
			}
		}
	}
}
