# itss-batch-ingestion
This repository is for batch processing and ingestions

## Project description:

**itss-batch-ingestion** project is design for data migration process (contacts, transfers, products etc.).

The project uses at its core spring-batch framework that reads raw data from a **.csv** file, validates it, maps it to a request body and makes a call to an **integration-api** service (as of now to Contacts Bulk Import Api). You can check the status of a run job(s) and also at the end of the job(s) execution, a report is being generated.

> **Note_1:**  
> As of now we are able to import **Contacts** only  

> **Note_2:**   
> The bulk information that needs to be migrated is stored in **.csv** files that are located under /resources/inputFiles. The values inside the **.csv** file needs to have a specific order (that was agreed with the team) in order to be properly processed. (As of now) For Contacts have been used only for 4 fields (as a POC) that are required to create an actual contact, but note that this objects can be bigger and complex.
> >**ToBe Developed** - for the future under **resources/inputFiles** folder probably it would be a good idea to have folders per job, which will hold all **.csv** files related to that particular job.

> **Note_3:**   
> All classes containing ***Template*** at the end of their name are just an example to be used for future jobs development.

> **Note_4:**   
> The processor packages holds the job process step. As of now we don’t do any processing in this step (but will be useful for future jobs).

<br><br>

## Pre-requisites 
- Java 11 JDK
- Maven (with Backbase repo credentials set up properly)
- IDE (IntelliJ preferably and recommended)
- Postman Client (requests signature is available at the end of this document) 

<br><br>

## Extra plug-ins and dependencies:
**1. Liquibase integration** – used for DB management  
**2. Swagger Doc** – used for API doc  
**3. Sonar** – used for code bug statistics   
**4. Mapstruct**  - used for mapping objects  
**5. Lombok** – used for annotation generated getters/setters/constructors etc.

<br><br>

## Running the App Locally:
This is a normal spring-boot application, there are no custom configurations to be made in order to run the application.
- Make sure you have all the pre-requisites set up. 
- Before running the application you need to change some configurations in application.yml:
  - Database (url, username, password) – to your local configuration;
  - Server Port (in case 9999 is used on your machine);
  - Change the **integration-api** endpoint 
  
    >**Note:**   
    In case you have a Backbase **business-banking-app** project that you run locally just give your url. Otherwise, you can use the DEV endpoint – only take into account that you will use the DEV DB in this case – which means in the **.csv** file you need to provide a valid DEV **externalUserId** value and also beware that you will populate DEV DB with junk data. 

  - If you are running a Windows machine you need to make sure to change all path slashed from **forward slash [ / ]** to **backward slash [ \\ ]** 
    - input; 
    - report > path; 
     - liquibase > change-log;

To start up the application  
- run: mvn clean install 
- open: SpringBatchApplication (main) class and click on the play button

<br><br>

## Running the App DEV Environment:

The endpoint for **itss-batch-ingestion** is not exposed for the public, therefore you need to access DEV Kubernetes container and obtain a list of PODs by running:  

`kubectl get pods --namespace=backbase`

1. In the list of PODs you need to find the service name you need, in this case: `batch-ingestion-batch-ingestion-7ddbf5cd7f-7vzj4`
> **Note:** Each redeploy of the POD will append a random hash at the of the POD's name **{service-name-7ddbf5cd7f-7vzj4}**

2. Now in order to be able to run requests to this service on DEV, you need to do a port-forward for this service by running:  

**Template >** `kubectl port-forward -n {namespace} {service-name} {your_local_port}:{port_on_which_service_is_running_on_DEV}`  
**Example >** `kubectl port-forward -n backbase batch-ingestion-batch-ingestion-7ddbf5cd7f-7vzj4 9999:8080`

Now you can run request to DEV via `http://localhost:9999`

**Example >** `http://localhost:9999/api/fullIngestionJob`

<br><br>

## Project features:
### 1. Ingestion Process  

The ingestion process can be run via **/api/fullIngestionJob** endpoint with the giving body

```
{
    "externalUserId": "2c93b0817f97340c017f973b9a470000",  
    "serviceAgreementId": "2c93b0817f97340c017f973b9a470000",   
    "skipJobs": {  
        "skipContacts": false,  
        "skipTransfers": true,  
        "skipSavings": true  
    }  
}
```

Where **externalUserId** and **serviceAgreementId** – as of now the values are not used anywhere, therefor they are not being validated. 
> **ToBe Developed:**   
> This is a feature needs to be developed in case we don’t have **externalUserId** or **serviceAgreementId** in the **.csv** file – we need a backup solution to be able to send this values via request body. This feature needs to contain a flag set in application.yml that will allow switching to read the data either from **.csv** file or via request body.

**skipJobs** – can hold a list of all available jobs 
> **Note:**
>* **ContactsJob** is the only functional job (as of now)
>* **TransfersJob** was in course of development and it was not finished till the end ==ToDo: Finish the development of this job==
>* **SavingJob** is just a dummy job (for the purpose of POC)

By default, any job flag is set to **false**, which means if you run the ingestion (migration) process, all available jobs will be run. This feature was developed in case you don’t want to run particular jobs, then you set the flag to **true**.   
>In this case only the **Contacts Job** will be run since the flag is set to **false**;   
> **Transfers Job** and **“Savings Job”** will be skipped since the flag is set to **true**.

<br><br>

### 2. Job Status (master)

The master job status can be run via **/api/status** endpoint with the giving body

```
{
    "fullIngestionJobId": "{{fullIngestionJobId}}"
}
```

Where **fullIngestionJobId** is the value that you receive on response after running the ingestion endpoint. This feature offers the possibility to check the status for all available jobs that where run – this feature is useful for large set of data.

<br><br>

### 3. Job Status (child)

The child job status can be run via **/api/status** endpoint with the giving body

```
{
    "contactsJobId": "{{contactsJobId}}"
}
```

Where **contactsJobId** is the value that you receive on response after running the ingestion endpoint. This feature offers the possibility to check the status for a particular job (in this case **Contacts**) – this feature is useful when you want to check a certain job current status.

<br><br>

### 4. Report Generation

Report generation is a feature that creates a report after the ingestion process is run. A reports folder is being created under **resources** folder `(resources/reports )` of the project that consists of 3 parts. 
1. a general **.txt** report file is created that tells you the nr. of successful executions, fail/skipped executions. 
2. a folder is created with the job name (as of now **contacts**) under **reports** folder `(resources/reports/contacts)` – there are stored all **.txt** report files which offers more detailed information about the job that was run for a particular user, how many lines were processed successfully and how many failed or got skipped. 
3. a folder is created under job name folder (in this case **contacts**), the folder is called **errors** `(resources/reports/contacts/errors)` that holds a **.txt** file report where you can see for a particular user why certain line got skipped (failed – which means they didn’t passed the validation process) – you will see the request object and issue with that object or request.

The format of each report looks like this: ==**result_ExternalUserId_Timestamp.txt**==

> **Note:**   
> The report generation folder is not available on DEV environment due to the lack of storage solution. 
> >***This issue needs to be discussed and a solution must be found ASAP!***  
> 
> Note that storage system should not be located under the container's POD – since all the reports' history will be lost in case the POD fails or is redeployed.
> 
> The report can only be visualized when you run the app locally.

<br><br>

## CSV validations:
CSV validations are done at read step. 
**ContactsCsv** class (under package `model/csv`) holds all the fields mapped from the **.csv** file to java object and each field has validation annotations such as `@NotBlank, @Size, @Pattern`. 

- As of now only the maximum field size was given `via @Size (max = value)`. The maximum field size is the size which Backbase can hold to validate a valid request. 

> **ToBe Developed:** The minimum field size should be discussed and agreed by business – ==it needs to be added==, for each field separately depending on existing data. 

- The pattern validator excludes any symbols that a value can hold. Only alphanumeric characters are allowed and dashes or underscores - ==this as well needs to be discussed and agreed by business== for each field.

**ContactsFileReader class** (under package `reader`) holds a couple of things:
1. The configuration from where the file should be read (the path from internal storage) and the lines to be skipped. As of now the value is 1 which means only the header will not be read and mapped – in case the **.csv** file contains a header, otherwise if it doesn't contain, it should be 0. 
2. A template that maps the **.csv** file value order to a specific header order, and it's linked to **ContactsCsv** class. 
3. The logic from the override method **doRead()** allows for the annotations in **ContactsCsv** class to be processed and validated, otherwise it will not work. 

> **ToBe Developed**   
> The method **doRead()** needs to be created as a generic method under `utils` package and should be used across all jobs.

<br><br>

## Exceptions:
Under package `exceptions` there is the **ControllerAdvisor** class which contains all custom configuration for your error handling.

<br><br>

## Listener:
Under package `listener` there is the **JobExecutionListenerImpl** class that holds ==the report== (general and job) logic.

<br><br>

## Mappers:
Under package `mapper` are located interfaces used for mapping the **.csv** file with the request class that will call the **integration-api** endpoint for that particular job. As of now we have the **ContactsMapper** interface that maps **ContactsCsv** class to **ContactsRequest** class that is used to access **integration-api** for Contacts Bulk Import.

<br><br>

## Object mappers:
Under package `model/pojos` there are classes that hold (should hold) all available fields for the request body that is used to access the **integration-api** service for a particular component.
> **ToBe Developed**  
> Probably it will be a good idea to create subpackages and group each pojo class for particular job `e.g.: model/pojos/contacts/`, unless they can’t be reused for other jobs.

<br><br>

## Services:
Under package `service` there are classes that hold the restTemplate call logic for **Contacts** `ContactsService` and the developed functionality for **/api/fullingestionJob** and **/api/status** endpoint in **JobService**.

<br><br>

## Custom DB table

This service includes a custom DB called **custom-batch-ingestion**, this custom DB is created via liquibase and the changelog file can be found under `resources/db/changelog`.
The table contents contains all the BATCH tables (out-of-the-box) that are keeping track of Job/Step executions etc., and these tables are created by running a **.sql** file.

Also, there is a new custom table that was added called BATCH_JOB_MAPPING that can be found under `resources/db/changelog`, and it is created via liquibase commands (of type **.yml**).

BATCH_JOB_MAPPING contains 4 fields:
* JOB_MAPPING_ID
* JOB_JOB_INSTANCE_ID
* JOB_NAME
* JOB_MASTER_ID

This table was design to keep track of the ingestion process job executions and it's directly linked to API Get Status (Master and Child)

<br><br>

## Accessing Swagger Doc
Swagger Doc is integrated in the project and can be accessed by following links below:

**URL access:**

`$host/v2/api-docs` > for viewing available apis  
`$host/swagger-ui.html` > for viewing in browser the signature of this service 

**Examples:**  

`http://localhost:9999/v2/api-docs`  
`http://localhost:9999/swagger-ui.html`

> **Note:**   
> Make sure you document each field with descriptions, examples etc., as well as to include request/response examples

<br><br>

# ITSS-BATCH-INGESTION APIs signature:

### 1. Run Ingestion Job

* RequestType: POST
* URL: ${host}/api/fullIngestionJob
* RequestBody:
```
{
    "externalUserId": "2c93b0817f97340c017f973b9a470000",
    "serviceAgreementId": "2c93b0817f97340c017f973b9a470000",
    "skipJobs": {
        "skipContacts": false,
        "skipTransfers": true,
        "skipSavings": true
    }
}
```

* ResponseBody:
```
{
	"fullIngestionJobId": "a6c96041-8b3a-4404-8b6c-8f887bd209eb",
	"partialIngestion": {
		"contactsJobId": "11"
	}
}
```

* Script (under Tests) > useful to get dynamically **fullIngestionJobId** and **contactsJobId** that are used in Get Status requests (master and child)
```
var response = JSON.parse(responseBody);
postman.setEnvironmentVariable("fullIngestionJobId", response.fullIngestionJobId);
postman.setEnvironmentVariable("contactsJobId", response.partialIngestion.contactsJobId);

```

> **NOTE:** Make sure you created in environmental variables the following variables (for the script to work) > **fullIngestionJobId** and **contactsJobId**

> **NOTE_2:** The script will need to be modified and add more variables for future jobs 

<br><br>

### 2. Get Status (Master)

* RequestType: POST
* URL: ${host}/api/status
* RequestBody:
```
{
    "fullIngestionJobId": "{{fullIngestionJobId}}"
}
```

* ResponseBody:
```
{
	"fullIngestionJobStatus": "DONE",
	"partialIngestionStatus": {
		"contactsJobStatus": "DONE"
	}
}
```

<br><br>

### 3. Get Status (Child)

* RequestType: POST
* URL: ${host}/api/status
* RequestBody:
```
{
    "contactsJobId": "{{contactsJobId}}"
}
```

* ResponseBody:
```
{
	"fullIngestionJobStatus": "DONE",
	"partialIngestionStatus": {
		"contactsJobStatus": "DONE"
	}
}
```