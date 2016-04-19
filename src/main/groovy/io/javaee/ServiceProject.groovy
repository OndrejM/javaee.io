package io.javaee

import org.yaml.snakeyaml.Yaml

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import java.util.logging.Level
import java.util.logging.Logger

@ApplicationScoped
class ServiceProject {
    private Logger logger = Logger.getLogger(this.class.name)

    @Inject
    private ServiceGithub github

    private def loadYaml(DtoConfigFile configFile) {
        try {
            return new Yaml().load(configFile.content)
        } catch (e) {
            logger.log(Level.WARNING, "Invalid yaml file: '${configFile.name}'", e)
        }
        return null
    }

    private DtoProjectInfo getDtoProjectInfo(String configFile) {
        DtoProjectInfo info = availableProjects.find { it.configFile == configFile }
        if (!info) {
            throw new ExceptionApplication("Project not found: '${configFile}'")
        }
        return info
    }

    Set<DtoProjectInfo> getAvailableProjects() {
        Set<DtoProjectInfo> result = []
        github.getConfigurationFiles().each {
            def conf = loadYaml(it)
            if (!conf) {
                return
            }
            result << new DtoProjectInfo(
                    configFile: it.name,
                    name: conf.name as String,
                    friendlyName: conf.friendly_name as String,
                    description: conf.name ? github.getRepoDescription(conf.name as String) : '',
                    home: conf.home as String,
                    resources: conf.resources?.collect { resource ->
                        def dto = new DtoProjectResource()
                        if (String.class.isInstance(resource)) {
                            dto.url = resource
                        } else {
                            dto.url = resource.url
                            dto.title = resource.title
                        }
                        return dto
                    },
                    spec: conf.spec != null ? conf.spec : false,
                    related: conf.related
            )
        }
        return result
    }

    DtoProjectDetail getDetails(String configFile) {
        DtoProjectInfo info = getDtoProjectInfo(configFile)
        def conf = loadYaml(github.getConfigurationFiles().find {
            it.name == configFile
        })
        Set<DtoProjectContributor> contributors = github.getRepoContributors(conf.name as String)
        info.related.each { relatedIt ->
            def relatedConf = loadYaml(github.getConfigurationFiles().find {
                it.name == relatedIt
            })
            contributors.addAll(github.getRepoContributors(relatedConf.name))
        }
        return new DtoProjectDetail(
                info: info,
                contributors: contributors
        )
    }

    String getApplicationPage(String resourceName) {
        return github.getAppPage(resourceName)
    }

    String getProjectPage(String configFile, String resourceName) {
        def conf = loadYaml(github.getConfigurationFiles().find {
            it.name == configFile
        })
        def computedResourceName = resourceName
        if (!computedResourceName) {
            computedResourceName = conf.home as String
        }
        if (!computedResourceName) {
            computedResourceName = 'README.adoc'
        }
        return github.getRepoPage(conf.name as String, computedResourceName)
    }



    byte[] getApplicationRaw(String resourceName) {
        return github.getAppRaw(resourceName)
    }

    byte[] getRaw(String configFile, String resourceName) {
        DtoProjectInfo info = getDtoProjectInfo(configFile)
        return github.getRepoRaw(info.name, resourceName)
    }

    List<DtoContributor> getAllContributors() {
        Map<String, DtoContributor> contributors = [:]
        getAvailableProjects().each { project ->
            def details = getDetails(project.configFile)
            details.contributors.each { projContributor ->
                DtoContributor contributor = contributors.get(projContributor.login)
                if (!contributor) {
                    contributor = new DtoContributor(
                            login: projContributor.login
                    )
                    contributors.put(projContributor.login, contributor)
                }
                contributor.projects << project.name
                contributor.contributions += projContributor.contributions
            }
        }
        return contributors.values() as List<DtoContributor>
    }

}

