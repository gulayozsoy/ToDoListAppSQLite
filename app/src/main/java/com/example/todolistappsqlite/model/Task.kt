package com.example.todolistappsqlite.model

import java.io.Serializable

//tabloya yeni veri eklerken task id eklemeyeceğiz. Tabloda auto imcremental idi.
//bu nedenle koymamız da gerektiğinden sembolik olarak 0 deriz.
data class Task(
    var id: Int = 0,
    var name: String,
    var date: String
): Serializable