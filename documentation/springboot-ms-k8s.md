# Spring boot microservice with Kubernetes
### Kubernetes:
 - Kubernetes, also known as K8s, is an open source system for automating deployment, scaling, and management of containerized applications.
 - It groups containers that make up an application into logical units for easy management and discovery. 
 - Kubernetes builds upon 15 years of experience of running production workloads at Google, combined with best-of-breed ideas and practices from the community.
- **Typical features:**
  - *Auto Scaling:* 
    - Scale containers based on demand.
  - *Auto Discovery:* 
    - Help microservices to find one another.
  - *Load Balancer:* 
    - Distribute load among multiple instances of a microservice.
  - *Self Healing:* 
    - Do health check and replace falling instances.
  - *Zero Downtime Deployment:* 
    - Release new versions without downtime.
- **Docker container orchestration services:**
  - *AWS:*
    - Elastic Container Service (ECS)
    - Fargate:
      - Serverless version of ECS.
  - *Cloud Neutral:*
    - Kubernetes (k8s)
    - Can be used in internal Data centers.
      - Also, supports all cloud platforms.
        - AWS   --> Elastic Kubernetes Services (EKS)
        - GCP   --> Google Kubernets Engine (GKE)
        - Azure --> Azure Kubernetes Services (AKS)
---

## 1. Install Kubernetes locallly {TODO}
### Project ref: *N/A*
- **<ins>Purpose / Feature</ins>**
  - Kubernetes on local machine. 
- **<ins>Steps</ins>**
  - ***Project Setup:*** Install kubernetes locally.
    - [https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/](https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/)
  - ***Step-1:*** Create kubernetes cluster with default node pool.
    - Needs installation of `kubeadm`, `kind` or `minikube` for cluster administration.
      - [https://kubernetes.io/docs/tasks/tools/](https://kubernetes.io/docs/tasks/tools/)
  - ***Step-2:*** TODO


- **<ins>References:</ins>**
  - [https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/](https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/)
  - [https://kubernetes.io/docs/tasks/tools/](https://kubernetes.io/docs/tasks/tools/)

---

## 2. Google Cloud - Introduction to Google Kubernetes Engine (GKE)
- **<ins>About / Introduction</ins>**
  - Provides Kubernetes solution in cloud.
  - **Terms:**
    - **Cluster:** A group of compute engine instances.
      - *Master Node:*
      - *Worker Nodes:*
    - **Master Node:**
      - Also known as `Control plane` as it manages everything inside cluster. E.g. Creating new nodes or creating new deployment for a service etc.
      - `cubectl` commands are execued on `Master Node`.
      - It consists of a set of components.
      - **Components:**
        - *API Server:*
          - Manages all communications for a K8S cluster (from nodes and outside).
          - When any command is executed, it is received by this component.
        - *Scheduler:*
          - Decides nodes for the deployment of a service.
        - *Controller Manager:*
          - Manages deployments and replicasets.
        - *etcd:*
          - A distributed dataset storing the cluster state. This information is used in providing high availability. 
    - **Worker Node:**
      - Runs the work load. All our services (as PODs) run on worker nodes.
      - **Components:**
        - *kubelet:*
          - Manages communication with Master node(s).
    - **Kubernetes Pods:**
      - These are smallest deployable units in kubernetes.
      - A Pod *contains one or more containers*. However most of the Pods contains single container.
      - Each Pod is assigned an *ephemeral (that lasts for short duration)* IP address.
      - All containers in a Pods share:
        - Network
        - Storage
        - IP Address
        - Ports and
        - Volumes (Shared persistence disks)
      - Pods can in any status of the following: **Running / Pending / Succeeded / Failed / Unknown** 
  - **Deployment vs Replica set:**
    - *Deployment:*
      - A deployment is created for each microservice using `kubectl create deployment ....` command.
        - It will create a new replica set for the microservice.
      - A single deployment represents a microservice (with all it's releases).
      - It manage new releases ensuring `0 downtime` using `kubectl set image deployment ....` command.
        - It will create a new replica set version for the new version of microservice.
    - *Replica set:*
      - A ReplicaSet's purpose is to maintain a stable set of replica Pods running at any given time. As such, it is often used to guarantee the availability of a specified number of identical Pods.
        - Ensures that a specific number of Pods are running for a specific microservice version.
        - A Deployment is a higher-level concept that manages ReplicaSets and provides declarative updates to Pods along with a lot of other useful features. Therefore, we recommend using Deployments instead of directly using ReplicaSets, unless you require custom update orchestration or don't require updates at all.
      - Automitically launches new pods if a pod goes down / killed.
    - **Kubernetes Service:**
      - Each time a new Pod is created it will have a new IP address. This could impact the external users accessing Pods with IP address.
      - To ensure that external users are not impacted even though internally IP is changed we expose service using stable service Pod / IP address.
        - `kubectl expose deployment hello-world-rest-api --type=LoadBalancer --port=8080`
      - **Types:**
        - *Cluster IP:*
          - It exposes service on a cluster-internal IP, to access inside cluster (intra cluster communications).
        - *Load Balancer:*
          - It exposes service externally using a cloud provider's load balancer.
          - Check in `GCP load balancers` for all load balancers created on platform.
          - This approach work on all cloud provider kubernetes engine/service.
            - *Not recommended:* We can also create load balancer for each micrservice.
        - *Node port:*
          - It exposes service on each node's ip at a static port (the NodePort).
          - *Use case:*
            - When we don't want to create an exernal load balancer for each micro-service. We can create one ingress component to load balance multiple microserices by routing.
            - Using ingress we can expose Pods NodePort to expose externally to outside world.
---
## 3. GCP - Google Cloud with Kubernetes [Hands-on]
### ***Project ref:*** *e1-kubernetes-deployment-with-yaml*
- **<ins>About / Introduction</ins>**
  - GCP kubernetes engine provides default node pool with 3 nodes.
  - We can add more *Node pools* with different type of hardware specification.
  - **Workloads:**
    - In workloads we can see all runnin services.
    - Each running instance is called **POD**.
- **<ins>GCP K8S project setup</ins>**
  - ***Step-1:*** Create project in console `kubernetes-poc-1`.
  - ***Step-2:*** Enable `Kubernetes API` in GCP platform (pop-up).
  - ***Step-3:*** Create cluster and choose `Standard`.
    - With Autopilot, GKE provisions and manages your cluster's underlying infrastructure, including nodes and node pools, giving you an optimized cluster with a hands-off experience. 
    - If you need more control over your cluster configuration, select "Switch to Standard cluster."
    - Cluster Modes:
      - Standard:
      - Autopilot:
  - ***Step-4:*** Fetch cluster auth data and configures cluster.
    - `gcloud container clusters get-credentials my-standard-cluster-1 --zone us-central1-c --project operating-ally-446306-t7`
  - ***Step-5:*** Deploy microservices
    - `kubectl create deployment hello-world-rest-api --image=in28min/hello-world-rest-api:0.0.1.RELEASE`
  - ***Step-6:*** View deployed services
    - `kubectl get deployment`
  - ***Step-7:*** Exponse deployed service using load blancer on port 8080
    - `kubectl expose deployment hello-world-rest-api --type=LoadBalancer --port=8080`
  - ***Step-8:*** List running service with details (IP, port, age etc.)
    - `kubectl get services`
  - ***Step-9:*** watch progress
    - `kubectl get services --watch`
  - ***Step-10:*** Hit service
    - `curl 35.184.204.214:8080/hello-world`
  - **Enable google cloud shell:**
    - `https://shell.cloud.google.com/?pli=1&show=ide&environment_deployment=ide`
  - **Note:**
    - **Services** are set of pods with a network endpoint that can be used for discovery and load balancing.
    - **Ingress** are collection of rules for routing external HTTP(S) trafic to servies.
      - Using ingress we can create one load balacer for multiple services.
      - It can route requests to load balancers and Node ports.
  - **Commands:**
    ```sh
      
      # initalize the sdk
      # Requires login to GCP account.
      gcloud init

      # If using gcloud local installation
      # login to GCP account
      gcloud auth login

      # Connect to gcloud-shell
      # It will generate and store a local ssh file for future logins
      gcloud cloud-shell ssh

      # Automatic authentication with GCP CLI tools in Cloud Shell is disabled. To enable, rerun above command with `--authorize-session` flag.
      gcloud cloud-shell --authorize-session ssh

      # set project using project id-operating-ally-446306-t7
      # Check project info for 'project-id'
      gcloud config set project operating-ally-446306-t7

      # Fetch cluster auth data and configures cluster
      gcloud container clusters get-credentials my-standard-cluster-1 --zone us-central1-c --project operating-ally-446306-t7
      
      # Deploy microservices
      kubectl create deployment hello-world-rest-api --image=in28min/hello-world-rest-api:0.0.1.RELEASE
      
      # View deployed services
      kubectl get deployment
      
      # View deployment info for specific deployment
      kubectl get deployment currency-exchange-service

      # view the deployment config in yaml format
      kubectl get deployment currency-exchange-service -o yaml

      # Exponse deployed service using load blancer (stable service IP) on port 8080
      kubectl expose deployment hello-world-rest-api --type=LoadBalancer --port=8080

      # List running service with details (IP, port, age etc.)
      kubectl get services

      # View service info for specific deployment
      kubectl get services currency-exchange-service

      # view the deployment config in yaml format
      kubectl get services currency-exchange-service -o yaml

      #watch progress
      kubectl get services --watch
      
      # hit service
      curl 35.184.204.214:8080/hello-world
      
      # get list of events for the cluster
      kubectl get events

      # Scale the service deployement to 3 PODs
      kubectl scale deployment hello-world-rest-api --replicas=3
      
      # List all the PODs
      kubectl get pod

      # Mnual scaling
      # Default cluster pool has only 3 nodes.
      # If we need more pods then we need to scale-up our cluster
      gcloud container clusters resize my-standard-cluster-1 --node-pool default-pool --num-nodes=2 --zone=us-central1-c
      
      # Horizontal Auto scaling microservice
      # It can scale only upto number of nodes present in cluster.
      kubectl autoscale deployment hello-world-rest-api --max=4 --cpu-percent=70

      # Details of HPA - Horizontal pod autoscaling
      kubectl get hpa
      
      # cluster auto scaling
      gcloud container cluster update my-standard-cluster-1 --enable-autoscaling --min-nodes=1 --max-nodes=10

      # Config Map
      # Configuration to database or something like that
      kubectl create configmap hello-world-config --from-literal=RDS_DB_NAME=todos
      
      # list configs present
      kubectl get configmap

      # Show data in config map
      kubectl describe configmap hello-world-config
      
      # Store secrets / password in config map
      # Secret is stores as 'Opaque' type
      kubectl create secret generic hello-world-secrets-1 --from-literal=RDS_PASSWORD=dummytodos
      kubectl get secret
      kubectl describe secret hello-world-secrets-1
      
      # Update/deploy cluster using yaml definition
      kubectl apply -f deployment.yaml
      
      # Create new node-pool
      gcloud container node-pools create my-new-pool --cluster=my-standard-cluster-1 --zone=us-central1-c

      # Delete nodes from node-pool
      gcloud container node-pools delete my-new-pool --cluster=my-standard-cluster-1 --zone=us-central1-c

      # list node pools details (name, disk-size, machine-type, node-version)
      gcloud container node-pools list --zone=us-central1-c --cluster=my-standard-cluster-1
      
      # get detailed status of pods - Name, status, containers, restart, IP, age etc.
      kubectl get pods -o wide
      
      # Update / Set a new image for running deployement / microservice
      # This will automatically deploy the new image with '0 downtime'.
      kubectl set image deployment hello-world-rest-api hello-world-rest-api=in28min/hello-world-rest-api:0.0.2.RELEASE
      
      # list services replicasets & pods
      kubectl get services
      kubectl get replicasets
      kubectl get pods
      
      # delete pod
      kubectl delete pod hello-world-rest-api-58dc9d7fcc-8pv7r
      
      # Set desired cound of Pods in the replica-set
      kubectl scale deployment hello-world-rest-api --replicas=1
      
      # List projects
      gcloud projects list
      
      # delete ervice
      kubectl delete service hello-world-rest-api
      
      # delete deployment
      kubectl delete deployment hello-world-rest-api
      
      # Delete all services related to a service - pods, service replicasets & deployment.apps
      kubectl delete all -l app=currency-exchange-service

      # delete cluster
      gcloud container clusters delete my-standard-cluster-1 --zone us-central1-c 

      # check logs of a POD
      kubectl logs currency-exchange-service-5bf8dd7984-p7tmm

      # follow / tail logs of a POD
      kubectl logs -f currency-exchange-service-5bf8dd7984-p7tmm
    
    ``` 
- **<ins>References:</ins>**
  - [https://github.com/springdoc/springdoc-openapi/blob/main/springdoc-openapi-starter-webmvc-ui/pom.xml](https://github.com/springdoc/springdoc-openapi/blob/main/springdoc-openapi-starter-webmvc-ui/pom.xml)
  - [https://cloud.google.com/sdk/gcloud/reference/cloud-shell](https://cloud.google.com/sdk/gcloud/reference/cloud-shell)
  - [https://cloud.google.com/sdk/gcloud/reference/cloud-shell/scp](https://cloud.google.com/sdk/gcloud/reference/cloud-shell/scp)

---

## 4. GCP - Install gcloud locally
- **<ins>About / Introduction</ins>**
  - This is abc feature.
  - This is xyz feature.
- **<ins>Steps</ins>**
  - ***Project Setup:*** Requires **kubectl**.
    - Follow `Section 1` for instructions.
  - ***Step-1:*** Download ***Google Cloud SDK*** and follow instructions on website to instal it.
  - ***Step-2:*** Download ***kubectl*** executable and follow instructions on website to install it.
  - ***Step-3:*** Install `gke-gcloud-auth-plugin` required to use `kubectl` with `gcloud`.
    - `gcloud components install gke-gcloud-auth-plugin`
  - ***Step-4:*** Initialize gcloud `gcloud init` to initialize the sdk.
  - ***Step-5:*** Authorize with GCP account. `gcloud auth login`.

> Note: Install the SDK where user level access are granted. Don't use root user / sudo.

- **<ins>References:</ins>**
  - [https://cloud.google.com/sdk/docs/install](https://cloud.google.com/sdk/docs/install)
  - [https://cloud.google.com/sdk/docs/uninstall-cloud-sdk](https://cloud.google.com/sdk/docs/uninstall-cloud-sdk)

---

## 5. Deploy application using kubernetes 
### Project ref: 
  - *e2-currency-exchange-service-kubernetes* & 
  - *e3-currency-conversion-service-openfeign-kubernetes*
- **<ins>Purpose / Feature</ins>**
  - Deploy microservices in GKE from local using `gcloud CLI` and `kubectl` commands.
- **<ins>Steps</ins>**
  - ***Project Setup:*** POM.xml: Remove following dependencies as `kubernetes` provide inbuild support for these funcationalities.
    - Eureka client
    - config client
  - ***Step-1:*** *Feign proxy:* Update feign client to use url property to define service URL.
    - `@FeignClient(name = "E2-CURRENCY-EXCHANGE-SERVICE-KUBERNETES", url = "${CURRENCY_EXCHANGE_SERVICE_HOST:http://localhost}:8000")`
  - ***Step-2:*** *application.properties* Add management properties to enable monitoring.
  - ***Step-3:*** gcloud setup
    - `gcloud auth login`
    - `gcloud container clusters get-credentials my-standard-cluster-1 --zone us-central1-c --project operating-ally-446306-t7`
  - Cretae deployment(Deploy services) using following command
    - `kubectl create deployment currency-exchange-service --image=srvivek/e2-currency-exchange-service-kubernetes:0.0.11-SNAPSHOT`
    - `kubectl create deployment currency-conversion-service --image=srvivek/e3-currency-conversion-service-openfeign-kubernetes:0.0.11-SNAPSHOT`
  - ***Step-4:*** Set currency exchange host name using env. prop. to be used in feign.
    - `kubectl set env deployment/currency-conversion-service CURRENCY_EXCHANGE_SERVICE_HOST=currency-exchange-service` 
      - **Imp.:** Check notes for default host property available in kubernetes.
  - ***Step-5:*** List env set for all pods
    - `kubectl set env pods --all --list`
  - ***Step-6:*** Exponse deployed service using load blancer service.
    - `kubectl expose deployment currency-exchange-service --type=LoadBalancer --port=8000`
    - `kubectl expose deployment currency-conversion-service --type=LoadBalancer --port=8100`
  - ***Step-7:*** Lets download the deployment and service config in yaml
    - **Deployment** `kubectl get deployment currency-exchange-service -o yaml >> deployment.yaml`
    - **Service:** `kubectl get services currency-exchange-service -o yaml >> services.yaml`
  - ***Step-8:*** We can change the yaml config as per requirement and deploy to upadate it on GKE.
    - **Difference:** `kubectl diff -f deployment.yaml`
    - **Deploy:** `kubectl apply -f deployment.yaml`
  - ***Step-9:*** Verify pods and run test to verify load balancer
    - `watch -n 0.1 curl http://34.44.230.142:8100/currency-conversion-feign/from/UsD/to/iNr/quantity/100`
  - **Step-10:** Cleaning generated config yaml.
    - check `deployment.yaml` code in below section.
- **<ins>Code / Config changes</ins>**
  - **Feign proxy:** *CurrencyExchangeProxy.java*
    - imports
      - `import org.springframework.cloud.openfeign.FeignClient;`
    - Annotate the method parameter for validation.
	```java
      @FeignClient(name = "E2-CURRENCY-EXCHANGE-SERVICE-KUBERNETES", url = "${CURRENCY_EXCHANGE_SERVICE_HOST:http://localhost}:8000")
      public interface CurrencyExchangeProxy {

        /**
        * Method as defined in the host service.
        * @param from
        * @param to
        * @return
        */
        @GetMapping("/jpa/currency-exchange/from/{from}/to/{to}")
        public CurrencyConversion retrieveExchangeRateFromDatabase(@PathVariable String from, @PathVariable String to);

      }
	```
  - **Application Config:** *application.properties*
	```properties
      spring.application.name=e3-currency-conversion-service-ofeign-kubernetes
      server.port=8100

      # Start: CHANGE-KUBERNETES
      management.endpoint.health.probes.enabled=true
      management.health.livenessstate.enabled=true
      management.health.readinessstate.enabled=true
      # End: CHANGE-KUBERNETES

      # Start: Spring Load Balancer
      # Enable load balancer.
      spring.cloud.loadbalancer.enabled=true

      # Enables LoadBalancer retries.
      spring.cloud.loadbalancer.retry.enabled=true
      # End: Spring Load Balancer
	```
  - **Kubernetes declarative deployment:** *deployment.yaml*
	```yaml
      apiVersion: apps/v1
      kind: Deployment
      metadata:
        annotations:
          deployment.kubernetes.io/revision: "1"
        labels:
          app: currency-exchange-service
        name: currency-exchange-service
        namespace: default
      spec:
        replicas: 2 # 2 instances should be available
        selector:
          matchLabels:
            app: currency-exchange-service
        strategy:
          rollingUpdate:
            maxSurge: 25%
            maxUnavailable: 25%
          type: RollingUpdate
        template:
          metadata:
            labels:
              app: currency-exchange-service
          spec:
            containers:
            - image: srvivek/e2-currency-exchange-service-kubernetes:0.0.11-SNAPSHOT
              imagePullPolicy: IfNotPresent
              name: e2-currency-exchange-service-kubernetes
            restartPolicy: Always
      ---
      apiVersion: v1
      kind: Service
      metadata:
        annotations:
          cloud.google.com/neg: '{"ingress":true}'
        labels:
          app: currency-exchange-service
        name: currency-exchange-service
        namespace: default
      spec:
        allocateLoadBalancerNodePorts: true
        ports:
        - port: 8000
          protocol: TCP
          targetPort: 8000
        selector:
          app: currency-exchange-service
        sessionAffinity: None
        type: LoadBalancer
	```
- **<ins>Notes:</ins>**
  - When a deployment is created, kubernetes automatically creates service host property.
    - `<DEPLOYMENT_SERVICE_NAME>_SERVICE_HOST`

- **<ins>App links:</ins>**
  - [https://hub.docker.com/repository/create?namespace=srvivek](https://hub.docker.com/repository/create?namespace=srvivek)

- **<ins>References:</ins>**
  - [https://kubernetes.io/docs/reference/kubectl/generated/kubectl_set/kubectl_set_env/](https://kubernetes.io/docs/reference/kubectl/generated/kubectl_set/kubectl_set_env/)
  - 

---
## 6. Google Cloud: Logging, Tracing and Monitoring
- **<ins>About / Introduction</ins>**
  - Centerlized logs for the all services/containers/pods etc.
  - Centerlized monitoring for all services/containers/pods etc.
- **<ins>Steps</ins>**
  - ***Project Setup:*** Ensure micrometer dependencies are available in latest image.
  - ***Step-1:*** Enable logging and tracing in GCP.
    - Navigate to API and Services in GKE, goto `API Library`.
    - Search and open below services.
      - `cloud logging API`
      - `Stackdriver API`
    - If not enabled, Click on `Manage` and 
  - ***Step-2:*** Fire requests (10/sec) to generate logs in system.
    - `watch -n 0.1 curl http://34.42.22.155:8100/currency-conversion-feign/from/UsD/to/iNr/quantity/100`
  - ***Step-3:*** Now go to cluster in GCP, open the cluster link.
    - Go below to `Features section` and click on:
      - `View logs` - To check logs.
        - By default logs filter is set on `cluster`, clear this.
        - Click on `Kubernetes containers` in filter and select desired `pod`.
          - You also select different options from filter, e.g. - cluster, service, container etc.
      - `View GKE Dashboard` - For Cloud monitoring.
  - ***Logs Explorer:***
    - We can add conditions in the query panel to filter the logs.
    - We can also use id's from log entries to find all logs to that id from query panel.
      - E.g. `resource.type="k8s_container" textPayload:"a0f973a78782bd1ec580c444fda324c1"`
  - ***GKE Dashboard:***
    - All matrics for sytem monitoring and resource utilization for cluster, namespaces, Nodes, workloads, services, pods, containers etc.
      - eg. CPU, Memory & Disk Utilization.
      - Containers restart, error logs etc.
    - Provides alert service.
> Note: Ensure following dependencies are present in POM.
  - implementation platform('io.micrometer:micrometer-tracing-bom:latest.release')
  - implementation 'io.micrometer:micrometer-tracing'

- **<ins>References:</ins>**
  - [https://micrometer.io/docs/tracing](https://micrometer.io/docs/tracing)

---
## 7. Google Cloud: GKE ConfigMap - Centralized Configuration
- **<ins>About / Introduction</ins>**
  - Provides centerlize configuration.
  - A ConfigMap is an API object used to store non-confidential data in key-value pairs. 
  - Pods can consume ConfigMaps as environment variables, command-line arguments, or as configuration files in a volume.
    - **Caution:**
      - ConfigMap does not provide secrecy or encryption. If the data you want to store are confidential, use a Secret rather than a ConfigMap, or use additional (third party) tools to keep your data private.
      - The spec of a static Pod cannot refer to a ConfigMap or any other API objects.
    - **Note:** 
      - A ConfigMap is not designed to hold large chunks of data. The data stored in a ConfigMap cannot exceed 1 MiB. 
      - If you need to store settings that are larger than this limit, you may want to consider mounting a volume or use a separate database or file service.
  - A ConfigMap allows you to decouple environment-specific configuration from your container images, so that your applications are easily portable.
  - A ConfigMap has data and binaryData fields. These fields accept key-value pairs as their values.
    - Both the data field and the binaryData are optional. 
    - The data field is designed to contain UTF-8 strings while the binaryData field is designed to contain binary data as base64-encoded strings.
  - The name of a ConfigMap must be a valid `DNS subdomain name`.
- **<ins>Steps</ins>**
  - ***Project Setup:*** GKE has microservices running.
  - ***Step-1:*** Create a configMap.
    - `kubectl create configmap currency-conversion-service-openfeign --from-literal=CURRENCY_EXCHANGE_URI=http://currency-exchange-service`
  - ***Step-2:*** Verify / list the config map
    - `kubectl get configmap currency-conversion-service-openfeign`
    - `kubectl get configmap currency-conversion-service-openfeign -o yaml`
  - ***Step-3:*** We can add the config map content in our `deloyment.yaml` file.
    - Add `envFrom` declaration in `containers` to read data from `configmap`.
    - Remove `env` declaration from `containers`.
    - **New File:** *deployment-0-3-with-configmap.yaml*
  - ***Step-4:*** Apply the new configuration.
    - **Difference:**
      - `kubectl diff -f deployment-0-3-with-configmap.yaml`
    - **Apply:**
      - `kubectl apply -f deployment-0-3-with-configmap.yaml`

> Note: ***Must*** veriy the difference before applying the update yaml file. I also helps to validate yaml by throwing error, if there's any syntactical mistakes.

- **<ins>References:</ins>**
  - [https://kubernetes.io/docs/concepts/configuration/configmap/](https://kubernetes.io/docs/concepts/configuration/configmap/)
  - 
---

## 8. Kubernetes : Exploring deployments - Liveness and Readiness probes
- **<ins>About / Introduction</ins>**
  - **Liveness Probes:**
    - The kubelet uses liveness probes to check health of a microservice and/or to restart POD if liveness probe is not successful. 
      - For example, liveness probes could catch a deadlock, where an application is running, but unable to make progress. Restarting a container in such a state can help to make the application more available despite bugs.
    - A common pattern for liveness probes is to use the same low-cost HTTP endpoint as for readiness probes, but with a higher failureThreshold. This ensures that the pod is observed as not-ready for some period of time before it is hard killed.
    - **Caution:**
      - Liveness probes can be a powerful way to recover from application failures, but they should be used with caution. 
      - Liveness probes must be configured carefully to ensure that they truly indicate unrecoverable application failure, for example a deadlock.
    - **Note:**
      - Incorrect implementation of liveness probes can lead to cascading failures. This results in restarting of container under high load; failed client requests as your application became less scalable; and increased workload on remaining pods due to some failed pods. 
      - Understand the difference between readiness and liveness probes and when to apply them for your app.
  - **Readiness Probes:**
    - The kubelet uses readiness probes to know when a container is ready to start accepting traffic. One use of this signal is to control which Pods are used as backends for Services. 
      - A Pod is considered ready when its Ready condition is true. When a Pod is not ready, it is removed from Service load balancers. A Pod's Ready condition is false when its Node's Ready condition is not true, when one of the Pod's readinessGates is false, or when at least one of its containers is not ready.
    - ***If `Readiness probe` is not succesfull. no traffic is sent.***
  - **Startup Probes:**
    - The kubelet uses startup probes to know when a container application has started. If such a probe is configured, liveness and readiness probes do not start until it succeeds, making sure those probes don't interfere with the application startup. 
      - This can be used to adopt liveness checks on slow starting containers, avoiding them getting killed by the kubelet before they are up and running.
  - **Springboot Actuator:** Springboot Actuator (>=2.3) provides inbuild readiness and liveness probes.
    - `actuator/health/readiness`
    - `actuator/health/liveness`
  - 
- **<ins>Steps</ins>**
  - ***Project Setup:*** Microservices running in GKE.
  - ***Step-1:*** Get rollout history/revisions of specific deployment.
    - `kubectl rollout history deployment currency-conversion-service-openfeign`
  - ***Step-2:*** In case of any error occurs during deployment. We can always rollback to old working revision.
    - `kubectl rollout undo deployment currency-conversion-service-openfeign --to-revision=2`

- **<ins>deployment.yaml</ins>**
  - Liveness and Readiness probes updated in deployment decaration.
  ```yaml
        # ... other config
        template:
          metadata:
            labels:
              app: currency-exchange-service
          spec:
            containers:
            - image: srvivek/e2-currency-exchange-service-kubernetes:0.0.11-SNAPSHOT
              imagePullPolicy: IfNotPresent
              name: e2-currency-exchange-service-kubernetes
              readinessProbe:
                httpGet:
                  port: 8000
                  path: /actuator/health/readiness
              livenessProbe:
                httpGet:
                  port: 8000
                  path: /actuator/health/liveness
            restartPolicy: Always
        # ... other config
  ```
> Note: Readiness and Liveness probes help to increase high availability of our application.

- **<ins>References:</ins>**
  - [https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/)

---

## 9. Kubernetes: Horizontal Autoscaling microservices
- **<ins>About / Introduction</ins>**
  - **Types:**
    - *Horizonal*
  - **Horizontal:** 
    - In Kubernetes, a *HorizontalPodAutoscaler* automatically updates a workload resource (such as a Deployment or StatefulSet), with the aim of automatically scaling the workload to match demand.
    - Horizontal scaling means that the response to increased load is to deploy more Pods. 
      - This is different from vertical scaling, which for Kubernetes would mean assigning more resources (for example: memory or CPU) to the Pods that are already running for the workload.
    - If the load decreases, and the number of Pods is above the configured minimum, the HorizontalPodAutoscaler instructs the workload resource (the Deployment, StatefulSet, or other similar resource) to scale back down.
- **<ins>Steps</ins>**
  - ***Project Setup:*** Microservices running kubernetes cluster.
  - ***Step-1:*** Manual autoscaling
    - `kubectl autoscale deployment currency-conversion-service-openfeign --min=1 --max=3 --cpu-percent=5`
  - ***Step-2:*** list horizonal autoscaling
    - `kubectl get hpa`
    - `kubectl get hpa -o yaml`
  - ***Step-3:*** See the CPU / Memory utilization.
    - `kubectl top pods`
    - `kubectl top nodes`
  - ***Step-4:*** Verify pods by increasing traffice on load.

- **<ins>References:</ins>**
  - [https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale/](https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale/)
  - [https://kubernetes.io/docs/concepts/workloads/autoscaling/](https://kubernetes.io/docs/concepts/workloads/autoscaling/)
  - [https://kubernetes.io/docs/reference/kubectl/generated/kubectl_autoscale/](https://kubernetes.io/docs/reference/kubectl/generated/kubectl_autoscale/)
---

