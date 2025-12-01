package br.com.pereiraeng.math.timeseries.unit;

import java.util.Arrays;
import java.util.Calendar;

import br.com.pereiraeng.math.timeseries.esp.MedDataType;

/**
 * Classe dos objeto de dados temporais que podem ser representados por um vetor
 * de bytes de tamanho variável (<strong>V</strong>ariable
 * <strong>L</strong>ength <strong>B</strong>inaries)
 * 
 * @author Philipe PEREIRA
 * @version October 7th, 2020
 */
public abstract class Vlb extends TData {

	protected final String[] labels;

	/**
	 * Padrão das primitivas que compõe o vetor de binários
	 */
	private final MedDataType[] dataTypes;

	/**
	 * Número de medições no padrão {@link #getDataTypes()} que {@link #values este
	 * objeto} comporta para cada
	 */
	private final int m;

	/**
	 * bytes por medição (ver {@link MedDataType#getDataSize(MedDataType[])})
	 */
	private final int dataSize;

	protected byte[] values;

	protected Vlb(Calendar time, MedDataType[] dataTypes, int m, String... labels) {
		super(time);
		this.labels = labels;
		this.dataTypes = dataTypes;
		this.dataSize = m * MedDataType.getDataSize(dataTypes);
		this.m = m;
	}

	public MedDataType[] getDataTypes() {
		return dataTypes;
	}

	public void setValues(byte[] values) {
		this.values = values;
	}

	public byte[] getValues() {
		return this.values;
	}

	/**
	 * Função que retorna o número de medições no padrão {@link #getDataTypes()} que
	 * este objeto comporta para cada {@link #getLabel(int) etiqueta}
	 * 
	 * @return
	 */
	public int getM() {
		return m;
	}

	protected byte[] get(int pos1) {
		int e = (pos1 + 1) * this.dataSize;
		if (e <= this.values.length)
			try {
				return Arrays.copyOfRange(this.values, pos1 * this.dataSize, e);
			} catch (ArrayIndexOutOfBoundsException ex) {
				System.out.println();
				return null;
			}
		else
			return null;
	}

	public String[] getLabels() {
		return labels;
	}

	public String getLabel(int pos1) {
		return labels[pos1];
	}

	// ---------------------------------

	// por primitiva...

	/**
	 * 
	 * @param pos1
	 * @return
	 */
	public float[] getF(int pos1) {
		byte[] bs = get(pos1);
		if (bs == null)
			return null;
		return MedDataType.getFloats(bs, getDataTypes(), getM());
	}
	
	public byte[] getB(int pos1) {
		byte[] bs = get(pos1);
		if (bs == null)
			return null;
		return MedDataType.getBytes(bs, getDataTypes(), getM());
	}
}
