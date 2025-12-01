package br.com.pereiraeng.math.timeseries.unit;

import java.util.Calendar;

/**
 * Medição de grandeza contínua no tempo
 * 
 * @author Philipe PEREIRA
 *
 */
public abstract class Ct extends TData {

	public static final byte ESTIM_DISP = 2, ERRO = 3, FALHA_COM = 5, INIBIDO = 6, SIMULADO = 7;

	protected byte mask = 0;

	public Ct(Calendar time) {
		super(time);
	}

	public void setMask(byte mask) {
		this.mask = mask;
	}

	public byte getMask() {
		return mask;
	}

	/**
	 * Função que aciona o indicador de que esta medição é estimada
	 */
	public void setEstimated() {
		setEstimated(true);
	}

	public void setEstimated(boolean estimated) {
		mask = setMask(mask, estimated, SIMULADO);
	}

	public boolean isEstimated() {
		return getMask(mask, SIMULADO);
	}

	public void setCommFailed() {
		setCommFailed(true);
	}

	public void setCommFailed(boolean commFailed) {
		mask = setMask(mask, commFailed, FALHA_COM);
	}

	public boolean isCommFailed() {
		return getMask(mask, FALHA_COM);
	}

	public static byte setMask(byte mask, boolean value, byte maskPos) {
		if (value)
			return (byte) (mask | (1 << maskPos));
		else
			return (byte) (mask ^ (1 << maskPos));
	}

	public static boolean getMask(byte mask, byte maskPos) {
		return (mask & (1 << maskPos)) != 0;
	}
}
