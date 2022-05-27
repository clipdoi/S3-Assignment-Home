# The Friend Management RESTFul Api

### Project Features

* Build Api endpoints for Friend management project
* Write unit test for API endpoints

### Project Requirement
1. Java 8+
2. Maven 3.x.x
3. Install and run PostgreSQL on your localhost for storing data
4. ...

### Test coverage
    Class: %
    Method: %
    Line: %

### How to use from this sample project
##### Clone the repository
```
git clone https://github.com/clipdoi/S3-Assignment-Home.git
```
##### Test api with postman
```
https://www.postman.com/downloads/
```

### RestApi Enpoints

* Create a friend connection between two email addresses: http://localhost:9596/api/emails/add
 ````
  Example Request:
     {
        "friends":[
            "hongson@gmail.com",
            "minhthong@gmail.com"
        ]
     }
  Success Response Example:
    {
      "success": true
    }
  Error Response Example
   {
    "statusCode": 400,
    "message": "Invalid input email format",
    "timestamp": "2022/05/25 13:45:03",
    "description": "uri=/api/emails/add"
   }
   ````
  -------------------------------------------------------------

* Retrieve the friends list for an email address: http://localhost:9596/api/emails/friends
````
 Example Request:
    {
        "email":"hongson@gmail.com"
    }
 Success Response Example:
    {
        "success": true,
        "friends": [
            "minhthong@gmail.com",
            "saomai@gmail.com",
            "nguyenphi@gmail.com"
        ],
        "count": 3
    }
  Error Response Example:
   {
        "statusCode": 400,
        "message": "Invalid input email format",
        "timestamp": "2022/05/25 13:51:54",
        "description": "uri=/api/emails/friends"
   }
 ````
 -------------------------------------------------------------

* Retrieve the common friends list between two email addresses: http://localhost:9596/api/emails/common
 ````
  Example Request:
      {
        "friends":[
            "hongson@gmail.com",
            "minhthong@gmail.com"
        ]
      }
 Success Response Example:
      {
        "success": true,
        "friends": [
            "saomai@gmail.com"
        ],
        "count": 1
      }
 Error Response Example:
      {
        "statusCode": 400,
        "message": "Invalid input email format",
        "timestamp": "2022/05/25 13:55:42",
        "description": "uri=/api/emails/common"
      }
  ````
  -------------------------------------------------------------

* Subscribe to updates from an email address: http://localhost:9596/api/emails/subscribe
 ````
  Example Request:
        {
            "requester":"nguyenphi@gmail.com",
            "target":"nguyenquang@gmail.com"
        }
  Success Response Example:
       {
            "success": true
       }
  Error Response Example:
       {
            "statusCode": 400,
            "message": "Invalid input email format",
            "timestamp": "2022/05/25 14:00:52",
            "description": "uri=/api/emails/subscribe"
       }
  -------------------------------------------------------------
````
* Block updates from an email address(PUT method): http://localhost:9596/api/emails/block
````
  Example Request:
    {   
        "requester":"hongson@gmail.com",
        "target":"nguyenquang@gmail.com"
    } 
  Success Response Example:
   {
        "success": true
   }
  Error Response Example:
   {
        "statusCode": 400,
        "message": "Invalid input email format",
        "timestamp": "2022/05/25 14:04:39",
        "description": "uri=/api/emails/block"
   }
   ````
  -------------------------------------------------------------

* Retrieve all email addresses that can receive updates from an email address: http://localhost:9596/api/emails/retrieve
````
  Example Request:
    {
        "sender": "hongson@gmail.com",
        "text": "Hello World! nguyenvu@gmail.com"
    }
  Success Response Example:
    {
        "success": true,
        "recipients": [
            "nguyenvu@gmail.com",
            "minhthong@gmail.com",
            "saomai@gmail.com",
            "nguyenphi@gmail.com"
        ]
    }
  Error Response Example:
    {
        "statusCode": 400,
        "message": "Invalid input email format",
        "timestamp": "2022/05/25 14:07:29",
        "description": "uri=/api/emails/retrieve"
    }
````