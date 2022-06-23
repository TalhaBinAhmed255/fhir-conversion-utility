package com.example.springjsontofhirparser;

import com.example.springjsontofhirparser.BusinessLogic.DataService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringJsonToFhirParserApplication {
    public static final Logger LOGGER= LogManager.getLogger(SpringJsonToFhirParserApplication.class);
    static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext=	SpringApplication.run(SpringJsonToFhirParserApplication.class, args);
        DataService dataService=applicationContext.getBean(DataService.class);

        dataService.storeSinglePatient("95067");//stores a single patient.

        //dataService.storeThousandPatientsAsBundles();

//        dataService.storeAllBundles();//SDFSDFS
    }
}
