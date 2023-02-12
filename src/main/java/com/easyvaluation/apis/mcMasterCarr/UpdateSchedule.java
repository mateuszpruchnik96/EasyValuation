package com.easyvaluation.apis.mcMasterCarr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class UpdateSchedule {
    private static final Logger log = LoggerFactory.getLogger(UpdateSchedule.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 1000*60*60*24)
    public void updateProductList() {
        log.info(dateFormat.format(new Date()));
    }



    private static void loginToService(){

    }
}
