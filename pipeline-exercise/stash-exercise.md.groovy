pipeline {
  agent none
  stages {
    stage('Buzz Build') {
      parallel {
        stage('Build Java 7') {
          agent {
            node {
              label 'java7'
            }

          }
          steps {
            sh('''
                echo "I am ${BUZZ_NAME}"
                ./jenkins/build.sh
               ''')
            archiveArtifacts(artifacts: 'target/*.jar', fingerprint: true)
            stash(name: 'Buzz Java 7', includes: 'target/**')
          }
        }

        stage('Build Java 8') {
          agent {
            node {
              label 'java8'
            }

          }
          environment {
            BUZZ_NAME = 'Java 8 Bee'
          }
          steps {
            sh('''
                echo I am ${BUZZ_NAME}
                ./jenkins/build.sh
               ''')
            archiveArtifacts(artifacts: 'target/*.jar', fingerprint: true)
            stash(name: 'Buzz Java 8', includes: 'target/**')
          }
        }

      }
    }

    stage('Buzz Test') {
      parallel {
        stage('Testing A 7') {
          agent {
            node {
              label 'java7'
            }

          }
          steps {
            unstash 'Buzz Java 7'
            sh './jenkins/test-all.sh'
            junit '**/surefire-reports/**/*.xml'
          }
        }

        stage('Testing B 7') {
          agent {
            node {
              label 'java7'
            }

          }
          steps {
            unstash 'Buzz Java 7'
            sh './jenkins/test-all.sh'
            junit '**/surefire-reports/**/*.xml'
          }
        }

        stage('Testing A 8') {
          agent {
            node {
              label 'java8'
            }

          }
          steps {
            unstash 'Buzz Java 8'
            sh './jenkins/test-all.sh'
            junit '**/surefire-reports/**/*.xml'
          }
        }

        stage('Testing B 8') {
          agent {
            node {
              label 'java8'
            }

          }
          steps {
            unstash 'Buzz Java 8'
            sh './jenkins/test-all.sh'
            junit '**/surefire-reports/**/*.xml'
          }
        }

      }
    }

  }
  environment {
    BUZZ_NAME = 'Worker Bee'
  }
}
