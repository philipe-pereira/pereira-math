package br.com.pereiraeng.math.expression.continuous;

import java.awt.Graphics;
import java.util.LinkedList;

/**
 * Bloco de operação que transforma uma série de valores em um outro
 * 
 * @author Philipe PEREIRA
 *
 */
public class N_ario extends Operador {
	private LinkedList<Valor> operandos;

	public N_ario(TipoOperador operador) {
		super(operador);
		this.operandos = new LinkedList<Valor>();
	}

	public Valor getOperando(int i) {
		return this.operandos.get(i);
	}

	public void setOperando(Valor valor) {
		this.operandos.add(valor);
	}

	public int getOperandosCount() {
		return this.operandos.size();
	}

	@Override
	public double getValor() {
		double out = 0.0;
		switch (operador) {
		case ADICAO:
			for (Valor v : operandos)
				out += v.getValor();
			break;
		case MULTIPLICACAO:
			out = 1.0;
			for (Valor v : operandos)
				out *= v.getValor();
			break;
		case MEDIA:
			for (Valor v : operandos)
				out += v.getValor();
			out /= operandos.size();
			break;
		case MAX:
			out = Double.NEGATIVE_INFINITY;
			for (Valor v : operandos)
				out = Math.max(out, v.getValor());
			break;
		case MIN:
			out = Double.POSITIVE_INFINITY;
			for (Valor v : operandos)
				out = Math.min(out, v.getValor());
			break;
		default:
			break;
		}
		return out;
	}

	@Override
	public int getLargura() {
		int l = 0;
		for (Valor v : operandos) {
			l += v.getLargura();
		}
		return l;
	}

	@Override
	public void setPosicoes(int x) {
		operandos.get(0).setPosicao(x);
		int p = x;
		for (Valor v : operandos) {
			v.setPosicao(p);
			p += v.getLargura();
		}
	}

	@Override
	public void draw(Graphics g, int l) {

		int xo = (int) ((posicao + getLargura() / 2f) * l);
		int yo = (int) ((profundidade + .7f) * (2.5f * l));
		int yv = (int) ((profundidade + 1.2f) * (2.5f * l));
		int xv = -1;

		for (int i = 0; i < operandos.size(); i++) {
			Valor v = operandos.get(i);

			if (v instanceof Variavel) {
				if (xv != -1)
					xv += (int) ((operandos.get(i - 1).getLargura() + 1) * l / 2f);
				else
					xv = (int) ((this.getPosicao() + .5f) * l);

				int xV = (int) ((((Variavel) v).getLarguraTotal() / 2f) * l);
				int yV = (int) ((v.getProfundidade() + .2f - .5f) * (2.5f * l));
				int yV1 = (int) ((v.getProfundidade() + .2f) * (2.5f * l));

				// linha operador -> linha sub operador
				g.drawLine(xo, yo, xv, yv);
				// linha sub operador -> linha sobre variáveis (vertical)
				g.drawLine(xv, yv, xv, yV);
				// linha sobre variáveis -> linha variáveis
				g.drawLine(xv, yV, xV, yV1);
			} else {
				xv = (int) ((v.getPosicao() + v.getLargura() / 2f) * l);
				g.drawLine(xo, yo, xv, yv);
			}

			v.draw(g, l);
		}
		super.draw(g, l);
	}
}