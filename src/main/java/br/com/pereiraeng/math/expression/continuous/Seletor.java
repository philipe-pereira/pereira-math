package br.com.pereiraeng.math.expression.continuous;

import java.util.TreeSet;

import br.com.pereiraeng.core.collections.ListUtils;

/**
 * Bloco de valor que caracteriza um valor variável, mas não é a abscissa da
 * função. Este bloco pode ser usado para estudar como se comporta uma função a
 * se variar este parâmetro. O valor varia dentro de um
 * <strong>conjunto</strong> de valores permitidos.
 * 
 * @author Philipe PEREIRA
 *
 */
public class Seletor extends Valor {

	private TreeSet<Double> values;

	public Seletor(double... values) {
		super(-1);
		this.values = new TreeSet<>();
		for (double d : values)
			this.values.add(d);
		super.valor = values[0];
	}

	public void setValor(int index) {
		super.valor = ListUtils.getElementAt(this.values, index);
	}

	public TreeSet<Double> getValues() {
		return values;
	}

	@Override
	public int getLargura() {
		return 1;
	}

	@Override
	public double getValor() {
		return super.valor;
	}
}
