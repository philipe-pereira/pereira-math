package br.com.pereiraeng.math.expression.continuous;

import java.awt.Graphics;

/**
 * Bloco de operação que transforma dois valores em um outro
 * 
 * @author Philipe PEREIRA
 *
 */
public class Binario extends Operador {

	public Valor operando1;

	public Valor operando2;

	public Binario(TipoOperador operador) {
		super(operador);
	}

	public void setOperando1(Valor valor) {
		operando1 = valor;
	}

	public void setOperando2(Valor valor) {
		operando2 = valor;
	}

	@Override
	public Valor getOperando(int i) {
		if (i == 0)
			return this.operando1;
		else
			return this.operando2;
	}

	@Override
	public int getOperandosCount() {
		return 2;
	}

	@Override
	public double getValor() {
		switch (operador) {
		case SUBTRACAO:
			return (operando1.getValor() - operando2.getValor());
		case DIVISAO:
			return (operando1.getValor() / operando2.getValor());
		case LOGARITMO:
			return Math.log(operando1.getValor()) / Math.log(operando2.getValor());
		case ARCOTANGENTE2:
			return Math.atan2(operando1.getValor(), operando2.getValor());
		case POTENCIA:
			return Math.pow(operando1.getValor(), operando2.getValor());
		case RADICIACAO:
			return Math.pow(operando1.getValor(), (1 / operando2.getValor()));
		case HYPOT:
			return Math.hypot(operando1.getValor(), operando2.getValor());
		default:
			return 0.0;
		}
	}

	@Override
	public int getLargura() {
		return operando1.getLargura() + operando2.getLargura();
	}

	@Override
	public void setPosicoes(int x) {
		operando1.setPosicao(x);
		operando2.setPosicao(x + operando1.getLargura());
	}

	@Override
	public void draw(Graphics g, int l) {

		int xo = (int) ((posicao + getLargura() / 2f) * l);
		int yo = (int) ((profundidade + .7f) * (2.5f * l));
		int yv = (int) ((profundidade + 1.2f) * (2.5f * l));
		int xv = 0;

		if (operando1 instanceof Variavel) {
			xv = (int) ((this.getPosicao() + operando1.getLargura() / 2f) * l);

			int xV = (int) ((((Variavel) operando1).getLarguraTotal() / 2f) * l);
			int yV = (int) ((operando1.getProfundidade() - .3f) * (2.5f * l));
			int yV1 = (int) ((operando1.getProfundidade() + .2f) * (2.5f * l));

			// linha operador -> linha sub operador
			g.drawLine(xo, yo, xv, yv);
			// linha sub operador -> linha sobre variáveis (vertical)
			g.drawLine(xv, yv, xv, yV);
			// linha sobre variáveis -> linha variáveis
			g.drawLine(xv, yV, xV, yV1);
		} else {
			xv = (int) ((operando1.getPosicao() + operando1.getLargura() / 2f) * l);
			g.drawLine(xo, yo, xv, yv);
		}
		operando1.draw(g, l);

		if (operando2 instanceof Variavel) {
			xv += (int) ((operando1.getLargura() + 1) * l / 2f);

			int xV = (int) ((((Variavel) operando2).getLarguraTotal() / 2f) * l);
			int yV = (int) ((operando2.getProfundidade() - .3f) * (2.5f * l));
			int yV1 = (int) ((operando2.getProfundidade() + .2f) * (2.5f * l));

			// linha operador -> linha sub operador
			g.drawLine(xo, yo, xv, yv);
			// linha sub operador -> linha sobre variáveis (vertical)
			g.drawLine(xv, yv, xv, yV);
			// linha sobre variáveis -> linha variáveis
			g.drawLine(xv, yV, xV, yV1);
		} else {
			xv = (int) ((operando2.getPosicao() + operando2.getLargura() / 2f) * l);
			g.drawLine(xo, yo, xv, yv);
		}
		operando2.draw(g, l);

		super.draw(g, l);
	}
}
