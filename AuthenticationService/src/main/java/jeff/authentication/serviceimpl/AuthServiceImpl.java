package jeff.authentication.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jeff.authentication.model.bo.CustomUserDetails;
import jeff.authentication.model.dto.send.LoginSuccessRes;
import jeff.authentication.model.dto.send.RefreshATSuccessRes;
import jeff.authentication.service.AuthService;
import jeff.authentication.service.JWTService;

@Component
public class AuthServiceImpl implements AuthService{
	
	private final JWTService jwtService;
	
	@Autowired
	public AuthServiceImpl(JWTServiceImpl jwtServiceImpl) {
		this.jwtService = jwtServiceImpl;
	}

	@Override
	public LoginSuccessRes generateFullJWTs(CustomUserDetails user) {
		LoginSuccessRes res = new LoginSuccessRes();
		res.setRefreshToken(jwtService.generateRefreshToken(user));
		res.setAccessToken(jwtService.generateAccessToken(user));
		return res;
	}

	@Override
	public RefreshATSuccessRes refreshAT(CustomUserDetails user) {
		RefreshATSuccessRes res = new RefreshATSuccessRes();
		res.setAccessToken(jwtService.generateAccessToken(user));
		return res;
	}

	
}
