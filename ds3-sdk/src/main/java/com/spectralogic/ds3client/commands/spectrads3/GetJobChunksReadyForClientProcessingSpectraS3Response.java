/*
 * ******************************************************************************
 *   Copyright 2014-2015 Spectra Logic Corporation. All Rights Reserved.
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

// This code is auto-generated, do not modify
package com.spectralogic.ds3client.commands.spectrads3;

import com.spectralogic.ds3client.networking.WebResponse;
import java.io.IOException;
import java.io.InputStream;
import com.spectralogic.ds3client.models.JobWithChunksApiBean;
import com.spectralogic.ds3client.serializer.XmlOutput;
import com.spectralogic.ds3client.commands.AbstractResponse;
import com.spectralogic.ds3client.commands.RetryAfterExpectedException;

public class GetJobChunksReadyForClientProcessingSpectraS3Response extends AbstractResponse {

    private JobWithChunksApiBean jobWithChunksApiBeanResult;

    public enum Status {
        AVAILABLE, RETRYLATER
    }

    private Status status;
    private int retryAfterSeconds;

    public Status getStatus() {
        return this.status;
    }

    public int getRetryAfterSeconds() {
        return this.retryAfterSeconds;
    }

    public GetJobChunksReadyForClientProcessingSpectraS3Response(final WebResponse response) throws IOException {
        super(response);
    }

    @Override
    protected void processResponse() throws IOException {
        try (final WebResponse webResponse = this.getResponse()) {
            this.checkStatusCode(200);

            switch (this.getStatusCode()) {
            case 200:
                try (final InputStream content = webResponse.getResponseStream()) {
                    this.jobWithChunksApiBeanResult = XmlOutput.fromXml(content, JobWithChunksApiBean.class);
                    if (this.jobWithChunksApiBeanResult.getObjects() == null) {
                        this.status = Status.RETRYLATER;
                        this.retryAfterSeconds = parseRetryAfter(webResponse);
                    } else {
                        this.status = Status.AVAILABLE;
                    }
                }
                break;
            default:
                assert false : "checkStatusCode should have made it impossible to reach this line.";
            }
        } finally {
            this.getResponse().close();
        }
    }

    private static int parseRetryAfter(final WebResponse webResponse) {
        final String retryAfter = webResponse.getHeaders().get("Retry-After").get(0);
        if (retryAfter == null) {
            throw new RetryAfterExpectedException();
        }
        return Integer.parseInt(retryAfter);
    }

    public JobWithChunksApiBean getJobWithChunksApiBeanResult() {
        return this.jobWithChunksApiBeanResult;
    }


}