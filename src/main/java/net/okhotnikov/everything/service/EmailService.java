package net.okhotnikov.everything.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Sergey Okhotnikov.
 */
@Service
public class EmailService {

    private final SendGrid sg;

    @Value("${mail.address.root}")
    private String rootEmail;

    @Value("${mail.address.admin}")
    private String adminEmail;

    public static final String REGISTER_SUBJECT ="Добро пожаловать в сообщество everything-from.one";
    public static final String REGISTER_BODY = "К письму приложен код доступка к числам. Код один для всех пользователей, но действует ограниченное время. В течение 7 дней после регистрации вы будете получать новые коды доступа по почте." +
            " По окончании этого периода для получения нового кода приводите новых пользователей или свяжитесь с администрацией. Ваш код доступа: ";
    public static final String RENEW_SUBJECT ="Обновленный код достпуа для everything-from.one";
    public static final String RENEW_BODY = "Ваш новый код доступа: ";



    public EmailService(SendGrid sg) {
        this.sg = sg;
    }

    public int send(String fromAddress, String subject, String toAddress, String body) throws IOException {
        Email from = new Email(fromAddress);
        Email to = new Email(toAddress);
        Content content = new Content("text/plain", body);

        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);

        return response.getStatusCode();
    }

    public String getRootEmail() {
        return rootEmail;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public int send(String username, String readersToken) throws IOException {
        return send(adminEmail,REGISTER_SUBJECT, username, REGISTER_BODY + readersToken);
    }

    public int sendRenew(String username, String readersToken) throws IOException {
        return send(adminEmail,RENEW_SUBJECT, username, RENEW_BODY + readersToken);
    }
}
