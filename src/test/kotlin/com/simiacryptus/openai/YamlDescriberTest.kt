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
            |    type: string
            |  c:
            |    type: array
            |    items:
            |      type: string
            |  d:
            |    type: map
            |    keys:
            |      type: string
            |    values:
            |      type: integer
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
            |    type: string
            |responses:
            |  application/json:
            |    schema:
            |      type: string
            |
            """.trimMargin()

}
