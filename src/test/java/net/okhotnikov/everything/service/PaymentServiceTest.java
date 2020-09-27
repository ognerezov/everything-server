package net.okhotnikov.everything.service;

import net.okhotnikov.everything.exceptions.UnverifiedPurchaseException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @Test()
    void appStorePurchase() {
        Exception exception = assertThrows(UnverifiedPurchaseException.class, () -> {
            paymentService.appStorePurchase("user","not a token");
        });
    }
}