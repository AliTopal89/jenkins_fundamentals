
pipeline {
  stages {
    stage('Buzz Build') {
      parallel {
        stage('Build Java 7') {
          steps {
            sh """
               echo I am $BUZZ_NAME
               ./jenkins/build.sh
               """
          }
          post {
            always {
              archiveArtifacts(artifacts: 'target/*.jar', fingerprint: true)
            }

            success {
              stash(name: 'Buzz Java 7', includes: 'target/**')
            }
          }
        }
      }
    }
  }
}
