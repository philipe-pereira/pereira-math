package br.com.pereiraeng.math.expression.continuous;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Objeto abstrato que pode tanto representar tanto um operador que retorna um
 * valor número quanto uma variável intermediária no cálculo de uma expressão
 * 
 * @author Philipe PEREIRA
 *
 */
public abstract class Bloco {
	
	/**
	 * abscissa
	 */
	protected transient int posicao;

	/**
	 * ordenada
	 */
	protected transient int profundidade;

	public abstract int getLargura();

	/**
	 * Função que retorna o valor correspondente e este elemento da expressão
	 * 
	 * @return valor numérico decimal
	 */
	public abstract double getValor();

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public int getPosicao() {
		return this.posicao;
	}

	public int getProfundidade() {
		return profundidade;
	}

	public void draw(Graphics g, int l) {
		// determinar deslocamento horizontal
		int x = (int) ((posicao + getLargura() / 2f - .5f) * l);

		// determinar profundidade e cor
		Color interna = null;
		int y = 0;
		if (this instanceof Valor) {
			y = (int) (profundidade * (2.5f * l));
			if (this instanceof Constante) {
				interna = Color.YELLOW;
			} else if (this instanceof VariavelIntermed) {
				interna = Color.CYAN;
			} else if (this instanceof Parametro) {
				interna = Color.GREEN;
			}
		} else if (this instanceof Operador) {
			y = (int) ((profundidade + .5f) * (2.5f * l));
			interna = Color.RED;
		}

		// preenchimento
		g.setColor(interna);
		g.fillRect(x, y, l, l);

		// borda
		g.setColor(Color.BLACK);
		g.drawRect(x, y, l, l);

		// símbolo
		g.drawString(this.toString(), x + l / 4, y + l / 2);
	}
}