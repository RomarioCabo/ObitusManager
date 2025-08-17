package com.br.obitus_manager.infrastructure.persistence.provider;

import com.br.obitus_manager.domain.message.MessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "messaging", url = "${base_url_external}")
public interface MessagingApiHttpRestClient {

    @PostMapping("/api/v1/send")
    void sendEmail(@RequestBody MessageRequest request);
}
