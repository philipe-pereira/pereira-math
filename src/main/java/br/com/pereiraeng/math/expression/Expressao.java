package br.com.pereiraeng.math.expression;

import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;

import br.com.pereiraeng.math.expression.continuous.Binario;
import br.com.pereiraeng.math.expression.continuous.Constante;
import br.com.pereiraeng.math.expression.continuous.Constante.Const;
import br.com.pereiraeng.math.expression.continuous.N_ario;
import br.com.pereiraeng.math.expression.continuous.Operador;
import br.com.pereiraeng.math.expression.continuous.Operador.TipoOperador;
import br.com.pereiraeng.math.expression.continuous.Parametro;
import br.com.pereiraeng.math.expression.continuous.Unario;
import br.com.pereiraeng.math.expression.continuous.Valor;
import br.com.pereiraeng.math.expression.continuous.Variavel;
import br.com.pereiraeng.math.expression.continuous.VariavelIntermed;

/**
 * Classe do objeto que representa uma função matemática que transforma números
 * em outro número
 * 
 * @author Philipe PEREIRA
 *
 */
public class Expressao implements Icon {

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
	private Valor root;

	/**
	 * Folhas da árvore: variáveis
	 */
	private Variavel[] xs;

	/**
	 * Folhas da árvore: parâmetros
	 */
	private Parametro[] as;

	/**
	 * Sequência de caracteres com as pontuações que podem aparecer na expressão
	 */
	public static final String PUNCT = "\\(\\)\\+\\-\\*/;:";

	/**
	 * Construtor do objeto Expressão onde as varíaveis e os parâmetros estão na
	 * forma padronizadas (i.e. iniciados respectivamente com 'x' e 'a')
	 * 
	 * @param expressao expressão regular
	 */
	public Expressao(String expressao) {
		this(locateValues(expressao, true), locateValues(expressao, false), expressao);
	}

	/**
	 * Construtor do objeto Expressão onde as varíaveis e os parâmetros são
	 * indicados explicitamente
	 * 
	 * @param variaveis  vetor contendo sequências de caracteres designando
	 *                   variáveis
	 * @param parametros vetor contendo sequência de caracteres designando
	 *                   parâmetros
	 * @param expressao  expressão regular
	 */
	public Expressao(String[] variaveis, String[] parametros, String expressao) {
		// variáveis
		xs = new Variavel[variaveis.length];
		for (int i = 0; i < variaveis.length; i++)
			xs[i] = new Variavel(variaveis[i]);

		// parâmetros
		as = new Parametro[parametros.length];
		for (int i = 0; i < parametros.length; i++)
			as[i] = new Parametro(parametros[i]);

		// expressão original
		this.eo = expressao;

		// inicialmente, a extendida é igual a original
		this.ee = eo;

		// modificar a extendida, adicionando-lhe parênteses

		char[] operadores = new char[] { ';', '+', '-', '*', '/', '^' };

		// não se agrupa com parênteses elementos que são separados por
		// ponto-e-vírgula, por isso ele é o menos prioritário
		ee = adicionarParenteses(ee, operadores[5], operadores);
		ee = adicionarParenteses(ee, operadores[4], Arrays.copyOfRange(operadores, 0, 5));
		ee = adicionarParenteses(ee, operadores[3], Arrays.copyOfRange(operadores, 0, 3));
		ee = adicionarParenteses(ee, operadores[2], Arrays.copyOfRange(operadores, 0, 3));
		ee = adicionarParenteses(ee, operadores[1], Arrays.copyOfRange(operadores, 0, 1));

		// construir árvore
		root = construirArvore(ee, -1);

		float l = root.getLargura() * 2f / (xs.length + 1f);
		for (int i = 0; i < xs.length; i++) {
			xs[i].setLargura(Math.round((i + 1f) * l));
			xs[i].setProfundidade(this.getProfundidade());
		}

		root.setPosicao(0);
	}

	// ********* ESTABELECIMENTO DE VALORES PARA CÁLCULO **********

	/**
	 * Função que retorna o valor da expressão calculada para os valores dados para
	 * as variáveis
	 * 
	 * @param x vetor com valores de cada uma das variáveis da expressão
	 * @return
	 */
	public double f(double... x) {
		if (x.length != xs.length)
			throw new IllegalArgumentException("Número de argumentos enviados diferente dos da expressão");

		for (int i = 0; i < xs.length; i++)
			this.xs[i].setValue(x[i]);

		return root.getValor();
	}

	/**
	 * Função com a qual se estabelece os valores dos parâmetros da expressão
	 * 
	 * @param a valores dos parâmetros
	 */
	public void setAs(double... a) {
		if (a.length != as.length)
			throw new IllegalArgumentException("Número de parâmetros enviados diferente dos da expressão");

		for (int i = 0; i < as.length; i++)
			this.as[i].setValor(a[i]);
	}

	/**
	 * Função com a qual se estabelece os valores mínimos dos parâmetros da
	 * expressão
	 * 
	 * @param a vetor contendo os valores mínimos
	 */
	public void setAmin(double... a) {
		if (a.length != as.length)
			throw new IllegalArgumentException("Número de parâmetros enviados diferente dos da expressão");

		for (int i = 0; i < as.length; i++)
			this.as[i].setMin(a[i]);
	}

	/**
	 * Função com a qual se estabelece os valores máximos dos parâmetros da
	 * expressão
	 * 
	 * @param a vetor contendo os valores máximos
	 */
	public void setAmax(double... a) {
		if (a.length != as.length)
			throw new IllegalArgumentException("Número de parâmetros enviados diferente dos da expressão");

		for (int i = 0; i < as.length; i++)
			this.as[i].setMax(a[i]);
	}

	// ***************** PRÉ-EDIÇÃO DA EXPRESSÃO ******************
	// *** CONSTRUIR EXPRESSÃO EXTENDIDA ADICIONANDO PARÊNTESES ***

	/**
	 * Função que adiciona parênteses entre os blocos a serem operados
	 * 
	 * @param operador          operador da lista a ser isolado por parênteses
	 * @param menosPrioritarios lista de operadores menos prioritários (isto é, que
	 *                          não operados antes que aquele que está sendo
	 *                          isolado)
	 */
	public static String adicionarParenteses(String exp, char operador, char[] menosPrioritarios) {
		int i, j, k;

		for (j = 0; j < exp.length(); j++) {
			if (exp.charAt(j) == operador) {

				i = localisarOperador(exp, menosPrioritarios, false, false, j - 1, 0) + 1;

				k = localisarOperador(exp, menosPrioritarios, false, true, j + 1, -1) - 1;

				if (i > 0 && k < exp.length() - 1) {
					// se já não houver parênteses
					if (exp.charAt(i - 1) != '(' || exp.charAt(k + 1) != ')')
						exp = adicionarParenteses(exp, i, k);
				} else {
					exp = adicionarParenteses(exp, i, k);
				}
				j++;
			}
		}

		return exp;
	}

	/**
	 * Função que adiciona parênteses entre dois determinados pontos de uma
	 * expressão
	 * 
	 * @param exp expressão onde serão incluídos os parênteses
	 * @param i   posição do abre parênteses
	 * @param k   posição do fecha parênteses
	 * @return expressão com os parênteses incluídos
	 */
	public static String adicionarParenteses(String exp, int i, int k) {
		return (exp.substring(0, i) + "(" + exp.substring(i, k + 1) + ")" + exp.substring(k + 1, exp.length()));
	}

	// *************** LOCALISAÇÃO DOS OPERADORES *****************

	/**
	 * Função que retorna a posição do operador mais próximo de uma parte da
	 * sequência de caracteres numa mesma profundidade.
	 * 
	 * @param exp        sequência de caracteres
	 * @param operadores operadores procurados
	 * @param dentro     se <code>true</code>, ignora o primeiro e o último
	 *                   caracteres.
	 * @param direcao    se <code>true</code>, busca pela direita, se
	 *                   <code>false</code>, busca pela esquerda
	 * @param comeco     posição inicial da busca
	 * @param fim        posição final da busca
	 * @return posição do operador procurado
	 */
	public static int localisarOperador(String exp, char[] operadores, boolean dentro, boolean direcao, int comeco,
			int fim) {
		int i = 0;
		int sentido = (direcao ? 1 : -1);
		int deep = 0;
		fim = (fim == -1 ? exp.length() : fim);

		for (i = comeco + (dentro ? 1 : 0); ((i < fim - (dentro ? 1 : 0)) && direcao)
				|| ((i >= fim + (dentro ? 1 : 0)) && (!direcao)); i += sentido) {
			char c = exp.charAt(i);
			if (c == '(')
				deep += (direcao ? 1 : -1);
			else if (c == ')')
				deep += (direcao ? -1 : 1);
			boolean stop = false;
			for (char m : operadores)
				stop |= (c == m);
			if ((stop && deep == 0) || deep < 0)
				break;
		}
		return i;
	}

	/**
	 * Função que retorna as posiçoes dos operadores de uma dada expressão que estão
	 * numa mesma profundidade.
	 * 
	 * @param exp    sequência de caracteres da expressão
	 * @param op     operador procurado
	 * @param dentro <code>true</code> se o primeiro e o último caracteres serão
	 *               ignorados
	 * @return lista com as posições do operador
	 */
	public static ArrayList<Integer> localisarOperadores(String exp, char op, boolean dentro) {
		int deep = 0;
		ArrayList<Integer> posicaoOperador = new ArrayList<Integer>();
		for (int i = dentro ? 1 : 0; i < exp.length() - (dentro ? 1 : 0); i++) {
			char c = exp.charAt(i);
			if (c == '(')
				deep++;
			else if (c == ')')
				deep--;
			if ((c == op && deep == 0) || deep < 0)
				posicaoOperador.add(i);
		}
		return posicaoOperador;
	}

	/**
	 * Função que localiza o primeiro abre parênteses da expressão, retornando a
	 * indicação do operador que vem antes dele
	 * 
	 * @param exp expressão
	 * @return indicação do operador polonês procurado
	 */
	private String getOperadorPolones(String exp) {
		int i = exp.indexOf('(');
		if (i != -1)
			return exp.substring(0, i);
		return null;
	}

	// *********** CONSTRUÇÃO DA ÁRVORE DA EXPRESSÃO **************

	/**
	 * Função que é chamada recursivamente de modo a construir a árvore da expressão
	 * 
	 * @param exp          trecho da expressão a ser lido
	 * @param profundidade profundidade da recursão
	 * @return nó da árvore
	 */
	private Valor construirArvore(String exp, int profundidade) {
		int i = 0;
		Valor v = null;

		// ************ OPERADORES NÃO-POLONESES ************

		// soma
		ArrayList<Integer> posicaoOperador = localisarOperadores(exp, '+', true);

		if (posicaoOperador.size() != 0) {
			VariavelIntermed vi = new VariavelIntermed(new N_ario(Operador.TipoOperador.ADICAO), profundidade + 1);

			int corte = 1;
			for (i = 0; i < posicaoOperador.size(); i++) {
				((N_ario) vi.getFilho())
						.setOperando(construirArvore(exp.substring(corte, posicaoOperador.get(i)), profundidade + 1));
				corte = posicaoOperador.get(i) + 1;
			}
			((N_ario) vi.getFilho())
					.setOperando(construirArvore(exp.substring(corte, exp.length() - 1), profundidade + 1));

			v = vi;
		}

		// subtração
		i = localisarOperador(exp, new char[] { '-' }, true, true, 0, -1);
		if (i < exp.length() - 1) {
			VariavelIntermed vi = new VariavelIntermed(new Binario(Operador.TipoOperador.SUBTRACAO), profundidade + 1);

			String minuendo = exp.substring(1, i);
			Valor m1, m2;
			((Binario) vi.getFilho())
					.setOperando1(m1 = construirArvore((minuendo.equals("") ? "0" : minuendo), profundidade + 1));
			((Binario) vi.getFilho())
					.setOperando2(m2 = construirArvore(exp.substring(i + 1, exp.length() - 1), profundidade + 1));

			if (m1 instanceof Constante && m2 instanceof Constante)
				// se for uma expressão que contenha (-5), por exemplo, esta
				// será traduzida como 0-5, que é o mesmo que uma constante
				// igual a -5. PS: dá para estender essa linha de pensamento a
				// todas os operadores: sempre que todos os argumentos de uma
				// função for constante, a variável intermediária também será
				// contante, podendo ser substituída por uma constante
				v = new Constante(m1.getValor() - m2.getValor(), profundidade + 1);
			else
				v = vi;
		}

		// multiplicação
		posicaoOperador = localisarOperadores(exp, '*', true);
		if (posicaoOperador.size() != 0) {
			VariavelIntermed vi = new VariavelIntermed(new N_ario(Operador.TipoOperador.MULTIPLICACAO),
					profundidade + 1);

			int corte = 1;
			for (i = 0; i < posicaoOperador.size(); i++) {
				((N_ario) vi.getFilho())
						.setOperando(construirArvore(exp.substring(corte, posicaoOperador.get(i)), profundidade + 1));
				corte = posicaoOperador.get(i) + 1;
			}
			((N_ario) vi.getFilho())
					.setOperando(construirArvore(exp.substring(corte, exp.length() - 1), profundidade + 1));

			v = vi;
		}

		// divisão
		i = localisarOperador(exp, new char[] { '/' }, true, true, 0, -1);
		if (i < exp.length() - 1) {
			VariavelIntermed vi = new VariavelIntermed(new Binario(Operador.TipoOperador.DIVISAO), profundidade + 1);

			((Binario) vi.getFilho()).setOperando1(construirArvore(exp.substring(1, i), profundidade + 1));
			((Binario) vi.getFilho())
					.setOperando2(construirArvore(exp.substring(i + 1, exp.length() - 1), profundidade + 1));

			v = vi;
		}

		// potência
		i = localisarOperador(exp, new char[] { '^' }, true, true, 0, -1);
		if (i < exp.length() - 1) {
			VariavelIntermed vi = new VariavelIntermed(new Binario(Operador.TipoOperador.POTENCIA), profundidade + 1);

			((Binario) vi.getFilho()).setOperando1(construirArvore(exp.substring(1, i), profundidade + 1));
			((Binario) vi.getFilho())
					.setOperando2(construirArvore(exp.substring(i + 1, exp.length() - 1), profundidade + 1));

			v = vi;
		}

		// se for um operador polonês ou algum valor, não há necessidade
		// dos parênteses
		if (exp.startsWith("(") && exp.endsWith(")"))
			exp = exp.substring(1, exp.length() - 1);

		// *********** OPERADORES POLONESES ***********

		if (v == null) {

			// sequência de caracteres do operador polonês (tudo que vier antes
			// do parênteses)
			String sop = getOperadorPolones(exp);

			if (sop != null) {

				// procurar pelo operador do início da sentença na lista de
				// unários
				TipoOperador t = TipoOperador.getOpePolones(sop, 1);

				if (t != null) {
					// **** se for um operador unário ****

					VariavelIntermed vi = new VariavelIntermed(new Unario(t), profundidade + 1);

					((Unario) vi.getFilho())
							.setOperando(construirArvore(exp.substring(sop.length(), exp.length()), profundidade + 1));

					v = vi;
				}

				// procurar pelo operador do início da sentença na lista de
				// binários
				t = TipoOperador.getOpePolones(sop, 2);

				if (t != null) {
					// **** se for um operador binário ****

					VariavelIntermed vi = new VariavelIntermed(new Binario(t), profundidade + 1);

					// argumentos separados por uma vírgula
					String arg = exp.substring(sop.length() + 1, exp.length() - 1);

					// localizar a vírgula que separa os argumentos
					i = localisarOperador(arg, new char[] { ';' }, false, true, 0, -1);

					((Binario) vi.getFilho()).setOperando1(construirArvore(arg.substring(0, i), profundidade + 1));
					((Binario) vi.getFilho())
							.setOperando2(construirArvore(arg.substring(i + 1, arg.length()), profundidade + 1));

					v = vi;
				}

				// procurar pelo operador do início da sentença na lista de
				// n-ários
				t = TipoOperador.getOpePolones(sop, 3);

				if (t != null) {
					// **** se for um operador n-ário ****

					VariavelIntermed vi = new VariavelIntermed(new N_ario(t), profundidade + 1);

					// argumentos separados por uma vírgula
					String arg = exp.substring(sop.length() + 1, exp.length() - 1);

					posicaoOperador = localisarOperadores(arg, ';', false);

					int corte = 0;
					if (posicaoOperador.size() == 0) {
						// se o n-ário é utilizado como unário (besteiras que
						// podem ocorrer, como por exemplo achar o máximo de uma
						// só expressão, que será ela mesma)

						// neste caso, devolve os parênteses
						arg = "(" + arg + ")";
					} else {
						for (i = 0; i < posicaoOperador.size(); i++) {
							((N_ario) vi.getFilho()).setOperando(
									construirArvore(arg.substring(corte, posicaoOperador.get(i)), profundidade + 1));
							corte = posicaoOperador.get(i) + 1;
						}
					}
					((N_ario) vi.getFilho())
							.setOperando(construirArvore(arg.substring(corte, arg.length()), profundidade + 1));

					v = vi;
				}
			}
		}

		// **************** VALORES ****************

		// variável
		if (v == null) {
			Variavel var = getVariavel(exp);

			if (var != null) {
				var.setProfundidade(profundidade + 2);
				v = var;
			}
		}

		// parâmetro
		if (v == null) {

			Parametro par = getParametro(exp);

			if (par != null) {
				par.setProfundidade(profundidade + 1);
				v = par;
			}
		}

		// constante
		if (v == null) {
			// ver se é uma constante cadastrada (pi, número de euler, etc.)
			Const cte = Const.getConstante(exp);

			if (cte != null)
				v = new Constante(cte, profundidade + 1);
			else {
				double value = Double.NaN;
				try {
					value = Double.parseDouble(exp.replace(',', '.'));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				if (!Double.isNaN(value))
					v = new Constante(value, profundidade + 1);
			}
		}

		// se der tudo errado...
		if (v == null)
			throw new IllegalArgumentException(eo + " não é uma expressão válida");

		return v;
	}

	// ************** GETTERS **************

	public String getExpresaoOriginal() {
		return this.eo;
	}

	public String getExpresaoExtendida() {
		return this.ee;
	}

	public Valor getRoot() {
		return this.root;
	}

	/**
	 * Função que retonra no número total de variáveis da expressão
	 * 
	 * @return número inteiro que indica o número de variáveis desta expressão
	 */
	public int getValuesCount() {
		return this.xs.length;
	}

	/**
	 * Função que retorna o nome de uma variável numa dada posição
	 * 
	 * @param i inteiro que indica a posição
	 * @return sequência de caracteres que designa a variável
	 */
	public String getValues(int i) {
		return this.xs[i].toString();
	}

	public int getParamsCount() {
		return this.as.length;
	}

	public int getProfundidade() {
		int max = Integer.MIN_VALUE;
		for (Variavel x : xs) {
			int depth = x.getProfundidade();
			if (depth > max)
				max = depth;
		}
		return max;
	}

	// ************** AUXILIAR - BUSCA VARIÁVEIS **************

	private static final Pattern PAR = Pattern.compile("(a|A)\\d*");

	/**
	 * Procura na expressão por todas variáveis (iniciadas com 'x') ou parâmetros
	 * (iniciados com 'a')
	 * 
	 * @param expressao sequência de caracteres da expressão
	 * @param v         se <code>true</code> a função procura por variáveis, senão
	 *                  por parâmetros
	 * @return vetor com as variáveis ou parâmetros achados ao longo da expressão
	 */
	private static String[] locateValues(String expressao, boolean v) {
		TreeSet<String> out = new TreeSet<>();

		Matcher m = (v ? Var.VAR : PAR).matcher(expressao);
		while (m.find())
			out.add(m.group().trim());

		return out.toArray(new String[out.size()]);
	}

	private Variavel getVariavel(String v) {
		for (int i = 0; i < xs.length; i++)
			if (v.equalsIgnoreCase(xs[i].toString()))
				return xs[i];
		return null;
	}

	private Parametro getParametro(String p) {
		for (int i = 0; i < as.length; i++)
			if (p.equalsIgnoreCase(as[i].toString()))
				return as[i];
		return null;
	}

	// ======================== GRAPHICS ========================
	
//	public static void main(String[] args) {
//		JFrame f = new JFrame("Expression");
//		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		f.setSize(800, 700);
//		
//		Expressao exp = new Expressao("4*sin(3*x1+1+pi)");
//		JLabel p = new JLabel(exp);
//		f.setContentPane(p);
//		f.setVisible(true);
//	}

	private static final int LARGURA = 50;

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		root.draw(g, LARGURA);
	}

	@Override
	public int getIconHeight() {
		return 125 * (getProfundidade() + 1);
	}

	@Override
	public int getIconWidth() {
		return 50 * root.getLargura() + 6;
	}
}