/*******************************************************************************
 * Copyright (c) 2008, 2021 Lablicate GmbH.
 * 
 * All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Dr. Philip Wenig - initial API and implementation
 * Christoph Läubrich - add generics
 *******************************************************************************/
package net.openchrom.csd.converter.supplier.gaml.converter;

import java.io.File;

import org.eclipse.chemclipse.converter.chromatogram.AbstractChromatogramImportConverter;
import org.eclipse.chemclipse.csd.converter.io.IChromatogramCSDReader;
import org.eclipse.chemclipse.csd.model.core.IChromatogramCSD;
import org.eclipse.chemclipse.logging.core.Logger;
import org.eclipse.chemclipse.model.core.IChromatogramOverview;
import org.eclipse.chemclipse.processing.core.IProcessingInfo;
import org.eclipse.core.runtime.IProgressMonitor;

import net.openchrom.csd.converter.supplier.gaml.io.ChromatogramReader;
import net.openchrom.xxd.converter.supplier.gaml.internal.io.IConstants;

public class ChromatogramImportConverter extends AbstractChromatogramImportConverter<IChromatogramCSD> {

	private static final Logger logger = Logger.getLogger(ChromatogramImportConverter.class);
	private static final String DESCRIPTION = "GAML Chromatogram Import Converter";

	@Override
	public IProcessingInfo<IChromatogramCSD> convert(File file, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramCSD> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages()) {
			/*
			 * Read the chromatogram.
			 */
			IChromatogramCSDReader reader = new ChromatogramReader();
			monitor.subTask(IConstants.IMPORT_CHROMATOGRAM);
			try {
				IChromatogramCSD chromatogram = reader.read(file, monitor);
				processingInfo.setProcessingResult(chromatogram);
			} catch(Exception e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, "Something has definitely gone wrong with the file: " + file.getAbsolutePath());
			}
		}
		return processingInfo;
	}

	@Override
	public IProcessingInfo<IChromatogramOverview> convertOverview(File file, IProgressMonitor monitor) {

		IProcessingInfo<IChromatogramOverview> processingInfo = super.validate(file);
		if(!processingInfo.hasErrorMessages()) {
			IChromatogramCSDReader reader = new ChromatogramReader();
			try {
				IChromatogramOverview chromatogramOverview = reader.readOverview(file, monitor);
				processingInfo.setProcessingResult(chromatogramOverview);
			} catch(Exception e) {
				logger.warn(e);
				processingInfo.addErrorMessage(DESCRIPTION, "Something has definitely gone wrong with the file: " + file.getAbsolutePath());
			}
		}
		return processingInfo;
	}
}
