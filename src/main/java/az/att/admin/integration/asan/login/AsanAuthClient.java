package az.att.admin.integration.asan.login;

import az.att.admin.integration.asan.certificates.dto.AsanLoginRequest;
import az.att.admin.integration.asan.login.dto.AsanJwtResponse;
import az.att.admin.integration.asan.login.dto.AsanLoginResponse;

public interface AsanAuthClient {

    AsanLoginResponse authenticate(AsanLoginRequest asanLoginRequest);

    AsanJwtResponse parseToken(AsanLoginResponse asanLoginResponse);
}
