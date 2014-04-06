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
package org.cejug.footprint.core.security.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.MissingResourceException;

import org.cejug.footprint.core.security.FootprintSignatureVerifier;
import org.cejug.footprint.core.security.FootprintSigner;
import org.cejug.footprint.core.util.FootprintResourceBundle;
import org.cejug.footprint.jaxb.ConfigSecurity;
import org.cejug.footprint.jaxb.FootprintConfig;

/**
 * A factory for default security objects.
 * 
 * @author Felipe Gaucho
 */
public final class FootprintSecurityFactory {
    /** a reference to the singleton instance. */
    private static FootprintSecurityFactory instance = new FootprintSecurityFactory();

    /**
     * A private constructor to ensure the Singleton pattern.
     */
    private FootprintSecurityFactory() {
    }

    /**
     * Singleton access method.
     * 
     * @return the singleton instance of the factory.
     */
    public static FootprintSecurityFactory getInstance() {
        return instance;
    }

    /**
     * Reflection based instantiation.
     * 
     * @param classname
     *            the Signer type.
     * @param params
     *            the parameters required by the constructor of the Signer
     *            class.
     * @return an instance of the Signer.
     * @throws ClassNotFoundException
     *             when there is no class of the required type in the classpath.
     *             Either include the type in the classpath or review the
     *             classname. {@link java.lang.ClassNotFoundException}
     * @throws IllegalArgumentException
     *             reflection exception.
     * @throws NoSuchMethodException
     *             {@link java.lang.NoSuchMethodException}
     * @throws SecurityException
     *             {@link java.lang.SecurityException}
     * @throws InstantiationException
     *             {@link java.lang.InstantiationException}
     * @throws IllegalAccessException
     *             {@link java.lang.IllegalAccessException}
     * @throws InvocationTargetException
     *             {@link java.lang.reflect.InvocationTargetException}
     */
    public FootprintSigner getPdfSigner(String classname, Object[] params) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        Class<?>[] paramTypes = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++) {
            paramTypes[i] = params[i].getClass();
        }
        return getPdfSigner(classname, paramTypes, params);
    }

    /**
     * 
     * Reflection based instantiation, using the default constructor of the
     * target class.
     * 
     * @param classname
     *            the Signer type.
     * @return an instance of the Signer.
     * @throws ClassNotFoundException
     *             when there is no class of the required type in the classpath.
     *             Either include the type in the classpath or review the
     *             classname. {@link java.lang.ClassNotFoundException}
     * @throws IllegalArgumentException
     *             reflection exception.
     * @throws NoSuchMethodException
     *             {@link java.lang.NoSuchMethodException}
     * @throws SecurityException
     *             {@link java.lang.SecurityException}
     * @throws InstantiationException
     *             {@link java.lang.InstantiationException}
     * @throws IllegalAccessException
     *             {@link java.lang.IllegalAccessException}
     * @throws InvocationTargetException
     *             {@link java.lang.reflect.InvocationTargetException}
     */
    public FootprintSigner getPdfSigner(String classname) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return getPdfSigner(classname, new Object[0]);
    }

    /**
     * Reflection: tries to instantiate a document signer using its parameters.
     * 
     * @param classname
     *            the Signer type.
     * @param argTypes
     *            the type of the parameters required by the constructor of the
     *            Signer class.
     * @param args
     *            the parameters required by the constructor of the Signer
     *            class.
     * @return an instance of the Signer.
     * @throws ClassNotFoundException
     *             when there is no class of the required type in the classpath.
     *             Either include the type in the classpath or review the
     *             classname. {@link java.lang.ClassNotFoundException}
     * @throws IllegalArgumentException
     *             reflection exception.
     * @throws NoSuchMethodException
     *             {@link java.lang.NoSuchMethodException}
     * @throws SecurityException
     *             {@link java.lang.SecurityException}
     * @throws InstantiationException
     *             {@link java.lang.InstantiationException}
     * 
     * @throws IllegalAccessException
     *             {@link java.lang.IllegalAccessException}
     * @throws InvocationTargetException
     *             {@link java.lang.reflect.InvocationTargetException}
     */
    public FootprintSigner getPdfSigner(String classname, Class<?>[] argTypes, Object[] args) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<?> c = Class.forName(classname);
        Constructor<?> cc = c.getConstructor(argTypes);
        return (FootprintSigner) cc.newInstance(args);
    }

    /**
     * Returns an instance of the default Footprint Signer.
     * 
     * @param classname
     *            the Signer type.
     * @param argTypes
     *            the type of the parameters required by the constructor of the
     *            Signer class.
     * @param args
     *            the parameters required by the constructor of the Signer
     *            class.
     * @return an instance of the Signer.
     * @throws ClassNotFoundException
     *             when there is no class of the required type in the classpath.
     *             Either include the type in the classpath or review the
     *             classname. {@link java.lang.ClassNotFoundException}
     * @throws IllegalArgumentException
     *             reflection exception.
     * @throws NoSuchMethodException
     *             {@link java.lang.NoSuchMethodException}
     * @throws SecurityException
     *             {@link java.lang.SecurityException}
     * @throws InstantiationException
     *             {@link java.lang.InstantiationException}
     * 
     * @throws IllegalAccessException
     *             {@link java.lang.IllegalAccessException}
     * @throws InvocationTargetException
     *             {@link java.lang.reflect.InvocationTargetException}
     */
    public FootprintSigner getDefaultPdfSigner(FootprintConfig config) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return getPdfSigner(DefaultFootprintSigner.class.getName(), new Class[] { FootprintConfig.class }, new Object[] { config });

    }

    /**
     * Returns an instance of the default Footprint Validator.
     * 
     * @param classname
     *            the Validator type.
     * @param argTypes
     *            the type of the parameters required by the constructor of the
     *            Validator class.
     * @param args
     *            the parameters required by the constructor of the Validator
     *            class.
     * @return an instance of the Validator.
     * @throws ClassNotFoundException
     *             when there is no class of the required type in the classpath.
     *             Either include the type in the classpath or review the
     *             classname. {@link java.lang.ClassNotFoundException}
     * @throws IllegalArgumentException
     *             reflection exception.
     * @throws NoSuchMethodException
     *             {@link java.lang.NoSuchMethodException}
     * @throws SecurityException
     *             {@link java.lang.SecurityException}
     * @throws InstantiationException
     *             {@link java.lang.InstantiationException}
     * 
     * @throws IllegalAccessException
     *             {@link java.lang.IllegalAccessException}
     * @throws InvocationTargetException
     *             {@link java.lang.reflect.InvocationTargetException}
     */
    public FootprintSignatureVerifier getDefaultPdfVerifier(FootprintConfig config) throws IllegalArgumentException, SecurityException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return getSignatureValidator(DefaultFootprintSignatureVerifier.class.getName(), new Class[] { FootprintConfig.class }, new Object[] { config });

    }

    /**
     * Constructs a Footprint Validator using reflection.
     * 
     * @param classname
     *            the Validator type.
     * @param argTypes
     *            the type of the parameters required by the constructor of the
     *            Validator class.
     * @param args
     *            the parameters required by the constructor of the Validator
     *            class.
     * @return an instance of the Validator.
     * @throws ClassNotFoundException
     *             when there is no class of the required type in the classpath.
     *             Either include the type in the classpath or review the
     *             classname. {@link java.lang.ClassNotFoundException}
     * @throws IllegalArgumentException
     *             reflection exception.
     * @throws NoSuchMethodException
     *             {@link java.lang.NoSuchMethodException}
     * @throws SecurityException
     *             {@link java.lang.SecurityException}
     * @throws InstantiationException
     *             {@link java.lang.InstantiationException}
     * 
     * @throws IllegalAccessException
     *             {@link java.lang.IllegalAccessException}
     * @throws InvocationTargetException
     *             {@link java.lang.reflect.InvocationTargetException}
     */
    public FootprintSignatureVerifier getSignatureValidator(String classname, Class<?>[] argTypes, Object[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException,
            IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> c = Class.forName(classname);
        Constructor<?> cc = c.getConstructor(argTypes);
        return (FootprintSignatureVerifier) cc.newInstance(args);
    }

    /**
     * 
     * @param config
     * @return
     * @throws KeyStoreException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws MissingResourceException
     */
    public KeyStore loadKeystore(FootprintConfig config) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, IOException, InvalidKeyException,
            MissingResourceException {
        KeyStore keystore = null;
        ConfigSecurity security = config.getSecurity();
        String keystoreFilename = security.getKeystoreFilename();
        String keystoreProvider = security.getKeystorePkProvider();
        String keystoreType = security.getKeystoreType();
        if (keystoreProvider == null) {
            keystore = KeyStore.getInstance(keystoreType);
        } else {
            keystore = KeyStore.getInstance(keystoreType, keystoreProvider);
        }
        InputStream keystoreInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(keystoreFilename);
        keystore.load(keystoreInputStream, security.getKeystorePassword().toCharArray());
        String alias = security.getKeystorePkAlias();
        if (alias == null || alias.length() == 0) {
            alias = keystore.aliases().nextElement();
        }
        if (keystore.containsAlias(alias)) {
            return keystore;
        } else {
            throw new InvalidKeyException(FootprintResourceBundle.getInstance().getString("error.invalidKeyException", new String[] { alias }));
        }

    }
}
