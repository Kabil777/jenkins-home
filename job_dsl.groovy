import org.yaml.snakeyaml.Yaml
import jenkins.model.*
import org.jenkinsci.plugins.scriptsecurity.scripts.*

def sa = Jenkins.instance.getExtensionList(ScriptApproval.class)[0]
sa.getPendingSignatures().each { sig ->
    sa.approveSignature(sig.signature)
}

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
                // load the right Jenkinsfile template based on type
                script(readFileFromWorkspace("pipelines/${repo.type}.Jenkinsfile"))
            }
        }
    }
}

def sa = Jenkins.instance.getExtensionList(ScriptApproval.class)[0]
sa.getPendingSignatures().each { sig ->
    sa.approveSignature(sig.signature)
}


