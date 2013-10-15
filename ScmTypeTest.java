package fr.linsolas.enforcer.rule.checkscm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * User: Romain Linsolas
 */
@RunWith(value = Parameterized.class)
public class ScmTypeTest {

    private String scm;
    private ScmType expectedType;

    public ScmTypeTest(String scm, ScmType expectedType) {
        this.scm = scm;
        this.expectedType = expectedType;
    }

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(
                new Object[][]{
                        { "scm:git:git://github.com/path_to_repository", ScmType.GIT },
                        { "scm:git:http://github.com/path_to_repository", ScmType.GIT },
                        { "scm:git:https://github.com/path_to_repository", ScmType.GIT },
                        { "scm:git:ssh://github.com/path_to_repository", ScmType.GIT },
                        { "scm:git:file://localhost/path_to_repository", ScmType.GIT },

                        { "scm:svn:file:///svn/root/module", ScmType.SVN },
                        { "scm:svn:file://localhost/path_to_repository", ScmType.SVN },
                        { "scm:svn:file://my_server/path_to_repository", ScmType.SVN },
                        { "scm:svn:http://svn.apache.org/svn/root/module", ScmType.SVN },
                        { "scm:svn:https://username@svn.apache.org/svn/root/module", ScmType.SVN },
                        { "scm:svn:https://username:password@svn.apache.org/svn/root/module", ScmType.SVN },

                        { "scm:cvs:pserver:anoncvs:@cvs.apache.org:/cvs/root:module", ScmType.CVS },
                        { "scm:cvs|pserver|username@localhost|C:/Program Files/cvsnt/repositories|module_name", ScmType.CVS },
                        { "scm:cvs:ext:username@cvs.apache.org:/cvs/root:module", ScmType.CVS },
                        { "scm:cvs:local:/cvs/root:module", ScmType.CVS },
                        { "scm:cvs:sspi:cvs.apache.org:2222:/cvs/root:module", ScmType.CVS }
                });
    }

    @Test
    public void test_scm() {
        ScmType typeFound = ScmType.getFromSCMUrl(scm);
        assertThat(typeFound).isEqualTo(expectedType);
    }


}
