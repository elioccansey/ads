package com.eli.ads.common;

import com.eli.ads.common.address.AddressMapper;
import com.eli.ads.patient.PatientMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
 class BeansConfig {

    @Bean
    PatientMapper patientMapper(){
        return new PatientMapper();
    }

    @Bean
    AddressMapper addressMapper() {return  new AddressMapper();}

}
