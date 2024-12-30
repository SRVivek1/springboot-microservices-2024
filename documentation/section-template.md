## XX. Property, method param or Return type validation
### Project ref: *xx-xxxx-xx-xxxx*
- **<ins>Purpose / Feature</ins>**
  - This is xyz feature.
- **<ins>Steps</ins>**
  - ***Project Setup:*** Some change/step
  - ***Step-1:*** Some change/step
  - ***Step-2:*** Some change/step
- **<ins>Maven / External dependency</ins>**
  - Required dependency.
 	```xml
    	<dependency>
			<groupId>xxx.xxxx.xxxx</groupId>
			<artifactId>xxx-xxxx-xxx-xxxxx</artifactId>
		</dependency>
- **<ins>Code / Config changes</ins>**
  - **Controller:** *AbcController.java*
    - imports
      - `import some.dependent.resource`
    - Annotate the method parameter for validation.
	```java
		@PostMapping("/users")
		public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

			// Impacted code goes here.
		}
	```
  - **Service:** *AbcResource.java*
    - imports
      - `import some.dependent.resource`
    - Annotate the method parameter for validation.
	```java
		public Object createUser(@Valid @RequestBody User user) {

			// Impacted code goes here.
		}
	```
  - **Application Config:** *application.properties*
	```properties
		spring.abc.xyz=false
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

- **<ins>App links:</ins>**
  - [https://hub.docker.com/repository/create?namespace=srvivek](https://hub.docker.com/repository/create?namespace=srvivek)

- **<ins>References:</ins>**
  - [https://github.com/springdoc/springdoc-openapi/blob/main/springdoc-openapi-starter-webmvc-ui/pom.xml](https://github.com/springdoc/springdoc-openapi/blob/main/springdoc-openapi-starter-webmvc-ui/pom.xml)
  - [xyz service](http://website.com/some-resource-path)

---

