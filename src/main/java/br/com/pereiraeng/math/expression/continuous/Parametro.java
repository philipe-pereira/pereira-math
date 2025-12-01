package br.com.pereiraeng.math.expression.continuous;

/**
 * Bloco de valor que caracteriza um valor variável, mas não é a abscissa da
 * função. Este bloco pode ser usado para estudar como se comporta uma função a
 * se variar este parâmetro. O valor varia dentro de uma <strong>faixa</strong>
 * permitida.
 * 
 * @author Philipe PEREIRA
 *
 */
public class Parametro extends Valor {

	/**
	 * Designação do parâmetro
	 */
	private String nomeDoParametro;

	/**
	 * Valor máximo que o parâmetro pode assumir
	 */
	private double min;

	/**
	 * Valor máximo que o parâmetro pode assumir
	 */
	private double max;

	private boolean log = false;

	private boolean compact = false;

	public Parametro(String nomeDoParametro) {
		super(-1);
		this.nomeDoParametro = nomeDoParametro;

		super.valor = 0.0;
		this.min = Double.MIN_VALUE;
		this.max = Double.MAX_VALUE;
	}

	public void setValor(double valor) {
		if (valor >= min && valor <= max)
			super.valor = valor;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public void setLog(boolean log) {
		this.log = log;
	}

	/**
	 * Função que indica se é um parâmetro que varia logaritmicamente dentro do
	 * intervalo
	 * 
	 * @return Se <code>true</code>, a variação se dá de maneira logarítmica; se
	 *         <code>false</code>, linear
	 */
	public boolean islog() {
		return this.log;
	}

	/**
	 * Função que estabelece se este parâmetro, ao ser editado no ParametroInput,
	 * será exibido de maneira compacta ou não
	 * 
	 * @param compact se <code>true</code>, o ParametroInput será compactado; se
	 *                <code>false</code>, será exibido de maneira completa
	 */
	public void setCompact(boolean compact) {
		this.compact = compact;
	}

	/**
	 * Função que indica se este parâmetro, ao ser editado no ParametroInput, será
	 * exibido de maneira compacta ou não
	 * 
	 * @return se <code>true</code>, o ParametroInput será compactado; se
	 *         <code>false</code>, será exibido de maneira completa
	 */
	public boolean isCompact() {
		return compact;
	}

	@Override
	public double getValor() {
		return super.valor;
	}

	@Override
	public String toString() {
		return nomeDoParametro;
	}

	@Override
	public int getLargura() {
		return 1;
	}

	public void setProfundidade(int profundidade) {
		if (profundidade > super.profundidade)
			super.profundidade = profundidade;
	}
}
