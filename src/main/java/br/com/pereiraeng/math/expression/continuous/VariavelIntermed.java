package br.com.pereiraeng.math.expression.continuous;

import java.awt.Graphics;

/**
 * Bloco de valor que caracteriza um valor intermediário no cálculo da função
 * 
 * @author Philipe PEREIRA
 *
 */
public class VariavelIntermed extends Valor {

	private Operador child;

	public VariavelIntermed(Operador child, int profundidade) {
		super(profundidade);
		super.valor = 0.;
		this.child = child;
		this.child.setProfundidade(profundidade);
	}

	@Override
	public double getValor() {
		return (super.valor = child.getValor());
	}

	public Operador getFilho() {
		return this.child;
	}

	@Override
	public int getLargura() {
		return child.getLargura();
	}

	@Override
	public void setPosicao(int x) {
		super.setPosicao(x);
		child.setPosicao(x);
		child.setPosicoes(x);
	}

	@Override
	public void draw(Graphics g, int l) {
		int x = (int) ((posicao + getLargura() / 2f) * l);
		int y1 = (int) ((profundidade + .2f) * (2.5f * l));
		int y2 = (int) ((profundidade + .7f) * (2.5f * l));

		g.drawLine(x, y1, x, y2);

		super.draw(g, l);
		child.draw(g, l);
	}

	@Override
	public String toString() {
		return "";
	}
}
