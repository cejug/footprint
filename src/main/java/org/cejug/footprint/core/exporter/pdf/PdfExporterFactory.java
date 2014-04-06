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
package org.cejug.footprint.core.exporter.pdf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.cejug.footprint.core.exporter.Exporter;

/**
 * A factory for default exprters.
 * 
 * @author Felipe Gaucho
 */
public final class PdfExporterFactory {
    /** a reference to the singleton instance. */
    private static PdfExporterFactory instance = new PdfExporterFactory();

    /**
     * A private constructor to ensure the Singleton pattern.
     */
    private PdfExporterFactory() {
    }

    /**
     * Singleton access method.
     * 
     * @return the singleton instance of the factory.
     */
    public PdfExporterFactory getInstance() {
        return instance;
    }

    /**
     * Reflection based instantiation.
     * 
     * @param classname
     *            the Exporter type.
     * @param params
     *            the parameters required by the constructor of the exporter
     *            class.
     * @return an instance of the Exporter.
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
    public static Exporter getPdfExporter(String classname, Object[] params) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<?>[] paramTypes = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++) {
            paramTypes[i] = params[i].getClass();
        }
        return getPdfExporter(classname, paramTypes, params);
    }

    /**
     * 
     * Reflection based instantiation, using the default constructor of the
     * target class.
     * 
     * @param classname
     *            the Exporter type.
     * @return an instance of the Exporter.
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
    public static Exporter getPdfExporter(String classname) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return getPdfExporter(classname, new Object[0]);
    }

    /**
     * Reflection: tries to instantiate an exporter using its parameters.
     * 
     * @param classname
     *            the Exporter type.
     * @param argTypes
     *            the type of the parameters required by the constructor of the
     *            exporter class.
     * @param args
     *            the parameters required by the constructor of the exporter
     *            class.
     * @return an instance of the Exporter.
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
    public static Exporter getPdfExporter(String classname, Class<?>[] argTypes, Object[] args) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException,
            IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class<?> c = Class.forName(classname);
        Constructor<?> cc = c.getConstructor(argTypes);
        return (Exporter) cc.newInstance(args);
    }
}
