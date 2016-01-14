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
import com.spectralogic.ds3client.models.PoolHealth;
import com.spectralogic.ds3client.models.PoolState;
import com.spectralogic.ds3client.models.PoolType;
import com.spectralogic.ds3client.models.TapeState;
import com.spectralogic.ds3client.models.TapeType;

public class GetStorageDomainCapacitySummarySpectraS3Request extends AbstractRequest {

    // Variables
    
    private final UUID storageDomainId;

    private PoolHealth poolHealth;
    private PoolState poolState;
    private PoolType poolType;
    private TapeState tapeState;
    private TapeType tapeType;

    // Constructor
    public GetStorageDomainCapacitySummarySpectraS3Request(final UUID storageDomainId) {
        this.storageDomainId = storageDomainId;
        
    }
    public GetStorageDomainCapacitySummarySpectraS3Request withPoolHealth(final PoolHealth poolHealth) {
        this.poolHealth = poolHealth;
        this.updateQueryParam("pool_health", poolHealth.toString());
        return this;
    }

    public GetStorageDomainCapacitySummarySpectraS3Request withPoolState(final PoolState poolState) {
        this.poolState = poolState;
        this.updateQueryParam("pool_state", poolState.toString());
        return this;
    }

    public GetStorageDomainCapacitySummarySpectraS3Request withPoolType(final PoolType poolType) {
        this.poolType = poolType;
        this.updateQueryParam("pool_type", poolType.toString());
        return this;
    }

    public GetStorageDomainCapacitySummarySpectraS3Request withTapeState(final TapeState tapeState) {
        this.tapeState = tapeState;
        this.updateQueryParam("tape_state", tapeState.toString());
        return this;
    }

    public GetStorageDomainCapacitySummarySpectraS3Request withTapeType(final TapeType tapeType) {
        this.tapeType = tapeType;
        this.updateQueryParam("tape_type", tapeType.toString());
        return this;
    }


    @Override
    public HttpVerb getVerb() {
        return HttpVerb.GET;
    }

    @Override
    public String getPath() {
        return "/_rest_/capacity_summary/";
    }
    
    public UUID getStorageDomainId() {
        return this.storageDomainId;
    }


    public PoolHealth getPoolHealth() {
        return this.poolHealth;
    }

    public PoolState getPoolState() {
        return this.poolState;
    }

    public PoolType getPoolType() {
        return this.poolType;
    }

    public TapeState getTapeState() {
        return this.tapeState;
    }

    public TapeType getTapeType() {
        return this.tapeType;
    }

}