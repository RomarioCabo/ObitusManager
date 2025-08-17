package com.br.obitus_manager.infrastructure.persistence.provider;

import com.br.obitus_manager.domain.message.MessageRequest;
import com.br.obitus_manager.domain.provider.MessagingApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessagingApiImpl implements MessagingApi {

    private final MessagingApiHttpRestClient restClient;

    @Override
    public void sendEmail(MessageRequest request) {
        log.info("trying to send request to Messaging Api with body: {}", request);
        restClient.sendEmail(request);
    }
}
