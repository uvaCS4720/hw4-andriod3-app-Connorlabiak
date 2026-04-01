package edu.nd.pmcburne.hello.APIResponseObjects

import edu.nd.pmcburne.hello.APIResponseObjects.VisualCenter

data class APIResponseItem(
    val description: String,
    val id: Int,
    val name: String,
    val tag_list: List<String>,
    val visual_center: VisualCenter
)