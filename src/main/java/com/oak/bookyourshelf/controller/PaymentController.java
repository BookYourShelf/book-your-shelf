package com.oak.bookyourshelf.controller;

import com.oak.bookyourshelf.model.CreditCard;
import com.oak.bookyourshelf.model.Payment;
import com.oak.bookyourshelf.model.PaymentMethod;
import com.oak.bookyourshelf.model.PaymentResult;
import com.oak.bookyourshelf.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.sql.Timestamp;

@Controller
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @RequestMapping("/payment")
    public String showNewPaymentPage(Model model) {
        CreditCard creditCard = new CreditCard();
        model.addAttribute("creditCard", creditCard);

        return "payment";
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public String makePayment(@Valid @ModelAttribute CreditCard creditCard, BindingResult errors, Model model) {
        if (!errors.hasErrors()) {
            Payment payment = new Payment();
            payment.setIssueDate(new Timestamp(System.currentTimeMillis()));
            payment.setPayerId(1);
            payment.setPaymentMethod(PaymentMethod.PAYMENT_METHOD_BANK_TRANSFER);
            payment.setPaymentResult(PaymentResult.PAYMENT_RESULT_SUCCESS);
            paymentService.save(payment);
        }
        return ((errors.hasErrors()) ? "payment" : "thank-you");
    }


}
