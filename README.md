### Jenkins fundementals

Jenkins is an open source automation server

Jenkins is the CD Orchestrator
 - Commit -> Build -> Test -> Stage -> Deploy -> Run

Cloudbees and the Jenkins community
 - Organizes Jenkins World and supports numerous Jenkins events

Continuous Integration(CI)
  - is the frequent, automatic integration of code, meaning, automatically tests all new and modified code with the master code

Continuous Delivery(CD)
 - ensures that the code is always ready to be deployed

Continuous Deployment
  - automatically deploys all the validated changes to production

#### Source Code Management (SCM)
  - is used to track modifications to a source code repository. SCM tracks a running history of changes to a code base and helps resolve conflicts when merging updates from multiple contributors. 

#### Types of testing 
  - Unit tests - test a small piece of code 
   - a function or method or command
  - Integration tests - Validate integration between multiple sub-systems
   - including external sub-systems like a database
  - Smoke tests - validates basic functions of the system
  - Functional tests - validate the normal software behavior against the expectations and requirements
  - Non-regression tests - validate that the system still produces the same end result
  - Acceptance tests - test the full product from the perspective of an end user cases

### Installation requirements
  - Change JVM memory Heap Size: `-Xms1G -Xmx2G`
    - ```java
         java -Xmx64m -classpath ".:${THE_CLASSPATH}" ${PROGRAM_NAME} 
      ```
  - G1 garbage collector for heap > 4GB: `-XX:+UseG1GC`
    - `Garbage First garbage collector (G1 GC)`
    - `-XX:+UseG1GC - Tells the JVM to use the G1 Garbage collector.`
    - For example:
    ```java
    java -Xmx50m -Xms50m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar c:\javademos\demo\jfc\Java2D\Java2demo.jar
    ```
    
### Installing Jenkins and Running Jenkins

- Settings are in `/etc/default/jenkins` or in `/etc/sysconfig/jenkins`
- `$JENKINS_HOME` defaults to `/var/lib/jenkins`

Web Application Archive (WAR) distribution of jenkins can be either 
  - run as a stand-alone application 
  - servlet container

#### WAR as Standalone

```java
java ${JAVA_OPTS} -jar jenkins.war ${JENKINS_OPTS}
// JENKINS_OPTS - httpPort, prefix, logfile etc.
```
For example:

```java
java -jar jenkins.war --httpPort=8081 --prefix=/ci httpListenAddress=127.0.0.1
```  

JENKINS_HOME directory structure
  - config.xml - General configuration file
  - jobs/ directory - Build job workspaces, hisory and artifacts.
  - plugins/ directory - installed plugins
  - users/ directory - local user databases

**d. Artifacts are files which are associated with a single build. A jenkins build can have any number of artifacts associated with it**

### Install Jenkins updates

  - Download from "Manage Jenkins"
  - Use "Prepare Shutdown" screen under "Manage Jenkins"
  - Replace **jenkins.war** file usually on Linux this located in `/usr/share/jenkins`
  - Restart jenkins and apply the upgrade 

### Manage Jenkins

- **Configure System** 
  - most of the fundamental tools that Jenkins uses. Plugins may add sections to this page

- **Number of executors**
  - Unit of task execution on a computer
  - Set this to `0` to prevent builds from running on master

- **Quiet Period**
  - When this option is non-zero, newly triggered builds of this project will be added to the queue, but Jenkins will wait for the specified period of time (in seconds) before actually starting the build.

  - For example, if your builds take a long time to execute, you may want to prevent multiple source control commits that are made at approximately the same time from triggering multiple builds. Enabling the quiet period would prevent a build from being started as soon as Jenkins discovers the first commit;

- **SCM and AUthentication**
  - Git, SVN, Mercurial, Team Foundation Server... all use credentials plugin

- **Timestamper**
  - Systemtime format
    - `'<b>'HH:mm:ss'</b> '`
    - The system clock time format defines how the timestamps will be rendered. Default is like above
  - Endtime Format
    - The elapsed time format defines how the timestamps will be rendered when the elapsed time option has been selected.

#### Global Tools Configuration

For Example:

- **Install JDK**
  - Name	openjdk8
 	  JAVA_HOME	 /usr/lib/jvm/java-8-openjdk-amd64
  - AUtomatic install asks for `Install Oracle Java SE Development Kit from the website`
    - Select Java SE Development Kit installation 

- **Reload config from Disk**
  - Use this when you modify Jenkins or its environment outside of the UI

### Security

- **Configure Global Security**
  - Define how users are authenticated and what they are authorized to do.

- **Manage Users Screen**
  - is used to add user to the Jenkins user database and lists all users who are in the database

- **Configure Credentials**
  - Jenkins credentials control access to third party sites and applications such as artifact repos and cloud based storage

- **Script Console**
  - Allows you to execute arbitrary Groovy scripts on the server.
  - Apache Groovy is the foundation of the DSL used for Jenkins Pipelines
  - ```groovy
       Jenkins.instance.queue.clear()
    ```
- **Manage Nodes**
  - Recap of NODES, AGENTS & EXECUTORS:
    - A `node` (e.g. jdk8-node) is a server where Jenkins runs jobs on `executors`
    - The `agent` is the tool that manages the `executors` on a remote `node` on behalf of Jenkins.
    - The Jenkins master also runs on a `node` 

    - Click the flywheel to the right of the node to configure the items Jenkins monitors for all nodes it runs. 
    - This is where you can also define the number of executors and other characteristics of the node. 

- **Monitoring Node Usage**
  - Load Statistics:
    - page to monitor node utilization
  - Number of online executors
  - Number of busy executors
  - Number of available executors
  - Queue length

  - *Note: The number of busy executors and the number of available executors* 
  *need not necessarily be equal to the number of online executors as executors* 
  *can be suspended from accepting builds and thus be neither busy nor available.*

- **About Jenkins**
  - Release and version of Jenkins running
  - List of all third party libraries
  - List of installed plugins

- **System Information**
  - provides detailed information with what is available on this Jenkins instance, for example"
    - `java.version	1.8.0_212` - system
    - `JAVA_HOME	/usr/lib/jvm/java-1.8-openjdk` -env 
    - `gradle	1.36` - plugin

- **Jenkins CLI**
  - provides commands that perform functions that are usually executed using the GUI
    - Available commands, for example:
      - `java -jar jenkins-cli.jar -s http://localhost:5000/jenkins/ -webSocket help`

- **Service Lifecycle**
  - Managing options used to `/start/stop/reload`

### Install Maven lab notes learned the hard way

- install the jenkins.war file from previos lectures which looks like an example but it does need it seems like
- Manage Jenkins -> Global Configuration and set JDK Installation with 
  - `NAME` `ORACLE JDK8`
  - `NAME` `ORACLE JDK7`
  - Install automatically
  - Git - `Path to git executable` `/usr/bin/git`
  - Maven lab practive
    - `NAME` `mvn3`, `Version 3.5.4`, Install automatically
    - `NAME` `mvn2`, `Version 2.2.1`, Install automatically
  - APPLY & SAVE

Notes: You may have to install jenkins.war, from jenkins upgrades lecture.
I didn't install the jenkins.war file from that lecture at first, once I installed that `java -jar ~/Downloads/jenkins.war httpListenAddress=http://localhost:5000/jenkins/`
I was able to do maven project and free-style job works just fine. 


### Jenkins Plugins

What are Plugins?
- A plugin is JAR file with some special conventions. Jenkins uses plugins to provide much of its functionality.
This **"modular"** architecture means that your Jenkins installation has the features and functionality you need and not get
bloated with functionality you don't need.

- Contained in a fule with an hpi or jpi extension

- Stored in `${JENKINS_HOME}/plugins` unless you use `--pluginroot` to change the binaries of the plugin
- Plugins are versioned artifacts that can be upgraded

- Installation Wizard will suggest plugins that provide commonly-used functionality.

#### Manage Plugins

- Installed Tab:
  - Enabled - Check mark indicates that the plugin is enabled
    - Greyed out check marks indicate plugins that are required by other plugins 
    *(Always thought they were plugins installed/updated by Jenkins by default)*
- Uninstall a plugin:
  - Removes the plugin binary(hpi or jpi extension) from the disk
  - Does not remove the configuration that the plugin may have created 
    - If the plugin contributed extensions to any job/build/views/agents etc, Jenkins reports unrecognized
    fragments in config files
    - Until you remove the config file (config.xml) you can re-install the plugin and restart 
    jenkins to restore the configurations.
- Disable a plugin:
  - softer way to retire a plugin, usually binary is changed to jpi/hpi.disabled
- Update plugins:
  - Always read the Changelog information of new plugins before installing it.
  *(Yet Another Docker definitelly killed us at one point)*

*Click advanced on plugin manager if you are running under HTTP Proxy.*

- Plugins can be installed manually without "Manage Plugins"
  - The documentation for each plugin lists all dependencies, you would 
  have to manually install optional dependencies though
    - For Example click `Branch API` plugin, go to Dependencies tab 
      - ```
        Required
        Folders ≥ 6.14
        SCM API ≥ 2.6.3
        *Implied*
        Trilead API ≥ 1.0.4
        ```
  - Some plugins requie

Note on Implied plugins:
  - ```
    Plugins that depend on a Jenkins core version before such a plugin was detached 
    from core may or may not actually use any of its features. To ensure that 
    plugins don't break whenever functionality they depend on is detached from Jenkins core, 
    it is considered to have a dependency on the detached plugin if it declares a dependency 
    on a version of Jenkins core before the split. Since that dependency to the detached plugin 
    is not explicitly specified, it is implied.
    ```
