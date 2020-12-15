// .../vars/helloWorldSimple.groovy
def call(String name, String dayOfWeek) {
  sh "echo Hello World ${name}. It is ${dayOfWeek}"
}
// .../vars/sendNotifications.groovy
def call (Map config =[:]){
  slackSend (
    color: "${config.slackSendColor}",
    message: "${config.message}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
  )
}

@Library('shared-library@shared-starter') _
pipeline { 
    agent any 
    
    stages {
        stage('hello') {
            steps {
                helloWorldSimple('Vegeta', 'Thursday')
                //helloWorld(name: 'Senheiser')
            }
        }
    }
    
    stages {
        stage('Start') {
            steps {
                //the .groovy filename is used with call
                sendNotifications (
                  slackSendColor: "#fffb00",
                  message: "STARTED"
                )
            }
        }
    }
    
    post {
        success {
            sendNotifications (
              slackSendColor: "#00ff6e",
              message: "SUCCESSFUL yeay"
            )
        }
        failure {
            sendNotifications (
              slackSendColor: "#ff1500",
              message: "FAILED oh no"
            )
        }
    }
}
