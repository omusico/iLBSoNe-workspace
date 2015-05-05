package org.mapsforge.core;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public final class Coordinates {
	public static final double LATITUDE_MAX = 90.0D;
	public static final double LATITUDE_MIN = -90.0D;
	public static final double LONGITUDE_MAX = 180.0D;
	public static final double LONGITUDE_MIN = -180.0D;
	private static final double CONVERSION_FACTOR = 1000000.0D;
	private static final String DELIMITER = ",";

	public static int degreesToMicrodegrees(double coordinate) {
		return (int) (coordinate * 1000000.0D);
	}

	public static double microdegreesToDegrees(int coordinate) {
		return (coordinate / 1000000.0D);
	}

	public static double[] parseCoordinateString(String coordinatesString,
			int numberOfCoordinates) {
		StringTokenizer stringTokenizer = new StringTokenizer(
				coordinatesString, DELIMITER, true);
		boolean isDelimiter = true;
		List<String> tokens = new ArrayList<String>(numberOfCoordinates);
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			isDelimiter = !isDelimiter;
			if (isDelimiter) {
				continue;
			}
			tokens.add(token);
		}
		if (isDelimiter) {
			throw new IllegalArgumentException("invalid coordinate delimiter: "
					+ coordinatesString);
		} else if (tokens.size() != numberOfCoordinates) {
			throw new IllegalArgumentException(
					"invalid number of coordinate values: " + coordinatesString);
		}
		double[] coordinates = new double[numberOfCoordinates];
		for (int i = 0; i < numberOfCoordinates; ++i) {
			coordinates[i] = Double.parseDouble(tokens.get(i));
		}
		return coordinates;
	}

	public static void validateLatitude(double latitude) {
		if ((latitude < -90.0D) || (latitude > 90.0D))
			throw new IllegalArgumentException("invalid latitude: " + latitude);
	}

	public static void validateLongitude(double longitude) {
		if ((longitude < -180.0D) || (longitude > 180.0D))
			throw new IllegalArgumentException("invalid longitude: "
					+ longitude);
	}

	private Coordinates() {
		throw new IllegalStateException();
	}
}