package br.com.pereiraeng.math.timeseries.unit;

import java.util.Calendar;

import br.com.pereiraeng.math.timeseries.esp.MedDataType;
import br.com.pereiraeng.core.TimeUtils;

/**
 * Classe que representa uma dada medição para qual se obteve uma série de dados
 * que serão representados em um vetor de binários. Tais dados são referentes a
 * mais de um instante de tempo, tendo um {@link #getTime() início}, um
 * {@link #getEnd() fim} e um {@link #getStep() passo}.
 * 
 * @author Philipe PEREIRA
 *
 */
public class MedV extends Vlb {

	private final short step;

	private final int end;

	public MedV(String label, Calendar time, short step, int end, MedDataType[] dataTypes, int m) {
		super(time, dataTypes, m, label);
		this.step = step;
		this.end = end;
	}

	public short getStep() {
		return step;
	}

	public int getEnd() {
		return end;
	}

	public byte[] get(Calendar c) {
		int pos = getIndex(c);
		return get(pos);
	}

	private int getIndex(Calendar c) {
		int ci = TimeUtils.toInt(c);
		int ci0 = TimeUtils.toInt(super.time);
		int pos = (ci - ci0) / step;
		return pos;
	}

	// ---------------------------------

	// por primitiva...

	public float[] getF(Calendar c) {
		byte[] bs = get(c);
		if (bs == null)
			return null;
		return MedDataType.getFloats(bs, getDataTypes(), getM());
	}
}
