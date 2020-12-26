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

    options{
       durabilityHint('MAX_SURVIVABILTY')
    }
    stages {
      stage('Fluffy Build') {
        parallel {
          stage('Build Java 7') {
            agent {
              node {
                label 'java7'
              }
            }
            //...

          }
        }
      }
    }
  }