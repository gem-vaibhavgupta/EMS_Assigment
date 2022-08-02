package com.gemini.ems.service;

import com.gemini.ems.dao.EMSDao;
import com.gemini.ems.model.RegisterUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class RegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationService.class);

    private final EMSDao emsDao;

    public RegistrationService(final EMSDao emsDao) {
        this.emsDao = emsDao;
    }

    public UUID register(RegisterUser registerUser) throws ExecutionException, InterruptedException {
        LOGGER.info("Registering user with details {}", registerUser);
        UUID customerId = UUID.randomUUID();
        Boolean flag = CompletableFuture.supplyAsync(() ->
                emsDao.createUser(registerUser, customerId)).get();
        LOGGER.info("User registration flag {}", flag);
        if (flag)
            return customerId;
        else
            return null;
    }
}
