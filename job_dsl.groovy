import org.yaml.snakeyaml.Yaml
import jenkins.model.*
import org.jenkinsci.plugins.scriptsecurity.scripts.*

def yaml = new Yaml()
def config = yaml.load(readFileFromWorkspace("repository.yaml"))

config.repos.each { repo ->
    pipelineJob("${repo.name}-pipeline") {
        description("Pipeline for ${repo.name}")
       parameters {
        stringParam('REPO_URL', repo.url, 'Repository URL')
        stringParam('IMAGE_NAME', repo.image?.name ?: '', 'Docker image name')
        stringParam('IMAGE_TAG', repo.image?.tag ?: '', 'Docker image tag')
      }
       definition {
            cps {
                script(readFileFromWorkspace("pipelines/${repo.type}.Jenkinsfile"))
            }
        }
    }
}
