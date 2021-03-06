package pe.edu.upc.controller;

import net.bytebuddy.utility.RandomString;
import pe.edu.upc.model.Forgot;
import pe.edu.upc.model.MovPatient;
import pe.edu.upc.model.User;
import pe.edu.upc.repository.UserRepository;
import pe.edu.upc.security.Utility;
import pe.edu.upc.service.EmailSenderService;
import pe.edu.upc.service.MovPatientService;
import pe.edu.upc.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class ForgotPasswordAppController {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MovPatientService MovPService;

    private int randomNum = ThreadLocalRandom.current().nextInt(11111, 99999 + 1);

    @PostMapping("/forgot_password_app")
    @ResponseStatus(value = HttpStatus.OK)
    public void processForgotAppPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");

        String token = RandomString.make(30);

        try {
            sendEmailApp(email);
            //MovPService.updateResetPasswordToken(token, email);      this
            model.addAttribute("message", "Hemos enviado un enlace para restablecer la contrase?a a su correo electronico. Por favor, compruebe.");

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
        }
        //System.out.println("DEBUGGEOOOOOOOOOOOOOOOOOOOO");
    }

    public void sendEmailApp(String recipientEmail)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("it.innova.service@gmail.com", "Shopme Support");
        helper.setTo(recipientEmail);
        randomNum = ThreadLocalRandom.current().nextInt(11111, 99999 + 1);
        String subject = "Aqui? esta su codigo de verificacion";

        String content = "<p>Hola,</p>"
                + "<p>Ha solicitado restablecer su contrase?a.</p>"
                + "<p>El codigo de verificacion es el siguiente:</p>"
                + randomNum
                + "<br>"
                + "<p>Ingreselo en la aplicacion y procesa con los pasos para el restablecimiento de su contrase?a, ";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }


    @PostMapping("/reset_password_appx")
    @ResponseStatus(value = HttpStatus.OK)
    public void processResetAppPassword(@RequestBody Forgot forgot) {
        //String token = request.getParameter("token");
        //String password = request.getParameter("password");
        //password = "123";
        System.out.println(forgot.token);
        System.out.println(forgot.password);
        //System.out.println(password);
        /*
        MovPatient customer = MovPService.getByResetPasswordToken(token);

        model.addAttribute("title", "Reset your password");

        if (customer == null || Integer.parseInt(token) != randomNum) {
            model.addAttribute("message", "Invalid Code");
            System.out.println(customer);
            System.out.println("No funciono");
        } else {
            System.out.println(customer);
            MovPService.updatePassword(customer, "123");
            model.addAttribute("message", "You have successfully changed your password.");
        }*/
    }
}