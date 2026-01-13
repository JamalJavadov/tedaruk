package az.att.admin.integration.asan.certificates.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CertificateData {

    private final String phoneNumber;
    private final String voen;
    private final String structureName;
    private final String position;
    private final Boolean hasStamp;
    private final Boolean legal;
}
