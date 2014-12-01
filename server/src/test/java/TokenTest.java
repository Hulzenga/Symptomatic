import com.hulzenga.symptomatic.common.java.network.APIFactory;
import com.hulzenga.symptomatic.common.java.network.ServerSettings;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jouke on 12/1/14.
 */
public class TokenTest {

  @Test
  public void tokenTest() {
    String token = APIFactory.signIn(ServerSettings.DOCTOR_CLIENT,
        ServerSettings.DOCTOR_CLIENT_SECRET, "Alice", "Alice");

    Assert.assertNotNull(token);
  }
}
