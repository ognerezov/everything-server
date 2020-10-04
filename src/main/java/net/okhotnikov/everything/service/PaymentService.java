package net.okhotnikov.everything.service;

import net.okhotnikov.everything.api.in.AppleVerificationResponse;
import net.okhotnikov.everything.api.in.StatusResponse;
import net.okhotnikov.everything.api.out.TokenResponse;
import net.okhotnikov.everything.dao.RestDao;
import net.okhotnikov.everything.exceptions.UnverifiedPurchaseException;
import net.okhotnikov.everything.model.Role;
import net.okhotnikov.everything.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PaymentService {

    @Value("${apple.sandbox}")
    private boolean isSandbox;

    public static final String APPSTORE_SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
    public static final String APPSTORE_PROD_URL = "https://buy.itunes.apple.com/verifyReceipt";

    private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);


    private final UserService userService;
    private final RestDao restDao;
    private final RedisService redisService;

    public PaymentService(UserService userService, RestDao restDao, RedisService redisService) {
        this.userService = userService;
        this.restDao = restDao;
        this.redisService = redisService;
    }


    public TokenResponse appStorePurchase(String username, String token) throws IOException {
        LOG.debug("verify purchase for: "+ username+ "  with token: " + token);
        StatusResponse response = restDao.post(
                getAppStoreUrl(),
                restDao.getOneFieldMap("receipt-data",token),
                AppleVerificationResponse.class);

        LOG.info(response.toString());
        if (response.status != 0) {
            LOG.error("AppStore receipt verification fails with code: " + response.status);
            throw new UnverifiedPurchaseException(String.valueOf(response.status));
        }

        try {
            AppleVerificationResponse data = (AppleVerificationResponse) response;
            String storedUsername = redisService.get(data.transactionId());

            if (storedUsername != null){
                LOG.info(String.format("Restored purchase transaction %s for user %s ",
                        data.transactionId(), storedUsername));
                return userService.acceptLogin(userService.get(storedUsername));
            } else{
                LOG.info(String.format("Save purchase transaction %s for user %s ",
                        data.transactionId(), username));
                redisService.put(data.transactionId(),username);
            }
        }catch (Exception e){
            LOG.error(
                    String.format("Error getting original purchase for username %s, error: %s, message: %s",
                            username, e.getClass().getName(), e.getMessage()));
        }

        return new TokenResponse(userService.addRole(username, Role.ROLE_READER));
    }

    public String getAppStoreUrl(){
        return  isSandbox ? APPSTORE_SANDBOX_URL : APPSTORE_PROD_URL;
    }
}
