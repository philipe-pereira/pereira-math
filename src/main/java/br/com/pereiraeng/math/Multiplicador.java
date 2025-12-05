package br.com.pereiraeng.math;

/**
 * Enumeração dos principais multiplicadores de unidades de medida
 * 
 * @author Philipe PEREIRA
 * 
 */
public enum Multiplicador {
	GIGA("G", 9), MEGA("M", 6), QUILO("k", 3), HECTO("h", 2), DECA("da", 1), UNI("", 0), DECI("d", -1), CENTI("c", -2),
	MILI("m", -3), MICRO("\u03BC", -6), NANO("n", -9), PICO("p", -12);

	/**
	 * Vetor com os multiplicadores que são potências múltiplas de 3
	 */
	public static final Multiplicador[] POW3 = { PICO, NANO, MICRO, MILI, UNI, QUILO, MEGA, GIGA };

	private String symbol;
	private int power;

	private Multiplicador(String letter, int power) {
		this.symbol = letter;
		this.power = power;
	}

	public String getSymbol() {
		return symbol;
	}

	public int getPower() {
		return power;
	}

	public double getMultiplier() {
		return Math.pow(10, power);
	}

	public static Multiplicador getMultiplier(String symbol) {
		for (Multiplicador m : values())
			if (m.getSymbol().equals(symbol))
				return m;
		return null;
	}

	/**
	 * Função que transforma um número em uma sequência de caracteres onde está
	 * indicado o número acompanhado de seu respectivo multiplicador
	 * 
	 * @param valor     valor a ser transformado
	 * @param precision precisão de casas decimais
	 * @param mults     multiplicadores que podem ser usados na transformação
	 * @return sequência de caracteres
	 */
	public static String getMult(double valor, int precision, Multiplicador... mults) {
		if (valor == 0.)
			return "0";
		Multiplicador m = null;
		for (int i = 1; i < mults.length; i++) {
			Multiplicador mi = mults[i];
			if (Math.abs(valor) < mi.getMultiplier()) {
				// se está abaixo de um multiplicador, escolhe-se o anterior
				m = mults[i - 1];
				break;
			}
		}
		// se nenhum multiplicador está abaixo do maior, seleciona-se o
		// maior
		if (m == null)
			m = mults[mults.length - 1];

		// divisor
		double divider = m.getMultiplier();

		return String.format("%." + String.valueOf(precision) + "g %s", valor / divider, m.getSymbol());
	}
}
