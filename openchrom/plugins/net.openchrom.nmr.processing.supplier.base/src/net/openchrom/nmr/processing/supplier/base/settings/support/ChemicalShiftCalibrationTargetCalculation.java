/*******************************************************************************
 * Copyright (c) 2019, 2020 Lablicate GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Alexander Stark - initial API and implementation
 * Philip Wenig - refactoring
 *******************************************************************************/
package net.openchrom.nmr.processing.supplier.base.settings.support;

import org.apache.commons.math3.distribution.CauchyDistribution;
import org.ejml.simple.SimpleMatrix;

import net.openchrom.nmr.processing.supplier.base.core.UtilityFunctions;
import net.openchrom.nmr.processing.supplier.base.settings.ChemicalShiftCalibrationSettings;
import net.openchrom.nmr.processing.supplier.base.settings.IcoShiftAlignmentSettings;
import net.openchrom.nmr.processing.supplier.base.settings.support.IcoShiftAlignmentUtilities.Interval;

public class ChemicalShiftCalibrationTargetCalculation implements ChemicalShiftCalibrationTargetFunction {

	@Override
	public double[] calculateCalibrationTarget(SimpleMatrix data, Interval<Integer> interval, IcoShiftAlignmentSettings alignmentSettings, ChemicalShiftCalibrationSettings calibrationSettings) {

		/*
		 * For future use e.g. in KNIME, the data matrix and alignment settings
		 * are passed as arguments though they are not currently being used.
		 */
		double locationOfCauchyDistribution = calibrationSettings.getLocationOfCauchyDistribution();
		double scaleOfCauchyDistribution = calibrationSettings.getScaleOfCauchyDistribution();
		if(scaleOfCauchyDistribution < 0) {
			throw new IllegalArgumentException("Value of the scale of Cauchy Distribution must be greater than zero.");
		}
		double rangeOfCauchyDistribution = calibrationSettings.getRangeOfCauchyDistribution();
		// generate lorentzian distribution and calculate a peak from it
		CauchyDistribution cDistribution = new CauchyDistribution(locationOfCauchyDistribution, scaleOfCauchyDistribution);
		//
		int windowWidth = IcoShiftAlignmentUtilities.generateReferenceWindow(interval).length;
		double[] xAxisVector = UtilityFunctions.generateLinearlySpacedVector(-rangeOfCauchyDistribution / 2, rangeOfCauchyDistribution / 2, windowWidth);
		double[] probabilityDensityFunction = new double[xAxisVector.length];
		for(int i = 0; i < xAxisVector.length; i++) {
			// calculate a peak with some intensity and peak maximum in the middle of interval
			probabilityDensityFunction[i] = Math.pow(cDistribution.density(xAxisVector[i]), 2); // TARGET
		}
		return probabilityDensityFunction;
	}
}