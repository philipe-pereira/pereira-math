package br.com.pereiraeng.math;

/**
 * Interface que caracteriza as classes que possuem uma função numérica disposta
 * de uma forma padronizada
 * 
 * @author Philipe PEREIRA
 *
 */
public interface Funcao {

	/**
	 * Função que retorna o valor de uma dada função para uma dada entrada
	 * 
	 * @param xt   argumentos x para um dado instante de tempo
	 * @param t    argumento t
	 * @param vars variáveis intermediárias do cálculo
	 * @return vetor com os valores de saída da função para uma dada entrada
	 */
	public double[] ft(double[] xt, double t, double[] vars);
}
