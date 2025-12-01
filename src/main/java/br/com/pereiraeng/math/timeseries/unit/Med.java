package br.com.pereiraeng.math.timeseries.unit;

import java.util.Calendar;
import java.util.Date;

import br.com.pereiraeng.core.TimeUtils;

/**
 * Classe que representa uma dada medição (feita numa dada hora e onde obteve-se
 * um dado valor)
 * 
 * @author Philipe PEREIRA
 *
 */
public class Med extends Ct {

	/**
	 * valor medido
	 */
	private float value;

	/**
	 * Construtor do objeto que representa uma medição
	 */
	public Med() {
		this(Calendar.getInstance());
	}

	/**
	 * Construtor do objeto que representa uma medição
	 * 
	 * @param date horário em que a medição foi feita
	 */
	public Med(Calendar date) {
		this(date, Float.NaN);
	}

	/**
	 * Construtor do objeto que representa uma medição
	 * 
	 * @param date  horário em que a medição foi feita
	 * @param value valor medido
	 */
	public Med(Date date, float value) {
		this(TimeUtils.date2Calendar(date), value);
	}

	/**
	 * Construtor do objeto que representa uma medição
	 * 
	 * @param time  horário em que a medição foi feita
	 * @param value valor medido
	 */
	public Med(Calendar time, float value) {
		super(time);
		this.setValue(value);
	}

	public void setValue(float value) {
		this.value = value;
	}

	public void set(Med med) {
		this.setTime(med.getTime());
		this.setValue(med.getValue());
		this.setMask(med.getMask());
	}

	public float getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("%s %.1f", super.toString(), value);
	}
}
