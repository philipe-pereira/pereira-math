package br.com.pereiraeng.math.timeseries.unit;

import java.util.Calendar;

public class Ests extends Dt {

	private boolean[] estado;

	public Ests(Calendar time, boolean[] estado) {
		super(time);
		this.estado = estado;
	}

	public boolean[] getEstado() {
		return estado;
	}

	public boolean getEstado(int pos) {
		return estado[pos];
	}
}
