package br.com.pereiraeng.math.timeseries.unit;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import br.com.pereiraeng.core.TimeUtils;
import br.com.pereiraeng.core.collections.ArrayUtils;

/**
 * Classe que representa uma série de medições feitas numa mesma hora
 * 
 * @author Philipe PEREIRA
 *
 */
public class Meds extends Ct {

	private float[] value;

	/**
	 * máscara de dados (o tamanho desse vetor é value.length-1, pois este objeto
	 * utilizado o mask de TData)
	 */
	private byte[] masks;

	/**
	 * Construtor do objeto de medições
	 */
	public Meds() {
		this(0);
	}

	/**
	 * Construtor do objeto de medições
	 * 
	 * @param size número de medições que este objeto pode comportar
	 */
	public Meds(int size) {
		this(Calendar.getInstance(), ArrayUtils.floatVec(Float.NaN, size));
	}

	/**
	 * Construtor do objeto de medições
	 * 
	 * @param date  data e hora da medição
	 * @param value vetor com os valores das medições
	 */
	public Meds(Date date, float[] value) {
		this(TimeUtils.date2Calendar(date), value);
	}

	/**
	 * Construtor do objeto de medições
	 * 
	 * @param time  data e hora da medição
	 * @param value vetor com os valores das medições
	 */
	public Meds(Calendar time, float... value) {
		super(time);
		this.setValue(value);
	}

	public void setValue(float[] values) {
		this.value = values;
	}

	public void setValue(float value, int pos) {
		this.value[pos] = value;
	}

	public void addValue(float value, int pos) {
		this.value[pos] += value;
	}

	public float[] getValue() {
		return value;
	}

	public float getValue(int pos) {
		return value[pos];
	}

	public int length() {
		return value.length;
	}

	public void set(Meds med) {
		this.setTime(med.getTime());
		this.setValue(med.getValue());
	}

	@Override
	public String toString() {
		return String.format("%s\t%s", super.toString(), Arrays.toString(value));
	}

	public boolean isComplete() {
		for (int i = 0; i < value.length; i++)
			if (Float.isNaN(value[i]))
				return false;
		return true;
	}

	// ----------------

	public void setMasks(byte[] masks) {
		this.masks = masks;
	}

	/**
	 * Função que aciona o indicador de que esta medição é estimada
	 */
	public void setEstimated(int pos) {
		setEstimated(pos, true);
	}

	public void setEstimated(int pos, boolean estimated) {
		setMask(pos, estimated, SIMULADO);
	}

	public boolean isEstimated(int pos) {
		return getMask(pos, SIMULADO);
	}

	public void setCommFailed(int pos) {
		setCommFailed(pos, true);
	}

	public void setCommFailed(int pos, boolean commFailed) {
		setMask(pos, commFailed, FALHA_COM);
	}

	public boolean isCommFailed(int pos) {
		return getMask(pos, FALHA_COM);
	}

	public void setMask(int pos, boolean value, byte maskPos) {
		if (pos == 0)
			mask = setMask(mask, value, maskPos);
		else {
			if (masks == null)
				masks = new byte[this.value.length - 1];
			masks[pos - 1] = setMask(masks[pos - 1], value, maskPos);
		}
	}

	public byte getMask(int pos) {
		return pos == 0 ? mask : (masks == null ? 0 : masks[pos - 1]);
	}

	public boolean getMask(int pos, byte maskPos) {
		return getMask(getMask(pos), maskPos);
	}
}