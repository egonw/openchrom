/*******************************************************************************
 * Copyright (c) 2014, 2021 Lablicate GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 *******************************************************************************/
package net.openchrom.msd.converter.supplier.mzmlb.model;

import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.exceptions.AbundanceLimitExceededException;
import org.eclipse.chemclipse.msd.model.core.AbstractVendorMassSpectrum;
import org.eclipse.chemclipse.msd.model.core.IIon;
import org.eclipse.chemclipse.msd.model.exceptions.IonLimitExceededException;

public class VendorScan extends AbstractVendorMassSpectrum implements IVendorScan {

	/**
	 * Renew the serialVersionUID any time you have changed some fields or
	 * methods.
	 */
	private static final long serialVersionUID = -7237916814647121133L;
	private static final Logger logger = Logger.getLogger(VendorScan.class);
	/**
	 * MAX_MASSFRAGMENTS The total amount of ions to be stored in the
	 * chemclipse chromatogram.<br/>
	 * It does not mean, that ion 65535 is the upper bound, but only 65535 mass
	 * fragments can be stored in a mass spectrum.
	 */
	public static final int MAX_MASSFRAGMENTS = 65535;
	public static final int MIN_RETENTION_TIME = 0;
	public static final int MAX_RETENTION_TIME = Integer.MAX_VALUE;

	public VendorScan() {

		super();
	}

	@Override
	public int getMaxPossibleIons() {

		return MAX_MASSFRAGMENTS;
	}

	@Override
	public int getMinPossibleRetentionTime() {

		return MIN_RETENTION_TIME;
	}

	@Override
	public int getMaxPossibleRetentionTime() {

		return MAX_RETENTION_TIME;
	}

	// -------------------------------IMassSpectrumCloneable
	@Override
	public IVendorScan makeDeepCopy() throws CloneNotSupportedException {

		IVendorScan massSpectrum = (IVendorScan)super.clone();
		IVendorIon mzmlbIon;
		/*
		 * The instance variables have been copied by super.clone();.<br/> The
		 * ions in the ion list need not to be removed via
		 * removeAllIons as the method super.clone() has created a new
		 * list.<br/> It is necessary to fill the list again, as the abstract
		 * super class does not know each available type of ion.<br/>
		 * Make a deep copy of all ions.
		 */
		for(IIon ion : getIons()) {
			try {
				mzmlbIon = new VendorIon(ion.getIon(), ion.getAbundance());
				massSpectrum.addIon(mzmlbIon);
			} catch(AbundanceLimitExceededException e) {
				logger.warn(e);
			} catch(IonLimitExceededException e) {
				logger.warn(e);
			}
		}
		return massSpectrum;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		return makeDeepCopy();
	}
	// -------------------------------IMassSpectrumCloneable
}
