//  /shared-library/vars/corporatePipeline.groovy

def call(body) {
  // evaluate the body block, and collect configuration into the object
  
  //`body` is the *owner*, and by default is the *delegate*. 
  //But when you switch the *delegate* to be `config`, and tell it to use the delegate first, 
  //you get the variables `config's` scope.
  def pipelineParams = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = pipelineParams
  body()
  
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
            post {
              success {
                postBuildSuccess(stashName: "Java 7")
              }

            }
            steps {
              //sh './jenkins/build.sh'
              runLinuxScript(name: "build.sh")
            }
          }

          stage('Build Java 8') {
            agent {
              node {
                label 'java8'
              }

            }
            post {
              success {
                stash(name: 'Java 8', includes: 'target/**')
              }

            }
            steps {
              //sh './jenkins/build.sh'
              runLinuxScript(name: "build.sh")
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
            post {
              always {
                junit 'target/test-results/**TEST*.xml'
              }

            }
            steps {
              unstash 'Java 7'
              sh './jenkins/test-frontend.sh'
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
            post {
              always {
                junit 'target/surefire-reports/**TEST*.xml'
              }

            }
            steps {
              unstash 'Java 7'
              sh './jenkins/test-backend.sh'
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
            post {
              always {
                junit 'target/test-results/**TEST*.xml'
              }

            }
            steps {
              unstash 'Java 8'
              sh './jenkins/test-frontend.sh'
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
            post {
              always {
                junit 'target/surefire-reports/**TEST*.xml'
              }

            }
            steps {
              unstash 'Java 8'
              sh './jenkins/test-backend.sh'
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
        when {
          branch 'master'
        }
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
        when {
          branch 'master'
        }
        steps {
          unstash 'Java 7'
          sh "./jenkins/deploy.sh ${pipelineParams.deployTo}"
        }
      }
    }
  }  
}


//Jenkinsfile

@Library('shared-library') _
corporatePipeline {
    //pipelineParams.deployTo
    deployTo = "dev"
}