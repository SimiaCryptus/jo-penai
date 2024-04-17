


```json
{
    "fullyEncodeReservedExpansion": true,
    "revision": "20240410",
    "documentationLink": "https://developers.generativeai.google/api",
    "resources": {
        "operations": {
            "methods": {
                "list": {
                    "id": "generativelanguage.operations.list",
                    "response": {
                        "$ref": "ListOperationsResponse"
                    },
                    "path": "v1/{+name}",
                    "parameterOrder": [
                        "name"
                    ],
                    "description": "Lists operations that match the specified filter in the request. If the server doesn't support this method, it returns `UNIMPLEMENTED`.",
                    "flatPath": "v1/operations",
                    "httpMethod": "GET",
                    "parameters": {
                        "pageToken": {
                            "description": "The standard list page token.",
                            "location": "query",
                            "type": "string"
                        },
                        "pageSize": {
                            "description": "The standard list page size.",
                            "type": "integer",
                            "location": "query",
                            "format": "int32"
                        },
                        "name": {
                            "description": "The name of the operation's parent resource.",
                            "type": "string",
                            "required": true,
                            "pattern": "^operations$",
                            "location": "path"
                        },
                        "filter": {
                            "type": "string",
                            "location": "query",
                            "description": "The standard list filter."
                        }
                    }
                },
                "delete": {
                    "httpMethod": "DELETE",
                    "parameters": {
                        "name": {
                            "type": "string",
                            "pattern": "^operations/.*$",
                            "required": true,
                            "description": "The name of the operation resource to be deleted.",
                            "location": "path"
                        }
                    },
                    "response": {
                        "$ref": "Empty"
                    },
                    "id": "generativelanguage.operations.delete",
                    "path": "v1/{+name}",
                    "parameterOrder": [
                        "name"
                    ],
                    "flatPath": "v1/operations/{operationsId}",
                    "description": "Deletes a long-running operation. This method indicates that the client is no longer interested in the operation result. It does not cancel the operation. If the server doesn't support this method, it returns `google.rpc.Code.UNIMPLEMENTED`."
                }
            }
        },
        "tunedModels": {
            "resources": {
                "operations": {
                    "methods": {
                        "list": {
                            "response": {
                                "$ref": "ListOperationsResponse"
                            },
                            "id": "generativelanguage.tunedModels.operations.list",
                            "httpMethod": "GET",
                            "parameterOrder": [
                                "name"
                            ],
                            "parameters": {
                                "pageToken": {
                                    "type": "string",
                                    "location": "query",
                                    "description": "The standard list page token."
                                },
                                "filter": {
                                    "location": "query",
                                    "description": "The standard list filter.",
                                    "type": "string"
                                },
                                "pageSize": {
                                    "type": "integer",
                                    "format": "int32",
                                    "location": "query",
                                    "description": "The standard list page size."
                                },
                                "name": {
                                    "required": true,
                                    "description": "The name of the operation's parent resource.",
                                    "location": "path",
                                    "pattern": "^tunedModels/[^/]+$",
                                    "type": "string"
                                }
                            },
                            "path": "v1/{+name}/operations",
                            "description": "Lists operations that match the specified filter in the request. If the server doesn't support this method, it returns `UNIMPLEMENTED`.",
                            "flatPath": "v1/tunedModels/{tunedModelsId}/operations"
                        },
                        "cancel": {
                            "httpMethod": "POST",
                            "flatPath": "v1/tunedModels/{tunedModelsId}/operations/{operationsId}:cancel",
                            "description": "Starts asynchronous cancellation on a long-running operation. The server makes a best effort to cancel the operation, but success is not guaranteed. If the server doesn't support this method, it returns `google.rpc.Code.UNIMPLEMENTED`. Clients can use Operations.GetOperation or other methods to check whether the cancellation succeeded or whether the operation completed despite cancellation. On successful cancellation, the operation is not deleted; instead, it becomes an operation with an Operation.error value with a google.rpc.Status.code of 1, corresponding to `Code.CANCELLED`.",
                            "parameterOrder": [
                                "name"
                            ],
                            "id": "generativelanguage.tunedModels.operations.cancel",
                            "response": {
                                "$ref": "Empty"
                            },
                            "request": {
                                "$ref": "CancelOperationRequest"
                            },
                            "parameters": {
                                "name": {
                                    "location": "path",
                                    "pattern": "^tunedModels/[^/]+/operations/[^/]+$",
                                    "required": true,
                                    "type": "string",
                                    "description": "The name of the operation resource to be cancelled."
                                }
                            },
                            "path": "v1/{+name}:cancel"
                        },
                        "get": {
                            "id": "generativelanguage.tunedModels.operations.get",
                            "response": {
                                "$ref": "Operation"
                            },
                            "flatPath": "v1/tunedModels/{tunedModelsId}/operations/{operationsId}",
                            "httpMethod": "GET",
                            "description": "Gets the latest state of a long-running operation. Clients can use this method to poll the operation result at intervals as recommended by the API service.",
                            "path": "v1/{+name}",
                            "parameterOrder": [
                                "name"
                            ],
                            "parameters": {
                                "name": {
                                    "description": "The name of the operation resource.",
                                    "required": true,
                                    "type": "string",
                                    "pattern": "^tunedModels/[^/]+/operations/[^/]+$",
                                    "location": "path"
                                }
                            }
                        }
                    }
                }
            },
            "methods": {
                "generateContent": {
                    "description": "Generates a response from the model given an input `GenerateContentRequest`.",
                    "flatPath": "v1/tunedModels/{tunedModelsId}:generateContent",
                    "path": "v1/{+model}:generateContent",
                    "httpMethod": "POST",
                    "id": "generativelanguage.tunedModels.generateContent",
                    "parameters": {
                        "model": {
                            "pattern": "^tunedModels/[^/]+$",
                            "type": "string",
                            "description": "Required. The name of the `Model` to use for generating the completion. Format: `name=models/{model}`.",
                            "location": "path",
                            "required": true
                        }
                    },
                    "request": {
                        "$ref": "GenerateContentRequest"
                    },
                    "response": {
                        "$ref": "GenerateContentResponse"
                    },
                    "parameterOrder": [
                        "model"
                    ]
                }
            }
        },
        "models": {
            "methods": {
                "countTokens": {
                    "parameterOrder": [
                        "model"
                    ],
                    "request": {
                        "$ref": "CountTokensRequest"
                    },
                    "response": {
                        "$ref": "CountTokensResponse"
                    },
                    "flatPath": "v1/models/{modelsId}:countTokens",
                    "id": "generativelanguage.models.countTokens",
                    "path": "v1/{+model}:countTokens",
                    "description": "Runs a model's tokenizer on input content and returns the token count.",
                    "httpMethod": "POST",
                    "parameters": {
                        "model": {
                            "pattern": "^models/[^/]+$",
                            "required": true,
                            "description": "Required. The model's resource name. This serves as an ID for the Model to use. This name should match a model name returned by the `ListModels` method. Format: `models/{model}`",
                            "type": "string",
                            "location": "path"
                        }
                    }
                },
                "batchEmbedContents": {
                    "response": {
                        "$ref": "BatchEmbedContentsResponse"
                    },
                    "description": "Generates multiple embeddings from the model given input text in a synchronous call.",
                    "id": "generativelanguage.models.batchEmbedContents",
                    "httpMethod": "POST",
                    "path": "v1/{+model}:batchEmbedContents",
                    "flatPath": "v1/models/{modelsId}:batchEmbedContents",
                    "parameters": {
                        "model": {
                            "description": "Required. The model's resource name. This serves as an ID for the Model to use. This name should match a model name returned by the `ListModels` method. Format: `models/{model}`",
                            "type": "string",
                            "required": true,
                            "location": "path",
                            "pattern": "^models/[^/]+$"
                        }
                    },
                    "parameterOrder": [
                        "model"
                    ],
                    "request": {
                        "$ref": "BatchEmbedContentsRequest"
                    }
                },
                "embedContent": {
                    "parameterOrder": [
                        "model"
                    ],
                    "description": "Generates an embedding from the model given an input `Content`.",
                    "flatPath": "v1/models/{modelsId}:embedContent",
                    "id": "generativelanguage.models.embedContent",
                    "httpMethod": "POST",
                    "path": "v1/{+model}:embedContent",
                    "response": {
                        "$ref": "EmbedContentResponse"
                    },
                    "parameters": {
                        "model": {
                            "pattern": "^models/[^/]+$",
                            "description": "Required. The model's resource name. This serves as an ID for the Model to use. This name should match a model name returned by the `ListModels` method. Format: `models/{model}`",
                            "type": "string",
                            "required": true,
                            "location": "path"
                        }
                    },
                    "request": {
                        "$ref": "EmbedContentRequest"
                    }
                },
                "get": {
                    "path": "v1/{+name}",
                    "response": {
                        "$ref": "Model"
                    },
                    "id": "generativelanguage.models.get",
                    "description": "Gets information about a specific Model.",
                    "httpMethod": "GET",
                    "parameters": {
                        "name": {
                            "description": "Required. The resource name of the model. This name should match a model name returned by the `ListModels` method. Format: `models/{model}`",
                            "location": "path",
                            "type": "string",
                            "pattern": "^models/[^/]+$",
                            "required": true
                        }
                    },
                    "flatPath": "v1/models/{modelsId}",
                    "parameterOrder": [
                        "name"
                    ]
                },
                "list": {
                    "id": "generativelanguage.models.list",
                    "response": {
                        "$ref": "ListModelsResponse"
                    },
                    "parameterOrder": [],
                    "httpMethod": "GET",
                    "path": "v1/models",
                    "parameters": {
                        "pageToken": {
                            "type": "string",
                            "location": "query",
                            "description": "A page token, received from a previous `ListModels` call. Provide the `page_token` returned by one request as an argument to the next request to retrieve the next page. When paginating, all other parameters provided to `ListModels` must match the call that provided the page token."
                        },
                        "pageSize": {
                            "type": "integer",
                            "description": "The maximum number of `Models` to return (per page). The service may return fewer models. If unspecified, at most 50 models will be returned per page. This method returns at most 1000 models per page, even if you pass a larger page_size.",
                            "format": "int32",
                            "location": "query"
                        }
                    },
                    "flatPath": "v1/models",
                    "description": "Lists models available through the API."
                },
                "streamGenerateContent": {
                    "request": {
                        "$ref": "GenerateContentRequest"
                    },
                    "parameterOrder": [
                        "model"
                    ],
                    "description": "Generates a streamed response from the model given an input `GenerateContentRequest`.",
                    "flatPath": "v1/models/{modelsId}:streamGenerateContent",
                    "response": {
                        "$ref": "GenerateContentResponse"
                    },
                    "httpMethod": "POST",
                    "path": "v1/{+model}:streamGenerateContent",
                    "parameters": {
                        "model": {
                            "required": true,
                            "location": "path",
                            "pattern": "^models/[^/]+$",
                            "type": "string",
                            "description": "Required. The name of the `Model` to use for generating the completion. Format: `name=models/{model}`."
                        }
                    },
                    "id": "generativelanguage.models.streamGenerateContent"
                },
                "generateContent": {
                    "description": "Generates a response from the model given an input `GenerateContentRequest`.",
                    "request": {
                        "$ref": "GenerateContentRequest"
                    },
                    "response": {
                        "$ref": "GenerateContentResponse"
                    },
                    "flatPath": "v1/models/{modelsId}:generateContent",
                    "path": "v1/{+model}:generateContent",
                    "httpMethod": "POST",
                    "parameters": {
                        "model": {
                            "type": "string",
                            "pattern": "^models/[^/]+$",
                            "location": "path",
                            "required": true,
                            "description": "Required. The name of the `Model` to use for generating the completion. Format: `name=models/{model}`."
                        }
                    },
                    "parameterOrder": [
                        "model"
                    ],
                    "id": "generativelanguage.models.generateContent"
                }
            }
        }
    },
    "basePath": "",
    "id": "generativelanguage:v1",
    "discoveryVersion": "v1",
    "rootUrl": "https://generativelanguage.googleapis.com/",
    "ownerName": "Google",
    "version_module": true,
    "ownerDomain": "google.com",
    "servicePath": "",
    "canonicalName": "Generative Language",
    "kind": "discovery#restDescription",
    "parameters": {
        "upload_protocol": {
            "description": "Upload protocol for media (e.g. \"raw\", \"multipart\").",
            "location": "query",
            "type": "string"
        },
        "callback": {
            "type": "string",
            "description": "JSONP",
            "location": "query"
        },
        "$.xgafv": {
            "enum": [
                "1",
                "2"
            ],
            "description": "V1 error format.",
            "type": "string",
            "location": "query",
            "enumDescriptions": [
                "v1 error format",
                "v2 error format"
            ]
        },
        "uploadType": {
            "description": "Legacy upload protocol for media (e.g. \"media\", \"multipart\").",
            "type": "string",
            "location": "query"
        },
        "oauth_token": {
            "type": "string",
            "description": "OAuth 2.0 token for the current user.",
            "location": "query"
        },
        "alt": {
            "type": "string",
            "default": "json",
            "enumDescriptions": [
                "Responses with Content-Type of application/json",
                "Media download with context-dependent Content-Type",
                "Responses with Content-Type of application/x-protobuf"
            ],
            "enum": [
                "json",
                "media",
                "proto"
            ],
            "location": "query",
            "description": "Data format for response."
        },
        "key": {
            "type": "string",
            "description": "API key. Your API key identifies your project and provides you with API access, quota, and reports. Required unless you provide an OAuth 2.0 token.",
            "location": "query"
        },
        "prettyPrint": {
            "location": "query",
            "description": "Returns response with indentations and line breaks.",
            "type": "boolean",
            "default": "true"
        },
        "fields": {
            "location": "query",
            "type": "string",
            "description": "Selector specifying which fields to include in a partial response."
        },
        "quotaUser": {
            "location": "query",
            "type": "string",
            "description": "Available to use for quota purposes for server-side applications. Can be any arbitrary string assigned to a user, but should not exceed 40 characters."
        },
        "access_token": {
            "description": "OAuth access token.",
            "type": "string",
            "location": "query"
        }
    },
    "description": "The Gemini API allows developers to build generative AI applications using Gemini models. Gemini is our most capable model, built from the ground up to be multimodal. It can generalize and seamlessly understand, operate across, and combine different types of information. including language, images, audio, video, and code. You can use the Gemini API for use cases like reasoning across text and images, content generation, dialogue agents, summarization and classification systems, and more.",
    "icons": {
        "x16": "http://www.google.com/images/icons/product/search-16.gif",
        "x32": "http://www.google.com/images/icons/product/search-32.gif"
    },
    "mtlsRootUrl": "https://generativelanguage.mtls.googleapis.com/",
    "protocol": "rest",
    "version": "v1",
    "title": "Generative Language API",
    "name": "generativelanguage",
    "schemas": {
        "CreateTunedModelMetadata": {
            "type": "object",
            "properties": {
                "snapshots": {
                    "description": "Metrics collected during tuning.",
                    "type": "array",
                    "items": {
                        "$ref": "TuningSnapshot"
                    }
                },
                "tunedModel": {
                    "type": "string",
                    "description": "Name of the tuned model associated with the tuning operation."
                },
                "completedPercent": {
                    "type": "number",
                    "format": "float",
                    "description": "The completed percentage for the tuning operation."
                },
                "totalSteps": {
                    "type": "integer",
                    "description": "The total number of tuning steps.",
                    "format": "int32"
                },
                "completedSteps": {
                    "type": "integer",
                    "description": "The number of steps completed.",
                    "format": "int32"
                }
            },
            "description": "Metadata about the state and progress of creating a tuned model returned from the long-running operation",
            "id": "CreateTunedModelMetadata"
        },
        "Blob": {
            "description": "Raw media bytes. Text should not be sent as raw bytes, use the 'text' field.",
            "id": "Blob",
            "properties": {
                "mimeType": {
                    "type": "string",
                    "description": "The IANA standard MIME type of the source data. Accepted types include: \"image/png\", \"image/jpeg\", \"image/heic\", \"image/heif\", \"image/webp\"."
                },
                "data": {
                    "type": "string",
                    "description": "Raw bytes for media formats.",
                    "format": "byte"
                }
            },
            "type": "object"
        },
        "CitationMetadata": {
            "description": "A collection of source attributions for a piece of content.",
            "type": "object",
            "id": "CitationMetadata",
            "properties": {
                "citationSources": {
                    "type": "array",
                    "items": {
                        "$ref": "CitationSource"
                    },
                    "description": "Citations to sources for a specific response."
                }
            }
        },
        "GenerateContentResponse": {
            "description": "Response from the model supporting multiple candidates. Note on safety ratings and content filtering. They are reported for both prompt in `GenerateContentResponse.prompt_feedback` and for each candidate in `finish_reason` and in `safety_ratings`. The API contract is that: - either all requested candidates are returned or no candidates at all - no candidates are returned only if there was something wrong with the prompt (see `prompt_feedback`) - feedback on each candidate is reported on `finish_reason` and `safety_ratings`.",
            "type": "object",
            "id": "GenerateContentResponse",
            "properties": {
                "promptFeedback": {
                    "description": "Returns the prompt's feedback related to the content filters.",
                    "$ref": "PromptFeedback"
                },
                "candidates": {
                    "description": "Candidate responses from the model.",
                    "items": {
                        "$ref": "Candidate"
                    },
                    "type": "array"
                }
            }
        },
        "ListModelsResponse": {
            "description": "Response from `ListModel` containing a paginated list of Models.",
            "properties": {
                "nextPageToken": {
                    "description": "A token, which can be sent as `page_token` to retrieve the next page. If this field is omitted, there are no more pages.",
                    "type": "string"
                },
                "models": {
                    "description": "The returned Models.",
                    "type": "array",
                    "items": {
                        "$ref": "Model"
                    }
                }
            },
            "type": "object",
            "id": "ListModelsResponse"
        },
        "GenerateContentRequest": {
            "properties": {
                "contents": {
                    "items": {
                        "$ref": "Content"
                    },
                    "type": "array",
                    "description": "Required. The content of the current conversation with the model. For single-turn queries, this is a single instance. For multi-turn queries, this is a repeated field that contains conversation history + latest request."
                },
                "safetySettings": {
                    "description": "Optional. A list of unique `SafetySetting` instances for blocking unsafe content. This will be enforced on the `GenerateContentRequest.contents` and `GenerateContentResponse.candidates`. There should not be more than one setting for each `SafetyCategory` type. The API will block any contents and responses that fail to meet the thresholds set by these settings. This list overrides the default settings for each `SafetyCategory` specified in the safety_settings. If there is no `SafetySetting` for a given `SafetyCategory` provided in the list, the API will use the default safety setting for that category. Harm categories HARM_CATEGORY_HATE_SPEECH, HARM_CATEGORY_SEXUALLY_EXPLICIT, HARM_CATEGORY_DANGEROUS_CONTENT, HARM_CATEGORY_HARASSMENT are supported.",
                    "type": "array",
                    "items": {
                        "$ref": "SafetySetting"
                    }
                },
                "generationConfig": {
                    "$ref": "GenerationConfig",
                    "description": "Optional. Configuration options for model generation and outputs."
                }
            },
            "description": "Request to generate a completion from the model.",
            "type": "object",
            "id": "GenerateContentRequest"
        },
        "Part": {
            "description": "A datatype containing media that is part of a multi-part `Content` message. A `Part` consists of data which has an associated datatype. A `Part` can only contain one of the accepted types in `Part.data`. A `Part` must have a fixed IANA MIME type identifying the type and subtype of the media if the `inline_data` field is filled with raw bytes.",
            "id": "Part",
            "properties": {
                "inlineData": {
                    "$ref": "Blob",
                    "description": "Inline media bytes."
                },
                "text": {
                    "description": "Inline text.",
                    "type": "string"
                }
            },
            "type": "object"
        },
        "Empty": {
            "type": "object",
            "id": "Empty",
            "properties": {},
            "description": "A generic empty message that you can re-use to avoid defining duplicated empty messages in your APIs. A typical example is to use it as the request or the response type of an API method. For instance: service Foo { rpc Bar(google.protobuf.Empty) returns (google.protobuf.Empty); }"
        },
        "BatchEmbedContentsResponse": {
            "id": "BatchEmbedContentsResponse",
            "description": "The response to a `BatchEmbedContentsRequest`.",
            "type": "object",
            "properties": {
                "embeddings": {
                    "description": "Output only. The embeddings for each request, in the same order as provided in the batch request.",
                    "items": {
                        "$ref": "ContentEmbedding"
                    },
                    "readOnly": true,
                    "type": "array"
                }
            }
        },
        "EmbedContentRequest": {
            "id": "EmbedContentRequest",
            "description": "Request containing the `Content` for the model to embed.",
            "type": "object",
            "properties": {
                "model": {
                    "type": "string",
                    "description": "Required. The model's resource name. This serves as an ID for the Model to use. This name should match a model name returned by the `ListModels` method. Format: `models/{model}`"
                },
                "title": {
                    "description": "Optional. An optional title for the text. Only applicable when TaskType is `RETRIEVAL_DOCUMENT`. Note: Specifying a `title` for `RETRIEVAL_DOCUMENT` provides better quality embeddings for retrieval.",
                    "type": "string"
                },
                "outputDimensionality": {
                    "format": "int32",
                    "type": "integer",
                    "description": "Optional. Optional reduced dimension for the output embedding. If set, excessive values in the output embedding are truncated from the end. Supported by newer models since 2024, and the earlier model (`models/embedding-001`) cannot specify this value."
                },
                "content": {
                    "$ref": "Content",
                    "description": "Required. The content to embed. Only the `parts.text` fields will be counted."
                },
                "taskType": {
                    "enumDescriptions": [
                        "Unset value, which will default to one of the other enum values.",
                        "Specifies the given text is a query in a search/retrieval setting.",
                        "Specifies the given text is a document from the corpus being searched.",
                        "Specifies the given text will be used for STS.",
                        "Specifies that the given text will be classified.",
                        "Specifies that the embeddings will be used for clustering.",
                        "Specifies that the given text will be used for question answering.",
                        "Specifies that the given text will be used for fact verification."
                    ],
                    "enum": [
                        "TASK_TYPE_UNSPECIFIED",
                        "RETRIEVAL_QUERY",
                        "RETRIEVAL_DOCUMENT",
                        "SEMANTIC_SIMILARITY",
                        "CLASSIFICATION",
                        "CLUSTERING",
                        "QUESTION_ANSWERING",
                        "FACT_VERIFICATION"
                    ],
                    "description": "Optional. Optional task type for which the embeddings will be used. Can only be set for `models/embedding-001`.",
                    "type": "string"
                }
            }
        },
        "SafetySetting": {
            "description": "Safety setting, affecting the safety-blocking behavior. Passing a safety setting for a category changes the allowed proability that content is blocked.",
            "id": "SafetySetting",
            "type": "object",
            "properties": {
                "threshold": {
                    "enum": [
                        "HARM_BLOCK_THRESHOLD_UNSPECIFIED",
                        "BLOCK_LOW_AND_ABOVE",
                        "BLOCK_MEDIUM_AND_ABOVE",
                        "BLOCK_ONLY_HIGH",
                        "BLOCK_NONE"
                    ],
                    "description": "Required. Controls the probability threshold at which harm is blocked.",
                    "enumDescriptions": [
                        "Threshold is unspecified.",
                        "Content with NEGLIGIBLE will be allowed.",
                        "Content with NEGLIGIBLE and LOW will be allowed.",
                        "Content with NEGLIGIBLE, LOW, and MEDIUM will be allowed.",
                        "All content will be allowed."
                    ],
                    "type": "string"
                },
                "category": {
                    "enumDescriptions": [
                        "Category is unspecified.",
                        "Negative or harmful comments targeting identity and/or protected attribute.",
                        "Content that is rude, disrespectful, or profane.",
                        "Describes scenarios depicting violence against an individual or group, or general descriptions of gore.",
                        "Contains references to sexual acts or other lewd content.",
                        "Promotes unchecked medical advice.",
                        "Dangerous content that promotes, facilitates, or encourages harmful acts.",
                        "Harasment content.",
                        "Hate speech and content.",
                        "Sexually explicit content.",
                        "Dangerous content."
                    ],
                    "enum": [
                        "HARM_CATEGORY_UNSPECIFIED",
                        "HARM_CATEGORY_DEROGATORY",
                        "HARM_CATEGORY_TOXICITY",
                        "HARM_CATEGORY_VIOLENCE",
                        "HARM_CATEGORY_SEXUAL",
                        "HARM_CATEGORY_MEDICAL",
                        "HARM_CATEGORY_DANGEROUS",
                        "HARM_CATEGORY_HARASSMENT",
                        "HARM_CATEGORY_HATE_SPEECH",
                        "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                        "HARM_CATEGORY_DANGEROUS_CONTENT"
                    ],
                    "type": "string",
                    "description": "Required. The category for this setting."
                }
            }
        },
        "CitationSource": {
            "description": "A citation to a source for a portion of a specific response.",
            "id": "CitationSource",
            "type": "object",
            "properties": {
                "uri": {
                    "description": "Optional. URI that is attributed as a source for a portion of the text.",
                    "type": "string"
                },
                "endIndex": {
                    "type": "integer",
                    "description": "Optional. End of the attributed segment, exclusive.",
                    "format": "int32"
                },
                "license": {
                    "description": "Optional. License for the GitHub project that is attributed as a source for segment. License info is required for code citations.",
                    "type": "string"
                },
                "startIndex": {
                    "format": "int32",
                    "description": "Optional. Start of segment of the response that is attributed to this source. Index indicates the start of the segment, measured in bytes.",
                    "type": "integer"
                }
            }
        },
        "Status": {
            "id": "Status",
            "description": "The `Status` type defines a logical error model that is suitable for different programming environments, including REST APIs and RPC APIs. It is used by [gRPC](https://github.com/grpc). Each `Status` message contains three pieces of data: error code, error message, and error details. You can find out more about this error model and how to work with it in the [API Design Guide](https://cloud.google.com/apis/design/errors).",
            "type": "object",
            "properties": {
                "message": {
                    "type": "string",
                    "description": "A developer-facing error message, which should be in English. Any user-facing error message should be localized and sent in the google.rpc.Status.details field, or localized by the client."
                },
                "details": {
                    "items": {
                        "type": "object",
                        "additionalProperties": {
                            "type": "any",
                            "description": "Properties of the object. Contains field @type with type URL."
                        }
                    },
                    "type": "array",
                    "description": "A list of messages that carry the error details. There is a common set of message types for APIs to use."
                },
                "code": {
                    "format": "int32",
                    "description": "The status code, which should be an enum value of google.rpc.Code.",
                    "type": "integer"
                }
            }
        },
        "Candidate": {
            "description": "A response candidate generated from the model.",
            "id": "Candidate",
            "type": "object",
            "properties": {
                "safetyRatings": {
                    "type": "array",
                    "items": {
                        "$ref": "SafetyRating"
                    },
                    "description": "List of ratings for the safety of a response candidate. There is at most one rating per category."
                },
                "content": {
                    "readOnly": true,
                    "$ref": "Content",
                    "description": "Output only. Generated content returned from the model."
                },
                "tokenCount": {
                    "format": "int32",
                    "readOnly": true,
                    "description": "Output only. Token count for this candidate.",
                    "type": "integer"
                },
                "citationMetadata": {
                    "description": "Output only. Citation information for model-generated candidate. This field may be populated with recitation information for any text included in the `content`. These are passages that are \"recited\" from copyrighted material in the foundational LLM's training data.",
                    "$ref": "CitationMetadata",
                    "readOnly": true
                },
                "index": {
                    "format": "int32",
                    "type": "integer",
                    "description": "Output only. Index of the candidate in the list of candidates.",
                    "readOnly": true
                },
                "finishReason": {
                    "type": "string",
                    "enumDescriptions": [
                        "Default value. This value is unused.",
                        "Natural stop point of the model or provided stop sequence.",
                        "The maximum number of tokens as specified in the request was reached.",
                        "The candidate content was flagged for safety reasons.",
                        "The candidate content was flagged for recitation reasons.",
                        "Unknown reason."
                    ],
                    "readOnly": true,
                    "enum": [
                        "FINISH_REASON_UNSPECIFIED",
                        "STOP",
                        "MAX_TOKENS",
                        "SAFETY",
                        "RECITATION",
                        "OTHER"
                    ],
                    "description": "Optional. Output only. The reason why the model stopped generating tokens. If empty, the model has not stopped generating the tokens."
                }
            }
        },
        "CountTokensResponse": {
            "id": "CountTokensResponse",
            "type": "object",
            "properties": {
                "totalTokens": {
                    "format": "int32",
                    "description": "The number of tokens that the `model` tokenizes the `prompt` into. Always non-negative.",
                    "type": "integer"
                }
            },
            "description": "A response from `CountTokens`. It returns the model's `token_count` for the `prompt`."
        },
        "SafetyRating": {
            "properties": {
                "blocked": {
                    "type": "boolean",
                    "description": "Was this content blocked because of this rating?"
                },
                "probability": {
                    "enum": [
                        "HARM_PROBABILITY_UNSPECIFIED",
                        "NEGLIGIBLE",
                        "LOW",
                        "MEDIUM",
                        "HIGH"
                    ],
                    "type": "string",
                    "enumDescriptions": [
                        "Probability is unspecified.",
                        "Content has a negligible chance of being unsafe.",
                        "Content has a low chance of being unsafe.",
                        "Content has a medium chance of being unsafe.",
                        "Content has a high chance of being unsafe."
                    ],
                    "description": "Required. The probability of harm for this content."
                },
                "category": {
                    "description": "Required. The category for this rating.",
                    "enumDescriptions": [
                        "Category is unspecified.",
                        "Negative or harmful comments targeting identity and/or protected attribute.",
                        "Content that is rude, disrespectful, or profane.",
                        "Describes scenarios depicting violence against an individual or group, or general descriptions of gore.",
                        "Contains references to sexual acts or other lewd content.",
                        "Promotes unchecked medical advice.",
                        "Dangerous content that promotes, facilitates, or encourages harmful acts.",
                        "Harasment content.",
                        "Hate speech and content.",
                        "Sexually explicit content.",
                        "Dangerous content."
                    ],
                    "type": "string",
                    "enum": [
                        "HARM_CATEGORY_UNSPECIFIED",
                        "HARM_CATEGORY_DEROGATORY",
                        "HARM_CATEGORY_TOXICITY",
                        "HARM_CATEGORY_VIOLENCE",
                        "HARM_CATEGORY_SEXUAL",
                        "HARM_CATEGORY_MEDICAL",
                        "HARM_CATEGORY_DANGEROUS",
                        "HARM_CATEGORY_HARASSMENT",
                        "HARM_CATEGORY_HATE_SPEECH",
                        "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                        "HARM_CATEGORY_DANGEROUS_CONTENT"
                    ]
                }
            },
            "id": "SafetyRating",
            "type": "object",
            "description": "Safety rating for a piece of content. The safety rating contains the category of harm and the harm probability level in that category for a piece of content. Content is classified for safety across a number of harm categories and the probability of the harm classification is included here."
        },
        "GenerationConfig": {
            "description": "Configuration options for model generation and outputs. Not all parameters may be configurable for every model.",
            "properties": {
                "temperature": {
                    "type": "number",
                    "format": "float",
                    "description": "Optional. Controls the randomness of the output. Note: The default value varies by model, see the `Model.temperature` attribute of the `Model` returned from the `getModel` function. Values can range from [0.0, 2.0]."
                },
                "candidateCount": {
                    "format": "int32",
                    "description": "Optional. Number of generated responses to return. Currently, this value can only be set to 1. If unset, this will default to 1.",
                    "type": "integer"
                },
                "topK": {
                    "type": "integer",
                    "description": "Optional. The maximum number of tokens to consider when sampling. Models use nucleus sampling or combined Top-k and nucleus sampling. Top-k sampling considers the set of `top_k` most probable tokens. Models running with nucleus sampling don't allow top_k setting. Note: The default value varies by model, see the `Model.top_k` attribute of the `Model` returned from the `getModel` function. Empty `top_k` field in `Model` indicates the model doesn't apply top-k sampling and doesn't allow setting `top_k` on requests.",
                    "format": "int32"
                },
                "maxOutputTokens": {
                    "description": "Optional. The maximum number of tokens to include in a candidate. Note: The default value varies by model, see the `Model.output_token_limit` attribute of the `Model` returned from the `getModel` function.",
                    "format": "int32",
                    "type": "integer"
                },
                "topP": {
                    "type": "number",
                    "format": "float",
                    "description": "Optional. The maximum cumulative probability of tokens to consider when sampling. The model uses combined Top-k and nucleus sampling. Tokens are sorted based on their assigned probabilities so that only the most likely tokens are considered. Top-k sampling directly limits the maximum number of tokens to consider, while Nucleus sampling limits number of tokens based on the cumulative probability. Note: The default value varies by model, see the `Model.top_p` attribute of the `Model` returned from the `getModel` function."
                },
                "stopSequences": {
                    "items": {
                        "type": "string"
                    },
                    "type": "array",
                    "description": "Optional. The set of character sequences (up to 5) that will stop output generation. If specified, the API will stop at the first appearance of a stop sequence. The stop sequence will not be included as part of the response."
                }
            },
            "id": "GenerationConfig",
            "type": "object"
        },
        "Operation": {
            "id": "Operation",
            "description": "This resource represents a long-running operation that is the result of a network API call.",
            "properties": {
                "response": {
                    "description": "The normal, successful response of the operation. If the original method returns no data on success, such as `Delete`, the response is `google.protobuf.Empty`. If the original method is standard `Get`/`Create`/`Update`, the response should be the resource. For other methods, the response should have the type `XxxResponse`, where `Xxx` is the original method name. For example, if the original method name is `TakeSnapshot()`, the inferred response type is `TakeSnapshotResponse`.",
                    "additionalProperties": {
                        "type": "any",
                        "description": "Properties of the object. Contains field @type with type URL."
                    },
                    "type": "object"
                },
                "name": {
                    "description": "The server-assigned name, which is only unique within the same service that originally returns it. If you use the default HTTP mapping, the `name` should be a resource name ending with `operations/{unique_id}`.",
                    "type": "string"
                },
                "error": {
                    "$ref": "Status",
                    "description": "The error result of the operation in case of failure or cancellation."
                },
                "metadata": {
                    "description": "Service-specific metadata associated with the operation. It typically contains progress information and common metadata such as create time. Some services might not provide such metadata. Any method that returns a long-running operation should document the metadata type, if any.",
                    "additionalProperties": {
                        "description": "Properties of the object. Contains field @type with type URL.",
                        "type": "any"
                    },
                    "type": "object"
                },
                "done": {
                    "type": "boolean",
                    "description": "If the value is `false`, it means the operation is still in progress. If `true`, the operation is completed, and either `error` or `response` is available."
                }
            },
            "type": "object"
        },
        "ListOperationsResponse": {
            "id": "ListOperationsResponse",
            "type": "object",
            "properties": {
                "nextPageToken": {
                    "type": "string",
                    "description": "The standard List next-page token."
                },
                "operations": {
                    "type": "array",
                    "items": {
                        "$ref": "Operation"
                    },
                    "description": "A list of operations that matches the specified filter in the request."
                }
            },
            "description": "The response message for Operations.ListOperations."
        },
        "Model": {
            "type": "object",
            "description": "Information about a Generative Language Model.",
            "properties": {
                "displayName": {
                    "description": "The human-readable name of the model. E.g. \"Chat Bison\". The name can be up to 128 characters long and can consist of any UTF-8 characters.",
                    "type": "string"
                },
                "baseModelId": {
                    "description": "Required. The name of the base model, pass this to the generation request. Examples: * `chat-bison`",
                    "type": "string"
                },
                "version": {
                    "description": "Required. The version number of the model. This represents the major version",
                    "type": "string"
                },
                "inputTokenLimit": {
                    "type": "integer",
                    "description": "Maximum number of input tokens allowed for this model.",
                    "format": "int32"
                },
                "name": {
                    "description": "Required. The resource name of the `Model`. Format: `models/{model}` with a `{model}` naming convention of: * \"{base_model_id}-{version}\" Examples: * `models/chat-bison-001`",
                    "type": "string"
                },
                "topK": {
                    "type": "integer",
                    "format": "int32",
                    "description": "For Top-k sampling. Top-k sampling considers the set of `top_k` most probable tokens. This value specifies default to be used by the backend while making the call to the model. If empty, indicates the model doesn't use top-k sampling, and `top_k` isn't allowed as a generation parameter."
                },
                "description": {
                    "type": "string",
                    "description": "A short description of the model."
                },
                "supportedGenerationMethods": {
                    "description": "The model's supported generation methods. The method names are defined as Pascal case strings, such as `generateMessage` which correspond to API methods.",
                    "items": {
                        "type": "string"
                    },
                    "type": "array"
                },
                "outputTokenLimit": {
                    "type": "integer",
                    "format": "int32",
                    "description": "Maximum number of output tokens available for this model."
                },
                "topP": {
                    "description": "For Nucleus sampling. Nucleus sampling considers the smallest set of tokens whose probability sum is at least `top_p`. This value specifies default to be used by the backend while making the call to the model.",
                    "type": "number",
                    "format": "float"
                },
                "temperature": {
                    "type": "number",
                    "description": "Controls the randomness of the output. Values can range over `[0.0,1.0]`, inclusive. A value closer to `1.0` will produce responses that are more varied, while a value closer to `0.0` will typically result in less surprising responses from the model. This value specifies default to be used by the backend while making the call to the model.",
                    "format": "float"
                }
            },
            "id": "Model"
        },
        "BatchEmbedContentsRequest": {
            "properties": {
                "requests": {
                    "items": {
                        "$ref": "EmbedContentRequest"
                    },
                    "description": "Required. Embed requests for the batch. The model in each of these requests must match the model specified `BatchEmbedContentsRequest.model`.",
                    "type": "array"
                }
            },
            "id": "BatchEmbedContentsRequest",
            "type": "object",
            "description": "Batch request to get embeddings from the model for a list of prompts."
        },
        "TuningSnapshot": {
            "id": "TuningSnapshot",
            "type": "object",
            "properties": {
                "epoch": {
                    "format": "int32",
                    "readOnly": true,
                    "description": "Output only. The epoch this step was part of.",
                    "type": "integer"
                },
                "step": {
                    "description": "Output only. The tuning step.",
                    "readOnly": true,
                    "format": "int32",
                    "type": "integer"
                },
                "computeTime": {
                    "readOnly": true,
                    "description": "Output only. The timestamp when this metric was computed.",
                    "format": "google-datetime",
                    "type": "string"
                },
                "meanLoss": {
                    "readOnly": true,
                    "description": "Output only. The mean loss of the training examples for this step.",
                    "format": "float",
                    "type": "number"
                }
            },
            "description": "Record for a single tuning step."
        },
        "EmbedContentResponse": {
            "type": "object",
            "properties": {
                "embedding": {
                    "$ref": "ContentEmbedding",
                    "readOnly": true,
                    "description": "Output only. The embedding generated from the input content."
                }
            },
            "id": "EmbedContentResponse",
            "description": "The response to an `EmbedContentRequest`."
        },
        "CancelOperationRequest": {
            "properties": {},
            "type": "object",
            "description": "The request message for Operations.CancelOperation.",
            "id": "CancelOperationRequest"
        },
        "ContentEmbedding": {
            "type": "object",
            "id": "ContentEmbedding",
            "description": "A list of floats representing an embedding.",
            "properties": {
                "values": {
                    "description": "The embedding values.",
                    "items": {
                        "format": "float",
                        "type": "number"
                    },
                    "type": "array"
                }
            }
        },
        "CountTokensRequest": {
            "id": "CountTokensRequest",
            "description": "Counts the number of tokens in the `prompt` sent to a model. Models may tokenize text differently, so each model may return a different `token_count`.",
            "properties": {
                "contents": {
                    "description": "Required. The input given to the model as a prompt.",
                    "type": "array",
                    "items": {
                        "$ref": "Content"
                    }
                }
            },
            "type": "object"
        },
        "Content": {
            "type": "object",
            "properties": {
                "role": {
                    "type": "string",
                    "description": "Optional. The producer of the content. Must be either 'user' or 'model'. Useful to set for multi-turn conversations, otherwise can be left blank or unset."
                },
                "parts": {
                    "description": "Ordered `Parts` that constitute a single message. Parts may have different MIME types.",
                    "type": "array",
                    "items": {
                        "$ref": "Part"
                    }
                }
            },
            "id": "Content",
            "description": "The base structured datatype containing multi-part content of a message. A `Content` includes a `role` field designating the producer of the `Content` and a `parts` field containing multi-part data that contains the content of the message turn."
        },
        "PromptFeedback": {
            "properties": {
                "safetyRatings": {
                    "type": "array",
                    "items": {
                        "$ref": "SafetyRating"
                    },
                    "description": "Ratings for safety of the prompt. There is at most one rating per category."
                },
                "blockReason": {
                    "enum": [
                        "BLOCK_REASON_UNSPECIFIED",
                        "SAFETY",
                        "OTHER"
                    ],
                    "description": "Optional. If set, the prompt was blocked and no candidates are returned. Rephrase your prompt.",
                    "enumDescriptions": [
                        "Default value. This value is unused.",
                        "Prompt was blocked due to safety reasons. You can inspect `safety_ratings` to understand which safety category blocked it.",
                        "Prompt was blocked due to unknown reaasons."
                    ],
                    "type": "string"
                }
            },
            "id": "PromptFeedback",
            "description": "A set of the feedback metadata the prompt specified in `GenerateContentRequest.content`.",
            "type": "object"
        }
    },
    "baseUrl": "https://generativelanguage.googleapis.com/",
    "batchPath": "batch"
}
```