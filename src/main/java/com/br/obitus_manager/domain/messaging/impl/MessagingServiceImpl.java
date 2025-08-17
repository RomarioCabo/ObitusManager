package com.br.obitus_manager.domain.messaging.impl;

import com.br.obitus_manager.domain.message.MessageRequest;
import com.br.obitus_manager.domain.messaging.MessagingService;
import com.br.obitus_manager.domain.provider.MessagingApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingServiceImpl implements MessagingService {

    private final MessagingApi messagingApi;

    @Override
    public void sendEmail(MessageRequest request) {
        messagingApi.sendEmail(request);
    }
}
