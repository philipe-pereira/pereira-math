package br.com.pereiraeng.math.expression.discret;

import br.com.pereiraeng.math.expression.Var;

/**
 * Bloco de valor que caracteriza a abscissa da função binária (pode valer
 * <code>true</code> ou <code>false</code>)
 * 
 * @author Philipe PEREIRA
 *
 */
public class VariavelB extends ValorB implements Var {
	private String x;

	public VariavelB(String x) {
		this.x = x;
		super.valor = false;
	}

	@Override
	public boolean getValue() {
		return super.valor;
	}

	@Override
	public void setValue(Object valor) {
		super.valor = (boolean) valor;
	}

	@Override
	public String toString() {
		return x;
	}
}
