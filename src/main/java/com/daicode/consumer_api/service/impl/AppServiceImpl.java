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
    public String useSomeOperation() {
        RequestClientDTO requestClientDTO = RequestClientDTO.builder().number(100).someString("some").build();
        ResponseClientDTO responseClientDTO;
        double startTime = System.currentTimeMillis();
        double duration;
        UUID transactionIdTest = UUID.fromString("8ebfaf70-d298-4914-b617-f58268bd0b00");

        try {
            // usando cliente con feign
            responseClientDTO = this.iSomeClient.callMock(requestClientDTO);

            // NOTA: si usan otro cliente, ejem: validar status y <> 200 pueden enviar al logger con status FALSE
            // aquí se puede validar status y si es 200 escribir true si no ya uds validan

            duration = System.currentTimeMillis() - startTime;
            SubjectLoggerDTO subjectLoggerDTO = SubjectLoggerDTO.builder()
                    .transactionId(transactionIdTest)
                    .originService("our-service")
                    .duration(duration)
                    .subscriberNumber("3158457850")
                    .originUrl("client-url")
                    .status(true)
                    .date(OffsetDateTime.now())
                    .build();
            //String strRequest = "{\"dev\": \"Elkin\"}";
            //String strRequest = "Response plain text";

            this.publishLogger.publish(subjectLoggerDTO, requestClientDTO /*strRequest*/, responseClientDTO);

            return "Finish process success";
        } catch (FeignException e) {
            // publicar al logger con status FALSE
            // validar si requieren hacer otras acciones posteriores, ejm: por status, pueden reintentar nak o darle ack
            log.error("Client error: {}", e.getMessage());
            duration = System.currentTimeMillis() - startTime;
            SubjectLoggerDTO subjectLoggerDTO = SubjectLoggerDTO.builder()
                    .transactionId(transactionIdTest)
                    .originService("our-service")
                    .duration(duration)
                    .subscriberNumber("3158457850")
                    .originUrl("client-url")
                    .status(false)
                    .date(OffsetDateTime.now())
                    .build();

            this.publishLogger.publish(subjectLoggerDTO, requestClientDTO, e.getMessage());
        }
        return "Finish program...";
    }
}
