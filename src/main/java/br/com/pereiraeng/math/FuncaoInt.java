package br.com.pereiraeng.math;

/**
 * Interface que caracteriza as classes que possuem uma função numérica disposta
 * de uma forma padronizada
 * 
 * @author Philipe PEREIRA
 *
 */
public interface FuncaoInt extends Funcao {

	/**
	 * Função que calcula as variáveis intermediárias a partir do estado e das
	 * constantes
	 * 
	 * @param xt   argumentos x para um dado instante de tempo
	 * @param t    argumento t
	 * @param dt   passo de tempo
	 * @param ctes constantes
	 * @param vars variáveis intermediárias do cálculo a ser atualizadas
	 */
	public void refreshVars(double[] xt, double t, double dt, double[] ctes, double[] vars);

	/**
	 * Função que efetua o pós-calculo
	 * 
	 * @param xt   argumentos x para um dado instante de tempo
	 * @param t    argumento t
	 * @param vars variáveis intermediárias do cálculo
	 * @return vetor com os valores de saída da função pós-calculo
	 */
	public double[] gt(double[] xt, double t, double[] vars);

}
