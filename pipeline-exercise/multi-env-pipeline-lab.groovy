pipeline {
  agent none
  stages {
    stage('Fluffy Build') {
      parallel {
        stage('Build Java 7') {
          agent {
            node {
              label 'java7'
            }

          }
          steps {
            sh './jenkins/build.sh'
            stash(name: 'Java 7', includes: 'target/**')
            archiveArtifacts(artifacts: 'target/*.jar', fingerprint: true)
          }
        }

        stage('Build Java 8') {
          agent {
            node {
              label 'java8'
            }

          }
          steps {
            sh './jenkins/build.sh'
            stash(name: 'Java 8', includes: 'target/**')
          }
        }

      }
    }

    stage('Fluffy Test') {
      parallel {
        stage('Test Java 7 frontend') {
          agent {
            node {
              label 'java7'
            }

          }
          steps {
            unstash 'Java 7'
            sh './jenkins/test-frontend.sh'
            junit 'target/test-results/**TEST*.xml'
          }
        }

        stage('Test Java 7 static') {
          agent {
            node {
              label 'java7'
            }

          }
          steps {
            unstash 'Java 7'
            sh './jenkins/test-static.sh'
          }
        }

        stage('Test Java 7 backend') {
          agent {
            node {
              label 'java7'
            }

          }
          steps {
            unstash 'Java 7'
            sh './jenkins/test-backend.sh'
            junit 'target/surefire-reports/**TEST*.xml'
          }
        }

        stage('Test Java 7 performance') {
          agent {
            node {
              label 'java7'
            }

          }
          steps {
            unstash 'Java 7'
            sh './jenkins/test-performance.sh'
          }
        }

        stage('Test Java 8 frontend') {
          agent {
            node {
              label 'java8'
            }

          }
          steps {
            unstash 'Java 8'
            sh './jenkins/test-frontend.sh'
            junit 'target/test-results/**TEST*.xml'
          }
        }

        stage('Test Java 8 static') {
          agent {
            node {
              label 'java8'
            }

          }
          steps {
            unstash 'Java 8'
            sh './jenkins/test-static.sh'
          }
        }

        stage('Test Java 8 backend') {
          agent {
            node {
              label 'java8'
            }

          }
          steps {
            unstash 'Java 8'
            sh './jenkins/test-backend.sh'
            junit 'target/surefire-reports/**TEST*.xml'
          }
        }

        stage('Test Java 8 performance') {
          agent {
            node {
              label 'java8'
            }

          }
          steps {
            unstash 'Java 8'
            sh './jenkins/test-performance.sh'
          }
        }

      }
    }

    stage('Confirm Deploy') {
      steps {
        timeout(time: 30, unit: 'SECONDS') {
          input(message: 'Do you want to deploy to staging?', ok: 'Yes, lets do it!')
        }

      }
    }

    stage('Fluffy Deploy') {
      agent {
        node {
          label 'java8'
        }

      }
      steps {
        unstash 'Java 7'
        sh './jenkins/deploy.sh staging'
      }
    }

  }
}
