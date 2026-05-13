package br.com.pereiraeng.math.timeseries.esp;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.pereiraeng.core.TimeUtils;
import br.com.pereiraeng.core.collections.ArrayUtils;

/**
 * Classe do objeto que representa uma série de medições, onde uma chave inteira
 * é associada a uma ou mais medições e/ou dados, sendo tais dados guardados na
 * forma de um vetor de bytes, segundo um padrão das {@link MedDataType
 * primitivas} que o compõe. Cada {@link #getLabel(int) etiqueta} pode guardar
 * {@link #m uma ou mais} medições segundo o mesmo {@link #dataTypes padrão de
 * primitivas}.
 * 
 * @author Philipe PEREIRA
 * @version 2019
 */
public class RegS extends SrTS<Integer> {
	private static final long serialVersionUID = -7039852565098282887L;

	/**
	 * 
	 * @param header cabeçalho binário
	 * @param regs   número de m-medições por horário
	 * @param m      Número de medições no padrão {@link #dataTypes} que
	 *               {@link #values() este objeto} comporta para cada
	 *               {@link #getLabel(int) etiqueta}
	 * @param dts    Padrão das primitivas que compõe o vetor de binários
	 */
	public RegS(byte[] header, int regs, int m, MedDataType... dts) {
		this(header, new String[regs], m, dts);
	}

	/**
	 * 
	 * @param header cabeçalho binário
	 * @param labels etiquetas das grandezas
	 * @param m      Número de medições no padrão {@link #dataTypes} que
	 *               {@link #values() este objeto} comporta para cada
	 *               {@link #getLabel(int) etiqueta}
	 * @param dts    Padrão das primitivas que compõe o vetor de binários
	 */
	public RegS(byte[] header, String[] labels, int m, MedDataType... dts) {
		super(header, labels, m, dts);
	}

	@Override
	public String toStringK(Integer key) {
		return String.format("%1$tH:%1$tM %1$td/%1$tm/%1$ty", TimeUtils.toCalendar(key));
	}

	// --------------------------- SETTER'S/PUTTER'S ---------------------------

	/**
	 * Função que retorna um mapa de medições ordenado pelo horário
	 * 
	 * @param pos posição no vetor de registros
	 * @return mapa ordenado as medições
	 */
	public TreeMap<Calendar, byte[]> getMap(int pos) {
		TreeMap<Calendar, byte[]> out = null;
		if (pos >= 0 && pos < length()) {
			out = new TreeMap<>();
			int sb = getDataSize();
			for (Entry<Integer, byte[]> e : this.entrySet()) {
				byte[] bs = e.getValue();
				bs = Arrays.copyOfRange(bs, pos * sb, (pos + 1) * sb);
				out.put(TimeUtils.toCalendar(e.getKey()), bs);
			}
		}
		return out;
	}

	public TreeMap<Calendar, float[]> getMapFloat(int pos) {
		TreeMap<Calendar, float[]> out = null;
		if (pos >= 0 && pos < length()) {
			out = new TreeMap<>();
			int sb = getDataSize();
			for (Entry<Integer, byte[]> e : this.entrySet()) {
				byte[] bs = e.getValue();
				bs = Arrays.copyOfRange(bs, pos * sb, (pos + 1) * sb);
				out.put(TimeUtils.toCalendar(e.getKey()), MedDataType.getFloats(bs, super.dataTypes));
			}
		}
		return out;
	}

	public TreeMap<Calendar, float[]> getMapFloats(int pos, int... pos2) {
		TreeMap<Calendar, float[]> out = null;
		if (pos >= 0 && pos < length()) {
			out = new TreeMap<>();
			int sb = getDataSize();
			for (Entry<Integer, byte[]> e : this.entrySet()) {
				byte[] bs = e.getValue();
				bs = Arrays.copyOfRange(bs, pos * sb, (pos + 1) * sb);
				float[] fs = MedDataType.getFloats(bs, super.dataTypes);
				fs = ArrayUtils.getElements(fs, pos2);
				out.put(TimeUtils.toCalendar(e.getKey()), fs);
			}
		}
		return out;
	}

	public double[][] getMatrix(int pos, Calendar begin, Calendar end) {
		if (begin == null || end == null)
			return getMatrix(pos, true);
		else
			return getMatrix(this, pos, TimeUtils.toInt(begin), TimeUtils.toInt(end), true);
	}
}
