package az.att.admin.integration.asan.login;

import az.att.admin.integration.asan.certificates.dto.AsanLoginRequest;
import az.att.admin.integration.asan.login.dto.AsanLoginResponse;
import az.att.auth.dto.JwtPayloadDto;

public interface AsanAuthClient {

    AsanLoginResponse authenticate(AsanLoginRequest asanLoginRequest);

    JwtPayloadDto parseToken(AsanLoginResponse asanLoginResponse);
}
