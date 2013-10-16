scm-info-enforcer-rule
======================

A Maven [enforcer rule](http://maven.apache.org/enforcer/maven-enforcer-plugin/) to check the validity of SCM information in the pom.xml

# Usage

In your pom.xml, add the Maven enforcer plugin configuration, as follow:

```
<project>
    ...
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-enforcer-plugin</artifactId>
                <version>1.3.1</version>
                <dependencies>
                    <dependency>
                        <groupId>fr.linsolas</groupId>
                        <artifactId>scm-info-enforcer-rule</artifactId>
                        <version>1.0-SNAPSHOT</version>
                    </dependency>
                </dependencies>
                <configuration>
                    <rules>
                        <myCustomRule implementation="fr.linsolas.enforcer.rule.checkscm.CheckScmRule"/>
                    </rules>
                </configuration>
            </plugin>
        </plugins>
    </build>
    ...
</project>
```

That's all.


# What is supported?

* For CI servers, *Jenkins* (and maybe *Hudson*, but not tested).
* For SCM systems, only *GIT* and *Subversion*.

# Todo

- [ ] Support of others SCM systems (CVS, Perforce, etc.)
- [ ] Support of others CI servers (TeamCity, etc.)
- [ ] Upload in Maven repository


