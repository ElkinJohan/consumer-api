package com.daicode.consumer_api.service.impl;

import com.daicode.consumer_api.client.ISomeClient;
import com.daicode.consumer_api.dto.RequestClientDTO;
import com.daicode.consumer_api.dto.ResponseClientDTO;
import com.daicode.consumer_api.dto.SubjectLoggerDTO;
import com.daicode.consumer_api.service.IAppService;
import com.daicode.consumer_api.util.PublishLogger;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class AppServiceImpl implements IAppService {

    private final ISomeClient iSomeClient;
    private final PublishLogger publishLogger;

    @Override
    public String useSomeOperation(boolean flag) {
        RequestClientDTO requestClientDTO = RequestClientDTO.builder().number(100).someString("some").build();
        ResponseClientDTO responseClientDTO;
        double startTime = System.currentTimeMillis();
        double duration;

        if (!flag) {
            // EJEMPLO CUANDO EL CLIENTE RESPONDE CON ERROR
            try {
                // si se utiliza feign
                this.iSomeClient.callMockFail(requestClientDTO);
            } catch (FeignException e) {
                // publicar al logger con status FALSE
                // validar si requieren hacer otras acciones posteriores, ejm: por status, pueden reintentar nak o darle ack
                log.error("Client error: {}", e.getMessage());
            }
            // NOTA: en el caso de usar otro cliente, ejem: validar el status y si es <> de 200 pueden enviar al logger con status FALSE

            duration = System.currentTimeMillis() - startTime;
            // publicar en logger
            // crear DTO subjectLogger
            //this.publishLogger.publish(duration);
            return "Finish process fail";
        } else {
            // EJEMPLO CUANDO EL CLIENTE RESPONDE CON EXITOSO
            try {
                // caso cliente feign, si es otro recordar apoyarse en status por ejemplo.
                responseClientDTO = this.iSomeClient.callMockSuccess(requestClientDTO);
                duration = System.currentTimeMillis() - startTime;
                // usar el publicador logger
                SubjectLoggerDTO subjectLoggerDTO = SubjectLoggerDTO.builder()
                        .transactionId(UUID.fromString("b04fa4c4-c9dc-41b6-83a9-74a5a57354cf"))
                        .originService("our-service")
                        .duration(duration)
                        .subscriberNumber("3158457850")
                        .originUrl("client-url")
                        .status(true)
                        .date(OffsetDateTime.now())
                        .build();

                this.publishLogger.publish(subjectLoggerDTO, requestClientDTO, responseClientDTO);

            } catch (FeignException e) {
                log.error("Client error: {}", e.getMessage());
            }
            return "Finish process success";

        }

    }
}
