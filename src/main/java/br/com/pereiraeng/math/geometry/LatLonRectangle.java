package br.com.pereiraeng.math.geometry;

public class LatLonRectangle extends SolidAngle {

	private Coordinate.Double mM, Mm;

	public LatLonRectangle(Coordinate.Double mM, Coordinate.Double Mm) {
		this.mM = mM;
		this.Mm = Mm;
		// https://en.wikipedia.org/wiki/Solid_angle
		super.value = (Math.sin(getPhiNrad()) - Math.sin(getPhiSrad())) * (getThetaErad() - getThetaWrad());
		super.ok = true;
	}

	public double getPhiN() {
		return mM.y;
	}

	public double getPhiNrad() {
		return Math.toRadians(getPhiN());
	}

	public double getPhiS() {
		return Mm.y;
	}

	public double getPhiSrad() {
		return Math.toRadians(getPhiS());
	}

	public double getThetaW() {
		return mM.x;
	}

	public double getThetaWrad() {
		return Math.toRadians(getThetaW());
	}

	public double getThetaE() {
		return Mm.x;
	}

	public double getThetaErad() {
		return Math.toRadians(getThetaE());
	}
}
