package br.com.pereiraeng.math.timeseries.unit;

import java.util.Calendar;

/**
 * Medição de grandeza discreta no tempo
 * 
 * @author Philipe PEREIRA
 *
 */
public abstract class Dt extends TData {

	public Dt(Calendar time) {
		super(time);
	}
}
