package br.com.pereiraeng.math.expression.discret;

import java.util.LinkedList;

/**
 * Bloco de operação transformam um ou mais blocos de valor <code>boolean</code>
 * em um outro bloco <code>boolean</code>
 * 
 * @author Philipe PEREIRA
 *
 */
public class OperadorB2B extends OperadorB {
	private TipoOperadorB2B operador;

	private LinkedList<ValorB> operandos;

	public enum TipoOperadorB2B {
		AND("AND"), OR("OR"), NOT("NOT"), XOR("XOR");

		public String s;

		private TipoOperadorB2B(String s) {
			this.s = s;
		}
	}

	public OperadorB2B(TipoOperadorB2B operador) {
		this.operador = operador;
		this.operandos = new LinkedList<>();
	}

	public void setOperando(ValorB valor) {
		operandos.add(valor);
	}

	@Override
	public boolean getValue() {
		switch (operador) {
		case AND:
			boolean out = true;
			for (ValorB v : operandos)
				out &= v.getValue();
			return out;
		case OR:
			out = false;
			for (ValorB v : operandos)
				out |= v.getValue();
			return out;
		case XOR:
			out = operandos.get(0).getValue();
			for (int i = 1; i < operandos.size(); i++)
				out ^= operandos.get(i).getValue();
			return out;
		case NOT:
			return !operandos.get(0).getValue();
		default:
			return false;
		}
	}
}
