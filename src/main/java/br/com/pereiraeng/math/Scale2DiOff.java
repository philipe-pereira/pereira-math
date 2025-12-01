package br.com.pereiraeng.math;

import java.awt.Dimension;
import java.awt.Point;

/**
 * Classe do objeto que representa uma operação afim inteira com offset
 * 
 * @author Philipe PEREIRA
 *
 */
public class Scale2DiOff extends Scale2Di {

	protected Point offset;

	public Scale2DiOff(int width, int height) {
		super(width, height);
		this.setOffset(new Point());
	}

	public Scale2DiOff(int width, int height, int x0, int y0) {
		super(width, height);
		this.setOffset(new Point(x0, y0));
	}

	public Scale2DiOff(Scale2Di grade, Point offset) {
		super(grade);
		this.setOffset(offset);
	}

	public Scale2DiOff(Dimension dimension, Point offset) {
		super(dimension);
		this.setOffset(offset);
	}

	public void setOffset(Point offset) {
		this.offset = offset;
	}

	public void setOffset(int x0, int y0) {
		this.offset.setLocation(x0, y0);
	}

	public Point getOffset() {
		return offset;
	}

	public int getX() {
		return offset.x;
	}

	public int getY() {
		return offset.y;
	}

	// ---------------------------------------------------------

	@Override
	public boolean equals(Object anObject) {
		if (this == anObject)
			return true;
		if (anObject instanceof Scale2DiOff) {
			Scale2DiOff s = (Scale2DiOff) anObject;
			return super.equals(s) && s.getOffset().equals(this.getOffset());
		}
		return false;
	}

	@Override
	public String toString() {
		return "(" + width + ";" + height + ";" + offset.x + ";" + offset.y + ")";
	}
}
