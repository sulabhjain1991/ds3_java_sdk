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
package com.spectralogic.ds3client.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spectralogic.ds3client.models.CanceledJob;
import java.util.List;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class CanceledJobList {

    // Variables
    @JsonProperty("CanceledJob")
    @JacksonXmlElementWrapper
    private List<CanceledJob> canceledJob;

    // Constructor
    public CanceledJobList(final List<CanceledJob> canceledJob) {
        this.canceledJob = canceledJob;
    }

    // Getters and Setters
    
    public List<CanceledJob> getCanceledJob() {
        return this.canceledJob;
    }

    public void setCanceledJob(final List<CanceledJob> canceledJob) {
        this.canceledJob = canceledJob;
    }

}