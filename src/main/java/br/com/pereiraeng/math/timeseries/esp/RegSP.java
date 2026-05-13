package br.com.pereiraeng.math.timeseries.esp;

import java.util.Calendar;

import br.com.pereiraeng.core.ExtendedMath;
import br.com.pereiraeng.core.TimeUtils;
import br.com.pereiraeng.math.timeseries.RegP;

/**
 * Classe do objeto que representa uma série de medições onde se é
 * {@link RegSP#fitFreq(int) forçada} uma periodicidade dos instantes de tempo
 * (ou seja, as medições tem-se de manter uma {@link RegSP#getFreq() distância
 * temporal mínima} uns dos outros)
 * 
 * @author Philipe PEREIRA
 * @version 2019
 *
 */
public class RegSP extends RegS {
	private static final long serialVersionUID = 1L;

	/**
	 * Frequência com que os registros são guardados
	 */
	private int freq;

	/**
	 * Frequência padrão com que os registros são guardados (15')
	 */
	private static final int FREQ_DEFAULT = 15;

	/**
	 * Construtor de um novo objeto de registro de medições. A distância de tempo
	 * mínima entre duas medições é de {@link RegP#FREQ_DEFAULT 15} minutos.
	 * 
	 * @param m      Número de medições no padrão {@link #dataTypes} que
	 *               {@link #values() este objeto} comporta para cada
	 *               {@link #getLabel(int) etiqueta}
	 * @param header cabeçalho binário
	 * @param regs   número de m-medições por horário
	 * @param dts    Padrão das primitivas que compõe o vetor de binários
	 */
	public RegSP(int m, byte[] header, int regs, MedDataType... dts) {
		this(m, header, regs, FREQ_DEFAULT, dts);
	}

	/**
	 * 
	 * @param m      Número de medições no padrão {@link #dataTypes} que
	 *               {@link #values() este objeto} comporta para cada
	 *               {@link #getLabel(int) etiqueta}
	 * @param header cabeçalho binário
	 * @param labels etiquetas das grandezas
	 * @param dts    Padrão das primitivas que compõe o vetor de binários
	 */
	public RegSP(int m, byte[] header, String[] labels, MedDataType... dts) {
		this(m, header, labels, FREQ_DEFAULT, dts);
	}

	/**
	 * 
	 * @param m      Número de medições no padrão {@link #dataTypes} que
	 *               {@link #values() este objeto} comporta para cada
	 *               {@link #getLabel(int) etiqueta}
	 * @param header cabeçalho binário
	 * @param regs   número de m-medições por horário
	 * @param freq   frequência de tempo, em minutos, em que são inseridos na tabela
	 * @param dts    Padrão das primitivas que compõe o vetor de binários
	 */
	public RegSP(int m, byte[] header, int regs, int freq, MedDataType... dts) {
		this(m, header, new String[regs], freq, dts);
	}

	/**
	 * 
	 * @param m      Número de medições no padrão {@link #dataTypes} que
	 *               {@link #values() este objeto} comporta para cada
	 *               {@link #getLabel(int) etiqueta}
	 * @param header cabeçalho binário
	 * @param labels etiquetas das grandezas
	 * @param freq   frequência de tempo, em minutos, em que são inseridos na tabela
	 * @param dts    Padrão das primitivas que compõe o vetor de binários
	 */
	public RegSP(int m, byte[] header, String[] labels, int freq, MedDataType... dts) {
		super(header, labels, m, dts);
		this.setFreq(freq);
	}

	// --------------------------- SETTER'S/PUTTER'S ---------------------------

	@Override
	public byte[] put(Integer ci, int pos, byte[] value) {
		return super.put(fitFreq(ci), pos, value);
	}

	@Override
	public byte[] put(Integer key, byte[] values) {
		return super.put(fitFreq(key), values);
	}

	@Override
	public byte[] put(Integer key, int pos1, int pos2, byte[] value) {
		return super.put(fitFreq(key), pos1, pos2, value);
	}

	// ------------------------------- GETTER'S ------------------------------

	@Override
	public byte[] get(Integer ci, int pos) {
		return super.get(fitFreq(ci), pos);
	}

	// ------------------------------- TIME FIT ------------------------------

	/**
	 * Função que retorna o intervalo mínimo de tempo, em minutos, entre duas
	 * medições
	 * 
	 * @return inteiro que indica a cadência de medições, em minutos
	 */
	public int getFreq() {
		return freq;
	}

	/**
	 * Função que altera o intervalo mínimo de tempo, em minutos, entre duas
	 * medições
	 * 
	 * @param freq inteiro que indica a cadência de medições, em minutos
	 */
	public void setFreq(int freq) {
		this.freq = freq;
	}

	/**
	 * Função que faz com que o número inteiro que representa a data seja múltiplo
	 * de um dado valor
	 * 
	 * @param ic inteiro que representa a data (ver {@link TimeUtils#toInt(Calendar)
	 *           conversão para inteiro})
	 * @return inteiro múltiplo da {@link RegP#freq frequência} mais próximo do
	 *         inteiro dado
	 */
	private int fitFreq(int ic) {
		return fitFreq(ic, getFreq());
	}

	/**
	 * Função que faz com que o número inteiro que representa a data seja múltiplo
	 * de um dado valor
	 * 
	 * @param ic   inteiro que representa a data (ver {@link TimeUtils#toInt(Calendar)
	 *             conversão para inteiro})
	 * @param freq frequência dada
	 * @return inteiro múltiplo da frequência mais próximo do inteiro dado
	 */
	private static int fitFreq(int ic, int freq) {
		// passar a frequência para segundos
		freq *= 60;
		// desloca metade da frequência (centraliza o intervalo)
		int o = ic + freq / 2;
		// arredonda para o maior múltiplo da frequência menor que o dado valor
		return o - ExtendedMath.mod(o, freq);
	}
}
