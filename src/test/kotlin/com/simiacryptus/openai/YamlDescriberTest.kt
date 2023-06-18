package com.simiacryptus.util

import com.simiacryptus.TypeDescriberTestBase
import com.simiacryptus.util.describe.TypeDescriber
import com.simiacryptus.util.describe.YamlDescriber

class YamlDescriberTest : TypeDescriberTestBase() {
    override val typeDescriber: TypeDescriber get() = YamlDescriber()
    override val classDescription: String
        get() =
            //language=yaml
            """
            |type: object
            |class: com.simiacryptus.TypeDescriberTestBase${"$"}DataClassExample
            |properties:
            |  a:
            |    description: This is an integer
            |    type: object
            |    class: int
            |  b:
            |    type: object
            |    class: java.lang.String
            |  c:
            |    type: object
            |    class: java.util.List<java.lang.String>
            |  d:
            |    type: object
            |    class: java.util.Map<java.lang.String, java.lang.Integer>
            """.trimMargin()

    override val methodDescription
        get() =
            //language=yaml
            """
            |operationId: methodExample
            |description: This is a method
            |parameters:
            |  - name: p1
            |    description: This is a parameter
            |    type: object
            |    class: int
            |  - name: p2
            |    type: object
            |    class: java.lang.String
            |responses:
            |  application/json:
            |    schema:
            |      type: object
            |      class: java.lang.String
            |
            """.trimMargin()

}
