/*
 * ModelsLab AI API's
 * This is ModelsLab Stable Diffusion and Multiple AI APIs, here you can pass details to generate images using API, without needs of GPU locally.  You will need to have _**api key**_ to generate images, if you don't have it, get it from [https://modelslab.com](https://modelslab.com/)  Send Json post request with data and links, don't send files as raw format, send accessible links instead
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package com.simiacryptus.api.java.modelslab;

import com.fasterxml.jackson.core.type.TypeReference;

import com.simiacryptus.api.java.ApiException;
import com.simiacryptus.api.java.ApiClient;
import com.simiacryptus.api.java.Configuration;
import com.simiacryptus.api.java.Pair;

import com.simiacryptus.api.java.modelslab.model.DreamboothFetchQueuedImagesRequest;
import com.simiacryptus.api.java.modelslab.model.DreamboothImg2imgRequest;
import com.simiacryptus.api.java.modelslab.model.DreamboothInpaintingRequest;
import com.simiacryptus.api.java.modelslab.model.DreamboothReloadModelRequest;
import com.simiacryptus.api.java.modelslab.model.DreamboothText2VideoRequest;
import com.simiacryptus.api.java.modelslab.model.DreamboothText2image200Response;
import com.simiacryptus.api.java.modelslab.model.DreamboothText2imageRequest;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-03-09T16:25:01.849546200-05:00[America/New_York]")
public class CommunityModelsApiApi {


  private ApiClient apiClient;

  public CommunityModelsApiApi() {
    this(Configuration.getDefaultApiClient());
  }

  public CommunityModelsApiApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * DreamBooth Fetch Queued Images
   * DreamBooth Fetch Queued Images
   * @param dreamboothFetchQueuedImagesRequest  (optional)
   * @throws ApiException if fails to make API call
   */
  public void dreamboothFetchQueuedImages(DreamboothFetchQueuedImagesRequest dreamboothFetchQueuedImagesRequest) throws ApiException {
    this.dreamboothFetchQueuedImages(dreamboothFetchQueuedImagesRequest, Collections.emptyMap());
  }


  /**
   * DreamBooth Fetch Queued Images
   * DreamBooth Fetch Queued Images
   * @param dreamboothFetchQueuedImagesRequest  (optional)
   * @param additionalHeaders additionalHeaders for this call
   * @throws ApiException if fails to make API call
   */
  public void dreamboothFetchQueuedImages(DreamboothFetchQueuedImagesRequest dreamboothFetchQueuedImagesRequest, Map<String, String> additionalHeaders) throws ApiException {
    Object localVarPostBody = dreamboothFetchQueuedImagesRequest;
    
    // create path and map variables
    String localVarPath = "/api/v4/dreambooth/fetch";

    StringJoiner localVarQueryStringJoiner = new StringJoiner("&");
    String localVarQueryParameterBaseName;
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    
    localVarHeaderParams.putAll(additionalHeaders);

    
    
    final String[] localVarAccepts = {
      
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    apiClient.invokeAPI(
        localVarPath,
        "POST",
        localVarQueryParams,
        localVarCollectionQueryParams,
        localVarQueryStringJoiner.toString(),
        localVarPostBody,
        localVarHeaderParams,
        localVarCookieParams,
        localVarFormParams,
        localVarAccept,
        localVarContentType,
        localVarAuthNames,
        null
    );
  }

  /**
   * Dreambooth Img2Img
   * Dreambooth Img2Img
   * @param dreamboothImg2imgRequest  (optional)
   * @throws ApiException if fails to make API call
   */
  public void dreamboothImg2img(DreamboothImg2imgRequest dreamboothImg2imgRequest) throws ApiException {
    this.dreamboothImg2img(dreamboothImg2imgRequest, Collections.emptyMap());
  }


  /**
   * Dreambooth Img2Img
   * Dreambooth Img2Img
   * @param dreamboothImg2imgRequest  (optional)
   * @param additionalHeaders additionalHeaders for this call
   * @throws ApiException if fails to make API call
   */
  public void dreamboothImg2img(DreamboothImg2imgRequest dreamboothImg2imgRequest, Map<String, String> additionalHeaders) throws ApiException {
    Object localVarPostBody = dreamboothImg2imgRequest;
    
    // create path and map variables
    String localVarPath = "/api/v4/dreambooth/img2img";

    StringJoiner localVarQueryStringJoiner = new StringJoiner("&");
    String localVarQueryParameterBaseName;
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    
    localVarHeaderParams.putAll(additionalHeaders);

    
    
    final String[] localVarAccepts = {
      
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    apiClient.invokeAPI(
        localVarPath,
        "POST",
        localVarQueryParams,
        localVarCollectionQueryParams,
        localVarQueryStringJoiner.toString(),
        localVarPostBody,
        localVarHeaderParams,
        localVarCookieParams,
        localVarFormParams,
        localVarAccept,
        localVarContentType,
        localVarAuthNames,
        null
    );
  }

  /**
   * Dreambooth Inpainting
   * Dreambooth Inpainting
   * @param dreamboothInpaintingRequest  (optional)
   * @throws ApiException if fails to make API call
   */
  public void dreamboothInpainting(DreamboothInpaintingRequest dreamboothInpaintingRequest) throws ApiException {
    this.dreamboothInpainting(dreamboothInpaintingRequest, Collections.emptyMap());
  }


  /**
   * Dreambooth Inpainting
   * Dreambooth Inpainting
   * @param dreamboothInpaintingRequest  (optional)
   * @param additionalHeaders additionalHeaders for this call
   * @throws ApiException if fails to make API call
   */
  public void dreamboothInpainting(DreamboothInpaintingRequest dreamboothInpaintingRequest, Map<String, String> additionalHeaders) throws ApiException {
    Object localVarPostBody = dreamboothInpaintingRequest;
    
    // create path and map variables
    String localVarPath = "/api/v4/dreambooth/inpaint";

    StringJoiner localVarQueryStringJoiner = new StringJoiner("&");
    String localVarQueryParameterBaseName;
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    
    localVarHeaderParams.putAll(additionalHeaders);

    
    
    final String[] localVarAccepts = {
      
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    apiClient.invokeAPI(
        localVarPath,
        "POST",
        localVarQueryParams,
        localVarCollectionQueryParams,
        localVarQueryStringJoiner.toString(),
        localVarPostBody,
        localVarHeaderParams,
        localVarCookieParams,
        localVarFormParams,
        localVarAccept,
        localVarContentType,
        localVarAuthNames,
        null
    );
  }

  /**
   * DreamBooth Reload Model
   * After 7 days of inactivity, model will be deleted from inference server.  To reload a model and using it again, you can make api call. it will take 2 minutes to reload a model.
   * @param dreamboothReloadModelRequest  (optional)
   * @throws ApiException if fails to make API call
   */
  public void dreamboothReloadModel(DreamboothReloadModelRequest dreamboothReloadModelRequest) throws ApiException {
    this.dreamboothReloadModel(dreamboothReloadModelRequest, Collections.emptyMap());
  }


  /**
   * DreamBooth Reload Model
   * After 7 days of inactivity, model will be deleted from inference server.  To reload a model and using it again, you can make api call. it will take 2 minutes to reload a model.
   * @param dreamboothReloadModelRequest  (optional)
   * @param additionalHeaders additionalHeaders for this call
   * @throws ApiException if fails to make API call
   */
  public void dreamboothReloadModel(DreamboothReloadModelRequest dreamboothReloadModelRequest, Map<String, String> additionalHeaders) throws ApiException {
    Object localVarPostBody = dreamboothReloadModelRequest;
    
    // create path and map variables
    String localVarPath = "/api/v4/dreambooth/model_reload";

    StringJoiner localVarQueryStringJoiner = new StringJoiner("&");
    String localVarQueryParameterBaseName;
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    
    localVarHeaderParams.putAll(additionalHeaders);

    
    
    final String[] localVarAccepts = {
      
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    apiClient.invokeAPI(
        localVarPath,
        "POST",
        localVarQueryParams,
        localVarCollectionQueryParams,
        localVarQueryStringJoiner.toString(),
        localVarPostBody,
        localVarHeaderParams,
        localVarCookieParams,
        localVarFormParams,
        localVarAccept,
        localVarContentType,
        localVarAuthNames,
        null
    );
  }

  /**
   * Dreambooth Text 2 Video
   * Create video from a trained model or publicly available community models.  Pass prompt and it will generate video with model_id
   * @param dreamboothText2VideoRequest  (optional)
   * @throws ApiException if fails to make API call
   */
  public void dreamboothText2Video(DreamboothText2VideoRequest dreamboothText2VideoRequest) throws ApiException {
    this.dreamboothText2Video(dreamboothText2VideoRequest, Collections.emptyMap());
  }


  /**
   * Dreambooth Text 2 Video
   * Create video from a trained model or publicly available community models.  Pass prompt and it will generate video with model_id
   * @param dreamboothText2VideoRequest  (optional)
   * @param additionalHeaders additionalHeaders for this call
   * @throws ApiException if fails to make API call
   */
  public void dreamboothText2Video(DreamboothText2VideoRequest dreamboothText2VideoRequest, Map<String, String> additionalHeaders) throws ApiException {
    Object localVarPostBody = dreamboothText2VideoRequest;
    
    // create path and map variables
    String localVarPath = "/api/v4/dreambooth/text2video";

    StringJoiner localVarQueryStringJoiner = new StringJoiner("&");
    String localVarQueryParameterBaseName;
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    
    localVarHeaderParams.putAll(additionalHeaders);

    
    
    final String[] localVarAccepts = {
      
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    apiClient.invokeAPI(
        localVarPath,
        "POST",
        localVarQueryParams,
        localVarCollectionQueryParams,
        localVarQueryStringJoiner.toString(),
        localVarPostBody,
        localVarHeaderParams,
        localVarCookieParams,
        localVarFormParams,
        localVarAccept,
        localVarContentType,
        localVarAuthNames,
        null
    );
  }

  /**
   * Dreambooth Text2Image
   * Use dreambooth text2img API to generate images from any custom trained model or from any publicly available model.  You can also pass scheduler in API  possible values for scheduler are  - DDPMScheduler - PNDMScheduler - EulerAncestralDiscreteScheduler - DDIMScheduler - LMSDiscreteScheduler - EulerDiscreteScheduler - DPMSolverMultistepScheduler
   * @param dreamboothText2imageRequest  (optional)
   * @return DreamboothText2image200Response
   * @throws ApiException if fails to make API call
   */
  public DreamboothText2image200Response dreamboothText2image(DreamboothText2imageRequest dreamboothText2imageRequest) throws ApiException {
    return this.dreamboothText2image(dreamboothText2imageRequest, Collections.emptyMap());
  }


  /**
   * Dreambooth Text2Image
   * Use dreambooth text2img API to generate images from any custom trained model or from any publicly available model.  You can also pass scheduler in API  possible values for scheduler are  - DDPMScheduler - PNDMScheduler - EulerAncestralDiscreteScheduler - DDIMScheduler - LMSDiscreteScheduler - EulerDiscreteScheduler - DPMSolverMultistepScheduler
   * @param dreamboothText2imageRequest  (optional)
   * @param additionalHeaders additionalHeaders for this call
   * @return DreamboothText2image200Response
   * @throws ApiException if fails to make API call
   */
  public DreamboothText2image200Response dreamboothText2image(DreamboothText2imageRequest dreamboothText2imageRequest, Map<String, String> additionalHeaders) throws ApiException {
    Object localVarPostBody = dreamboothText2imageRequest;
    
    // create path and map variables
    String localVarPath = "/api/v4/dreambooth";

    StringJoiner localVarQueryStringJoiner = new StringJoiner("&");
    String localVarQueryParameterBaseName;
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    
    localVarHeaderParams.putAll(additionalHeaders);

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    TypeReference<DreamboothText2image200Response> localVarReturnType = new TypeReference<DreamboothText2image200Response>() {};
    return apiClient.invokeAPI(
        localVarPath,
        "POST",
        localVarQueryParams,
        localVarCollectionQueryParams,
        localVarQueryStringJoiner.toString(),
        localVarPostBody,
        localVarHeaderParams,
        localVarCookieParams,
        localVarFormParams,
        localVarAccept,
        localVarContentType,
        localVarAuthNames,
        localVarReturnType
    );
  }

}
