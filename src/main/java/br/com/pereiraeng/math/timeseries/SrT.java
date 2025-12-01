package br.com.pereiraeng.math.timeseries;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import br.com.pereiraeng.core.TimeUtils;
import br.com.pereiraeng.core.collections.ArrayUtils;
import br.com.pereiraeng.core.collections.ListUtils;
import br.com.pereiraeng.core.collections.MapUtils;

import java.util.Map.Entry;

import br.com.pereiraeng.math.Dupla;
import br.com.pereiraeng.math.expression.Expressao;

/**
 * Classe que representa uma <strong>s</strong>é<strong>r</strong>ie
 * <strong>t</strong>emporal de uma ou mais medições.
 * 
 * @author Philipe PEREIRA
 *
 * @param <T> classe do número que representa o instante de tempo
 */
public class SrT<T extends Number> extends TpD<T, float[]> {
	private static final long serialVersionUID = 1L;

	/**
	 * Construtor do objeto que representa dados etiquetados pelo tempo
	 * 
	 * @param regs número de grandezas registradas
	 */
	public SrT(int regs) {
		this(new String[regs]);
	}

	/**
	 * Construtor do objeto que representa dados etiquetados pelo tempo
	 * 
	 * @param labels etiquetas das grandezas
	 */
	public SrT(String[] labels) {
		super(labels);
	}

	@Override
	protected String toStringK(T key) {
		return key == null ? "" : key.toString();
	}

	@Override
	protected String toString(float[] value) {
		return Arrays.toString(value);
	}

	// -------------------------- NÚMERO DE REGISTROS --------------------------

	@Override
	public void setRegs(int regs) {
		super.setRegs(regs);

		for (Entry<T, float[]> tv : this.entrySet()) {
			// para cada horário, recria os vetores

			float[] oldF = tv.getValue();
			float[] newF = Arrays.copyOf(oldF, this.length());

			// completar o vetor com NaN caso o tamanho final seja maior que atual
			for (int i = oldF.length; i < newF.length; i++)
				newF[i] = Float.NaN;

			// troca a chave
			this.put(tv.getKey(), newF);
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
	public float put(T ci, int pos, float value) {
		if (pos >= length())
			// se não tem espaço, alarga...
			insert(pos);
		float[] oldF = super.get(ci);
		if (oldF != null) {
			float old = oldF[pos];
			oldF[pos] = value;
			return old;
		} else {
			oldF = new float[length()];
			for (int i = 0; i < length(); i++) {
				if (i == pos)
					oldF[i] = value;
				else
					oldF[i] = Float.NaN;
			}
			super.put(ci, oldF);
			return Float.NaN;
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
	public float[] put(T key, float[] values) {
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
	public void set(Map<T, Float> med, int pos) {
		Iterator<Entry<T, Float>> it = med.entrySet().iterator();
		while (it.hasNext()) {
			Entry<T, Float> e = it.next();
			put(e.getKey(), pos, e.getValue());
		}
	}

	/**
	 * Função que adiciona mais uma série de registros
	 * 
	 * @param med tabela de dispersão que associa os inteiros que representam os
	 *            instantes de tempo às medições
	 */
	public void add(TreeMap<T, Float> med) {
		// insere uma coluna
		insert(length());
		for (Entry<T, Float> e : med.entrySet())
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
	public float get(T t, int pos) {
		float[] ms = this.get(t);
		if (ms != null)
			return ms[pos];
		else
			return Float.NaN;
	}

	/**
	 * Função que retorna um vetor com os valores para diferentes instantes de um
	 * dos registros
	 * 
	 * @param pos posição do registro no objeto
	 * @return vetor com as medidas de um dos registros
	 */
	public float[] getV(int pos) {
		float[] out = new float[this.size()];
		int j = 0;
		for (float[] fs : this.values())
			out[j++] = fs[pos];
		return out;
	}

	/**
	 * Função que retorna registro numa dada posição temporal
	 * 
	 * @param index inteiro que indica a ordem temporal do registro
	 * @return objeto vetor com as medidas de umd dado horário
	 */
	public float[] getP(int index) {
		if (index < this.size())
			return MapUtils.getEntryAt(this, index).getValue();
		return null;
	}

	/**
	 * Função que retorna o valor numa dada posição temporal
	 * 
	 * @param index inteiro que indica a ordem temporal do registro
	 * @param pos   posição do registro no objeto
	 * @return valor da medição
	 */
	public float getP(int index, int pos) {
		float[] vs = getP(index);
		return vs != null ? vs[pos] : Float.NaN;
	}

	/**
	 * Função que retorna um mapa de medições ordenado pelo horário
	 * 
	 * @param pos posição no vetor de registros
	 * @return mapa ordenado das medições
	 */
	public TreeMap<T, Float> getMapI(int pos) {
		TreeMap<T, Float> out = null;
		if (pos >= 0 && pos < length()) {
			out = new TreeMap<>();

			for (Entry<T, float[]> e : this.entrySet())
				out.put(e.getKey(), e.getValue()[pos]);
		}
		return out;
	}

	/**
	 * Função que retorna um mapa de medições ordenado pelo horário
	 * 
	 * @param s rótulo da medição (caso o {@link #getIndex(String) index} esteja
	 *          habilitado)
	 * @return mapa ordenado das medições
	 */
	public TreeMap<T, Float> getMapI(String s) {
		return getMapI(getIndex(s));
	}

	/**
	 * Função que retorna uma matriz com duas linhas, ordenada pelo horário
	 * 
	 * @param pos posição no vetor de registros
	 * @return par de vetores, formando uma matriz, estando na primeira posição os
	 *         inteiros representado os instantes de tempo (<strong>cada unidade
	 *         representa um minuto</strong>) e na segunda os valores registrados
	 */
	public double[][] getMatrix(int pos) {
		return getMatrix(pos, true);
	}

	/**
	 * Função que retorna uma matriz com duas linhas, ordenada pelo horário,
	 * podendo-se remover as medições não válidas ( {@link Float#NaN} ).
	 * 
	 * @param pos posição no vetor de registros
	 * @param nan <code>true</code> se for para manter os registros com
	 *            {@link Float#NaN}, <code>false</code> se for para removê-los
	 * @return par de vetores, formando uma matriz, estando na primeira posição os
	 *         inteiros representado os instantes de tempo e na segunda os valores
	 *         registrados
	 */
	public double[][] getMatrix(int pos, boolean nan) {
		return getMatrix(this, pos, null, null, nan);
	}

	/**
	 * 
	 * @param <T> classe do número que representa o instante de tempo
	 * @param st
	 * @param pos
	 * @param beg
	 * @param end
	 * @param nan
	 * @return par de vetores, formando uma matriz, estando na primeira posição os
	 *         inteiros representado os instantes de tempo e na segunda os valores
	 *         registrados
	 */
	protected static <T extends Number> double[][] getMatrix(SrT<T> st, int pos, T beg, T end, boolean nan) {
		SortedMap<T, float[]> map = null;
		if (beg == null && end == null)
			map = st;
		else
			map = st.subMap(beg, end);

		if (nan) {
			// se for para manter os NaN...
			double[][] out = null;
			if (pos >= 0 && pos < st.length()) {
				out = new double[2][map.size()];
				int i = 0;
				for (Entry<T, float[]> c : map.entrySet()) {
					out[0][i] = c.getKey().doubleValue();
					out[1][i++] = c.getValue()[pos];
				}
			}
			return out;
		} else {
			// se for excluir os NaN...
			List<Double> xs = new LinkedList<>();
			List<Float> ys = new LinkedList<>();
			if (pos >= 0 && pos < st.length()) {
				for (Entry<T, float[]> c : map.entrySet()) {
					float y = c.getValue()[pos];
					if (!Float.isNaN(y)) {
						xs.add(c.getKey().doubleValue());
						ys.add(y);
					}
				}
			}

			double[] xa = new double[xs.size()];
			int i = 0;
			for (Double x : xs)
				xa[i++] = x;

			double[] ya = new double[ys.size()];
			i = 0;
			for (Float y : ys)
				ya[i++] = y;

			return new double[][] { xa, ya };
		}
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
	public static <T extends Number> void insert(SrT<T> st, int... pos) {
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

			// altera tamanho e desloca o vetor de etiquetas, deixando um null
			// na posição recém-inserida
			st.labels = Arrays.copyOf(st.labels, st.length() + 1);
			for (int i = st.length() - 1; i > p; i--)
				st.labels[i] = st.labels[i - 1];
			st.labels[p] = null;

			for (Entry<T, float[]> tv : st.entrySet()) {
				// para cada horário, recria os vetores

				float[] oldF = tv.getValue();
				float[] newF = new float[st.length()];

				// transferir dados de um vetor para outro, pulando a nova casa
				// que está sendo inserida
				int k = 0;
				for (int i = 0; i < oldF.length; i++) {
					if (i == p)
						newF[k++] = Float.NaN;
					newF[k++] = oldF[i];
				}

				st.put(tv.getKey(), newF);
			}
		}
	}

	// remoção

	/**
	 * Função que remove uma coluna de medições deste registro
	 * 
	 * @param pos colunas a serem copiadas
	 */
	public void removeP(int... pos) {
		removeP(this, pos);
	}

	/**
	 * Função que remove uma coluna de medições de um dado registro
	 * 
	 * @param <T> classe do número que representa o instante de tempo
	 * @param st  registro a ser modificado
	 * @param pos colunas a serem copiadas
	 */
	public static <T extends Number> void removeP(SrT<T> st, int... pos) {
		// remove um conjunto de medições por vez, logo a função chama a si própria com
		// uma coluna por vez
		if (pos.length > 1) {
			Arrays.sort(pos);

			// remover de trás para frente um conjunto por vez
			for (int i = pos.length - 1; i >= 0; i--)
				removeP(st, pos[i]);
		} else if (pos.length == 1) {
			int p = pos[0];

			// verificar os argumentos
			if (p < 0 || p >= st.length())
				return;

			// novo tamanho do vetor
			removeLabels(st, p);

			for (Entry<T, float[]> tv : st.entrySet()) {
				// ler todos os horários

				float[] f = tv.getValue();
				float[] newF = new float[st.length()];

				// transferir dados de um vetor para outro, pulando aquele que
				// será removido
				int k = 0;
				for (int i = 0; i < f.length; i++) {
					if (i != p)
						newF[k++] = f[i];
				}

				// substitui no registro o vetor velho pelo novo
				st.put(tv.getKey(), newF);
			}
		}
	}

	/**
	 * Função que remove as tags do registro que só possuirem medições vazias
	 */
	public void removeEmpty() {
		boolean[] check = new boolean[length()];

		for (float[] v : this.values())
			for (int i = 0; i < v.length; i++)
				check[i] |= !Float.isNaN(v[i]);

		List<Integer> pos = new LinkedList<>();
		for (int i = 0; i < length(); i++)
			if (!check[i])
				pos.add(i);

		removeP(ListUtils.toIntArray(pos));
	}

	/**
	 * Função que troca de posições duas colunas deste registro
	 * 
	 * @param i índice de uma das colunas
	 * @param j índice da outra coluna
	 */
	public void swap(int i, int j) {
		swap(this, i, j);
	}

	/**
	 * Função que troca de posições duas colunas de um dado registro
	 * 
	 * @param <T> classe do número que representa o instante de tempo
	 * @param in  registro a ser modificado
	 * @param i   índice de uma das colunas
	 * @param j   índice da outra coluna
	 */
	public static <T extends Number> void swap(SrT<T> in, int i, int j) {
		if (i < 0 || i >= in.length() || j < 0 || j >= in.length())
			throw new IllegalArgumentException("O registro não possui o número de medições indicado.");

		for (float[] f : in.values()) {
			// ler todos os horários
			float temp = f[i];
			f[i] = f[j];
			f[j] = temp;
		}
	}

	public void replace(int pos, float value, T begin, T end) {
		replace(this, pos, value, begin, end);
	}

	/**
	 * 
	 * @param <T>   classe do número que representa o instante de tempo
	 * @param r
	 * @param pos
	 * @param value
	 * @param begin
	 * @param end
	 */
	public static <T extends Number> void replace(SrT<T> r, int pos, float value, T begin, T end) {
		if (r.size() == 0)
			return;

		// somente a interseção do conjunto de medições e o conjunto de
		// correções é que é modificado
		T b = begin.doubleValue() > r.firstKey().doubleValue() ? begin : r.firstKey();
		T e = end.doubleValue() < r.lastKey().doubleValue() ? end : r.lastKey();

		if (b.doubleValue() > e.doubleValue()) // se a interseção é nula
			return;

		SortedMap<T, float[]> sm = r.subMap(b, e);
		for (float[] vs : sm.values())
			vs[pos] = value;
	}

	/**
	 * Função que remove os instantes de tempo em que os valores das medições
	 * associadas estão zeradas
	 * 
	 * @param and <code>true</code> para apagar o instante de tempo só se todos
	 *            estiverem zerados, <code>false</code> para apagar o registro se
	 *            qualquer um deles estiver zerado
	 */
	public void removeNull(boolean and) {
		Iterator<Entry<T, float[]>> it = this.entrySet().iterator();
		while (it.hasNext()) {
			float[] vs = it.next().getValue();
			if (and) {
				// todos eles
				boolean flag = true;
				for (int i = 0; i < vs.length; i++) {
					if (vs[i] != 0f) {
						flag = false;
						break;
					}
				}
				if (flag)
					it.remove();
			} else {
				// qualquer um deles
				for (int i = 0; i < vs.length; i++) {
					if (vs[i] == 0f) {
						it.remove();
						break;
					}
				}
			}
		}
	}

	/**
	 * Função que remove os instantes de tempo em que os valores de medições
	 * associadas estão vazios (sendo preenchidos, portanto, com o valor
	 * {@link Float#NaN número inválido})
	 * 
	 * @param and <code>true</code> para apagar o instante de tempo só se todos
	 *            estiverem {@link Float#NaN inválidos}, <code>false</code> para
	 *            apagar o registro se qualquer um deles estiver inválido
	 */
	public void removeNaN(boolean and) {
		Iterator<Entry<T, float[]>> it = this.entrySet().iterator();
		while (it.hasNext()) {
			float[] vs = it.next().getValue();
			if (and) {
				// todos eles
				boolean flag = true;
				for (int i = 0; i < vs.length; i++) {
					if (!Float.isNaN(vs[i])) {
						flag = false;
						break;
					}
				}
				if (flag)
					it.remove();
			} else {
				// qualquer um deles
				for (int i = 0; i < vs.length; i++) {
					if (Float.isNaN(vs[i])) {
						it.remove();
						break;
					}
				}
			}
		}
	}

	/**
	 * Função que transfere os valores de um conjunto de medições para <strong>a
	 * posição final</strong> de outro
	 * 
	 * @param <T>  classe do número que representa o instante de tempo
	 * @param from conjunto de medições de onde serão selecionados os dados
	 * @param f    índice da coluna de origem
	 * @param to   conjunto de medições no final do qual irão os dados
	 */
	public static <T extends Number> void transfer(SrT<T> from, int f, SrT<T> to) {
		transfer(from, f, to, to.length());
	}

	/**
	 * Função que transfere os valores de um conjunto de medições para outro
	 * 
	 * @param <T>  classe do número que representa o instante de tempo
	 * @param from conjunto de medições de onde serão selecionados os dados
	 * @param f    índice da coluna de origem
	 * @param to   conjunto de medições para onde irão os dados
	 * @param t    índice da coluna de destino
	 */
	public static <T extends Number> void transfer(SrT<T> from, int f, SrT<T> to, int t) {
		// verificar os argumentos
		if (f < 0 || f >= from.length())
			throw new IllegalArgumentException(String.format("O registro de origem não possui o índice %d.", f));

		// no máximo põe-se os registros na última posição
		if (t > to.length())
			t = to.length();

		// dados
		if (from.size() > 0) {
			for (Entry<T, float[]> tv : from.entrySet()) {
				float value = tv.getValue()[f];
				if (!Float.isNaN(value))
					to.put(tv.getKey(), t, value);
			}
		} else if (t == to.length())
			insert(to, t);

		// index
		to.setLabel(t, from.getLabel(f));
	}

	/**
	 * Função que preenche um registro a partir de colunas de um dado registro
	 * 
	 * @param <T> classe do número que representa o instante de tempo
	 * @param out registro preenchido com as colunas selecionadas
	 * @param in  registro de base
	 * @param g   colunas a serem copiadas
	 */
	public static <T extends Number> void select(SrT<T> out, SrT<T> in, int... g) {
		for (int i = 0; i < g.length; i++)
			if (g[i] < 0 || g[i] >= in.length())
				throw new IllegalArgumentException("O registro não possui o número de medições indicado.");

		for (Entry<T, float[]> tv : in.entrySet()) {
			// ler todos os horários
			float[] r = tv.getValue();
			for (int i = 0; i < g.length; i++)
				out.put(tv.getKey(), i, r[g[i]]);
		}

		// labels
		for (int i = 0; i < g.length; i++)
			out.setLabel(i, in.getLabel(g[i]));
	}

	/**
	 * Função que justapõe um grupo de medições, formando um registro único com
	 * todas as colunas
	 * 
	 * @param <T> classe do número que representa o instante de tempo
	 * @param out medições onde serão inseridos os registros
	 * @param in  registros inseridos
	 */
	@SafeVarargs
	public static <T extends Number> void merge(SrT<T> out, SrT<T>... in) {
		// verificar número de registros dados como argumentos da função
		if (in.length == 0)
			return;

		// alargar o vetor de saída
		int size = out.length();
		int k = size;
		for (int i = 0; i < in.length; i++)
			size += in[i].length();
		out.setRegs(size);

		for (int i = 0; i < in.length; i++) {
			// para cada registro

			// atualiza o index
			for (int j = 0; j < in[i].length(); j++)
				out.setLabel(k + j, in[i].getLabel(j));

			for (Entry<T, float[]> ic : in[i].entrySet()) {
				// ler todos os horários

				float[] f = ic.getValue();
				for (int j = 0; j < f.length; j++)
					out.put(ic.getKey(), k + j, f[j]);
			}
			k += in[i].length();
		}
	}

	/**
	 * Função que unifica uma ou mais colunas com um valor que não seja em branco
	 * 
	 * @param <T> classe do número que representa o instante de tempo
	 * @param out registro com as colunas selecionadas
	 * @param t   coluna de destino
	 * @param pos colunas a serem copiadas
	 */
	public static <T extends Number> void merge(SrT<T> out, int t, int... pos) {
		if (pos.length == 0)
			pos = ArrayUtils.progVec(true, out.length());

		for (float[] vs : out.values()) {
			// ler todos os horários

			float v = Float.NaN;
			for (int i = 0; i < pos.length; i++) {
				int p = pos[i];
				if (!Float.isNaN(vs[p])) {
					v = vs[p];
					break;
				}
			}
			vs[t] = v;
		}
	}

	private static final char SUM = '+', SUMMOD = 'P', MINUS = '-', PRODUCT = '*', DIV = '/', POWER = '^', MEDIA = 'm',
			ATAN2 = '2', HIPOT = 'h', MAX = 'M', MIN = 'n', ABS = 'a', SQRT = 'r', SIN = 's', COS = 'c', TAN = 't',
			ASIN = 'S', ACOS = 'C', ATAN = 'T', LG10 = 'l', LN = 'N', EXP = 'e';

	// registro [+-*:^] número -> mesmo registro

	/**
	 * registro [+-*:^] número -> mesmo registro
	 * 
	 * @param <T>  classe do número que representa o instante de tempo
	 * @param op   caractere indicando a operação, podendo ser:
	 *             <ul>
	 *             <li>{@link #SUM soma};</i>
	 *             <li>{@link #MINUS substração};</i>
	 *             <li>{@link #PRODUCT multiplição};</i>
	 *             <li>{@link #DIV divisão};</i>
	 *             <li>{@link #POWER potenciação}.</i>
	 *             </ul>
	 * @param arg2 argumento a ser operado com os registros
	 * @param arg1 registro de medições
	 * @param pos  vetor com as posições no registro onde estão os elementos a serem
	 *             operados (se estiver vazio, todos os elementos da série serão
	 *             operados)
	 */
	public static <T extends Number> void operation(char op, float arg2, SrT<T> arg1, int... pos) {
		for (float[] v : arg1.values()) {
			switch (op) {
			case SUM:
				if (pos.length != 0)
					for (int p : pos)
						v[p] += arg2;
				else
					for (int i = 0; i < v.length; i++)
						v[i] += arg2;
				break;
			case MINUS:
				if (pos.length != 0)
					for (int p : pos)
						v[p] -= arg2;
				else
					for (int i = 0; i < v.length; i++)
						v[i] -= arg2;
				break;
			case PRODUCT:
				if (pos.length != 0)
					for (int p : pos)
						v[p] *= arg2;
				else
					for (int i = 0; i < v.length; i++)
						v[i] *= arg2;
				break;
			case DIV:
				if (pos.length != 0)
					for (int p : pos)
						v[p] /= arg2;
				else
					for (int i = 0; i < v.length; i++)
						v[i] /= arg2;
				break;
			case POWER:
				if (pos.length != 0)
					for (int p : pos)
						v[p] = (float) Math.pow(v[p], arg2);
				else
					for (int i = 0; i < v.length; i++)
						v[i] = (float) Math.pow(v[i], arg2);
				break;
			}
		}

		// label
		for (int p : pos) {
			String s = arg1.getLabel(p);
			s = String.format("(%s)%c%g", s, op, arg2);
			arg1.setLabel(p, s);
		}
	}

	// registro [+-*:^] número - novo registro

	/**
	 * registro [+-*:^] número - novo registro
	 * 
	 * @param <T>     classe do número que representa o instante de tempo
	 * @param tempOut
	 * @param arg1
	 * @param pos
	 * @param op      caractere indicando a operação, podendo ser:
	 *                <ul>
	 *                <li>{@link #SUM soma};</i>
	 *                <li>{@link #MINUS substração};</i>
	 *                <li>{@link #PRODUCT multiplição};</i>
	 *                <li>{@link #DIV divisão};</i>
	 *                <li>{@link #POWER potenciação}.</i>
	 *                </ul>
	 * @param arg2
	 */
	public static <T extends Number> void operation(SrT<T> tempOut, SrT<T> arg1, int pos, char op, float arg2) {
		operation(tempOut, arg1, pos, op, arg2, null, null);
	}

	/**
	 * registro [+-*:^] número - novo registro
	 * 
	 * @param <T>     classe do número que representa o instante de tempo
	 * @param tempOut registro onde será alocada o resultado da operação (na coluna
	 *                0)
	 * @param arg1    registro de medições
	 * @param pos     posição no registro onde estão os elementos a serem operados
	 * @param op      caractere indicando a operação, podendo ser:
	 *                <ul>
	 *                <li>{@link #SUM soma};</i>
	 *                <li>{@link #MINUS substração};</i>
	 *                <li>{@link #PRODUCT multiplição};</i>
	 *                <li>{@link #DIV divisão};</i>
	 *                <li>{@link #POWER potenciação}.</i>
	 *                </ul>
	 * @param arg2    argumento a ser operado com os registros
	 * @param begin   número inteiro que representa o tempo inicial a partir do qual
	 *                serão feitas as operações
	 * @param end     número inteiro que representa o tempo final até onde serão
	 *                feitas as operações
	 */
	public static <T extends Number> void operation(SrT<T> tempOut, SrT<T> arg1, int pos, char op, float arg2, T begin,
			T end) {
		// verificar número de registros dados como argumentos da função
		if (pos >= arg1.length())
			throw new IllegalArgumentException("Não há o número de registro indicados");

		// providencia espaço no vetor de resposta
		tempOut.setRegs(1);

		for (Entry<T, float[]> tv : arg1.entrySet()) {
			T t = tv.getKey();
			if ((begin == null ? true : t.doubleValue() >= begin.doubleValue())
					&& (end == null ? true : t.doubleValue() < end.doubleValue())) {
				// ler todos os horários dentro do período

				float o1 = tv.getValue()[pos];
				if (Float.isNaN(o1))
					tempOut.put(t, 0, o1);

				switch (op) {
				case SUM:
					tempOut.put(t, 0, o1 + arg2);
					break;
				case MINUS:
					tempOut.put(t, 0, o1 - arg2);
					break;
				case PRODUCT:
					tempOut.put(t, 0, o1 * arg2);
					break;
				case DIV:
					tempOut.put(t, 0, o1 / arg2);
					break;
				case POWER:
					tempOut.put(t, 0, (float) Math.pow(o1, arg2));
					break;
				}
			}
		}

		if (begin == null && end == null) {
			// TODO label?
		}
	}

	// este registro [+-*:mM2hn] este registro -> este registro

	/**
	 * este registro [+-*:mM2hn] este registro -> este registro
	 *
	 * @param op      caractere indicando a operação, podendo ser:
	 *                <ul>
	 *                <li>{@link #SUM soma};</i>
	 *                <li>{@link #MINUS substração};</i>
	 *                <li>{@link #PRODUCT multiplição};</i>
	 *                <li>{@link #DIV divisão};</i>
	 *                <li>{@link #MEDIA média};</i>
	 *                <li>{@link #ATAN2 arcotangente2(y,x)};</i>
	 *                <li>{@link #HIPOT sqrt(x^2,y^2)};</i>
	 *                <li>{@link #MAX max(x;y)};</i>
	 *                <li>{@link #MIN min(x;y)}.</i>
	 *                </ul>
	 * @param and     se <code>true</code>, todos os vetores devem ter um dado
	 *                registro horário para que a operação seja feita;
	 *                <code>false</code> para substituir os buracos nos dados pelo
	 *                elemento neutro da operação
	 * @param posDest posição onde as respostas serão alocadas
	 * @param posArg  posições dos operandos dentro deste registro
	 */
	public void operation(char op, boolean and, int posDest, int... posArg) {
		SrT<T> temp = new SrT<>(0);

		Dupla[] ds = new Dupla[posArg.length];
		for (int i = 0; i < posArg.length; i++)
			ds[i] = new Dupla(0, posArg[i]);

		operation(temp, op, and, ds, this);
		transfer(temp, 0, this, posDest);
	}

	// este registro [+-*:mM2hn] outro registro -> este registro

	/**
	 * este registro [+-*:mM2hn] outro registro -> este registro
	 * 
	 * @param op      caractere indicando a operação, podendo ser:
	 *                <ul>
	 *                <li>{@link #SUM soma};</i>
	 *                <li>{@link #SUMMOD soma dos módulos};</i>
	 *                <li>{@link #MINUS substração};</i>
	 *                <li>{@link #PRODUCT multiplição};</i>
	 *                <li>{@link #DIVISION divisão};</i>
	 *                <li>{@link #MEDIA média};</i>
	 *                <li>{@link #ATAN2 arcotangente2(y,x)};</i>
	 *                <li>{@link #HIPOT sqrt(x^2,y^2)};</i>
	 *                <li>{@link #MAX max(x;y)};</i>
	 *                <li>{@link #MIN min(x;y)}.</i>
	 *                </ul>
	 * @param posThis posição de onde vão partir os argumento deste registro e
	 *                também onde as respostas serão alocadas
	 * @param and     se <code>true</code>, todos os vetores devem ter um dado
	 *                registro horário para que a operação seja feita;
	 *                <code>false</code> para substituir os buracos nos dados pelo
	 *                elemento neutro da operação
	 * @param arg     registro que será operado
	 * @param posArg  posições dos operandos dentro do registro
	 */
	public void operation(char op, int posThis, boolean and, SrT<T> arg, int... posArg) {
		SrT<T> temp = new SrT<>(0);

		Dupla[] ds = new Dupla[1 + posArg.length];
		ds[0] = new Dupla(0, posThis);
		for (int i = 0; i < posArg.length; i++)
			ds[i + 1] = new Dupla(1, posArg[i]);

		operation(temp, op, and, ds, this, arg);
		transfer(temp, 0, this, posThis);
	}

	// este registro [+-*:mM2hn] outros registros -> este registro

	/**
	 * este registro [+-*:mM2hn] outros registros -> este registro
	 * 
	 * @param op      caractere indicando a operação, podendo ser:
	 *                <ul>
	 *                <li>{@link #SUM soma};</i>
	 *                <li>{@link #SUMMOD soma dos módulos};</i>
	 *                <li>{@link #MINUS substração};</i>
	 *                <li>{@link #PRODUCT multiplição};</i>
	 *                <li>{@link #DIVISION divisão};</i>
	 *                <li>{@link #MEDIA média};</i>
	 *                <li>{@link #ATAN2 arcotangente2(y,x)};</i>
	 *                <li>{@link #HIPOT sqrt(x^2,y^2)};</i>
	 *                <li>{@link #MAX max(x;y)};</i>
	 *                <li>{@link #MIN min(x;y)}.</i>
	 *                </ul>
	 * @param posThis posição onde as respostas serão alocadas neste registro
	 * @param and     se <code>true</code>, todos os vetores devem ter um dado
	 *                registro horário para que a operação seja feita;
	 *                <code>false</code> para substituir os buracos nos dados pelo
	 *                elemento neutro da operação
	 * @param posArgs posições dos operandos dentro dos registros. Por exemplo, se
	 *                um dos elementos do vetor for a dupla (2, 1), um do operados
	 *                será a primeira coluna do registro que está na posição 2 do
	 *                argumento <code>args</code>, que é um vetor de tamanho
	 *                variável.
	 * @param arg     vetor de tamanho variável contendo os registros que serão
	 *                operados
	 */
	@SuppressWarnings("unchecked")
	public void operation(char op, int posThis, boolean and, Dupla[] posArgs, SrT<T>... arg) {
		SrT<T> temp = new SrT<>(0);
		operation(temp, op, and, posArgs, arg);
		transfer(temp, 0, this, posThis);
	}

	// registro [+-*:m] registro [+-*:m] registro ... -> novo registro

	/**
	 * registro [+-*:m] registro [+-*:m] registro ... -> novo registro
	 * 
	 * @param <T>     classe do número que representa o instante de tempo
	 * @param tempOut registro onde será alocada o resultado da operação (na coluna
	 *                0)
	 * @param op      caractere indicando a operação, podendo ser:
	 *                <ul>
	 *                <li>{@link #SUM soma};</i>
	 *                <li>{@link #MINUS substração};</i>
	 *                <li>{@link #PRODUCT multiplição};</i>
	 *                <li>{@link #DIV divisão};</i>
	 *                <li>{@link #MEDIA média};</i>
	 *                <li>{@link #ATAN2 arcotangente2(y,x)};</i>
	 *                <li>{@link #HIPOT sqrt(x^2,y^2)};</i>
	 *                <li>{@link #MAX max(x;y)};</i>
	 *                <li>{@link #MIN min(x;y)}.</i>
	 *                </ul>
	 * @param and     se <code>true</code>, todos os vetores devem ter um dado
	 *                registro horário para que a operação seja feita;
	 *                <code>false</code> para substituir os buracos nos dados pelo
	 *                elemento neutro da operação
	 * @param arg     registro que será operado
	 * @param posArgs posições dos operandos dentro do registro
	 */
	public static <T extends Number> void operation(SrT<T> tempOut, char op, boolean and, SrT<T> arg, int... posArgs) {
		Dupla[] ds = new Dupla[posArgs.length];
		for (int i = 0; i < posArgs.length; i++)
			ds[i] = new Dupla(0, posArgs[i]);
		operation(tempOut, op, and, ds, arg);
	}

	/**
	 * registro [+-*:m] registro [+-*:m] registro ... -> novo registro
	 * 
	 * @param <T>     classe do número que representa o instante de tempo
	 * @param tempOut registro onde será alocada o resultado da operação (na coluna
	 *                0)
	 * @param op      caractere indicando a operação, podendo ser:
	 *                <ul>
	 *                <li>{@link #SUM soma};</i>
	 *                <li>{@link #SUMMOD soma dos módulos};</i>
	 *                <li>{@link #MINUS substração};</i>
	 *                <li>{@link #PRODUCT multiplição};</i>
	 *                <li>{@link #DIVISION divisão};</i>
	 *                <li>{@link #MEDIA média};</i>
	 *                <li>{@link #ATAN2 arcotangente2(y,x)};</i>
	 *                <li>{@link #HIPOT sqrt(x^2,y^2)};</i>
	 *                <li>{@link #MAX max(x;y)};</i>
	 *                <li>{@link #MIN min(x;y)}.</i>
	 *                </ul>
	 * @param and     se <code>true</code>, todos os vetores devem ter um dado
	 *                registro horário para que a operação seja feita;
	 *                <code>false</code> para substituir os buracos nos dados pelo
	 *                elemento neutro da operação
	 * @param posArgs posições dos operandos dentro dos registros. Por exemplo, se
	 *                um dos elementos do vetor for a dupla (2, 1), um do operados
	 *                será a primeira coluna do registro que está na posição 2 do
	 *                argumento <code>args</code>, que é um vetor de tamanho
	 *                variável.
	 * @param args    vetor de tamanho variável contendo os registros que serão
	 *                operados
	 */
	@SafeVarargs
	public static <T extends Number> void operation(SrT<T> tempOut, char op, boolean and, Dupla[] posArgs,
			SrT<T>... args) {
		// verificar número de registros dados como argumentos da função
		if (args.length == 0 || posArgs.length == 0)
			return;

		for (int i = 0; i < posArgs.length; i++) {
			if (posArgs[i].get1() >= args.length)
				throw new IllegalArgumentException("Não há o número de registro indicados");
			if (posArgs[i].get2() >= args[posArgs[i].get1()].length())
				throw new IllegalArgumentException("Um dos registros não possui o número de medições indicado");
		}

		if ((op == ATAN2 || op == HIPOT) && posArgs.length != 2)
			throw new IllegalArgumentException("Esta operação só pode ser feita com dois argumentos");

		// primeiro argumento forma a base
		SrT<T> arg = args[posArgs[0].get1()];
		if (op == MEDIA && !and) {
			// caso esteja se fazendo a operação de média e caso não se exclua
			// casos vazias, É NECESSÁRIA uma segunda coluna que contabilize
			// quantas células não vazias há.

			// providencia espaço no vetor de resposta
			tempOut.setRegs(2);

			for (Entry<T, float[]> tv : arg.entrySet()) {
				// ler para todos os horários
				float a1 = tv.getValue()[posArgs[0].get2()];
				if (!Float.isNaN(a1)) {
					tempOut.put(tv.getKey(), 0, a1);
					tempOut.put(tv.getKey(), 1, 1f);
				}
			}
		} else {
			// providencia espaço no vetor de resposta
			tempOut.setRegs(1);

			for (Entry<T, float[]> ic : arg.entrySet()) {
				// ler para todos os horários
				float a1 = ic.getValue()[posArgs[0].get2()];
				if (!Float.isNaN(a1))
					tempOut.put(ic.getKey(), 0, a1);
			}
		}

		for (int i = 1; i < posArgs.length; i++) {
			// para todos os outros argumentos da operação
			arg = args[posArgs[i].get1()];

			if (and) {
				// se todos os operandos devem ter um valor válido para um dado
				// horário, itera-se sempre sobre a base do primeiro argumento

				Iterator<Map.Entry<T, float[]>> it = tempOut.entrySet().iterator();

				while (it.hasNext()) {
					Entry<T, float[]> tv = it.next();
					T t = tv.getKey();

					// o próximo argumento deve ter o tempo da base,
					// senão tanto o registro da base quanto deste
					// argumento são ignorados (condição AND)
					float o2 = Float.NaN;
					float[] vs = arg.get(t);
					if (vs != null)
						o2 = vs[posArgs[i].get2()];

					if (!Float.isNaN(o2)) {
						float[] v = tv.getValue();
						// efetua uma das operações
						switch (op) {
						case SUM:
							v[0] += o2;
							break;
						case SUMMOD:
							v[0] += (v[0] < 0 ? -Math.abs(o2) : Math.abs(o2));
							break;
						case MINUS:
							v[0] -= o2;
							break;
						case PRODUCT:
							v[0] *= o2;
							break;
						case DIV:
							v[0] /= o2;
							break;
						case MEDIA:
							v[0] = (i * v[0] + o2) / (i + 1);
							break;
						case ATAN2:
							v[0] = (float) Math.atan2(o2, v[0]);
							break;
						case HIPOT:
							v[0] = (float) Math.hypot(v[0], o2);
							break;
						case MAX:
							v[0] = (float) Math.max(v[0], o2);
							break;
						case MIN:
							v[0] = (float) Math.min(v[0], o2);
							break;
						}
					} else
						it.remove();
				}
			} else {
				// se qualquer um dos operandos pode ter o valor para um dado horário, itera-se
				// cada vez sobre um argumento

				for (Entry<T, float[]> tv : arg.entrySet()) {
					// primeiro operando (o que está chegando)
					float o2 = tv.getValue()[posArgs[i].get2()];
					boolean nan = Float.isNaN(o2);
					if (nan)
						o2 = neutro(op);

					T t = tv.getKey();

					// segundo operando (que já está na lista principal)
					float[] v = tempOut.get(t);
					if (v == null) {
						// se for o primeiro operando que entra na lista (pois o primeiro estava vazio)
						// põe o elemento neutro...

						if (nan)// ...a não ser que este também estava vazio
							continue;

						if (op != MEDIA)
							tempOut.put(t, v = new float[] { neutro(op) });
						else // na média, elemento neutro não conta (logo a coluna '1', a do contador, fica
								// zerada)
							tempOut.put(t, v = new float[] { 0f, 0f });
					}

					// efetua uma das operações
					switch (op) {
					case MEDIA:
						if (!nan)// na média, só elemento não neutro conta
							tempOut.put(t, 1, v[1] + 1f);
					case SUM:
						v[0] += o2;
						break;
					case SUMMOD:
						v[0] += (v[0] < 0 ? -Math.abs(o2) : Math.abs(o2));
						break;
					case MINUS:
						v[0] -= o2;
						break;
					case PRODUCT:
						v[0] *= o2;
						break;
					case DIV:
						v[0] /= o2;
						break;
					case ATAN2:
						v[0] = (float) Math.atan2(o2, v[0]);
						break;
					case HIPOT:
						v[0] = (float) Math.hypot(v[0], o2);
						break;
					case MAX:
						v[0] = (float) Math.max(v[0], o2);
						break;
					case MIN:
						v[0] = (float) Math.min(v[0], o2);
						break;
					}
				}
			}
		}

		if (op == MEDIA && !and) {
			// caso esteja se fazendo a operação de média e caso não se exclua
			// casos vazias, efetua-se a divisão e elimina-se a coluna com o
			// número de células não vazias
			for (float[] vs : tempOut.values())
				vs[0] /= vs[1];
			tempOut.setRegs(1);
		}

		// labels
		String ind = "";
		for (int i = 0; i < posArgs.length; i++) {
			Dupla d = posArgs[i];
			SrT<T> r = args[d.get1()];
			String s = r.getLabel(d.get2());
			if (s == null)
				s = "";
			ind += s + op;
		}
		tempOut.setLabel(0, ind.substring(0, ind.length() - 1));
	}

	// este registro [sqrt sin cos tan...] -> este registro

	/**
	 * este registro [sqrt sin cos tan...] -> este registro na mesma posição
	 * 
	 * @param op  as mesmas operações desta
	 *            {@link SrT#operation(SrT, char, SrT, int) função}
	 * @param pos posições no registro onde estão os elementos a serem operados
	 */
	public void operation(char op, int pos) {
		operation(pos, op, pos);
	}

	/**
	 * este registro [sqrt sin cos tan...] -> este registro em outra posição
	 * 
	 * @param posDest posição onde as respostas serão alocadas
	 * @param op      as mesmas operações desta
	 *                {@link SrT#operation(SrT, char, SrT, int) função}
	 * @param posSrc  posições no registro onde estão os elementos a serem operados
	 */
	public void operation(int posDest, char op, int posSrc) {
		SrT<T> temp = new SrT<>(1);
		operation(temp, op, this, posSrc);
		transfer(temp, 0, this, posDest);
	}

	// registro [sqrt sin cos tan...] -> novo registro

	/**
	 * registro [sqrt sin cos tan...] -> novo registro
	 * 
	 * @param <T>     classe do número que representa o instante de tempo
	 * @param tempOut registro onde será alocada o resultado da operação (na coluna
	 *                0)
	 * @param op      caractere indicando a operação, podendo ser:
	 *                <ul>
	 *                <li>{@link #ABS módulo};</i>
	 *                <li>{@link #SQRT raiz} ;</i>
	 *                <li>{@link #SIN seno} ;</i>
	 *                <li>{@link #COS cosseno};</i>
	 *                <li>{@link #TAN tangente};</i>
	 *                <li>{@link #ASIN arcosseno};</i>
	 *                <li>{@link #ACOS arcocosseno};</i>
	 *                <li>{@link #ATAN arcotangente};</i>
	 *                <li>{@link #LG10 logaritmo na base 10};</i>
	 *                <li>{@link #LN logaritmo natural};</i>
	 *                <li>{@link #EXP exponenciação na base e}.</i>
	 *                </ul>
	 * @param arg     registro de medições
	 * @param pos     posições no registro onde estão os elementos a serem operados
	 */
	public static <T extends Number> void operation(SrT<T> tempOut, char op, SrT<T> arg, int pos) {
		// verificar número de registros dados como argumentos da função
		if (pos >= arg.length())
			throw new IllegalArgumentException("Não há o número de registro indicados");

		// providencia espaço no vetor de resposta
		tempOut.setRegs(1);

		for (Entry<T, float[]> tv : arg.entrySet()) {
			// ler todos os horários

			T t = tv.getKey();

			float o1 = tv.getValue()[pos];
			if (Float.isNaN(o1))
				tempOut.put(t, 0, o1);
			else {
				switch (op) {
				case ABS:
					tempOut.put(t, 0, Math.abs(o1));
					break;
				case SQRT:
					tempOut.put(t, 0, (float) Math.sqrt(o1));
					break;
				case SIN:
					tempOut.put(t, 0, (float) Math.sin(o1));
					break;
				case COS:
					tempOut.put(t, 0, (float) Math.cos(o1));
					break;
				case TAN:
					tempOut.put(t, 0, (float) Math.tan(o1));
					break;
				case ASIN:
					tempOut.put(t, 0, (float) Math.asin(o1));
					break;
				case ACOS:
					tempOut.put(t, 0, (float) Math.acos(o1));
					break;
				case ATAN:
					tempOut.put(t, 0, (float) Math.atan(o1));
					break;
				case LG10:
					tempOut.put(t, 0, (float) Math.log10(o1));
					break;
				case LN:
					tempOut.put(t, 0, (float) Math.log(o1));
					break;
				case EXP:
					tempOut.put(t, 0, (float) Math.exp(o1));
					break;
				}
			}
		}

		// TODO labels?
	}

	/**
	 * Função que retorna o elemento neutro de uma dado operação
	 * 
	 * @param op caractere indicando a operação
	 * @return elemento neutro
	 */
	private static float neutro(char op) {
		switch (op) {
		case SUM:
		case SUMMOD:
		case MEDIA:
		case HIPOT:
			return 0f;
		case PRODUCT:
			return 1f;
		case MAX:
			return Float.MIN_VALUE;
		case MIN:
			return Float.MAX_VALUE;
		default:
			return Float.NaN;
		}
	}

	/**
	 * Função que efetua uma operação com os registros contidos nesse objeto, sendo
	 * o resultado posto na última posição do vetor de valores.
	 * 
	 * @param exp expressão regular cujas variáveis a serem carregadas são, na
	 *            ordem, os registros contidos no objeto
	 */
	public void operation(Expressao exp) {
		Iterator<Map.Entry<T, float[]>> it = this.entrySet().iterator();

		while (it.hasNext()) {
			float[] fs = it.next().getValue();
			double[] args = new double[exp.getValuesCount()];
			for (int i = 0; i < args.length; i++) {
				if (Float.isNaN(fs[i]))
					args[i] = 0f;
				else
					args[i] = fs[i];
			}
			fs[fs.length - 1] = (float) exp.f(args);
		}
	}

	/**
	 * Função que efetua uma operação com os últimos registros válidos de cada
	 * posição, sendo o resultado posto na última posição do vetor de valores do
	 * último registro.
	 * 
	 * @param exp expressão regular cujas variáveis a serem carregadas são, na
	 *            ordem, os registros contidos no objeto
	 */
	public void operationIntoFirst(Expressao exp) {
		operation(exp, false);
	}

	/**
	 * Função que efetua uma operação com os primeiros registros válidos de cada
	 * posição, sendo o resultado posto na última posição do vetor de valores do
	 * primeiro registro.
	 * 
	 * @param exp expressão regular cujas variáveis a serem carregadas são, na
	 *            ordem, os registros contidos no objeto
	 */
	public void operationIntoLast(Expressao exp) {
		operation(exp, true);
	}

	private void operation(Expressao exp, boolean order) {
		if (this.size() == 1)
			operation(exp);

		float[] dest = order ? lastEntry().getValue() : firstEntry().getValue();
		// montar o vetor com os argumentos (não serão somente elementos de
		// dest, mas também de outros vetores)
		double[] args = new double[exp.getValuesCount()];
		for (int pos = 0; pos < args.length; pos++) {
			if (!Float.isNaN(dest[pos])) {
				// se na última posição há uma medição válida...
				args[pos] = dest[pos];
				continue;
			}

			// se na última posição tem um NaN, vai até a próxima válida
			NavigableMap<T, float[]> dm = order ? descendingMap() : this;
			for (float[] vs : dm.values()) {
				if (!Float.isNaN(vs[pos])) {
					args[pos] = vs[pos];
					break;
				}
			}
		}
		dest[dest.length - 1] = (float) exp.f(args);
	}

	// -------------------------------- ANÁLISE --------------------------------

	public boolean hasData(int pos) {
		return !isEmpty(pos) && !isNull(pos);
	}

	public boolean hasData(Collection<Integer> pos) {
		return !isEmpty(pos) && !isNull(pos);
	}

	public boolean hasHole(int pos) {
		for (float[] v : this.values())
			if (Float.isNaN(v[pos]))
				return true;
		return false;
	}

	public boolean hasHole(Collection<Integer> pos) {
		for (Integer p : pos)
			if (hasHole(p))
				return true;
		return false;
	}

	/**
	 * Função que verifica se há alguma medição válida para uma dada posição do
	 * registro
	 * 
	 * @param pos posição do registro no objeto
	 * @return <code>true</code> se pelo menos um registro é {@link Float#isNaN
	 *         válido}
	 */
	public boolean isEmpty(int pos) {
		for (float[] v : this.values())
			if (!Float.isNaN(v[pos]))
				return false;
		return true;
	}

	/**
	 * Função que analisa se nas posições indicadas há valores válidos de medição
	 * 
	 * @param pos posições a serem inspecionadas
	 * @return <code>true</code> se há pelo menos uma medição válida,
	 *         <code>false</code> senão
	 */
	public boolean isEmpty(Collection<Integer> pos) {
		for (Integer p : pos)
			if (!isEmpty(p))
				return false;
		return true;
	}

	/**
	 * Função que analisa se na posição indicada só há valores nulos ou não
	 * 
	 * @param pos posição a ser inspecionada
	 * @return <code>true</code> se todos os valores forem nulos, <code>false</code>
	 *         senão
	 */
	public boolean isNull(int pos) {
		for (float[] v : this.values())
			if (v[pos] != 0f)
				return false;
		return true;
	}

	public boolean isNull(Collection<Integer> pos) {
		for (Integer p : pos)
			if (!isNull(p))
				return false;
		return true;
	}

	/**
	 * 
	 * @param pos
	 * @param discret <code>true</code> para medições contínuas, <code>false</code>
	 * @return
	 */
	public boolean isInvalid(int pos, boolean discret) {
		if (this.size() == 0)
			return true;
		Iterator<float[]> it = this.values().iterator();
		float[] v = it.next();
		float v0 = v[pos];
		if (Float.isNaN(v0)) // zerado ou NaN é info inválida
			v0 = 0f;
		boolean noCnst = discret, data = v0 != 0f;
		if (this.size() == 1)
			return !data;
		while (it.hasNext()) {
			v = it.next();
			float v1 = v[pos];
			if (Float.isNaN(v1)) // zerado ou NaN é info inválida
				v1 = 0f;
			boolean nn = v1 != 0f;
			data |= nn;
			if (nn)
				noCnst |= v1 != v0;

			if (data && noCnst) // se tem alguma info que não seja repetida
				return false;
		}
		return true;
	}

	/**
	 * Função que indica que uma dada posição do registro é composto somente por
	 * números não positivos
	 * 
	 * @param pos posição do registro
	 * @return <code>true</code> se todos os valores forem nulos ou negativos,
	 *         <code>false</code> senão
	 */
	public boolean isNegative(int pos) {
		if (pos < 0)
			throw new IllegalArgumentException("Posição inválida");
		for (float[] v : this.values())
			if (Float.isNaN(v[pos]) ? false : v[pos] > 0f)
				return false;
		return true;
	}

	// ---------------------------- ESTATÍSTICO ----------------------------

	/**
	 * Função que calcula o valor médio de um dado conjunto de registros
	 * 
	 * @param reg registro de medições
	 * @param pos posição no mapa de registros de onde serão tomados os dados para
	 *            cálculo da média
	 * @return valor médio
	 */
	public static float getMed(SrT<?> reg, int pos) {
		// corrrigir desvios da entrada
		if (pos >= reg.length())
			pos = reg.length() - 1;

		// soma parcial e contador
		float m = 0;
		int count = 0;

		for (float[] vs : reg.values()) {
			if (!Float.isNaN(vs[pos])) {
				m += vs[pos];
				count++;
			}
		}
		return m / count;
	}

	/**
	 * Função que calcula o valor médio dos módulos de um dado conjunto de registros
	 * 
	 * @param reg registro de medições
	 * @param pos posição no mapa de registros de onde serão tomados os dados para
	 *            cálculo da média
	 * @return valor médio dos módulos
	 */
	public static float getMedM(SrT<?> reg, int pos) {
		// corrrigir desvios da entrada
		if (pos >= reg.length())
			pos = reg.length() - 1;

		// soma parcial e contador
		float m = 0;
		int count = 0;

		for (float[] vs : reg.values()) {
			if (!Float.isNaN(vs[pos])) {
				m += Math.abs(vs[pos]);
				count++;
			}
		}
		return m / count;
	}

	/**
	 * 
	 * @param pos
	 * @return
	 */
	public float getR2(int... pos) {
		if (pos.length == 0)
			pos = ArrayUtils.progVec(true, this.length());

		int count = 0;
		float d = 0;
		for (float[] vs : this.values()) {

			float med = 0f;
			int div = 0;
			// média
			for (int i = 0; i < pos.length; i++) {
				if (!Float.isNaN(vs[pos[i]])) {
					med += vs[pos[i]];
					div++;
				}
			}
			if (div > 1) {
				med /= div;

				// distância
				float dist = 0f;
				for (int i = 0; i < pos.length; i++)
					if (!Float.isNaN(vs[pos[i]]))
						dist += Math.pow(vs[pos[i]] - med, 2);

				double medDist = Math.sqrt(dist) / div;
				if (med != 0f)
					medDist /= med;
				d += Math.abs(medDist);
				count++;
			}
		}
		return d / count;
	}
}