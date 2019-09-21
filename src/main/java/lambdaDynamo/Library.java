/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package lambdaDynamo;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;


public class Library {
    private DynamoDB dynamoDb;
    private String DYNAMODB_TABLE_NAME = "task";
    private Regions REGION = Regions.US_WEST_2;

    public boolean logMe(Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("This has been logged: " + context.toString());
        return true;
    }

    public Task save(Task task) {
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);

        Task t = new Task(task.getId(), task.getTitle(),
                task.getDescription(), task.getAssignee(),
                task.getHistoryList(), task.getUrl());

        ddbMapper.save(t);

        return task;
    }

    public List<Task> getAllTasks(){
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);

        List<Task> tasks = ddbMapper.scan(Task.class, new DynamoDBScanExpression());

        return tasks;
    }

    public APIGatewayProxyResponseEvent getUserTasks(APIGatewayProxyRequestEvent event, Context context){
        HashMap<String, AttributeValue> eav = new HashMap<>();
        Map<String,String> map = event.getPathParameters();
        String assignee = map.get("user");
        eav.put(":v1", new AttributeValue().withS(assignee));
        DynamoDBScanExpression scan = new DynamoDBScanExpression()
                .withFilterExpression("(assignee = :v1)")
                .withExpressionAttributeValues(eav);
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();

        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
        List<Task> tasks = ddbMapper.scan(Task.class, scan);

        //we are creating our return here
        APIGatewayProxyResponseEvent res = new APIGatewayProxyResponseEvent();

        //convert to JSON format
        Gson gson = new Gson();
        String json = gson.toJson(tasks);
        //set into the response
        res.setBody(json);

        return res;
    }
    //the response body for a return of res looks like this:
//    [
//    {
//        "id": "bdc4ddcd-70e4-4d4f-b06a-9ebac560616c",
//            "title": "friday afternoon check",
//            "description": "153 pm second flight",
//            "status": "assigned",
//            "assignee": "Jack",
//            "url": "https://cdn.drawception.com/images/panels/2016/2-13/zhcYmxr64r-2.png",
//            "historyList": [
//        {
//            "date": "Fri Sep 13 20:56:57 UTC 2019",
//                "action": "assigned"
//        },
//        {
//            "date": "Fri Sep 13 20:56:57 UTC 2019",
//                "action": "task is assigned to: Jack"
//        }
//    ]
//    }
//]

    public Task deleteTask(Task task){
        final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper ddbMapper = new DynamoDBMapper(ddb);
        Task t = ddbMapper.load(Task.class, task.getId());

        ddbMapper.delete(t);

        return task;
    }


}



