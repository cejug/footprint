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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import org.cejug.footprint.core.util.FootprintDefaults;
import org.cejug.footprint.jaxb.ConfigEmail;
import org.cejug.footprint.jaxb.FootprintConfig;
import org.cejug.footprint.jaxb.MailerI18N;
import org.cejug.footprint.jaxb.PostProcessMode;

/**
 * This is a utility class for sending emails. The Footprint roadmap includes
 * the plan to create a Document Management System able to handle the archiving
 * and distribution of the certificates.
 * 
 * @author Felipe Gaucho
 */
public class DefaultMailerStrategy extends AbstractFootprintMailer {
    private transient FootprintConfig config = null;
    private transient Logger logger = null;

    public DefaultMailerStrategy(final FootprintConfig config, final Logger logger) {
        super();
        this.config = config;
        this.logger = logger;
    }

    /**
     * Creates a default logger that prints messages in the console.
     * 
     * @param config
     *            the footprint configuration object.
     */
    public DefaultMailerStrategy(FootprintConfig config) {
        this(config, FootprintDefaults.getDefaultFootprintLogger());
    }

    @Override
    public void send() throws MessagingException {
        String generatedPath = config.getTemplate().getPdfGeneratedPath();
        File generatedPdfsFolder = new File(generatedPath);
        FilenameFilter filter = new GeneratedFilesFilter(config);
        String[] names = generatedPdfsFolder.list(filter);

        if (names.length == 0) {
            logger.log(Level.WARNING, MailerI18N.MAILER_EMPTY_FOLDER.value(), new String[] { generatedPdfsFolder.getAbsolutePath() });
        }
        for (String certificate : names) {
            String msgTo = "";
            try {
                msgTo = certificate.substring(0, certificate.lastIndexOf('.'));
                File certFile = new File(generatedPdfsFolder, certificate);
                String filename = certFile.getAbsolutePath();
                composeEmail(msgTo, filename);
                super.sendMail();
                postProcess(certFile);
                logger.log(Level.INFO, MailerI18N.MAILER_SENT_OK.value(), new String[] { msgTo });
            } catch (Exception error) {
                logger.log(Level.SEVERE, MailerI18N.MAILER_SENT_NOT_OK.value(), new String[] { msgTo, (error.getMessage() == null ? error.toString() : error.getMessage()) });
                logger.throwing(DefaultMailerStrategy.class.getName(), msgTo, error);
            }
        }
    }

    /**
     * Post processing the generated file, it is a work around for the absence
     * of a management system.
     * 
     * @param certificate
     *            the generated certificate.
     */
    private void postProcess(File certificate) {
        PostProcessMode mode = config.getEmail().getPostProcessingType();
        if (mode == null) {
            return;
        } else if (mode.equals(PostProcessMode.MOVE)) {
            // move the generated certificate to a sent folder
            File newFile = new File(getTargetFolder(), certificate.getName());
            if (!certificate.renameTo(newFile)) {
                logger.log(Level.SEVERE, MailerI18N.MAILER_SENT_MOVE_FAILURE.value(), new String[] { newFile.getAbsolutePath() });
            }
        } else if (mode.equals(PostProcessMode.RENAME) && !certificate.renameTo(new File(certificate.getAbsoluteFile() + File.separator + config.getEmail().getPostProcessingRenameSufix()))) {
            logger.log(Level.SEVERE, MailerI18N.MAILER_SENT_RENAME_FAILURE.value(), new String[] { certificate.getAbsolutePath() });

        }
    }

    /**
     * This is a hard-code first trial, useless for production. Please contact
     * dev@footprint.dev.java.net
     * 
     * @param msgTo
     * @param filename
     * @throws MessagingException
     */
    public void composeEmail(String msgTo, String filename) throws MessagingException {
        ConfigEmail email = config.getEmail();
        setMessageBody(email.getMsgBody());
        setMsgFrom(email.getMsgFrom());
        setMsgSubject(email.getMsgSubject());
        setMsgTo(msgTo);
        setSmtpAuth(Boolean.toString(email.isSmtpAuth()));
        setAttachmentFilename(filename);
        setAttachmentLabel(msgTo + config.getTemplate().getPdfGeneratedExtension());
        setSmtpContentType(email.getSmtpContentType());
        setSmtpHost(email.getSmtpHost());
        setSmtpPassword(email.getSmtpPassword());
        setSmtpUser(email.getSmtpUser());
        setStarTtlsEnable(Boolean.toString(email.isSmtpStarTtlsEnable()));
        setTransportProtocol(email.getSmtpTransferProtocol());
        setSocksProxyHost(email.getSocksProxyHost());
        setSocksProxyPort(email.getSocksProxyPort());
    }

    /**
     * Detect a new name for the folder where the generated files will be moved
     * in case of the parameter <em>post.processing.type</em> is set.
     * 
     * @param config
     *            the configuration object.
     */
    private File getTargetFolder() {
        String parent = config.getTemplate().getPdfGeneratedPath();
        File trialFile = new File(parent + File.separatorChar + "sent" + File.separatorChar);
        int review = 1;
        while (trialFile.exists()) {
            trialFile = new File(parent + File.separator + "sent" + review++ + File.separator);
        }
        if (!trialFile.mkdirs()) {
            logger.log(Level.SEVERE, MailerI18N.MAILER_SENT_MOVE_FAILURE.value(), new String[] { trialFile.getAbsolutePath() });
        }
        return trialFile;
    }
}
