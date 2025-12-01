package br.com.pereiraeng.math.expression.discret;

/**
 * Objeto abstrato que pode tanto representar tanto um operador que retorna um
 * objeto booleano quanto uma variável intermediária no cálculo de uma expressão
 * 
 * @author Philipe Pereira
 *
 */
public abstract class BlocoB {

	/**
	 * Função que retorna o valor correspondente e este elemento da expressão
	 * 
	 * @return valor binário
	 */
	public abstract boolean getValue();
}