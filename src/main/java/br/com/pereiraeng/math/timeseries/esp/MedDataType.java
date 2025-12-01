package br.com.pereiraeng.math.timeseries.esp;

import java.nio.ByteBuffer;
import java.util.Arrays;

public enum MedDataType {
	BYTE(1), INT(4), FLOAT(4), DOUBLE(8);

	private final int size;

	private MedDataType(int size) {
		this.size = size;
	}

	public int size() {
		return size;
	}

	public void writeNull(byte[] bs, int pos) {
		switch (this) {
		case FLOAT:
			ByteBuffer.wrap(bs, pos, 4).putFloat(Float.NaN);
			break;
		case DOUBLE:
			ByteBuffer.wrap(bs, pos, 8).putDouble(Double.NaN);
			break;
		default:
			for (int i = 0; i < this.size; i++)
				bs[pos + i] = 0;
			break;
		}
	}

	public static int getDataPos(MedDataType[] dts, int pos) {
		if (pos == 0)
			return 0;
		else {
			int out = 0;
			for (int i = 0; i < pos; i++)
				out += dts[i].size();
			return out;
		}
	}

	/**
	 * Função que retorna o número de bytes utilizados para cada dado
	 * 
	 * @return número de byte utilizados para guardar cada dado
	 */
	public static int getDataSize(MedDataType[] dts) {
		if (dts == null ? true : dts.length == 0)
			return 0;
		int out = 0;
		for (int i = 0; i < dts.length; i++)
			out += dts[i].size();
		return out;
	}

	public static MedDataType[] getDefault(byte bpm) {
		if (bpm <= 4)
			return new MedDataType[] { MedDataType.FLOAT }; // valor
		else {
			MedDataType[] out = null;
			if (bpm % 5 == 0) { // (valor + máscara)s
				out = new MedDataType[2 * (bpm / 5)];
				for (int i = 0; i < out.length; i++)
					out[i] = i % 2 == 0 ? MedDataType.FLOAT : MedDataType.BYTE;
			} else if ((bpm - 1) % 4 == 0) { // padrão RegHist (valores + máscara)
				out = new MedDataType[(bpm - 1) / 4 + 1];
				Arrays.fill(out, MedDataType.FLOAT);
				out[out.length - 1] = MedDataType.BYTE;
			}
			return out;
		}
	}

	// ------------------------------- FLOATS ------------------------------

	public static float[] getFloats(byte[] bs, MedDataType[] dts) {
		return getFloats(bs, dts, bs.length / getDataSize(dts));
	}

	/**
	 * 
	 * @param bs  vetor de bytes para um dado instante de tempo
	 * @param dts vetor de {@link MedDataType}
	 * @param m   número de dados (vetor de bytes seguindo o padrão do vetor de
	 *            {@link MedDataType}) há por ponto
	 * @return
	 */
	public static float[] getFloats(byte[] bs, MedDataType[] dts, int m) {
		ByteBuffer bb = ByteBuffer.wrap(bs);
		float[] out = new float[getFloats(dts) * m];
		int o = 0, c = 0;
		for (int j = 0; j < m; j++) {
			for (int i = 0; i < dts.length; i++) {
				if (dts[i] == MedDataType.FLOAT)
					out[c++] = bb.getFloat(o);
				o += dts[i].size();
			}
		}
		return out;
	}

//	public static float getFloat(byte[] bs, MedDataType[] dts, int pos2) {
//		int ds = getDataSize(dts);
//		ByteBuffer bb = ByteBuffer.wrap(bs, pos2 * ds, (pos2 + 1) * ds);
//		return bb.getFloat(MedDataType.getDataPos(dts, pos2));
//	}

	public static int getFloats(MedDataType[] medDataTypes) {
		int out = 0;
		for (int i = 0; i < medDataTypes.length; i++)
			if (medDataTypes[i] == MedDataType.FLOAT)
				out++;
		return out;
	}

	// ------------------------------- BYTES ------------------------------

	public static byte[] getBytes(byte[] bs, MedDataType[] dts, int m) {
		byte[] out = new byte[getBytes(dts) * m];
		int o = 0, c = 0;
		for (int j = 0; j < m; j++) {
			for (int i = 0; i < dts.length; i++) {
				if (dts[i] == MedDataType.BYTE)
					out[c++] = bs[o];
				o += dts[i].size();
			}
		}
		return out;
	}

	public static int getBytes(MedDataType[] medDataTypes) {
		int out = 0;
		for (int i = 0; i < medDataTypes.length; i++)
			if (medDataTypes[i] == MedDataType.BYTE)
				out++;
		return out;
	}

	// ------------------------------- AUXILIARES ------------------------------

	// DataType.FLOAT, DataType.BYTE

	public static float[] floatMask2float(byte[] bloco) {
		int m = bloco.length / 5;
		float[] out = new float[m];
		ByteBuffer bf = ByteBuffer.wrap(bloco);
		for (int i = 0; i < m; i++)
			out[i] = bf.getFloat(5 * i);
		return out;
	}

	public static byte[] float2floatMask(float[] bloco) {
		byte[] out = new byte[bloco.length * 5];
		ByteBuffer bf = ByteBuffer.wrap(out);
		for (int i = 0; i < bloco.length; i++)
			bf.putFloat(5 * i, bloco[i]);
		return out;
	}

	// DataType.DOUBLE

	public static double[] double2double(byte[] bloco) {
		int m = bloco.length / 8;
		double[] out = new double[m];
		ByteBuffer bf = ByteBuffer.wrap(bloco);
		for (int i = 0; i < m; i++)
			out[i] = bf.getDouble(8 * i);
		return out;
	}

	public static byte[] double2double(double[] bloco) {
		byte[] out = new byte[bloco.length * 8];
		ByteBuffer bf = ByteBuffer.wrap(out);
		for (int i = 0; i < bloco.length; i++)
			bf.putDouble(8 * i, bloco[i]);
		return out;
	}
}
