package br.com.pereiraeng.math.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.pereiraeng.math.expression.discret.OperadorB2B;
import br.com.pereiraeng.math.expression.discret.OperadorV2B;
import br.com.pereiraeng.math.expression.discret.ValorB;
import br.com.pereiraeng.math.expression.discret.VariavelB;
import br.com.pereiraeng.math.expression.discret.VariavelIntermediariaB;
import br.com.pereiraeng.math.expression.discret.VariavelNB;
import br.com.pereiraeng.math.expression.discret.OperadorV2B.TipoOperadorV2B;
import br.com.pereiraeng.math.expression.discret.VariavelNB.TypeValue;

/**
 * Classe do objeto que representa uma função booleana, ou seja, que retorna
 * valores <code>true</code> ou <code>false</code>
 * 
 * @author Philipe PEREIRA
 *
 */
public class ExpressaoB {

	/**
	 * Expressão original, antes da expansão com parênteses
	 */
	private String eo;

	/**
	 * Expressão extendida
	 */
	private String ee;

	/**
	 * Raiz da árvore da expressão
	 */
	private VariavelIntermediariaB root;

	/**
	 * Folhas da árvore: variáveis
	 */
	private Var[] xs;

	public ExpressaoB(String expressao, TypeValue... types) {
		this(locateValues(expressao), expressao, types);
	}

	public ExpressaoB(String[] variaveis, String expressao, TypeValue... types) {
		// confere número de tipos
		if (variaveis.length != types.length)
			throw new IllegalArgumentException(
					"Número de tipos enviados diferente do número de variáveis da expressão");

		// variáveis
		xs = new Var[variaveis.length];
		for (int i = 0; i < variaveis.length; i++) {
			TypeValue tv = types[i];
			if (tv != null)
				xs[i] = new VariavelNB(variaveis[i], tv);
			else
				xs[i] = new VariavelB(variaveis[i]);
		}

		// expressão original
		this.eo = expressao;

		// inicialmente, a extendida é igual a original
		this.ee = translateSymbols(eo);

		// modificar a extendida, adicionando-lhe parênteses
		char[] operadores = new char[] { '^', '|', '&', '~' };
		ee = Expressao.adicionarParenteses(ee, operadores[3], operadores);
		ee = Expressao.adicionarParenteses(ee, operadores[2], Arrays.copyOfRange(operadores, 0, 2));
		ee = Expressao.adicionarParenteses(ee, operadores[1], Arrays.copyOfRange(operadores, 0, 1));
		ee = Expressao.adicionarParenteses(ee, operadores[0], new char[] {});

		// construir árvore
		root = (VariavelIntermediariaB) construirArvore(ee);
	}

	// ********* ESTABELECIMENTO DE VALORES PARA CÁLCULO **********

	/**
	 * Função que retorna o valor da expressão calculada para os valores dados para
	 * as variáveis
	 * 
	 * @param x vetor com valores de cada uma das variáveis da expressão
	 * @return <code>true</code> ou <code>false</code>
	 */
	public boolean f(Object... x) {
		if (x.length != xs.length)
			throw new IllegalArgumentException("Número de argumentos enviados diferente dos da expressão");

		for (int i = 0; i < xs.length; i++)
			this.xs[i].setValue(x[i]);

		return root.getValue();
	}

	/**
	 * Função que retorna o valor da expressão calculada para os valores dados para
	 * as variáveis
	 * 
	 * @param x vetor com valores de cada uma das variáveis da expressão
	 * @return <code>true</code> ou <code>false</code>
	 */
	public boolean f(String... x) {
		if (x.length != xs.length)
			throw new IllegalArgumentException("Número de argumentos enviados diferente dos da expressão");

		for (int i = 0; i < xs.length; i++)
			((VariavelNB) this.xs[i]).setStringValue(x[i].trim());

		return root.getValue();
	}

	// *********** CONSTRUÇÃO DA ÁRVORE DA EXPRESSÃO **************

	/**
	 * Função que é chamada recursivamente de modo a construir a árvore da expressão
	 * 
	 * @param exp          trecho da expressão a ser lido
	 * @param profundidade profundidade da recursão
	 * @return nó da árvore
	 */
	private Object construirArvore(String exp) {
		int i = 0;
		Object v = null;
		exp = exp.trim();

		// ************ OPERADORES BOOLEANOS ************

		// ou exclusivo
		ArrayList<Integer> posicaoOperador = Expressao.localisarOperadores(exp, '^', true);
		if (posicaoOperador.size() != 0) {
			VariavelIntermediariaB vi = new VariavelIntermediariaB(new OperadorB2B(OperadorB2B.TipoOperadorB2B.XOR));

			int corte = 1;
			for (i = 0; i < posicaoOperador.size(); i++) {
				((OperadorB2B) vi.getChild())
						.setOperando((ValorB) construirArvore(exp.substring(corte, posicaoOperador.get(i))));
				corte = posicaoOperador.get(i) + 1;
			}
			((OperadorB2B) vi.getChild()).setOperando((ValorB) construirArvore(exp.substring(corte, exp.length() - 1)));

			v = vi;
		}

		// ou
		posicaoOperador = Expressao.localisarOperadores(exp, '|', true);
		if (posicaoOperador.size() != 0) {
			VariavelIntermediariaB vi = new VariavelIntermediariaB(new OperadorB2B(OperadorB2B.TipoOperadorB2B.OR));

			int corte = 1;
			for (i = 0; i < posicaoOperador.size(); i++) {
				((OperadorB2B) vi.getChild())
						.setOperando((ValorB) construirArvore(exp.substring(corte, posicaoOperador.get(i))));
				corte = posicaoOperador.get(i) + 1;
			}
			((OperadorB2B) vi.getChild()).setOperando((ValorB) construirArvore(exp.substring(corte, exp.length() - 1)));

			v = vi;
		}

		// e
		posicaoOperador = Expressao.localisarOperadores(exp, '&', true);
		if (posicaoOperador.size() != 0) {
			VariavelIntermediariaB vi = new VariavelIntermediariaB(new OperadorB2B(OperadorB2B.TipoOperadorB2B.AND));

			int corte = 1;
			for (i = 0; i < posicaoOperador.size(); i++) {
				((OperadorB2B) vi.getChild())
						.setOperando((ValorB) construirArvore(exp.substring(corte, posicaoOperador.get(i))));
				corte = posicaoOperador.get(i) + 1;
			}
			((OperadorB2B) vi.getChild()).setOperando((ValorB) construirArvore(exp.substring(corte, exp.length() - 1)));

			v = vi;
		}

		// não
		i = Expressao.localisarOperador(exp, new char[] { '~' }, true, true, 0, -1);
		if (i < exp.length() - 1) {
			VariavelIntermediariaB vi = new VariavelIntermediariaB(new OperadorB2B(OperadorB2B.TipoOperadorB2B.NOT));

			((OperadorB2B) vi.getChild()).setOperando((ValorB) construirArvore(exp.substring(i + 1)));

			v = vi;
		}

		// se algum valor, não há necessidade dos parênteses

		if (exp.startsWith("(") && exp.endsWith(")"))
			exp = exp.substring(1, exp.length() - 1);

		// ********* OPERADORES RELACIONAIS *********

		if (v == null) {
			TipoOperadorV2B t = TipoOperadorV2B.getOperator(exp);

			if (t != null) {
				VariavelIntermediariaB vi = new VariavelIntermediariaB(new OperadorV2B(t));

				String[] args = exp.split(t.op);

				((OperadorV2B) vi.getChild()).setOperando((VariavelNB) construirArvore(args[0]), 0);

				switch (t) {
				case BETWEEN:
				case LIKE:
				case REGEXP:
				case IN:
					((OperadorV2B) vi.getChild()).setModifier(args[1]);
					break;
				default:
					((OperadorV2B) vi.getChild()).setOperando((VariavelNB) construirArvore(args[1]), 1);
					break;
				}

				v = vi;
			}
		}

		// **************** VALORES ****************

		// variável
		if (v == null) {
			Var var = getVariavel(exp.trim());

			if (var != null)
				v = var;
		}

		// constante
		if (v == null) {
			VariavelNB var = new VariavelNB(exp);

			if (var != null)
				v = var;
		}

		return v;
	}

	// ************** GETTERS **************

	public String getExpresaoOriginal() {
		return eo;
	}

	public String getExpresaoExtendida() {
		return ee;
	}

	public int getValuesCount() {
		return xs.length;
	}

	public Var getVar(int i) {
		return xs[i];
	}

	// ************** AUXILIAR - BUSCA VARIÁVEIS **************

	/**
	 * Procura na expressão por todas variáveis (iniciadas com 'x')
	 * 
	 * @param expressao sequência de caracteres da expressão
	 * @return vetor com as variáveis ou parâmetros achados ao longo da expressão
	 */
	private static String[] locateValues(String expressao) {
		TreeSet<String> out = new TreeSet<>();

		Matcher m = Var.VAR.matcher(expressao);
		while (m.find())
			out.add(m.group().trim());

		return out.toArray(new String[out.size()]);
	}

	private Var getVariavel(String v) {
		for (int i = 0; i < xs.length; i++)
			if (v.equalsIgnoreCase(xs[i].toString()))
				return xs[i];
		return null;
	}

	// ************** AUXILIAR - TRADUZIR SÍMBOLOS **************

	private static final Pattern BET = Pattern.compile("BETWEEN.+?AND");

	private static final Pattern NL = Pattern.compile("(\\(| )[\\p{Alnum}_-]++( )+NOT LIKE");

	private static String translateSymbols(String expression) {

		// o operador BETWEEN no sql utiliza AND para indicar o intervalor. AND
		// É RESERVADO PARA OS BOOLEANOS, LOGO AO INVÉS DE: a BETWEEN b AND c;
		// teremos: a @ b / c
		Matcher m = BET.matcher(expression);
		while (m.find()) {
			expression = expression.substring(0, m.start()) + "@" + expression.substring(m.start() + 7, m.end() - 3)
					+ "/" + expression.substring(m.end());
		}

		// o operador LIKE é negado com o NOT depois do argumento, o que foge do
		// padrão da negação. Tem-se: a NOT LIKE [pattern]; teremos: NOT a LIKE
		// [pattern]
		m = NL.matcher(expression);
		while (m.find()) {
			String[] b = m.group().trim().split(" ");
			expression = expression.substring(0, m.start()) + " " + b[1] + " " + b[0] + " " + b[2]
					+ expression.substring(m.end());
		}

		// substitui os operadores escritos pelos símbolos
		expression = expression.replace(" AND ", " & ").replace(" OR ", " | ").replace(" XOR ", " ^ ")
				.replaceAll(" NOT ", " ~ ").replace("(NOT ", "(~ ");
		return expression;
	}
}
