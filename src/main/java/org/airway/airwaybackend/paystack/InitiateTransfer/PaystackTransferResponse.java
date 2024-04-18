package org.airway.airwaybackend.paystack.InitiateTransfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "transfer_code",
        "message"
})
public class PaystackTransferResponse {
    @JsonProperty("status")
    private String status;
    @JsonProperty("transfer_code")
    private String transfer_code;
    @JsonProperty("message")
    private String message;
    private org.airway.airwaybackend.paystack.InitiateTransfer.Data data;
}

