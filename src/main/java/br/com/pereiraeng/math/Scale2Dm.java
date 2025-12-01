package br.com.pereiraeng.math;

import java.awt.Dimension;

/**
 * Classe do objeto que representa uma operação afim mixta (grade inteira e
 * fator de escala decimal)
 * 
 * @author Philipe PEREIRA  
 * @version September 16th, 2020    
 */
public class Scale2Dm extends Scale2Di {

	protected float scale;

	public Scale2Dm(float scale, int width, int height) {
		super(width, height);
		this.setScale(scale);
	}

	public Scale2Dm(float scale, Dimension dimension) {
		super(dimension);
		this.setScale(scale);
	}

	public Scale2Dm(float scale, Scale2Di s) {
		super(s);
		this.setScale(scale);
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	// ---------------------------------------------------------

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof Scale2Dm) {
			Scale2Dm s = (Scale2Dm) anObject;
			return super.equals(s) && s.scale == this.scale;
		}
		return false;
	}

	@Override
	public String toString() {
		return "(" + super.width + ";" + super.height + ";" + scale + ")";
	}
}
