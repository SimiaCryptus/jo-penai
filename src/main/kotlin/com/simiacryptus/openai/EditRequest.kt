package com.simiacryptus.openai


class EditRequest {
    var model: String = ""
    var input: String? = null
    var instruction: String = ""

    @Suppress("unused")
    var temperature: Double? = null

    @Suppress("unused")
    var n: Int? = null
    var top_p: Double? = null

    @Suppress("unused")
    constructor()

    constructor(obj: EditRequest) {
        model = obj.model
        top_p = obj.top_p
        input = obj.input
        instruction = obj.instruction
        temperature = obj.temperature
        n = obj.n
    }

    fun setModel(model: String): EditRequest {
        this.model = model
        return this
    }

    fun setInput(input: String?): EditRequest {
        this.input = input
        return this
    }

    fun setInstruction(instruction: String): EditRequest {
        this.instruction = instruction
        return this
    }

    fun setTemperature(temperature: Double?): EditRequest {
        top_p = null
        this.temperature = temperature
        return this
    }

    fun setN(n: Int?): EditRequest {
        this.n = n
        return this
    }

    fun setTop_p(top_p: Double?): EditRequest {
        temperature = null
        this.top_p = top_p
        return this
    }

    override fun toString(): String {
        return "EditRequest{" + "model='" + model + '\'' +
                ", input='" + input + '\'' +
                ", instruction='" + instruction + '\'' +
                ", temperature=" + temperature +
                ", n=" + n +
                ", top_p=" + top_p +
                '}'
    }

}