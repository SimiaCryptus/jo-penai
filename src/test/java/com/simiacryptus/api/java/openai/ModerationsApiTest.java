/*
 * OpenAI API
 * The OpenAI REST API. Please see https://platform.openai.com/docs/api-reference for more details.
 *
 * The version of the OpenAPI document: 2.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.simiacryptus.api.java.openai;

import com.simiacryptus.api.java.ApiException;
import com.simiacryptus.api.java.openai.model.CreateModerationRequest;
import com.simiacryptus.api.java.openai.model.CreateModerationResponse;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Assert;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ModerationsApi
 */
@Ignore
public class ModerationsApiTest {

    private final ModerationsApi api = new ModerationsApi();

    /**
     * Classifies if text is potentially harmful.
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createModerationTest() throws ApiException {
        CreateModerationRequest createModerationRequest = null;
        CreateModerationResponse response = api.createModeration(createModerationRequest);

        // TODO: test validations
    }
}
