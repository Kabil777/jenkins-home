import org.yaml.snakeyaml.Yaml
import jenkins.model.*
import javaposse.jobdsl.dsl.*

def yaml = new Yaml()
def config = yaml.load(readFileFromWorkspace("repository.yaml"))

config.repos.each { repo ->
    def jobName = "${repo.name}-pipeline"
    def jobExists = Jenkins.instance.getItem(jobName) != null

    if (repo.update || !jobExists) {
        pipelineJob(jobName) {
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
    } else {
        println "Skipping update for ${jobName} (update flag = false)"
    }
}

