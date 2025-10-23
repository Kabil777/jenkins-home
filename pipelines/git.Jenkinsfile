pipeline {
    agent any

    environment {
        REPO_URL = "git@github.com:Kabil777/argo-repo.git"
    }

    stages {
        stage('Clone Repo') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ssh-privatekey', keyFileVariable: 'SSH_KEY', usernameVariable: 'GIT_USER')]) {
                    sh '''
                        export GIT_SSH_COMMAND="ssh -i $SSH_KEY -o StrictHostKeyChecking=no"
                        git clone $REPO_URL repo
                    '''
                }
            }
        }

        stage('Push Changes') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ssh-privatekey', keyFileVariable: 'SSH_KEY', usernameVariable: 'GIT_USER')]) {
                    sh '''
                        export GIT_SSH_COMMAND="ssh -i $SSH_KEY -o StrictHostKeyChecking=no"
                        cd repo
                        git config user.email "jenkins@kabidev.in"
                        git config user.name "Jenkins CI"
                        git add .
                        git diff --cached --quiet || git commit -m "Automated update"
                        git push origin main
                    '''
                }
            }
        }
    }
}

