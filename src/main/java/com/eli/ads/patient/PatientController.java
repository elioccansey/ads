package com.eli.ads.patient;

import com.eli.ads.patient.dto.PatientRequest;
import com.eli.ads.patient.dto.PatientResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientResponse>> displayPatientsSortedByLastName(
    ){
        return ResponseEntity.ok(patientService.displayPatientsSortedByLastName());
    }

    @GetMapping("/{patient-id}")
    @ResponseStatus(HttpStatus.OK)
    public PatientResponse getPatientById(@PathVariable("patient-id") Long patientId){
        return patientService.getPatientById(patientId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponse createPatient(@Valid @RequestBody PatientRequest patientRequest){
        return  patientService.savePatient(patientRequest);
    }

    @PutMapping("/{patient-id}")
    @ResponseStatus(HttpStatus.OK)
    public PatientResponse updatePatient(
            @PathVariable("patient-id") Long patientId,
            @Valid @RequestBody PatientRequest patientRequest){
        return patientService.updatePatient(patientId, patientRequest);
    }
    @DeleteMapping("/{patient-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable("patient-id") Long patientId){
         patientService.deletePatient(patientId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<PatientResponse> searchPatient(@RequestParam("searchString") String searchString){
        return  patientService.searchPatient(searchString);
    }


}
