package com.picsy.config;

import com.picsy.domain.CommunityFactory;
import com.picsy.domain.CommunityState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialStateConfig {

    @Bean
    public CommunityState communityState() {
        // start with a modest default size; UI can extend via API later.
        return CommunityFactory.createUniformCommunity(5);
    }
}
