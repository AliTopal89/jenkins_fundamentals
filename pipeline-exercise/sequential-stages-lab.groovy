//  /shared-library/vars/corporateSequentialPipeline.groovy

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
      stage('Fluffy Build and Fluffy Test') {
        parallel {
          stage('Java 8') {
            agent { label 'java8' }
            stages {
              stage ('build 8') {
                steps {
                  //sh './jenkins/build.sh'
                  runLinuxScript(name: "build.sh")
                }
            
                post {
                  success {
                    stash(name: 'Java 8', includes: 'target/**')
                  }
                } // post
              } // build 8

              stage('Test Java 8 frontend') {
                steps {
                  unstash 'Java 8'
                  sh './jenkins/test-frontend.sh'
                }
                    
                post {
                  always {
                    junit 'target/test-results/**TEST*.xml'
                  }
                } 
              }

              stage('Test Java 8 static') {
                steps {
                  unstash 'Java 8'
                  sh './jenkins/test-static.sh'
                }
              }

              stage('Test Java 8 backend') {
                steps {
                  unstash 'Java 8'
                  sh './jenkins/test-backend.sh'
                }
                post {
                  always {
                    junit 'target/surefire-reports/**TEST*.xml'
                  }
                } 
              }

              stage('Test Java 8 performance') {
                steps {
                  unstash 'Java 8'
                  sh './jenkins/test-performance.sh'
                }
              }
            } // stages - nested
          } // Java 8
          stage('Java 7') {
            agent { label 'java7' }
            stages {
              stage('build Java 7') {
                steps {
                    //sh './jenkins/build.sh'
                    runLinuxScript(name: "build.sh")
                }
                post {
                  success {
                    postBuildSuccess(stashName: "Java 7")
                  }
                } // post
              }
                
              stage('Test Java 7 frontend') {
                steps {
                  unstash 'Java 7'
                  sh './jenkins/test-frontend.sh'
                }
                post {
                  always {
                    junit 'target/test-results/**TEST*.xml'
                  }
                }
              }

              stage('Test Java 7 static') {
                steps {
                  unstash 'Java 7'
                  sh './jenkins/test-static.sh'
                }
              }

              stage('Test Java 7 backend') {
                steps {
                  unstash 'Java 7'
                  sh './jenkins/test-backend.sh'
                }
                post {
                  always {
                  junit 'target/surefire-reports/**TEST*.xml'
                  }
                }
              }

              stage('Test Java 7 performance') {
                steps {
                  unstash 'Java 7'
                  sh './jenkins/test-performance.sh'
                }
              }
            } //stages - nested
          }  // Java 7
        } // paralel
      } // Fluffy Build and Fluffy Test
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
        agent { label 'Java 7' }
        when {
          branch 'master'
        }
        steps {
          unstash 'Java 7'
          sh "./jenkins/deploy.sh ${pipelineParams.deployTo}"
        }
      }
    } // stages - outer
    options {
        durabilityHint('MAX_SURVIVVABILITY')
    }
  }  
}


//Jenkinsfile

@Library('shared-library') _
corporatePipelineSequential {
    //pipelineParams.deployTo
    deployTo = "dev"
}