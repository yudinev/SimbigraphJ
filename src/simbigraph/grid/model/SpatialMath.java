package simbigraph.grid.model;


/**
 * @author Nick Collier
 */
public class SpatialMath {

	private static double[] rotate(double[] plane, double angle) {
		double x = plane[0];
		double y = plane[1];
		plane[0] = x * Math.cos(angle) - y * Math.sin(angle);
		plane[1] = y * Math.cos(angle) + x * Math.sin(angle);
		return plane;
	}

	/**
	 * Scales the point by the specified amount. The point is updated to the new value.
	 * 
	 * @param point
	 *            the point to scale
	 * @param scale
	 *            the amount to scale the point by
	 */
	public static void scale(double[] point, double scale) {
		for (int i = 0; i < point.length; i++) {
			point[i] = point[i] * scale;
		}
	}

	/**
	 * Returns the displacement
	 * 
	 * @param dimCount
	 *            the number of dimensions in the space
	 * @param unitDimension
	 *            the dimension that we are rotating relative to
	 * @param scale
	 *            the distance we are moving (length of the displacement vector)
	 * @param anglesInRadians
	 *            the angles relative to the unit dimension
	 * @return the displacement
	 */
	public static double[] getDisplacement(int dimCount, int unitDimension, double scale,
			double... anglesInRadians) {
		double[] displacement = new double[dimCount];
		displacement[unitDimension] = 1;
		double[] tmp = new double[2];
		int c = 0;
		for (int i = 0; i < dimCount; i++) {
			if (i == unitDimension) {
				continue;
			} else if (i > unitDimension) {
				tmp[0] = displacement[unitDimension];
				tmp[1] = displacement[i];
				rotate(tmp, anglesInRadians[c]);
				displacement[unitDimension] = tmp[0];
				displacement[i] = tmp[1];
			} else if (i < unitDimension) {
				tmp[0] = displacement[i];
				tmp[1] = displacement[unitDimension];
				rotate(tmp, anglesInRadians[c]);
				displacement[unitDimension] = tmp[1];
				displacement[i] = tmp[0];
			}
			c++;
		}
		scale(displacement, scale);
		return displacement;
	}

	/**
	 * Returns the displacement
	 * TODO: try and optimize this so taht 
	 * 
	 * @param dimCount
	 *            the number of dimensions in the space
	 * @param unitDimension
	 *            the dimension that we are rotating relative to
	 * @param scale
	 *            the distance we are moving (length of the displacement vector)
	 * @param anglesInRadians
	 *            the angles relative to the unit dimension
	 * @return the displacement
	 */
	public static int[] getDisplacementInt(int dimCount, int unitDimension, double scale,
			double... anglesInRadians) {
		double[] displacement = getDisplacement(dimCount, unitDimension, scale, anglesInRadians);
		
		int[] intDisplacement = new int[dimCount];
		for (int i = 0; i < dimCount; i++) {
			intDisplacement[i] = (int) Math.rint(displacement[i]);
		}
		
		return intDisplacement;
	}
}
