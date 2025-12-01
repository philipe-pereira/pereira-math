package br.com.pereiraeng.math.expression.continuous;

import br.com.pereiraeng.core.ExtendedMath;

/**
 * Bloco de valor constante
 * 
 * @author Philipe PEREIRA
 *
 */
public class Constante extends Valor {

	public enum Const {
		PI("pi", Math.PI), E("e", Math.E), SQRT2("raiz2", ExtendedMath.SQRT2), SQRT3("raiz3", ExtendedMath.SQRT3);

		public String s;

		private double value;

		private Const(String s, double value) {
			this.s = s;
			this.value = value;
		}

		public double getValue() {
			return value;
		}

		public static Const getConstante(String exp) {
			for (int i = 0; i < Const.values().length; i++) {
				if (exp.equalsIgnoreCase(Const.values()[i].s))
					return Const.values()[i];
			}
			return null;
		}
	}

	public Constante(double valor, int profundidade) {
		super(profundidade);
		super.valor = valor;
	}

	public Constante(Const c, int profundidade) {
		this(c.getValue(), profundidade);
	}

	@Override
	public double getValor() {
		return super.valor;
	}

	@Override
	public int getLargura() {
		return 1;
	}
}
