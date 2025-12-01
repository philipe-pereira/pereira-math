package br.com.pereiraeng.math;

import java.awt.Dimension;

/**
 * Classe do objeto que representa uma operação afim inteira
 * 
 * @author Philipe PEREIRA  
 * @version September 16h, 2020  
 */
public class Scale2Di {

	protected int width;
	protected int height;

	public Scale2Di(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Scale2Di(Dimension d) {
		this(d.width, d.height);
	}

	public Scale2Di(Scale2Di s) {
		this(s.width, s.height);
	}

	public void set(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	// ---------------------------------------------------------

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof Scale2Di) {
			Scale2Di s = (Scale2Di) anObject;
			return s.height == this.height && s.width == this.width;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(width + height).hashCode();
	}

	@Override
	public String toString() {
		return "(" + width + ";" + height + ")";
	}
}
