package fr.linsolas.enforcer.rule.checkscm;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.model.Scm;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

/**
 * User: Romain Linsolas
 */
public class CheckScmRule implements EnforcerRule {

    @Override
    public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException {
        Log log = helper.getLog();
        try {
            MavenProject project = (MavenProject) helper.evaluate("${project}");
            Scm scm = project.getScm();
            failIfNull(scm, "The XML tag <xml> is not defined in the pom.xml");
            String connection = scm.getConnection();
            failIfNull(connection, "The XML tag <connection> is not defined in the pom.xml");
            ScmType type = ScmType.getFromSCMUrl(connection);
            failIfNull(type, "Unknown or not supported SCM type [" + connection + "]");
            String jenkinsUrl = type.getScmUrlFromJenkins();
            failIfNull(jenkinsUrl, "SCM URL not exposed by Jenkins.");
            // Check if the SCM url are identical
            if (!StringUtils.endsWithIgnoreCase(connection, jenkinsUrl)) {
                throw new EnforcerRuleException("The SCM url defined in the <scm> tag in the pom.xml ('" + connection
                        + "') is different from the one used by Jenkins ('" + jenkinsUrl + "').");
            }
        } catch (ExpressionEvaluationException e) {
            log.error("Could not evaluate an expression", e);
            throw new EnforcerRuleException("Unable to lookup a component", e);
        }
    }

    private void failIfNull(Object object, String message) throws EnforcerRuleException {
        if (object == null) {
            throw new EnforcerRuleException(message);
        }
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    @Override
    public boolean isResultValid(EnforcerRule enforcerRule) {
        return false;
    }

    @Override
    public String getCacheId() {
        return null;
    }

}
