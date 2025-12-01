package br.com.pereiraeng.math.timeseries.unit;

import java.util.Calendar;

public class Est extends Dt {

	private boolean estado;

	public Est(Calendar time, boolean estado) {
		super(time);
		this.estado = estado;
	}

	public boolean getEstado() {
		return estado;
	}
}
