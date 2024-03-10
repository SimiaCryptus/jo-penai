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

import com.fasterxml.jackson.core.type.TypeReference;

import com.simiacryptus.api.java.ApiException;
import com.simiacryptus.api.java.ApiClient;
import com.simiacryptus.api.java.Configuration;
import com.simiacryptus.api.java.Pair;

import java.math.BigDecimal;
import com.simiacryptus.api.java.openai.model.CreateSpeechRequest;
import com.simiacryptus.api.java.openai.model.CreateTranscription200Response;
import com.simiacryptus.api.java.openai.model.CreateTranscriptionRequestModel;
import com.simiacryptus.api.java.openai.model.CreateTranslation200Response;
import java.io.File;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-03-09T16:24:46.732753800-05:00[America/New_York]")
public class AudioApi {


  private ApiClient apiClient;

  public AudioApi() {
    this(Configuration.getDefaultApiClient());
  }

  public AudioApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Generates audio from the input text.
   * 
   * @param createSpeechRequest  (required)
   * @return File
   * @throws ApiException if fails to make API call
   */
  public File createSpeech(CreateSpeechRequest createSpeechRequest) throws ApiException {
    return this.createSpeech(createSpeechRequest, Collections.emptyMap());
  }


  /**
   * Generates audio from the input text.
   * 
   * @param createSpeechRequest  (required)
   * @param additionalHeaders additionalHeaders for this call
   * @return File
   * @throws ApiException if fails to make API call
   */
  public File createSpeech(CreateSpeechRequest createSpeechRequest, Map<String, String> additionalHeaders) throws ApiException {
    Object localVarPostBody = createSpeechRequest;
    
    // verify the required parameter 'createSpeechRequest' is set
    if (createSpeechRequest == null) {
      throw new ApiException(400, "Missing the required parameter 'createSpeechRequest' when calling createSpeech");
    }
    
    // create path and map variables
    String localVarPath = "/audio/speech";

    StringJoiner localVarQueryStringJoiner = new StringJoiner("&");
    String localVarQueryParameterBaseName;
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    
    localVarHeaderParams.putAll(additionalHeaders);

    
    
    final String[] localVarAccepts = {
      "application/octet-stream"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "application/json"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "ApiKeyAuth" };

    TypeReference<File> localVarReturnType = new TypeReference<File>() {};
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

  /**
   * Transcribes audio into the input language.
   * 
   * @param _file The audio file object (not file name) to transcribe, in one of these formats: flac, mp3, mp4, mpeg, mpga, m4a, ogg, wav, or webm.  (required)
   * @param model  (required)
   * @param language The language of the input audio. Supplying the input language in [ISO-639-1](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) format will improve accuracy and latency.  (optional)
   * @param prompt An optional text to guide the model&#39;s style or continue a previous audio segment. The [prompt](/docs/guides/speech-to-text/prompting) should match the audio language.  (optional)
   * @param responseFormat The format of the transcript output, in one of these options: &#x60;json&#x60;, &#x60;text&#x60;, &#x60;srt&#x60;, &#x60;verbose_json&#x60;, or &#x60;vtt&#x60;.  (optional, default to json)
   * @param temperature The sampling temperature, between 0 and 1. Higher values like 0.8 will make the output more random, while lower values like 0.2 will make it more focused and deterministic. If set to 0, the model will use [log probability](https://en.wikipedia.org/wiki/Log_probability) to automatically increase the temperature until certain thresholds are hit.  (optional, default to 0)
   * @param timestampGranularities The timestamp granularities to populate for this transcription. &#x60;response_format&#x60; must be set &#x60;verbose_json&#x60; to use timestamp granularities. Either or both of these options are supported: &#x60;word&#x60;, or &#x60;segment&#x60;. Note: There is no additional latency for segment timestamps, but generating word timestamps incurs additional latency.  (optional
   * @return CreateTranscription200Response
   * @throws ApiException if fails to make API call
   */
  public CreateTranscription200Response createTranscription(File _file, CreateTranscriptionRequestModel model, String language, String prompt, String responseFormat, BigDecimal temperature, List<String> timestampGranularities) throws ApiException {
    return this.createTranscription(_file, model, language, prompt, responseFormat, temperature, timestampGranularities, Collections.emptyMap());
  }


  /**
   * Transcribes audio into the input language.
   * 
   * @param _file The audio file object (not file name) to transcribe, in one of these formats: flac, mp3, mp4, mpeg, mpga, m4a, ogg, wav, or webm.  (required)
   * @param model  (required)
   * @param language The language of the input audio. Supplying the input language in [ISO-639-1](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) format will improve accuracy and latency.  (optional)
   * @param prompt An optional text to guide the model&#39;s style or continue a previous audio segment. The [prompt](/docs/guides/speech-to-text/prompting) should match the audio language.  (optional)
   * @param responseFormat The format of the transcript output, in one of these options: &#x60;json&#x60;, &#x60;text&#x60;, &#x60;srt&#x60;, &#x60;verbose_json&#x60;, or &#x60;vtt&#x60;.  (optional, default to json)
   * @param temperature The sampling temperature, between 0 and 1. Higher values like 0.8 will make the output more random, while lower values like 0.2 will make it more focused and deterministic. If set to 0, the model will use [log probability](https://en.wikipedia.org/wiki/Log_probability) to automatically increase the temperature until certain thresholds are hit.  (optional, default to 0)
   * @param timestampGranularities The timestamp granularities to populate for this transcription. &#x60;response_format&#x60; must be set &#x60;verbose_json&#x60; to use timestamp granularities. Either or both of these options are supported: &#x60;word&#x60;, or &#x60;segment&#x60;. Note: There is no additional latency for segment timestamps, but generating word timestamps incurs additional latency.  (optional
   * @param additionalHeaders additionalHeaders for this call
   * @return CreateTranscription200Response
   * @throws ApiException if fails to make API call
   */
  public CreateTranscription200Response createTranscription(File _file, CreateTranscriptionRequestModel model, String language, String prompt, String responseFormat, BigDecimal temperature, List<String> timestampGranularities, Map<String, String> additionalHeaders) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter '_file' is set
    if (_file == null) {
      throw new ApiException(400, "Missing the required parameter '_file' when calling createTranscription");
    }
    
    // verify the required parameter 'model' is set
    if (model == null) {
      throw new ApiException(400, "Missing the required parameter 'model' when calling createTranscription");
    }
    
    // create path and map variables
    String localVarPath = "/audio/transcriptions";

    StringJoiner localVarQueryStringJoiner = new StringJoiner("&");
    String localVarQueryParameterBaseName;
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    
    localVarHeaderParams.putAll(additionalHeaders);

    
    if (_file != null)
      localVarFormParams.put("file", _file);
if (model != null)
      localVarFormParams.put("model", model);
if (language != null)
      localVarFormParams.put("language", language);
if (prompt != null)
      localVarFormParams.put("prompt", prompt);
if (responseFormat != null)
      localVarFormParams.put("response_format", responseFormat);
if (temperature != null)
      localVarFormParams.put("temperature", temperature);
if (timestampGranularities != null)
      localVarFormParams.put("timestamp_granularities[]", timestampGranularities);

    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "multipart/form-data"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "ApiKeyAuth" };

    TypeReference<CreateTranscription200Response> localVarReturnType = new TypeReference<CreateTranscription200Response>() {};
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

  /**
   * Translates audio into English.
   * 
   * @param _file The audio file object (not file name) translate, in one of these formats: flac, mp3, mp4, mpeg, mpga, m4a, ogg, wav, or webm.  (required)
   * @param model  (required)
   * @param prompt An optional text to guide the model&#39;s style or continue a previous audio segment. The [prompt](/docs/guides/speech-to-text/prompting) should be in English.  (optional)
   * @param responseFormat The format of the transcript output, in one of these options: &#x60;json&#x60;, &#x60;text&#x60;, &#x60;srt&#x60;, &#x60;verbose_json&#x60;, or &#x60;vtt&#x60;.  (optional, default to json)
   * @param temperature The sampling temperature, between 0 and 1. Higher values like 0.8 will make the output more random, while lower values like 0.2 will make it more focused and deterministic. If set to 0, the model will use [log probability](https://en.wikipedia.org/wiki/Log_probability) to automatically increase the temperature until certain thresholds are hit.  (optional, default to 0)
   * @return CreateTranslation200Response
   * @throws ApiException if fails to make API call
   */
  public CreateTranslation200Response createTranslation(File _file, CreateTranscriptionRequestModel model, String prompt, String responseFormat, BigDecimal temperature) throws ApiException {
    return this.createTranslation(_file, model, prompt, responseFormat, temperature, Collections.emptyMap());
  }


  /**
   * Translates audio into English.
   * 
   * @param _file The audio file object (not file name) translate, in one of these formats: flac, mp3, mp4, mpeg, mpga, m4a, ogg, wav, or webm.  (required)
   * @param model  (required)
   * @param prompt An optional text to guide the model&#39;s style or continue a previous audio segment. The [prompt](/docs/guides/speech-to-text/prompting) should be in English.  (optional)
   * @param responseFormat The format of the transcript output, in one of these options: &#x60;json&#x60;, &#x60;text&#x60;, &#x60;srt&#x60;, &#x60;verbose_json&#x60;, or &#x60;vtt&#x60;.  (optional, default to json)
   * @param temperature The sampling temperature, between 0 and 1. Higher values like 0.8 will make the output more random, while lower values like 0.2 will make it more focused and deterministic. If set to 0, the model will use [log probability](https://en.wikipedia.org/wiki/Log_probability) to automatically increase the temperature until certain thresholds are hit.  (optional, default to 0)
   * @param additionalHeaders additionalHeaders for this call
   * @return CreateTranslation200Response
   * @throws ApiException if fails to make API call
   */
  public CreateTranslation200Response createTranslation(File _file, CreateTranscriptionRequestModel model, String prompt, String responseFormat, BigDecimal temperature, Map<String, String> additionalHeaders) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter '_file' is set
    if (_file == null) {
      throw new ApiException(400, "Missing the required parameter '_file' when calling createTranslation");
    }
    
    // verify the required parameter 'model' is set
    if (model == null) {
      throw new ApiException(400, "Missing the required parameter 'model' when calling createTranslation");
    }
    
    // create path and map variables
    String localVarPath = "/audio/translations";

    StringJoiner localVarQueryStringJoiner = new StringJoiner("&");
    String localVarQueryParameterBaseName;
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    
    localVarHeaderParams.putAll(additionalHeaders);

    
    if (_file != null)
      localVarFormParams.put("file", _file);
if (model != null)
      localVarFormParams.put("model", model);
if (prompt != null)
      localVarFormParams.put("prompt", prompt);
if (responseFormat != null)
      localVarFormParams.put("response_format", responseFormat);
if (temperature != null)
      localVarFormParams.put("temperature", temperature);

    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "multipart/form-data"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "ApiKeyAuth" };

    TypeReference<CreateTranslation200Response> localVarReturnType = new TypeReference<CreateTranslation200Response>() {};
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
