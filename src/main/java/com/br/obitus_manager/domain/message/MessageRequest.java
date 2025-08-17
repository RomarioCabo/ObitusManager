package com.br.obitus_manager.domain.message;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private String templateId;
    private Map<String, Object> templateVariables;
    private String emailTo;
    private String subject;
}
