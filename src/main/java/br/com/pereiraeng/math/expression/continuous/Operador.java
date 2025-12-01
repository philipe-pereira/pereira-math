package br.com.pereiraeng.math.expression.continuous;

/**
 * Bloco de operação transformam um ou mais blocos de valor em um outro bloco. A
 * transformação a ser efetuada depende do valor do campo
 * <code>TipoOperador</code>
 * 
 * @author Philipe PEREIRA
 *
 */
public abstract class Operador extends Bloco {
	protected TipoOperador operador;

	public enum TipoOperador {
		// unários poloneses - 0 a 13 operadores (ver a função getOpePolones)
		SENO("SIN", "SEN"), COSSENO("COS"), TANGENTE("TAN", "TG"), ARCOSSENO("ASIN", "ARCSEN"),
		ARCOCOSSENO("ACOS", "ARCCOS"), ARCOTANGENTE("ATAN", "ARCTG"), RAIZ_QUADRADA("RAIZ", "SQRT"),
		RAIZ_CUBICA("RAIZ3", "CBRT"), EXPONENCIAL("EXP"), LOGARITMO_NATURAL("LN"), LOGARITMO10("LOG10"),
		TETO("TETO", "CEILING"), CHAO("CHAO", "FLOOR"), MODULO("MOD", "ABS")
		// operadores binários poloneses - 14 a 17 (ver a função getOpePolones)
		, LOGARITMO("LOG"), ARCOTANGENTE2("ATAN2"), RADICIACAO("RAIZN"), HYPOT("HIPOT")
		// operadores n-ários poloneses - 18 a 20 (ver a função getOpePolones)
		, MEDIA("AVERAGE", "MEDIA"), MAX("MAX"), MIN("MIN")
		// operadores não-poloneses binários e n-ários resolvidos
		, ADICAO("+"), MULTIPLICACAO("\u00D7"), SUBTRACAO("\u002D"), DIVISAO("\u00F7"), POTENCIA("^");

		private String[] ops;

		private TipoOperador(String... ops) {
			this.ops = ops;
		}

		/**
		 * Função que retorna o item da enumeração {@link TipoOperador TipoOperador}
		 * associado a um dado operador onde o símbolo vem antes dos operadores (notação
		 * polonesa)
		 * 
		 * @param exp  expressão
		 * @param args número de argumentos do operador polonês (1 para operadores
		 *             unários, 2 para binários, 3 ou mais para n-ários)
		 * @return vetor de objetos, sendo que na primeira posição está o item da
		 *         enumeração <code>TipoOperador</code>, na segunda estão os caracteres
		 *         encontrados
		 */
		public static TipoOperador getOpePolones(String exp, int args) {
			// ajustar faixa de operadores procurados em função do tipo
			int beg = args == 1 ? 0 : args == 2 ? LOGARITMO.ordinal() : MEDIA.ordinal();
			int end = args == 1 ? LOGARITMO.ordinal() : args == 2 ? MEDIA.ordinal() : values().length;
			// varrer lista
			for (int i = beg; i < end; i++) {
				TipoOperador t = TipoOperador.values()[i];
				if (t.isOperador(exp))
					return t;
			}
			return null;
		}

		/**
		 * Função que procura na lista de possíveis formas como pode se escrever um dado
		 * operador se ele é o que está no início de uma dada expressão
		 * 
		 * @param exp expressão
		 * @return forma como está escrito um dado operador
		 */
		private boolean isOperador(String exp) {
			for (String op : ops) {
				if (exp.equalsIgnoreCase(op))
					return true;
			}
			return false;
		}
	}

	/**
	 * Construtor do operador
	 * 
	 * @param operador tipo de operação que o objeto efetuará com os operandos
	 */
	public Operador(TipoOperador operador) {
		this.operador = operador;
	}

	public TipoOperador getOperador() {
		return this.operador;
	}

	@Override
	public String toString() {
		return operador.ops[0];
	}

	public void setProfundidade(int profundidade) {
		super.profundidade = profundidade;
	}

	public abstract void setPosicoes(int x);

	public abstract Valor getOperando(int i);

	public abstract int getOperandosCount();
}