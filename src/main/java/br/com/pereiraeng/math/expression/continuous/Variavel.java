package br.com.pereiraeng.math.expression.continuous;

import java.awt.Color;
import java.awt.Graphics;

import br.com.pereiraeng.math.expression.Var;

/**
 * Bloco de valor que caracteriza a abscissa da função
 * 
 * @author Philipe PEREIRA
 *
 */
public class Variavel extends Valor implements Var {

	private String x;

	public Variavel(String x) {
		super(-1);
		super.valor = 0.0;
		this.x = x;
	}

	@Override
	public void setValue(Object valor) {
		super.valor = (double) valor;
	}

	@Override
	public double getValor() {
		return super.valor;
	}

	/**
	 * faixa do eixo horizontal ocupada pelo bloco
	 */
	private transient int largura = 0;

	@Override
	public int getLargura() {
		return 1;
	}

	public int getLarguraTotal() {
		return largura;
	}

	public void setLargura(int largura) {
		this.largura = largura;
	}

	@Override
	public String toString() {
		return x;
	}

	public void setProfundidade(int profundidade) {
		// a profundidade da variável é a mais alta daquelas que lhe foram
		// passadas (de modo que ela fique na última linha sempre)
		if (profundidade > super.profundidade)
			super.profundidade = profundidade;
	}

	@Override
	public void setPosicao(int posicao) {
		super.posicao = 0;
	}

	@Override
	public void draw(Graphics g, int l) {
		int x = (int) ((largura / 2f - .5f) * l);
		int y = (int) (profundidade * (2.5f * l));

		// preenchimento
		g.setColor(Color.ORANGE);
		g.fillRect(x, y, l, l);

		// borda
		g.setColor(Color.BLACK);
		g.drawRect(x, y, l, l);

		// símbolo
		g.drawString(this.toString(), x + l / 4, y + l / 2);
	}
}