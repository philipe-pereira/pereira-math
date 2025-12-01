package br.com.pereiraeng.math.expression;

import java.util.regex.Pattern;

public interface Var {

	public static final Pattern VAR = Pattern.compile("(x|X)\\d*[^\\p{Alpha}\\p{Punct}]");

	/**
	 * Função que estabelece o valor do objeto
	 * 
	 * @param value valor do objeto
	 */
	public void setValue(Object value);
}
