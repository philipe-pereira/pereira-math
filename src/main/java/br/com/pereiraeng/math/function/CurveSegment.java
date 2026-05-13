package br.com.pereiraeng.math.function;

public interface CurveSegment {
	boolean containsX(double x);

	double y(double x);

	double getLower();

	double getUpper();
}