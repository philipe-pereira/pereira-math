package br.com.pereiraeng.math;

import java.util.ArrayList;

/**
 * <p>
 * Classe dos objetos que representam uma matriz quadrada simétrica.
 * </p>
 * 
 * <p>
 * Uma vez que a matriz é simétrica, não é necessário guardar os elementos
 * abaixo da diagonal principal (pois são iguais àqueles acima), de modo que é
 * necessário menos espaço para guardar os elementos desta matriz (somente
 * N*(N+1)/2, ao invés de N^2).
 * </p>
 * 
 * @author Philipe PEREIRA
 *
 */
public class SymMatrix {
	private ArrayList<Double> sym;

	private int order;

	/**
	 * Construtor da matriz simétrica
	 * 
	 * @param order
	 *            tamanho da matriz
	 */
	public SymMatrix(int order) {
		this.setOrder(order);
	}

	/**
	 * Função que retorna o tamanho da matriz
	 * 
	 * @return tamanho da matriz
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Função que altera o tamanho da matriz
	 * 
	 * @param order
	 *            novo tamanho da matriz
	 */
	public void setOrder(int order) {
		this.order = order;
		int params = order * (order + 1) / 2;

		if (sym == null)
			sym = new ArrayList<>(params);
		else
			sym.ensureCapacity(params);

		while (params != sym.size()) {
			if (sym.size() < params)
				sym.add(new Double(0.));
			else
				sym.remove(sym.size() - 1);
		}
	}

	public double[][] getMatriz() {
		double[][] out = new double[this.order][];
		for (int i = 0; i < this.order; i++) {
			out[i] = new double[i + 1];
			for (int j = 0; j <= i; j++)
				out[i][j] = this.sym.get(getIndex(i, j));
		}
		return out;
	}

	public void setMatriz(double[][] m) {
		for (int i = 0; i < this.order; i++)
			for (int j = 0; j <= i; j++)
				set(i, j, m[i][j]);
	}

	/**
	 * Função que retorna um valor da matriz
	 * 
	 * @param index1
	 *            índice da linha (ou da coluna)
	 * @param index2
	 *            índice da coluna (ou da linha)
	 * @return valor procurado
	 */
	public double get(int index1, int index2) {
		if (index1 == index2)
			return getPri(index1);
		else
			return getNotPri(index1, index2);
	}

	/**
	 * Função que retorna o valor localizado na diagonal principal
	 * 
	 * @param index
	 *            índice da linha (ou da coluna, na diagonal são iguais)
	 * @return valor procurado
	 */
	public double getPri(int index) {
		if (index >= 0 && index < this.order)
			return sym.get(index);
		else
			return Double.NaN;
	}

	/**
	 * Função que retorna o valor localizado fora da diagonal principal
	 * 
	 * @param index1
	 *            índice da linha (ou da coluna)
	 * @param index2
	 *            índice da coluna (ou da linha)
	 * @return valor procurado
	 */
	public double getNotPri(int index1, int index2) {
		if (index1 >= 0 && index1 < this.order && index2 >= 0 && index2 < this.order && index1 != index2)
			return sym.get(getIndex(index1, index2));
		else
			return Double.NaN;
	}

	public void set(int index1, int index2, double value) {
		if (index1 == index2)
			setPri(index1, value);
		else
			setNotPri(index1, index2, value);
	}

	/**
	 * Função que estabelece um valor na diagonal principal
	 * 
	 * @param index
	 *            índice da linha (ou da coluna, na diagonal são iguais)
	 * @param value
	 *            valor indicado
	 */
	public void setPri(int index, double value) {
		if (index >= 0 && index < this.order)
			sym.set(index, value);
	}

	/**
	 * Função que estabelece um valor fora da diagonal principal
	 * 
	 * @param index1
	 *            índice da linha (ou da coluna)
	 * @param index2
	 *            índice da coluna (ou da linha)
	 * @param value
	 *            valor indicado
	 */
	public void setNotPri(int index1, int index2, double value) {
		if (index1 >= 0 && index1 < this.order && index2 >= 0 && index2 < this.order && index1 != index2)
			sym.set(getIndex(index1, index2), value);
	}

	/**
	 * Função que retorna a posição do valor na lista a partir do índice da
	 * linha e da coluna. É a função inversa de {@link SymMatrix#getIndex1(int)}
	 * e {@link SymMatrix#getIndex2(int)}.
	 * 
	 * @param index1
	 *            índice da linha (ou da coluna)
	 * @param index2
	 *            índice da coluna (ou da linha)
	 * @return índice do valor na lista
	 */
	protected int getIndex(int index1, int index2) {
		if (index1 == index2) {
			// item da diagonal principal
			return index1;
		} else {
			// fora da diagonal principal
			int min = Math.min(index1, index2);
			int max = min == index1 ? index2 : index1;

			// pula as indutâncias próprias (windings) +
			// pula as colunas abaixo da diagonal +
			// conta a linha
			int index = this.order + ((2 * this.order - 2 - (min - 1)) * min) / 2 + (max - min - 1);
			return index;
		}
	}

	/**
	 * Função que retorna o índice da linha a partir da posição do valor na
	 * lista. É a função inversa de {@link SymMatrix#getIndex(int, int)}.
	 * 
	 * @param index
	 *            índice do valor na lista
	 * @return índice da linha (ou da coluna)
	 */
	protected int getIndex1(int index) {
		if (index < order) {
			// item da diagonal principal
			return index;
		} else {
			// fora da diagonal principal
			index -= order;
			int o = order - 1;
			int out = 0;
			while (index >= o) {
				index -= o;
				o--;
				out++;
			}
			return out;
		}
	}

	/**
	 * Função que retorna o índice da coluna a partir da posição do valor na
	 * lista. É a função inversa de {@link SymMatrix#getIndex(int, int)}.
	 * 
	 * @param index
	 *            índice do valor na lista
	 * @return índice da coluna (ou da linha)
	 */
	protected int getIndex2(int index) {
		if (index < order) {
			// item da diagonal principal
			return index;
		} else {
			// fora da diagonal principal
			int o = order - 1;
			index -= o;
			while (index > o) {
				index -= o;
				o--;
			}
			return (order - o) + index - 1;
		}
	}
}
