package com.example.springjsontofhirparser.BusinessLogic;


import com.example.springjsontofhirparser.Dto.*;
import com.example.springjsontofhirparser.Dto.epEncounter.*;
import com.example.springjsontofhirparser.FhirResources.*;
import com.example.springjsontofhirparser.Repositories.BundleRepository;
import com.example.springjsontofhirparser.Repositories.DictionaryRepository;
import com.example.springjsontofhirparser.Repositories.ValueSetRepository;
import com.example.springjsontofhirparser.Util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StoreBundleListTask implements Runnable{

    public static final Logger LOGGER= LogManager.getLogger(StoreBundleListTask.class);

    List<EpEncounter> epEncounterList;

    BundleRepository bundleRepository;

    DictionaryRepository dictionaryRepository;

    ValueSetRepository valueSetRepository;

    ThreadTaskCompleted isTaskCompleted=null;

    ExecutorService executorService= Executors.newFixedThreadPool(10);

    HashMap<String,Dictionary> dictionaryHashMap;
    HashMap<String,ValueSetDictionary> valueSetDictionaryHashMap;

    public StoreBundleListTask(List<EpEncounter> epEncounterList, BundleRepository bundleRepository, DictionaryRepository dictionaryRepository, ValueSetRepository valueSetRepository, ThreadTaskCompleted isTaskCompleted, HashMap<String, Dictionary> dictionaryHashMap, HashMap<String, ValueSetDictionary> valueSetDictionaryHashMap) {
        this.epEncounterList = epEncounterList;
        this.bundleRepository = bundleRepository;
        this.dictionaryRepository = dictionaryRepository;
        this.valueSetRepository = valueSetRepository;
        this.isTaskCompleted = isTaskCompleted;
        this.dictionaryHashMap = dictionaryHashMap;
        this.valueSetDictionaryHashMap = valueSetDictionaryHashMap;
    }



    public StoreBundleListTask(List<EpEncounter> epEncounter, BundleRepository bundleRepository, DictionaryRepository dictionaryRepository,ValueSetRepository valueSetRepository,ThreadTaskCompleted isTaskCompleted) {
        this.epEncounterList = epEncounter;
        this.bundleRepository = bundleRepository;
        this.dictionaryRepository = dictionaryRepository;
        this.valueSetRepository=valueSetRepository;
        this.isTaskCompleted=isTaskCompleted;
    }

    public StoreBundleListTask( BundleRepository bundleRepository, DictionaryRepository dictionaryRepository,ValueSetRepository valueSetRepository) {

        this.bundleRepository = bundleRepository;
        this.dictionaryRepository = dictionaryRepository;
        this.valueSetRepository=valueSetRepository;
    }

    synchronized void  saveBundles(List<Bundle> bundleList) {
        bundleRepository.saveAll(bundleList);
        LOGGER.info("Bundle of batch Size "+bundleList.size()+" is stored!!");
    }

    void saveBundle(Bundle bundle) {
        bundleRepository.save(bundle);
    }



    @Override
    public void run() {
        List<Bundle> bundleList=new LinkedList<>();
        if(epEncounterList!=null) {
            LOGGER.info("EpEncounter List size" +epEncounterList.size() +" Going to submit following number of tasks " + epEncounterList.size());
            for (EpEncounter epEncounter : epEncounterList) {
                try {

                    executorService.submit(new ProcessPatient(dictionaryHashMap,valueSetDictionaryHashMap,dictionaryRepository,valueSetRepository,epEncounter,bundleList));

                }catch(Exception e){
                    LOGGER.error("Exception occured for patientId "+epEncounter.getPatientId());
                }
            }

            while(bundleList.size()!=epEncounterList.size()){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        saveBundles(bundleList);
        bundleList.clear();
        executorService.shutdown();
        isTaskCompleted.isTaskCompleted=true;
    }
}
