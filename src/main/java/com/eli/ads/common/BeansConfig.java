package com.eli.ads.common;

import com.eli.ads.common.address.AddressMapper;
import com.eli.ads.user.patient.PatientMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
 class BeansConfig {

    @Bean
    PatientMapper patientMapper(){
        return new PatientMapper();
    }

    @Bean
    AddressMapper addressMapper() {return  new AddressMapper();}

}
