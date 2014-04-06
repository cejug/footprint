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

import javax.mail.MessagingException;

/**
 * A mailer interface.
 * 
 * @author Felipe Gaucho
 */
public interface Mailer {
    String getSmtpTransportProtocol();

    String getSmtpStarTtlsEnable();

    String getSmtpHost();

    String getSmtpAuth();

    String getSmtpUser();

    String getSmtpPassword();

    String getSmtpContentType();

    String getMessageBody();

    String getMsgSubject();

    String getMsgFrom();

    String getMsgTo();

    String getMsgCc();

    String getAttachmentLabel();

    String getAttachmentFilename();

    /**
     * Only dispatch the generated files, ignoring extra files in the generated
     * directory.
     */
    void send() throws MessagingException;
}
