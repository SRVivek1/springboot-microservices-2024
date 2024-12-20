## 2. Property, method param or Return type validation

### Project ref:  a3-sboot-ms-validation
- **<ins>Maven / External dependency</ins>**
  - Add spring validation dependency.
 	```
    	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
- **<ins>Code changes</ins>**
  - imports
    - `import jakarta.validation.Valid;`
  - Annotate the method parameter for validation.
	```
		@PostMapping("/users")
		public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

			// Impacted code goes here.
		}
	```

  - imports
    - `import jakarta.validation.constraints.Past;`
	- `import jakarta.validation.constraints.Size;`
  - Add validation in the properties of the bean.
	```
		public class User {

			// Impacted code goes here.
		}
	```
  - **<ins>Notes:</ins>**
    - Spring internally usages `jakarta-validation` API.
      - `@Valid` annotation:
        - Marks a property, method parameter or method return type for validation cascading.
      - `@Size` annotation
        - Validates property value to match defined size constraints.

  - **<ins>References:</ins>**
    - `https://github.com/springdoc/springdoc-openapi/blob/main/springdoc-openapi-starter-webmvc-ui/pom.xml`
    - `xyz`

---

