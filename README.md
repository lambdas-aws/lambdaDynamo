# lambdaDynamo
Author: Jack Daniel Kinne.
Challenge by: Codefellows.
<!-- Short summary or background information -->
write a lambda to add records when pictures are added to an S3 bucket.

## Challenge
<!-- Description of the challenge -->
- Create a lambda function, in Java, that can add a record to your Taskmaster table.
- Run this only in “Test” mode
- It should receive the same object that your API was handling earlier.
- Repeat for “PUT” / update functionality
- Do NOT handle images at this point.
- You might want to keep this warm…
## Approach & Efficiency
<!-- What approach did you take? Why? What is the Big O space/time for this approach? -->
- copy task and history models from taskmaster backend.
- make a function that takes a task as its passed.  
- save it in the DB. 
- return task.


## credits and contributions
- Matt Stuhring
- Nhu Trinh
- @Bomibear
- Travis Cox
- Peter Lee
- Padmapriya Gannapathi
- Renee Messick
- Jack Kinne