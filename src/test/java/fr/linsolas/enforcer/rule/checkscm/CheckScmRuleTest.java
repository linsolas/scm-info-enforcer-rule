package fr.linsolas.enforcer.rule.checkscm;

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.model.Scm;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * User: Romain Linsolas
 */
public class CheckScmRuleTest {

    private EnforcerRuleHelper helper;
    private MavenProject project;

    private static final String SVN_POM = "scm:svn:http://svn.apache.org/svn/root/module";
    private static final String SVN_JENKINS_OK = "http://svn.apache.org/svn/root/module";
    private static final String SVN_JENKINS_BAD = "http://svn.apache.org/svn/root/another";

    private static final String GIT_POM = "scm:git:https://github.com/path_to_repository";
    private static final String GIT_JENKINS_OK = "https://github.com/path_to_repository";
    private static final String GIT_JENKINS_BAD = "https://github.com/another_repository";

    @Before
    public void initMock() throws Exception {
        helper = mock(EnforcerRuleHelper.class);
        when(helper.getLog()).thenReturn(mock(Log.class));
        project = mock(MavenProject.class);
        when(helper.evaluate(anyString())).thenReturn(project);
    }


    @Test
    public void should_fail_as_no_scm_in_pom_xml() throws Exception {
        when(project.getScm()).thenReturn(null);
        CheckScmRule rule = new CheckScmRule();
        try {
            rule.execute(helper);
        } catch (Exception exception) {
            assertThat(exception).isInstanceOf(EnforcerRuleException.class)
                    .hasMessage("The XML tag <xml> is not defined in the pom.xml");
        }
    }


    @Test
    public void should_fail_as_no_connection_in_pom_xml() throws Exception {
        Scm scm = mock(Scm.class);
        when(scm.getConnection()).thenReturn(null);
        when(project.getScm()).thenReturn(scm);
        CheckScmRule rule = new CheckScmRule();
        try {
            rule.execute(helper);
        } catch (Exception exception) {
            assertThat(exception).isInstanceOf(EnforcerRuleException.class)
                    .hasMessage("The XML tag <connection> is not defined in the pom.xml");
        }
    }

    @Test
    public void should_fail_as_SVN_info_dont_match() throws Exception {
        Scm scm = mock(Scm.class);
        when(scm.getConnection()).thenReturn(SVN_POM);
        when(project.getScm()).thenReturn(scm);
        System.setProperty(ScmType.SVN.getJenkinsProperty(), SVN_JENKINS_BAD);
        CheckScmRule rule = new CheckScmRule();
        try {
            rule.execute(helper);
        } catch (Exception exception) {
            assertThat(exception).isInstanceOf(EnforcerRuleException.class)
                    .hasMessageContaining("The SCM url defined in the <scm> tag in the pom.xml")
                    .hasMessageContaining("is different from the one used by Jenkins");
        }
    }

    @Test
    public void should_fail_as_GIT_info_dont_match() throws Exception {
        Scm scm = mock(Scm.class);
        when(scm.getConnection()).thenReturn(GIT_POM);
        when(project.getScm()).thenReturn(scm);
        System.setProperty(ScmType.GIT.getJenkinsProperty(), GIT_JENKINS_BAD);
        CheckScmRule rule = new CheckScmRule();
        try {
            rule.execute(helper);
        } catch (Exception exception) {
            assertThat(exception).isInstanceOf(EnforcerRuleException.class)
                    .hasMessageContaining("The SCM url defined in the <scm> tag in the pom.xml")
                    .hasMessageContaining("is different from the one used by Jenkins");
        }
    }

    @Test
    public void should_fail_as_jenkins_dont_expose_SVN_info() throws Exception {
        Scm scm = mock(Scm.class);
        when(scm.getConnection()).thenReturn(SVN_POM);
        when(project.getScm()).thenReturn(scm);
        System.clearProperty(ScmType.SVN.getJenkinsProperty());
        CheckScmRule rule = new CheckScmRule();
        try {
            rule.execute(helper);
        } catch (Exception exception) {
            assertThat(exception).isInstanceOf(EnforcerRuleException.class)
                    .hasMessageContaining("SCM URL not exposed by Jenkins.");
        }
    }

    @Test
    public void should_fail_as_jenkins_dont_expose_GIT_info() throws Exception {
        Scm scm = mock(Scm.class);
        when(scm.getConnection()).thenReturn(GIT_POM);
        when(project.getScm()).thenReturn(scm);
        System.clearProperty(ScmType.GIT.getJenkinsProperty());
        CheckScmRule rule = new CheckScmRule();
        try {
            rule.execute(helper);
        } catch (Exception exception) {
            assertThat(exception).isInstanceOf(EnforcerRuleException.class)
                    .hasMessageContaining("SCM URL not exposed by Jenkins.");
        }
    }

    @Test
    public void correct_SVN_info() throws Exception {
        Scm scm = mock(Scm.class);
        when(scm.getConnection()).thenReturn(SVN_POM);
        when(project.getScm()).thenReturn(scm);
        System.setProperty(ScmType.SVN.getJenkinsProperty(), SVN_JENKINS_OK);
        CheckScmRule rule = new CheckScmRule();
        rule.execute(helper);
    }

    @Test
    public void correct_GIT_info() throws Exception {
        Scm scm = mock(Scm.class);
        when(scm.getConnection()).thenReturn(GIT_POM);
        when(project.getScm()).thenReturn(scm);
        System.setProperty(ScmType.GIT.getJenkinsProperty(), GIT_JENKINS_OK);
        CheckScmRule rule = new CheckScmRule();
        rule.execute(helper);
    }

}
