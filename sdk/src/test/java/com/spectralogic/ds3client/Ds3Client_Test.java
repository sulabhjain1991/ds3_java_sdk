/*
 * ******************************************************************************
 *   Copyright 2014 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3client;

import com.spectralogic.ds3client.commands.*;
import com.spectralogic.ds3client.commands.AllocateJobChunkResponse.Status;
import com.spectralogic.ds3client.models.Bucket;
import com.spectralogic.ds3client.models.Contents;
import com.spectralogic.ds3client.models.ListAllMyBucketsResult;
import com.spectralogic.ds3client.models.ListBucketResult;
import com.spectralogic.ds3client.models.bulk.BulkObject;
import com.spectralogic.ds3client.models.bulk.Ds3Object;
import com.spectralogic.ds3client.models.bulk.MasterObjectList;
import com.spectralogic.ds3client.models.bulk.Objects;
import com.spectralogic.ds3client.networking.FailedRequestException;
import com.spectralogic.ds3client.serializer.XmlProcessingException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SignatureException;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class Ds3Client_Test {
    @Test
    public void getService() throws IOException, SignatureException {
        final String stringResponse = "<ListAllMyBucketsResult xmlns=\"http://doc.s3.amazonaws.com/2006-03-01\">\n" +
                "<Owner><ID>ryanid</ID><DisplayName>ryan</DisplayName></Owner><Buckets><Bucket><Name>testBucket2</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest1</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest2</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest3</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest4</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest5</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>bulkTest6</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>testBucket3</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>testBucket1</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket><Bucket><Name>testbucket</Name><CreationDate>2013-12-11T23:20:09</CreationDate></Bucket></Buckets></ListAllMyBucketsResult>";
        
        final List<String> expectedBucketNames = Arrays.asList(
            "testBucket2",
            "bulkTest",
            "bulkTest1",
            "bulkTest2",
            "bulkTest3",
            "bulkTest4",
            "bulkTest5",
            "bulkTest6",
            "testBucket3",
            "testBucket1",
            "testbucket"
        );
        
        final GetServiceResponse response = MockNetwork
                .expecting(HttpVerb.GET, "/", null, null)
                .returning(200, stringResponse)
                .asClient()
                .getService(new GetServiceRequest());
        final ListAllMyBucketsResult result = response.getResult();
        assertThat(result.getOwner().getDisplayName(), is("ryan"));
        assertThat(result.getOwner().getId(), is("ryanid"));
        
        final List<Bucket> buckets = result.getBuckets();
        final List<String> bucketNames = new ArrayList<>();
        for (final Bucket bucket : buckets) {
            bucketNames.add(bucket.getName());
            assertThat(bucket.getCreationDate(), is("2013-12-11T23:20:09"));
        }
        assertThat(bucketNames, is(expectedBucketNames));
    }

    @Test(expected = FailedRequestException.class)
    public void getBadService() throws IOException, SignatureException {
        MockNetwork
                .expecting(HttpVerb.GET, "/", null, null)
                .returning(400, "")
                .asClient()
                .getService(new GetServiceRequest());
    }

    @Test
    public void getBucket() throws IOException, SignatureException {
        final String xmlResponse = "<ListBucketResult xmlns=\"http://s3.amazonaws.com/doc/2006-03-01/\"><Name>remoteTest16</Name><Prefix/><Marker/><MaxKeys>1000</MaxKeys><IsTruncated>false</IsTruncated><Contents><Key>user/hduser/gutenberg/20417.txt.utf-8</Key><LastModified>2014-01-03T13:26:47.000Z</LastModified><ETag>8B19F3F41868106382A677C3435BDCE5</ETag><Size>674570</Size><StorageClass>STANDARD</StorageClass><Owner><ID>ryan</ID><DisplayName>ryan</DisplayName></Owner></Contents><Contents><Key>user/hduser/gutenberg/5000.txt.utf-8</Key><LastModified>2014-01-03T13:26:47.000Z</LastModified><ETag>9DE344878423E44B129730CE22B4B137</ETag><Size>1423803</Size><StorageClass>STANDARD</StorageClass><Owner><ID>ryan</ID><DisplayName>ryan</DisplayName></Owner></Contents><Contents><Key>user/hduser/gutenberg/4300.txt.utf-8</Key><LastModified>2014-01-03T13:26:47.000Z</LastModified><ETag>33EE4519EA7DDAB27CA4E2742326D70B</ETag><Size>1573150</Size><StorageClass>DEEP</StorageClass><Owner><ID>ryan</ID><DisplayName>ryan</DisplayName></Owner></Contents></ListBucketResult>";
        
        final ListBucketResult result = MockNetwork
                .expecting(HttpVerb.GET, "/remoteTest16", null, null)
                .returning(200, xmlResponse)
                .asClient()
                .getBucket(new GetBucketRequest("remoteTest16"))
                .getResult();
        
        assertThat(result.getName(), is("remoteTest16"));
        assertThat(result.getPrefix(), is(nullValue()));
        assertThat(result.getMarker(), is(nullValue()));
        assertThat(result.getMaxKeys(), is(1000));

        final List<Contents> contentsList = result.getContentsList();
        assertThat(contentsList, is(notNullValue()));
        assertThat(contentsList.size(), is(3));
        this.assertContentsEquals(
            contentsList.get(0),
            "user/hduser/gutenberg/20417.txt.utf-8",
            "2014-01-03T13:26:47.000Z",
            "8B19F3F41868106382A677C3435BDCE5",
            674570,
            "STANDARD"
        );
        this.assertContentsEquals(
            contentsList.get(1),
            "user/hduser/gutenberg/5000.txt.utf-8",
            "2014-01-03T13:26:47.000Z",
            "9DE344878423E44B129730CE22B4B137",
            1423803,
            "STANDARD"
        );
        this.assertContentsEquals(
            contentsList.get(2),
            "user/hduser/gutenberg/4300.txt.utf-8",
            "2014-01-03T13:26:47.000Z",
            "33EE4519EA7DDAB27CA4E2742326D70B",
            1573150,
            "DEEP"
        );
    }

    private void assertContentsEquals(
            final Contents contents,
            final String key,
            final String lastModified,
            final String eTag,
            final long size,
            final String storageClass) {
        assertThat(contents.getKey(), is(key));
        assertThat(contents.getLastModified(), is(lastModified));
        assertThat(contents.geteTag(), is(eTag));
        assertThat(contents.getSize(), is(size));
        assertThat(contents.getStorageClass(), is(storageClass));
    }

    @Test
    public void putBucket() throws IOException, SignatureException {
        MockNetwork
                .expecting(HttpVerb.PUT, "/bucketName", null, null)
                .returning(200, "")
                .asClient()
                .putBucket(new PutBucketRequest("bucketName"));
    }
    
    @Test
    public void deleteBucket() throws IOException, SignatureException {
        MockNetwork
                .expecting(HttpVerb.DELETE, "/bucketName", null, null)
                .returning(204, "")
                .asClient()
                .deleteBucket(new DeleteBucketRequest("bucketName"));
    }

    @Test
    public void deleteObject() throws IOException, SignatureException {
        MockNetwork
                .expecting(HttpVerb.DELETE, "/bucketName/my/file.txt", null, null)
                .returning(204, "")
                .asClient()
                .deleteObject(new DeleteObjectRequest("bucketName", "my/file.txt"));
    }

    @Test(expected = FailedRequestException.class)
    public void getBadBucket() throws IOException, SignatureException {
        MockNetwork
                .expecting(HttpVerb.GET, "/remoteTest16", null, null)
                .returning(400, "")
                .asClient()
                .getBucket(new GetBucketRequest("remoteTest16"));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void getObjectWithoutJobId() throws IOException, SignatureException {
        final String stringResponse = "Response";
        final InputStream content = MockNetwork
                .expecting(HttpVerb.GET, "/bucketName/object", null, null)
                .returning(200, stringResponse)
                .asClient()
                .getObject(new GetObjectRequest("bucketName", "object"))
                .getContent();
        assertThat(IOUtils.toString(content), is(stringResponse));
    }

    @Test
    public void getObject() throws IOException, SignatureException {
        final String jobIdString = "a4a586a1-cb80-4441-84e2-48974e982d51";
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("job", jobIdString);
        queryParams.put("offset", Long.toString(0));
        
        final String stringResponse = "Response";
        final InputStream content = MockNetwork
                .expecting(HttpVerb.GET, "/bucketName/object", queryParams, null)
                .returning(200, stringResponse)
                .asClient()
                .getObject(new GetObjectRequest("bucketName", "object", 0,UUID.fromString(jobIdString)))
                .getContent();
        assertThat(IOUtils.toString(content), is(stringResponse));
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void putObjectWithoutJobId() throws IOException, SignatureException {
        final String output = "This is some data.";
        final ByteArrayInputStream requestStream = new ByteArrayInputStream(output.getBytes("UTF-8"));
        MockNetwork
                .expecting(HttpVerb.PUT, "/bucketName/objectName", null, output)
                .returning(200, "")
                .asClient()
                .putObject(new PutObjectRequest("bucketName", "objectName", requestStream.available(), requestStream));
    }
    
    @Test
    public void putObject() throws IOException, SignatureException {
        final String jobIdString = "a4a586a1-cb80-4441-84e2-48974e982d51";
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("job", jobIdString);
        queryParams.put("offset", Long.toString(0));
        
        final String output = "This is some data.";
        final ByteArrayInputStream requestStream = new ByteArrayInputStream(output.getBytes("UTF-8"));
        MockNetwork
                .expecting(HttpVerb.PUT, "/bucketName/objectName", queryParams, output)
                .returning(200, "")
                .asClient()
                .putObject(new PutObjectRequest("bucketName", "objectName", UUID.fromString(jobIdString), requestStream.available(), 0, requestStream));
    }
    
    @Test
    public void bulkPut() throws IOException, SignatureException, XmlProcessingException {
        this.runBulkTest(BulkCommand.PUT, new BulkTestDriver() {
            @Override
            public MasterObjectList performRestCall(final Ds3Client client, final String bucket, final List<Ds3Object> objects)
                    throws SignatureException, IOException, XmlProcessingException {
                return client.bulkPut(new BulkPutRequest(bucket, objects)).getResult();
            }
        });
    }
    
    @Test
    public void bulkGet() throws IOException, SignatureException, XmlProcessingException {
        this.runBulkTest(BulkCommand.GET, new BulkTestDriver() {
            @Override
            public MasterObjectList performRestCall(final Ds3Client client, final String bucket, final List<Ds3Object> objects)
                    throws SignatureException, IOException, XmlProcessingException {
                return client.bulkGet(new BulkGetRequest(bucket, objects)).getResult();
            }
        });
    }
    
    private interface BulkTestDriver {
        MasterObjectList performRestCall(final Ds3Client client, final String bucket, final List<Ds3Object> objects)
                throws SignatureException, IOException, XmlProcessingException;
    }
    
    public void runBulkTest(final BulkCommand command, final BulkTestDriver driver) throws IOException, SignatureException, XmlProcessingException {
        final List<Ds3Object> objects = Arrays.asList(
            new Ds3Object("file1", 256),
            new Ds3Object("file2", 1202),
            new Ds3Object("file3", 2523)
        );

        final String expectedXmlBody;

        if (command == BulkCommand.GET) {
            expectedXmlBody = "<Objects><Object Name=\"file1\"/><Object Name=\"file2\"/><Object Name=\"file3\"/></Objects>";
        }
        else {
            expectedXmlBody = "<Objects><Object Name=\"file1\" Size=\"256\"/><Object Name=\"file2\" Size=\"1202\"/><Object Name=\"file3\" Size=\"2523\"/></Objects>";
        }

        final String xmlResponse = "<MasterObjectList BucketName=\"lib\" JobId=\"9652a41a-218a-4158-af1b-064ab9e4ef71\" Priority=\"NORMAL\" RequestType=\"PUT\" StartDate=\"2014-07-29T16:08:39.000Z\"><Nodes><Node EndPoint=\"FAILED_TO_DETERMINE_DATAPATH_IP_ADDRESS\" HttpPort=\"80\" HttpsPort=\"443\" Id=\"b18ee082-1352-11e4-945e-080027ebeb6d\"/></Nodes><Objects ChunkId=\"cfa3153f-57de-41c7-b1fb-f30fa4154232\" ChunkNumber=\"0\"><Object Name=\"file2\" InCache=\"false\" Length=\"1202\" Offset=\"0\"/><Object Name=\"file1\" InCache=\"false\" Length=\"256\" Offset=\"0\"/><Object Name=\"file3\" InCache=\"false\" Length=\"2523\" Offset=\"0\"/></Objects></MasterObjectList>";
        
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", command.toString());
        
        final Ds3Client client = MockNetwork
                .expecting(HttpVerb.PUT, "/_rest_/bucket/bulkTest", queryParams, expectedXmlBody)
                .returning(200, xmlResponse)
                .asClient();
        
        final List<Objects> objectListList = driver.performRestCall(client, "bulkTest", objects).getObjects();
        assertThat(objectListList.size(), is(1));
        
        final List<BulkObject> objectList = objectListList.get(0).getObjects();
        assertThat(objectList.size(), is(3));

        assertObjectEquals(objectList.get(0), "file2", 1202);
        assertObjectEquals(objectList.get(1), "file1", 256);
        assertObjectEquals(objectList.get(2), "file3", 2523);
    }
    
    private static void assertObjectEquals(final BulkObject object, final String name, final long size) {
        assertThat(object.getName(), is(name));
        assertThat(object.getLength(), is(size));
    }
    
    @Test
    public void allocateJobChunk() throws SignatureException, IOException
    {
        final String responseString =
            "<Objects ChunkId=\"203f6886-b058-4f7c-a012-8779176453b1\" ChunkNumber=\"3\" NodeId=\"a02053b9-0147-11e4-8d6a-002590c1177c\">"
            + "  <Object Name=\"client00obj000004-8000000\" InCache=\"true\" Length=\"5368709120\" Offset=\"0\"/>"
            + "  <Object Name=\"client00obj000004-8000000\" InCache=\"true\" Length=\"2823290880\" Offset=\"5368709120\"/>"
            + "  <Object Name=\"client00obj000003-8000000\" InCache=\"true\" Length=\"2823290880\" Offset=\"5368709120\"/>"
            + "  <Object Name=\"client00obj000003-8000000\" InCache=\"true\" Length=\"5368709120\" Offset=\"0\"/>"
            + "</Objects>";
        final UUID chunkId = UUID.fromString("203f6886-b058-4f7c-a012-8779176453b1");
        final UUID nodeId = UUID.fromString("a02053b9-0147-11e4-8d6a-002590c1177c");

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "allocate");
        final AllocateJobChunkResponse response = MockNetwork
            .expecting(HttpVerb.PUT, "/_rest_/job_chunk/203f6886-b058-4f7c-a012-8779176453b1", queryParams, null)
            .returning(200, responseString)
            .asClient()
            .allocateJobChunk(new AllocateJobChunkRequest(chunkId));
        
        assertThat(response.getStatus(), is(Status.ALLOCATED));
        final Objects chunk = response.getObjects();
        
        assertThat(chunk.getChunkId(), is(chunkId));
        assertThat(chunk.getChunkNumber(), is(3L));
        assertThat(chunk.getNodeId(), is(nodeId));
        
        final List<BulkObject> objects = chunk.getObjects();
        assertThat(objects.size(), is(4));

        final BulkObject object0 = objects.get(0);
        assertThat(object0.getName(), is("client00obj000004-8000000"));
        assertThat(object0.isInCache(), is(true));
        assertThat(object0.getOffset(), is(0L));
        assertThat(object0.getLength(), is(5368709120L));

        final BulkObject object1 = objects.get(1);
        assertThat(object1.getName(), is("client00obj000004-8000000"));
        assertThat(object1.isInCache(), is(true));
        assertThat(object1.getOffset(), is(5368709120L));
        assertThat(object1.getLength(), is(2823290880L));

        final BulkObject object2 = objects.get(2);
        assertThat(object2.getName(), is("client00obj000003-8000000"));
        assertThat(object2.isInCache(), is(true));
        assertThat(object2.getOffset(), is(5368709120L));
        assertThat(object2.getLength(), is(2823290880L));

        final BulkObject object3 = objects.get(3);
        assertThat(object3.getName(), is("client00obj000003-8000000"));
        assertThat(object3.isInCache(), is(true));
        assertThat(object3.getOffset(), is(0L));
        assertThat(object3.getLength(), is(5368709120L));
    }
    
    @Test
    public void allocateJobChunkReturnsChunkNotFound() throws SignatureException, IOException
    {
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "allocate");
        final AllocateJobChunkResponse response = MockNetwork
            .expecting(HttpVerb.PUT, "/_rest_/job_chunk/203f6886-b058-4f7c-a012-8779176453b1", queryParams, null)
            .returning(404, "")
            .asClient()
            .allocateJobChunk(new AllocateJobChunkRequest(UUID.fromString("203f6886-b058-4f7c-a012-8779176453b1")));
        
        assertThat(response.getStatus(), is(Status.NOTFOUND));
    }
    
    @Test
    public void allocateJobChunkReturnsRetryAfter() throws SignatureException, IOException
    {
        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("operation", "allocate");
        final Map<String, String> headers = new HashMap<>();
        headers.put("retry-after", "300");
        final AllocateJobChunkResponse response = MockNetwork
            .expecting(HttpVerb.PUT, "/_rest_/job_chunk/203f6886-b058-4f7c-a012-8779176453b1", queryParams, null)
            .returning(503, "", headers)
            .asClient()
            .allocateJobChunk(new AllocateJobChunkRequest(UUID.fromString("203f6886-b058-4f7c-a012-8779176453b1")));
        
        assertThat(response.getStatus(), is(Status.RETRYLATER));
        assertThat(response.getRetryAfterSeconds(), is(300));
    }
}