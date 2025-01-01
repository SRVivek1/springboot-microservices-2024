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
      
      # Auto scaling microservice
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
      
      # deploy cluster using yaml definition
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
      
      # download yaml config for current deployment


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
      
      # delete cluster
      gcloud container clusters delete my-standard-cluster-1 --zone us-central1-c 
    
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

## 5. Deploy application using kubernetes [***in progress***]
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

> Note: This is an ***important*** note.

- **<ins>Notes:</ins>**
  - When a deployment is created, kubernetes automatically creates service host property.
    - `<DEPLOYMENT_SERVICE_NAME>_SERVICE_HOST`
  - Some takeaway:
    - Sub topic takeaway.

- **<ins>Pros & Cons</ins>**

| Pros | Cons |
| ---- | ---- |
| Pros 1 | Cons 1 |
| Pros 2 | Cons 2 |

- **<ins>App links:</ins>**
  - [https://hub.docker.com/repository/create?namespace=srvivek](https://hub.docker.com/repository/create?namespace=srvivek)

- **<ins>References:</ins>**
  - [https://kubernetes.io/docs/reference/kubectl/generated/kubectl_set/kubectl_set_env/](https://kubernetes.io/docs/reference/kubectl/generated/kubectl_set/kubectl_set_env/)
  - 

---


