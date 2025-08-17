package com.br.obitus_manager.domain.messaging;

import com.br.obitus_manager.domain.message.MessageRequest;

public interface MessagingService {
    void sendEmail(MessageRequest request);
}
