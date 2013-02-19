package coursebuzzserver;

import java.io.IOException;

public class GetAuthenticationToken {

	public static void main(String[] args) throws IOException {
		String token = AuthenticationUtil.getToken(SecureStorage.USER,
				SecureStorage.PASSWORD);
		System.out.println(token);
	}
}