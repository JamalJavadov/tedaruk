package az.att.admin.integration.asan.certificates.dto;

import az.att.admin.integration.asan.login.dto.StructureDto;
import lombok.Builder;
import lombok.Data;

import az.att.auth.dto.UserJwtDto;

@Data
@Builder
public class AttTokenClaimsDto {
    private String sub;
    private UserJwtDto user;
    private Boolean isActive;
    private String role;
    private String voen;
    private String phoneNumber;
    private StructureDto structure;
}
