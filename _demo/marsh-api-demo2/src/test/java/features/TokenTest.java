package features;

import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.sessionstate.jwt.JwtUtils;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;

/**
 * @author noear 2021/3/30 created
 */
//@RunWith(SolonJUnit4ClassRunner.class)
//@SolonTest(App.class)
public class TokenTest {
//    @Test
    public void test(){
        System.out.println(JwtUtils.createKey());
        //Claims claims = JwtUtils.parseJwt("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJ3d3cuZXhhbXBsZS5jb20iLCJuYmYiOjE2MTY5ODkxOTEsImlzcyI6IkNsb3VkV29ya3MiLCJleHAiOjE2MTk1ODExOTEsInVzZXJpZCI6MTAwMDY5NiwiaWF0IjoxNjE2OTg5MTkxLCJ0aW1lc3RhbXAiOjE2MTY5ODkxOTE5MjR9.4HSnBrEJ71vDPREF-nas-0DgCrCRRN4mY0JA4IMN574");
    }
}
