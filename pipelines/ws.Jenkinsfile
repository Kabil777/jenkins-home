pipeline{
  stages{
     stage('start') {
        steps{
          sh 'echo "hello world" > example.txt'
             websocketin(
               url: 'wss://localhost:8000/',
               startMessage: "Build ${env.JOB_NAME} has started."
             )
          }
     stage('read') {
     steps{
         sh 'cat example.txt'
         websocketin(
          url: 'wss://localhost:8000/',
          startMessage: "Read the content from file "
        )
    }
    }
    stage('end') {
    steps{
      sh 'rm -f exampl.txt'
      websocketin(
          url: 'wss://localhost:8000/',
          startMessage: "Completed build ${env.JOB_NAME}"
      )
    }
  }
}

pipeline{
  stages{
     stage('start') {
        steps{
          sh 'echo "hello world" > example.txt'
             websocketin(
               url: 'wss://localhost:8000/',
               startMessage: "Build ${env.JOB_NAME} has started."
             )
          }
    }
     stage('read') {
     steps{
         sh 'cat example.txt'
         websocketin(
          url: 'wss://localhost:8000/',
          startMessage: "Read the content from file "
        )
    }
    }
    stage('end') {
    steps{
      sh 'rm -f exampl.txt'
      websocketin(
          url: 'wss://localhost:8000/',
          startMessage: "Completed build ${env.JOB_NAME}"
      )
    }
  }
}
