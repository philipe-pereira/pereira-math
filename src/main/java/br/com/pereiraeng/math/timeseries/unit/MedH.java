package br.com.pereiraeng.math.timeseries.unit;

import java.util.Calendar;

import br.com.pereiraeng.core.collections.ArrayUtils;
import br.com.pereiraeng.math.timeseries.esp.MedDataType;

/**
 * Classe que representa uma dada medição para qual se obteve uma série de dados
 * que serão representados em um vetor de binários. Tais dados são referentes
 * {@link #getTime() ao mesmo instante de tempo}.
 * 
 * @author Philipe PEREIRA
 *
 */
public class MedH extends Vlb {

	public MedH(Calendar time, String[] labels, MedDataType[] dataTypes, int m) {
		super(time, dataTypes, m, labels);
	}

	public int getIndex(String label) {
		return ArrayUtils.indexOf(super.labels, label);
	}

	public byte[] get(String label) {
		int pos = getIndex(label);
		if (pos == -1)
			return null;
		else
			return get(pos);
	}

	// ---------------------------------

	// por primitiva...

	public float[] getF(String label) {
		byte[] bs = get(label);
		if (bs == null)
			return null;
		return MedDataType.getFloats(bs, getDataTypes(), getM());
	}
}
