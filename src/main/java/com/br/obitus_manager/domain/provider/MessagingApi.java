package com.br.obitus_manager.domain.provider;

import com.br.obitus_manager.domain.message.MessageRequest;

public interface MessagingApi {
    void sendEmail(MessageRequest request);
}
