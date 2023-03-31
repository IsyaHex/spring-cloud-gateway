package uz.epam.msa.gateway.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;
import uz.epam.msa.gateway.constants.Constants;

import java.util.Map;

@Component
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    private HttpStatus errorStatus = HttpStatus.I_AM_A_TEAPOT;
    private String errorMessage = "Custom exception for the special case";

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> map = super.getErrorAttributes(request, options);
//        HttpStatus status = ((ResponseStatusException) getError(request)).getStatus();

        if (getError(request) instanceof ResponseStatusException) {
            map.put(Constants.STATUS_LOWERCASE, HttpStatus.EXPECTATION_FAILED.value());
            map.put(Constants.ERROR_LOWERCASE, HttpStatus.EXPECTATION_FAILED.getReasonPhrase());
            map.put(Constants.MESSAGE_LOWERCASE, Constants.ERROR_MESSAGE_INVALID_URL);
        } else {
            map.put(Constants.STATUS_LOWERCASE, HttpStatus.I_AM_A_TEAPOT.value());
            map.put(Constants.ERROR_LOWERCASE, HttpStatus.I_AM_A_TEAPOT.getReasonPhrase());
            map.put(Constants.MESSAGE_LOWERCASE, Constants.ERROR_MESSAGE_TEA_POT);
        }
        return map;
    }

    public HttpStatus getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(HttpStatus errorStatus) {
        this.errorStatus = errorStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}