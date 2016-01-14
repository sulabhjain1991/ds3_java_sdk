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

import com.spectralogic.ds3client.commands.AbstractRequest;
import com.spectralogic.ds3client.HttpVerb;
import java.util.UUID;
import java.lang.String;

public class ModifyUserSpectraS3Request extends AbstractRequest {

    // Variables
    
    private final UUID userId;

    private UUID defaultDataPolicyId;
    private String name;

    // Constructor
    public ModifyUserSpectraS3Request(final UUID userId) {
        this.userId = userId;
        
    }
    public ModifyUserSpectraS3Request withDefaultDataPolicyId(final UUID defaultDataPolicyId) {
        this.defaultDataPolicyId = defaultDataPolicyId;
        this.updateQueryParam("default_data_policy_id", defaultDataPolicyId.toString());
        return this;
    }

    public ModifyUserSpectraS3Request withName(final String name) {
        this.name = name;
        this.updateQueryParam("name", name);
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.PUT;
    }

    @Override
    public String getPath() {
        return "/_rest_/user/" + userId.toString();
    }
    
    public UUID getUserId() {
        return this.userId;
    }


    public UUID getDefaultDataPolicyId() {
        return this.defaultDataPolicyId;
    }

    public String getName() {
        return this.name;
    }

}