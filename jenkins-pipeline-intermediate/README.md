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

### Further Reading and References

1. [What is new in declarative](https://www.jenkins.io/blog/2018/04/09/whats-in-declarative/)
1. [Docker pipeline plugin](https://docs.cloudbees.com/docs/admin-resources/latest/plugins/docker-workflow)