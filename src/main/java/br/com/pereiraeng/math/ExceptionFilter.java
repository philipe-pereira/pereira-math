package br.com.pereiraeng.math;

import java.util.Set;

/**
 * Classe dos objetos que fazem uma pós triagem em um grupo de elementos
 * 
 * @author Philipe PEREIRA
 *
 * @param <K>
 *            classe dos elementos filtrados
 */
public class ExceptionFilter<K> {

	/**
	 * exceções positivas (i.e., é para rejeitar tendo sido aceito)
	 */
	protected Set<K> ep;

	/**
	 * exceções negativas (i.e., é para aceitar tendo sido rejeitado)
	 */
	protected Set<K> en;

	/**
	 * Construtor do filtro de pós-triagem
	 * 
	 * @param ep
	 *            exceções positivas (i.e., é para rejeitar tendo sido aceito)
	 * @param en
	 *            exceções negativas (i.e., é para aceitar tendo sido rejeitado)
	 */
	public ExceptionFilter(Set<K> ep, Set<K> en) {
		this.ep = ep;
		this.en = en;
	}

	/**
	 * Função que avalia se o elemento deve ser aceito ou não após já ter sido
	 * avaliado (pós triagem - ver exceções)
	 * 
	 * @param filter
	 *            <code>true</code> se o elemento foi aceito pelo filtro (ver se
	 *            está nas exceções positivas), <code>false</code> se o elemento foi
	 *            rejeitado (ver se está nas exceções negativas)
	 * @param k
	 *            elemento a ser filtrado
	 * @return <code>true</code> para aceitar, <code>false</code> para rejeitar
	 */
	public boolean accept(boolean filter, K k) {
		return filter ? !ep.contains(k) : en.contains(k);
	}
}
