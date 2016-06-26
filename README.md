# goraydar-test

This project for Backend/Search Engineer Entrance test.
I use Java as main programming language for this project.

## How to run

- Use ```download-solr.sh``` to download solr and setup for solrcloud.
- Start solr by using ```run-solr.sh```. Solr must be started first for crawler and service.
- To build crawler use ```crawler/build.sh```
- To run crawler use ```crawler/run.sh```
- To build service use ```service/build.sh```
- To run service use ```service/run.sh```

## Crawler

I prefer Nutch, it can make crawler more scalable. But for the limited time of the test I will use Crawler4j.

## Web application

Jetty + Servlet will be used as web application.

**Search API**
```
GET localhost:8080/search
```

| Params | Meaning |
|---|---|
| q | User query |

Example
> GET localhost:8080/search?q=game

Result
```json
{
    // Time run query in miliseconds
    "QTime" : 258,
    // Total hits for paging
     "numHits" : 1000,
     "docs" : [
        //Matched docs
     ]
}
```

**Like API**
```
GET localhost:8080/search
```

| Params | Meaning |
|---|---|
| user | Current user |
| docid | Liked docid |

Example
> GET localhost:8080/like?user=datcm&docid=6ec6a57b-981d-4171-b1fb-27e9088e97e6

Result : 200

**Recommendation API**
> GET localhost:8080/rec

| Params | Meaning |
|---|---|
| user | Current user |
| q | Current query |

Example
> GET localhost:8080/rec?user=datcm&q=game

Result
```json
{
    // Time run query in miliseconds
    "QTime" : 258,
    // Total hits for paging
     "numHits" : 1000,
     "docs" : [
        //Matched docs
     ]
}
```


## Recommendation

I will use the lastest feature (streaming expression) of Solr to make recommendation based on collaborative featuring.
For example we have this likes log

| Query | User | Liked docid |
|---|---|---|
|game | datcm | 6ec6a57b-981d-4171-b1fb-27e9088e97e6 |
|game | datcm | f394cdfb-f8e6-49f6-8e7e-a3c394547e6b |
|game | keith | 6ec6a57b-981d-4171-b1fb-27e9088e97e6 |
|game | keith | f394cdfb-f8e6-49f6-8e7e-a3c394547e6b |
|game | keith | e2274473-402f-4886-9d7f-f84be19abe5d |
|game | keith | f795b7cb-4901-4da3-8022-9d635a838113 |
|game | khang | 6ec6a57b-981d-4171-b1fb-27e9088e97e6 |
|game | khang | f394cdfb-f8e6-49f6-8e7e-a3c394547e6b |
|game | khang | e2274473-402f-4886-9d7f-f84be19abe5d |
|love story | keith | 94b3d8eb-9867-4f3a-9ceb-647e87b6174f |
|love story | khang | 94b3d8eb-9867-4f3a-9ceb-647e87b6174f |

If user ```datcm``` wanna recommendations for ```game``` query.
- System will find out that keith and khang will be the most relevant user (because they also like 6ec6a57b, f394cdfb)
- For these users e2274473 and f795b7cb is liked photos. We not take account for 94b3d8eb because it's not the match the query
- So system will recommend e2274473 and f795b7cb ( e2274473 in first because it's liked by two users)

Like above exam, If user ```datcm``` wanna recommendations for ```love story``` query. System will recommend 94b3d8eb doc.

**Notice that all the computation is computed in parallel**
