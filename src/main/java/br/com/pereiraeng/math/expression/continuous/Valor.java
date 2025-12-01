package br.com.pereiraeng.math.expression.continuous;

/**
 * Bloco de valor que contem um determinado valor, sendo este obtido seja
 * através de uma tranformação, seja ao se estipular quanto ele deve valer.
 * 
 * @author Philipe PEREIRA
 *
 */
public abstract class Valor extends Bloco {

	/**
	 * valor numérico (provisório ou não) do objeto
	 */
	protected double valor;

	public Valor(int profundidade) {
		super.profundidade = profundidade;
	}

	@Override
	public String toString() {
		return String.format("%.3f", valor);
	}
}
