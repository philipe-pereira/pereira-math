package br.com.pereiraeng.math.expression.discret;

/**
 * Bloco de operação transformam um ou mais blocos de valores em um outro bloco
 * <code>boolean</code>
 * 
 * @author Philipe PEREIRA
 *
 */
public class OperadorV2B extends OperadorB {
	private TipoOperadorV2B operador;

	private VariavelNB[] operandos;

	private String modifier;

	public enum TipoOperadorV2B {
		NEQUAL("<>", 2), GEQ(">=", 2), LEQ("<=", 2), GT(">", 2), LT("<", 2), EQUAL("=", 2), BETWEEN(" @ ", 1),
		LIKE(" LIKE ", 1), REGEXP(" REGEXP ", 1), IN(" IN", 1);

		public String op;
		public int numOpe;

		private TipoOperadorV2B(String s, int numOpe) {
			this.op = s;
			this.numOpe = numOpe;
		}

		public static TipoOperadorV2B getOperator(String exp) {
			for (int i = 0; i < TipoOperadorV2B.values().length; i++) {
				TipoOperadorV2B t = TipoOperadorV2B.values()[i];
				if (exp.contains(t.op)) {
					return t;
				}
			}
			return null;
		}
	}

	public OperadorV2B(TipoOperadorV2B operador) {
		this.operador = operador;
		this.operandos = new VariavelNB[operador.numOpe];
	}

	public void setOperando(VariavelNB valor, int pos) {
		this.operandos[pos] = valor;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public boolean getValue() {
		boolean pat = true;
		switch (operador) {
		case EQUAL:
			return operandos[0].equals(operandos[1]);
		case NEQUAL:
			return !operandos[0].equals(operandos[1]);
		case GT:
			return operandos[0].gt(operandos[1]);
		case LT:
			return operandos[1].gt(operandos[0]);
		case GEQ:
			return operandos[0].geq(operandos[1]);
		case LEQ:
			return operandos[1].geq(operandos[0]);
		case BETWEEN:
			return operandos[0].between(modifier);
		case LIKE:
			pat = modifier.contains("%") || modifier.contains("_");
		case REGEXP:
			return operandos[0].pattern(modifier, pat);
		case IN:
			return operandos[0].in(modifier);
		default:
			return false;
		}
	}
}
