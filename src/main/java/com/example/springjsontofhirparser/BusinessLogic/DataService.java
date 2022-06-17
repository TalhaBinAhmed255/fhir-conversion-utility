package com.example.springjsontofhirparser.BusinessLogic;

import com.example.springjsontofhirparser.Dto.Dictionary;
import com.example.springjsontofhirparser.Dto.FhirCustomOids;
import com.example.springjsontofhirparser.Dto.ThreadTaskCompleted;
import com.example.springjsontofhirparser.Dto.ValueSetDictionary;
import com.example.springjsontofhirparser.Dto.epEncounter.EpEncounter;
import com.example.springjsontofhirparser.FhirResources.Bundle;
import com.example.springjsontofhirparser.FhirResources.Entry;
import com.example.springjsontofhirparser.FhirResources.Resource;
import com.example.springjsontofhirparser.Repositories.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DataService {
    public static final Logger LOGGER= LogManager.getLogger(DataService.class);
    @Autowired
    BundleRepository bundleRepository;

    @Autowired
    EpEncounterRepository epEncounterRepository;

    @Autowired
    DictionaryRepository dictionaryRepository;

    @Autowired
    ValueSetRepository valueSetRepository;

    @Autowired
    FhirCustomOidRepository fhirCustomOidRepository;

    boolean isAllPatientsLoaded=false;
    List<EpEncounter>allEpEncounterPatients=new LinkedList<>();

    EpEncounter getEpencounterObjectByPatientId(String patientId){
        return epEncounterRepository.findEpEncounterByPatientId(patientId);
    }

    List<EpEncounter> getAllPatientsUsingPageNumber(int pageNumber,int pageSize){
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        return epEncounterRepository.findAll( paging).getContent();
    }

    //
    public void storeSinglePatient(String patientId ){

        try{
            EpEncounter epEncounter=getEpencounterObjectByPatientId(patientId);
            Bundle bundle=new Bundle();
            List<Resource> resourceList = new LinkedList<>();
            bundle.setId(epEncounter.getPatientId());
            Entry entry=new Entry();
            ProcessPatient processPatient=new ProcessPatient(bundleRepository,dictionaryRepository,valueSetRepository);
            processPatient.mapPatient(bundle,epEncounter,resourceList);
            processPatient.mapCoverages(bundle,epEncounter,resourceList);
            processPatient.mapProcedures(bundle,epEncounter,resourceList);
            processPatient.mapConditions(bundle,epEncounter,resourceList);
            processPatient.mapObservations(bundle,epEncounter,resourceList);
            processPatient.mapEncounter(bundle,epEncounter,resourceList);
            processPatient.mapMedicationAdministrations(bundle,epEncounter,resourceList);
            processPatient.mapMedicationOrdered(bundle,epEncounter,resourceList);

            bundle.setGender(epEncounter.getGender().equals("F")? "female":"male");
            bundle.setBirthDate(processPatient.getFormattedDate(epEncounter.getBirthDateString()));
            bundle.setPayerCodes(processPatient.mapPayersCodeInList(epEncounter.getInsurance()));
            //entry.setResource(resourceList);
            bundle.getEntry().add(resourceList);

            bundleRepository.save(bundle);


            LOGGER.info("Bundle Saved!!!!");
        }catch(Exception e){
            LOGGER.error("Exception Occured for PatientId" +patientId);
        }
    }
//    void startLoadPatientListner(){
//        ExecutorService executorService=Executors.newSingleThreadExecutor();
//        executorService.submit(new LoadPatientListner(epEncounterRepository,isAllPatientsLoaded,allEpEncounterPatients));
//
//    }


  //-------------------------Store Thousand Patients:-
    List<EpEncounter> getThousandEpEncounterPatients(){
        long totalNumberOfElements=getTotalNumberOfElementsInEpEncounter();
        totalNumberOfElements=1000;//this is changed;
        int pageSize=1000;
        int totalPages= (int) (totalNumberOfElements/pageSize);
        int pageNumber=0;

        Pageable paging = PageRequest.of(pageNumber, pageSize);
        LOGGER.info("Task Submitted for PageNumber "+pageNumber);
        return epEncounterRepository.getEpEncounterByEpids( "certificationhedis2020",paging).getContent();
    }

     public void storeThousandPatientsAsBundles(){
         LOGGER.info("Going to process/store 1000 patients !!");

         List<EpEncounter> epEncounterList=getThousandEpEncounterPatients();

         LOGGER.info("All patients are Obtained from Ep_Encounter!!");
         int batchSize=100;
         int epEncounterEnteries=epEncounterList.size();
         int poolSize=10;
         int epEncounterEnteriesProcessed=0;
         int epEncounterEnteriesLeft;
         ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

         for(int i=0;i<epEncounterEnteries;i++){
             epEncounterEnteriesLeft=epEncounterEnteries-epEncounterEnteriesProcessed;
             if(epEncounterEnteriesLeft>=batchSize){
                 executorService.submit(new StoreBundleListTask(epEncounterList.subList(i, i + batchSize),bundleRepository,dictionaryRepository,valueSetRepository,new ThreadTaskCompleted()));
                 i+=batchSize-1;
                 epEncounterEnteriesProcessed+=batchSize;
             }
             else{
                 executorService.submit(new StoreBundleListTask(epEncounterList.subList(i,i+epEncounterEnteriesLeft),bundleRepository,dictionaryRepository,valueSetRepository,new ThreadTaskCompleted()));
                 i+=epEncounterEnteriesLeft;
                 epEncounterEnteriesProcessed+=epEncounterEnteriesLeft;
             }
         }

         while(true){
             if(executorService.isTerminated()){
                 executorService.shutdown();
                 LOGGER.info("Thread pool Succesfully shutdown!!");
                 break;
             }
             try {
                 Thread.sleep(10000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
     }

    //------------------------------Store all Patients-------------------------------------------------
    long getTotalNumberOfElementsInEpEncounter(){
        int page=0,size=1;
        Pageable paging = PageRequest.of(page, size);
        return epEncounterRepository.getEpEncounterByEpids( "certificationhedis2020",paging).getTotalElements();
    }

    int getTotalNumberOfPagesInEpEncounter(){
        long totalNumberOfElements=getTotalNumberOfElementsInEpEncounter();
        int pageSize=1000;
        return (int) (totalNumberOfElements/pageSize);
    }

    List<EpEncounter> getEpEncounterPatientsUsingPageNumber(int pageNumber){
        long totalNumberOfElements=getTotalNumberOfElementsInEpEncounter();
        //totalNumberOfElements=1000;//this is changed;
        int pageSize=1000;//
        int totalPages= getTotalNumberOfPagesInEpEncounter();
        // int pageNumber=0;

        Pageable paging = PageRequest.of(pageNumber, pageSize);
        LOGGER.info("Task Submitted for PageNumber "+pageNumber);
        return epEncounterRepository.getEpEncounterByEpids( "certificationhedis2020",paging).getContent();
    }

    boolean isAllTasksCompletedByThreads(List<ThreadTaskCompleted> isAllTasksCompleted){
        for(ThreadTaskCompleted isTaskCompleted:isAllTasksCompleted){
            if(isTaskCompleted.isTaskCompleted==false){
                return false;
            }
        }
        return true;
    }

    HashMap<String,Dictionary> getDictionariesHashMap(){
        LOGGER.info("Going to generate Dictionary HashMap!");
        List<FhirCustomOids> fhirCustomOidsList=fhirCustomOidRepository.findAll();
        HashMap<String,Dictionary> dictionaryHashMap=new LinkedHashMap<>();
        for(FhirCustomOids fhirCustomOids:fhirCustomOidsList){
            Dictionary dictionary=dictionaryRepository.findDictionaryByOid(fhirCustomOids.getOid());
            if(dictionary!=null) {
                List<String> values = dictionary.getValues();
                for (String value : values) {
                    dictionaryHashMap.put(value, dictionary);
                }
            }
        }
        return dictionaryHashMap;
    }

    HashMap<String, ValueSetDictionary> getValueSetHashMap(){
        LOGGER.info("Going to generate ValueSet HashMap!");

        HashMap<String,ValueSetDictionary> valueSetDictionaryHashMap=new LinkedHashMap<>();
        List<ValueSetDictionary> valueSetDictionaryList=valueSetRepository.findAll();

        for (ValueSetDictionary valueSetDictionary:valueSetDictionaryList){
            valueSetDictionaryHashMap.put(valueSetDictionary.getCode(),valueSetDictionary);
        }
        return valueSetDictionaryHashMap;
    }

    public void storeAllBundles() {
        LOGGER.info("Going to store all bundles!!");

        HashMap<String,Dictionary> dictionaryHashMap=getDictionariesHashMap();
        HashMap<String,ValueSetDictionary> valueSetDictionaryHashMap=getValueSetHashMap();

        int totalPagesInEpEncounter = getTotalNumberOfPagesInEpEncounter();
        int poolSize = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        for (int pageNumber = 0; pageNumber <= totalPagesInEpEncounter; pageNumber++) {

            List<EpEncounter> epEncounterList = getEpEncounterPatientsUsingPageNumber(pageNumber);
            LOGGER.info("All patients are Obtained from Ep_Encounter!!" + ", PatientList Size = "+epEncounterList.size());

            List<ThreadTaskCompleted> isAllTasksCompleted=new LinkedList<>();
            int batchSize = 100;
            int epEncounterEnteries = epEncounterList.size();
            int epEncounterEnteriesProcessed = 0;
            int epEncounterEnteriesLeft;

            for (int i = 0; i < epEncounterEnteries; i++) {
                epEncounterEnteriesLeft = epEncounterEnteries - epEncounterEnteriesProcessed;
                if (epEncounterEnteriesLeft >= batchSize) {
                    ThreadTaskCompleted isTaskCompleted=new ThreadTaskCompleted();
                    isAllTasksCompleted.add(isTaskCompleted);
                    executorService.submit(new StoreBundleListTask(epEncounterList.subList(i, i + batchSize ), bundleRepository, dictionaryRepository, valueSetRepository,isTaskCompleted,dictionaryHashMap,valueSetDictionaryHashMap));
                    i += batchSize - 1;
                    epEncounterEnteriesProcessed += batchSize;
                } else {
                    ThreadTaskCompleted isTaskCompleted=new ThreadTaskCompleted();
                    isAllTasksCompleted.add(isTaskCompleted);
                    executorService.submit(new StoreBundleListTask(epEncounterList.subList(i, i + epEncounterEnteriesLeft), bundleRepository, dictionaryRepository, valueSetRepository,isTaskCompleted,dictionaryHashMap,valueSetDictionaryHashMap));
                    i += epEncounterEnteriesLeft;
                    epEncounterEnteriesProcessed += epEncounterEnteriesLeft;
                }
            }

            //
            while(true){
                if(isAllTasksCompletedByThreads(isAllTasksCompleted)){
                    //executorService.shutdown();
                    LOGGER.info("****** Patients Stored as bundles for PageNumber = "+pageNumber+"\"******");
                    break;
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        executorService.shutdown();
        LOGGER.info("****** Shutting down the thread pool ******");
    }
}
