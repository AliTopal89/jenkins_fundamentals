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
#### Manual Plugin installation

Chuck Norris Plugin
- Go to `http://localhost:5000/chucknorris.hpi` 
- `Manage Plugin` -> `Advanced` -> `Upload Plugin`
- Upload the hpi file, then restart jenkins fom Update Center

Ways to install PLugins:
  - https://github.com/jenkinsci/docker#preinstalling-plugins -jenkins docker image 
  provides a script that fetches the plugins

  - https://github.com/jenkinsci/custom-war-packager - which allows you to package your
  own plugins. Custom WAR Packager (CWP) allows building ready-to-fly Jenkins packages using 
  a YAML specification. The tool can produce Docker images, WAR files, and Jenkinsfile Runner 
  docker images (aka single-shot Jenkins masters).

### Configure Notifications
- Notifications for CI/CD can be delivered with email, Slack and the most unfortunate TEAMS
- Built in Email Notifications
  - Main configuration is about SMTP(sending) server
  - Every failed build
  - Every successful build after a failed
  - Every unstable build after a successful 
    - triggers a new email
- Slack Notification:
  - Notifications can be configured to be sent for any build status
    - Start, Aborted, Failure, Unstable
    - Custom content like Commit list can be added etc.

### Distributed Build Architecture

Distrubuted builds run on nodes instead of master node. 
  - The Jenkins master is webserver that also actis as a brain for deciding how/when/where to run tasks
  - A `node` is a server whre Jenkins runs jobs for `executors`.
  - The `agent` is the tool that manages executors on remote node.
  - Jenkins Master
    - Files written when a pipeline executes, are written to the filesystem on the master 
      unless they are off-loaded to an artifact repo such as nexus.
    - Some administrative tasks (such as backups) are run on master 
    node (in our case at work, it is mounted to EFS from the agent).
    - A `node` is taken offline if Disk Space, free swap etc. go outside the configured threshold.
    - Distributed Builds:
      - Master - serves the http request and stores all important information `/var/lib/jenkins/workspace/...`
      - Advantages of distributed builds - `${JENKINS_HOME}` is proected, and makes the instance more reliable
      - 
    - Distributed Build agents:
      - Uses `slave.jar` file
      - on your browser at `jnlpJars/slave.jar` under `$JENKINS_URL`

#### Master agent connectors

- Agent to master control enabled by default
- An agent can be launced by ssh from master, agent managed as windwos service, 
  JNLP(Java web start), or a custom script from master
- Communication between Jenkins and Unix agents can use SSH
- SSHD only need to be installed on agents
  - Agents JAR file is managed by Jenkins master
  - masters SSH public key must be in remote node's authorized keys

##### Windows Service
To connect Jenkins to Windows agent, DCOM is an option *Although cloudbees suggest not to, info [here](https://support.cloudbees.com/hc/en-us/articles/203773700-Why-not-use-DCOM)*
  - Just need an admin username and password.

##### JNLP
- launced as a single command with `slave.jar` file
- TCP or HTTP tunneling socket connection is made
- TCP port must be defined on Global security
  - *TCP is connection-oriented, and a connection between client and* 
    *server is established before data can be sent. The server must be listening* 
    *(passive open) for connection requests from clients before a connection is established.*
- Can also be run "headlessly" (is software capable of working on a device without a graphical user interface) 
  via jar instead of Java Web Start with Gui

##### Custom Script
- Fetch `slave.jar` & Establish connection by launching remote agent.

#### Benefits of master agent node
- Scalability - when build demand increases scale up merely pointing Jenkins at another agent node,
- Security 
  - Job that run on master have full permissions to jenkins resources on master, potentially allowing
    malicious user to access private information
  - Use Cloud agents, that are created for each build then destroyed (YAD Plugin for example)

#### Installed tool problems

JDK versions, Maven, Gradle etc are installed causing too many versioning problems?

Dedicated Agents:

  - Specialized hardware/ operating systems
  - Specialized Software
  - User the `Restrict where this project can be run` option from project configuration

Fungible Agents:

  - Fungibility is the property of a good or commodity whose individual units
    are capable of mutual substitution
  - Configuring a machine to act as an agent inside your infrastructure can be tedious 
  and time consuming. This is especially true when the same set-up has to be replicated on 
  a large pool of agents. Because of this, is ideal to have `fungible` agents, which are agents 
  that are easily replaceable. Agents should be generic for all builds rather customized for a 
  specific job or a set of jobs. The more generic the agents, the more easily they are 
  interchanged, which in turn allows for a better use of resources and a reduced impact 
  on productivity if some agents suffer an outage

Cloud agents:
  - Agent in a cloud like Amazon EC2, MS Azure
  - Easier to do with container tech like Docker
    - Start a Cloud-Based agent
    - Run the build inside the agent
    - Then tear it down

#### Monitoring nodes

- NodeMonitor in Jenkins
- Runs healthcheck on CPU Memory Usage, Disk Space etc.

### Distributed Jenkins Build (LAB)

- ```bash
  cloudbees-devbox $ ssh -i /ssh-keys/vagrant_insecure_key -p22 \
  jenkins@jdk8-ssh-agent \
  echo "-- Hello from jdk8-ssh-agent"
  ```

- Copy the content of `/ssh-keys/vagrant_insecure_key` somewhere 

- Manage Jenkins -> Manage Nodes -> New Node
  - #2 of executors
  - Usage - `Use this node as much as possible`
    - *Use this node as much as possible: This is the default setting.*
      *In this mode, Jenkins uses this node freely. Whenever there is a*
      *build that can be done by using this node, Jenkins will use it.*
  - Host `jdk8-ssh-agent`
  - Credentials - `Jenkins (SSH Key for agent)` 
  - Click Advanced - Port 22
  - Host Key Verification Strategy: `Non Verifying Verification Strategy`
    - *This is the legacy behavior. It does not perform Host Key Verification and is considered unsafe.*
    - More [strategies](https://support.cloudbees.com/hc/en-us/articles/115000073552-Host-Key-Verification-for-SSH-Agents#howtosetuphostkeyverificationforjenkins)
  - Name - JAVA_HOME
  - Value - `/var/lib/jvm/java-1.8-openjdk`
  - Save
  - Agent may be offline for a few, click on Logs from left pannel to see the progress

#### Reconfiguring Master agent.

- Configure System
- numer of executors #0
- Usage - Only build jobs with label restrictions matching this node. 
- Configure Global Security
  - Enable Agent -> Master Access Control

### SECURITY

- Know the system
- Least privilede  - do not give permissions to everyone to do everything
  Manage AAA concepts:
    - Authentication - User acess
    - Authorization - User priviledges
    - Accounting - Monitor your system
- Defense in Depth - put scurity on layered systems
- Prevention is Good, Detection is better - monitor jenkins installation, detective mode!

#### Agents and Security

By Default, the pipeline  executes with full authority.
All of the pipeline logic, groovy conditional and loops executes on master
Creates `workspace` for each build that runs.
Pipeline calls steps that run on agents - Each of which are combination of scripts or commands.
  - Agent writes some files to nodes, sends data back to master, request info from master
  - Agents on master jenkins maybe able to access Jenkins configuration and workspace of other builds
  - An agent can write malicious code to local disk so the node can get tainted

For maximum security, run all builds on ephermal agents in the cloud 
that get destroyed after the build is finished.

Pipelines may need access to external resources such as Brakeman, Nexus, Elasticsearch DB
  - Store the usernames and password in Credentials instead of directly on pipeline

##### Authentication

- Security Realm tell jenkins which referential to use for authentication, which is a dedicated
database for user and passwords
  - Defines the security implementation used to establish the identity of users
  - Only one security realm can be active at a time
  - By default, users and groups come from jenkins internal db.
- 4 kinds of Realm
  - Jenkins User Database
  - Unix user/group Database
  - Server Container
  - External LDAP
    - *LDAP provides the communication language that applications use to communicate* 
    *with other directory services servers. Directory services store the users,*
    *passwords, and computer accounts, and share that information with other entities on the network.*

Eg.
Configure Global Security -> Realm: Jenkins own user database -> 
  Manage Users -> Create User -> with Username Password and email
  - Moodify User with Flywheel to give description of user, 
    ssh public keys, timezone, modify username & password 

##### AUthorization

- Grants access rights to:
  - `Resource`: Task, Object/Action to manipulate
  - `Role`: Set of privileges grouped by comodity
  - `Requester`: User or group with role that want to manipulate resources

- Use security matrix to populate authorization
  - `Matrix based security`: assign global privileges to users/groups
  - `Project based matrix authorization strategy`: assign privileges based on specific projec
  - `Role Staretgy`: assign permission to specific roles

- Configure authorization

  - `Logged in users can do anything`: In this mode, every logged-in user gets 
  full control of Jenkins. The only user who won't have full control is 
  anonymous user, who only gets read access.

  - `Matrix-based security`: Permissions are additive. 
  That is, if an user X is in group A, B, and C, then the permissions that this 
  user actually has are the union of all permissions given to X, A, B, C, and anonymous.
    - `anonymous`: permission granted to all *unauthenticated* users who access Jenkins env
    - `authenticated` permissions granted to all *authenticated* users who access jenkins env
    - Permissions Groups: 
      - `Overall`
        - Administer - Make system wide configuration changes
        - Read - View almost all pages within Jenkins env
        - RunScripts - Run groovy scripts via the groovy console or groovy cli command.
          - If the script attempts and unapproved operation, it is killed
          - Unapproved operation is added to approval queue which an administrator can review
            and add it to the whitelist. 
        - UploadPlugins - Upload arbitrary plugins.
        - ConfigureUpdateCenter - Configure update sites and proxy settings.
      - `Credentials`
      - `Agent`
      - `Job`
      - `Run`
      - `View`
      - `SCM`
      - `Metrics`
      - `Lockable Resources`
  - `Project Based Matrix Authorization`: Similiar to Matrix based but define authorization by project,
  rather than user.
     - This authorization scheme is an extension to *Matrix-based security* which 
     allows additional access control lists (ACLs) to be defined for each project 
     separately in the Project configuration screen. This allows granting specific 
     users or groups access only to specified projects, instead of all projects in the Jenkins environment.

##### Accounting

- Occurs in the context of a user who is authenticated
- Mesures resources used or cosumed by users during access.
- all logging output to `stdout`

Logs on Linux:
- Default logs are available in `/var/log/jenkins/jenkins.log` can be customized though:
  - `/etc/default/jenkins` - for `*.deb`
  - `/etc/sysconfig/jenkins` - for `*.rpm`

Logs on Windows
  - `%JENKINS_HOME%/jenkins.out`
  - `%JENKINS_HOME%/jenkins.err`
  - location can be be customized in `%JENKINS_HOME%/jenkins.xml`

Logs on Docker, if jenkins inside Docker detached container
  - `docker logs <contanierID>`

Logs on Jenkins GUI
  - Manage Jenkins -> System Log

Monitor Server Load
  - Manage Jenkins -> Load Statistic
    - Displays a graph of server load time for the master node
    - Graph Keeps track of 
      - **Number of online executors**
        `For a computer`: if the computer is online then this is the number of executors 
        that the computer has; if the computer is offline then this is zero.
        `For a label`: this is the sum of all executors across all online computers in this label.
        `For the entire Jenkins`: this is the sum of all executors across all online 
        computers in this Jenkins installation.
        Other than configuration changes, this value can also change when agents go offline.
      - **Number of busy executors**
        This line tracks the number of executors (among the executors counted above) 
        that are carrying out builds. The ratio of this to the number of online 
        executors gives you the resource utilization. If all your executors are busy 
        for a prolonged period of time, consider adding more computers to your Jenkins cluster.
      - **Number of available executors**
        This line tracks the number of executors (among the online executors counted above) 
        that are available to carry out builds. The ratio of this to the total number of executors 
        gives you the resource availability. If none of your executors are available for a prolonged 
        period of time, consider adding more computers to your Jenkins cluster.
      - **Queue length**
        This is the number of jobs that are in the build queue, waiting for an available 
        executor (of this computer, of this label, or in this Jenkins, respectively). This doesn't 
        include jobs that are in the quiet period, nor does it include jobs that are in the queue 
        because earlier builds are still in progress. If this line ever goes above 0, that means 
        your Jenkins will run more builds by adding more computers.

Monitoring
  - **Monitoring**
    - The Monitoring plugin provides monitoring of Jenkins with JavaMelody
    - Charts of CPU, memory, system load average, HTTP response time
    - Details of HTTP sessions, errors and logs, actions for GC, heap dump, invalid session(s).

  - **Disk Usage**
    - The Disk Usage plugin shows project-wide details for all jobs and all workspaces
    - It also displays Disk Usage Trend.

  - **Build Monitor**
    - The Build Monitor Plugin provides a detailed view of the status of selected Jenkins jobs
    - It also shows the names of people who might be responsible for "breaking the build".

Auditing
  - **Audit Trail**
    - Keeps a log of users who performed particular Jenkins operations, such as configuring jobs
    - This plugin adds an Audit Trail section in the main Jenkins configuration page
    - Many configuration options are supported
      - Save output audit logs in rolling files
      - Send audit logs to a Syslog server
      - Output audit logs in `stdout` or `stderr`. Primarily intended for debugging purposes
  - **Job Config History**
    - Saves a copy of the configuration file of a job (config.xml) for each change made and of 
    the system configuration
    - Provides an overview page of all changes
    - The overview page only lists changes made to the system configuration (for performance reasons)
      - Use links to view either all job configuration histories or just the deleted jobs 
        or all kinds of configuration history entries together.

### Global Security Settings

Global Security Settings close off intrusion paths to Jenkins instance
**Access Control for Builds**
  - Similar to access control for users, builds in Jenkins run with an 
  associated user authorization. By default, builds run as the internal 
  SYSTEM user that has full permissions to run on any node, create or delete jobs, 
  start and cancel other builds, etc.
  
  In a Jenkins setup with fine-grained permissions control, this is undesirable. 
  For example, having builds run as SYSTEM could allow users with access to configure 
  and start one job to start builds of any other jobs using Pipeline Build Step Plugin.
  
  The solution to this is to configure access control for builds. The most notable 
  plugin in this space is Authorize Project Plugin, which allows flexible configuration 
  of global and per-project build authorization
  - Project default build authorization:
    - Specify the authorization to use for projects.
  - Per-project configurable build authorization
    - Allows the authentication that a project will run as to be configured 
      from the project configuration page.
      - **Run as specific user**
        - Run a build as a specified user. You are required to "one" of following 
        condition to successfully save the configuration.
          - You are an administrator.
          - You yourself are the specified user.
          - The specified user is not changed from the last configuration,
          and "No need for re-authentication" was checked in the last configuration.
          - You enter the password for the specified user.
      - **Run as user who triggred the build**
        - Run a build as a user who triggered it. If the build was triggered 
        as a downstream build, the build runs as a user who triggered the upstream build.
        This does not work when the build is triggered by SCM polling or scheduled triggering. 
        The build runs as SYSTEM authorization in those cases.
      - **Run as anonymous user**
        - Run a build as an anonymous user.
      - **Run as system**
        - Run as SYSTEM user

**CSRF (Cross Site Request Forgery)**

- is an exploit that enables an unauthorized third party to perform requests
  against a web application by impersonating another, authenticated user. 
- when enabled Jenkins checks for a CSRF token or "crumb" with default crumb issuer.
  In practical terms, this means that each request to the Jenkins API needs to have 
  what is known as a crumb defined in the headers.
- Accessing Jenkins through a poorly configured reverse proxy may result in 
  CSRF HTP header being stripped consider [Jenkins behind a NGINX reverse proxy](https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-with-jenkins/)

**JNLP (Java Network Launch Protocol)**

- can be used to launch an application on a client desktop by using resources that are hosted
  on a remote web server. (ECS jenkins rails instance config)
- Use "agents" field to configure JNLP
  - If you are not using inbound agents, it's recommended that you disable this entirely, 
    which is the default installation behavior. Jenkins uses a TCP port to communicate 
    with agents connected inbound. If you're going to use inbound agents, you can allow 
    the system to randomly select a port at launch 
    (this avoids interfering with other programs, including other Jenkins instances). 
    As it's hard for firewalls to secure a random port, you can instead specify 
    a fixed port number and configure your firewall accordingly.

**Agent-to-Master Access Control**

- filters the commands that agents can send to the Jenkins master
- recommended to keep this protection enabled, especially 
  because of agents can be taken from other teams.
- if builds are failing 
  - upgrade latest version of all plugins
  - whitelist specific commands that you need
  - Add `allow/deny` rules to refine list of files on master, ant type of access on agent

**Markup Formatng**

- Jenkins allows some user input
- HTML formatting could inadvertently inserting unsafe HTML/JS.
  - Could be used for XSS (Cross-site scripting) attacks
- Use Markup Formatter to control how HTML is rendered
- You can choose Safe HTML to allow subset of HTML markup
- Plain Text option:
  - Treats all input as plain text. HTML unsafe characters 
  like `<` and `&` are escaped to their respective character entities.

**Content Security Policy**

- is applied to static files on Jenkins
- This header is set to a very restrictive default set of permissions 
to protect Jenkins users from malicious HTML/JS files in workspaces, 
`/userContent`, or archived artifacts.

- The CSP header sent by Jenkins can be modified by setting 
the system property `hudson.model.DirectoryBrowserSupport.CSP:`

- For example:

```groovy
  System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "default-src 'self'; style-src 'self' 'unsafe-inline';")
 ```


### Going Further
1. [Distributed Builds](https://wiki.jenkins.io/display/jenkins/distributed+builds)
1. [Distributed Builds Architecture](https://www.jenkins.io/doc/book/architecting-for-scale/#distributed-builds-architecture)
1. [So you wanna build the world's largest Jenkins cluster](https://www.cloudbees.com/sites/default/files/2016-jenkins-world-soyouwanttobuildtheworldslargestjenkinscluster_final.pdf)
1. [Jenkins Security - Authorization](https://www.jenkins.io/doc/book/managing/security/#authorization)
1. [LDAP vs. ActiveDirectory](https://www.varonis.com/blog/the-difference-between-active-directory-and-ldap/)
1. [Matrix Based Security](https://wiki.jenkins.io/display/jenkins/matrix-based+security)