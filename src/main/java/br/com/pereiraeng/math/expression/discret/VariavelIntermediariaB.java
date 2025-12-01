package br.com.pereiraeng.math.expression.discret;

/**
 * Bloco de valor que caracteriza um valor intermediário no cálculo da função
 * 
 * @author Philipe PEREIRA
 *
 */
public class VariavelIntermediariaB extends ValorB {

	private OperadorB child;

	public VariavelIntermediariaB(OperadorB child) {
		super.valor = false;
		this.child = child;
	}

	@Override
	public boolean getValue() {
		return (super.valor = child.getValue());
	}

	/**
	 * Função que retorna o filho desta variável na árvore da expressão. Pode ser
	 * tanto o resultado de outro expressão
	 * 
	 * @return bloco booleano, seja uma variável ou um operador
	 */
	public OperadorB getChild() {
		return this.child;
	}
}
