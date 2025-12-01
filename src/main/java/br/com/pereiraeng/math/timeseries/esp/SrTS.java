package br.com.pereiraeng.math.timeseries.esp;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.Map.Entry;

import br.com.pereiraeng.math.timeseries.TpD;

/**
 * Classe que representa uma <strong>s</strong>é<strong>r</strong>ie
 * <strong>t</strong>emporal de uma ou mais medições e/ou dados, onde tais dados
 * são guardados em um vetor de bytes, segundo um padrão das {@link MedDataType
 * primitivas} que o compõe. Cada {@link #getLabel(int) etiqueta} pode guardar
 * {@link #m uma ou mais} medições segundo o mesmo {@link #dataTypes padrão de
 * primitivas}.
 * 
 * @author Philipe PEREIRA
 *
 * @param <T> chave numérica ordenável que indexa no tempo as medições
 */
public class SrTS<T extends Number> extends TpD<T, byte[]> {
	private static final long serialVersionUID = 1L;

	/**
	 * Cabeçalho
	 */
	protected byte[] header;

	/**
	 * Número de medições no padrão {@link #dataTypes} que {@link #values() este
	 * objeto} comporta para cada {@link #getLabel(int) etiqueta}
	 */
	private final int m;

	/**
	 * Padrão das primitivas que compõe o vetor de binários
	 */
	protected final MedDataType[] dataTypes;

	/**
	 * Número de bytes para cada medição de uma dada {@link #getLabel(int) etiqueta}
	 * ({@link #getM()} &times; {@link MedDataType#getDataSize(MedDataType[])})
	 */
	private final int dataSize;

	private final byte[] nullData;

	/**
	 * 
	 * @param header    cabeçalho binário
	 * @param regs      número de m-medições por horário
	 * @param m         Número de medições no padrão {@link #dataTypes} que
	 *                  {@link #values() este objeto} comporta para cada
	 *                  {@link #getLabel(int) etiqueta}
	 * @param dataTypes Padrão das primitivas que compõe o vetor de binários
	 */
	public SrTS(byte[] header, int regs, int m, MedDataType... dataTypes) {
		this(header, new String[regs], m, dataTypes);
	}

	/**
	 * 
	 * @param header    cabeçalho binário
	 * @param labels    etiquetas das grandezas
	 * @param m         Número de medições no padrão {@link #dataTypes} que
	 *                  {@link #values() este objeto} comporta para cada
	 *                  {@link #getLabel(int) etiqueta}
	 * @param dataTypes Padrão das primitivas que compõe o vetor de binários
	 */
	public SrTS(byte[] header, String[] labels, int m, MedDataType... dataTypes) {
		super(labels);
		this.m = m;
		if (dataTypes.length == 0)
			throw new IllegalArgumentException("Não faz sentido um registro que armazene tipo nenhum.");
		this.dataTypes = dataTypes;
		this.header = header;
		this.dataSize = m * MedDataType.getDataSize(this.dataTypes);
		this.nullData = getNull();
	}

	@Override
	protected String toStringK(T key) {
		return key == null ? "" : key.toString();
	}

	@Override
	protected String toString(byte[] value) {
		StringBuilder sb = new StringBuilder();
		int p = 0;
		for (int i = 0; i < length(); i++) {
			for (int j = 0; j < m; j++) {
				for (int k = 0; k < dataTypes.length; k++) {
					sb.append("\t");
					switch (dataTypes[k]) {
					case DOUBLE:
						sb.append(ByteBuffer.wrap(value).getDouble(p));
						break;
					case FLOAT:
						sb.append(ByteBuffer.wrap(value).getFloat(p));
						break;
					case INT:
						sb.append(ByteBuffer.wrap(value).getInt(p));
						break;
					case BYTE:
						sb.append(value[p]);
						break;
					}
					p += dataTypes[k].size();
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Função que retorna o cabeçalho do registro
	 * 
	 * @return cabeçalho
	 */
	public byte[] getHeader() {
		return header;
	}

	/**
	 * Função que retorna o número de medições no padrão {@link #dataTypes} que
	 * {@link #values() este objeto} comporta para cada {@link #getLabel(int)
	 * etiqueta}
	 * 
	 * @return número de medições por etiqueta
	 */
	public int getM() {
		return m;
	}

	/**
	 * Função que retorna o número de bytes para cada medição de uma dada
	 * {@link #getLabel(int) etiqueta} ( {@link #getM()} &times;
	 * {@link MedDataType#getDataSize(MedDataType[])})
	 * 
	 * @return número de bytes por etiqueta para cada instante de tempo
	 */
	public int getDataSize() {
		return dataSize;
	}

	/**
	 * Função que retorna o elemento nulo deste conjunto de dados
	 * 
	 * @return vetor de byte indicando o elemento nulo (que designa uma posição
	 *         vazia)
	 */
	protected byte[] getNull() {
		if (this.nullData != null)
			return nullData;
		byte[] out = new byte[dataSize];
		writeNull(out, 0);
		return out;
	}

	/**
	 * Função que retorna o número de bytes utilizados para cada bloco de dados
	 * ({@link #length()} &times; {@link #getM()} &times;
	 * {@link MedDataType#getDataSize(MedDataType[])})
	 * 
	 * @return número de byte utilizados para guardar cada bloco de dados (é igual
	 *         ao número de posições do vetor retornado para cada instante de tempo)
	 */
	public int getArraySize() {
		return this.dataSize * super.length();
	}

	/**
	 * 
	 * @param pos posição do dado no bloco
	 * @param bs  vetor onde os dados serão escritos
	 */
	public void writeNull(int pos, byte[] bs) {
		writeNull(bs, pos * this.dataSize);
	}

	/**
	 * 
	 * @param bs  vetor onde os dados serão escritos
	 * @param pos posição no vetor
	 */
	private void writeNull(byte[] bs, int pos) {
		for (int i = 0; i < dataTypes.length; i++) {
			MedDataType dt = dataTypes[i];
			dt.writeNull(bs, pos);
			pos += dt.size();
		}
	}

	// -------------------------- NÚMERO DE REGISTROS --------------------------

	@Override
	public void setRegs(int regs) {
		int old = super.length();
		super.setRegs(regs);

		for (Entry<T, byte[]> tv : this.entrySet()) {
			// para cada horário, recria os vetores

			byte[] oldB = tv.getValue();
			byte[] newB = Arrays.copyOf(oldB, getArraySize());

			// completar o vetor caso o tamanho final seja maior que atual
			for (int i = old; i < regs; i++)
				writeNull(i, newB);

			// troca a chave
			this.put(tv.getKey(), newB);
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
	 * @param pos   posição do dado no bloco
	 * @param value bytes dos dados
	 * @return dados anteriores que estava na dada posição
	 */
	public byte[] put(T ci, int pos, byte[] value) {
		if (pos >= length())
			// se não tem espaço, alarga...
			insert(pos);
		if (value.length != dataSize)
			throw new IllegalArgumentException("Este bloco de dados não cabe neste registro.");

		byte[] oldB = super.get(ci);
		if (oldB != null) {
			byte[] old = Arrays.copyOfRange(oldB, pos * dataSize, (pos + 1) * dataSize);
			ByteBuffer.wrap(oldB, pos * dataSize, value.length).put(value);
			return old;
		} else {
			oldB = new byte[getArraySize()];
			for (int i = 0; i < length(); i++) {
				if (i == pos)
					ByteBuffer.wrap(oldB, pos * dataSize, value.length).put(value);
				else
					writeNull(i, oldB);
			}
			super.put(ci, oldB);
			return nullData;
		}
	}

	public byte[] put(T ci, int pos1, int pos2, byte[] value) {
		if (pos1 >= length())
			// se não tem espaço, alarga...
			insert(pos1);
		if (value.length != dataTypes[pos2].size())
			throw new IllegalArgumentException("Este bloco de dados não cabe neste registro.");

		int pos = pos1 * dataSize + MedDataType.getDataPos(dataTypes, pos2);

		byte[] oldB = super.get(ci);
		if (oldB != null) {
			byte[] old = Arrays.copyOfRange(oldB, pos, pos + dataTypes[pos2].size());
			ByteBuffer.wrap(oldB, pos, value.length).put(value);
			return old;
		} else {
			oldB = new byte[getArraySize()];
			for (int i = 0; i < length(); i++) {
				writeNull(i, oldB);
				if (i == pos1)
					ByteBuffer.wrap(oldB, pos, value.length).put(value);
			}
			super.put(ci, oldB);
			return nullData;
		}
	}

	/**
	 * Função que associa a um dado horário um vetor de dados
	 * 
	 * @param key    inteiro equivalente ao horário (ver
	 *               {@link TimeUtils#toInt(Calendar) conversão para inteiro})
	 * @param values vetor de bytes dos dados
	 * @return vetor de bytes dos dados que era associado a este horário
	 */
	@Override
	public byte[] put(T key, byte[] values) {
		if (values.length != getArraySize())
			throw new IllegalArgumentException("O tamanho do vetor de medições deve ser igual a " + length() + ".");
		return super.put(key, values);
	}

	// --------------------------- GETTER'S - VALUES ---------------------------

	/**
	 * Função que retorna um dado bloco de dados associados a um dado instante,
	 * estando alocado numa dada posição
	 * 
	 * @param t   número que indica o instante dos registro (chave da tabela)
	 * @param pos posição do dado no bloco
	 * @return bytes dos dados
	 */
	public byte[] get(T t, int pos) {
		byte[] ms = this.get(t);
		if (ms != null)
			return Arrays.copyOfRange(ms, pos * dataSize, (pos + 1) * dataSize);
		else
			return nullData;
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
	 * @param st  sequência de dados
	 * @param pos posições do(s) novo(s) registro(s) de medições
	 */
	public static <T extends Number> void insert(SrTS<T> st, int... pos) {
		// insere um conjunto de medições por vez, logo a função chama a si
		// própria com
		// uma coluna por vez
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
			// na posição
			// recém-inserida
			st.labels = Arrays.copyOf(st.labels, st.length() + 1);
			for (int i = st.length() - 1; i > p; i--)
				st.labels[i] = st.labels[i - 1];
			st.labels[p] = null;

			for (Entry<T, byte[]> tv : st.entrySet()) {
				// para cada horário, recria os vetores

				byte[] oldB = tv.getValue();
				byte[] newB = new byte[st.getArraySize()];

				// transferir dados de um vetor para outro, pulando a nova casa
				// que está sendo
				// inserida
				int k = 0, step = MedDataType.getDataSize(st.dataTypes), pi = p * step;
				for (int i = 0; i < oldB.length; i++) {
					if (i == pi)
						st.writeNull(newB, k += step);
					newB[k++] = oldB[i];
				}

				st.put(tv.getKey(), newB);
			}
		}
	}

	/**
	 * Função que retorna uma matriz com duas linhas, ordenada pelo horário, com a
	 * {@link TimeUtils#toInt(Calendar) chave inteira} do objeto Reg convertida em
	 * número decimal
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
	 * Função que retorna uma matriz com duas linhas, ordenada pelo horário, com a
	 * {@link TimeUtils#toInt(Calendar) chave inteira} do objeto Reg convertida em
	 * número decimal, podendo-se remover as medições não válidas (NaN).
	 * 
	 * @param pos posição no vetor de registros
	 * @param nan <code>true</code> se for para manter os registros com NaN,
	 *            <code>false</code> se for para removê-los
	 * @return par de vetores, formando uma matriz, estando na primeira posição os
	 *         inteiros representado os instantes de tempo (<strong>cada unidade
	 *         representa um minuto</strong>) e na segunda os valores registrados
	 */
	public double[][] getMatrix(int pos, boolean nan) {
		return getMatrix(this, pos, null, null, nan);
	}

	protected static <T extends Number> double[][] getMatrix(SrTS<T> st, int pos1, T beg, T end, boolean nan) {
		SortedMap<T, byte[]> map = null;
		if (beg == null && end == null)
			map = st;
		else
			map = st.subMap(beg, end);

		if (nan) {
			// se for para manter os NaN...
			double[][] out = null;
			if (pos1 >= 0 && pos1 < st.length()) {
				out = new double[1 + st.getM()][map.size()];
				int i = 0;
				for (Entry<T, byte[]> c : map.entrySet()) {
					out[0][i] = c.getKey().doubleValue();
					ByteBuffer bb = ByteBuffer.wrap(c.getValue());
					int p = pos1 * st.getDataSize();
					for (int pos2 = 0; pos2 < st.getM(); pos2++)
						out[pos2 + 1][i] = bb.getFloat(p + MedDataType.getDataPos(st.dataTypes, pos2));
					i++;
				}
			}
			return out;
		} else {
			// se for excluir os NaN...
			List<Double> xs = new LinkedList<>();
			List<double[]> yss = new LinkedList<>();
			if (pos1 >= 0 && pos1 < st.length()) {
				xl: for (Entry<T, byte[]> c : map.entrySet()) {
					double x0 = c.getKey().doubleValue();
					ByteBuffer bb = ByteBuffer.wrap(c.getValue());
					int p = pos1 * st.getDataSize();
					double[] vs = new double[st.getM()];
					for (int pos2 = 0; pos2 < st.getM(); pos2++) {
						float value = bb.getFloat(p + MedDataType.getDataPos(st.dataTypes, pos2));
						if (Float.isNaN(value))
							continue xl;
						else
							vs[pos2] = value;
					}
					xs.add(x0);
					yss.add(vs);
				}
			}

			// unbox
			double[][] out = new double[1 + st.getM()][map.size()];
			int i = 0;
			for (Double x : xs) {
				out[0][i] = x;
				double[] ys = yss.get(i);
				for (int j = 0; j < ys.length; j++)
					out[j + 1][i] = ys[j];
				i++;
			}
			return out;
		}
	}
}