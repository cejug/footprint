/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (C) 2007 Felipe Gaúcho
 
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 
 This file is part of the FOOTPRINT Project (a generator of signed PDF
 documents, originally used as JUG events certificates), hosted at
 https://github.com/cejug/footprint
 
 
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
package org.cejug.footprint.core.mailer;

import java.io.File;
import java.io.FilenameFilter;

import org.cejug.footprint.jaxb.FootprintConfig;

/**
 * A filter to read the generated files only with a valid extension - used in
 * case of RENAME strategy of post-processing.
 * 
 * @author Felipe Gaucho
 */
public class GeneratedFilesFilter implements FilenameFilter {
    private transient final String validExtension;

    /**
     * A file extension filter.
     * 
     * @param config
     *            the configuration object.
     */
    public GeneratedFilesFilter(FootprintConfig config) {
        validExtension = config.getTemplate().getPdfGeneratedExtension();
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(validExtension);
    }
}
