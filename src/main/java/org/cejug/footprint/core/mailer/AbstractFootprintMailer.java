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

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * This is a simple mailer that send an email containning a brief explanation
 * about its contents, and with the attached PDF.
 * 
 * @author Felipe Gaucho
 * 
 */
abstract class AbstractFootprintMailer implements Mailer {
    private String transportProtocol;
    private String starTtlsEnable;
    private String smtpHost;
    private String smtpAuth;
    private String smtpUser;
    private String smtpPassword;

    // optional attributes should be initialized with default values
    private String socksProxyPort;
    private String socksProxyHost;

    private String smtpContentType;

    private String messageBody;
    private String msgSubject;
    private String msgFrom;
    private String msgCc;
    private String msgTo;

    private String attachmentLabel;
    private String attachmentFilename;

    /**
     * It assemble a mail message to confirm an advertisement publishing. After
     * that, the system dispatch the message to the advertisement owner.
     * 
     * @throws MessagingException
     * @see net.java.dev.cejug.classifieds.command.ClassifiedsCommand#execute(net.java.dev.cejug.classifieds.bean.AbstractClassifiedsFormBean)
     */
    protected void sendMail() throws MessagingException {
        boolean debug = false;

        // Set the host smtp address
        Properties props = new Properties();
        props.put(SmtpPropertiesEnumeration.MAIL_TRANSPORT_PROTOCOL.toString(), getSmtpTransportProtocol());
        props.put(SmtpPropertiesEnumeration.MAIL_SMTP_STARTTLS_ENABLE.toString(), getSmtpStarTtlsEnable());
        props.put(SmtpPropertiesEnumeration.MAIL_SMTP_HOST.toString(), getSmtpHost());
        props.put(SmtpPropertiesEnumeration.MAIL_SMTP_AUTH.toString(), getSmtpAuth());

        props.put(SmtpPropertiesEnumeration.SOCKS_PROXY_HOST.toString(), getSocksProxyHost());
        props.put(SmtpPropertiesEnumeration.SOCKS_PROXY_PORT.toString(), getSocksProxyPort());

        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(debug);

        Message msg = new MimeMessage(session);

        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();

        // Fill the message
        messageBodyPart.setText(getMessageBody());

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // Part two is attachment
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(getAttachmentFilename());
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(getAttachmentLabel());
        multipart.addBodyPart(messageBodyPart);

        // Put parts in message
        msg.setContent(multipart);

        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(getMsgFrom());
        msg.setFrom(addressFrom);
        InternetAddress[] addressTo = new InternetAddress[1];
        addressTo[0] = new InternetAddress(getMsgTo());

        msg.setRecipients(Message.RecipientType.TO, addressTo);
        msg.setSubject(getMsgSubject());
        // msg.setContent(getMessageBody(), "multipart/form-data");
        Transport.send(msg);
    }

    /**
     * SimpleAuthenticator is used to do simple authentication when the SMTP
     * server requires it.
     */
    public class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            String username = getSmtpUser();
            String password = getSmtpPassword();
            return new PasswordAuthentication(username, password);
        }
    }

    public String getSmtpTransportProtocol() {
        return transportProtocol;
    }

    public String getSmtpStarTtlsEnable() {
        return starTtlsEnable;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getSmtpAuth() {
        return smtpAuth;
    }

    public String getSmtpUser() {
        return smtpUser;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public String getSmtpContentType() {
        return smtpContentType;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getMsgSubject() {
        return msgSubject;
    }

    public String getMsgFrom() {
        return msgFrom;
    }

    public String getMsgCc() {
        return msgCc;
    }

    public String getMsgTo() {
        return msgTo;
    }

    public String getStarTtlsEnable() {
        return starTtlsEnable;
    }

    protected void setStarTtlsEnable(String starTtlsEnable) {
        this.starTtlsEnable = starTtlsEnable;
    }

    protected String getTransportProtocol() {
        return transportProtocol;
    }

    protected void setTransportProtocol(String transportProtocol) {
        this.transportProtocol = transportProtocol;
    }

    protected void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    protected void setSmtpAuth(String smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    protected void setSmtpUser(String smtpUser) {
        this.smtpUser = smtpUser;
    }

    protected void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    protected void setSmtpContentType(String smtpContentType) {
        this.smtpContentType = smtpContentType;
    }

    protected void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    protected void setMsgSubject(String msgSubject) {
        this.msgSubject = msgSubject;
    }

    protected void setMsgFrom(String msgFrom) {
        this.msgFrom = msgFrom;
    }

    protected void setMsgCc(String msgCc) {
        this.msgCc = msgCc;
    }

    protected void setMsgTo(String msgTo) {
        this.msgTo = msgTo;
    }

    public String getAttachmentLabel() {
        return attachmentLabel;
    }

    public void setAttachmentLabel(String attachmentLabel) {
        this.attachmentLabel = attachmentLabel;
    }

    public String getAttachmentFilename() {
        return attachmentFilename;
    }

    public void setAttachmentFilename(String attachmentFilename) {
        this.attachmentFilename = attachmentFilename;
    }

    public String getSocksProxyPort() {
        return socksProxyPort;
    }

    public void setSocksProxyHost(String socksProxyHost) {
        this.socksProxyHost = socksProxyHost;
    }

    public String getSocksProxyHost() {
        return socksProxyHost;
    }

    public void setSocksProxyPort(String socksProxyPort) {
        this.socksProxyPort = socksProxyPort;
    }

    /**
     * Constants of the keys used in the SMTP properties.
     */
    private enum SmtpPropertiesEnumeration {
        MAIL_TRANSPORT_PROTOCOL("mail.transport.protocol"), MAIL_SMTP_STARTTLS_ENABLE("mail.smtp.starttls.enable"), MAIL_SMTP_HOST("mail.smtp.host"), MAIL_SMTP_AUTH("mail.smtp.auth"), MAIL_SMTP_USER(
                "mail.smtp.user"), MAIL_SMTP_PASSWORD("mail.smtp.password"), SOCKS_PROXY_PORT("socks.proxy.port"), SOCKS_PROXY_HOST("socks.proxy.host");

        private String key;

        private SmtpPropertiesEnumeration(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }
    }
}
