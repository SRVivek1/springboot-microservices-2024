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

## 2. GCP - Google Cloud with Kubernetes
### ***Project ref:*** *e1-kubernetes-deployment-with-yaml*
- **<ins>About / Introduction</ins>**
  - GCP kubernetes engine provides default node pool with 3 nodes.
  - We can add more *Node pools* with different type of hardware specification.
  - **Workloads:**
    - In workloads we can see all runnin services.
    - Each running instance is called **POD**.
- **<ins>GCP K8S project setup</ins>**
  - ***Step-1:*** Create project in console `kubernetes-poc-1`.
  - ***Step-2:*** Enable `Kubernetes API`.
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
    - Services are set of pods with a network endpoint that can be used for discovery and load balancing.
    - Ingress are collection of rules for routing external HTTP(S) trafic to servies.
      - Using ingress we can create one load balacer for multiple services.
  - **Commands:**
    ```sh
      
      # If using gcloud local installation
      # login
      gcloud auth login

      # Connect to gcloud-shell
      # It will generate and store a local ssh file for future logins
      gcloud cloud-shell ssh

      # set project using project id-operating-ally-446306-t7
      # Check project info for 'project-id'
      gcloud config set project operating-ally-446306-t7

      # Fetch cluster auth data and configures cluster
      gcloud container clusters get-credentials my-standard-cluster-1 --zone us-central1-c --project operating-ally-446306-t7
      
      # Deploy microservices
      kubectl create deployment hello-world-rest-api --image=in28min/hello-world-rest-api:0.0.1.RELEASE
      
      # View deployed services
      kubectl get deployment
      
      # Exponse deployed service using load blancer on port 8080
      kubectl expose deployment hello-world-rest-api --type=LoadBalancer --port=8080

      # List running service with details (IP, port, age etc.)
      kubectl get services

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
      
      #
      kubectl get pods -o wide
      
      #
      kubectl set image deployment hello-world-rest-api hello-world-rest-api=in28min/hello-world-rest-api:0.0.2.RELEASE
      
      # list services replicasets & pods
      kubectl get services
      kubectl get replicasets
      kubectl get pods
      
      # delete pod
      kubectl delete pod hello-world-rest-api-58dc9d7fcc-8pv7r
      
      #
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
> Note: This is an ***important*** note.

- **<ins>Notes:</ins>**
  - Some important key point / takeaway note.
  - Some takeaway:
    - Sub topic takeaway.

- **<ins>Pros & Cons</ins>**

| Pros | Cons |
| ---- | ---- |
| Pros 1 | Cons 1 |
| Pros 2 | Cons 2 |

- **<ins>References:</ins>**
  - [https://github.com/springdoc/springdoc-openapi/blob/main/springdoc-openapi-starter-webmvc-ui/pom.xml](https://github.com/springdoc/springdoc-openapi/blob/main/springdoc-openapi-starter-webmvc-ui/pom.xml)
  - [https://cloud.google.com/sdk/gcloud/reference/cloud-shell](https://cloud.google.com/sdk/gcloud/reference/cloud-shell)
  - [https://cloud.google.com/sdk/gcloud/reference/cloud-shell/scp](https://cloud.google.com/sdk/gcloud/reference/cloud-shell/scp)

---




---