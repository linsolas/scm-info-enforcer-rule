package fr.linsolas.enforcer.rule.checkscm;

import org.apache.commons.lang3.StringUtils;

/**
 * User: Romain Linsolas
 */
public enum ScmType {

    CVS("scm:cvs", ""), // CVS plugin does not expose any property?
    SVN("scm:svn", "SVN_URL"),
    GIT("scm:git", "GIT_URL"); // Supported by Git Plugin since v1.3.0 (12/03/2013)


    private String scmUrl;

    private String jenkinsProperty;

    private ScmType(String scmUrl, String jenkinsProperty) {
        this.jenkinsProperty = jenkinsProperty;
        this.scmUrl = scmUrl;
    }

    public static ScmType getFromSCMUrl(String scmUrl) {
        for (ScmType type : values()) {
            if (StringUtils.startsWithIgnoreCase(scmUrl, type.scmUrl)) {
                return type;
            }
        }
        return null;
    }

    public String getScmUrlFromJenkins() {
        return System.getProperty(jenkinsProperty);
    }

    public String getJenkinsProperty() {
        return jenkinsProperty;
    }
}
