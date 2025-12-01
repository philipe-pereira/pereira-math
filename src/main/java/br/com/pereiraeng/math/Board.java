package br.com.pereiraeng.math;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Classe do objeto que representa um tabuleiro quadrado, em que as coordenadas
 * inteiras das abscissas ou das ordenadas podem estar
 * {@link #contains(int, int) ocupadas ou não}
 * 
 * @author Philipe PEREIRA
 *
 */
public class Board extends HashMap<Integer, HashSet<Integer>> {
	private static final long serialVersionUID = 1L;

	private int xmax;
	private int ymax;

	public Board(int xmax, int ymax) {
		this.xmax = xmax;
		this.ymax = ymax;
	}

	public int getXmax() {
		return xmax;
	}

	public int getYmax() {
		return ymax;
	}

	public void add(int x0, int y0) {
		HashSet<Integer> row = this.get(x0);
		if (row == null)
			this.put(x0, row = new HashSet<Integer>());
		row.add(y0);
	}

	/**
	 * Função que indica se uma dada posição, designada pelas suas coordenadas
	 * inteiras, está ocupada ou não
	 * 
	 * @param x abscissa inteira
	 * @param y ordenada inteira
	 * @return <code>true</code> se estiver ocupada, <code>false</code> se não
	 */
	public boolean contains(int x, int y) {
		if (x < 0 || x >= xmax || y < 0 || y >= ymax)
			return false;
		return containsKey(x) ? this.get(x).contains(y) : false;
	}

	// --------------------------------------------------------------

	/**
	 * Função que retorna uma casa vaga nos arredores de um dado ponto
	 * 
	 * @param x0 abscissa do ponto entorno do qual se procura uma casa vaga
	 * @param y0 ordenada do ponto entorno do qual se procura uma casa vaga
	 * @return vetor com duas posições indicando a casa vaga
	 */
	public int[] getEmptyPos(int x0, int y0) {
		return getEmptyPos(x0, y0, this);
	}

	/**
	 * Função que retorna uma casa vaga nos arredores de um dado ponto
	 * 
	 * @param x0 abscissa do ponto entorno do qual se procura uma casa vaga
	 * @param y0 ordenada do ponto entorno do qual se procura uma casa vaga
	 * @param b  objeto que representa o tabuleiro
	 * @return vetor com duas posições indicando a casa vaga
	 */
	public static int[] getEmptyPos(int x0, int y0, Board b) {
		int d = (int) Math.floor(Math.random() * 8);
		int i = 0;
		int k = 0;
		while (k < 20) {
			int dx = 0, dy = 0;
			switch (d) {
			case 0:
				x0 += 1;
				break;
			case 1:
				x0 += 1;
				y0 += 1;
				break;
			case 2:
				y0 += 1;
				break;
			case 3:
				x0 -= 1;
				y0 += 1;
				break;
			case 4:
				x0 -= 1;
				break;
			case 5:
				x0 -= 1;
				y0 -= 1;
				break;
			case 6:
				y0 -= 1;
				break;
			case 7:
				x0 += 1;
				y0 -= 1;
				break;
			}
			int newX0 = x0 + dx;
			int newY0 = y0 + dy;

			boolean occupied = b.contains(newX0, newY0);
			if (occupied) {
				i++;
				if (i == 8) {
					x0 = newX0;
					y0 = newY0;

					d = (int) Math.floor(Math.random() * 8);
					i = 0;
					k++;
				} else {
					d = (d + 1) % 8;
				}
			} else
				return new int[] { newX0, newY0 };
		}
		return getEmptyPos(b);
	}

	/**
	 * Função que retorna uma casa vaga numa posição aleatória
	 * 
	 * @return vetor com duas posições indicando a casa vaga
	 */
	public int[] getEmptyPos() {
		return getEmptyPos(this);
	}

	/**
	 * Função que retorna uma casa vaga numa posição aleatória
	 * 
	 * @param b objeto que representa o tabuleiro
	 * @return vetor com duas posições indicando a casa vaga
	 */
	public static int[] getEmptyPos(Board b) {
		while (true) {
			int x = (int) Math.floor(Math.random() * b.getXmax());
			int y = (int) Math.floor(Math.random() * b.getYmax());
			if (!b.contains(x, y))
				return new int[] { x, y };
		}
	}
}
