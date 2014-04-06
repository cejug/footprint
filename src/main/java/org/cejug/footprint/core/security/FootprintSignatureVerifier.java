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
package org.cejug.footprint.core.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.MissingResourceException;

/**
 * A signature verifier is respnsible to check if the digital certificate used
 * to sign a PDF matches one of the certificates stored in the local key store.
 * 
 * @author Felipe Gaucho
 */
public interface FootprintSignatureVerifier {
    /**
     * Verify the signature of a PDF document against a key store.
     * 
     * @param document
     *            the document to be validated.
     * @param keystore
     *            the keystore containing the public keys used to validate the
     *            document.
     * @throws IOException
     *             problems trying to read the signed PDF or the keystore.
     * @throws SignatureException
     *             error trying to verify the signature.
     * @return if the document is valid.
     * @throws SignatureException
     * @throws MissingResourceException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws KeyStoreException
     * @throws InvalidKeyException
     */
    boolean verify(String filename, KeyStore keystore) throws IOException, SignatureException, InvalidKeyException, KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException,
            CertificateException, MissingResourceException;
}
