package br.com.pereiraeng.math.expression.continuous;

import java.awt.Graphics;

/**
 * Bloco de operação que transforma um somente valor em um outro.
 * 
 * @author Philipe PEREIRA
 *
 */
public class Unario extends Operador {

	public Valor operando;

	public Unario(TipoOperador operador) {
		super(operador);
	}

	public void setOperando(Valor valor) {
		operando = valor;
	}

	@Override
	public Valor getOperando(int i) {
		return this.operando;
	}

	@Override
	public int getOperandosCount() {
		return 1;
	}

	@Override
	public double getValor() {
		switch (operador) {
		case SENO:
			return Math.sin(operando.getValor());
		case COSSENO:
			return Math.cos(operando.getValor());
		case TANGENTE:
			return Math.tan(operando.getValor());
		case ARCOSSENO:
			return Math.asin(operando.getValor());
		case ARCOCOSSENO:
			return Math.acos(operando.getValor());
		case ARCOTANGENTE:
			return Math.atan(operando.getValor());
		case MODULO:
			return Math.abs(operando.getValor());
		case RAIZ_QUADRADA:
			return Math.sqrt(operando.getValor());
		case EXPONENCIAL:
			return Math.exp(operando.getValor());
		case LOGARITMO_NATURAL:
			return Math.log(operando.getValor());
		case LOGARITMO10:
			return Math.log10(operando.getValor());
		case RAIZ_CUBICA:
			return Math.cbrt(operando.getValor());
		case TETO:
			return Math.ceil(operando.getValor());
		case CHAO:
			return Math.floor(operando.getValor());
		default:
			return 0.0;
		}
	}

	@Override
	public int getLargura() {
		return operando.getLargura();
	}

	@Override
	public void setPosicoes(int x) {
		operando.setPosicao(x);
	}

	@Override
	public void draw(Graphics g, int l) {
		super.draw(g, l);

		int xo = (int) ((posicao + getLargura() / 2f) * l);
		int yo = (int) ((profundidade + .7f) * (2.5f * l));
		int yv = (int) ((profundidade + 1.2f) * (2.5f * l));
		int xv = (int) ((this.getPosicao() + operando.getLargura() / 2f) * l);

		g.drawLine(xo, yo, xv, yv);
		if (operando instanceof Variavel) {
			int xV = (int) ((((Variavel) operando).getLarguraTotal() / 2f) * l);
			int yV = (int) ((operando.getProfundidade() - .3f) * (2.5f * l));
			int yW = (int) ((operando.getProfundidade() + .2f) * (2.5f * l));

			// linha sub operador -> linha sobre variáveis (vertical)
			g.drawLine(xv, yv, xv, yV);
			// linha sobre variáveis -> linha variáveis
			g.drawLine(xv, yV, xV, yW);
		}

		operando.draw(g, l);

		super.draw(g, l);
	}
}