/*
 * ******************************************************************************
 *   Copyright 2014-2016 Spectra Logic Corporation. All Rights Reserved.
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

import com.spectralogic.ds3client.networking.HttpVerb;
import com.spectralogic.ds3client.commands.interfaces.AbstractRequest;
import com.google.common.net.UrlEscapers;

public class DeleteS3TargetBucketNameSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final String s3TargetBucketName;

    // Constructor
    
    
    public DeleteS3TargetBucketNameSpectraS3Request(final String s3TargetBucketName) {
        this.s3TargetBucketName = s3TargetBucketName;
        
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.DELETE;
    }

    @Override
    public String getPath() {
        return "/_rest_/s3_target_bucket_name/" + s3TargetBucketName;
    }
    
    public String getS3TargetBucketName() {
        return this.s3TargetBucketName;
    }

}