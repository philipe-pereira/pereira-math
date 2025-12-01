package br.com.pereiraeng.math.timeseries.unit;

import java.util.Calendar;

public abstract class TData implements Comparable<TData> {

	protected Calendar time;

	protected int channel = -1;

	public TData(Calendar time) {
		this.setTime(time);
	}

	public void setTime(Calendar time) {
		if (time == null)
			throw new IllegalArgumentException("Medição tem de ter pelo menos data e hora.");
		this.time = time;
	}

	public Calendar getTime() {
		return time;
	}

	public boolean before(Calendar c) {
		return this.getTime().before(c);
	}

	/**
	 * Função que estabelece o canal que indexa esta medição
	 * 
	 * @param channel número inteiro que indica o canal da medição
	 */
	public void setChannel(int channel) {
		this.channel = channel;
	}

	/**
	 * Função que retorna o canal que indexa esta medição
	 * 
	 * @return número inteiro que indica o canal da medição
	 */
	public int getChannel() {
		return this.channel;
	}

	@Override
	public String toString() {
		return String.format("%1$td/%1$tm/%1$ty %1$tH:%1$tM", time);
	}

	@Override
	public int compareTo(TData med) {
		return this.getTime().compareTo(med.getTime());
	}
}
