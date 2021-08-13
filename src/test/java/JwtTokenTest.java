import com.suzu.pojo.PlainUser;
import com.suzu.pojo.PlainUserDTO;
import com.suzu.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;


class JwtTokenTest {
	public static void main(String[] args) {
		JwtTokenUtil util = new JwtTokenUtil();
		PlainUserDTO user = new PlainUserDTO();
		user.setUsername("wangzisfa");
		String token = util.generateToken(user, false);
		System.out.println(token);
	}
}
