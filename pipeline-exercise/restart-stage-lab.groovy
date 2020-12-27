def call(body) {

  def pipelineParams = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = pipelineParams
  body()

  pipeline {
    agent none
    stages {
      stage('Fluffy Build and Fluffy Test') {
        parallel {        
        stage('Java 7') {
            agent { label 'java7' }
            stages {
            // ...
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
        agent { label 'java7' }
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
      durabilityHint('MAX_SURVIVABILITY')
      preserveStashes(buildCount: 5)
    }
  }  
}