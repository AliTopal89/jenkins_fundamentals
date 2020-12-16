## JENKINS PIPELINE INTERMEDIATES

### Recent Declarative Pipeline Features

**Declarative Directive Generator**:
- Allows ypu to generate Pipeline code for Declarative Pipeline directive such as `agent`, `option`, `parameters`, `when` etc. For example:
  - ```groovy
        parameters {
          choice choices: ['', 'blah', 'moreBlah', 'evenMoreBlah'], description: 'types of blah', name: 'blah'
        }
    ```
**New When Conditions**:
- equals - returns true if two values are equal. can be used for `when { not { equals } }`

  -   ```groovy
          when { equals expected: 2, actual: currentBuild.number }
          steps {
           sh 'make install'
          }
      ```
- changeRequest - Executes the stage if the current build is for a "change request". By adding a filter attribute with parameter to the change request, the stage can be made to run only on matching change requests. Possible attributes are `id`, `target`, `branch`, `fork`, `url`, `title`, `author`, `authorDisplayName`, and `authorEmail`. The optional parameter comparator may be added after an attribute to specify how any patterns are evaluated for a match:
  - ```groovy
         when { changeRequest authorEmail: "[\\w_-.]+@example.com", comparator: 'REGEXP' }
    ```
- buildingTag - condition that just checkers if the pipeline is running a tag vs branch or commmit
  - ```groovy
         when { buildingTag() }
    ```
- tag - a more detailed version of `buildingTag` with specific naming convention
  - ```groovy
       stage('Deploy'){
           when { tag "release-*" }
           sh 'make deploy'
       }
    ```
- beforeAgent - This allows you to specify that the when conditions should be evaluated before entering the `agent` for the `stage`, rather than the normal behavior of evaluating when conditions after entering the agent. When `beforeAgent` true is specified, you will not have access to the agent’s workspace, but you can avoid unnecessary SCM checkouts and waiting for a valid `agent` to be available. This can speed up your Pipeline’s execution in some cases.
  - ```groovy
       stage('Deploy '){
          agent{ label 'agent-007' }
          when { 
            beforeAgent true
            branch 'production'
          }
          steps {
            echo 'deploying'
          }
       }
    ```
**New Post Conditions**:

- `fixed` - This will check to see if the current run is successful, and if the previous run was either failed or unstable.
- `regression` - This will check to see if the current run’s status is worse than the previous run’s status. So if the previous run was successful, and the current run is unstable, this will fire and its block of steps will execute. It will also run if the previous run was unstable, and the current run is a failure.

**New Options**:

- `checkoutToSubdirectory` - Using `checkoutToSubdirectory("foo")`, your Pipeline will checkout your repository to `"$WORKSPACE/foo"`, rather than the default of `"$WORKSPACE"`.

- `newContainerPerStage` - If you’re using a top-level `docker` or `dockerfile` agent, and want to ensure that each of your stages run in a fresh container of the same image, you can use this option. Any `stage` without its own `agent` specified will run in a new container using the image you’ve specified or built, on the same computer and with access to the same workspace.

**Stage Options**:

Sometimes, you may only want to disable automatic checkout of your repository, using the `skipDefaultCheckout(true)` option, for one specific stage in your Pipeline. Or perhaps you want to have a `timeout` that covers an entire `stage`, including time spent waiting for a valid `agent`, `post` condition execution, or the new input directive for stages.
You can use a subset of the top-level options content in a stage’s `options - wrapper steps, and Declarative-specific options that are marked as legal in a stage.

**Input Directive to Stage**:

- ```groovy
    pipeline {
    agent none
        stages {
            stage('Example') {
                input {
                    message "Should we continue?"
                    ok "Yes, we should."
                    submitter "alice,bob"
                    parameters {
                    string(name: 'PERSON', defaultValue: 'Mr Jenkins', description: 'Who should I say hello to?')
                    }
                }

                when { equals expected: "Fred Durst", actual: "${PERSON}" }
                
                agent any
                steps {
                  echo "Hello, ${PERSON}, nice to meet you."
                }
            }
        }
    }
  ```

- the input directive is evaluated before you enter any agent specified on this stage, so if you are using a top-level agent none and each stage has its own agent specified, you can avoid consuming an executor while waiting for the input to be submitted.

- with the same parameters as the input step. When you use the stage input directive rather than using the step directly, any parameters you’ve specified for the input will be made available in the stage’s environment, meaning you can reference parameters from the `input in when conditions, or in environment variables.

### Using Docker wth Pipeline

#### Caching Data for Containers

Pipeline supports adding custom arguments that are passed to Docker, allowing users to specify custom Docker Volumes to mount - used to cache data betweeen pipeline runs for example:

```groovy
pipeline {
  agent {
    docker {
      image 'maven-3:alpine'
      args '-v $HOME/.m2:root/.m2'
    }
  }
}
```
#### Using Multiple Containers

Combining Docker and Pipeline allows a Jenkinsfile to use multiple types of languages by combining
`agent {}` directive, with different stages

[Multile Containers](../pipeline-exercise/multi-container.groovy)

#### Using a Dockerfile

```Dockerfile
FROM node:11-alpine
RUN apk add -u subversion
```

```groovy
pipeline{
  agent { dockerfile true }
  stages {
    stage('Test') {
      steps {
        sh 'node --version'
        sh 'svn --version'
      }
    }
  }
}
```

### Prepare for Shared Libraries

#### Scripted Pipeline

Scripted Syntax is a domain specific language based on Apache Groovy

Scripted syntax provides flexibility and extensibility to Jenkins users but the learning curve
is steep, howver, declarative syntax offers simpler and opinionated syntax for jenkins pipelines.

Both Scripted and Declarative:
  - Have the same Pipeline subsystem underneath
  - Can use steps built into the pipeline or provided by plugins
  - Can utilize Shared Libraries

Declartive - limits what is available to the user with mor strict pre-defined structure making it an ideal choice for simpler CI
Scripted - the only limits on structure and syntax are from Groovy's own limitations, not Pipeline-specific. 

Use `script` step to introduce Scripted syntax only when you really need to.

#### Using a Jenkinsfile

Jenkins pipeline uses rules similiar to Groovy for string interpolation

Groovy supports declaring a string with either single quotes or double quotes

```groovy
def singleQuote = 'Hello'
def doubleQuote = "World"
```
String interpolation only works for strings in double quotes, not for single-quote strings

For Example:

```groovy
def username = 'Jenkins'
echo 'Hello Mr. ${username}'
echo "Yo Hello Mr. ${username}"
```
Result in:

```
Hello Mr. ${username}
Yo Hello Mr. Jenkins
```

#### Using/Setting ENV Variables

- Using env variables:

  ```groovy
  steps {
    echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL}"
  }
  ```
- Setting Env Variables
  - An `environment` directive defined within a `stage` applies to steps within that stage, used in 
    top-level `pipeline` block applies to all steps.

  - ```groovy
       pipeline {
         agent {docker { image 'node:8-alpine'} }
         environment {
           CC = 'clang'
         }
         stages {
           stage('Example') {
           environment {
             DEBUG_FLAGS = '-verbose'
             }
             steps {
                 sh 'blah'
             }
           }
         }
       }
    ```

#### Credentials

The environment directive supports a special helper method `credentials()`

**Username and Password**:
- The environment variable specified set to use `username:password` will have two additional
  env variables that are defined automatically `MYVARNAME_USR` & `MYVARNAME_PSW`
- ```groovy
       pipeline {
         agent { label 'docker-jenkins-rails' }
         environment { SERVICE_CREDS = credentials('my-denfined-cred') }

         stages {
           stage() {
             steps {
               sh """
                  echo "Service user $SERVICE_CREDS_USR"
                  echo "Service Password $SERVICE_CREDS_PSW"
                  curl -u $SERVICE_CREDS https://mycoolservice.example.com
                  """
             }
           }
         }
       }
  ```

**Secret Text**:

- ```groovy
     //...
     environment {SOME_SECRET_TEXT = credentials('jenkins-secret-id') }
     //stages stage etc...
         steps {
           sh """
               echo "Hello this is the $SOME_SECRET_TEXT"
              """
         }
    // env variable specified will be set to the secret text context. 
  ```

**Secret File**:

- ```groovy
     //...
     environment {SOME_SECRET_FILE = credentials('jenkins-secret-id') }
     //stages stage etc...
         steps {
           sh """
               echo "Hello this is the $SOME_SECRET_FILE"
              """
         }
    // the env variable specified will be set to the location of the file that is temporarily created
  ```

**SSH with Private Key**:

- ```groovy
     //...
     environment { SSH_CREDS = credentials('jenkins-ssh-creds') }
     //stages stage etc...
         steps {
           sh """
               echo " Ssh Private key is located at $SSH_CREDS "
               echo "Ssh user $SSH_CREDS_USR"
               echo "Ssh passphrase $SSH_CREDS_PSW"
              """
         }
    // env variable will be set to location of the ssh key file, created with two additonal
    // env variables with is SSH_CREDS_USR & SSH_CREDS_PSW(holding the private key passphrase)
  ```

#### Parameters

```groovy
//pipeline agent ...
parameters {
  sttring (name: 'Greetings', defaultValue: 'Hey whats up', description: 'How should I greet?')
}
// stages stage
    steps {
      echo "${params.Greeting} Jenkins"
    }
```

#### Handling Failure

```groovy
pipeline {
  // agent stages stage steps...
  post {
    always {
        junit '**/target/*.xml'
    }
    failure {
      mail to: mail.example.com, subject: 'Hey this build failed :('
    }
  }
}
```

#### Multibranch Pipelines

- Configured to point to a SCM
- Without Multibranch, each pipeline maps to only one branch of the SCM
- Supports Pull Requests as well
- Basically its a folder and is implemented as a Jenkins job type
- Customizable retention policy
- Triggers - if you are not using webhooks you can use it like cron jobs

#### Pipeline without Blue Ocean

Create a new multibranch job -> configure your scm and then create a Jenkinsfile like:
```groovy
pipeline {
  agent any
  stages {
    stage('Example') {
      steps {
        sh 'echo Hello Jenkins example'
      }
    }
  }
}
```
and then git push

#### Intro to Shared Libraries

- Allows you share and reuse pipeline code
- Supports collaboration between larger number of teams working on a large number of projects
- A seperate SCM repo that contains reusable custom steps that can be called from Pipelines
- Configured once per jenkins instance
  - Manage Jenkins » Configure System » Global Pipeline Libraries
- Cloned at Build Time
- Loaded and used as code libraries for Jenkins Pipelines
- First step is not easy, requires deeper understanding of Pipeline

- For Shared Libraries which only define Global Variables (`vars/`), or a `Jenkinsfile` which only needs a Global Variable, the annotation pattern `@Library('my-shared-library') _ `may be useful for keeping code concise. In essence, instead of annotating an unnecessary import statement, the symbol `_` is annotated.

##### Notes:

retention: the continued possession, use, or control of something.
retention policy: It describes how long a business needs to keep a piece of information (record), where it's stored and how to dispose of the record when its time

### Create Shared Libraries

- Configure a Global Pipeline Library in Jenkins
- Create a Seperate SCM repo

**SCM Directory Structure**:

```
(root)
+- src                     # Groovy source files
|   +- org
|       +- foo
|           +- Bar.groovy  # for org.foo.Bar class
+- vars
|   +- foo.groovy          # for global 'foo' variable
|   +- foo.txt             # help for 'foo' variable
+- resources               # resource files (external libraries only)
|   +- org
|       +- foo
|           +- bar.json    # static helper data for org.foo.Bar
```

- `src` directory uses standard Java based structure
- This directory is added to the `classpath` when executing pipelines

- `vars` directory contains scripts that define "custom steps" accessible from Pipelines
- can't use subdirectories for vars directory
- the matching `.txt` (or `.md`) etc can contain documentation

- *libraryResources* reads files from `resources` directory
- Exernal libraries may load adjunct files from a `resources/` directory using the
  `libraryResource`  step. The argument is a relative pathname, akin to Java resource loading:

  ```groovy
  def request = libraryResource 'com/mycorp/pipeline/somelib/request.json'
  ```

**How to Configure Shared Librarires**:

-  Global Libraries configured in Jenkins are considered *trusted*
  - Stepls from this library runs *outside* of Groovy Sandbox
- Libraries configured at multibranch/folder level are considered *not trusted*
  - Steps from this librart run *inside* the Groovy Sandbox
  - Prefer libraries at multibranch/folder level to reduce risk to Jenkins server
    from libraries outside the sandbox
- When *Load Implicty* is enabled, the default branch is automatically available to all
  Pipelines custom steps which can be also loaded manualy using `@Library` annotation.

### Lab: Global Pipeline shared library

- Manage Jenkins -> Configure System -> Add Global Pipeline Libraries
- Retrieval Method:`Modern SCM` -> Git -> `http://localhost:5000/gitserver/butler/shared-library`

[Jenkinsfile for shared library](../pipeline-exercise/global-pipeline-lab.groovy)

### Shared Library Custom Steps

- Create a file that has the desired name of the custom step
- Add `call()` method inside the file
- ```groovy
    def call(String name, String dayOfWeek) {
      sh "echo Hello World ${name}. It is ${dayOfWeek}"
    }
  ```
- another example:
  ```groovy
    // inside shared library /vars/postBuildSuccess.groovy
    def call(Map config = [:]){
      stash(name: "${config.stashName}", includes: 'target/**')
      archiveArtifacts(artifacts: 'target/*.jar')
    }
    

    // inside Jenkinsfile
    //...
    steps {
      postBuildSuccess(stashName: "Java 7")
    }
  ```

### Call the Shared Library Custom Step

- Simple Hello World example:

[Jenkinsfile and groovy call method](../pipeline-exercise/shared-library-simple-hello.groovy)

#### Custom step to send slack notifications

[Custom Slack simple notifications](../pipeline-exercise/custom-simple-slack-notifications.groovy)

However this can be made more concise and less repetitive looking add new call methods and use those on the post conditions

### Lab: Use a Custom Step

[Custom Step Lab Exercise](../pipeline-exercise/custom-step-lab.groovy)

### Library Resource

- For Example:
  - instead of doing an inline body for an email, load the body of the message from a file

[Email extension configuration exercise](../pipeline-exercise/email-template-render.groovy)

### Further Reading and References

1. [What is new in declarative](https://www.jenkins.io/blog/2018/04/09/whats-in-declarative/)
1. [Docker pipeline plugin](https://docs.cloudbees.com/docs/admin-resources/latest/plugins/docker-workflow)
1. [Scripted Pipeline](https://www.jenkins.io/doc/book/pipeline/syntax/#scripted-pipeline)
1. [Branch and Pull Requests](https://www.jenkins.io/doc/book/pipeline/multibranch/)
1. [Pipeline as code with multibranch](https://www.jenkins.io/blog/2015/12/03/pipeline-as-code-with-multibranch-workflows-in-jenkins/)
1. [Extending with Shared Libraries](https://www.jenkins.io/doc/book/pipeline/shared-libraries/)
1. [Java Class GStringEngine](http://docs.groovy-lang.org/docs/groovy-2.4.9/html/gapi/groovy/text/GStringTemplateEngine.html)
