//.../vars/sendNotifications.groovy

def renderTemplate(input, binding) {
  def engine = new groovy.text.GStringTemplateAgent()
  def template = engine.createTemplate(input).make(binding)
  return template.toString()
}

def call (Map config =[:]){
  // commented for library resource exercise
  //slackSend (
    //color: "${config.slackSendColor}",
    //message: "${config.message}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
  //)


  //located in  ../resources/emailtemplates/build-results.html/
  def rawBody = libraryResource 'emailtemplates/build-results.html'
  def binding = [
    applicationName: env.JOB_NAME,
    buildNumber    : env.BUILD_NUMBER,
    buildUrl       : env.BUILD_URL,
    message        : config.message
  ]
  def emailBody = renderTemplate(rawBody,binding)
  
  emailext (
    subject: "${config.message}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
    body: emailBody,
    recipientProviders: [[$class: 'DevelopersRecipientProvider']]
  )
}

// ../resources/emailtemplates/build-results.html/
```html
<p>$message: Job '$applicationName [$buildNumber]':</p>
<p>Check console output at <a href="$buildUrl">$applicationName [$buildNumber]</a></p>

```

// used to be
/*  emailext (
    subject: "${config.message}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
    body: """<p>${config.message}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
<p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>""",
    recipientProviders:[[$class: 'DevelopersRecipientProvider']]
  )
} */
