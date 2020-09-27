package net.okhotnikov.everything.service;

import net.okhotnikov.everything.api.in.AppleVerificationResponse;
import net.okhotnikov.everything.api.in.StatusResponse;
import net.okhotnikov.everything.dao.RestDao;
import net.okhotnikov.everything.exceptions.UnverifiedPurchaseException;
import net.okhotnikov.everything.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PaymentService {

    @Value("${apple.sandbox}")
    private boolean isSandbox;

    public static final String APPSTORE_SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
    public static final String APPSTORE_PROD_URL = "https://buy.itunes.apple.com/verifyReceipt";


    private final UserService userService;
    private final RestDao restDao;

    public PaymentService(UserService userService, RestDao restDao) {
        this.userService = userService;
        this.restDao = restDao;
    }


    public void appStorePurchase(String username, String token) throws IOException {
        StatusResponse response = restDao.post(
                getAppStoreUrl(),
                restDao.getOneFieldMap("receipt-data",token),
                AppleVerificationResponse.class);

        if (response.status != 0)
            throw new UnverifiedPurchaseException();

        userService.addRole(username, Role.ROLE_READER);
    }

    public String getAppStoreUrl(){
        return  isSandbox ? APPSTORE_SANDBOX_URL : APPSTORE_PROD_URL;
    }
}
