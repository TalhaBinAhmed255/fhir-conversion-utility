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

public class ProcessPatient implements Runnable{
    public static final Logger LOGGER= LogManager.getLogger(ProcessPatient.class);

    public ProcessPatient(DictionaryRepository dictionaryRepository, ValueSetRepository valueSetRepository, EpEncounter epEncounter, List<Bundle> bundleList) {
        this.dictionaryRepository = dictionaryRepository;
        this.valueSetRepository = valueSetRepository;
        this.epEncounter = epEncounter;
        this.bundleList = bundleList;
    }

    public ProcessPatient( BundleRepository bundleRepository, DictionaryRepository dictionaryRepository,ValueSetRepository valueSetRepository) {

        this.bundleRepository = bundleRepository;
        this.dictionaryRepository = dictionaryRepository;
        this.valueSetRepository=valueSetRepository;
    }

    public ProcessPatient(HashMap<String, Dictionary> dictionaryHashMap, HashMap<String, ValueSetDictionary> valueSetDictionaryHashMap, DictionaryRepository dictionaryRepository, ValueSetRepository valueSetRepository, EpEncounter epEncounter, List<Bundle> bundleList) {
        this.dictionaryHashMap = dictionaryHashMap;
        this.valueSetDictionaryHashMap = valueSetDictionaryHashMap;
        this.dictionaryRepository = dictionaryRepository;
        this.valueSetRepository = valueSetRepository;
        this.epEncounter = epEncounter;
        this.bundleList = bundleList;
    }

    HashMap<String,Dictionary> dictionaryHashMap;
    HashMap<String,ValueSetDictionary> valueSetDictionaryHashMap;

    DictionaryRepository dictionaryRepository;
    ValueSetRepository valueSetRepository;
    EpEncounter epEncounter;
    List<Bundle> bundleList;
    BundleRepository bundleRepository;

    public Dictionary getDictionaryByCode(String code){
        //this is added------------------
//        if(dictionaryHashMap.get(code)!=null){
//            return dictionaryHashMap.get(code);
//        }
        //------------------------
        List<Dictionary> dictionaryList=dictionaryRepository.findDictionaryByValues(code);
        if(dictionaryList!=null && dictionaryList.size()!=0){
            return dictionaryList.get(0);
        }
        return new Dictionary();
    }

    public ValueSetDictionary getValueSetDictionaryByCode(String code){
//        if(valueSetDictionaryHashMap.get(code)!=null){
//            return valueSetDictionaryHashMap.get(code);
//        }

        List<ValueSetDictionary> valueSetDictionaryList=valueSetRepository.getValueSetDictionaryByCode(code);
        return (valueSetDictionaryList.size()!=0 && valueSetDictionaryList.get(0) != null) ? valueSetDictionaryList.get(0) : new ValueSetDictionary();
    }

    Date getParsedDateInRequiredFormat(String date, String format){
        SimpleDateFormat sdformat = new SimpleDateFormat(format);
        try{
            return sdformat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    boolean isCodeMatchesCodeType(String code,String codeType){
        Dictionary dictionary=getDictionaryByCode(code);
        if(codeType.equals(dictionary.getOid())){
            return true;
        }
        return false;
    }

    String getHospiceFlag(List<Premium> premiumList){
        if(null!=premiumList && premiumList.size()>0) {
            for (Premium premium : premiumList) {
                if (null != premium.getHospice() && premium.getHospice().equals("Y") &&  null!=premium.getHospiceDateString() && premium.getHospiceDateString().substring(0, 4).equals("2022")) {
                    return "Y";
                }
            }
        }
        return "N";
    }

    void mapSpecialPayerCodes(List<String> payersList,String payerCode){
        if(payerCode.equals("MD") || payerCode.equals("MDE") || payerCode.equals("MLI")|| payerCode.equals("MRB") || payerCode.equals("MMP")){
            payersList.add("MCD");
        }
        else if(payerCode.equals("SN1") || payerCode.equals("SN2") || payerCode.equals("SN3")){
            payersList.add("MCR");
        }
        else{
            payersList.add(payerCode);
        }
    }

    public List<PayerInfo> mapPayersInfoInList(List<Insurance>insuranceList){
        List<PayerInfo> payerInfoList=new LinkedList<>();
        if(insuranceList != null && insuranceList.size() != 0){
            for (int i = 0; i < insuranceList.size(); i++) {
                PayerInfo payerInfo=new PayerInfo();
                payerInfo.setPayerCode(insuranceList.get(i).getPayerCode());
                payerInfo.setCoverageStartDate(insuranceList.get(i).getCoverageStartDate());
                payerInfo.setCoverageEndDate(insuranceList.get(i).getCoverageEndDate());
                payerInfo.setCoverageStartDateString(insuranceList.get(i).getCoverageStartDateString());
                payerInfo.setCoverageEndDateString(insuranceList.get(i).getCoverageEndDateString());

                payerInfoList.add(payerInfo);
            }
        }
        return payerInfoList;
    }


    public List<String> mapPayersCodeInList(List<Insurance>insuranceList){
        List<String> payersList=new LinkedList<>();
        if(insuranceList != null && insuranceList.size() != 0) {
            Date measurementPeriodEndingDate = getParsedDateInRequiredFormat("2022-12-31", "yyyy-MM-dd");
            Date insuranceDate = null;
            for (int i = 0; i < insuranceList.size(); i++) {
                insuranceDate = insuranceList.get(i).getCoverageEndDate();
                if (null!=insuranceDate && insuranceDate.compareTo(measurementPeriodEndingDate) >= 0 && !insuranceList.get(i).getCoverageStartDateString().equals("20240101")) {
//                    payersList.add(insuranceList.get(i).getPayerCode());
                    mapSpecialPayerCodes(payersList,insuranceList.get(i).getPayerCode());
                }
            }

            //If no payer matches the above condition than the recent payer code in appended in payerlist
            //Commenting as Faizan bhai said.
            if (payersList.isEmpty() || payersList.size() == 0) {

                String lastCoverageObjectStartDate=insuranceList.get(insuranceList.size() - 1).getCoverageStartDateString();
                String lastCoverageObjectEndDate=insuranceList.get(insuranceList.size() - 1).getCoverageEndDateString();
                if((null!=lastCoverageObjectStartDate) && (null!=lastCoverageObjectEndDate )) {

                    if(getDictionaryByCode(insuranceList.get(insuranceList.size() - 1).getPayerCode()).getOid().equals(Constants.CODE_TYPE_COMMERCIAL)){
                        if (!lastCoverageObjectStartDate.equals("20240101") && (lastCoverageObjectEndDate.substring(0, 4).equals("2022")   ||  lastCoverageObjectEndDate.substring(0, 4).equals("2020") || lastCoverageObjectEndDate.substring(0, 4).equals("2021")   )) {
                            mapSpecialPayerCodes(payersList, insuranceList.get(insuranceList.size() - 1).getPayerCode());
                        }
                    }
                    else if ((!lastCoverageObjectStartDate.equals("20240101") ) && (lastCoverageObjectEndDate.substring(0, 4).equals("2022"))) {
                        mapSpecialPayerCodes(payersList, insuranceList.get(insuranceList.size() - 1).getPayerCode());
                    }
                }
            }
        }
        return payersList;
    }

    public void mapCoverageCodingObject(Coding coding, String code){
        Dictionary dictionary=getDictionaryByCode(code);
        if(dictionary.getOid().equals(Constants.CODE_TYPE_COMMERCIAL) ||dictionary.getOid().equals("HEDIS.Payer.Exchange.Codes.22") ||dictionary.getOid().equals("HEDIS.Payer.Custom.Codes.22") ){
            coding.setCode(Constants.CODE_MCPOL);
            coding.setSystem(Constants.ACTCODE_SYSTEM);
        }else if(dictionary.getOid().equals(Constants.CODE_TYPE_MEDICAID)){
            coding.setCode(Constants.CODE_SUBSIDIZ);
            coding.setSystem(Constants.ACTCODE_SYSTEM);
        }else if(dictionary.getOid().equals(Constants.CODE_TYPE_MEDICARE)){
            coding.setCode(Constants.CODE_RETIRE);
            coding.setSystem(Constants.ACTCODE_SYSTEM);
        }else{
            coding.setCode(code);
            coding.setSystem(dictionary.getOid());
        }
    }

    public void mapCodingObjectByDictionaries(Coding coding,String code){
        ValueSetDictionary valueSetDictionary=getValueSetDictionaryByCode(code);
        if( valueSetDictionary!=null && valueSetDictionary.getSystem()!=null ){
            coding.setSystem(valueSetDictionary.getSystem());
            coding.setCode(code);
            coding.setDisplay(valueSetDictionary.getDisplay());
            coding.setVersion(valueSetDictionary.getVersion());
        }
        else{
            Dictionary dictionary=getDictionaryByCode(code);
            coding.setSystem("urn:oid:"+dictionary.getOid());
            coding.setDisplay(dictionary.getName());
            coding.setCode(code);
        }
    }
    String getFormattedDate(String date){

        if(date!=null){
            return (date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8));
        }
        return "";
    }

    void mapPatient(Bundle bundle, EpEncounter epEncounter, List<Resource> resourceList){
        if(epEncounter !=null) {
            List<Identifier> identifierList=new LinkedList<>();
            Patient patient = new Patient();
            patient.setId(epEncounter.getPatientId());
            patient.setBirthDate(getFormattedDate(epEncounter.getBirthDateString()));

            List<String> given=new LinkedList<>();
            given.add(epEncounter.getGivenName());
            Name name=new Name();
            name.setGiven(given);
            name.setFamily(epEncounter.getFamilyName());

            //patient.setName(name);
            patient.getName().add(name);
            patient.setGender(epEncounter.getGender().equals("F")? "female":"male");


            if(epEncounter.getInsurance()!=null) {
                for (int i = 0; i < epEncounter.getInsurance().size(); i++) {
                    Insurance insurance = epEncounter.getInsurance().get(i);
                    Identifier identifier = new Identifier();
                    identifier.setValue(epEncounter.getPatientId());
                    identifier.getAssigner().setDisplay(insurance.getPayerName());
                    identifierList.add(identifier);
                }
            }
            patient.setIdentifier(identifierList);

            ResourceChild resourceChild=new ResourceChild();
            resourceChild.setResource(patient);
            resourceList.add(resourceChild);
        }
    }

    void mapCoverages(Bundle bundle,EpEncounter epEncounter,List<Resource> resourceList){
        if(epEncounter !=null && epEncounter.getInsurance()!=null) {
            for(int i=0;i<epEncounter.getInsurance().size();i++){
                Insurance insurance= epEncounter.getInsurance().get(i);
                Coverage coverage=new Coverage();
                coverage.setId(epEncounter.getPatientId());
                coverage.getPolicyHolder().setReference("Patient/"+epEncounter.getPatientId());


                coverage.getPeriod().setStart(insurance.getCoverageStartDateTime()!=null? Constants.getIsoDateInRequiredFormat(insurance.getCoverageStartDateTime()):"");
                coverage.getPeriod().setEnd(insurance.getCoverageEndDateTime()!=null? Constants.getIsoDateInRequiredFormat(insurance.getCoverageEndDateTime()):"");


                coverage.getPayor().setReference("Organization/"+insurance.getPayerName());
                coverage.getBeneficiary().setReference("Patient/"+epEncounter.getPatientId());
                coverage.getSubscriber().setReference("Patient/"+epEncounter.getPatientId());

                Identifier identifier=new Identifier();
                identifier.setValue(epEncounter.getPatientId());
                identifier.getAssigner().setDisplay(insurance.getPayerName());
                List<Identifier> identifierList=new LinkedList<>();
                identifierList.add(identifier);
                coverage.setIdentifier(identifierList);

                Type type=new Type();
                Coding coding=new Coding();

                mapCoverageCodingObject(coding,insurance.getPayerCode());
                type.getCoding().add(coding);

                coverage.setType(type);

                ResourceChild resourceChild=new ResourceChild();
                resourceChild.setResource(coverage);
                resourceList.add(resourceChild);


            }


        }
    }

    String getEncounterCode(EpEncounter epEncounter){
        if(epEncounter!=null){
            if(epEncounter.getEncounterStatus().get(0)!=null){
                return epEncounter.getEncounterStatus().get(0).getCode();
            }
        }
        return "";
    }

    List<Reference>getLabResultsCodes(EpEncounter epEncounter){
        List<Reference> report=new LinkedList<>();
        if(epEncounter.getLabTestResult()!=null) {
            for (LabTestResults labResult : epEncounter.getLabTestResult()) {
                Reference reference = new Reference();
                reference.setReference("DiagnosticReport/" + labResult.getTestCode());
                report.add(reference);

            }
        }
        return report;
    }

    void mapProcedures(Bundle bundle,EpEncounter epEncounter,List<Resource> resourceList){
        if(epEncounter !=null) {
            if(epEncounter.getProcedurePerformed()!=null){
                for(int i=0;i<epEncounter.getProcedurePerformed().size();i++) {

                    if(epEncounter.getProcedurePerformed().get(i).getPosCode()==null || !epEncounter.getProcedurePerformed().get(i).getPosCode().equals("81")){

                        ProcedurePerformed procedurePerformed = epEncounter.getProcedurePerformed().get(i);
                        Procedure procedure = new Procedure();
                        procedure.setId(epEncounter.getPatientId());
                        procedure.setStatus(procedurePerformed.getEndDate() == null ? "in-progress" : "completed");

                        Code code = new Code();

                        Coding coding = new Coding();
                        mapCodingObjectByDictionaries(coding, procedurePerformed.getProcedureCode());
                        code.getCoding().add(coding);



                        procedure.setCode(code);

                        procedure.getSubject().setReference("Patient/" + epEncounter.getPatientId());

                        procedure.getEncounter().setReference("Encounter/" + getEncounterCode(epEncounter));

                        procedure.getPerformedPeriod().setStart(getFormattedDate(procedurePerformed.getPerformedDateString()) + "T00:00:00.0" );
                        if(procedurePerformed.getEndDateString()!=null) {
                            procedure.getPerformedPeriod().setEnd(getFormattedDate(procedurePerformed.getEndDateString()) + "T00:00:00.0");
                        }

                        procedure.setReport(getLabResultsCodes(epEncounter));

                        ResourceChild resourceChild = new ResourceChild();
                        resourceChild.setResource(procedure);
                        resourceList.add(resourceChild);

                    }
                }
            }}
    }

    void mapConditions(Bundle bundle,EpEncounter epEncounter,List<Resource> resourceList){
        if(epEncounter !=null && epEncounter.getProblem()!=null) {
            for(int i=0;i<epEncounter.getProblem().size();i++){
                if(epEncounter.getProblem().get(i).getPosCode()==null || !epEncounter.getProblem().get(i).getPosCode().equals("81")){//jis problem ma poscode 81 hou uska condition ka object ni banana


                    Problem problem= epEncounter.getProblem().get(i);
                    Condition condition=new Condition();
                    condition.setId("Condition/"+problem.getProblemCode()+"/"+problem.getProblemDateString());

                    ClinicalStatusCoding clinicalStatusCoding=new ClinicalStatusCoding();
                    clinicalStatusCoding.setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical");
                    clinicalStatusCoding.setCode("active");
                    condition.getClinicalStatus().getCoding().add(clinicalStatusCoding);

                    Coding verificationStatusCoding=new Coding();
                    verificationStatusCoding.setSystem("http://terminology.hl7.org/CodeSystem/condition-ver-status");
                    verificationStatusCoding.setCode("confirmed");
                    condition.getVerificationStatus().getCoding().add(verificationStatusCoding);

                    Coding problemCategoryCoding=new Coding();
                    problemCategoryCoding.setSystem("http://terminology.hl7.org/CodeSystem/condition-category");
                    problemCategoryCoding.setCode("problem-list-item");
                    problemCategoryCoding.setDisplay("Problem List Item");
                    Type problemCategoryType=new Type();
                    problemCategoryType.getCoding().add(problemCategoryCoding);
                    condition.getCategory().add(problemCategoryType);

                    Code problemCode=new Code();
                    Coding problemCoding=new Coding();

                    mapCodingObjectByDictionaries(problemCoding,problem.getProblemCode());
                    problemCode.getCoding().add(problemCoding);
                    condition.setCode(problemCode);

                    condition.getSubject().setReference("Patient/"+epEncounter.getPatientId());

                    condition.getEncounter().setReference("Encounter/"+problem.getVisitId());

                    condition.setOnsetDateTime(problem.getProblemDateTime()!=null? Constants.getIsoDateInRequiredFormat(problem.getProblemDateTime()):"");
                    condition.setRecordedDate(problem.getProblemDate()!=null?Constants.getIsoDateInRequiredFormat(problem.getProblemDate()):"");

                    if(problem.getEndDate()!=null){
                        condition.setAbatementDateTime(Constants.getIsoDateInRequiredFormat(problem.getEndDate()));
                    }

                    ResourceChild resourceChild=new ResourceChild();
                    resourceChild.setResource(condition);
                    resourceList.add(resourceChild);

                }
            }
        }
    }

    void mapVitalSign(List<VitalSignsChilds> vitalSignsChildsList,List<Component> componentList){
        if(vitalSignsChildsList!=null && vitalSignsChildsList.size()!=0  ) {
            for (VitalSignsChilds vitalSignsChild : vitalSignsChildsList) {
                Component component = new Component();
                Code code = new Code();
                List<Coding> codingList = new LinkedList<>();
                Coding coding = new Coding();

                mapCodingObjectByDictionaries(coding,vitalSignsChild.getLoinc());
                codingList.add(coding);
                code.setCoding(codingList);

                component.setCode(code);

                component.getValueQuantity().setValue(vitalSignsChild.getNumericValue());
                component.getValueQuantity().setSystem("http://unitsofmeasure.org");
                component.getValueQuantity().setCode(vitalSignsChild.getUnit());
                component.getValueQuantity().setUnit(vitalSignsChild.getUnit());

                componentList.add(component);
            }
        }
    }

    List<Component> mapVitalSigns(VitalSigns vitalSigns){
        List<Component> componentList=new LinkedList<>();

        List<VitalSignsChilds> hr=vitalSigns.getHr();
        List<VitalSignsChilds> bmi=vitalSigns.getBmi();
        List<VitalSignsChilds> bpd=vitalSigns.getBpd();
        List<VitalSignsChilds> bps=vitalSigns.getBps();
        List<VitalSignsChilds> temp=vitalSigns.getTemp();
        List<VitalSignsChilds> height=vitalSigns.getHeight();
        List<VitalSignsChilds> weight=vitalSigns.getWeight();

        mapVitalSign(hr,componentList);
        mapVitalSign(bmi,componentList);
        mapVitalSign(bpd,componentList);
        mapVitalSign(bps,componentList);
        mapVitalSign(temp,componentList);
        mapVitalSign(height,componentList);
        mapVitalSign(weight,componentList);

        return componentList;
    }




    void mapObservations(Bundle bundle,EpEncounter epEncounter,List<Resource> resourceList){
        if(epEncounter !=null && epEncounter.getLabTestResult()!=null) {
            for(int i=0;i<epEncounter.getLabTestResult().size();i++){
                LabTestResults labTestResults = epEncounter.getLabTestResult().get(i);
                Observation observation=new Observation();
                observation.setId(epEncounter.getPatientId());
                observation.setStatus("final");//for the time being ye rakha

                Code code=new Code();
                Coding coding=new Coding();
                mapCodingObjectByDictionaries(coding,labTestResults.getTestCode());
                code.getCoding().add(coding);


                observation.setCode(code);

                observation.getSubject().setReference("Patient/"+epEncounter.getPatientId());

                observation.getEncounter().setReference("Encounter/"+getEncounterCode(epEncounter));

                observation.getEffectivePeriod().setStart(labTestResults.getReportedDateTime()!=null?Constants.getIsoDateInRequiredFormat(labTestResults.getReportedDateTime()):"");
                observation.getEffectivePeriod().setEnd(labTestResults.getReportedDateTime()!=null?Constants.getIsoDateInRequiredFormat(labTestResults.getReportedDateTime()):"");//same because we dont have end ISO Date

                observation.setIssued(labTestResults.getReportedDateTime()!=null?Constants.getIsoDateInRequiredFormat(labTestResults.getReportedDateTime()):"");

                observation.setValueInteger(Integer.parseInt(labTestResults.getNumericResult()));

                if(labTestResults.getNumericResult() !=null){
                    observation.setValueQuantity(new ValueQuantity());
                    observation.getValueQuantity().setValue( labTestResults.getNumericResult());
                    observation.getValueQuantity().setUnit(labTestResults.getNumericResultUnit());
                    observation.getValueQuantity().setSystem("http://unitsofmeasure.org");
                }



                List<Performer>performerList=new LinkedList<>();
                Actor actor=new Actor();
                actor.setReference("Practitioner/"+epEncounter.getProviderId());
                actor.setDisplay("Practitioner/"+epEncounter.getProviderId());
                Performer performer=new Performer();
                performer.setActor(actor);
                performerList.add(performer);
                observation.setPerformer(performerList);

                observation.setComponent(mapVitalSigns(epEncounter.getVitalSigns()));

                ResourceChild resourceChild=new ResourceChild();
                resourceChild.setResource(observation);
                resourceList.add(resourceChild);
            }
        }
    }

    boolean isEncounterConditionResourceDatesOverlap(EncounterStatus encounterStatus,Problem problem){
        Date encounterStartDate = encounterStatus.getStartDate() ,encounterEndDate=encounterStatus.getEndDate(),
                problemStartDate = problem.getProblemDate(),problemEndDate = problem.getEndDate();

        if(problemEndDate==null){
            if(encounterEndDate.compareTo(problemStartDate)>=0){
                return true;
            }
        }
        else{
            if(problemEndDate.compareTo(encounterStartDate)>=0 && problemEndDate.compareTo(encounterEndDate)<=0){
                return true;
            }
        }
        return false;
    }

    void mapEncounter(Bundle bundle,EpEncounter epEncounter,List<Resource> resourceList){
        if(epEncounter !=null && epEncounter.getEncounterStatus()!=null) {
            List<String> mappedProblemObects=new LinkedList<>();
            for(int i=0;i<epEncounter.getEncounterStatus().size();i++) {

                if(epEncounter.getEncounterStatus().get(i).getPosCode()==null || !epEncounter.getEncounterStatus().get(i).getPosCode().equals("81")){


                    EncounterStatus encounterStatus = epEncounter.getEncounterStatus().get(i);
                    Encounter encounter = new Encounter();
                    encounter.setId(epEncounter.getPatientId());

                    encounter.setStatus(encounterStatus.getEndDate() == null ? "in-progress" : "finished");

                    Coding classCoding = new Coding();

                    mapCodingObjectByDictionaries(classCoding, encounterStatus.getCode());
                    encounter.set_class(classCoding);

                    Type type = new Type();
                    List<Coding> codingList = new LinkedList<>();
                    codingList.add(classCoding);
                    type.setCoding(codingList);
                    encounter.getType().add(type);

                    encounter.getSubject().setReference("Patient/" + epEncounter.getPatientId());

                    if (epEncounter.getProblem() != null) {
                        for (Problem problem : epEncounter.getProblem()) {

                            String problemId= "Condition/" +problem.getProblemCode() + "/"+problem.getProblemDateString() ;

                            //problem pos code should not be =81 and problem visitId and encounter visitId should match and should be on same dates
//                            if (((problem.getPosCode()==null) || !(problem.getPosCode().equals("81"))) &&
//                                    (null!=problem.getVisitId() && null!=encounterStatus.getVisitId() && problem.getVisitId().equals(encounterStatus.getVisitId())
//                                     && null!=problem.getProblemDate() && null!=encounterStatus.getStartDateString() && problem.getProblemDateString().equals(encounterStatus.getStartDateString()))) {
//                            if (((problem.getPosCode()==null) || !(problem.getPosCode().equals("81")))
//                                    && null!=problem.getProblemDate() && null!=encounterStatus.getStartDateString() && problem.getProblemDateString().equals(encounterStatus.getStartDateString())) {
//                            if (((problem.getPosCode()==null) || !(problem.getPosCode().equals("81")))
//                                    && isEncounterConditionResourceDatesOverlap(encounterStatus,problem)
//                             && !mappedProblemObects.contains(problemId)) {
                            if (((problem.getPosCode()==null) || !(problem.getPosCode().equals("81")))
                                    && isEncounterConditionResourceDatesOverlap(encounterStatus,problem)
                                    ) {

                                Diagnosis diagnosis = new Diagnosis();
                                diagnosis.getCondition().setReference(problemId);
                                Coding diagnosisCoding = new Coding();
//                        dictionary = getDictionaryByCode(problem.getProblemCode());
//                        if (dictionary != null) {
//                            diagnosisCoding.setSystem("urn:oid:" + dictionary.getOid());
//                            diagnosisCoding.setDisplay(dictionary.getName());
//                        }
//                        diagnosisCoding.setCode(encounterStatus.getCode());
                                mapCodingObjectByDictionaries(diagnosisCoding, problem.getProblemCode());

                                Type diagnosisUse = new Type();
                                List<Coding> diagnosisCodingList = new LinkedList<>();
                                diagnosisCodingList.add(diagnosisCoding);
                                diagnosisUse.setCoding(diagnosisCodingList);

                                diagnosis.setUse(diagnosisUse);
//                        diagnosis.getCondition().setReference("Condition/" + dictionary.getName());
                                diagnosis.setRank(problem.getProblemPriority() != null ? Integer.parseInt(problem.getProblemPriority()) : 0);
                                encounter.getDiagnosis().add(diagnosis);

                                mappedProblemObects.add(problemId);
                            }


                        }
                    }

                    encounter.getPeriod().setStart(getFormattedDate(encounterStatus.getStartDateString()) + "T00:00:00.0" );
                    encounter.getPeriod().setEnd(getFormattedDate(encounterStatus.getEndDateString()) + "T00:00:00.0" );//same because we dont have end ISO Date

                    encounter.getServiceProvider().setReference("Practitioner/" + epEncounter.getProviderId());

                    ResourceChild resourceChild = new ResourceChild();
                    resourceChild.setResource(encounter);
                    resourceList.add(resourceChild);

                }
            }
        }
    }

    void mapMedicationAdministrations(Bundle bundle,EpEncounter epEncounter,List<Resource> resourceList){
        if(epEncounter !=null && epEncounter.getMedicationAdministered()!=null) {
            for(int i=0;i<epEncounter.getMedicationAdministered().size();i++){
                MedicationAdministered medicationAdministered= epEncounter.getMedicationAdministered().get(i);
                MedicationAdministration medicationAdministration=new MedicationAdministration();
                medicationAdministration.setId(epEncounter.getPatientId());
                medicationAdministration.setStatus(medicationAdministered.getEndDate()==null?"in-progress":"completed");

                medicationAdministration.getMedicationReference().setReference("Medication/"+medicationAdministered.getMedicationCode());

                medicationAdministration.getSubject().setReference("Patient/"+epEncounter.getPatientId());

                medicationAdministration.getEffectivePeriod().setStart(medicationAdministered.getAdministeredDate()!=null?Constants.getIsoDateInRequiredFormat(medicationAdministered.getAdministeredDate()):"");
                medicationAdministration.getEffectivePeriod().setEnd(medicationAdministered.getEndDate()!=null ? Constants.getIsoDateInRequiredFormat(medicationAdministered.getEndDate()):"");

                Dosage dosage=new Dosage();
                Coding dosageCoding=new Coding();
                dosageCoding.setDisplay(medicationAdministered.getMedicationDescription());
                dosageCoding.setSystem(medicationAdministered.getMedicationCodesystem());
                dosageCoding.setCode(medicationAdministered.getMedicationCode());
                dosage.getRoute().getCoding().add(dosageCoding);

                DoseQuantity dose=new DoseQuantity();
                if(epEncounter.getMedicationList()!=null) {
                    for (MedicationList medicationList : epEncounter.getMedicationList()) {
                        if (medicationAdministered.getMedicationCode().equals(medicationList.getMedicationCode())) {
                            dose.setCode(medicationList.getDoseUnit());
                            dose.setSystem(medicationList.getMedicationCodesystem());
                            dose.setUnit(medicationList.getDoseUnit());
                            dose.setValue(medicationList.getDose()!=null ? Integer.parseInt(medicationList.getDose()):0);
                        }
                    }
                }
                dosage.setDose(dose);

                medicationAdministration.setDosage(dosage);
                ResourceChild resourceChild=new ResourceChild();
                resourceChild.setResource(medicationAdministration);
                resourceList.add(resourceChild);            }
        }
    }


    void mapMedicationOrdered(Bundle bundle,EpEncounter epEncounter,List<Resource> resourceList){
        if(epEncounter !=null && epEncounter.getMedicationOrdered()!=null) {
            for(int i=0;i<epEncounter.getMedicationOrdered().size();i++){
                MedicationOrdered medicationOrdered= epEncounter.getMedicationOrdered().get(i);
                MedicationDispense medicationDispense=new MedicationDispense();

                medicationDispense.setId(epEncounter.getPatientId());

                if(medicationOrdered.getMedicationStatusIsActive()!=null) {
                    medicationDispense.setStatus(medicationOrdered.getMedicationStatusIsActive() == true ? "in-progress" : "completed");
                }
                medicationDispense.getMedicationReference().setReference("Medication/"+medicationOrdered.getMedicationCode());

                medicationDispense.getSubject().setReference("Patient/"+epEncounter.getPatientId());

                Performer performer=new Performer();
                Actor actor=new Actor();
                actor.setReference("Practitioner/"+epEncounter.getProviderId());
                actor.setDisplay("Practitioner/"+epEncounter.getProviderId());
                performer.setActor(actor);
                medicationDispense.getPerformer().add(performer);


                medicationDispense.getQuantity().setSystem("http://unitsofmeasure.org");
                medicationDispense.getQuantity().setValue(medicationOrdered.getOrderQuantity()!=null?Integer.parseInt(medicationOrdered.getOrderQuantity()):0);

                int daySupply=0;
                double frequency=0;
                double dose=0;
                double orderQuantity=0;
                String periodUnit="";
                String doseUnit="";
                if(epEncounter.getMedicationList()!=null) {
                    for (MedicationList medicationList : epEncounter.getMedicationList()) {
                        if (medicationOrdered.getMedicationCode().equals(medicationList.getMedicationCode())) {
                            if(medicationList.getFrequency() !=null){
                                frequency=Double.parseDouble(medicationList.getFrequency());
                            }
                            if(medicationList.getDose() !=null){
                                dose=Double.parseDouble(medicationList.getDose());
                            }
                            if(medicationOrdered.getOrderQuantity() !=null){
                                orderQuantity=Double.parseDouble(medicationOrdered.getOrderQuantity());
                            }
                            periodUnit=medicationList.getFrequencyUnit();
                            doseUnit=medicationList.getDoseUnit();
                            if(frequency>0 && dose>0 && orderQuantity>0){
                                daySupply=(int)(Math.ceil(orderQuantity/(dose *frequency )));
                                medicationDispense.getDaysSupply().setValue(daySupply);
                                medicationDispense.getDaysSupply().setUnit("Day");
                                medicationDispense.getDaysSupply().setCode("d");
                                medicationDispense.getDaysSupply().setSystem("http://unitsofmeasure.org");
                            }
                            medicationDispense.setWhenHandedOver(medicationList.getStartDate()!=null ? Constants.getIsoDateInRequiredFormat(medicationList.getStartDate()):Constants.getIsoDateInRequiredFormat(medicationOrdered.getOrderDate()));
                        }
                    }
                }
//                medicationDispense.setWhenHandedOver(medicationOrdered.getOrderDate()!=null ? Constants.getIsoDateInRequiredFormat(medicationOrdered.getOrderDate()):"");


                Reference recieverReference=new Reference();
                recieverReference.setReference("Patient/"+epEncounter.getPatientId());
                medicationDispense.getReceiver().add(recieverReference);


                Coding medicationCodeableConcept=new Coding();
                mapCodingObjectByDictionaries(medicationCodeableConcept, medicationOrdered.getMedicationCode());
                medicationDispense.getMedicationCodeableConcept().getCoding().add(medicationCodeableConcept);


                //dosageInstruction
                Timing timing=new Timing();
                timing.getRepeat().setFrequency((int) frequency);
                timing.getRepeat().setPeriod(daySupply);
                timing.getRepeat().setPeriodUnit(periodUnit);
///////////////////////////////
                Type route=new Type();
                Coding coding=new Coding();

                mapCodingObjectByDictionaries(coding, medicationOrdered.getMedicationCode());
                route.getCoding().add(coding);
///////////////////////////////
                //DoseRate
                Coding doseAndRateCoding=new Coding();
                doseAndRateCoding.setSystem("http://terminology.hl7.org/CodeSystem/dose-rate-type");
                doseAndRateCoding.setCode("ordered");
                doseAndRateCoding.setDisplay("Ordered");

                DoseQuantity doseQuantity=new DoseQuantity();
                doseQuantity.setValue((int)Math.round(dose));
                doseQuantity.setUnit(doseUnit);
                doseQuantity.setCode(doseUnit);
                doseQuantity.setSystem("http://unitsofmeasure.org");

                DosageInstruction dosageInstruction=new DosageInstruction();
                DoseRate doseRate=new DoseRate();
                doseRate.setDoseQuantity(doseQuantity);
                doseRate.getType().getCoding().add(doseAndRateCoding);

                dosageInstruction.getDoseAndRate().add(doseRate);
                dosageInstruction.setRoute(route);
                dosageInstruction.setTiming(timing);

                medicationDispense.getDosageInstruction().add(dosageInstruction);

                ResourceChild resourceChild=new ResourceChild();
                resourceChild.setResource(medicationDispense);
                resourceList.add(resourceChild);
            }
        }
    }


    void mapImmunizationFromMedicationOrdered(Bundle bundle,EpEncounter epEncounter,List<Resource> resourceList){
        if(epEncounter !=null && epEncounter.getMedicationOrdered()!=null) {
            for(int i=0;i<epEncounter.getMedicationOrdered().size();i++){
                MedicationOrdered medicationOrdered= epEncounter.getMedicationOrdered().get(i);
                Immunization immunization=new Immunization();

                immunization.setId(epEncounter.getPatientId());

//                if(medicationOrdered.getMedicationStatusIsActive()!=null) {
//                    immunization.setStatus(medicationOrdered.getMedicationStatusIsActive() == true ? "entered-in-error" : "completed");
//                }
                immunization.setStatus("completed");

                Coding vacinationCoding=new Coding();
                mapCodingObjectByDictionaries(vacinationCoding, medicationOrdered.getMedicationCode());
                immunization.getVaccineCode().getCoding().add(vacinationCoding);


                immunization.getPatient().setReference("Patient/" + epEncounter.getPatientId());

                immunization.getEncounter().setReference("Encounter/" + getEncounterCode(epEncounter));

                immunization.setOccurrenceDateTime(getFormattedDate(medicationOrdered.getOrderDateString()));


                immunization.getDoseQuantity().setValue(medicationOrdered.getQuantity()==0?0:medicationOrdered.getQuantity());
                immunization.getDoseQuantity().setSystem("http://unitsofmeasure.org");

                ResourceChild resourceChild=new ResourceChild();
                resourceChild.setResource(immunization);
                resourceList.add(resourceChild);
            }
        }
    }

    void mapImmunizationFromProcedurePerformed(Bundle bundle,EpEncounter epEncounter,List<Resource> resourceList) {
        if (epEncounter != null) {
            if (epEncounter.getProcedurePerformed() != null) {
                for (int i = 0; i < epEncounter.getProcedurePerformed().size(); i++) {

                    if (epEncounter.getProcedurePerformed().get(i).getPosCode() == null || !epEncounter.getProcedurePerformed().get(i).getPosCode().equals("81")) {

                        ProcedurePerformed procedurePerformed = epEncounter.getProcedurePerformed().get(i);
                        Immunization immunization = new Immunization();

                        immunization.setId(epEncounter.getPatientId());

//                if(medicationOrdered.getMedicationStatusIsActive()!=null) {
//                    immunization.setStatus(medicationOrdered.getMedicationStatusIsActive() == true ? "entered-in-error" : "completed");
//                }
                        immunization.setStatus("completed");

                        Coding vacinationCoding = new Coding();
                        mapCodingObjectByDictionaries(vacinationCoding, procedurePerformed.getProcedureCode());
                        immunization.getVaccineCode().getCoding().add(vacinationCoding);


                        immunization.getPatient().setReference("Patient/" + epEncounter.getPatientId());

                        immunization.getEncounter().setReference("Encounter/" + getEncounterCode(epEncounter));

                        immunization.setOccurrenceDateTime(getFormattedDate(procedurePerformed.getPerformedDateString()));


//                        immunization.getDoseQuantity().setValue(medicationOrdered.getQuantity() == 0 ? 0 : medicationOrdered.getQuantity());
                        immunization.getDoseQuantity().setSystem("http://unitsofmeasure.org");

                        ResourceChild resourceChild = new ResourceChild();
                        resourceChild.setResource(immunization);
                        resourceList.add(resourceChild);
                    }
                }
            }
        }
    }

    void mapBundleItems(Bundle bundle,EpEncounter epEncounter){
        bundle.setGender(epEncounter.getGender().equals("F") ? "female" : "male");
        bundle.setBirthDate(getFormattedDate(epEncounter.getBirthDateString()));
        bundle.setPayerInfo(mapPayersInfoInList(epEncounter.getInsurance()));
        bundle.setHospiceFlag(getHospiceFlag(epEncounter.getPremium()));
        bundle.setPremium(epEncounter.getPremium());

        bundle.setEthnicity(epEncounter.getEthnicity());
        bundle.setEthnicityCode(epEncounter.getEthnicityCode());
        bundle.setEthnicityDS(epEncounter.getEthnicityDS());
        bundle.setRace(epEncounter.getRace());
        bundle.setRaceCode(epEncounter.getRaceCode());
        bundle.setRaceDS(epEncounter.getRaceDS());
    }

    @Override
    public void run() {
        try {


            LOGGER.info("Thread processing the patientId " + epEncounter.getPatientId());
            Bundle bundle = new Bundle();
            List<Resource> resourceList = new LinkedList<>();
            bundle.setId(epEncounter.getPatientId());
//        Entry entry=new Entry();

            mapPatient(bundle, epEncounter, resourceList);
            mapCoverages(bundle, epEncounter, resourceList);
            mapProcedures(bundle, epEncounter, resourceList);
            mapConditions(bundle, epEncounter, resourceList);
            mapObservations(bundle, epEncounter, resourceList);
            mapEncounter(bundle, epEncounter, resourceList);
            mapMedicationAdministrations(bundle, epEncounter, resourceList);
            mapMedicationOrdered(bundle, epEncounter, resourceList);
            mapImmunizationFromMedicationOrdered(bundle, epEncounter, resourceList);

            mapBundleItems(bundle,epEncounter);

            // entry.setResource(resourceList);
            bundle.getEntry().add(resourceList);

//            saveBundle(bundle);
            bundleList.add(bundle);
        }catch(Exception e){
            LOGGER.error("Exception occured for patientId "+epEncounter.getPatientId());
        }
    }
}
